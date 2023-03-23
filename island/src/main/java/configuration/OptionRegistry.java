package configuration;

import org.apache.commons.cli.Options;

/**
 * Registry of different option suites. All option suites must have: 
 * -i (Input mesh address), -o (Output mesh address)
 */
public class OptionRegistry {
    static Options fullOptions(){
        Options options = new Options();
        options.addOption("i", null, true, "Input mesh address");
        options.addOption("o", null, true, "Output mesh address");
        options.addOption("m", "mode", true, "Island generation mode");
        options.addOption("h", false, "Displays this");
        return options;
    }
}
