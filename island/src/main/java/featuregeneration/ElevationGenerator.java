package featuregeneration;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Random;

import attributes.Attribute;
import attributes.ElevationAttribute;
import attributes.LandAttribute;
import island.Island.Tile;

public class ElevationGenerator extends Generator{

    private ElevationModes mode;

    public ElevationGenerator(ElevationModes mode){
    }

    @Override
    public Set<Class<? extends Attribute>> preRequisiteAttributes() {
        Set<Class<? extends Attribute>> set = new HashSet<>();
        return set;
    }

    @Override
    public Map<Tile, ? extends Attribute> generate(Set<Tile> tiles) {
        HashMap<Tile, ElevationAttribute> attributeLayer = new HashMap<>();
        
        
        if(mode.equals(ElevationModes.MOUNTAIN)){
            determineElevation(1, 1, 1.35, tiles, attributeLayer);
        }

        else if(mode.equals(ElevationModes.HILLS)){
            determineElevation(20, 0.7, 1, tiles, attributeLayer);
        }

        else if (mode.equals(ElevationModes.PLAINS)){
            determineElevation(30, 0.4, 0.7, tiles, attributeLayer);
        }

        return attributeLayer;
    }

    private void determineElevation(int peaksNum, double maxHeight, double slopeOff, Set <Tile> tiles, HashMap<Tile, ElevationAttribute> attributeLayer){
        Random bag = new Random();
        Set <Tile> hills = new HashSet<>();
        for (int j = 0; j < peaksNum; j++){
            Tile hill = null;
            do{
                int randomPeakID = bag.nextInt(tiles.size());
                int i = 0;
                for (Tile tile : tiles){
                    if (i == randomPeakID){
                        hill = tile;
                        break;
                    }
                    i++;
                }
            }
            while(hill.getAttribute(LandAttribute.class).isLand && !hills.contains(hill));
            hills.add(hill);
        }

        for (Tile tile : tiles){
            double minDistance = 100;
            for (Tile hill : hills){
                minDistance = Math.sqrt(Math.pow(tile.getX() - hill.getX(), 2) + Math.pow(tile.getY() - hill.getY(), 2)) < minDistance ? Math.sqrt(Math.pow(tile.getX() - hill.getX(), 2) + Math.pow(tile.getY() - hill.getY(), 2)) : minDistance;
            }

            double elevation = Math.max(maxHeight - minDistance * slopeOff, 0);
            System.out.println(elevation);
            attributeLayer.put(tile, new ElevationAttribute(elevation));
        }
    }
    
    public enum ElevationModes{
        HILLS, PLAINS, MOUNTAIN
    }
}
