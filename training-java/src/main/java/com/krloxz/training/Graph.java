package com.krloxz.training;

import java.util.*;
import java.util.stream.*;

class Graph<T> {

    private final boolean weighted;
    private final Map<Vertex, Vertex> vertices;

    public Graph() {
        this(false);
    }

    public Graph(final boolean weighted) {
        this.weighted = weighted;
        this.vertices = new HashMap<>();
    }

    public void addEdge(final T start, final T end, final int weight) {
        final Vertex startVertex = this.vertices.getOrDefault(new Vertex(start), new Vertex(start));
        final Vertex endVertex = this.vertices.getOrDefault(new Vertex(end), new Vertex(end));
        startVertex.addNeighbor(endVertex, weight);
        endVertex.addNeighbor(startVertex, weight);
        
        this.vertices.putIfAbsent(startVertex, startVertex);
        this.vertices.putIfAbsent(endVertex, endVertex);
    }

    public void addEdge(final T start, final T end) {
        addEdge(start, end, 1);
    }

    public List<T> findShortestPath(T root, T target) {
        this.vertices.values().stream()
            .forEach(v -> v.reset());

        final Vertex rootVertex = this.vertices.get(new Vertex(root));
        rootVertex.updateDistanceToRoot(0, null);
        final Queue<Vertex> queue = newQueue();
        queue.add(rootVertex);
        while (!queue.isEmpty()) {
            final Vertex currentVertex = queue.remove();
            currentVertex.visited();
            currentVertex.getUnvisitedNeighbors()
                .forEach((vertex, weight) -> {
                    final int newDistance = weight + currentVertex.getDistanceToRoot();
                    if (newDistance < vertex.getDistanceToRoot()) {
                        vertex.updateDistanceToRoot(newDistance, currentVertex);
                    }
                    queue.add(vertex);
                });
        }

        final Deque<Vertex> stack = new ArrayDeque<>();
        stack.push(this.vertices.get(new Vertex(target)));
        while (stack.peek().hasParent()) {
            stack.push(stack.peek().getParent());
        }
        return stack.stream()
            .map(v -> (T) v.getId())
            .collect(Collectors.toList());
    }

    private Queue<Vertex> newQueue() {
        if (this.weighted) {
            return new PriorityQueue<>((e1, e2) -> e1.getDistanceToRoot() - e2.getDistanceToRoot());
        }
        return new ArrayDeque<>();
    }

}
