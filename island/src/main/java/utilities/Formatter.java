package utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import attributes.BiomeAttribute;
import attributes.CityAttribute;
import attributes.ElevationAttribute;
import attributes.LakeAttribute;
import attributes.LandAttribute;
import attributes.MoistureAttribute;
import attributes.RiverAttribute;
import attributes.TemperatureAttribute;
import attributes.CityAttribute.Cities;
import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import graphADT.Graph;
import graphADT.Node;
import island.Edge;
import island.Island;
import island.Tile;

public class Formatter {
    private Structs.Mesh mesh;
    private double maxX;
    private double maxY;
    private final Heatmap HEATMAP;
    
    static final String LAND_COLOR = "163,134,114";
    static final String WATER_COLOR = "170,194,206";
    static final String BEACH_COLOR = "210,176,140";
    static final String LAKE_COLOUR = "19,163,235";
    static final String SNOW_COLOUR = "255,255,255";
    static final String FIELD_COLOUR = "250,225,115";
    static final String ROCKY_LIGHT_COLOUR = "125,125,125";
    static final String ROCKY_DARK_COLOUR = "55,55,55";
    static final String ICE_COLOUR = "177,252,252";
    static final String SWAMP_COLOUR = "11,51,1";
    static final String RAINFOREST_COLOUR = "39,143,10";
    static final String SHRUBS_COLOUR = "216,247,208";
    static final String TUNDRA_COLOUR = "163,134,114";
    static final String GRASSLAND_COLOUR = "100,200,100";
    static final String FOREST_COLOUR = "50,150,50";

    static final String VERTEX_COLOR = "255,0,0";
    static final String VERTEX_THICKNESS = "0";
    static final String VERTEX_TRANSPARENCY = "0";
    static final String SEGMENT_THICKNESS = "1";
    static final String SEGMENT_TRANSPARENCY = "100";
    

    public Formatter(Structs.Mesh inputMesh, Heatmap heatmap){
        this.mesh = inputMesh;
        this.HEATMAP = heatmap;
    }

    public enum Heatmap{
        NONE, ELEVATION, MOISTURE, TEMPERATURE
    }

    public Island convertToIsland(){
        // find the size of the mesh
        this.maxX = 0;
        this.maxY = 0;
        for (Structs.Vertex v: mesh.getVerticesList()) {
            maxX = (Double.compare(maxX, v.getX()) < 0 ? v.getX(): maxX);
            maxY = (Double.compare(maxY, v.getY()) < 0 ? v.getY(): maxY);
        }


        //create segments
        Map<Integer, Edge> edges = new HashMap<>();
        for(int i = 0;i < mesh.getSegmentsCount(); i++){
            Structs.Segment s = mesh.getSegments(i);
            edges.put(i, new Edge(i, s.getV1Idx(), s.getV2Idx()));
        }

        //create tiles from mesh
        Map<Integer, Tile> tiles = new HashMap<>();
        for(int i = 0;i < mesh.getPolygonsCount(); i++){
            Structs.Polygon p = mesh.getPolygons(i);

            //get and normalize centroid coordinates
            Structs.Vertex v = mesh.getVertices(p.getCentroidIdx());
            double x = v.getX() / maxX;
            double y = v.getY() / maxY;

            //add the edges that make up the tile
            Set<Edge> pEdges = new HashSet<>();
            for(Integer segmentId : p.getSegmentIdxsList()){
                pEdges.add(edges.get(segmentId));
            }

            tiles.put(i, new Tile(i, x, y, pEdges));
        }

        //set the neighbors for each tile
        for(int i = 0;i < mesh.getPolygonsCount(); i++){
            Structs.Polygon p = mesh.getPolygons(i);
            List<Integer> neighbourIds = p.getNeighborIdxsList();
            Set<Tile> neighbourTiles = new HashSet<>();
            for(Integer neighbourId : neighbourIds){
                neighbourTiles.add(tiles.get(neighbourId));
            } 

            boolean setFail = !tiles.get(i).setNeighbours(neighbourTiles);
            if(setFail) throw new Error("Error: Tried re-setting neighbours during island construction");
        }

        return new Island(tiles, edges);
    }

