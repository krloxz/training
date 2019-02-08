package com.krloxz.training;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import java.util.*;
import java.util.stream.*;

public class CombinationTest {

	@Test
	public void shouldFindNCombinationsWhenChoosing1FromN() {
		final Character[] array = {'A', 'B', 'C'};
		final List<Object[]> combinations = combine(array, 1);
		assertThat(combinations.size(), is(3));
		
		System.out.println("Result:");
		combinations.stream()
			.map(a -> Arrays.toString(a))
			.forEach(e -> System.out.println(e));
	}

	@Test
	public void shouldFindNCombinationsWhenChoosingRFromN() {
		final Character[] array = {'A', 'B', 'C', 'D', 'E'};
		final List<Object[]> combinations = combine(array, 3);
		assertThat(combinations.size(), is(10));
		
		System.out.println("Result:");
		combinations.stream()
			.map(a -> Arrays.toString(a))
			.forEach(e -> System.out.println(e));
	}

	private List<Object[]> combine(Object[] array, int size) {
		return combineSubArray(array, 0, new Object[size], 0, size);
	}

	private List<Object[]> combineSubArray(Object[] array, int arrayIndex, Object[] combination, int combinationIndex, int combinationSize) {
		if (combinationSize == 1) {
			return Arrays.stream(array, arrayIndex, array.length)
				.peek(e -> combination[combinationIndex] = e)
				.map(e -> Arrays.copyOf(combination, combination.length))
				.collect(Collectors.toList());
		}
		final List<Object[]> combinations = new ArrayList<>();
		for (int i = arrayIndex; i <= array.length - combinationSize; i++) {
			combination[combinationIndex] = array[i];
			combinations.addAll(combineSubArray(array, i + 1, combination, combinationIndex + 1, combinationSize - 1));
		}
		return combinations;
		// return Arrays.stream(array, arrayIndex, array.length - combinationSize)
		// 	.peek(e -> combination[combinationIndex] = e)
		// 	.peek(e -> System.out.println("Combination1: " + Arrays.toString(combination)))
		// 	.map(e -> y(array, arrayIndex + 1, combination, combinationIndex + 1, combinationSize - 1))
		// 	.reduce(new ArrayList<>(), (l1, l2) -> {
		// 		l1.addAll(l2);
		// 		return l1;
		// 	});
	}	
	
}