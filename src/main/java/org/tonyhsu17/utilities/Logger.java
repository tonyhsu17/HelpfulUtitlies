package org.tonyhsu17.utilities;


/**
 * An alternative to using System.out.println. Provide 3 different tags: debug, info, error.
 * To use, just implement Logger
 * @author Tony Hsu
 *
 */
public interface Logger {
    public default void info(Object message) {
        System.out.println(
            "[INFO] " + getClass().getSimpleName() + "." + Thread.currentThread().getStackTrace()[2].getMethodName() + "(): " + message);
    }

    public default void debug(Object message) {
        System.out.println(
            "[DEBUG] " + getClass().getSimpleName() + "." + Thread.currentThread().getStackTrace()[2].getMethodName() + "(): " + message);
    }

    public default void error(Object message) {
        System.err.println(
            "[ERROR] " + getClass().getSimpleName() + "." + Thread.currentThread().getStackTrace()[2].getMethodName() + "(): " + message);
    }
}
