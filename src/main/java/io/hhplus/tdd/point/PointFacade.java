package io.hhplus.tdd.point;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PointFacade {
  private final PointHistoryService pointHistoryService;
  private final UserPointService userPointService;

  public void charge(Long id,Long amount) throws Exception {
    userPointService.chargePointById(id, amount);
    pointHistoryService.recordTransactionHistory(id, amount, TransactionType.CHARGE);
  }

  public void use(Long id, Long amount) throws Exception {
    userPointService.usePointById(id, amount);
    pointHistoryService.recordTransactionHistory(id, amount, TransactionType.USE);
  }
}
