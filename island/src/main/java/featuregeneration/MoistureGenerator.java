package featuregeneration;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import attributes.AquiferAttribute;
import attributes.Attribute;
import attributes.LakeAttribute;
import attributes.LandAttribute;
import attributes.MoistureAttribute;
import island.Island.Tile;

public class MoistureGenerator extends Generator{

    private double maxMoisture;
    private double minMoisture;
    private double moistureRange;

    public MoistureGenerator(double maxMoisture, double minMoisture){
        this.maxMoisture = maxMoisture;
        this.minMoisture = minMoisture;
        this.moistureRange = maxMoisture - minMoisture;
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
    public Map<Tile, MoistureAttribute> generate(Set<Tile> tiles) {
        Map<Tile, MoistureAttribute> map = new HashMap<>();
        for(Tile t : tiles){
            double minDistance = 2;
            for(Tile other : tiles){
                if(other == t) continue;
                if(other.getAttribute(LandAttribute.class).isLand 
                    && !other.getAttribute(AquiferAttribute.class).isAquifer) continue;

                double distance = Math.sqrt(Math.pow(t.getX() - other.getX(), 2) + Math.pow(t.getY() - other.getY(), 2));
                if(distance < minDistance) minDistance = distance;
            }
            
            double moisture = minDistance * minDistance * moistureRange + minMoisture;
            map.put(t, new MoistureAttribute(moisture));
        }
        return map;
    }
    
}
