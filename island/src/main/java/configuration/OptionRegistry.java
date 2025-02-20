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
        options.addOption("s", "shape", true, "Island shape: circle, square, lagoon");
        options.addOption("h", false, "Displays this");
        options.addOption("e", null, true, "Elevation type: hills, mountain, or plains");
        options.addOption("l", null, true, "Number of lakes");
        options.addOption("a", null, true, "Number of aquifers");
        options.addOption("seed", "seed", true, "Integer to specify a seed for reproducability");
        options.addOption("w", null, true, "Whittaker diagram to use");
        options.addOption("r", null, true, "Number of rivers");
        options.addOption("soil", "soil", true, "Soil profile: absorbant, standard, or parched");
        options.addOption("heat", "heatmap", true, "Draw heatmap of specified attribute: elevation, moisture or temperature");
        options.addOption("c", null, true, "Number of cities");
        return options;
    }
}