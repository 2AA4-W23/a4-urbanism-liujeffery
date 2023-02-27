package ca.mcmaster.cas.se2aa4.a2.generator;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;

import org.locationtech.jts.geom.*;
import org.locationtech.jts.triangulate.DelaunayTriangulationBuilder;
import org.locationtech.jts.triangulate.VoronoiDiagramBuilder;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;

public class MeshADT{
    public final PrecisionModel pm;
    public final GeometryFactory gf;
    public final double width;
    public final double height;
    public final double precision;
    public List<Structs.Vertex> vertices;
    public List<Structs.Segment> segments;
    public List<Structs.Polygon> polygons;
    public MeshADT(double width, double height, double precision){
        this.width = width;
        this.height = height;
        this.precision = precision;
        this.pm = new PrecisionModel(precision);
        this.gf = new GeometryFactory(pm);
        this.vertices = new ArrayList<>();
        this.segments = new ArrayList<>();
        this.polygons = new ArrayList<>();

        //create points on each end to make sure our canvas is the right width and height
    }
    public int addVertex(double x, double y){
        //enforce the precision
        Coordinate c1 = new Coordinate(x, y);
        return this.addVertex(c1, "0,0,0");
    }
    public int addVertex(Coordinate c1){
        return this.addVertex(c1, "0,0,0");
    }
    public int addVertex(double x, double y, String colorCode){
        //enforce the precision
        Coordinate c1 = new Coordinate(x, y);
        return this.addVertex(c1, colorCode);
    }
    public int addVertex(Coordinate c1, String colorCode){
        //enforce the precision
        this.pm.makePrecise(c1);

        //check if it's out of bounds
        if(c1.getX() < 0 || this.width < c1.getX() || c1.getY() < 0 || this.height < c1.getY()){
            System.out.println("Attempting to add out of bounds " + c1.getX() + "," + c1.getY());
            return -1;
        }
        //should we clamp it if one of x/y is in bounds?

        //check the point doesn't already exist
        int idx = this.findVertex(c1);
        if(idx != -1){
            System.out.println("trying to add duplicate vertex");
            return idx;
        }
        
        System.out.println("Added vertex at " + c1.getX() + "," + c1.getY());
        this.vertices.add(Structs.Vertex.newBuilder().setX(c1.getX()).setY(c1.getY()).addProperties(Structs.Property.newBuilder().setKey("rgb_color").setValue(colorCode).build()).build());
        return this.vertices.size()-1;
    }

    public int findVertex(double x, double y){
        //enforce the precision
        Coordinate c1 = new Coordinate(x, y);
        return this.findVertex(c1);
    }

    public int findVertex(Coordinate c1){
        this.pm.makePrecise(c1);
        for(int i = 0;i < this.vertices.size(); i++){
            Structs.Vertex v = this.vertices.get(i);
            Coordinate c2 = new Coordinate(v.getX(), v.getY());
            this.pm.makePrecise(c2);
            if(c1.equals2D(c2)){
                return i;
            }
        }
        return -1;
    }

    public int addSegment(int v1Idx, int v2Idx){
        return this.addSegment(v1Idx, v2Idx, "0,0,0");
    }

    public int addSegment(int v1Idx, int v2Idx, String colorCode){
        //check if it's out of bounds
        if(v1Idx < 0 || this.vertices.size() <= v1Idx || v2Idx < 0 || this.vertices.size() <= v2Idx){
            return -1;
        }

        //check if a segment already exists that connects the two vertices
        int idx = this.findSegment(v1Idx, v2Idx);
        if(idx != -1){
            return idx; //if it does, don't add but return the idx of the existing segment
        }

        Structs.Segment s = Structs.Segment.newBuilder().setV1Idx(v1Idx).setV2Idx(v2Idx).addProperties(Structs.Property.newBuilder().setKey("rgb_color").setValue(colorCode).build()).build();
        this.segments.add(s);
        return this.segments.size()-1;
    }

    public int findSegment(int v1Idx, int v2Idx){
        //check the point doesn't already exist
        for(int i = 0;i < this.segments.size(); i++){
            Structs.Segment s = this.segments.get(i);
            if(s.getV1Idx() == v1Idx && s.getV2Idx() == v2Idx){ //compare segments to see if they refer to the same vertices
                return i;
            }
        }
        return -1;
    }

    private int addPolygon(Geometry g){
        if(!g.getGeometryType().equals("Polygon")){
            return -1;
        }

        //find the centroid that this polygon was created around
        Coordinate centroid = g.getCentroid().getCoordinate();
        this.pm.makePrecise(centroid);
        int centroidIdx = this.addVertex(centroid, "255,0,0");
        if(centroidIdx == -1){
            return -1;
        }
        
        //add polygon segments to mesh
        Coordinate coords[] = g.getCoordinates();
        List<Integer> segmentIdxs = new ArrayList<>();
        for(int j = 1; j < coords.length; j++){
            int v1Idx = this.addVertex(coords[j-1], "0,0,0");
            int v2Idx = this.addVertex(coords[j], "0,0,0");
            if(v1Idx == -1 || v2Idx == -1) continue;
            segmentIdxs.add(this.addSegment(v1Idx, v2Idx));
        }

        return this.addPolygon(centroidIdx, segmentIdxs, new ArrayList<Integer>());
    }

