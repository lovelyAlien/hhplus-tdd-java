package io.hhplus.tdd.point;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class PointValidator {
  public static final Long MAX_BALANCE = 1_000_000L;

  public void validateChargePoint(UserPoint userPoint, long amount){
    if(userPoint.point() + amount > MAX_BALANCE){
      throw new CustomException(ErrorCode.EXCEEDED_MAXIMUM_POINT_BALANCE.getStatus(), ErrorCode.EXCEEDED_MAXIMUM_POINT_BALANCE.getMessage());
    }
  }

  public void validateUsePoint(UserPoint userPoint, long amount) {
    if(userPoint.point() < amount){
      throw new CustomException(ErrorCode.INSUFFICIENT_POINT_USAGE_FAILED.getStatus(), ErrorCode.INSUFFICIENT_POINT_USAGE_FAILED.getMessage());
    }
  }

  public void validateAmount(Long amount) {
    if (amount == null) {
      throw new CustomException(ErrorCode.INVALID_POINT_OPERATION.getStatus(), ErrorCode.INVALID_POINT_OPERATION.getMessage());
    }
    if (amount < 0) {
      throw new CustomException(ErrorCode.INVALID_POINT_OPERATION.getStatus(), ErrorCode.INVALID_POINT_OPERATION.getMessage());
    }
  }
}
