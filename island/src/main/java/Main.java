import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import featuregeneration.BasicGenerator;
import island.Island;
import island.IslandBuilder;
import island.IslandBuilder.MissingAttributeError;
import utilities.Configuration;
import utilities.Formatter;
import utilities.IO;

/**
 * Hello world!
 *
 */
public class Main 
{
    public static void main( String[] args ) throws MissingAttributeError {
        Configuration config = new Configuration(args);
        if(!config.isValid()){
            config.printHelp();
            return;
        }
        Structs.Mesh inputMesh = IO.readMesh(config.inputAddress);
        if(inputMesh == null) return;
        Formatter meshFormatter = new Formatter(inputMesh);
        Island island = meshFormatter.convertToIsland();
        
        IslandBuilder ib = new IslandBuilder();
        ib.addGenerator(new BasicGenerator());

        ib.build(island);
        IO.writeMesh(meshFormatter.meshFromIsland(island), config.outputAddress);
        System.out.println("Success");
    }
}