    public int addPolygon(int centroidIdx, Iterable<? extends Integer> segmentIdxs, Iterable<? extends Integer> neighbourIdxs){
        Structs.Polygon p = Structs.Polygon.newBuilder().setCentroidIdx(centroidIdx).addAllSegmentIdxs(segmentIdxs).addAllNeighborIdxs(neighbourIdxs).build(); //takes segment and neighbors
        this.polygons.add(p);
        return this.polygons.size()-1;
    }

    public int findPolygon(Coordinate centroidVertex){
        int centroidIdx = this.findVertex(centroidVertex);
        if(centroidIdx == -1){
            return -1;
        }

        for(int i = 0;i < this.polygons.size(); i++){
            if(this.polygons.get(i).getCentroidIdx() == centroidIdx){
                return i;
            }
        }

        return -1;
    }

    public void addVoronoiPolygons(){
        //convert vertices to sites
        List<Coordinate> sites = this.getSitesFromVertices();

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

        //when polygons are added, they add a vertex representing their centroid
        //because of this we want to remove the old vertices the polygons were generated around
        this.vertices.clear();

        //has to add polygons, add polygons that don't have neighbors for now
        for(int i = 0; i < diagram.getNumGeometries(); i++){
            Geometry g = diagram.getGeometryN(i);
            if(g.getGeometryType().equals("Polygon")){
                this.addPolygon(g);
            }
        }
    }

    public void computeNeighbours(){
        //calculate neighbor information
        DelaunayTriangulationBuilder dtb = new DelaunayTriangulationBuilder();
        
        //convert vertices to coordinates
        //TODO: update this to use polygons instead
        List<Coordinate> sites = this.getSitesFromPolygons();
        dtb.setSites(sites);

        Geometry triangles = dtb.getTriangles(this.gf);

        //take the triangles and find the polygons that it references
        for(int i = 0;i < triangles.getNumGeometries(); i++){
            Geometry triangle = triangles.getGeometryN(i);
            List<Integer> neighbourIdxs = new ArrayList<>();
            for(Coordinate c : triangle.getCoordinates()){
                int idx = this.findPolygon(c);
                if(idx == -1){
                    System.out.println("couldn't find " + c.toString());
                    continue;
                }
                neighbourIdxs.add(this.findPolygon(c));
            }
            //remove the last one because it's a duplicate of the first (that's how the coordinates are generated)
            neighbourIdxs.remove(neighbourIdxs.size()-1);

            //update the polygons to include neighbor information
            for(int j = 0;j < neighbourIdxs.size(); j++){
                //store the idx of the segment we want to update
                int currIdx = neighbourIdxs.get(j);
                //remove self from list of neighbors
                neighbourIdxs.remove(j);

                //rebuild the old polygon
                Structs.Polygon old = this.polygons.get(currIdx);
                //remove duplicate neighbours
                Set<Integer> uniqueNeighbourIdxs = new HashSet<>();
                for(Integer idx : neighbourIdxs){
                    uniqueNeighbourIdxs.add(idx);
                }
                for(Integer idx : old.getNeighborIdxsList()){
                    uniqueNeighbourIdxs.add(idx);
                }

                this.polygons.set(currIdx, Structs.Polygon.newBuilder().setCentroidIdx(old.getCentroidIdx()).addAllSegmentIdxs(old.getSegmentIdxsList()).addAllNeighborIdxs(uniqueNeighbourIdxs).build());

                //add back the idx to the list of neighbors
                neighbourIdxs.add(j, currIdx);
            }
        }
    }

    private List<Coordinate> getSitesFromVertices(){
        List<Coordinate> sites = new ArrayList<>();
        for(int i = 0;i < this.vertices.size(); i++){
            Structs.Vertex v = this.vertices.get(i);
            Coordinate c = new Coordinate(v.getX(), v.getY());
            this.pm.makePrecise(c);
            sites.add(c);
        }
        return sites;
    }

    private List<Coordinate> getSitesFromPolygons(){
        List<Coordinate> sites = new ArrayList<>();
        for(int i = 0;i < this.polygons.size(); i++){
            Structs.Polygon p = this.polygons.get(i);
            Structs.Vertex centroid = this.vertices.get(p.getCentroidIdx());
            Coordinate c = new Coordinate(centroid.getX(), centroid.getY());
            this.pm.makePrecise(c);
            sites.add(c);
        }
        return sites;
    }

    public Structs.Mesh makeMesh(){
        return Structs.Mesh.newBuilder().addAllVertices(this.vertices).addAllSegments(this.segments).addAllPolygons(this.polygons).build();
    }
}