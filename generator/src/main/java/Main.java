import ca.mcmaster.cas.se2aa4.a2.generator.MeshADT;
import ca.mcmaster.cas.se2aa4.a2.io.MeshFactory;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        MeshADT generator = new MeshADT(500, 500, 100);
        for(int i = 0;i <= 500; i+=20){
            for(int j = 0;j <= 500; j+=20){
                generator.addVertex(i, j);
            }
        }
        Mesh myMesh = generator.makeMesh();

        MeshFactory factory = new MeshFactory();
        factory.write(myMesh, args[0]);
    }

}
