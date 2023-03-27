package featuregeneration;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import attributes.Attribute;
import attributes.BiomeAttribute;
import attributes.LandAttribute;
import attributes.MoistureAttribute;
import attributes.TemperatureAttribute;
import attributes.BiomeAttribute.Biome;
import island.Island.Tile;

public class BiomeGenerator extends Generator{



    @Override
    public Set<Class<? extends Attribute>> preRequisiteAttributes() {
        Set<Class<? extends Attribute>> set = new HashSet<>();
        set.add(TemperatureAttribute.class);
        set.add(MoistureAttribute.class);
        return set;
    }

    @Override
    public Map<Tile, BiomeAttribute> generate(Set<Tile> tiles) {
        Map<Tile, BiomeAttribute> map = new HashMap<>();
        for(Tile t : tiles){
            if(!t.getAttribute(LandAttribute.class).isLand)  
                map.put(t, new BiomeAttribute(Biome.OCEAN));
            double moisture = t.getAttribute(MoistureAttribute.class).moistureLevel;
            double temperature = t.getAttribute(TemperatureAttribute.class).temperature;
            map.put(t, new BiomeAttribute(selectBiome(moisture, temperature)));
        }
        return map;
    }
    

    private BiomeAttribute.Biome selectBiome(double moisture, double temperature){
        if(moisture > 0.5 && temperature > 0.8)
            return Biome.BEACH;
        else if(moisture > 0.5 && temperature > 0.4)
            return Biome.GRASSLAND;
        else if(temperature > 0.8)
            return Biome.DESERT;
        else if(temperature > 0.2)
            return Biome.TUNDRA;
        else
            return Biome.SNOW;
    }
}
