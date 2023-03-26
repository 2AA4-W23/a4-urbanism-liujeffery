package island;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import attributes.Attribute;

public class Island {
    public class Tile {
        private int id;
        private Set<Tile> neighbours;
        private Set<Class<? extends Attribute>> attributes;
    
        public Tile(int id){
            neighbours = new HashSet<Tile>();
            this.attributes = new HashSet<Class<? extends Attribute>>();
        }
    
        /**
         * Sets the neighbours if they have not been set already. If they have been,
         * does nothing.
         * @return if neighbours were set in this call
         */
        public boolean setNeighbours(Set<Tile> neighbours){
            if(this.neighbours.isEmpty()){
                for(Tile t : neighbours)
                    this.neighbours.add(t);
                return true;
            }
            else return false;
        }
    
        public Set<Tile> getNeighbours(){
            Set<Tile> copy = new HashSet<Tile>(neighbours.size());
            copy.addAll(neighbours);
            return copy;
        }
    
        public int getId(){
            return id;
        }
    
        public boolean hasAttribute(Class<? extends Attribute> type){
            return attributes.contains(type);
        }
    
        /**
         * @return Set of attributes possessed by the island
         */
        public Set<Class<? extends Attribute>> getAttributes(){
            Set<Class<? extends Attribute>> set = new HashSet<>();
            set.addAll(attributes);
            return set;
        }

        /**
         * 
         * @return Set of tiles possessed by the island
         */
        public Set<Tile> getTiles(){
            Set<Tile> set = new HashSet<>();
            set.addAll(tiles);
            return set;
        }
    }

    // Adj. List
    private Set<Class<? extends Attribute>> attributes; 
    private Set<Tile> tiles;
    private HashMap<Integer, Tile> idMap;

    public Island(Map<Integer, List<Integer>> adjacencyMap){
        this.attributes = new HashSet<Class<? extends Attribute>>();
        this.tiles = new HashSet<Tile>();
        idMap = new HashMap<>();

        for(Integer id : adjacencyMap.keySet()){
            Tile tile = new Tile(id);
            this.tiles.add(tile);
            this.idMap.put(id, tile);
        }   // Create tile for every id
        for(Integer id : adjacencyMap.keySet()){
            Tile tile = this.idMap.get(id);
            Set<Tile> neighbours = new HashSet<>();
            for(Integer neighbourId : adjacencyMap.get(id)){
                neighbours.add(this.idMap.get(neighbourId));
            } // Add neighbour Tile objects as a neighbour
            
            boolean setFail = !tile.setNeighbours(neighbours);
            if(setFail) throw new Error("Error: Tried re-setting neighbours during island construction");
        }   // Assign neighbours
    }

    public Set<Class<? extends Attribute>> getAttributes(){
        return attributes;
    }

    public Set<Tile> getTiles(){
        Set<Tile> copy = new HashSet<>();
        copy.addAll(tiles);
        return copy;
    }
    
    public Tile getTileByID(int id){
        return idMap.get(id);
    }

    /**
     * Adds attributes to each of the tiles in the island based on the mapping of attributes
     * @param attributeMap
     * @return whether the add was successful
     */
    protected boolean addAttributeLayer(HashMap<Tile, Attribute> attributeMap){
        if(attributeMap.size() != tiles.size()) return false; // size check
        for(Tile t : attributeMap.keySet())
            if(!tiles.contains(t)) return false; // check for same objects

        for(Tile t : attributeMap.keySet())
            t.attributes.add(attributeMap.get(t)); // Add attributes
        return true;
    }

}