package utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeMap;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Polygon;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Vertex;
import island.Island;
import island.Island.Tile;

public class Formatter {
    private Structs.Mesh mesh;
    private double maxX;
    private double maxY;

    static final String VERTEX_COLOR = "255,0,0";
    static final String VERTEX_THICKNESS = "0";
    static final String VERTEX_TRANSPARENCY = "100";
    static final String SEGMENT_THICKNESS = "1";
    static final String SEGMENT_TRANSPARENCY = "100";
    

    public Formatter(Structs.Mesh inputMesh){
        this.mesh = inputMesh;
    }

    public Island convertToIsland(){
        // Neighbours
        HashMap<Integer, List<Integer>> adjList = new HashMap<Integer, List<Integer>>();
        List<Structs.Polygon> polygonList = mesh.getPolygonsList();
        for(Polygon p : polygonList){
            adjList.put(p.getCentroidIdx(), p.getNeighborIdxsList());
        }
        
        // find the size of the mesh
        maxX = 0;
        maxY = 0;
        List<Structs.Vertex> vertexList = mesh.getVerticesList();
        for (Structs.Vertex v: vertexList) {
            maxX = (Double.compare(maxX, v.getX()) < 0? v.getX(): maxX);
            maxY = (Double.compare(maxY, v.getY()) < 0? v.getY(): maxY);
        }
        
        // Coordinates
        HashMap<Integer, Double> xCoords = new HashMap<Integer, Double>();
        HashMap<Integer, Double> yCoords = new HashMap<Integer, Double>();
        for (Polygon p : polygonList) {
            Vertex v = mesh.getVertices(p.getCentroidIdx());
            //normalize coordinates to be between 0 and 1
            xCoords.put(p.getCentroidIdx(), v.getX() / maxX);
            yCoords.put(p.getCentroidIdx(), v.getY() / maxY);
        }
        
        System.out.println(adjList.keySet().toString());
        return new Island(adjList, xCoords, yCoords);
    }

    public Structs.Mesh meshFromIsland(Island island){
        Set<Tile> tiles = island.getTiles();
        ArrayList<Structs.Polygon> polygons = new ArrayList<>(mesh.getPolygonsCount());
        ArrayList<Structs.Segment> segments = new ArrayList<>(mesh.getSegmentsCount());
        ArrayList<Structs.Vertex> vertices = new ArrayList<>(mesh.getVerticesCount());


        // TreeMap<Integer, Structs.Polygon> polygons = new TreeMap<>();
        // TreeMap<Integer, Structs.Segment> segments = new TreeMap<>();
        // TreeMap<Integer, Structs.Vertex> vertices = new TreeMap<>();

        // convert tiles to structs.polygon
        for(int i = 0; i < mesh.getPolygonsCount(); i++){ 
            Tile t = island.getTileByID(i);
            System.out.println(t.getId());

            // for every tile, find corresponding polygon
            // add fill colour depending on land/water
            Structs.Polygon oldPolygon = mesh.getPolygons(i);
            Structs.Polygon.Builder pb = oldPolygon.toBuilder();
            Structs.Property.Builder tileColorPropertyBuilder = Structs.Property.newBuilder().setKey("rgb_color");
            tileColorPropertyBuilder.setValue("0,0,255"); // default everything is water
            // TODO CHECK FOR TILE TYPE ATTRIBUTE, COLOUR ACCORDINGLY
            pb.addProperties(tileColorPropertyBuilder.build());
            polygons.add(i, pb.build());

            // pb.setCentroidIdx(t.getId());
            // New Segments

            // pb.addAllNeighborIdxs(oldPolygon.getNeighborIdxsList());
        }

        // New Segments
        for(int i = 0; i < mesh.getSegmentsCount(); i++){
            Structs.Segment oldSegment = mesh.getSegments(i);
            Structs.Segment.Builder sb = oldSegment.toBuilder().clearProperties();
            sb.addProperties(Structs.Property.newBuilder().setKey("rgb_color").setValue("255,0,0").build());
            sb.addProperties(Structs.Property.newBuilder().setKey("thickness").setValue(SEGMENT_THICKNESS).build());
            sb.addProperties(Structs.Property.newBuilder().setKey("transparency").setValue(SEGMENT_TRANSPARENCY).build());

            Structs.Segment newSegment = sb.build();
            // if(sIndex >= segments.size() || segments.get(sIndex) != null) // add segment if doesnt exist
            segments.add(i, newSegment); // copy to same index
            // pb.addSegmentIdxs(sIndex);
        }

        // New Vertices (All)
        for(int i = 0; i < mesh.getVerticesCount(); i++){
            Structs.Vertex oldVertex = mesh.getVertices(i);
            if(oldVertex == null)  continue;
            Structs.Vertex.Builder vb = oldVertex.toBuilder().clearProperties();
            vb.addProperties(Structs.Property.newBuilder().setKey("rgb_color").setValue(VERTEX_COLOR).build());
            vb.addProperties(Structs.Property.newBuilder().setKey("thickness").setValue(VERTEX_THICKNESS).build());
            vb.addProperties(Structs.Property.newBuilder().setKey("transparency").setValue(VERTEX_TRANSPARENCY).build());
            vertices.add(i, vb.build());
        }

        System.out.println(vertices.toString());
        return Structs.Mesh.newBuilder().addAllVertices(vertices).addAllSegments(segments).addAllPolygons(polygons).build();
    }

    // /**
    //  * if index would be directly appended or be pushed into the arraylist, does so.
    //  * Otherwise, makes elements in al equal to the
    //  * @param al
    //  * @param index
    //  */
    // private static void pushFastForward(ArrayList<?> al, int index){
    //     int sizeDiff = index - al.size();
    //     if(sizeDiff <=)
    // }
}
