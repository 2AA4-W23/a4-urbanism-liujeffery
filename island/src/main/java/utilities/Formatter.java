package utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import attributes.BiomeAttribute;
import attributes.ElevationAttribute;
import attributes.LakeAttribute;
import attributes.LandAttribute;
import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import island.Island;
import island.Tile;

public class Formatter {
    private Structs.Mesh mesh;
    private double maxX;
    private double maxY;

    static final String LAND_COLOR = "163,134,114";
    static final String WATER_COLOR = "170,194,206";
    static final String BEACH_COLOR = "210,176,140";
    static final String LAKE_COLOUR = "19,163,235";

    static final String VERTEX_COLOR = "255,0,0";
    static final String VERTEX_THICKNESS = "0";
    static final String VERTEX_TRANSPARENCY = "100";
    static final String SEGMENT_THICKNESS = "1";
    static final String SEGMENT_TRANSPARENCY = "100";
    

    public Formatter(Structs.Mesh inputMesh){
        this.mesh = inputMesh;
    }

    public Island convertToIsland(){
        // find the size of the mesh
        this.maxX = 0;
        this.maxY = 0;
        for (Structs.Vertex v: mesh.getVerticesList()) {
            maxX = (Double.compare(maxX, v.getX()) < 0 ? v.getX(): maxX);
            maxY = (Double.compare(maxY, v.getY()) < 0 ? v.getY(): maxY);
        }

        //create tiles from mesh
        Set<Tile> tiles = new HashSet<>();
        for(int i = 0;i < mesh.getPolygonsCount(); i++){
            Structs.Polygon p = mesh.getPolygons(i);

            //normalize centroid coordinates
            Structs.Vertex v = mesh.getVertices(p.getCentroidIdx());
            double x = v.getX() / maxX;
            double y = v.getY() / maxY;

            Tile t = new Tile(i, x, y);
            tiles.add(t);
        }

        //create adjacency list
        Map<Integer, List<Integer>> tileAdjList = new HashMap<>();
        for(int i = 0;i < mesh.getPolygonsCount(); i++){
            tileAdjList.put(i, mesh.getPolygons(i).getNeighborIdxsList());
        }

        return new Island(tiles, tileAdjList);
    }

    public Structs.Mesh meshFromIsland(Island island){
        ArrayList<Structs.Polygon> polygons = new ArrayList<>(mesh.getPolygonsCount());
        ArrayList<Structs.Segment> segments = new ArrayList<>(mesh.getSegmentsCount());
        ArrayList<Structs.Vertex> vertices = new ArrayList<>(mesh.getVerticesCount());

        // New Polygons
        for(int i = 0; i < mesh.getPolygonsCount(); i++){ 
            Tile t = island.getTileByID(i);

            // for every tile, find corresponding polygon
            Structs.Polygon oldPolygon = mesh.getPolygons(i);
            Structs.Polygon.Builder pb = oldPolygon.toBuilder();
            Structs.Property.Builder tileColorPropertyBuilder = Structs.Property.newBuilder().setKey("rgb_color");

            // Tile colour logic
            if(t.getAttribute(LandAttribute.class).isLand){
                tileColorPropertyBuilder.setValue(LAND_COLOR);
                tileColorPropertyBuilder.setValue((String)((int)(t.getAttribute(ElevationAttribute.class).elevation * 255) + "," + (int)(t.getAttribute(ElevationAttribute.class).elevation * 255) + "," + (int)(t.getAttribute(ElevationAttribute.class).elevation * 255)));
                if((t.getAttribute(BiomeAttribute.class) != null) && (t.getAttribute(BiomeAttribute.class).biome == BiomeAttribute.Biome.BEACH)){
                    tileColorPropertyBuilder.setValue(BEACH_COLOR);
                }
                if (t.getAttribute(LakeAttribute.class).isLake){
                    tileColorPropertyBuilder.setValue(LAKE_COLOUR);
                }
            }
            else{
                tileColorPropertyBuilder.setValue(WATER_COLOR);
            }
            

            pb.addProperties(tileColorPropertyBuilder.build());
            polygons.add(i, pb.build());
        }

        // New Segments
        for(int i = 0; i < mesh.getSegmentsCount(); i++){
            Structs.Segment oldSegment = mesh.getSegments(i);
            Structs.Segment.Builder sb = oldSegment.toBuilder().clearProperties();
            sb.addProperties(Structs.Property.newBuilder().setKey("rgba_color").setValue("0,0,255,255").build());
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

        // add rivers here

        // System.out.println(vertices.toString());
        return Structs.Mesh.newBuilder().addAllVertices(vertices).addAllSegments(segments).addAllPolygons(polygons).build();
    }
}
