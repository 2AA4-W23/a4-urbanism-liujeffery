import configuration.Configuration;
import featuregeneration.AquiferGenerator;
import featuregeneration.BeachGenerator;
import featuregeneration.ElevationGenerator;
import featuregeneration.LakeGenerator;
import featuregeneration.LandGenerator;
import island.IslandBuilder;
import island.IslandBuilder.MissingAttributeError;
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

        Formatter meshFormatter = new Formatter(IO.readMesh(config.inputAddress));
        
        IslandBuilder ib = new IslandBuilder();
        ib.addGenerator(new LandGenerator(config.shape));
        ib.addGenerator(new BeachGenerator());
        ib.addGenerator(new ElevationGenerator(config.elevation));
        ib.addGenerator(new LakeGenerator(config.lakes));
        ib.addGenerator(new AquiferGenerator(config.aquifers));

        IO.writeMesh(meshFormatter.meshFromIsland(ib.build(meshFormatter.convertToIsland())), config.outputAddress);
        System.out.println("Success");
    }
}
