package island;

import java.util.HashSet;
import java.util.Set;

import attributes.Attribute;

public class Tile {
    private int id;
    private double x; // Center of tile, scaled 0-1
    private double y;

    private Set<Edge> edges;
    private Set<Tile> neighbours;
    private Set<Attribute> attributes;
    public Tile(int id, double x, double y){
        this.id = id;
        this.x = x;
        this.y = y;
        this.edges = new HashSet<Edge>();
        this.neighbours = new HashSet<Tile>();
        this.attributes = new HashSet<Attribute>();
    }

    public Tile(int id, double x, double y, Set<Edge> edges){
        this.id = id;
        this.x = x;
        this.y = y;
        this.edges = edges;
        this.neighbours = new HashSet<Tile>();
        this.attributes = new HashSet<Attribute>();
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

    public Set<Edge> getEdges(){
        Set<Edge> copy = new HashSet<Edge>(this.edges.size());
        copy.addAll(this.edges);
        return copy;
    }

    public int getId(){
        return id;
    }

    public void addAttribute(Attribute a){
        attributes.add(a);
    }


    @SuppressWarnings("unchecked")
    public <T extends Attribute> T getAttribute(Class<T> type){
        for(Attribute attr : attributes){
            if(attr.getClass() == type) return (T)attr;
        }
        return null;
    }
}
