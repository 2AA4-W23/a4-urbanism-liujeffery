package graphADT;

import java.util.ArrayList;
import java.util.Set;

public interface PathfindingInterface {
    public void setNode1(Node node);
    public void setNode2(Node node);
    public void setEdges(Set<Edge> edges);
    public void setNodes(Set<Node> nodes);
    public ArrayList<Edge> calculatePath();
}
