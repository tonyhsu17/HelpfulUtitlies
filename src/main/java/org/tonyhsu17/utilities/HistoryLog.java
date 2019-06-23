package org.tonyhsu17.utilities;


import java.io.*;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;



/**
 * Manages persistent (file-based) history log for any purposes.
 *
 * @author Tony Hsu
 */
public class HistoryLog implements Logger {
    public static final int DEFAULT_MAX_LOG_LENGTH = 1000;
    public static final String DEFAULT_LOG_NAME = ".log.txt";
    public static final boolean DEFAULT_ALLOW_DUPS = false;
    private String logName; // log name
    private String logPath; // path of log
    private int logSize; // max number of entries
    private LinkedList<String> list;
    private HashSet<String> dupCheck;
    private boolean allowDups;
    private Charset charset;

    /**
     * Initializes and reads in log file.
     *
     * @param srcPath Directory to store/read log file
     * @throws FileNotFoundException
     * @throws IOException
     */
    public HistoryLog(String srcPath) throws FileNotFoundException, IOException {
        this(srcPath, DEFAULT_LOG_NAME, DEFAULT_MAX_LOG_LENGTH, DEFAULT_ALLOW_DUPS);
    }

    /**
     * Initializes and reads in log file.
     *
     * @param srcPath Directory to store/read log file
     * @param logSize Max entry size to store
     * @throws IOException
     */
    public HistoryLog(String srcPath, int logSize) throws IOException {
        this(srcPath, DEFAULT_LOG_NAME, logSize, DEFAULT_ALLOW_DUPS);
    }

    /**
     * Initializes and reads in log file.
     *
     * @param srcPath Directory to store/read log file
     * @param logName Name of log
     * @throws IOException
     */
    public HistoryLog(String srcPath, String logName) throws IOException {
        this(srcPath, logName, DEFAULT_MAX_LOG_LENGTH, DEFAULT_ALLOW_DUPS);
    }

    /**
     * Initializes and reads in log file.
     *
     * @param srcPath   Directory to store/read log file
     * @param logName   Name of log
     * @param allowDups allow same entries to be added
     * @throws IOException
     */
    public HistoryLog(String srcPath, String logName, boolean allowDups) throws IOException {
        this(srcPath, logName, DEFAULT_MAX_LOG_LENGTH, allowDups);
    }

    /**
     * Initializes and reads in log file.
     *
     * @param srcPath   Directory to store/read log file
     * @param logName   Name of log
     * @param logSize   Max entry size to store
     * @param allowDups allow same entries to be added
     * @throws IOException
     */
    public HistoryLog(String srcPath, String logName, int logSize, boolean allowDups) throws IOException {
        list = new LinkedList<String>();
        dupCheck = new HashSet<String>();
        this.logName = logName;
        this.logSize = logSize;
        this.allowDups = allowDups;
        // append file separator if not found in srcPath
        logPath = srcPath + (srcPath.endsWith(File.separator) ? "" : File.separator) + logName;
        charset = Charset.forName("UTF-8");
        readInFile();
    }

    /**
     * Read in log file.
     *
     * @throws IOException
     */
    private void readInFile() throws IOException {
        if(new File(logPath).exists()) {
            BufferedReader sc = new BufferedReader(
                new InputStreamReader(
                    new FileInputStream(new File(logPath)), charset.newDecoder()));
            String line = sc.readLine();
            while(line != null) {
                list.add(line);
                dupCheck.add(line);
                line = sc.readLine();
            }
            sc.close();
        }
    }

    /**
     * Override charset to use. Default is UTF-8
     *
     * @param charset {@link Charset}
     * @return
     */
    public HistoryLog setCharset(Charset charset) {
        charset = charset;
        return this;
    }

    public void add(String str) {
        add(str, "");
    }

    /**
     * Queue string to be written to file.
     *
     * @param str      String to write
     * @param modifier Extra value to add to string
     */
    public void add(String str, String modifier) {
        // if allow dups or don't allow dups and doesn't contain item
        if(allowDups || !dupCheck.contains(str + modifier)) {
            list.add(str + modifier);
            dupCheck.add(str + modifier);

            if(list.size() > logSize) {
                dupCheck.remove(list.remove());
            }
        }
    }

    /**
     * Write queue to file.
     *
     * @throws IOException
     */
    public void save() throws IOException {
        if(list.isEmpty()) {
            return;
        }
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(logPath)), charset.newEncoder()));
        for(String str : list) {
            bw.write(str);
            bw.newLine();
        }
        bw.close();
    }

    /**
     * Checks if string is in history.
     *
     * @param str String to check
     * @return True if string found in history
     */
    public boolean contains(String str) {
        return isInHistory(str, "");
    }

    /**
     * Checks if string is in history.
     *
     * @param str      String to check
     * @param modifier Extra value to add string
     * @return True if string found in history
     */
    public boolean contains(String str, String modifier) {
        return isInHistory(str, modifier);
    }

    /**
     * Checks if string is in history.
     *
     * @param str      String to check
     * @param modifier Extra value to add string
     * @return True if string found in history
     */
    public boolean contains(String str, long modifier) {
        return isInHistory(str, modifier);
    }

    /**
     * Checks if string is in history.
     *
     * @param str       String to check
     * @param modifiers Extra value to add string
     * @return True if string found in history
     */
    public boolean contains(String str, Object... modifiers) {
        return isInHistory(str, modifiers);
    }

    /**
     * Checks if string is in history.
     *
     * @param str String to check
     * @return True if string found in history
     */
    public boolean isInHistory(String str) {
        return isInHistory(str, "");
    }

    //    /**
    //     * Checks if string is in history.
    //     *
    //     * @param str      String to check
    //     * @param modifier Extra value to add string
    //     * @return True if string found in history
    //     */
    //    public boolean isInHistory(String str, long modifier) {
    //        return isInHistory(str, modifier);
    //    }

    //    /**
    //     * Checks if string is in history.
    //     *
    //     * @param str      String to check
    //     * @param modifier Extra value to add string
    //     * @return True if string found in history
    //     */
    //    public boolean isInHistory(String str, String modifier) {
    //        return isInHistory(str, modifier);
    //    }

    public boolean isInHistory(String str, Object... modifiers) {
        StringBuilder sb = new StringBuilder();
        for(Object modifier : modifiers) {
            sb.append(modifier.toString());
        }
        return dupCheck.contains(str + sb.toString());
    }

    /**
     * Removes specified string from history list
     *
     * @param str       String to check
     * @param modifiers Extra value to add string
     * @return True if string found in history and removed
     */
    public boolean remove(String str, Object... modifiers) {
        StringBuilder sb = new StringBuilder();
        for(Object modifier : modifiers) {
            sb.append(modifier.toString());
        }
        dupCheck.remove(str + sb.toString());
        return list.remove(str + sb.toString());
    }

    /**
     * Returns the file name of the log.
     *
     * @return
     */
    public String getName() {
        return logName;
    }

    /**
     * Returns the file path of the log.
     *
     * @return
     */
    public String getPath() {
        return logPath;
    }

    /**
     * Returns list of values read from file.
     *
     * @return
     */
    public List<String> getSavedList() {
        return list;
    }
}
