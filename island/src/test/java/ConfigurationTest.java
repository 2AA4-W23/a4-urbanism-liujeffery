import static org.junit.jupiter.api.Assertions.assertTrue;

import configuration.Configuration;

public class ConfigurationTest {
    public void testValidConfigurations(){
        String[] args = {"i", "sample.mesh", "o", "output.mesh"};
        Configuration configTest = new Configuration(args);

        boolean inCorrect = args[1].equals(configTest.inputAddress);
        boolean outCorrect = args[3].equals(configTest.outputAddress);

        assertTrue(inCorrect && outCorrect);
    }
}
