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
        existingList = new LinkedList<String>();
        toWritelist = new LinkedList<String>();
        dupCheck = new HashSet<String>();
        this.logName = logName;
        this.logSize = logSize;
        this.allowDups = allowDups;
        logPath = srcPath + File.separator + logName;
        readInFile();
    }

    private void readInFile() throws FileNotFoundException {
        if(new File(logPath).exists()) {
            Scanner sc = new Scanner(new File(logPath));
            while(sc.hasNext()) {
                String line = sc.nextLine();
                existingList.add(line);
                if(!allowDups) {
                    dupCheck.add(line);
                }
            }
            sc.close();
        }
    }

    public void addToWriteList(String file) {
        addToWriteList(file, "");
    }

    public void addToWriteList(String file, long lastModified) {
        addToWriteList(file, lastModified + "");
    }

    public void addToWriteList(String file, String modifier) {
        if(allowDups) {
            toWritelist.add(file + modifier);
        }
        else if(!allowDups && !dupCheck.contains(file + " " + modifier)) {
            dupCheck.add(file + modifier);
            toWritelist.add(file + modifier);
        }
    }

    public void writeToFile() throws IOException {
        if(toWritelist.isEmpty()) {
            return;
        }

        if(existingList.size() + toWritelist.size() <= logSize) {
            FileWriter fw = new FileWriter(logPath, true);
            for(String str : toWritelist) {
                fw.write(str + System.lineSeparator());
            }
            toWritelist.clear();
            fw.close();
        }
        else {
            FileWriter fw = new FileWriter(logPath);
            for(int i = existingList.size() - toWritelist.size(); i < existingList.size() && i >= 0; i++) {
                fw.write(existingList.get(i) + System.lineSeparator());
            }
            for(int i = Math.max(0, toWritelist.size() - logSize); i < toWritelist.size(); i++) {
                fw.write(toWritelist.get(i) + System.lineSeparator());
            }
            toWritelist.clear();
            fw.close();
        }
    }

    public boolean isInHistory(String file) {
        return isInHistory(file, "");
    }

    public boolean isInHistory(String file, long lastModified) {
        return isInHistory(file, lastModified + "");
    }

    public boolean isInHistory(String file, String modifier) {
        return existingList.contains(file + modifier) || toWritelist.contains(file + modifier);
    }

    public boolean contains(String file, String modifier) {
        return isInHistory(file, modifier);
    }

    public String getName() {
        return logName;
    }

    public String getPath() {
        return logPath;
    }

    public List<String> getSavedList() {
        return existingList;
    }

    public List<String> getPendingWriteList() {
        return toWritelist;
    }
}
