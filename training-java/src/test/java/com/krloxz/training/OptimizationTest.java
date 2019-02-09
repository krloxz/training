package com.krloxz.training;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import java.util.*;
import java.util.stream.*;

public class OptimizationTest {

    @Test
    public void test() {
        // Integer[0] = ID, Integer[1] = cost
        final List<Integer[]> pairs1 = Arrays.<Integer[]>asList(
            new Integer[] { 1, 3 },
            new Integer[] { 2, 5 },
            new Integer[] { 3, 6 },
            new Integer[] { 4, 4 }
        );
        final List<Integer[]> pairs2 = Arrays.<Integer[]>asList(
            new Integer[] { 1, 5 },
            new Integer[] { 3, 8 }
        );
        final int optimalCost = 10;

        final List<Integer[]> optimalCombination = findOptimalCombination(pairs1, pairs2, optimalCost);
        optimalCombination.stream()
            .forEach(e -> System.out.println(Arrays.toString(e)));
        assertThat(optimalCombination,
            contains(arrayContaining(2, 1)));
    }

    @Test
    public void test2() {
        // Integer[0] = ID, Integer[1] = cost
        final List<Integer[]> pairs1 = Arrays.<Integer[]>asList(
            new Integer[] { 11, 3 },
            new Integer[] { 12, 1 },
            new Integer[] { 13, 5 },
            new Integer[] { 14, 2 },
            new Integer[] { 15, 4 }
        );
        final List<Integer[]> pairs2 = Arrays.<Integer[]>asList(
            new Integer[] { 21, 2 },
            new Integer[] { 22, 1 }
        );
        final int optimalCost = 10;

        final List<Integer[]> optimalCombination = findOptimalCombination(pairs1, pairs2, optimalCost);
        optimalCombination.stream()
            .forEach(e -> System.out.println(Arrays.toString(e)));
        assertThat(optimalCombination,
            contains(arrayContaining(13, 21), arrayContaining(14, 22)));
    }

    @Test
    @Disabled("Won't find the proper combination; is pair repetition allowed?")
    public void test3() {
        // Integer[0] = ID, Integer[1] = cost
        final List<Integer[]> pairs1 = Arrays.<Integer[]>asList(
            new Integer[] { 11, 4 },
            new Integer[] { 12, 1 },
            new Integer[] { 13, 1 },
            new Integer[] { 14, 4 },
            new Integer[] { 15, 1 },
            new Integer[] { 16, 1 }
        );
        final List<Integer[]> pairs2 = Arrays.<Integer[]>asList(
            new Integer[] { 21, 1 },
            new Integer[] { 22, 11 }
        );
        final int optimalCost = 10;

        final List<Integer[]> optimalCombination = findOptimalCombination(pairs1, pairs2, optimalCost);
        optimalCombination.stream()
            .forEach(e -> System.out.println(Arrays.toString(e)));
        assertThat(optimalCombination,
            contains(arrayContaining(11, 21), arrayContaining(14, 21)));
    }

    private List<Integer[]> findOptimalCombination(List<Integer[]> pairs1, List<Integer[]> pairs2, int optimalCost) {
        final List<Integer> costs =  pairs1.stream()
            .flatMap(p1 -> pairs2.stream().map(p2 -> p1[1] + p2[1]))
            // .peek(c -> System.out.println(c))
            // .sorted()
            .collect(Collectors.toList());

        final Deque<Integer> optimalCostSequences = IntStream.range(0, costs.size())
            .mapToObj(e -> findOptimalCostSequence(costs, e, optimalCost))
            .filter(e -> e.getLast() > 0)
            // .peek(e -> System.out.println(e))
            .max((e1, e2) -> e1.getLast().compareTo(e2.getLast()))
            .get();
        optimalCostSequences.removeLast();

        return optimalCostSequences.stream()
            .map(e -> {
                return new Integer[] {
                    pairs1.get(e / pairs2.size())[0],
                    pairs2.get(e % pairs2.size())[0]
                };
            })
            // .peek(e -> System.out.println("final: " + Arrays.toString(e)))
            .collect(Collectors.toList());
    }

    private Deque<Integer> findOptimalCostSequence(final List<Integer> costs, final int start, int optimalCost) {
        final Deque<Integer> bestCosts = new ArrayDeque<>();
        int totalCost = 0;
        for (int i = start; i < costs.size() + start; i++) {
            final int costsIndex = i % costs.size();
            if (totalCost + costs.get(costsIndex) <= optimalCost) {
                bestCosts.add(costsIndex);
                totalCost += costs.get(costsIndex);
            }
        }
        bestCosts.add(totalCost);
        return bestCosts;
    }

}
