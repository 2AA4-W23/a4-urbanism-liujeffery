package graphADT;

import java.util.ArrayList;
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

    public void addEdge(Node node1, Node node2){
        addEdge(new Edge(node1, node2));
    }

    public void addNode(Node node){
        nodes.add(node);
    }

    public Node getNodeByID(int id){
        for (Node node : nodes){
            if (node.getId() == id){
                return node;
            }
        }
        return null;
    }

    public ArrayList<Edge> calculatePath(Node node1, Node node2, PathfindingInterface pathfinder){
        pathfinder.setNode1(node1);
        pathfinder.setNode2(node2);
        pathfinder.setEdges(edges);
        pathfinder.setNodes(nodes);
        
        ArrayList<Edge> path = pathfinder.calculatePath();
        return path;
    }
}