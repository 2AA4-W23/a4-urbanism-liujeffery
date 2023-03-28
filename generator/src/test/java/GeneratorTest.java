import org.junit.jupiter.api.Test;

public class GeneratorTest {

    @Test
    public void testGenerator(){
        try{
            Main.main(new String[]{"-k", "irregular", "-o", "test.mesh"});
        }
        catch (Exception e){
            e.printStackTrace();
            assert(false);
        }
        assert(true);
    }
}