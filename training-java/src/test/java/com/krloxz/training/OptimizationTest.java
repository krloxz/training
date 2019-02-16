package com.krloxz.training;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import java.util.*;
import java.util.stream.*;

public class OptimizationTest {

    @Test
    // @Disabled
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

        assertThat(findOptimalCombination(pairs1, pairs2, optimalCost),
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

        assertThat(findOptimalCombination(pairs1, pairs2, optimalCost),
            contains(arrayContaining(11, 21), arrayContaining(12, 21), arrayContaining(12, 22)));
    }

    @Test
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

        assertThat(findOptimalCombination(pairs1, pairs2, optimalCost),
            contains(arrayContaining(11, 21), arrayContaining(14, 21)));
    }

    private List<Integer[]> findOptimalCombination(final List<Integer[]> pairs1, final List<Integer[]> pairs2, final int optimalCost) {
        final List<Integer> costs =  pairs1.stream()
            .flatMap(p1 -> pairs2.stream().map(p2 -> p1[1] + p2[1]))
            // .peek(c -> System.out.println(c))
            .collect(Collectors.toList());

        final int[] bestCostIndexes = findBestCostIndexes(optimalCost, costs);
        return IntStream.range(0, costs.size())
            .filter(e -> bestCostIndexes[e] == 1)
            .mapToObj(e -> {
                return new Integer[] {
                    pairs1.get(e / pairs2.size())[0],
                    pairs2.get(e % pairs2.size())[0]
                };
            })
            // .peek(e -> System.out.println("final: " + Arrays.toString(e)))
            .collect(Collectors.toList());
    }

    private int[] findBestCostIndexes(final int optimal, final List<Integer> costs) {
        final int[][] bestCosts = new int[costs.size() + 1][optimal + 1];
        for (int i = 0; i <= costs.size(); i++) {
            for (int j = 0; j <= optimal; j++) {
                if (i == 0 || j == 0) {
                    bestCosts[i][j] = 0;
                } else {
                    final int previousBestCost = bestCosts[i - 1][j];
                    final int currentCost = costs.get(i - 1);
                    if (currentCost > j) {
                        bestCosts[i][j] = previousBestCost;
                    } else {
                        bestCosts[i][j] = Math.max(previousBestCost, currentCost + bestCosts[i - 1][j - currentCost]);
                    }
                }
            }
        }

        final int[] bestCostIndexes = new int[costs.size()];
        int x = optimal;
        for (int i = bestCosts.length - 1; i > 0; i--) {
            if (bestCosts[i][x] > bestCosts[i - 1][x]) {
                bestCostIndexes[i - 1] = 1;
                x -= costs.get(i - 1);
            }
        }
        return bestCostIndexes;
    }

}
