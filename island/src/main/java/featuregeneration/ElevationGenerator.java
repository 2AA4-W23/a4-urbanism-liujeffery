package featuregeneration;

import java.util.HashSet;
import java.util.Set;
import java.util.Random;

import attributes.Attribute;
import attributes.ElevationAttribute;
import attributes.LandAttribute;
import island.Tile;
import utilities.RandomSingleton;

public class ElevationGenerator extends Generator{

    private ElevationModes mode;

    public ElevationGenerator(ElevationModes mode){
        this.mode = mode;
    }

    @Override
    public Set<Class<? extends Attribute>> preRequisiteAttributes() {
        Set<Class<? extends Attribute>> set = new HashSet<>();
        set.add(LandAttribute.class);
        return set;
    }

    @Override
    public ElevationAttribute generate(Set<Tile> tiles) {
        ElevationAttribute attribute = new ElevationAttribute(0);
        
        
        if(mode.equals(ElevationModes.MOUNTAIN)){
            determineElevation(1, 1, 1.35, 0.2, tiles);
        }

        else if(mode.equals(ElevationModes.HILLS)){
            determineElevation(20, 0.7, 3, 0.1, tiles);
        }

        else if (mode.equals(ElevationModes.PLAINS)){
            determineElevation(30, 0.4, 1.5, 0.05, tiles);
        }

        return attribute;
    }

    private void determineElevation(int peaksNum, double maxHeight, double slopeOff, double randomness, Set <Tile> tiles){
        Random bag = RandomSingleton.getInstance();
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
            while(!hill.getAttribute(LandAttribute.class).isLand && !hills.contains(hill));
            hills.add(hill);
        }

        for (Tile tile : tiles){
            double minDistance = 100;
            for (Tile hill : hills){
                minDistance = Math.sqrt(Math.pow(tile.getX() - hill.getX(), 2) + Math.pow(tile.getY() - hill.getY(), 2)) < minDistance ? Math.sqrt(Math.pow(tile.getX() - hill.getX(), 2) + Math.pow(tile.getY() - hill.getY(), 2)) : minDistance;
            }
            double elevation = (bag.nextDouble() * randomness) - randomness / 2;
            elevation = maxHeight - minDistance * slopeOff + elevation * (1 - minDistance);
            elevation = Math.min(elevation, 1);
            elevation = Math.max(elevation, 0);
            
            tile.addAttribute(new ElevationAttribute(elevation));
        }
    }
    
    public enum ElevationModes{
        HILLS, PLAINS, MOUNTAIN
    }
}
