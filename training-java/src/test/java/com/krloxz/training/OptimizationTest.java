package com.krloxz.training;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import java.util.*;
import java.util.stream.*;
import java.util.function.*;

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

    @Test
    public void test4() {
        // Integer[0] = ID, Integer[1] = cost
        final List<Integer[]> pairs1 = Arrays.<Integer[]>asList(
            new Integer[] { 11, 2 },
            new Integer[] { 12, 4 },
            new Integer[] { 13, 5 }
        );
        final List<Integer[]> pairs2 = Arrays.<Integer[]>asList(
            new Integer[] { 21, 1 },
            new Integer[] { 22, 2 }
        );
        final int optimalCost = 9;

        assertThat(findOptimalCombination(pairs1, pairs2, optimalCost),
            contains(arrayContaining(11, 22), arrayContaining(12, 21)));
    }

    private List<Integer[]> findOptimalCombination(final List<Integer[]> pairs1, final List<Integer[]> pairs2, final int optimalCost) {
        final int[][][] bestCosts = new int[pairs1.size() + 1][pairs2.size() + 1][optimalCost + 1];
        for (int i = 0; i <= pairs1.size(); i++) {
            for (int j = 0; j <= pairs2.size(); j++) {
                for (int k = 0; k <= optimalCost; k++) {
                    if (i == 0 || j == 0 || k == 0) {
                        bestCosts[i][j][k] = 0;
                    } else {
                        final int ii = i;
                        final int jj = j;
                        final int kk = k;
                        final int previousBestCost = IntStream.rangeClosed(0, pairs2.size())
                            .filter(e -> e != jj)
                            .map(e -> bestCosts[ii - 1][e][kk])
                            .max()
                            .getAsInt();
                        final int currentCost = pairs1.get(i - 1)[1] + pairs2.get(j - 1)[1];
                        if (currentCost > k) {
                            bestCosts[i][j][k] = previousBestCost;
                        } else {
                            final int previousAccumulatedCost = IntStream.rangeClosed(0, pairs2.size())
                                .filter(e -> e != jj)
                                .map(e -> bestCosts[ii - 1][e][kk - currentCost])
                                .max()
                                .getAsInt();
                            bestCosts[i][j][k] = Math.max(previousBestCost, currentCost + previousAccumulatedCost);
                        }
                    }
                }
                System.out.println(i + "," + j + ": " + Arrays.toString(bestCosts[i][j]));
            }
        }
        System.out.println("Result: " + bestCosts[pairs1.size()][pairs2.size()][optimalCost]);
        final List<Integer[]> bestPairs = new ArrayList<>();
        int k = optimalCost;
        int l = -1;
        for (int i = bestCosts.length - 1; i > 0; i--) {
            final int ii = i;
            final int kk = k;
            final int ll = l;
            final int j = IntStream.rangeClosed(0, pairs2.size())
                .filter(e -> e != ll)
                .reduce(0, (a, b) -> bestCosts[ii - 1][a][kk] > bestCosts[ii - 1][b][kk] ? a : b);
            final int jj = IntStream.rangeClosed(0, pairs2.size())
                .filter(e -> e != ll)
                .reduce(0, (a, b) -> bestCosts[ii][a][kk] > bestCosts[ii][b][kk] ? a : b);
            if (bestCosts[ii][jj][kk] > bestCosts[ii - 1][j][kk]) {
                bestPairs.add(new Integer[]{ pairs1.get(ii - 1)[0], pairs2.get(jj - 1)[0] });
                l -= pairs1.get(ii - 1)[1];
                l -= pairs2.get(jj - 1)[1];
            }
        }
        bestPairs.stream()
            .forEach(e -> System.out.println(Arrays.toString(e)));
        return bestPairs;


        // final List<Integer> costs =  pairs1.stream()
        //     .flatMap(p1 -> pairs2.stream().map(p2 -> p1[1] + p2[1]))
        //     // .peek(c -> System.out.println(c))
        //     .collect(Collectors.toList());

        // final int[] bestCostIndexes = findBestCostIndexes(optimalCost, costs);
        // return IntStream.range(0, costs.size())
        //     .filter(e -> bestCostIndexes[e] == 1)
        //     .mapToObj(e -> {
        //         return new Integer[] {
        //             pairs1.get(e / pairs2.size())[0],
        //             pairs2.get(e % pairs2.size())[0]
        //         };
        //     })
        //     .peek(e -> System.out.println("final: " + Arrays.toString(e)))
        //     .collect(Collectors.toList());
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
