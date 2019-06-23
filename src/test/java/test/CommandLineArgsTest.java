package test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.ParseException;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import org.tonyhsu17.utilities.commandline.CommandLineArgs;
import org.tonyhsu17.utilities.commandline.Parameter;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;



public class CommandLineArgsTest {
    private static class Params {
        final static Parameter D = new Parameter("d", "dest", true, "dest", true);
        final static Parameter T = new Parameter("t", false, "test mode");
        final static Parameter V = new Parameter("v", "verbose", false, "verbose on");
        final static Parameter S = new Parameter("s", true, "src");

        public static Parameter[] getParams = {D, T, V, S};
    }

    @Test
    public void testWithAllParams() throws ParseException {
        String[] args = {"-s", "some/path", "-d", "some/dest", "-t", "-v", "--verbose"};
        CommandLine cmd = CommandLineArgs.getCommandLine(Params.getParams, args);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(cmd.hasOption(Params.D.opt()), "Param D failed");
        softAssert.assertEquals(cmd.getOptionValue(Params.D.opt()), "some/dest", "Param D failed");
        softAssert.assertTrue(cmd.hasOption(Params.S.opt()), "Param S faied");
        softAssert.assertEquals(cmd.getOptionValue(Params.S.opt()), "some/path", "Param S failed");
        softAssert.assertTrue(cmd.hasOption(Params.T.opt()), "Param T faied");
        softAssert.assertTrue(cmd.hasOption(Params.V.opt()), "Param V faied");
        softAssert.assertTrue(cmd.hasOption(Params.V.longOpt()), "Param VERBOSE faied");

        CommandLineArgs.printHelp("test", Params.getParams);
        softAssert.assertAll();
    }

    @Test
    public void testMissingRequired() throws ParseException {
        String[] args = {"-t"};
        CommandLine cmd = null;
        SoftAssert softAssert = new SoftAssert();
        try {
            cmd = CommandLineArgs.getCommandLine(Params.getParams, args);
            softAssert.fail("required arg not found");
        }
        catch (MissingOptionException e) {
            softAssert.assertNull(cmd, "initialization didnt fail");
        }
        CommandLineArgs.printHelp("test", Params.getParams);
        softAssert.assertAll();
    }

    @Test
    public void testPrintHelp() {
        PrintStream original = System.out;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream stream = new PrintStream(baos);
        System.setOut(stream);
        CommandLineArgs.printHelp("app.exe", Params.getParams);
        SoftAssert softAssert = new SoftAssert();

        System.setOut(original);
        softAssert.assertEquals(baos.toString(), "usage: app.exe -d <arg> [-s <arg>] [-t] [-v]" + System.lineSeparator() +
                                                 " -d,--dest <arg>   dest" + System.lineSeparator() +
                                                 " -s <arg>          src" + System.lineSeparator() +
                                                 " -t                test mode" + System.lineSeparator() +
                                                 " -v,--verbose      verbose on" + System.lineSeparator());
        softAssert.assertAll();
    }
}
