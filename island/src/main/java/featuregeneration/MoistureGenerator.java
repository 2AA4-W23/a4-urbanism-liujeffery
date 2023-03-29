package featuregeneration;

import java.util.HashSet;
import java.util.Set;

import attributes.AquiferAttribute;
import attributes.Attribute;
import attributes.LakeAttribute;
import attributes.LandAttribute;
import attributes.MoistureAttribute;
import attributes.MoistureAttribute.Soil;
import island.Tile;

public class MoistureGenerator extends Generator{

    private double minMoisture;
    private double moistureRange;
    private double scalingFactor = 4;

    public MoistureGenerator(double maxMoisture, double minMoisture, Soil soil){
        this.minMoisture = minMoisture;
        this.moistureRange = maxMoisture - minMoisture;
        
        switch(soil){
            case ABSORBANT:
                this.scalingFactor = 2;
                break;
            case STANDARD:
                this.scalingFactor = 4;
                break;
            case PARCHED:
                this.scalingFactor = 6;
                break;
        }
    }

    @Override
    public Set<Class<? extends Attribute>> preRequisiteAttributes() {
        Set<Class<? extends Attribute>> set = new HashSet<>();
        set.add(LakeAttribute.class);
        set.add(AquiferAttribute.class);
        set.add(LandAttribute.class);
        return set;
    }

    @Override
    public MoistureAttribute generate(Set<Tile> tiles) {
        MoistureAttribute attribute = new MoistureAttribute(0);
        for(Tile t : tiles){
            double minDistance = Math.sqrt(2);
            for(Tile other : tiles){
                if(other == t) continue;
                if(other.getAttribute(LandAttribute.class).isLand 
                    && !other.getAttribute(AquiferAttribute.class).isAquifer
                    && !other.getAttribute(LakeAttribute.class).isLake) continue;

                double distance = Math.sqrt(Math.pow(t.getX() - other.getX(), 2) + Math.pow(t.getY() - other.getY(), 2));
                if(distance < minDistance) minDistance = distance;
            }
            if(t.getAttribute(AquiferAttribute.class).isAquifer) minDistance = 0;

            double moisture = Math.max(0, (1 - scalingFactor * minDistance) * moistureRange) + minMoisture;
            
            t.addAttribute(new MoistureAttribute(moisture));
        }
        return attribute;
    }
}
