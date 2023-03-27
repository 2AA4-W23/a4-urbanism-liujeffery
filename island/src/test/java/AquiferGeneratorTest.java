import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import attributes.AquiferAttribute;
import featuregeneration.AquiferGenerator;
import island.Island.Tile;

public class AquiferGeneratorTest extends GeneratorTest{

    public AquiferGeneratorTest(int size){
        super(size);
    }

    @Override
    public void testNumOfElements() {
        int expectedAquifers = 5;

        AquiferGenerator generator = new AquiferGenerator(expectedAquifers);

        Map<Tile, AquiferAttribute> aquiferTiles = generator.generate(super.tiles);

        int actualAquifers = 0;
        for (Tile tile : aquiferTiles.keySet()){
            if (aquiferTiles.get(tile).isAquifer){
                actualAquifers ++;
            }
        }

        assertEquals(expectedAquifers, actualAquifers);
    }
}
