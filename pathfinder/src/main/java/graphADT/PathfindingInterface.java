package graphADT;

import java.util.Set;

public interface PathfindingInterface {
    public void getNode1(Node node);
    public void getNode2(Node node);
    public void getEdges(Set<Edge> edges);
    public Set<Edge> calculatePath();
}
