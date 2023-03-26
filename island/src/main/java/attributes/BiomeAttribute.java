package attributes;

/**
 * Whether a tile is land or water.
 */
public class BiomeAttribute implements Attribute{
    public enum Biome{
        BEACH
    }
    public Biome biome;

    /**
     * @param isLand Whether the tile is land
     */
    public BiomeAttribute(Biome biome){
        this.biome = biome;
    }
}
