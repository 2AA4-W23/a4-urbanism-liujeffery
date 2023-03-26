import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.util.GeometricShapeFactory;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import featuregeneration.BasicGenerator;
import featuregeneration.LandGenerator;
import island.Island;
import island.IslandBuilder;
import island.IslandBuilder.MissingAttributeError;
import utilities.Configuration;
import utilities.Formatter;
import utilities.IO;

/**
 * Hello world!
 *
 */
public class Main 
{
    public static void main( String[] args ) throws MissingAttributeError {
        Configuration config = new Configuration(args);
        if(!config.isValid()){
            config.printHelp();
            return;
        }
        Structs.Mesh inputMesh = IO.readMesh(config.inputAddress);
        if(inputMesh == null) return;
        Formatter meshFormatter = new Formatter(inputMesh);
        Island island = meshFormatter.convertToIsland();
        
        IslandBuilder ib = new IslandBuilder();

        //TODO: remove this code from main
        GeometryFactory gf = new GeometryFactory();
        GeometricShapeFactory gsf = new GeometricShapeFactory(gf);
        gsf.setCentre(new Coordinate(0.5, 0.5));
        gsf.setSize(0.8);
        Polygon outerCircle = gsf.createCircle();

        gsf.setCentre(new Coordinate(0.5, 0.5));
        gsf.setSize(0.5);
        Polygon innerCircle = gsf.createCircle();

        ib.addGenerator(new LandGenerator(outerCircle.difference(innerCircle)));

        ib.build(island);
        IO.writeMesh(meshFormatter.meshFromIsland(island), config.outputAddress);
        System.out.println("Success");
    }
}
