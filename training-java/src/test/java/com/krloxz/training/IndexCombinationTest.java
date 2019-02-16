package com.krloxz.training;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import java.util.*;
import java.util.stream.*;

public class IndexCombinationTest {

    @Test
    public void shouldFindNCombinationsWhenChoosing1FromN() {
        assertThat(combine(3, 1).size(), is(3));
    }

    @Test
    public void shouldFindNCombinationsWhenChoosingRFromN() {
        assertThat(combine(100, 3).size(), is(161700));
    }

    @Test
    public void canCombineIndexesOfAnArray() {
        final List<int[]> combinations = combine(5, 3);
        assertThat(combinations.size(), is(10));
        
        // final Character[] array = {'A', 'B', 'C', 'D', 'E'};
        // combinations.stream()
        //     .forEach(e -> {
        //         IntStream.of(e)
        //             .forEach(i -> System.out.print(array[i] + ","));
        //         System.out.println();
        //     });
    }

    private List<int[]> combine(int n, int r) {
        return combine(n, r, 0, new int[r], 0);
    }

    private List<int[]> combine(int n, int r, int start, int[] combination, int combinationIndex) {
        if (r == 1) {
            return IntStream.range(start, n)
                .peek(e -> combination[combinationIndex] = e)
                .mapToObj(e -> Arrays.copyOf(combination, combination.length))
                .collect(Collectors.toList());
        }
        final List<int[]> combinations = new ArrayList<>();
        for (int i = start; i <= n - r; i++) {
            combination[combinationIndex] = i;
            combinations.addAll(combine(n, r - 1, i + 1, combination, combinationIndex + 1));
        }
        return combinations;
    }

}
