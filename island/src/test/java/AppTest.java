import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Polygon;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Vertex;
import configuration.Configuration;
import island.Island;
import island.Tile;
import utilities.Formatter;
import utilities.IO;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    @Order (Integer.MAX_VALUE)
    public void fullAppTest()
    {
        try {
            System.out.println(System.getProperty("user.dir"));
            Main.main(new String[]{"-i", "../generator/test.mesh", "-o", "output.mesh"});
           
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    @Order (1)
    public void testValidConfigurations(){
        String[] args = {"-i", "generator/test.mesh", "-o", "output.mesh"};
        Configuration configTest = new Configuration(args);

        boolean inCorrect = args[1].equals(configTest.inputAddress);
        boolean outCorrect = args[3].equals(configTest.outputAddress);

        assertTrue(inCorrect && outCorrect);
    }

    //@Test
    @Order (2)
    public void testFormatter(){
        Structs.Mesh mesh = IO.readMesh("../generator/test.mesh");
        Formatter meshFormatter = new Formatter(mesh);
        Island island = meshFormatter.convertToIsland();

        for (int i = 0; i < mesh.getPolygonsCount(); i++){
            Polygon p = mesh.getPolygons(i);

            Vertex center = mesh.getVertices(p.getCentroidIdx());
            Tile tile = island.getTileByID(i);

            assert(center.getX() == tile.getX() && center.getY() == tile.getY());
        }
    }
}
