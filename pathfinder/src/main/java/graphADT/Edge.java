package graphADT;

import java.util.HashSet;
import java.util.Set;

import attributes.Attribute;

public class Edge{
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

    public Edge(Node node1, Node node2, double distance){
        this.node1 = node1;
        this.node2 = node2;

        this.distance = distance;
        this.attributes = new HashSet<>();
    }

    public void addAttribute(Attribute attribute){
        attributes.add(attribute);
    }

    private double calculateDistance(){
        return Math.sqrt(Math.pow(node1.getX() - node2.getX(), 2) + Math.pow(node1.getY() - node2.getY(), 2));
    }

    public double getDistance(){
        return distance;
    }

    public boolean startsWith(Node node){
        return node.equals(node1);
    }

    public boolean endsWith(Node node){
        return node.equals(node2);
    }

    public boolean matches(Node node1, Node node2){
        return startsWith(node1) && endsWith(node2);
    }

    public Node getEnd(){
        return node2;
    }
}
