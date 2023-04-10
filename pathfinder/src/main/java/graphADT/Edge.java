package graphADT;

import java.util.HashSet;
import java.util.Set;

import attributes.Attribute;

public class Edge {
    private double distance;
    private Node node1;
    private Node node2;

    private Set<Attribute> attributes;

    public Edge(Node node1, Node node2){
        this.node1 = node1;
        this.node2 = node2;

        this.distance = calculateDistance();
        this.attributes = new HashSet<>();
    }

    public void addAttribute(Attribute attribute){
        attributes.add(attribute);
    }

    public double calculateDistance(){
        return Math.sqrt(Math.pow(node1.getX() - node2.getY(), 2) + Math.pow(node1.getY() - node2.getY(), 2));
    }

    public double getDistance(){
        return distance;
    }
}
