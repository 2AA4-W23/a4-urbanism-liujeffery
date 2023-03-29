package attributes;

public class TemperatureAttribute implements Attribute{
    public double temperature;

    /**
     * @param temperature the temperature of the tile
     */
    public TemperatureAttribute(double temperature){
        this.temperature = temperature;
    }
}
