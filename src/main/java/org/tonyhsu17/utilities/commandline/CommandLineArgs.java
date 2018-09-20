package org.tonyhsu17.utilities.commandline;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;



/**
 * Helper class for {@link CommandLine}. Single method for parsing arguments.
 * 
 * @author Tony Hsu
 *
 */
public class CommandLineArgs {
    private CommandLineArgs() {
        // no need to initialize class.
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
        Options options = new Options();

        for(Parameter opt : opts) {
            options.addOption(opt.opt(), opt.hasValue(), opt.desc());
        }
        CommandLineParser parser = new DefaultParser();
        return parser.parse(options, args);
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
}
