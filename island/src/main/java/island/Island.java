package island;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import attributes.Attribute;

public class Island {

    private Set<Class<? extends Attribute>> attributes; 
    private Set<Tile> tiles;
    private TreeMap<Integer, Tile> idMap;

    public Island(Set<Tile> tiles, Map<Integer, List<Integer>> tileAdjList){
        this.tiles = tiles;
        this.attributes = new HashSet<Class<? extends Attribute>>();
        this.idMap = new TreeMap<>();

        //create the mapping from id to tile object
        for(Tile t : this.tiles){
            this.idMap.put(t.getId(), t);
        }

        //set the neighbors for each tile
        for(Tile t : this.tiles){
            Set<Tile> neighbourTiles = new HashSet<>();
            List<Integer> neighbourIds = tileAdjList.get(t.getId());
            for(Integer neighbourId : neighbourIds){
                neighbourTiles.add(this.idMap.get(neighbourId));
            } 
            
            boolean setFail = !t.setNeighbours(neighbourTiles);
            if(setFail) throw new Error("Error: Tried re-setting neighbours during island construction");
        }
    }

    /**
     * @return Set of tiles possessed by the island
     */
    public Set<Tile> getTiles(){
        Set<Tile> copy = new HashSet<>();
        copy.addAll(tiles);
        return copy;
    }

    public Set<Integer> getIds(){
        Set<Integer> copy = new HashSet<>();
        copy.addAll(idMap.keySet());
        return copy;
    }

    /**
     * @return Set of attributes possessed by the island
     */
    public Set<Class<? extends Attribute>> getAttributes(){
        Set<Class<? extends Attribute>> set = new HashSet<>();
        set.addAll(attributes);
        return set;
    }

    public Tile getTileByID(int id){
        return idMap.get(id);
    }

    /**
     * Adds attributes to each of the tiles in the island based on the mapping of attributes
     * @param attributeMap
     * @return whether the add was successful
     */
    protected void addAttribute(Attribute attribute){
        this.attributes.add(attribute.getClass());
    }

}