package featuregeneration;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import attributes.Attribute;
import attributes.LandAttribute;
import island.Island.Tile;

public class BasicGenerator extends Generator {

    private static final double LAND_RADIUS = 0.4;

    @Override
    public Set<Class<? extends Attribute>> preRequisiteAttributes() {
        Set<Class<? extends Attribute>> set = new HashSet<>();
        return set;
    }

    @Override
    public Map<Tile, LandAttribute> generate(Set<Tile> tiles) {
        HashMap<Tile, LandAttribute> attributeLayer = new HashMap<>();
        for(Tile tile : tiles){
            double dist = Math.pow(tile.getX() - 0.5, 2) + Math.pow(tile.getY() - 0.5, 2);
            boolean isLand = dist > LAND_RADIUS;
            attributeLayer.put(tile, new LandAttribute(isLand));
        }
        return attributeLayer;
    }
}
