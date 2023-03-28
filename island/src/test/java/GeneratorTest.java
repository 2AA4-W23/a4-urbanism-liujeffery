import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import attributes.AquiferAttribute;
import attributes.BiomeAttribute;
import attributes.ElevationAttribute;
import attributes.LakeAttribute;
import attributes.LandAttribute;
import attributes.BiomeAttribute.Biome;
import featuregeneration.AquiferGenerator;
import featuregeneration.BeachGenerator;
import featuregeneration.ElevationGenerator;
import featuregeneration.LakeGenerator;
import featuregeneration.LandGenerator;
import featuregeneration.ElevationGenerator.ElevationModes;
import featuregeneration.LandGenerator.Shapes;
import island.Tile;
import island.IslandBuilder.MissingAttributeError;

public class GeneratorTest {
    @Test
    @Order (3)
    public void landGenTest() throws MissingAttributeError{
        //land generator test
        int water = 0;
        int land = 0;
        
        Set<Tile> tiles = new HashSet<>();

        for (int i = 0; i < 15; i++){
            tiles.add(new Tile(i, i/15.0, i/15.0));
        }

        LandGenerator landGen = new LandGenerator(Shapes.CIRCLE);
        landGen.generate(tiles);

        for (Tile tile : tiles){
            if (tile.getAttribute(LandAttribute.class).isLand){
                land ++;
            }
            else{
                water ++;
            }
        }
        assert(tiles.size() == water + land);
    }

    @Test
    @Order (4)
    public void beachGenTest() throws MissingAttributeError{
        //beach generator test
        Set<Tile> tiles = new HashSet<>();

        for (int i = 0; i < 15; i++){
            Tile tile = new Tile(i, i/15.0, i/15.0);
            tile.addAttribute(new LandAttribute(i % 2 == 0));
            tiles.add(tile);
        }

        BeachGenerator beachGen = new BeachGenerator();
        beachGen.generate(tiles);

        for (Tile tile : tiles){
            if (tile.getAttribute(BiomeAttribute.class).biome.equals(Biome.OCEAN)){
                for (Tile t : tile.getNeighbours()){
                    Biome tileBiome = t.getAttribute(BiomeAttribute.class).biome;
                    assert(tileBiome.equals(Biome.OCEAN) || tileBiome.equals(Biome.BEACH));
                }
            }
        }
    }

    @Test
    @Order (5)
    public void elevationGenTest() throws MissingAttributeError{
        //elevation test
        Set<Tile> tiles = new HashSet<>();

        for (int i = 0; i < 15; i++){
            Tile tile = new Tile(i, i/15.0, i/15.0);
            tile.addAttribute(new LandAttribute(i % 2 == 0));
            tiles.add(tile);
        }

        ElevationGenerator elevationGen = new ElevationGenerator(ElevationModes.MOUNTAIN);
        elevationGen.generate(tiles);

        for (Tile tile : tiles){
            double elevation = tile.getAttribute(ElevationAttribute.class).elevation;
            assert(elevation <= 1 && elevation >= 0);
        }
    }

    @Test
    @Order (6)
    public void lakeGenTest() throws MissingAttributeError{
        //lakes test
        int lakes = 5;

        Set<Tile> tiles = new HashSet<>();

        for (int i = 0; i < 15; i++){
            Tile tile = new Tile(i, i/15.0, i/15.0);
            tile.addAttribute(new LandAttribute(i % 2 == 0));
            tiles.add(tile);
        }

        LakeGenerator lakeGen = new LakeGenerator(lakes);
        lakeGen.generate(tiles);

        for (Tile tile : tiles){
            if (tile.getAttribute(LakeAttribute.class).isLake){
                for (Tile t : tile.getNeighbours()){
                    assert(t.getAttribute(LandAttribute.class).isLand);
                }
            }
        }
    }

    @Test
    @Order (7)
    public void aquiferGenTest() throws MissingAttributeError{
        int aquifersExpected = 3;
        int aquifersActual = 0;

        Set<Tile> tiles = new HashSet<>();

        for (int i = 0; i < 15; i++){
            Tile tile = new Tile(i, i/15.0, i/15.0);
            tile.addAttribute(new LandAttribute(i % 2 == 0));
            tiles.add(tile);
        }

        AquiferGenerator aquiferGen = new AquiferGenerator(aquifersExpected);
        aquiferGen.generate(tiles);

        for (Tile tile : tiles){
            if (tile.getAttribute(AquiferAttribute.class).isAquifer){
                aquifersActual ++;
            }
        }
        assert(aquifersExpected == aquifersActual);
    }
}
