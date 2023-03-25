package featuregeneration;

import java.util.Set;

import attributes.Attribute;

public abstract class FeatureGenerator {
    public abstract Set<Class<? extends Attribute>> preRequisiteAttributes();
    public abstract Set<Class<? extends Attribute>> newAttributes();
    public abstract void generate();
}   
