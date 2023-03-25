package island;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import attributes.Attribute;

public class Island {
    // Adj. List
    private Set<Class<? extends Attribute>> attributes; 
    private Set<Tile> tiles;

    public Island(HashMap<Integer, ArrayList<Integer>> adjacencyMap){
        attributes = new HashSet<Class<? extends Attribute>>();
        tiles = new HashSet<Tile>();

        HashMap<Integer, Tile> idMap = new HashMap<>();
        for(Integer id : adjacencyMap.keySet()){
            Tile tile = new Tile(id);
            tiles.add(tile);
            idMap.put(id, tile);
        }   // Create tile for every id
        for(Integer id : adjacencyMap.keySet()){
            Tile tile = idMap.get(id);
            Set<Tile> neighbours = new HashSet<>();
            for(Integer neighbourId : adjacencyMap.get(id)){
                neighbours.add(idMap.get(neighbourId));
            } // Add neighbour Tile objects as a neighbour
            
            boolean setFail = !tile.setNeighbours(neighbours);
            if(setFail) throw new Error("Error: Tried re-setting neighbours during island construction");
        }   // Assign neighbours
    }

    public Set<Class<? extends Attribute>> getAttributes(){
        return attributes;
    }



    /**
     * Gets the neighbours of a tile by tileid.
     */
    public ArrayList<Integer> getNeighbors(Integer tileId){
        return mesh.get(tileId);
    }

    public Set<Integer> getTiles(){
        return mesh.keySet();
    }
    
}