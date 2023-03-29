package featuregeneration;

import java.util.HashSet;
import java.util.Set;

import attributes.Attribute;
import attributes.ElevationAttribute;
import attributes.LandAttribute;
import attributes.TemperatureAttribute;
import island.Tile;

public class TemperatureGenerator extends Generator{
    private double maxTemp;
    private double tempRange;

    public TemperatureGenerator(double maxTemp, double minTemp){
        this.maxTemp = maxTemp;
        this.tempRange = maxTemp - minTemp;
    }

    @Override
    public Set<Class<? extends Attribute>> preRequisiteAttributes() {
        Set<Class<? extends Attribute>> set = new HashSet<>();
        set.add(LandAttribute.class);
        return set;
    }

    @Override
    public TemperatureAttribute generate(Set<Tile> tiles) {
        TemperatureAttribute attribute = new TemperatureAttribute(1);
        for(Tile t : tiles){
            double height = t.getAttribute(ElevationAttribute.class).elevation;

            double temperature = maxTemp - tempRange * height;
            temperature = Math.min(1, temperature);

            t.addAttribute(new TemperatureAttribute(temperature));
        }
        return attribute;
    }
} 