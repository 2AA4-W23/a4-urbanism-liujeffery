import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import attributes.LakeAttribute;
import featuregeneration.LakeGenerator;
import island.Island.Tile;

public class LakeGeneratorTest extends GeneratorTest{

    public LakeGeneratorTest(int size){
        super(size);
    }

    @Override
    public void testNumOfElements() {
        int expectedLakes = 5;
        LakeGenerator generator = new LakeGenerator(expectedLakes);

        Map<Tile, LakeAttribute> lakeTiles = generator.generate(super.tiles);

        int actualLakes = 0;
        for (Tile tile : lakeTiles.keySet()){
            if (lakeTiles.get(tile).isLake){
                actualLakes ++;
            }
        }

        assertEquals(expectedLakes, actualLakes);
    }
}
