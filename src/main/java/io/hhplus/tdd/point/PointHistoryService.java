package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PointHistoryService {
  private final PointHistoryTable pointHistoryTable;

  public void recordTransactionHistory(Long id, Long amount, TransactionType type) {
    pointHistoryTable.insert(id, amount, type, System.currentTimeMillis());
  };

  public List<PointHistory> getPointHistory(Long id) {
    return pointHistoryTable.selectAllByUserId(id);
  }
}
