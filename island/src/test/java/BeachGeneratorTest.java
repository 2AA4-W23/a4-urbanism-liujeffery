import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import attributes.BiomeAttribute;
import attributes.BiomeAttribute.Biome;
import featuregeneration.BeachGenerator;
import island.Island.Tile;

public class BeachGeneratorTest extends GeneratorTest{

    public BeachGeneratorTest (int size){
        super(size);
    }

    @Override
    public void testNumOfElements() {
        BeachGenerator generator = new BeachGenerator();

        Map<Tile, BiomeAttribute> beachTiles = generator.generate(super.tiles);

        boolean oceanNeighboursValid = true;

        for (Tile tile : beachTiles.keySet()){
            if (beachTiles.get(tile).biome.equals(Biome.BEACH)){
                for (Tile t : tile.getNeighbours()){
                    if(t.getAttribute(BiomeAttribute.class).biome.equals(Biome.LAND)){
                        oceanNeighboursValid = false;
                        break;
                    }
                }
            }
        }
        
        assertTrue(oceanNeighboursValid);
    }
    
}