    public Graph graphFromIsland(Island island){
        Graph graph = new Graph(maxX, maxY);
        for (int id : island.getIds()){
            Tile tile = island.getTileByID(id);

            graph.addNode(new Node(tile.getX(), tile.getY(), id));
        }
        for (Tile tile : island.getTiles()){
            for (Tile neighbour : tile.getNeighbours()){
                graph.addEdge(graph.getNodeByID(tile.getId()), graph.getNodeByID(neighbour.getId()));
            }
        }
        return graph;
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
                String landColour;
                switch(t.getAttribute(BiomeAttribute.class).biome){
                    case BEACH:
                        landColour = BEACH_COLOR;
                        break;
                    case FIELD:
                        landColour = FIELD_COLOUR;
                        break;
                    case ROCKY_LIGHT:
                        landColour = ROCKY_LIGHT_COLOUR;
                        break;
                    case ROCKY_DARK:
                        landColour = ROCKY_DARK_COLOUR;
                        break;
                    case SHRUBS:
                        landColour = SHRUBS_COLOUR;
                        break;
                    case ICE:
                        landColour = ICE_COLOUR;
                        break;
                    case SWAMP:
                        landColour = SWAMP_COLOUR;
                        break;
                    case RAINFOREST:
                        landColour = RAINFOREST_COLOUR;
                        break;
                    case GRASSLAND:
                        landColour = GRASSLAND_COLOUR;
                        break;
                    case LAND:
                        landColour = LAND_COLOR;
                        break;
                    case OCEAN:
                        landColour = WATER_COLOR;
                        break;
                    case SNOW:
                        landColour = SNOW_COLOUR;
                        break;
                    case FOREST:
                        landColour = FOREST_COLOUR;
                        break;
                    case TUNDRA:
                        landColour = TUNDRA_COLOUR;
                        break;
                    default:
                        landColour = "0,0,0";
                }
                if(t.getAttribute(LakeAttribute.class).isLake || t.getAttribute(RiverAttribute.class).isEndorheic){
                    landColour = LAKE_COLOUR;
                }
                if (!HEATMAP.equals(Heatmap.NONE)){
                    switch (HEATMAP){
                        case ELEVATION:
                            landColour = (int)(t.getAttribute(ElevationAttribute.class).elevation * 255) + "," + (int)(t.getAttribute(ElevationAttribute.class).elevation * 255) + "," + (int)(t.getAttribute(ElevationAttribute.class).elevation * 255);
                            break;
                        case TEMPERATURE:
                            landColour = (int)(t.getAttribute(TemperatureAttribute.class).temperature * 255) + "," + (int)(t.getAttribute(TemperatureAttribute.class).temperature * 255) + "," + (int)(t.getAttribute(TemperatureAttribute.class).temperature * 255);
                            break;
                        case MOISTURE:
                            landColour = (int)(t.getAttribute(MoistureAttribute.class).moisture * 255) + "," + (int)(t.getAttribute(MoistureAttribute.class).moisture * 255) + "," + (int)(t.getAttribute(MoistureAttribute.class).moisture * 255);
                            break;
                        default:
                            landColour = "0,0,0";
                    }
                }
                tileColorPropertyBuilder.setValue(landColour);
            }
            else {
                tileColorPropertyBuilder.setValue(WATER_COLOR);
            }

            pb.addProperties(tileColorPropertyBuilder.build());
            polygons.add(i, pb.build());
        }

        // New Segments
        for(int i = 0; i < mesh.getSegmentsCount(); i++){
            Structs.Segment oldSegment = mesh.getSegments(i);
            Structs.Segment.Builder sb = oldSegment.toBuilder().clearProperties();
            sb.addProperties(Structs.Property.newBuilder().setKey("rgb_color").setValue("255,255,255").build());
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
            vb.addProperties(Structs.Property.newBuilder().setKey("rgb_color").setValue(VERTEX_COLOR + ",0").build());
            vb.addProperties(Structs.Property.newBuilder().setKey("thickness").setValue(VERTEX_THICKNESS).build());
            vb.addProperties(Structs.Property.newBuilder().setKey("transparency").setValue(VERTEX_TRANSPARENCY).build());
            vertices.add(i, vb.build());
        }

        // create river segments here, shouldn't matter too much if we create duplicates
        for(Tile tile : island.getTiles()){
            RiverAttribute attr = tile.getAttribute(RiverAttribute.class);
            for(Edge e : attr.riverEdges.keySet()){
                Structs.Segment oldSegment = segments.get(e.id);
                Integer _SEGMENT_THICKNESS = Integer.parseInt(SEGMENT_THICKNESS)*attr.riverEdges.get(e);;

                Structs.Segment.Builder sb = oldSegment.toBuilder().clearProperties();
                sb.addProperties(Structs.Property.newBuilder().setKey("rgb_color").setValue("0,0,255").build());
                sb.addProperties(Structs.Property.newBuilder().setKey("thickness").setValue(_SEGMENT_THICKNESS.toString()).build());
                sb.addProperties(Structs.Property.newBuilder().setKey("transparency").setValue(SEGMENT_TRANSPARENCY).build());
                
                segments.add(sb.build());
            }
        }
        
        for (Tile tile : island.getTiles()){
            if (!tile.getAttribute(CityAttribute.class).city.equals(Cities.NONE)){
                Structs.Vertex.Builder vb = Structs.Vertex.newBuilder();
                vb.setX(tile.getX() * maxX).setY(tile.getY() * maxY);

                String cityColour = "";
                switch(tile.getAttribute(CityAttribute.class).city){
                    case VILLAGE:
                        cityColour = "7,84,117";
                        break;
                    case HAMLET:
                        cityColour = "242,91,41";
                        break;
                    case TOWN:
                        cityColour = "71,35,24";
                        break;
                    case CITY:
                        cityColour = "75,75,75";
                        break;
                    case METROPOLIS:
                        cityColour = "149,255,107";
                        break;
                }
                vb.addProperties(Structs.Property.newBuilder().setKey("rgb_color").setValue(cityColour).build());
                vb.addProperties(Structs.Property.newBuilder().setKey("thickness").setValue(VERTEX_THICKNESS).build());
                vb.addProperties(Structs.Property.newBuilder().setKey("transparency").setValue("100").build());
                
                vertices.add(vb.build());
            }
        }
        
        return Structs.Mesh.newBuilder().addAllVertices(vertices).addAllSegments(segments).addAllPolygons(polygons).build();
    }
}
