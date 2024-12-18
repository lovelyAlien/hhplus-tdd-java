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
}
