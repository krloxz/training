package com.krloxz.training;

import java.util.*;
import java.util.stream.*;

class Vertex {

    private final Object id;
    private final Map<Vertex, Integer> neighbors;
    private boolean visited;
    private int distanceToRoot;
    private Vertex parent;

    Vertex(final Object id) {
        this.id = id;
        this.neighbors = new HashMap<>();
        reset();
    }

    public void reset() {
        this.visited = false;
        this.distanceToRoot = Integer.MAX_VALUE;
        this.parent = null;
    }

    public Object getId() {
        return this.id;
    }

    public void addNeighbor(final Vertex neighbor, final int weight) {
        this.neighbors.put(neighbor, weight);
    }

    public Map<Vertex, Integer> getUnvisitedNeighbors() {
        return this.neighbors.entrySet()
            .stream()
            .filter(e -> !e.getKey().visited)
            .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
    }

    public void setParent(final Vertex parent) {
        this.parent = parent;
    }

    public boolean hasParent() {
        return this.parent != null;
    }

    public Vertex getParent() {
        return this.parent;
    }

    public void visited() {
        this.visited = true;
    }

    public int getDistanceToRoot() {
        return this.distanceToRoot;
    }

    public void updateDistanceToRoot(final int distanceToRoot, final Vertex parent) {
        this.distanceToRoot = distanceToRoot;
        this.parent = parent;
    }

    @Override
    public boolean equals(final Object object) {
        if (object instanceof Vertex) {
            final Vertex other = (Vertex) object;
            return this.id.equals(other.id);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

}