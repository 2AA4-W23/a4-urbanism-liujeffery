package ca.mcmaster.cas.se2aa4.a2.generator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Random;

import ca.mcmaster.cas.se2aa4.a2.io.Structs.Segment;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Vertex;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Property;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Polygon;

public class MeshADT {
    private ArrayList<Vertex> vertices = new ArrayList<>();
    private ArrayList<Segment> segments = new ArrayList<>();
    private ArrayList<Polygon> polygons = new ArrayList<>();

    //use default constructor
    public MeshADT(){
        this.vertices = new ArrayList<>();
        this.segments = new ArrayList<>();
        this.polygons = new ArrayList<>();
    }

    public Mesh makeMesh(){
        return Mesh.newBuilder().addAllVertices(this.vertices).addAllSegments(this.segments).addAllPolygons(this.polygons).build();
    }

    //TODO: change these to doubles / however we're keeping track of precision
    public int addSquare(Coordinate center, int sideLength, String colorCode){
        //add the four vertices
        int c = addVertex(new Coordinate(center.x, center.y), colorCode);
        int tl = addVertex(new Coordinate(center.x - (sideLength/2), center.y - (sideLength/2)), colorCode);
        int tr = addVertex(new Coordinate(center.x + (sideLength/2), center.y - (sideLength/2)), colorCode);
        int bl = addVertex(new Coordinate(center.x - (sideLength/2), center.y + (sideLength/2)), colorCode);
        int br = addVertex(new Coordinate(center.x + (sideLength/2), center.y + (sideLength/2)), colorCode);

        //add the segments
        int top = addSegment(tl, tr, colorCode);
        int right = addSegment(tr, br, colorCode);
        int bottom = addSegment(bl, br, colorCode);
        int left = addSegment(tl, bl, colorCode);

        ArrayList<Integer> segmentIdxs = new ArrayList<Integer>(Arrays.asList(top, right, bottom, left));

        //TODO: add neighbor idx
        return addPolygon(segmentIdxs, colorCode);
    }

    public int addPolygon(Iterable<? extends Integer> segmentIdxs, String colorCode){
        Property color = Property.newBuilder().setKey("rgb_color").setValue(colorCode).build();
        Polygon polygon = Polygon.newBuilder().addAllSegmentIdxs(segmentIdxs).build();
        this.polygons.add(polygon);
        return this.polygons.size();
    }
    public int addSegment(int V1Idx, int V2Idx, String colorCode){
        Property color = Property.newBuilder().setKey("rgb_color").setValue(colorCode).build();
        Segment s = Segment.newBuilder().setV1Idx(V1Idx).setV2Idx(V2Idx).addProperties(color).build();
        this.segments.add(s);
        return this.segments.size()-1;
    }
    public int addVertex(Coordinate c, String colorCode){
        Property color = Property.newBuilder().setKey("rgb_color").setValue(colorCode).build();
        Vertex v = Vertex.newBuilder().setX(c.x).setY(c.y).addProperties(color).build();
        this.vertices.add(v);
        return this.vertices.size()-1;
    }

}
