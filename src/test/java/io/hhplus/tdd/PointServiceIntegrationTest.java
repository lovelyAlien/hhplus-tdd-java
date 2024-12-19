package io.hhplus.tdd;

import io.hhplus.tdd.point.PointHistoryService;
import io.hhplus.tdd.point.UserPoint;
import io.hhplus.tdd.point.UserPointService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class PointServiceIntegrationTest {

  @Autowired
  private UserPointService userPointService;

  @Autowired
  private PointHistoryService pointHistoryService;

  @BeforeEach
  void beforeEach() {
    long userId = 1L;
    userPointService.chargePointById(userId, 500_000L);
  }

  @Test
  void 동시_포인트_충전() throws InterruptedException {
    // given
    long userId = 1L;
    int threadCount = 10;
    long chargeAmount  = 100L;
    long currentBalance = userPointService.getUserPoint(userId).point();
    ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
    CountDownLatch latch = new CountDownLatch(threadCount);

    for (int i = 0; i < threadCount; i++) {
      executorService.execute(() -> {
        UserPoint userPoint = userPointService.chargePointById(userId, chargeAmount);
        latch.countDown();
      });
    }

    latch.await();
    executorService.shutdown();

    // then
    UserPoint userPoint = userPointService.getUserPoint(userId);
    assertEquals( currentBalance + threadCount * chargeAmount, userPoint.point());
  }

  @Test
  public void 동시_포인트_사용() throws InterruptedException {
    long userId = 1L;
    int threadCount = 5;
    long useAmount = 100_000L;
    long currentBalance = userPointService.getUserPoint(userId).point();
    System.out.println(currentBalance);
    ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

    CountDownLatch latch = new CountDownLatch(threadCount);

    for (int i = 0; i < threadCount; i++) {
      executorService.execute(() -> {
        UserPoint userPoint = userPointService.usePointById(userId, useAmount);
        latch.countDown();
      });
    }

    latch.await();
    executorService.shutdown();

    // then
    UserPoint userPoint = userPointService.getUserPoint(userId);
    assertEquals(currentBalance - threadCount * useAmount, userPoint.point());
  }

  @Test
  public void 동시_포인트_충전_사용() throws InterruptedException {
    long userId = 1L;
    int chargeCount = 5;
    int useCount = 4;
    long chargeAmount = 100_000;
    long useAmount = 100_000;
    long currentBalance = userPointService.getUserPoint(userId).point();

    ExecutorService executorService = Executors.newFixedThreadPool(chargeCount + useCount);
    CountDownLatch latch = new CountDownLatch(chargeCount + useCount);
    for(int i = 0; i<chargeCount; i++) {
      executorService.execute(() -> {
        UserPoint userPoint = userPointService.chargePointById(userId, chargeAmount);
        latch.countDown();
      });
    }

    for(int i = 0; i<useCount; i++) {
      executorService.execute(() -> {
        UserPoint userPoint = userPointService.usePointById(userId, useAmount);
        latch.countDown();
      });
    }

    latch.await();
    executorService.shutdown();

    // then
    UserPoint userPoint = userPointService.getUserPoint(userId);
    assertEquals(currentBalance + chargeAmount * chargeCount - useAmount * useCount, userPoint.point());

  }
}
