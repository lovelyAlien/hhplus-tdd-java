package io.hhplus.tdd;

import io.hhplus.tdd.point.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class PointServiceIntegrationTest {

  @Autowired
  private PointFacade pointFacade;

  @Autowired
  private UserPointService userPointService;

  @Autowired
  private PointHistoryService pointHistoryService;

  @BeforeEach
  void beforeEach() {
    long userId = 1L;
    pointFacade.charge(userId, 500_000L);
  }

  @Test
  @DisplayName("여러 스레드에서 동시에 포인트 충전")
  void 동시_포인트_충전() throws InterruptedException {
    // given
    long userId = 1L;
    int threadCount = 10;
    long chargeAmount  = 100L;
    long currentBalance = userPointService.getUserPoint(userId).point();
    int currentHistoryCount = pointHistoryService.getPointHistory(userId).size();

    ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
    CountDownLatch latch = new CountDownLatch(threadCount);

    for (int i = 0; i < threadCount; i++) {
      executorService.execute(() -> {
        UserPoint userPoint = pointFacade.charge(userId, chargeAmount);
        latch.countDown();
      });
    }

    latch.await();
    executorService.shutdown();

    // then
    UserPoint userPoint = userPointService.getUserPoint(userId);
    List<PointHistory> pointHistories = pointHistoryService.getPointHistory(userId);
    assertEquals( currentBalance + threadCount * chargeAmount, userPoint.point());
    assertEquals(currentHistoryCount + threadCount, pointHistories.size());
  }

  @Test
  @DisplayName("여러 스레드에서 동시에 포인트 사용")
  public void 동시_포인트_사용() throws InterruptedException {
    // given
    long userId = 1L;
    int threadCount = 5;
    long useAmount = 100_000L;
    long currentBalance = userPointService.getUserPoint(userId).point();
    int currentHistoryCount = pointHistoryService.getPointHistory(userId).size();
    System.out.println(currentBalance);
    ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

    CountDownLatch latch = new CountDownLatch(threadCount);

    for (int i = 0; i < threadCount; i++) {
      executorService.execute(() -> {
        UserPoint userPoint = pointFacade.use(userId, useAmount);
        latch.countDown();
      });
    }

    latch.await();
    executorService.shutdown();

    // then
    UserPoint userPoint = userPointService.getUserPoint(userId);
    List<PointHistory> pointHistories = pointHistoryService.getPointHistory(userId);
    assertEquals(currentBalance - threadCount * useAmount, userPoint.point());
    assertEquals(currentHistoryCount + threadCount, pointHistories.size());
  }

  @Test
  @DisplayName("여러 스레드에서 동시에 포인트 충전과 사용")
  public void 동시_포인트_충전_사용() throws InterruptedException {
    // given
    long userId = 1L;
    int chargeCount = 5;
    int useCount = 4;
    long chargeAmount = 100_000;
    long useAmount = 100_000;
    long currentBalance = userPointService.getUserPoint(userId).point();
    int currentHistoryCount = pointHistoryService.getPointHistory(userId).size();

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
    List<PointHistory> pointHistories = pointHistoryService.getPointHistory(userId);
    assertEquals(currentBalance + chargeAmount * chargeCount - useAmount * useCount, userPoint.point());
    assertEquals(currentHistoryCount + chargeCount + useCount, pointHistories.size());

  }
}
