package island;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import attributes.Attribute;

public class Island {
    public class Tile {
        private int id;
        private Set<Tile> neighbours;
        private Set<Attribute> attributes;
        private double x; // Center of tile, scaled 0-1
        private double y;
    
        public Tile(int id, double x, double y){
            neighbours = new HashSet<Tile>();
            this.attributes = new HashSet<Attribute>();
            this.id = id;
            this.x = x;
            this.y = y;
        }
    
        public double getX(){
            return x;
        }
        
        public double getY(){
            return y;
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
    
        @SuppressWarnings("unchecked")
        public <T extends Attribute> T getAttribute(Class<T> type){
            for(Attribute attr : attributes){
                if(attr.getClass() == type) return (T)attr;
            }
            return null;
        }
    }

    private Set<Class<? extends Attribute>> attributes; 
    private Set<Tile> tiles;
    private TreeMap<Integer, Tile> idMap;

    /**
     * 
     * @param adjacencyMap
     * @param xCoords mapping of x coordinates of tiles, scaled from 0-1
     * @param yCoords mapping of x coordinates of tiles, scaled from 0-1
     */
    public Island(Map<Integer, List<Integer>> adjacencyMap, Map<Integer, Double> xCoords, Map<Integer, Double> yCoords){
        this.attributes = new HashSet<Class<? extends Attribute>>();
        this.tiles = new HashSet<Tile>();
        idMap = new TreeMap<>();

        Set<Integer> tileIds = adjacencyMap.keySet();
        for(Integer id : adjacencyMap.keySet()){
            Tile tile = new Tile(id, xCoords.get(id), yCoords.get(id));
            tiles.add(tile);
            this.idMap.put(id, tile);
        }   // Create tile for every id
        for(Integer id : tileIds){
            Tile tile = this.idMap.get(id);
            Set<Tile> neighbourTiles = new HashSet<>();
            List<Integer> neighbourIds = adjacencyMap.get(id);
            for(Integer neighbourId : neighbourIds){
                neighbourTiles.add(this.idMap.get(neighbourId));
            } // Add neighbour Tile objects as a neighbour
            
            boolean setFail = !tile.setNeighbours(neighbourTiles);
            if(setFail) throw new Error("Error: Tried re-setting neighbours during island construction");
        }   // Assign neighbours
    }

    /**
     * @return Set of tiles possessed by the island
     */
    public Set<Tile> getTiles(){
        Set<Tile> copy = new HashSet<>();
        copy.addAll(tiles);
        System.out.println(copy.toString());
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
    protected boolean addAttributeLayer(Map<Tile, ? extends Attribute> attributeMap){
        if(attributeMap.size() != tiles.size()) return false; // size check
        Set<Tile> tileSet = attributeMap.keySet();
        for(Tile t : tileSet)
            if(!tiles.contains(t)) return false; // check for same objects

        for(Tile t : tileSet)
            t.attributes.add(attributeMap.get(t)); // Add attributes
        return true;
    }

}