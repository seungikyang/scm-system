package com.example.scm.service;

import com.example.scm.common.exception.BusinessException;
import com.example.scm.common.exception.ErrorCode;
import com.example.scm.domain.Item;
import com.example.scm.domain.Partner;
import com.example.scm.repository.ItemRepository;
import com.example.scm.repository.PartnerRepository;
import java.util.List;
import com.example.scm.domain.SalesOrder;
import com.example.scm.domain.SalesOrderLine;
import com.example.scm.repository.SalesOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 수주서 한 장과 그 안의 품목 목록을 함께 저장합니다.
 * 번호 중복으로 실패한 시도와 다음 저장 시도를 서로 다른 묶음으로 나누는 역할입니다.
 *
 * 채번 충돌 시 실패한 트랜잭션을 분리한다.
 */
@Service
@RequiredArgsConstructor
public class SalesOrderPersistenceService {

    private final SalesOrderRepository salesOrderRepository;
    private final PartnerRepository partnerRepository;
    private final ItemRepository itemRepository;

    // REQUIRES_NEW는 새 저장 묶음을 시작합니다. saveAndFlush는 장부에 바로 보내 번호 충돌을 확인합니다.
    // 번호 충돌로 실패한 묶음은 끝내고, 다음 저장 시도에서 새로운 묶음을 사용합니다.
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public SalesOrder saveAndFlush(SalesOrder salesOrder) {
        lockAndRecheck(salesOrder.getPartnerId(),
                salesOrder.getLines().stream().map(SalesOrderLine::getItemId).toList());
        return salesOrderRepository.saveAndFlush(salesOrder);
    }

    // 주문을 만들기 전 검사와 실제 저장은 서로 다른 저장 묶음입니다. 그 사이에 거래처가 비활성화되거나
    // 품목이 단종되면 사용할 수 없는 거래처·품목으로 주문이 만들어질 수 있습니다.
    // 그래서 저장하는 묶음 안에서 거래처와 품목을 잠그고 상태를 한 번 더 확인합니다.
    // 품목은 입고·출고와 같은 ID 순서로 잠가 서로 반대 순서로 기다리는 일을 줄입니다.
    private void lockAndRecheck(Long partnerId, List<Long> lineItemIds) {
        // "ForShare"는 읽기용 공유 잠금입니다. 수정용 잠금과 달리 여러 주문이 같은 거래처를 동시에 잠가도 서로 막지 않아
        // 주문 저장이 줄 서서 느려지지 않습니다. 대신 이 묶음이 끝날 때까지 누군가 거래처를 비활성화하는 수정은 기다리게 합니다.
        Partner partner = partnerRepository.findByIdForShare(partnerId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PARTNER_NOT_FOUND));
        if (!partner.canBuy()) {
            throw new BusinessException(ErrorCode.PARTNER_TYPE_MISMATCH);
        }
        if (!partner.isActive()) {
            throw new BusinessException(ErrorCode.INVALID_STATUS, "비활성 상태의 거래처로는 수주할 수 없습니다.");
        }
        // 같은 품목이 두 줄에 나와도 한 번만 잠그도록 distinct로 중복을 빼고, sorted로 번호 순서를 맞춥니다.
        List<Long> itemIds = lineItemIds.stream().distinct().sorted().toList();
        List<Item> items = itemRepository.findAllByIdForShare(itemIds);
        // 요청한 품목 수보다 찾은 품목 수가 적으면, 그 사이 삭제된 품목이 섞여 있다는 뜻입니다.
        if (items.size() != itemIds.size()) {
            throw new BusinessException(ErrorCode.ITEM_NOT_FOUND);
        }
        if (items.stream().anyMatch(item -> !item.isActive())) {
            throw new BusinessException(ErrorCode.ITEM_DISCONTINUED);
        }
    }
}
