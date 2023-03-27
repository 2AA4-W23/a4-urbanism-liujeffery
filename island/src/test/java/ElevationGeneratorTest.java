import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import attributes.ElevationAttribute;
import featuregeneration.ElevationGenerator;
import featuregeneration.ElevationGenerator.ElevationModes;
import island.Island.Tile;

public class ElevationGeneratorTest extends GeneratorTest{

    public ElevationGeneratorTest(int size){
        super(size);
    }
    @Override
    public void testNumOfElements() {
        ElevationGenerator generator = new ElevationGenerator(ElevationModes.MOUNTAIN);

        Map<Tile, ElevationAttribute> elevationTiles = generator.generate(super.tiles);
        boolean validElevations = true;
        for (Tile tile : elevationTiles.keySet()){
            double elevation = elevationTiles.get(tile).elevation;
            if (elevation < 0 || elevation > 1){
                validElevations = false;
                break;
            }
        }

        assertTrue(validElevations);
    }
}
