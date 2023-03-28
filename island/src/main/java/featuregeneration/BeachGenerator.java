package featuregeneration;

import java.util.HashSet;
import java.util.Set;

import attributes.Attribute;
import attributes.LandAttribute;
import attributes.BiomeAttribute;
import attributes.BiomeAttribute.Biome;
import island.Tile;

public class BeachGenerator extends Generator {
    @Override
    public Set<Class<? extends Attribute>> preRequisiteAttributes() {
        Set<Class<? extends Attribute>> set = new HashSet<>();
        set.add(LandAttribute.class);
        return set;
    }

    @Override
    public BiomeAttribute generate(Set<Tile> tiles) {
        BiomeAttribute attribute = new BiomeAttribute(Biome.BEACH);
        for(Tile tile : tiles){
            if(tile.getAttribute(LandAttribute.class).isLand){
                if(tile.getAttribute(BiomeAttribute.class) == null)
                    tile.addAttribute(new BiomeAttribute(Biome.LAND));
                continue;
            }

            tile.addAttribute(new BiomeAttribute(Biome.OCEAN));

            //if it's a water tile, mark the surrounding land tiles as beach
            for(Tile neighbor : tile.getNeighbours()){
                if(neighbor.getAttribute(LandAttribute.class).isLand){
                    tile.addAttribute(new BiomeAttribute(Biome.BEACH));
                }
            }
        }
        return attribute;
    }
}
