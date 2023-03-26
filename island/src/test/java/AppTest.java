import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void noParameterTest()
    {
        try {    
            Main.main(new String[]{""});
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
        assertTrue( true );
    }

    @Test
    public void errorTest(){
        try {    
            Main.main(new String[]{"-i", "test.mesh", "-o", "islandtest.mesh"});
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
        assertTrue( true );
    }
}
