package org.tonyhsu17.utilities.commandline;

/**
 * Struct for initializing {@link CommandLineArgs}.
 * 
 * @author Tony Hsu
 *
 */
public class Parameter {
    private String opt;
    private boolean hasValue;
    private String description;

    public Parameter(String key, boolean hasValue, String description) {
        opt = key;
        this.hasValue = hasValue;
        this.description = description;
    }

    /**
     * Option keyword.
     * 
     * @return
     */
    public String opt() {
        return opt;
    }

    /**
     * Has value for option.
     * 
     * @return
     */
    public boolean hasValue() {
        return hasValue;
    }

    /**
     * Description of option.
     * 
     * @return
     */
    public String desc() {
        return description;
    }
}
