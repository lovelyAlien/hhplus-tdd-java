package io.hhplus.tdd.point;

import io.hhplus.tdd.database.UserPointTable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserPointService {
  private final UserPointTable userPointTable;
  public void chargePointById(Long id, Long chargeAmount) {};
  public void usePointById(Long id, Long point) {};
}
