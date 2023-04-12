import configuration.Configuration;
import featuregeneration.AquiferGenerator;
import featuregeneration.BiomeGenerator;
import featuregeneration.CityGenerator;
import featuregeneration.ElevationGenerator;
import featuregeneration.LakeGenerator;
import featuregeneration.LandGenerator;
import featuregeneration.MoistureGenerator;
import featuregeneration.TemperatureGenerator;
import graphADT.Graph;
import featuregeneration.RiverGenerator;
import island.Island;
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

        Formatter meshFormatter = new Formatter(IO.readMesh(config.inputAddress), config.heatmap);
        
        IslandBuilder ib = new IslandBuilder();
        ib.addGenerator(new LandGenerator(config.shape));
        ib.addGenerator(new ElevationGenerator(config.elevation));
        ib.addGenerator(new LakeGenerator(config.lakes));
        ib.addGenerator(new AquiferGenerator(config.aquifers));
        ib.addGenerator(new TemperatureGenerator(1, 0));
        ib.addGenerator(new MoistureGenerator(1, 0, config.soil));
        ib.addGenerator(new RiverGenerator(config.rivers));
        ib.addGenerator(new BiomeGenerator(config.whittaker));
        ib.addGenerator(new CityGenerator(config.cities));

        Island island = ib.build(meshFormatter.convertToIsland());

        IO.writeMesh(meshFormatter.meshFromIsland(island), config.outputAddress);
    }
}
