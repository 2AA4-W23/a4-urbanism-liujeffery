package island;

import java.util.HashSet;
import java.util.Set;

import attributes.Attribute;

public class Tile {
    private int id;
    private Set<Tile> neighbours;
    private Set<Attribute> attributes;

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

    public Attribute getAttribute(Class<? extends Attribute> type){
        for(Attribute attr : attributes){
            if(attr.getClass() == type) return attr;
        }
        return null;
    }

    public Set<Attribute> getAttributes(){
        Set<Attribute> set = new HashSet<>();
        set.addAll(attributes);
        return set;
    }
}
