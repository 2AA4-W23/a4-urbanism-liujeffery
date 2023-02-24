package ca.mcmaster.cas.se2aa4.a2.generator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Random;

import ca.mcmaster.cas.se2aa4.a2.io.Structs.Segment;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Vertex;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Property;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;

public class DotGen {

    private final int width = 500;
    private final int height = 500;
    private final int square_size = 20;

    public Mesh generate() {
        System.out.println("using new generator script");
        MeshADT temp = new MeshADT();
        // Create all the vertices and segments
        Random bag = new Random();
        for(int x = square_size/2; x < width; x += square_size) {
            for(int y = square_size/2; y < height; y += square_size) {
                int red = bag.nextInt(255);
                int green = bag.nextInt(255);
                int blue = bag.nextInt(255);
                String colorCode = red + "," + green + "," + blue;
                temp.addSquare(new Coordinate(x, y), square_size, colorCode);
            }
        }
        
        return temp.makeMesh();
    }

}
