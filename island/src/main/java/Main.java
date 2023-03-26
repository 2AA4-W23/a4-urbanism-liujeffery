import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import configuration.Configuration;
import island.Island;
import utilities.Formatter;
import utilities.IO;

/**
 * Hello world!
 *
 */
public class Main 
{
    public static void main( String[] args ) {
        Configuration config = new Configuration(args);
        if(!config.isValid()){
            config.printHelp();
            return;
        }
        Structs.Mesh inputMesh = IO.readMesh(config.inputAddress);
        if(inputMesh == null) return;
        Formatter meshFormatter = new Formatter(inputMesh);
        Island island = meshFormatter.convertAbstractMesh();
    }
}
