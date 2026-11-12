package com.example.scm.service;

import com.example.scm.common.exception.BusinessException;
import com.example.scm.common.exception.ErrorCode;
import com.example.scm.domain.Item;
import com.example.scm.domain.Partner;
import com.example.scm.repository.ItemRepository;
import com.example.scm.repository.PartnerRepository;
import java.util.List;
import com.example.scm.domain.PurchaseOrder;
import com.example.scm.domain.PurchaseOrderLine;
import com.example.scm.repository.PurchaseOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 발주서 한 장과 그 안의 품목 목록을 함께 저장합니다.
 * 번호가 겹쳐 저장이 실패하면 그 시도를 되돌리고, 다음 시도는 새 저장 묶음에서 시작할 수 있게 합니다.
 *
 * 발주 저장의 독립 트랜잭션 경계.
 *
 * <p>학습 모듈 10: 먼저 PurchaseOrderService의 saveWithOrderNumber를 읽고, 저장 재시도가
 * 왜 별도 Bean을 필요로 하는지 확인할 때 이 파일로 이동한다.</p>
 *
 * <p>발주번호 UNIQUE 충돌로 flush가 실패한 트랜잭션은 재사용할 수 없으므로,
 * 각 저장 시도를 별도 트랜잭션으로 격리한다. {@code REQUIRES_NEW}는 바깥 트랜잭션과
 * 분리된 새 트랜잭션을 시작하고, {@code saveAndFlush}는 INSERT를 즉시 보내 충돌 여부를
 * 이 메서드 안에서 확인하게 한다.</p>
 */
@Service
@RequiredArgsConstructor
public class PurchaseOrderPersistenceService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final PartnerRepository partnerRepository;
    private final ItemRepository itemRepository;

    // REQUIRES_NEW는 새 저장 묶음을 시작합니다. saveAndFlush는 장부에 바로 보내 번호 충돌을 확인합니다.
    // 다른 Service 객체에 맡겨야 Spring의 트랜잭션 장치가 새 묶음을 적용할 수 있습니다.
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public PurchaseOrder saveAndFlush(PurchaseOrder purchaseOrder) {
        lockAndRecheck(purchaseOrder.getPartnerId(),
                purchaseOrder.getLines().stream().map(PurchaseOrderLine::getItemId).toList());
        return purchaseOrderRepository.saveAndFlush(purchaseOrder);
    }

    // 주문을 만들기 전 검사와 실제 저장은 서로 다른 저장 묶음입니다. 그 사이에 거래처가 비활성화되거나
    // 품목이 단종되면 사용할 수 없는 거래처·품목으로 주문이 만들어질 수 있습니다.
    // 그래서 저장하는 묶음 안에서 거래처와 품목을 잠그고 상태를 한 번 더 확인합니다.
    // 품목은 입고·출고와 같은 ID 순서로 잠가 서로 반대 순서로 기다리는 일을 줄입니다.
    private void lockAndRecheck(Long partnerId, List<Long> lineItemIds) {
        Partner partner = partnerRepository.findByIdForShare(partnerId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PARTNER_NOT_FOUND));
        if (!partner.canSupply()) {
            throw new BusinessException(ErrorCode.PARTNER_TYPE_MISMATCH);
        }
        if (!partner.isActive()) {
            throw new BusinessException(ErrorCode.INVALID_STATUS, "비활성 상태의 거래처로는 발주할 수 없습니다.");
        }
        List<Long> itemIds = lineItemIds.stream().distinct().sorted().toList();
        List<Item> items = itemRepository.findAllByIdForShare(itemIds);
        if (items.size() != itemIds.size()) {
            throw new BusinessException(ErrorCode.ITEM_NOT_FOUND);
        }
        if (items.stream().anyMatch(item -> !item.isActive())) {
            throw new BusinessException(ErrorCode.ITEM_DISCONTINUED);
        }
    }
}
