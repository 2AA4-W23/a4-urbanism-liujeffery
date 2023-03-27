import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import attributes.LandAttribute;
import featuregeneration.BasicGenerator;
import island.Island.Tile;

public class BasicGeneratorTest extends GeneratorTest{

    public BasicGeneratorTest (int size){
        super(size);
    }

    @Override
    public void testNumOfElements() {
        
        BasicGenerator generator = new BasicGenerator();
        int numOfLand = 0;
        for (Tile tile : super.tiles){
            double dist = Math.pow(tile.getX() - 0.5, 2) + Math.pow(tile.getY() - 0.5, 2);
            if (dist < 0.4 * 0.4){
                numOfLand ++;
            }
        }

        Map<Tile, LandAttribute> lakeTiles = generator.generate(super.tiles);
        int actualLand = 0;
        for (Tile tile : lakeTiles.keySet()){
            if (lakeTiles.get(tile).isLand){
                actualLand ++;
            }
        }

        assertEquals(actualLand, numOfLand);
    }
    
}
