package org.tonyhsu17.utilities;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;



/**
 * Manages persistent (file-based) history log for any purposes.
 * 
 * @author Tony Hsu
 *
 */
public class HistoryLog implements Logger {
    public static final int DEFAULT_MAX_LOG_LENGTH = 1000;
    public static final String DEFAULT_LOG_NAME = ".log.txt";
    public static final boolean DEFAULT_ALLOW_DUPS = false;
    private String logName; // log name
    private String logPath; // path of log
    private int logSize; // max number of entries
    private LinkedList<String> list;
    private List<String> existingList;
    private List<String> toWritelist;
    private HashSet<String> dupCheck;
    private boolean allowDups;

    /**
     * Initializes and reads in log file.
     * 
     * @param srcPath Directory to store/read log file
     * @throws FileNotFoundException
     */
    public HistoryLog(String srcPath) throws FileNotFoundException {
        this(srcPath, DEFAULT_LOG_NAME, DEFAULT_MAX_LOG_LENGTH, DEFAULT_ALLOW_DUPS);
    }

    /**
     * Initializes and reads in log file.
     * 
     * @param srcPath Directory to store/read log file
     * @param logName Name of log
     * @throws FileNotFoundException
     */
    public HistoryLog(String srcPath, String logName) throws FileNotFoundException {
        this(srcPath, logName, DEFAULT_MAX_LOG_LENGTH, DEFAULT_ALLOW_DUPS);
    }

    /**
     * Initializes and reads in log file.
     * 
     * @param srcPath Directory to store/read log file
     * @param logName Name of log
     * @param allowDups allow same entries to be added
     * @throws FileNotFoundException
     */
    public HistoryLog(String srcPath, String logName, boolean allowDups) throws FileNotFoundException {
        this(srcPath, DEFAULT_LOG_NAME, DEFAULT_MAX_LOG_LENGTH, allowDups);
    }

    /**
     * Initializes and reads in log file.
     * 
     * @param srcPath Directory to store/read log file
     * @param logName Name of log
     * @param logSize Max entry size to store
     * @param allowDups allow same entries to be added
     * @throws FileNotFoundException
     */
    public HistoryLog(String srcPath, String logName, int logSize, boolean allowDups) throws FileNotFoundException {
        list = new LinkedList<String>();
        existingList = new LinkedList<String>();
        toWritelist = new LinkedList<String>();
        dupCheck = new HashSet<String>();
        this.logName = logName;
        this.logSize = logSize;
        this.allowDups = allowDups;
        logPath = srcPath + File.separator + logName;
        readInFile();
    }

    /**
     * Read in log file.
     * 
     * @throws FileNotFoundException
     */
    private void readInFile() throws FileNotFoundException {
        if(new File(logPath).exists()) {
            Scanner sc = new Scanner(new File(logPath));
            while(sc.hasNext()) {
                String line = sc.nextLine();
                list.add(line);
                dupCheck.add(line);
            }
            sc.close();
        }
    }

    public void add(String str) {
        add(str, "");
    }

    /**
     * Queue string to be written to file.
     * 
     * @param str String to write
     * @param modifier Extra value to add to string
     */
    public void add(String str, String modifier) {
        if(allowDups || (!allowDups && !dupCheck.contains(str + modifier))) {
            list.add(str);
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
        FileWriter fw = new FileWriter(logPath);
        for(String str : list) {
            fw.write(str + System.lineSeparator());
        }
        fw.close(); // no need to catch npe, as ioexception will be thrown if any erro has occurred before
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

    /**
     * Checks if string is in history.
     * 
     * @param str String to check
     * @param modifier Extra value to add string
     * @return True if string found in history
     */
    public boolean isInHistory(String str, long modifier) {
        return isInHistory(str, modifier + "");
    }

    /**
     * Checks if string is in history.
     * 
     * @param str String to check
     * @param modifier Extra value to add string
     * @return True if string found in history
     */
    public boolean isInHistory(String str, String modifier) {
        return dupCheck.contains(str + modifier);
    }

    /**
     * Checks if string is in history.
     * 
     * @param str String to check
     * @param modifier Extra value to add string
     * @return True if string found in history
     */
    public boolean contains(String file, String modifier) {
        return isInHistory(file, modifier);
    }

    /**
     * Checks if string is in history.
     * 
     * @param str String to check
     * @param modifier Extra value to add string
     * @return True if string found in history
     */
    public boolean contains(String file, long modifier) {
        return isInHistory(file, modifier);
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
