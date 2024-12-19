package io.hhplus.tdd.point;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PointFacade {
  private final PointHistoryService pointHistoryService;
  private final UserPointService userPointService;

  public UserPoint charge(Long id, Long amount) throws Exception {
    UserPoint userPoint = userPointService.chargePointById(id, amount);
    pointHistoryService.recordTransactionHistory(id, amount, TransactionType.CHARGE);
    return userPoint;
  }

  public UserPoint use(Long id, Long amount) throws Exception {
    UserPoint userPoint = userPointService.usePointById(id, amount);
    pointHistoryService.recordTransactionHistory(id, amount, TransactionType.USE);
    return userPoint;
  }
}
