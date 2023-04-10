package graphADT;

import java.util.HashSet;
import java.util.Set;

public class Graph {
    private Set<Edge> edges;
    private Set<Node> nodes;

    double x_bound;
    double y_bound;

    public Graph(double x_bound, double y_bound){
        this.x_bound = x_bound;
        this.y_bound = y_bound;

        edges = new HashSet<>();
        nodes = new HashSet<>();
    }

    public void addEdge(Edge edge){
        edges.add(edge);
    }

    public void addNode(Node node){
        nodes.add(node);
    }
}