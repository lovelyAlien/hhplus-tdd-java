package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointHistoryService {
  private final PointHistoryTable pointHistoryTable;
  public void addPointTransaction(Long id, Long point) {};
}
