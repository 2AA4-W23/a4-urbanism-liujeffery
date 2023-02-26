import ca.mcmaster.cas.se2aa4.a2.generator.MeshADT;
import ca.mcmaster.cas.se2aa4.a2.io.MeshFactory;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;

import java.io.IOException;
import java.util.Random;

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

        //create random points
        // Random bag = new Random(69);
        // for(int i = 0; i < 100; i++){
        //     int x = bag.nextInt(width);
        //     int y = bag.nextInt(height);
        //     baseMesh.addVertex(x, y);
        // }

        baseMesh.processVoronoi();
        Mesh myMesh = baseMesh.makeMesh();

        MeshFactory factory = new MeshFactory();
        factory.write(myMesh, args[0]);
    }
}
