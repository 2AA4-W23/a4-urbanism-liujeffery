package attributes;

public class LakeAttribute implements Attribute{
    public boolean isLake;

    /**
     * @param isLake determines if tile is lake or not
     */
    public LakeAttribute(boolean isLake){
        this.isLake = isLake;
    }
}
