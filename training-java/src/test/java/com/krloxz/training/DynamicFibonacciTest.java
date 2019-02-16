package com.krloxz.training;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import java.util.*;
import java.math.*;

class DynamicFibonacciTest {

	@Test
	void shouldCalculatefibonacciWhenNumberIsShort() {
		assertThat(fibonacci(0), is(BigInteger.ZERO));
		assertThat(fibonacci(1), is(BigInteger.ONE));
		assertThat(fibonacci(15), is(new BigInteger("610")));
	}

	@Test
	void shouldCalculatefibonacciWhenNumberIsLong() {
		assertThat(fibonacci(20000).toString().length(), is(4180));
	}

	@Test
	@Disabled("Takes several seconds to complete")
	void shouldCalculatefibonacciWhenNumberIsTooLong() {
		assertThat(fibonacci(1000000).toString().length(), is(208988));
	}

	private BigInteger fibonacci(final int n) {
		if (n == 0) {
			return BigInteger.ZERO;
		}
		BigInteger fibonacci2 = BigInteger.ZERO;
		BigInteger fibonacci1 = BigInteger.ONE;
		for (int i = 2; i <= n; i++) {
			final BigInteger temp = fibonacci1;
			fibonacci1 = temp.add(fibonacci2);
			fibonacci2 = temp;
		}
		return fibonacci1;
	}

}
