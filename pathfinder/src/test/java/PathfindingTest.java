import graphADT.Edge;
import graphADT.Node;
import graphADT.Pathfinding;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

public class PathfindingTest {

    @Test
    public void pathfindingTest(){
        Pathfinding path = new Pathfinding();

        Set<Node> nodes = new HashSet<>();
        Node node1 = new Node(0, 0, 0);
        nodes.add(node1);
        Node tempNode = new Node(1, 1, 1);
        nodes.add(tempNode);
        Node node2 = new Node(2, 2, 2);
        nodes.add(node2);
        Node tempNode1 = new Node(3, 0, 3);
        nodes.add(tempNode1);

        Set<Edge> edges = new HashSet<>();
        Edge edge1 = new Edge(node1, tempNode);
        edges.add(edge1);
        Edge edge2 = new Edge(tempNode, node2);
        edges.add(edge2);
        Edge edge3 = new Edge(node1, tempNode1);
        edges.add(edge3);
        Edge edge4 = new Edge(tempNode1, tempNode);
        edges.add(edge4);
        Edge edge5 = new Edge(tempNode1, node2);
        edges.add(edge5);

        path.setNode1(node1);
        path.setNode2(node2);
        path.setNodes(nodes);
        path.setEdges(edges);

        ArrayList<Edge> listEdges = path.calculatePath();
        assert(listEdges.size() == 2);
        for (int i = 0; i < listEdges.size(); i++){
            Edge edge = listEdges.get(i);
            assert(edge.getEnd().getId() == i + 1);
        }
    }
}
