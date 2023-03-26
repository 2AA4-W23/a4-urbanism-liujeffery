package utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Polygon;
import island.Island;

public class Formatter {
    private Structs.Mesh mesh;

    public Formatter(Structs.Mesh inputMesh){
        this.mesh = inputMesh;
    }

    public Island convertAbstractMesh(){
        HashMap<Integer, List<Integer>> adjList = new HashMap<Integer, List<Integer>>();
        for(Polygon p : mesh.getPolygonsList()){
            adjList.put(p.getCentroidIdx(), p.getNeighborIdxsList());
        }
        return new Island(adjList);
    }
}
