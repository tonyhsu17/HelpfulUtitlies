package test;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.tonyhsu17.utilities.Logger;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;


/**
 * Unit Tests for {@link Logger}
 *
 * @author Tony Hsu
 *
 */
public class LoggerTest implements Logger {
    @Test
    public void testInfo() {
        final String CALLER = "[INFO] LoggerTest:26: ";
        final String MESSAGE = "This ia a test info message!";

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        PrintStream oldStream = System.out;
        System.setOut(new PrintStream(outStream));
        info(MESSAGE);
        System.out.flush();
        System.setOut(oldStream);
        Assert.assertEquals(outStream.toString(),  CALLER + MESSAGE + System.lineSeparator(), "outputs mismatch.");
    }

    @Test
    public void testDebug() {
        final String CALLER = "[DEBUG] LoggerTest.testDebug():40: ";
        final String MESSAGE = "This ia a test debug message!";

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        PrintStream oldStream = System.out;
        System.setOut(new PrintStream(outStream));
        debug(MESSAGE);
        System.out.flush();
        System.setOut(oldStream);
        Assert.assertEquals(outStream.toString(),  CALLER + MESSAGE + System.lineSeparator(), "outputs mismatch.");
    }

    @Test
    public void testError() {
        final String CALLER = "[ERROR] LoggerTest.testError():54: ";
        final String MESSAGE = "This ia a test error message!";

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        PrintStream oldStream = System.out;
        System.setOut(new PrintStream(outStream));
        error(MESSAGE);
        System.out.flush();
        System.setOut(oldStream);
        Assert.assertEquals(outStream.toString(),  CALLER + MESSAGE + System.lineSeparator(), "outputs mismatch.");
    }
}
