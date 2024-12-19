package io.hhplus.tdd.point;

import org.springframework.stereotype.Component;

@Component
public class PointValidator {
  public static final Long MAX_BALANCE = 1_000_000L;

  public void validateChargePoint(UserPoint userPoint, long amount){
    if(userPoint.point() + amount > MAX_BALANCE){
      throw new RuntimeException("최대 잔고 초과");
    }
  }

  public void validateUsePoint(UserPoint userPoint, long amount) {
    if(userPoint.point() < amount){
      throw new RuntimeException("사용 한도 초과");
    }
  }

  public void validateAmount(Long amount) {
    if (amount == null) {
      throw new IllegalArgumentException("Point value cannot be null.");
    }
    if (amount < 0) {
      throw new IllegalArgumentException("Point value cannot be negative.");
    }
  }
}
