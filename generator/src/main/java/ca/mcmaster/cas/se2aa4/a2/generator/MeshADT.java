package ca.mcmaster.cas.se2aa4.a2.generator;
import java.util.ArrayList;
import org.locationtech.jts.geom.*;

import ca.mcmaster.cas.se2aa4.a2.io.Structs.*;

public class MeshADT{
    private PrecisionModel pm;
    private GeometryFactory gf;
    private double width;
    private double height;
    private ArrayList<Point> vertices;
    public MeshADT(double width, double height, double precision){
        this.pm = new PrecisionModel(precision);
        this.gf = new GeometryFactory(pm);
        this.width = width;
        this.height = height;
        this.vertices = new ArrayList<>();

        //create points on each end;
        this.addVertex(0, 0);
        this.addVertex(width, height);
    }
    public void addVertex(double x, double y){
        //enforce the precision
        Coordinate c1 = new Coordinate(x, y);
        this.pm.makePrecise(c1);

        //check the point doesn't already exist
        for(int i = 0;i < this.vertices.size(); i++){
            Coordinate c2 = this.vertices.get(i).getCoordinate();
            if(c1.equals2D(c2)){
                System.out.println("trying to add duplicate point");
                return;
            }
        }
        System.out.println("Added point at " + x + "," + y);
        this.vertices.add(this.gf.createPoint(c1));
    }
    public Mesh makeMesh(){
        ArrayList<Vertex> vertices = new ArrayList<>();
        Property color = Property.newBuilder().setKey("rgb_color").setValue("0,0,0").build();
        
        //create vertices
        for(Point v : this.vertices){
            vertices.add(Vertex.newBuilder().setX(v.getX()).setY(v.getY()).addProperties(color).build());
        }

        //TODO: segments and polygons
        return Mesh.newBuilder().addAllVertices(vertices).build();
    }
}