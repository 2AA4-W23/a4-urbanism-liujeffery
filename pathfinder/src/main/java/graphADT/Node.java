package graphADT;

import java.util.HashSet;
import java.util.Set;

import attributes.Attribute;

public class Node {
    private double x;
    private double y;

    private Set<Attribute> attributes;

    public Node(double x, double y){
        this.x = x;
        this.y = y;

        attributes = new HashSet<>();
    }

    public void addAttribute(Attribute attribute){
        attributes.add(attribute);
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }
}