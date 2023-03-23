import ca.mcmaster.cas.se2aa4.a2.io.MeshFactory;
import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import configuration.Configuration;

import java.io.IOException;


import island.IslandMesh;
import utilities.IO;

/**
 * Hello world!
 *
 */
public class Main 
{
    public static void main( String[] args ) {
        Configuration config = new Configuration(args);
        Structs.Mesh inputMesh = IO.readMesh(config.inputAddress);

        IslandMesh islandMesh = new IslandMesh(inputMesh, config.mode);
    }
}
