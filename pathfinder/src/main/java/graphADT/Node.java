package graphADT;

import java.util.HashSet;
import java.util.Set;

import attributes.Attribute;

public class Node implements Comparable<Node>{
    private double x;
    private double y;
    private int id;
    private double priority;

    private Set<Attribute> attributes;

    public Node(double x, double y, int id){
        this.x = x;
        this.y = y;
        this.id = id;

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

    public int getId(){
        return id;
    }

    public double getPriority(){
        return this.priority;
    }

    public void setPriority(double priority){
        this.priority = priority;
    }

    @Override
    public int compareTo(Node o) {
        return Double.compare(this.priority, o.getPriority());
    }
}