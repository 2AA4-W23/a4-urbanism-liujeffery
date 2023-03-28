package attributes;

import java.util.Map;
import java.util.HashMap;

import island.Edge;

/**
 * Whether a tile is land or water.
 */
public class RiverAttribute implements Attribute{
    private Map<Edge, Integer> riverEdges;

    /**
     * @param isLand Whether the tile is land
     */
    public RiverAttribute(){
        this.riverEdges = new HashMap<>();
    }

    public void addRiver(Edge e){
        Integer size = 0;
        if(this.riverEdges.containsKey(e)){
            size = this.riverEdges.get(e);
        }
        this.riverEdges.put(e, size+1);
        return;
    }
}
