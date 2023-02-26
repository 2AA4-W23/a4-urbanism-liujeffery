package ca.mcmaster.cas.se2aa4.a2.generator;
import java.util.ArrayList;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.triangulate.VoronoiDiagramBuilder;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;

public class MeshADT{
    public final PrecisionModel pm;
    public final GeometryFactory gf;
    public final double width;
    public final double height;
    public final double precision;
    public ArrayList<Structs.Vertex> vertices;
    public ArrayList<Structs.Segment> segments;
    public MeshADT(double width, double height, double precision){
        this.width = width;
        this.height = height;
        this.precision = precision;
        this.pm = new PrecisionModel(precision);
        this.gf = new GeometryFactory(pm);
        this.vertices = new ArrayList<>();
        this.segments = new ArrayList<>();

        //create points on each end to make sure our canvas is the right width and height
    }
    public int addVertex(double x, double y){
        //enforce the precision
        Coordinate c1 = new Coordinate(x, y);
        return this.addVertex(c1);
    }
    public int addVertex(Coordinate c1){
        return this.addVertex(c1, "0,0,0");
    }
    public int addVertex(Coordinate c1, String colorCode){
        //enforce the precision
        this.pm.makePrecise(c1);

        //check if it's out of bounds
        if(c1.getX() < 0 || this.width < c1.getX() || c1.getY() < 0 || this.height < c1.getY()){
            System.out.println("Attempting to add out of bounds " + c1.getX() + "," + c1.getY());
            return -1;
        }
        //should we clamp if it one of them is in bounds?

        //check the point doesn't already exist
        for(int i = 0;i < this.vertices.size(); i++){
            Structs.Vertex v = this.vertices.get(i);
            Coordinate c2 = new Coordinate(v.getX(), v.getY());
            this.pm.makePrecise(c2);
            if(c1.equals2D(c2)){
                System.out.println("trying to add duplicate vertex");
                return i;
            }
        }

        System.out.println("Added vertex at " + c1.getX() + "," + c1.getY());
        this.vertices.add(Structs.Vertex.newBuilder().setX(c1.getX()).setY(c1.getY()).addProperties(Structs.Property.newBuilder().setKey("rgb_color").setValue(colorCode).build()).build());
        return this.vertices.size()-1;
    }

    public int addSegment(int v1Idx, int v2Idx){
        //check if it's out of bounds
        if(v1Idx < 0 || this.vertices.size() <= v1Idx || v2Idx < 0 || this.vertices.size() <= v2Idx){
            return -1;
        }

        //check the point doesn't already exist
        for(int i = 0;i < this.segments.size(); i++){
            Structs.Segment s = this.segments.get(i);
            if(s.getV1Idx() == v1Idx && s.getV2Idx() == v2Idx){
                return i;
            }
        }
        Structs.Segment s =Structs.Segment.newBuilder().setV1Idx(v1Idx).setV2Idx(v2Idx).addProperties(Structs.Property.newBuilder().setKey("rgb_color").setValue("0,0,0").build()).build();
        this.segments.add(s);
        return this.segments.size()-1;
    }

    public void processVoronoi(){
        //convert vertices to sites
        ArrayList<Coordinate> sites = new ArrayList<>();
        for(int i = 0;i < this.vertices.size(); i++){
            Structs.Vertex v = this.vertices.get(i);
            Coordinate c = new Coordinate(v.getX(), v.getY());
            this.pm.makePrecise(c);
            sites.add(c);
        }

        VoronoiDiagramBuilder vdb = new VoronoiDiagramBuilder();
        vdb.setSites(sites);
        Coordinate bounds[] = {
            new Coordinate(0, 0),
            new Coordinate(this.width, 0),
            new Coordinate(this.width, this.height),
            new Coordinate(0, this.height),
            new Coordinate(0, 0),
        };
        Geometry diagram = vdb.getDiagram(this.gf).intersection(this.gf.createPolygon(bounds));

        //convert polygons to segments
        for(int i = 0; i < diagram.getNumGeometries(); i++){
            Geometry g = diagram.getGeometryN(i);
            System.out.println(g.toString());
            Coordinate coords[] = g.getCoordinates();
            for(int j = 1; j < coords.length; j++){
                int v1Idx = this.addVertex(coords[j-1], "100,100,100");
                int v2Idx = this.addVertex(coords[j], "100,100,100");
                if(v1Idx == -1 || v2Idx == -1) continue;
                this.addSegment(v1Idx, v2Idx);
            }
        }
    }

    public Structs.Mesh makeMesh(){
        return Structs.Mesh.newBuilder().addAllVertices(this.vertices).addAllSegments(this.segments).build();
    }
}