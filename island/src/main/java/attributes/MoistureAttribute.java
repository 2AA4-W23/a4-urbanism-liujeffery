package attributes;

public class MoistureAttribute implements Attribute{
    public double moisture;

    public enum Soil{
        ABSORBANT, STANDARD, PARCHED
    }

    public MoistureAttribute(double moisture){
        this.moisture = moisture;
    }
}
