package featuregeneration;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import attributes.Attribute;
import attributes.CityAttribute;
import attributes.LakeAttribute;
import attributes.LandAttribute;
import attributes.CityAttribute.Cities;
import island.Tile;
import utilities.RandomSingleton;

public class CityGenerator extends Generator{
    int cities;
    
    public CityGenerator(int cities){
        this.cities = cities;
    }

    @Override
    public Set<Class<? extends Attribute>> preRequisiteAttributes() {
        Set<Class<? extends Attribute>> set = new HashSet<>();
        set.add(LakeAttribute.class);
        set.add(LandAttribute.class);
        return set;
    }

    @Override
    public Attribute generate(Set<Tile> tiles) {
        CityAttribute attribute = new CityAttribute(Cities.NONE);
        Set<Tile> citiesList = new HashSet<>();
        Random bag = RandomSingleton.getInstance();

        
        boolean isValid = false;
        for (int i = 0; i < cities; i++){
            Tile city = null;
            do{
                int randomCityID = bag.nextInt(tiles.size());
                int j = 0;
                for (Tile tile : tiles){
                    if (j == randomCityID){
                        city = tile;
                        break;
                    }
                    j++;
                }
                isValid = city.getAttribute(LandAttribute.class).isLand && !city.getAttribute(LakeAttribute.class).isLake & !citiesList.contains(city);
            }
            while(!isValid);

            citiesList.add(city);
        }

        for (Tile tile: tiles){
            CityAttribute isCity;
            if (citiesList.contains(tile)){
                Cities randomAtt = Cities.NONE;
                while (randomAtt.equals(Cities.NONE)){
                    randomAtt = Cities.values()[bag.nextInt(Cities.values().length)];
                }
                isCity = new CityAttribute(randomAtt);
            }
            else{
                isCity = new CityAttribute(Cities.NONE);
            }
            tile.addAttribute(isCity);
        }
        return attribute;
    }
    
}
