package utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import attributes.BiomeAttribute;
import attributes.LandAttribute;
import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Polygon;
import island.Island;
import island.Island.Tile;

public class Formatter {
    private Structs.Mesh mesh;
    private double maxX;
    private double maxY;

    static final String LAND_COLOR = "163,134,114";
    static final String WATER_COLOR = "170,194,206";
    static final String BEACH_COLOR = "210,176,140";

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

        for(int i = 0; i < mesh.getPolygonsCount(); i++){
            adjList.put(i, mesh.getPolygons(i).getNeighborIdxsList());
        }
        
        // Mesh size
        maxX = 0;
        maxY = 0;
        List<Structs.Vertex> vertexList = mesh.getVerticesList();
        for (Structs.Vertex v: vertexList) {
            maxX = (Double.compare(maxX, v.getX()) < 0 ? v.getX(): maxX);
            maxY = (Double.compare(maxY, v.getY()) < 0 ? v.getY(): maxY);
        }
        
        // Coordinates
        HashMap<Integer, Double> xCoords = new HashMap<Integer, Double>();
        HashMap<Integer, Double> yCoords = new HashMap<Integer, Double>();
        for(int i = 0; i < mesh.getPolygonsCount(); i++){
            Structs.Polygon p = mesh.getPolygons(i);
            Structs.Vertex v = mesh.getVertices(p.getCentroidIdx());
            xCoords.put(i, v.getX() / maxX);
            yCoords.put(i, v.getY() / maxY);
            // System.out.println("x:" + v.getX() + " y:" + v.getY());
            // System.out.println("x:" + v.getX() / maxX + " y:" + v.getY() / maxY);
        }
        
        // System.out.println(adjList.keySet().toString());
        return new Island(adjList, xCoords, yCoords);
    }

    public Structs.Mesh meshFromIsland(Island island){
        ArrayList<Structs.Polygon> polygons = new ArrayList<>(mesh.getPolygonsCount());
        ArrayList<Structs.Segment> segments = new ArrayList<>(mesh.getSegmentsCount());
        ArrayList<Structs.Vertex> vertices = new ArrayList<>(mesh.getVerticesCount());

        // New Polygons
        for(int i = 0; i < mesh.getPolygonsCount(); i++){ 
            Tile t = island.getTileByID(i);
            // System.out.println(t.getId());

            // for every tile, find corresponding polygon
            Structs.Polygon oldPolygon = mesh.getPolygons(i);
            Structs.Polygon.Builder pb = oldPolygon.toBuilder();
            Structs.Property.Builder tileColorPropertyBuilder = Structs.Property.newBuilder().setKey("rgb_color");

            // Tile colour logic
            if(t.getAttribute(LandAttribute.class).isLand){
                tileColorPropertyBuilder.setValue(LAND_COLOR);
                System.out.println(t.getAttribute(BiomeAttribute.class).biome.toString());
                if((t.getAttribute(BiomeAttribute.class) != null) && (t.getAttribute(BiomeAttribute.class).biome == BiomeAttribute.Biome.BEACH))
                    tileColorPropertyBuilder.setValue(BEACH_COLOR);
            }
            else{
                tileColorPropertyBuilder.setValue(WATER_COLOR);
            }
            // tileColorPropertyBuilder.setValue((String)((int)(dist * 255) + "," + (int)(dist * 255) + "," + (int)(dist * 255)));

            pb.addProperties(tileColorPropertyBuilder.build());
            polygons.add(i, pb.build());
        }

        // New Segments
        for(int i = 0; i < mesh.getSegmentsCount(); i++){
            Structs.Segment oldSegment = mesh.getSegments(i);
            Structs.Segment.Builder sb = oldSegment.toBuilder().clearProperties();
            sb.addProperties(Structs.Property.newBuilder().setKey("rgb_color").setValue("255,0,0").build());
            sb.addProperties(Structs.Property.newBuilder().setKey("thickness").setValue(SEGMENT_THICKNESS).build());
            sb.addProperties(Structs.Property.newBuilder().setKey("transparency").setValue(SEGMENT_TRANSPARENCY).build());

            Structs.Segment newSegment = sb.build();
            segments.add(i, newSegment); // copy to same index
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

        // System.out.println(vertices.toString());
        return Structs.Mesh.newBuilder().addAllVertices(vertices).addAllSegments(segments).addAllPolygons(polygons).build();
    }
}
