package graphADT;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Stack;

public class Pathfinding implements PathfindingInterface{

    private Node node1;
    private Node node2;
    private Set<Edge> edges;
    private Set<Node> nodes;

    @Override
    public void setNode1(Node node) {
        this.node1 = node;
    }

    @Override
    public void setNode2(Node node) {
        this.node2 = node;
    }

    @Override
    public void setEdges(Set<Edge> edges) {
        this.edges = edges;
    }

    public void setNodes(Set<Node> nodes){
        this.nodes = nodes;
    }

    @Override
    public ArrayList<Edge> calculatePath() {
        ArrayList<Edge> result = new ArrayList<>();
        PriorityQueue <Node> queue = new PriorityQueue<>();

        Node[] path = new Node[nodes.size()];
        path[node1.getId()] = node1;

        double[] cost = new double[nodes.size()];
        for (int i = 0; i < cost.length; i++){
            cost[i] = -1;
        }
        cost[node1.getId()] = 0;
        queue.add(node1);

        while (!queue.isEmpty()){
            Node start = queue.poll();
            for (Edge edge : edges){
                if (edge.startsWith(start)){
                    Node end = edge.getEnd();
                    if (cost[end.getId()] == -1 || cost[start.getId()] + edge.getDistance() < cost[end.getId()]){
                        path[end.getId()] = start;
                        cost[end.getId()] = cost[start.getId()] + edge.getDistance();
                        end.setPriority(cost[end.getId()]);
                        queue.add(end);
                    }
                }
            }
        }

        Stack<Edge> edgesStack = new Stack<>();
        Node tempNode = path[node2.getId()];
        while (!tempNode.equals(node1)){
            tempNode = path[node2.getId()];
            for (Edge edge : edges){
                if (edge.matches(tempNode, node2)){
                    edgesStack.add(edge);
                    node2 = tempNode;
                    break;
                }
            }
        }

        while(!edgesStack.isEmpty()){
            result.add(edgesStack.pop());
        }
        return result;
    }
}
