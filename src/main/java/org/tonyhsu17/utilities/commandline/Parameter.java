package org.tonyhsu17.utilities.commandline;

/**
 * Data helper class for initializing Apache CommandLine
 *
 * @author Tony Hsu
 */
public class Parameter {
    private String opt;
    private String longOpt;
    private boolean hasValue;
    private String description;
    private boolean required;

    /**
     * Create a commandline option
     *
     * @param key         Commandline argument
     * @param hasValue    Does argument have value
     * @param description Description of argument
     * @param required    Is it a required option
     */
    public Parameter(String key, String longKey, boolean hasValue, String description, boolean required) {
        opt = key;
        longOpt = longKey;
        this.hasValue = hasValue;
        this.description = description;
        this.required = required;
    }

    /**
     * Create an optional commandline option. [longKey = null, required = false]
     *
     * @param key         Commandline argument
     * @param hasValue    Does argument have value
     * @param description Description of argument
     */
    public Parameter(String key, boolean hasValue, String description) {
        this(key, null, hasValue, description, false);
    }

    /**
     * Create a commandline option. [longKey = null]
     *
     * @param key         Commandline argument
     * @param hasValue    Does argument have value
     * @param description Description of argument
     * @param required    Is it a required option
     */
    public Parameter(String key, boolean hasValue, String description, boolean required) {
        this(key, null, hasValue, description, required);
    }

    public Parameter(String key, String longKey, boolean hasValue, String description) {
        this(key, longKey, hasValue, description, false);
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
     * Option full keyword.
     *
     * @return
     */
    public String longOpt() {
        return longOpt;
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

    /**
     * Is option required.
     *
     * @return
     */
    public boolean isRequired() {
        return required;
    }

    @Override public String toString() {
        return "Parameter{" +
               "opt='" + opt + '\'' +
               ", hasValue=" + hasValue +
               ", description='" + description + '\'' +
               ", required=" + required +
               '}';
    }
}
