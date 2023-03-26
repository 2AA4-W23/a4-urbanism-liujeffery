package utilities;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import island.Mode;

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
    public island.Mode mode;
    public String inputAddress;
    public String outputAddress;

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
        mode = Mode.LAGOON;

        // Required options
        inputAddress = cmd.getOptionValue("i");
        outputAddress = cmd.getOptionValue("o");

        // Options
        if(cmd.hasOption("m"))
            mode = Mode.valueOf(cmd.getOptionValue("m"));
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
