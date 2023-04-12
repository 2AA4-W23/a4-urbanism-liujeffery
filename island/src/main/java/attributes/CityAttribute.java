package attributes;

public class CityAttribute implements Attribute{
    public enum Cities{
        NONE, VILLAGE, HAMLET, TOWN, CITY, METROPOLIS
    }
    public Cities city;
    
    public CityAttribute(Cities city){
        this.city = city;
    }
}
