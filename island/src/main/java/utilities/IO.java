package utilities;

import java.io.IOException;

import ca.mcmaster.cas.se2aa4.a2.io.MeshFactory;
import ca.mcmaster.cas.se2aa4.a2.io.Structs;

public class IO {
    public static Structs.Mesh readMesh(String meshAddress){
        try {
            return new MeshFactory().read(meshAddress);
        } catch (IOException e) {
            System.out.println("Error reading input mesh, stacktrace:");
            e.printStackTrace();
            return null;
        }
    }

    public static boolean writeMesh(Structs.Mesh m, String filename){
        try{
            new MeshFactory().write(m, filename);
            return true;
        } catch (IOException e) {
            System.out.println("Error reading input mesh, stacktrace:");
            e.printStackTrace();
            return false;
        }
    }
}
