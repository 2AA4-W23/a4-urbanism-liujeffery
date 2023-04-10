package graphADT;

import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

public class Pathfinding implements PathfindingInterface{

    private Node node1;
    private Node node2;
    private Set<Edge> edges;

    @Override
    public void getNode1(Node node) {
        this.node1 = node;
    }

    @Override
    public void getNode2(Node node) {
        this.node2 = node;
    }

    @Override
    public void getEdges(Set<Edge> edges) {
        this.edges = edges;
    }

    @Override
    public Set<Edge> calculatePath() {
        Set<Edge> result = new HashSet<>();
        PriorityQueue <Node> queue = new PriorityQueue<>();

        queue.add(node1);
        queue.poll();

        return result;
    }
}
