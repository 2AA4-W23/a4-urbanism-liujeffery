package featuregeneration;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import attributes.Attribute;
import attributes.LandAttribute;
import attributes.BiomeAttribute;
import attributes.BiomeAttribute.Biome;
import island.Island.Tile;

public class BeachGenerator extends Generator {
    @Override
    public Set<Class<? extends Attribute>> preRequisiteAttributes() {
        Set<Class<? extends Attribute>> set = new HashSet<>();
        set.add(LandAttribute.class);
        return set;
    }

    @Override
    public Map<Tile, BiomeAttribute> generate(Set<Tile> tiles) {
        HashMap<Tile, BiomeAttribute> attributeLayer = new HashMap<>();
        for(Tile tile : tiles){
            if(tile.getAttribute(LandAttribute.class).isLand){
                continue;
            }
            
            //if it's a water tile, mark the surrounding land tiles as beach
            for(Tile neighbor : tile.getNeighbours()){
                if(neighbor.getAttribute(LandAttribute.class).isLand){
                    attributeLayer.put(neighbor, new BiomeAttribute(Biome.BEACH));
                }
            }
        }
        return attributeLayer;
    }
}
