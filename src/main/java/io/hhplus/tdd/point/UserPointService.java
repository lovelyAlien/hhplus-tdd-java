package io.hhplus.tdd.point;

import io.hhplus.tdd.database.UserPointTable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@Service
@RequiredArgsConstructor
public class UserPointService {
  private final UserPointTable userPointTable;
  private final PointValidator pointValidator;
  private final Map<Long, ReentrantLock> locks = new ConcurrentHashMap<>();

  private ReentrantLock getLock(Long id) {
    return locks.computeIfAbsent(id, k -> new ReentrantLock());
  }

  public UserPoint chargePointById(Long id, Long chargeAmount) throws Exception {
    ReentrantLock lock = getLock(id);
    lock.lock();
    try {
      pointValidator.validateAmount(chargeAmount);
      UserPoint userPoint = userPointTable.selectById(id);
      Long currentBalance = userPoint.point();
      pointValidator.validateChargePoint(userPoint, chargeAmount);
      userPointTable.insertOrUpdate(id, currentBalance + chargeAmount);
      return userPoint;
    } finally {
      lock.unlock();
    }
  };
  public UserPoint usePointById(Long id, Long userAmount) throws Exception {
    ReentrantLock lock = getLock(id);
    lock.lock();
    try {
      pointValidator.validateAmount(userAmount);
      UserPoint userPoint = userPointTable.selectById(id);
      Long currentBalance = userPoint.point();
      pointValidator.validateUsePoint(userPoint, userAmount);
      userPointTable.insertOrUpdate(id, currentBalance - userAmount);
      return userPoint;
    } finally {
      lock.unlock();
    }
  };

  public UserPoint getUserPoint(Long id) {
    return userPointTable.selectById(id);
  }
}
