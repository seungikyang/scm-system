package com.example.scm.service;

import com.example.scm.domain.PurchaseOrder;
import com.example.scm.repository.PurchaseOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
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

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public PurchaseOrder saveAndFlush(PurchaseOrder purchaseOrder) {
        return purchaseOrderRepository.saveAndFlush(purchaseOrder);
    }
}
