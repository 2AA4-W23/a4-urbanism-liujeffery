package attributes;

/**
 * Whether a tile is land or water.
 */
public class BiomeAttribute implements Attribute{
    public enum Whittaker{
        TROPICAL, ARCTIC, TEMPERATE
    }

    public enum Biome{
        BEACH, LAND, OCEAN, GRASSLAND, TUNDRA, SNOW, FOREST, ROCKY_LIGHT, ROCKY_DARK, SHRUBS, ICE, RAINFOREST, SWAMP, FIELD
    }
    public Biome biome;

    /**
     * @param biome the biome of the tile
     */
    public BiomeAttribute(Biome biome){
        this.biome = biome;
    }
}
