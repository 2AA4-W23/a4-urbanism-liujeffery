package island;

import java.util.HashSet;
import java.util.Set;

public class Tile {
    private int id;
    private Set<Tile> neighbours;
    public Set<Attribute> attributes;

    public Tile(int id){
        neighbours = new HashSet<Tile>();
        this.attributes = new HashSet<Attribute>();
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
}
