package ca.mcmaster.cas.se2aa4.a2.generator;

//TODO: switch to use BigDecimal for precision purposes
public class Coordinate {
    public int x;
    public int y;
    public Coordinate(int x, int y){
        this.x = x;
        this.y = y;
    }
    
    @Override
    public boolean equals(Object o){
        if(o == this){
            return true;
        }
        if(!(o instanceof Coordinate)){
            return false;
        }
        Coordinate c = (Coordinate) o;
        return this.x == c.x && this.y == c.y;
    }
}
