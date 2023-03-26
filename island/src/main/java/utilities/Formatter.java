package utilities;

import java.util.HashMap;
import java.util.List;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Polygon;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Vertex;
import island.Island;

public class Formatter {
    private Structs.Mesh mesh;
    private double maxX;
    private double maxY;

    public Formatter(Structs.Mesh inputMesh){
        this.mesh = inputMesh;
    }

    public Island convertAbstractMesh(){
        // Neighbours
        HashMap<Integer, List<Integer>> adjList = new HashMap<Integer, List<Integer>>();
        for(Polygon p : mesh.getPolygonsList()){
            adjList.put(p.getCentroidIdx(), p.getNeighborIdxsList());
        }
        
        // Mesh size
        maxX = 0;
        maxY = 0;
        for (Structs.Vertex v: mesh.getVerticesList()) {
            maxX = (Double.compare(maxX, v.getX()) < 0? v.getX(): maxX);
            maxY = (Double.compare(maxY, v.getY()) < 0? v.getY(): maxY);
        }
        
        // Coordinates
        HashMap<Integer, Double> xCoords = new HashMap<Integer, Double>();
        HashMap<Integer, Double> yCoords = new HashMap<Integer, Double>();
        for (Polygon p : mesh.getPolygonsList()) {
            Vertex v = mesh.getVertices(p.getCentroidIdx());
            xCoords.put(p.getCentroidIdx(), v.getX() / maxX);
            yCoords.put(p.getCentroidIdx(), v.getY() / maxY);
        }
        
        return new Island(adjList, xCoords, yCoords);
    }
}
