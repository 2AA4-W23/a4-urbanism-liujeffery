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
import island.Tile;

public class BiomeGenerator extends Generator{

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
