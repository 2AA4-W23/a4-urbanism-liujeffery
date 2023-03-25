package island;

import java.util.HashMap;

public class AttributeLayer <T extends Attribute>{
    private HashMap<Integer, T> attributeMap;

    public AttributeLayer(HashMap<Integer, T> attributeMap){
        this.attributeMap = attributeMap;
    }
    public T AttributegetAttribute(Integer tileId){
        return attributeMap.get(tileId);
    }
}
