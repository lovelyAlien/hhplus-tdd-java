package io.hhplus.tdd;

import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.UserPointService;
import io.hhplus.tdd.point.UserPoint;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PointServiceTest {

  @Mock
  private UserPointTable userPointTable;

  @InjectMocks
  private UserPointService userPointService;


  @Test
  void 포인트_충전_최대_잔고_초과() {
    //given
    Long userId = 1L;
    Long currentBalance = 900_000L;
    Long chargeAmount = 200_000L;
    UserPoint existingPoint = new UserPoint(userId, currentBalance, System.currentTimeMillis());
    //when
    when(userPointTable.selectById(userId))
      .thenReturn(new UserPoint(userId, currentBalance, System.currentTimeMillis()));
    //then
    assertThrows(Exception.class, () -> {
      userPointService.chargePointById(userId, chargeAmount);
    });
  }

  @Test
  void 포인트_한도_초과() {
    // given
    Long userId = 1L;
    Long currentBalance = 100_000L;
    Long useAmount = 200_000L;
    // then
    when(userPointTable.selectById(userId))
      .thenReturn(new UserPoint(userId, currentBalance, System.currentTimeMillis()));
    // when
    assertThrows(Exception.class, () -> {
      userPointService.usePointById(userId, useAmount);
    });
  }

  @Test
  void 사용_또는_충전_포인트_NULL() {
    // given
    Long amount = null;
    assertThrows(IllegalArgumentException.class, () -> {
      userPointService.isPointValid(amount);
    });
  }

  @Test
  void 사용_또는_충전_포인트_음수() {
    // given
    Long amount = -100_000L;
    assertThrows(IllegalArgumentException.class, () -> {
      userPointService.isPointValid(amount);
    });
  }
}
