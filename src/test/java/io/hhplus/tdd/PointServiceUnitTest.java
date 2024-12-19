package io.hhplus.tdd;

import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.PointValidator;
import io.hhplus.tdd.point.UserPointService;
import io.hhplus.tdd.point.UserPoint;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PointServiceUnitTest {

  @Mock
  private UserPointTable userPointTable;

  @Spy  // Mock 대신 Spy 사용
  private PointValidator pointValidator = new PointValidator();

  @InjectMocks
  private UserPointService userPointService;


  @Test
  void 포인트_충전_최대_잔고_초과() {
    // given
    Long userId = 1L;
    Long currentBalance = 900_000L;
    Long chargeAmount = 200_000L;
    // when
    when(userPointTable.selectById(userId))
      .thenReturn(new UserPoint(userId, currentBalance, System.currentTimeMillis()));
    // then
    assertThrows(RuntimeException.class, () -> {
      userPointService.chargePointById(userId, chargeAmount);
    });
  }

  @Test
  void 포인트_사용_잔고_부족() {
    // given
    Long userId = 1L;
    Long currentBalance = 100_000L;
    Long useAmount = 200_000L;
    // then
    when(userPointTable.selectById(userId))
      .thenReturn(new UserPoint(userId, currentBalance, System.currentTimeMillis()));
    // when
    assertThrows(RuntimeException.class, () -> {
      userPointService.usePointById(userId, useAmount);
    });
  }
}
