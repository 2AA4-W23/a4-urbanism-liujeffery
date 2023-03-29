package attributes;

/**
 * Whether a tile is land or water.
 */
public class BiomeAttribute implements Attribute{
    public enum Biome{
        BEACH, LAND, OCEAN, GRASSLAND, TUNDRA, SNOW, DESERT, FOREST
    }
    public Biome biome;

    /**
     * @param biome the biome of the tile
     */
    public BiomeAttribute(Biome biome){
        this.biome = biome;
    }
}
