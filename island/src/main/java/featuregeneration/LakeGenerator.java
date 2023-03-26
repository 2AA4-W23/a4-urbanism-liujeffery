package featuregeneration;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import attributes.Attribute;
import attributes.ElevationAttribute;
import attributes.LakeAttribute;
import attributes.LandAttribute;
import island.Island.Tile;

public class LakeGenerator extends Generator{
    int lakes;

    public LakeGenerator(int lakes){
        this.lakes = lakes;
    }

    @Override
    public Set<Class<? extends Attribute>> preRequisiteAttributes() {
        Set<Class<? extends Attribute>> set = new HashSet<>();
        return set;
    }

    @Override
    public Map<Tile, ? extends Attribute> generate(Set<Tile> tiles) {
        HashMap<Tile, LakeAttribute> attributeLayer = new HashMap<>();
        Set <Tile> lakesList = new HashSet<>();

        Random bag = new Random();
        
        for (int j = 0; j < lakes; j++){
            Tile lake = null;
            do{
                int randomLakeID = bag.nextInt(tiles.size());
                int i = 0;
                //for loop to choose source(s) of lake
                for (Tile tile : tiles){
                    if (i == randomLakeID){
                        lake = tile;
                        break;
                    }
                    i++;
                }
            }
            while(!lake.getAttribute(LandAttribute.class).isLand || checkIfCoastal(lake));

            //expanding lake based on if it is land and elevation is lower
            expandLake(lakesList, lake);
            
            System.out.println(lakesList.size());
        }

        for (Tile tile : tiles){
            attributeLayer.put(tile, new LakeAttribute(lakesList.contains(tile)));

        }

        return attributeLayer;
    }
    
    public void expandLake(Set <Tile> lakesList, Tile lake){
        for (Tile tile : lake.getNeighbours()){
            if (!checkIfCoastal(tile) && tile.getAttribute(ElevationAttribute.class).elevation < lake.getAttribute(ElevationAttribute.class).elevation){
                lakesList.add(tile);
                expandLake(lakesList, tile);
            }
        }
        lakesList.add(lake);
    }

    public boolean checkIfCoastal(Tile tile){
        for (Tile t : tile.getNeighbours()){
            if (t.getAttribute(LandAttribute.class).isLand){
                return true;
            }
        }
        return false;
    }
}
