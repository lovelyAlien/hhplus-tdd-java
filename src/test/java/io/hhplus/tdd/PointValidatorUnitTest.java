package io.hhplus.tdd;

import io.hhplus.tdd.point.PointValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class PointValidatorUnitTest {

  @InjectMocks
  private PointValidator pointValidator;

  @Test
  void 사용_또는_충전_포인트_NULL() {
    // given
    Long amount = null;
    assertThrows(IllegalArgumentException.class, () -> {
      pointValidator.validateAmount(amount);
    });
  }

  @Test
  void 사용_또는_충전_포인트_음수() {
    // given
    Long amount = -100_000L;
    assertThrows(IllegalArgumentException.class, () -> {
      pointValidator.validateAmount(amount);
    });
  }
}
