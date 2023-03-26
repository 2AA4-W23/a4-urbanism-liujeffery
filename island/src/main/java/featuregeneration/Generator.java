package featuregeneration;

import java.util.Map;
import java.util.Set;

import attributes.Attribute;
import island.Island.Tile;

public abstract class Generator {
    public abstract Set<Class<? extends Attribute>> preRequisiteAttributes();
    public abstract Map<Tile, ? extends Attribute> generate(Set<Tile> tiles);
}   
