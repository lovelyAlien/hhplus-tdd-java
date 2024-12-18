package io.hhplus.tdd.point;

import io.hhplus.tdd.database.UserPointTable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserPointService {
  private final UserPointTable userPointTable;
  public void chargePointById(Long id, Long chargeAmount) throws Exception {
    isPointValid(chargeAmount);
    UserPoint userPoint = userPointTable.selectById(id);
    Long currentBalance = userPoint.point();
    if(currentBalance + chargeAmount > 1_000_000L) {
      throw new Exception("최대 잔고 초과");
    }
    userPointTable.insertOrUpdate(id, currentBalance + chargeAmount);
  };
  public void usePointById(Long id, Long userAmount) throws Exception {
    isPointValid(userAmount);
    UserPoint userPoint = userPointTable.selectById(id);
    Long currentBalance = userPoint.point();
    if(userAmount > currentBalance) {
      throw new Exception("한도 초과");
    }
    userPointTable.insertOrUpdate(id, currentBalance - userAmount);
  };

  public void isPointValid(Long amount) {
    if (amount == null) {
      throw new IllegalArgumentException("Point value cannot be null.");
    }
    if (amount < 0) {
      throw new IllegalArgumentException("Point value cannot be negative.");
    }
  }
}
