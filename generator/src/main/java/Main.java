import ca.mcmaster.cas.se2aa4.a2.generator.MeshADT;
import ca.mcmaster.cas.se2aa4.a2.io.MeshFactory;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;

import java.io.IOException;
import java.util.Random;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Main {

    public static void main(String[] args) throws IOException, ParseException {
        Options options = new Options();
        options.addOption("t", true, "Type: grid or irregular");
        options.addOption("r", true, "Number of relaxation cycles");
        options.addOption("seed", true, "Random points generation seed");
        options.addOption("f", true, "REQUIRED: File to write mesh to");
        options.addOption("h", false, "Help");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        if(cmd.hasOption("h")){
            System.out.println(
                "Options:"
                + "\n\t -t\tType: grid or irregular"
                + "\n\t -r\tNumber of relaxation cycles"
                + "\n\t -seed\tSeed used for point generation"
                + "\n\t -f\tFile to write mesh to"
                + "\n\t -h\tDisplays this text"
            );
            return;
        }
        doMesh(cmd);
    }

    static void doMesh(CommandLine cmd) throws IOException{
        MeshADT baseMesh = new MeshADT(500, 500, 100);
        int width = 500, height = 500, squareSize = 20;
        String target = "irregular";
        if(cmd.hasOption("t"))
            target = cmd.getOptionValue("t");
        // create a uniform grid to test
        if(target.equals("grid")){
            for(int i = 0;i <= width; i+=squareSize){
                for(int j = 0;j <= height; j+=squareSize){
                    baseMesh.addVertex(i, j, "255,0,0");
                }
            }
        }else if(target.equals("irregular")){
            //create random points
            Random bag = new Random(69);
            if(cmd.hasOption("seed"))
                bag = new Random(Integer.parseInt(cmd.getOptionValue("seed")));
            for(int i = 0; i < 100; i++){
                int x = bag.nextInt(width);
                int y = bag.nextInt(height);
                baseMesh.addVertex(x, y, "255,0,0");
            }
        }        

        baseMesh.addVoronoiPolygons();
        
        int rCycles = 1;
        if(cmd.hasOption("r"))
            rCycles = Integer.parseInt(cmd.getOptionValue("r"));
        for(int i = 0; i < rCycles; i++){
            baseMesh.relaxMesh();
        }

        baseMesh.computeNeighbours();
        Mesh myMesh = baseMesh.makeMesh();

        MeshFactory factory = new MeshFactory();
        factory.write(myMesh, cmd.getOptionValue("f"));
    }
}