package attributes;

public class AquiferAttribute implements Attribute{
    public boolean isAquifer;

      /**
     * @param isAquifer is the tile an aquifer
     */
    public AquiferAttribute(boolean isAquifer){
        this.isAquifer = isAquifer;
    }
}
