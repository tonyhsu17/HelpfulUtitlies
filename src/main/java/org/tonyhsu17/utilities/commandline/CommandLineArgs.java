package org.tonyhsu17.utilities.commandline;

import org.apache.commons.cli.*;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;



/**
 * Helper class for {@link CommandLine}. Single method for parsing arguments.
 *
 * @author Tony Hsu
 */
public class CommandLineArgs {
    private CommandLineArgs() {
        // no need to initialize class.
    }

    /**
     * Convert Parameters into options.
     *
     * @param opts
     * @return
     */
    public static Options getOptions(List<Parameter> opts) {
        Options options = new Options();

        for(Parameter opt : opts) {
            if(opt.isRequired()) {
                options.addRequiredOption(opt.opt(), opt.longOpt(), opt.hasValue(), opt.desc());
            }
            else {
                options.addOption(opt.opt(), opt.longOpt(), opt.hasValue(), opt.desc());
            }

        }
        return options;
    }

    /**
     * Returns {@link CommandLine} with commandline parsed and options initialized.
     *
     * @param opts {@link Parameter}, the different options to parse with
     * @param args Arguments from main
     * @return {@link CommandLine}
     * @throws ParseException
     */
    public static CommandLine getCommandLine(List<Parameter> opts, String[] args) throws ParseException {
        CommandLineParser parser = new DefaultParser();
        return parser.parse(getOptions(opts), args);
    }

    /**
     * Returns {@link CommandLine} with commandline parsed and options initialized.
     *
     * @param opts {@link Parameter}, the different options to parse with
     * @param args Arguments from main
     * @return {@link CommandLine}
     * @throws ParseException
     */
    public static CommandLine getCommandLine(Parameter[] opts, String[] args) throws ParseException {
        return CommandLineArgs.getCommandLine(Arrays.asList(opts), args);
    }

    /**
     * Print out help and usage, full description.
     *
     * @param name Name of application
     * @param opts List of Parameters
     */
    public static void printHelp(String name, List<Parameter> opts) {
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp(name, getOptions(opts), true);
    }

    /**
     * Print out help and usage, full description.
     *
     * @param name Name of application
     * @param opts List of Parameters
     */
    public static void printHelp(String name, Parameter[] opts) {
        printHelp(name, Arrays.asList(opts));
    }

    /**
     * Print out just the usage, short form.
     *
     * @param name Name of application
     * @param opts List of Parameters
     */
    public static void printUsage(String name, List<Parameter> opts) {
        HelpFormatter helpFormatter = new HelpFormatter();
        PrintWriter output = new PrintWriter(System.out);
        helpFormatter.printUsage(output, HelpFormatter.DEFAULT_WIDTH, name, getOptions(opts));
        output.flush();
    }

    /**
     * Print out just the usage, short form.
     *
     * @param name Name of application
     * @param opts List of Parameters
     */
    public static void printUsage(String name, Parameter[] opts) {
        printUsage(name, Arrays.asList(opts));
    }
}
