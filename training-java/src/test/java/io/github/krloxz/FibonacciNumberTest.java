package io.github.krloxz;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.time.Duration;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ExceptionUtils;
import org.opentest4j.AssertionFailedError;

/**
 * Unit tests {@link FibonacciNumber}.
 *
 * @author Carlos Gomez
 */
class FibonacciNumberTest {

  @Nested
  class WhenNumberIsVerySmall {

    private static final int VERY_SMALL_NUMBER = 5;
    private static final int RESULT = 5;

    @Test
    void recursiveAlgorithmCalculatesValue() {
      assertThat(FibonacciNumber.of(VERY_SMALL_NUMBER).usingRecursion().value())
          .isEqualTo(RESULT);
    }

    @Test
    void memoizedAlgorithmCalculatesValue() {
      assertThat(FibonacciNumber.of(VERY_SMALL_NUMBER).usingMemoization().value())
          .isEqualTo(RESULT);
    }

    @Test
    void dynamicAlgorithmCalculatesValue() {
      assertThat(FibonacciNumber.of(VERY_SMALL_NUMBER).usingDynamic().value())
          .isEqualTo(RESULT);
    }

  }

  @Nested
  class WhenNumberIsSmall {

    private static final int SMALL_NUMBER = 45;
    private static final int RESULT = 1_134_903_170;

    @Test
    void recursiveAlgorithmIsSlow() {
      assertMinDuration(
          Duration.ofSeconds(3),
          () -> FibonacciNumber.of(SMALL_NUMBER).usingRecursion().value());
    }

    @Test
    void memoizedAlgorithmCalculatesValue() {
      assertThat(FibonacciNumber.of(SMALL_NUMBER).usingMemoization().value())
          .isEqualTo(RESULT);
    }

    @Test
    void dynamicAlgorithmCalculatesValue() {
      assertThat(FibonacciNumber.of(SMALL_NUMBER).usingDynamic().value())
          .isEqualTo(RESULT);
    }

  }

  @Nested
  class WhenNumberIsBig {

    private static final int BIG_NUMBER = 20_000;

    @Test
    void recursiveAlgorithmThrowsStackOverflow() {
      assertThatExceptionOfType(StackOverflowError.class)
          .isThrownBy(() -> FibonacciNumber.of(WhenNumberIsBig.BIG_NUMBER).usingRecursion().value());
    }

    @Test
    void memoizedAlgorithmThrowsStackOverflow() {
      assertThatExceptionOfType(StackOverflowError.class)
          .isThrownBy(() -> FibonacciNumber.of(WhenNumberIsBig.BIG_NUMBER).usingMemoization().value());
    }

    @Test
    void dynamicAlgorithmCalculatesValue() {
      assertThat(FibonacciNumber.of(WhenNumberIsBig.BIG_NUMBER).usingDynamic().value().toString())
          .hasSize(4_180);
    }

  }

  private static void assertMinDuration(final Duration timeout, final Runnable task) {
    final var executorService = Executors.newSingleThreadExecutor();
    try {
      final Future<?> future = executorService.submit(task);
      try {
        future.get(timeout.toMillis(), TimeUnit.MILLISECONDS);
        throw new AssertionFailedError("Duration was shorter than " + timeout);
      } catch (final TimeoutException e) {
        // ignore
      } catch (final ExecutionException ex) {
        throw ExceptionUtils.throwAsUncheckedException(ex.getCause());
      } catch (final Throwable ex) {
        throw ExceptionUtils.throwAsUncheckedException(ex);
      }
    } finally {
      executorService.shutdownNow();
    }
  }

}
