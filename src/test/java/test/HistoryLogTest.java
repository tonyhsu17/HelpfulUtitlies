package test;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import org.tonyhsu17.utilities.HistoryLog;
import org.tonyhsu17.utilities.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;



/**
 * Unit Tests for {@link HistoryLog}
 * 
 * @author Tony Hsu
 *
 */
public class HistoryLogTest implements Logger {
    private static final String testDir = "testFolder";
    private static final String testFileName = "historyTestFile.txt";
    private static final String filePath = testDir + "/" + testFileName;
    private String[] entries = {"a", "b", "c", "d", "e", "f"};
    private HistoryLog hl;

    @BeforeClass(alwaysRun = true)
    public void beforeClass() {
        new File(testDir).mkdirs();
    }

    @AfterClass(alwaysRun = true)
    public void afterClass() {
        new File(filePath).delete();
        new File(testDir).deleteOnExit();
    }

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        new File(filePath).delete();
    }
    
    @Test
    public void testFileWrite() {
        SoftAssert softAssert = new SoftAssert();
        Scanner sc = null;
        try {
            hl = new HistoryLog(testDir, testFileName);

            for(String entry : entries) {
                hl.add(entry);
            }
            hl.save();

            sc = new Scanner(new File(filePath));
            int index = 0;
            while(sc.hasNextLine()) {
                softAssert.assertEquals(sc.nextLine(), entries[index++]);
            }
        }
        catch (IOException e) {
            softAssert.fail(e.getMessage());
        }
        finally {
            if(sc != null) {
                sc.close();
            }
        }
        softAssert.assertAll();
    }
    
    @Test
    public void testFileWriteAndReadIn() {
        SoftAssert softAssert = new SoftAssert();
        Scanner sc = null;
        try {
            hl = new HistoryLog(testDir, testFileName);

            for(String entry : entries) {
                hl.add(entry, entry);
            }
            hl.save();
            hl = new HistoryLog(testDir, testFileName);

            for(String entry : entries) {
                softAssert.assertTrue(hl.contains(entry, entry), "str: [" + entry + entry + "] not found");
            }
        }
        catch (IOException e) {
            softAssert.fail(e.getMessage());
        }
        finally {
            if(sc != null) {
                sc.close();
            }
        }
        softAssert.assertAll();
    }

    @Test
    public void testSizeLimit() {
        SoftAssert softAssert = new SoftAssert();
        Scanner sc = null;
        for(int i = 1; i < 3; i++) {
            try {
                info("max size:" + i);
                HistoryLog hl = new HistoryLog(testDir, testFileName, i, false);

                for(String entry : entries) {
                    hl.add(entry);
                }
                hl.save();

                sc = new Scanner(new File(filePath));
                int index = entries.length - i;
                while(sc.hasNextLine()) {
                    try {
                        softAssert.assertEquals(sc.nextLine(), entries[index++], "mismatch on maxSize: " + i + " index: " + index);  
                    } catch(ArrayIndexOutOfBoundsException e) {
                        
                    }
                   
                }
            }
            catch (IOException e) {
                softAssert.fail(e.getMessage());
            }
            finally {
                if(sc != null) {
                    sc.close();
                }
            }
//            new File(filePath).delete();
        }
        softAssert.assertAll();
    }

    @Test
    public void testDupInLimit() {
        SoftAssert softAssert = new SoftAssert();
        Scanner sc = null;
        try {
            HistoryLog hl = new HistoryLog(testDir, testFileName, 5, true);

            for(String entry : entries) {
                hl.add(entry);
            }
            hl.add(entries[4]);
            hl.add(entries[4]);
            hl.save();

            sc = new Scanner(new File(filePath));
            softAssert.assertEquals(sc.nextLine(), entries[3]);
            softAssert.assertEquals(sc.nextLine(), entries[4]);
            softAssert.assertEquals(sc.nextLine(), entries[5]);
            softAssert.assertEquals(sc.nextLine(), entries[4]);
            softAssert.assertEquals(sc.nextLine(), entries[4]);
        }
        catch (IOException e) {
            softAssert.fail(e.getMessage());
        }
        finally {
            if(sc != null) {
                sc.close();
            }
        }

        softAssert.assertAll();
    }
}
