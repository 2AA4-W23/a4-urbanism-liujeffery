package featuregeneration;

import java.util.HashSet;
import java.util.Set;

import attributes.AquiferAttribute;
import attributes.Attribute;
import attributes.BiomeAttribute;
import attributes.LakeAttribute;
import attributes.LandAttribute;
import attributes.MoistureAttribute;
import attributes.TemperatureAttribute;
import attributes.BiomeAttribute.Biome;
import attributes.BiomeAttribute.Whittaker;
import island.Tile;

public class BiomeGenerator extends Generator{

    Whittaker whittaker;

    public BiomeGenerator(Whittaker whittaker){
        this.whittaker = whittaker;
    }

    @Override
    public Set<Class<? extends Attribute>> preRequisiteAttributes() {
        Set<Class<? extends Attribute>> set = new HashSet<>();
        set.add(LakeAttribute.class);
        set.add(AquiferAttribute.class);
        set.add(LandAttribute.class);
        set.add(TemperatureAttribute.class);
        set.add(MoistureAttribute.class);
        return set;
    }

    @Override
    public Attribute generate(Set<Tile> tiles) {
        BiomeAttribute attribute = new BiomeAttribute(Biome.BEACH);

        for (Tile tile : tiles){
            BiomeAttribute.Biome tileAttribute = selectBiome(tile.getAttribute(MoistureAttribute.class).moisture, tile.getAttribute(TemperatureAttribute.class).temperature);
            tile.addAttribute(new BiomeAttribute(tileAttribute));
        }

        return attribute;
    }
    
    private BiomeAttribute.Biome selectBiome(double moisture, double temperature){
        switch (whittaker){
            case TEMPERATE:
                if(moisture > 0.6 && temperature > 0.7)
                    return Biome.BEACH;
                else if(moisture > 0.3 && temperature > 0.4)
                    return Biome.GRASSLAND;
                else if(temperature > 0.2)
                    return Biome.FIELD;
                else if(temperature > 0.1)
                    return Biome.ROCKY_LIGHT;
                else
                    return Biome.ROCKY_DARK;
            case ARCTIC:
                if(moisture > 0.8 && temperature > 0.75)
                    return Biome.SHRUBS;
                else if(moisture > 0.7 && temperature > 0.4)
                    return Biome.ICE;
                else if(temperature > 0.2)
                    return Biome.SNOW;
                else if (temperature > 0.1)
                    return Biome.ROCKY_LIGHT;
                else
                    return Biome.SNOW;
            case TROPICAL:
                if(moisture > 0.7 && temperature > 0.7)
                    return Biome.BEACH;
                else if(moisture > 0.6 && temperature > 0.5)
                    return Biome.SWAMP;
                else if(temperature > 0.4)
                    return Biome.RAINFOREST;
                else if(temperature > 0.15)
                    return Biome.GRASSLAND;
                else
                    return Biome.SHRUBS;
            default:
                return Biome.BEACH;
        }
        
    }
}