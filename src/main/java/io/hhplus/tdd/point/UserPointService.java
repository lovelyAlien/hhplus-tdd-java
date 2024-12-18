package io.hhplus.tdd.point;

import io.hhplus.tdd.database.UserPointTable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserPointService {
  private final UserPointTable userPointTable;
  public void chargePointById(Long id, Long chargeAmount) throws Exception {
    UserPoint userPoint = userPointTable.selectById(id);
    Long currentBalance = userPoint.point();
    if(currentBalance + chargeAmount > 1_000_000L) {
      throw new Exception("최대 잔고 초과");
    }
  };
  public void usePointById(Long id, Long point) {};
}
