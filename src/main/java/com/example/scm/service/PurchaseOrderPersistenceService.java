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
 * <p>발주번호 UNIQUE 충돌로 flush가 실패한 트랜잭션은 재사용할 수 없으므로,
 * 각 저장 시도를 별도 트랜잭션으로 격리한다.</p>
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
