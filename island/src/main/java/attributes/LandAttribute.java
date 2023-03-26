package attributes;

/**
 * Whether a tile is land or water.
 */
public class LandAttribute implements Attribute{
    public Boolean isLand;

    /**
     * @param isLand Whether the tile is land
     */
    public LandAttribute(boolean isLand){
        this.isLand = isLand;
    }
}
