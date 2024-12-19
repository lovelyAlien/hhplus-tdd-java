package io.hhplus.tdd.point;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

  INVALID_POINT_OPERATION(HttpStatus.BAD_REQUEST, "유효하지 않은 포인트 작업입니다. 요청을 다시 확인해주세요."),
  EXCEEDED_MAXIMUM_POINT_BALANCE(HttpStatus.BAD_REQUEST, "최대 충전 가능한 포인트 한도를 초과하였습니다."),
  INSUFFICIENT_POINT_USAGE_FAILED(HttpStatus.BAD_REQUEST, "잔여 포인트가 부족하여 사용이 불가능합니다.");

  private final HttpStatus status;
  private final String message;

  }
