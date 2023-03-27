package featuregeneration;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import attributes.Attribute;
import attributes.ElevationAttribute;
import attributes.TemperatureAttribute;
import island.Island.Tile;

public class TemperatureGenerator extends Generator{

    private double maxTemp;
    private double minTemp;
    private double tempRange;

    public TemperatureGenerator(double maxTemp, double minTemp){
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.tempRange = maxTemp - minTemp;
    }


    @Override
    public Set<Class<? extends Attribute>> preRequisiteAttributes() {
        Set<Class<? extends Attribute>> set = new HashSet<>();
        set.add(ElevationAttribute.class);
        return set;
    }

    @Override
    public Map<Tile, TemperatureAttribute> generate(Set<Tile> tiles) {
        Map<Tile, TemperatureAttribute> map = new HashMap<>();
        for(Tile t : tiles){
            double height = t.getAttribute(ElevationAttribute.class).elevation;

            double temperature = tempRange * height + minTemp;
            temperature = Math.min(1, temperature);
            
            map.put(t, new TemperatureAttribute(temperature));
        }
        return map;
    }
}
