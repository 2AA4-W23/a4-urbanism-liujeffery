import configuration.Configuration;
import featuregeneration.AquiferGenerator;
import featuregeneration.BiomeGenerator;
import featuregeneration.ElevationGenerator;
import featuregeneration.LakeGenerator;
import featuregeneration.LandGenerator;
import featuregeneration.MoistureGenerator;
import featuregeneration.TemperatureGenerator;
import featuregeneration.RiverGenerator;
import island.IslandBuilder;
import island.IslandBuilder.MissingAttributeError;
import utilities.Formatter;
import utilities.IO;

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
        ib.addGenerator(new ElevationGenerator(config.elevation));
        ib.addGenerator(new LakeGenerator(config.lakes));
        ib.addGenerator(new AquiferGenerator(config.aquifers));
        ib.addGenerator(new TemperatureGenerator(1, 0));
        ib.addGenerator(new MoistureGenerator(1, 0));
        ib.addGenerator(new RiverGenerator(config.rivers));
        ib.addGenerator(new BiomeGenerator(config.whittaker));

        IO.writeMesh(meshFormatter.meshFromIsland(ib.build(meshFormatter.convertToIsland())), config.outputAddress);
    }
}
