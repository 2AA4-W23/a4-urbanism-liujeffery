package featuregeneration;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import attributes.AquiferAttribute;
import attributes.Attribute;
import attributes.LandAttribute;
import island.Island.Tile;

public class AquiferGenerator extends Generator{
    int aquifers;

    public AquiferGenerator(int aquifers){
        this.aquifers = aquifers;
    }

    @Override
    public Set<Class<? extends Attribute>> preRequisiteAttributes() {
        Set<Class<? extends Attribute>> set = new HashSet<>();
        set.add(LandAttribute.class);
        return set;
    }

    @Override
    public Map<Tile, AquiferAttribute> generate(Set<Tile> tiles) {
        HashMap<Tile, AquiferAttribute> attributeLayer = new HashMap<>();
        Set <Tile> aquifersList = new HashSet<>();
        Random bag = new Random();

        for (int j = 0; j < aquifers; j++){
            Tile aquifer = null;
            do{
                int randomAquiferID = bag.nextInt(tiles.size());
                int i = 0;
                for (Tile tile : tiles){
                    if (i == randomAquiferID){
                        aquifer= tile;
                        break;
                    }
                    i++;
                }
            }
            while(!aquifer.getAttribute(LandAttribute.class).isLand || aquifersList.contains(aquifer));

            aquifersList.add(aquifer);
        }

        for (Tile tile : tiles){
            attributeLayer.put(tile, new AquiferAttribute(aquifersList.contains(tile)));
        }

        return attributeLayer;
    }
}
