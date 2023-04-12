package graphADT;

import java.util.ArrayList;
import java.util.List;

import attributes.Attribute;

public class Node implements Comparable<Node>{
    private double x;
    private double y;
    private int id;
    private double priority;

    private List<Attribute> attributes;

    public Node(double x, double y, int id){
        this.x = x;
        this.y = y;
        this.id = id;

        attributes = new ArrayList<>();
    }

    public void addAttribute(Attribute attribute){
        attributes.add(attribute);
    }

    @SuppressWarnings("unchecked")
    public <T extends Attribute> T getAttribute(Class<T> type){
        for(Attribute attr : attributes){
            if(attr.getClass() == type) return (T)attr;
        }
        return null;
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

    public boolean equals(Node o){
        return id == o.getId();
    }
}