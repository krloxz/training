package com.krloxz.training;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import org.hamcrest.Matcher;
import java.util.*;

class GraphTest {

    @Test
    void shouldFindShortestPathBetweenTwoVerticesWhenEdgesAreNotWeighted() {
    	final Graph<Character> graph = new Graph();
    	graph.addEdge('A', 'B');
    	graph.addEdge('A', 'E');
        graph.addEdge('B', 'F');
        graph.addEdge('C', 'D');
        graph.addEdge('C', 'F');
        graph.addEdge('C', 'G');
        graph.addEdge('D', 'G');
        graph.addEdge('D', 'H');
        graph.addEdge('F', 'G');
        graph.addEdge('G', 'H');
    	assertThat(graph.findShortestPath('B', 'H'), contains('B', 'F', 'G', 'H'));
    }

    @Test
    void shouldFindShortestPathBetweenTwoVerticesWhenEdgesAreWeighted() {
        final Graph<String> graph = new Graph<>(true);
        graph.addEdge("Home", "A", 3);
        graph.addEdge("Home", "B", 2);
        graph.addEdge("Home", "C", 5);
        graph.addEdge("A", "D", 3);
        graph.addEdge("B", "D", 1);
        graph.addEdge("B", "E", 6);
        graph.addEdge("C", "E", 2);
        graph.addEdge("D", "F", 4);
        graph.addEdge("E", "F", 1);
        graph.addEdge("E", "School", 4);
        graph.addEdge("F", "School", 2);
        assertThat(graph.findShortestPath("Home", "School"), contains("Home", "B", "D", "F", "School"));
    }

    @Test
    void shouldFindDifferentPathsForWightedAndUnweightedGraphs() {
        final Graph<Character> weightedGraph = new Graph<>(true);
        weightedGraph.addEdge('A', 'B', 5);
        weightedGraph.addEdge('A', 'C', 2);
        weightedGraph.addEdge('C', 'B', 1);
        final Graph<Character> unweightedGraph = new Graph<>();
        unweightedGraph.addEdge('A', 'B');
        unweightedGraph.addEdge('A', 'C');
        unweightedGraph.addEdge('C', 'B');
        final List<Character> shortestPathForWeighted = weightedGraph.findShortestPath('A', 'B');
        final List<Character> shortestPathForUnweighted = unweightedGraph.findShortestPath('A', 'B');
        assertThat(shortestPathForWeighted, is(not(shortestPathForUnweighted)));
    }

    /*
    shouldAddVerticesWithNoEdges()
    shouldAddEdgesWhenBothVerticesExist()
    shouldAddEdgesWhenBothVerticesDoesntExist()
    shouldAddEdgesWhenOnlyOneVertexExistDoesntExist()

    remove

    contain

    shouldFindShortestPathsFromRootVertexWhenEdgesAreNotWeighted()
    shouldFindShortestPathsFromRootVertexWhenEdgesAreWeighted()
    shouldAnswerShortestPathExistWhenVerticesAreConnected()
    shouldAnswerShortestPathDoesnExistWhenVerticesAreNotConnected()
    shouldAnswerIsCyclicWhenCyclesArePresent()
    shouldAnswerIsAcyclicWhenCyclesAreNotPresent()
    */

}