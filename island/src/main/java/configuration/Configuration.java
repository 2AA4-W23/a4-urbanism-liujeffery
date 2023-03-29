package configuration;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import attributes.BiomeAttribute.Whittaker;
import attributes.MoistureAttribute.Soil;
import featuregeneration.LandGenerator;
import featuregeneration.ElevationGenerator.ElevationModes;

/**
 * Parses, validates, and stores information related to program configuration.
 */
public class Configuration {
    // Constants
    public static final Options OPTION_SUITE = OptionRegistry.fullOptions();

    // Meta-information
    private static CommandLineParser staticParser = new DefaultParser();
    private CommandLine cmd;
    private boolean valid = true;

    // Configuration information # THIS IS THE STUFF YOU WANT
    public LandGenerator.Shapes shape;
    public ElevationModes elevation;
    public int lakes;
    public int aquifers;
    public static int seed;
    public int rivers;
    public String inputAddress;
    public String outputAddress;
    public Whittaker whittaker;
    public Soil soil;

    public Configuration(String[] args){
        if(!parseArgs(args, OPTION_SUITE)){
            valid = false;
            return;
        }
        valid = validify();
        objectConversion();
    }

    /**
     * @return Whether the configuration is valid
     */
    public boolean isValid(){
        return valid;
    }


    /**
     * Parses the arguments into a cmd object. Returns if the parse
     * was succesful.
     * @param args Program entrypoint arguments
     * @param options Options to parse arguments against
     * @return 
     */
    private boolean parseArgs(String[] args, Options options) {
        try {
            this.cmd = staticParser.parse(options, args);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void objectConversion(){
        // Defaults
        shape = LandGenerator.Shapes.LAGOON;
        elevation = ElevationModes.MOUNTAIN;
        lakes = 2;
        aquifers = 2;
        rivers = 2;
        seed = -1;
        whittaker = Whittaker.TEMPERATE;
        soil = Soil.STANDARD;

        // Required options
        inputAddress = cmd.getOptionValue("i"); 
        outputAddress = cmd.getOptionValue("o");

        // Options
        if(cmd.hasOption("shape")){
            shape = LandGenerator.Shapes.valueOf(cmd.getOptionValue("shape").toUpperCase());
        }
        if (cmd.hasOption("e")){
            elevation = ElevationModes.valueOf(cmd.getOptionValue("e").toUpperCase());
        }
        if (cmd.hasOption("l")){
            lakes = Integer.parseInt(cmd.getOptionValue("l"));
        }
        if (cmd.hasOption("a")){
            aquifers = Integer.parseInt(cmd.getOptionValue("a"));
        }
        if(cmd.hasOption("r")){
            rivers = Integer.parseInt(cmd.getOptionValue("r"));
        }
        if (cmd.hasOption("seed")){
            seed = Integer.parseInt(cmd.getOptionValue("seed"));
        }
        if (cmd.hasOption("w")){
            whittaker = Whittaker.valueOf(cmd.getOptionValue("w").toUpperCase());
        }
        if(cmd.hasOption("soil")){
            soil = Soil.valueOf(cmd.getOptionValue("soil").toUpperCase());
        }
    }

    /**
     * TODO: Manual option validation of values
     */
    private boolean validify() {
        // Help option ||  Missing required options
        if(cmd.hasOption("h") || !(cmd.hasOption("i") && cmd.hasOption("o"))){
            return false;
        }
        return true;
    }

    public void printHelp(){
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("island", OPTION_SUITE);
    }
}
