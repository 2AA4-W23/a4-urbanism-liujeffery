package featuregeneration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Random;

import attributes.Attribute;
import attributes.ElevationAttribute;
import attributes.LandAttribute;
import island.Island.Tile;

public class ElevationGenerator extends Generator{

    private ElevationModes mode;

    public ElevationGenerator(String mode){
        switch (mode){
            case "hills":
                this.mode = ElevationModes.HILLS;
                break;
            case "mountain":
                this.mode = ElevationModes.MOUNTAIN;
                break;
            case "plains":
                this.mode = ElevationModes.PLAINS;
                break;
        }
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
            mountain(tiles, attributeLayer);
        }

        else if(mode.equals(ElevationModes.HILLS)){
            hill(20, tiles, attributeLayer);
        }

        else if (mode.equals(ElevationModes.PLAINS)){
            plains(tiles, attributeLayer);
        }

        return attributeLayer;
    }

    private void mountain(Set <Tile> tiles, HashMap<Tile, ElevationAttribute> attributeLayer){
        Random bag = new Random();
        Tile peak = null;
        do{
            int randomPeakID = bag.nextInt(tiles.size());
            int i = 0;
            for (Tile tile : tiles){
                if (i == randomPeakID){
                    peak = tile;
                    break;
                }
                i++;
            }
        }
        while(peak.getAttribute(LandAttribute.class).isLand);

        double peakX = peak.getX();
        double peakY = peak.getY();

        for (Tile tile : tiles){
            double distance = Math.sqrt(Math.pow((tile.getX() - peakX), 2) + Math.pow((tile.getY() - peakY), 2));
            double elevation = 1 - distance;
            attributeLayer.put(tile, new ElevationAttribute(elevation));
        }
    }

    private void hill(int hillsNum, Set <Tile> tiles, HashMap<Tile, ElevationAttribute> attributeLayer){
        Random bag = new Random();
        Set <Tile> hills = new HashSet<>();
        for (int j = 0; j < hillsNum; j++){
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
            double minDistanceX = 1;
            double minDistanceY = 1;
            for (Tile hill : hills){
                minDistanceX = Math.abs(tile.getX() - hill.getX()) < minDistanceX ? Math.abs(tile.getX() - hill.getX()) : minDistanceX;
                minDistanceY = Math.abs(tile.getY() - hill.getY()) < minDistanceY ? Math.abs(tile.getY() - hill.getY()) : minDistanceY;
            }

            double distance = Math.sqrt(Math.pow(minDistanceX, 2) + Math.pow(minDistanceY, 2));
            double elevation = (int)((0.3 - distance));
            attributeLayer.put(tile, new ElevationAttribute(elevation));
        }
    }

    private void plains(Set <Tile> tiles, HashMap<Tile, ElevationAttribute> attributeLayer){
        
    }
    
    private enum ElevationModes{
        HILLS, PLAINS, MOUNTAIN
    }
}
