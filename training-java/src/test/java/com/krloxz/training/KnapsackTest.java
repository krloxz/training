package com.krloxz.training;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

import org.junit.jupiter.api.*;
import java.util.*;
import java.math.*;

// Structured as per https://blog.codecentric.de/en/2018/09/structured-junit-5-testing/
class KnapsackTest {
    
    private int capacity;
    private int[][] items;

    @Nested
    class WhenNoItems {

        @BeforeEach
        void initItemAndCapacity() {
            capacity = 5;
            items = new int[][]{};
        }

        @Test
        void shouldFindZeroAsBestValue() {
            assertThat(knpsack(capacity, items), is(0));
        }

        @Test
        void shouldNotSelectItems() {
            assertThat(knpsackItems(capacity, items), is(new int[]{}));
        }
    }

    @Nested
    class WhencapacityIsZero {

        @BeforeEach
        void initItemAndCapacity() {
            capacity = 0;
            items = new int[][]{
                { 6, 1 }, { 10, 2 }, { 12, 3 }
            };
        }

        @Test
        void shouldFindZeroAsBestValue() {
            assertThat(knpsack(capacity, items), is(0));
        }

        @Test
        void shouldNotSelectItems() {
            assertThat(knpsackItems(capacity, items), is(new int[]{ 0, 0, 0}));
        }
    }

    @Nested
    class WhenSingleItemWeightIsGreaterThanCapacity {

        @BeforeEach
        void initItemAndCapacity() {
            capacity = 5;
            items = new int[][]{
                { 6, 10 }
            };
        }

        @Test
        void shouldFindZeroAsBestValue() {
            assertThat(knpsack(capacity, items), is(0));
        }

        @Test
        void shouldNotSelectItems() {
            assertThat(knpsackItems(capacity, items), is(new int[]{ 0 }));
        }
    }

    @Nested
    class WhenSingleItemWeightIsLessThanCapacity {

        @BeforeEach
        void initItemAndCapacity() {
            capacity = 5;
            items = new int[][]{
                { 6, 1 }
            };
        }

        @Test
        void shouldFindSingleItemValueAsBestValue() {
            assertThat(knpsack(capacity, items), is(6));
        }

        @Test
        void shouldSelectTheSingleItem() {
            assertThat(knpsackItems(capacity, items), is(new int[]{ 1 }));
        }
    }

    @Nested
    class WhenMultipleItemWeightsAreLessThanCapacity {

        @BeforeEach
        void initItemAndCapacity() {
            capacity = 18;
            items = new int[][]{
                { 12, 4 }, { 10, 6 }, { 8, 5 }, { 11, 7 }, { 14, 3 }, { 7, 1 }, { 9, 6 }
            };
        }

        @Test
        void shouldFindBestValue() {
            assertThat(knpsack(capacity, items), is(44));
        }

        @Test
        void shouldSelectSomeItems() {
            assertThat(knpsackItems(capacity, items), is(new int[]{ 1, 1, 1, 0, 1, 0, 0 }));
        }
    }

    private int knpsack(final int capacity, final int[][] items) {
        return knpsackMatrix(capacity, items)[items.length][capacity];
    }

    private int[] knpsackItems(final int capacity, final int[][] items) {
        final int[][] knpsack = knpsackMatrix(capacity, items);
        final int[] knpsackItems = new int[items.length];
        int w = capacity;
        for (int i = items.length; i > 0; i--) {
            if (knpsack[i][w] > knpsack[i - 1][w]) {
                knpsackItems[i - 1] = 1;
                w -= items[i - 1][1];
            }
        }
        return knpsackItems;
    }

    private int[][] knpsackMatrix(final int capacity, final int[][] items) {
        final int[][] knpsack = new int[items.length + 1][capacity + 1];
        for (int i = 0; i <= items.length; i++) {
            for (int w = 0; w <= capacity; w++) {
                if (i == 0 || w == 0) {
                    knpsack[i][w] = 0;
                } else if (items[i - 1][1] > w) {
                    knpsack[i][w] = knpsack[i - 1][w];
                } else {
                    final int itemValue = items[i - 1][0];
                    final int itemWeight = items[i - 1][1];
                    final int itemIncluded = itemValue + knpsack[i - 1][w - itemWeight];
                    final int itemNotIncluded = knpsack[i - 1][w];
                    knpsack[i][w] = Math.max(itemIncluded, itemNotIncluded);
                }
            }
        }
        return knpsack;
    }

    private int recursiveKnapsack(final int capacity, final int currentItem, final int[][] items) {
        if (currentItem == 0 || capacity == 0) {
            return 0;
        }
        final int[] item = items[currentItem - 1];
        if (item[1] > capacity) {
            return recursiveKnapsack(capacity, currentItem - 1, items);
        }
        final int itemIncluded = item[0] + recursiveKnapsack(capacity - item[1], currentItem - 1, items);
        final int itemNotIncluded = recursiveKnapsack(capacity, currentItem - 1, items);
        return Math.max(itemIncluded, itemNotIncluded);
    }

}
