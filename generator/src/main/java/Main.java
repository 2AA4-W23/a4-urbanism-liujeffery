import ca.mcmaster.cas.se2aa4.a2.generator.MeshADT;
import ca.mcmaster.cas.se2aa4.a2.io.MeshFactory;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;

import java.io.IOException;
import java.util.ArrayList;

import org.locationtech.jts.geom.*;
import org.locationtech.jts.triangulate.VoronoiDiagramBuilder;

public class Main {

    public static void main(String[] args) throws IOException {
        MeshADT baseMesh = new MeshADT(500, 500, 100);
        int width = 500, height = 500, squareSize = 20;
        //create a uniform grid to test
        for(int i = 0;i <= width; i+=squareSize){
            for(int j = 0;j <= height; j+=squareSize){
                baseMesh.addVertex(i, j);
            }
        }
        baseMesh.processVoronoi();
        Mesh myMesh = baseMesh.makeMesh();
        // Mesh myMesh = baseMesh.test();
        // Mesh myMesh = createVoronoiMesh(baseMesh);

        MeshFactory factory = new MeshFactory();
        factory.write(myMesh, args[0]);
    }

    // public static Mesh createVoronoiMesh(MeshADT baseMesh){
    //     MeshADT tempMesh = new MeshADT(baseMesh.width, baseMesh.height, baseMesh.precision);
        
    //     //create a voronoi mesh given a mesh with points in it;
    //     VoronoiDiagramBuilder diagramBuilder = new VoronoiDiagramBuilder();
    //     ArrayList<Coordinate> sites = new ArrayList<>();
    //     for(int i = 0; i < baseMesh.vertices.size(); i++){
    //         Point p = baseMesh.vertices.get(i);
    //         Coordinate c = p.getCoordinate();
    //         sites.add(c);
    //     }
    //     diagramBuilder.setSites(sites);
        

    //     Coordinate[] coords = new Coordinate[5];
    //     coords[0] = new Coordinate(0, 0);
    //     coords[1] = new Coordinate(tempMesh.width, 0);
    //     coords[2] = new Coordinate(tempMesh.width, tempMesh.height);
    //     coords[3] = new Coordinate(0, tempMesh.height);
    //     coords[4] = coords[0];
    //     LinearRing ring = tempMesh.gf.createLinearRing(coords);
    //     Polygon boundary = tempMesh.gf.createPolygon(ring, null);

    //     Geometry voronoiDiagram = diagramBuilder.getDiagram(baseMesh.gf).intersection(boundary);

    //     System.out.println(voronoiDiagram.toString());
    //     //go through each polygon and create a new mesh with the segments added
    //     for(int i = 0;i < voronoiDiagram.getNumGeometries(); i++){
    //         Geometry p = voronoiDiagram.getGeometryN(i);
    //         // System.out.println(p);
    //         // Coordinate coords[] = p.getCoordinates();
    //         // for(int j = 1; j < coords.length; j++){
    //         //     int v1Idx = tempMesh.addVertex(coords[j-1]);
    //         //     int v2Idx = tempMesh.addVertex(coords[j]);
    //         // }
    //         tempMesh.addVertex(p.getCentroid().getCoordinate());
    //     }

    //     return tempMesh.makeMesh();
    // }

}
