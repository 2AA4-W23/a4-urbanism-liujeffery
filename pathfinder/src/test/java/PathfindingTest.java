import graphADT.Edge;
import graphADT.Graph;
import graphADT.Node;
import graphADT.Pathfinding;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public class PathfindingTest {

    @Test
    public void pathfindingTest(){
        Graph g = new Graph(4, 4);
        Pathfinding path = new Pathfinding();

        Node node1 = new Node(0, 0, 0);
        g.addNode(node1);
        Node tempNode = new Node(1, 0, 1);
        g.addNode(tempNode);
        Node node2 = new Node(2, 0, 2);
        g.addNode(node2);
        Node tempNode1 = new Node(0, 3, 3);
        g.addNode(tempNode1);

        ArrayList<Edge> listEdges = g.calculatePath(node1, node2, path);
        assert(listEdges.size() == 0);

        Edge edge1 = new Edge(node1, tempNode);
        g.addEdge(edge1);
        Edge edge3 = new Edge(node1, tempNode1);
        g.addEdge(edge3);
        Edge edge4 = new Edge(tempNode1, tempNode);
        g.addEdge(edge4);
        Edge edge5 = new Edge(tempNode1, node2);
        g.addEdge(edge5);

        listEdges = g.calculatePath(node1, node2, path);
        int sumOfEnd = 0;
        for (int i = 0; i < listEdges.size(); i++){
            Edge edge = listEdges.get(i);
            sumOfEnd += edge.getEnd().getId();
        }
        assert(sumOfEnd == 5);

        Edge edge2 = new Edge(tempNode, node2);
        g.addEdge(edge2);


        listEdges = g.calculatePath(node1, node2, path);
        for (int i = 0; i < listEdges.size(); i++){
            Edge edge = listEdges.get(i);
            assert(edge.getEnd().getId() == i + 1);
        }
    }
}
