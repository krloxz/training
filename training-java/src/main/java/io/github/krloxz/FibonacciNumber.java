package io.github.krloxz;

import java.math.BigInteger;

/**
 * A Fibonacci number.
 *
 * @author Carlos Gomez
 */
public abstract class FibonacciNumber {

  /**
   * @return the numeric value of this Fibonacci number
   */
  public abstract BigInteger value();

  /**
   * @return the sequential place of this Fibonacci number
   */
  abstract int n();

  /**
   * Creates the 'nth' Fibonacci number.
   *
   * @param n
   *        sequential place of the Fibonacci number to create
   * @return the 'nth' Fibonacci number
   */
  public static FibonacciNumber of(final int n) {
    return new DynamicFibonacciNumber(n);
  }

  /**
   * @return this Fibonacci number, but it's {@link #value()} will be calculated using a recursive
   *         algorithm, which is slow and is limited by the stack memory to calculate values for large
   *         sequences
   */
  public FibonacciNumber usingRecursion() {
    return new RecursiveFibonacciNumber(n());
  }

  /**
   * @return this Fibonacci number, but it's {@link #value()} will be calculated using a memoized
   *         algorithm, which is limited by the stack memory to calculate values for large sequences
   */
  public FibonacciNumber usingMemoization() {
    return new MemoizedFibonacciNumber(n());
  }

  /**
   * @return this Fibonacci number, but it's {@link #value()} will be calculated using a dynamic
   *         programming algorithm, which is fast and isn't limited by the stack memory to calculate
   *         values for large sequences
   */
  public FibonacciNumber usingDynamic() {
    return new DynamicFibonacciNumber(n());
  }

  private final static class RecursiveFibonacciNumber extends FibonacciNumber {

    private final int n;

    RecursiveFibonacciNumber(final int n) {
      this.n = n;
    }

    @Override
    public BigInteger value() {
      return calculate(this.n);
    }

    @Override
    int n() {
      return this.n;
    }

    private BigInteger calculate(final int n) {
      if (n == 0) {
        return BigInteger.ZERO;
      }
      if (n == 1) {
        return BigInteger.ONE;
      }
      return calculate(n - 1).add(calculate(n - 2));
    }

  }

  private final static class MemoizedFibonacciNumber extends FibonacciNumber {

    private final int n;

    MemoizedFibonacciNumber(final int n) {
      this.n = n;
    }

    @Override
    public BigInteger value() {
      return calculate(this.n, new BigInteger[this.n + 1]);
    }

    @Override
    int n() {
      return this.n;
    }

    private BigInteger calculate(final int n, final BigInteger[] memo) {
      if (memo[n] != null) {
        return memo[n];
      }
      if (n == 0) {
        return BigInteger.ZERO;
      }
      if (n == 1) {
        return BigInteger.ONE;
      }
      memo[n] = calculate(n - 1, memo).add(calculate(n - 2, memo));
      return memo[n];
    }

  }

  private final static class DynamicFibonacciNumber extends FibonacciNumber {

    private final int n;

    DynamicFibonacciNumber(final int n) {
      this.n = n;
    }

    @Override
    public BigInteger value() {
      return calculate(this.n);
    }

    @Override
    int n() {
      return this.n;
    }

    private BigInteger calculate(final int n) {
      final var fibonaccis = new BigInteger[2];
      fibonaccis[0] = BigInteger.ZERO;
      fibonaccis[1] = BigInteger.ONE;
      for (var i = 2; i <= n; i++) {
        final var newFibonacci = fibonaccis[1].add(fibonaccis[0]);
        fibonaccis[0] = fibonaccis[1];
        fibonaccis[1] = newFibonacci;
      }
      return fibonaccis[1];
    }

  }

}
