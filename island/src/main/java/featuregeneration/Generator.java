package featuregeneration;

import java.util.Set;

import attributes.Attribute;
import island.Tile;

public abstract class Generator {
    public abstract Set<Class<? extends Attribute>> preRequisiteAttributes();
    public abstract Attribute generate(Set<Tile> tiles);
}   
