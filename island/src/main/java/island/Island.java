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
    private Map<Integer, Tile> tilesById;
    private Map<Integer, Edge> edgesById;


    public Island(Map<Integer, Tile> tiles, Map<Integer, Edge> edges){
        this.tilesById = tiles;
        this.edgesById = edges;
        this.tiles = new HashSet<>(tiles.values());
        this.attributes = new HashSet<Class<? extends Attribute>>();
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
        copy.addAll(tilesById.keySet());
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
        return tilesById.get(id);
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