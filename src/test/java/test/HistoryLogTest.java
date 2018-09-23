package test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import org.tonyhsu17.utilities.HistoryLog;



/**
 * Unit Tests for {@link HistoryLog}
 * 
 * @author Tony Hsu
 *
 */
final class HistoryLogTest {
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
                hl.addToWriteList(entry);
            }
            hl.writeToFile();

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
    
    @Test(dependsOnMethods={"testFileWrite"})
    public void testFileReadIn() {
        SoftAssert softAssert = new SoftAssert();
        try {
            FileWriter fw = new FileWriter(filePath);
            for(String entry : entries) {
                fw.write(entry + System.lineSeparator());
            }
            fw.close();
            
            for(String entry : entries) {
                softAssert.assertTrue(hl.isInHistory(entry), "str: [" + entry + "] not found");
            }
        }
        catch (IOException e) {
            Assert.fail(e.getMessage());
        }
        softAssert.assertAll();
    }

    @Test
    public void testSizeLimit() {
        SoftAssert softAssert = new SoftAssert();
        Scanner sc = null;
        for(int i = 1; i < entries.length; i++) {
            try {
                HistoryLog hl = new HistoryLog(testDir, testFileName, i, false);

                for(String entry : entries) {
                    hl.addToWriteList(entry);
                }
                hl.writeToFile();

                sc = new Scanner(new File(filePath));
                int index = entries.length - i;
                while(sc.hasNextLine()) {
                    softAssert.assertEquals(sc.nextLine(), entries[index++], "mismatch on maxSize: " + i + " index: " + index);
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
                hl.addToWriteList(entry);
            }
            hl.addToWriteList(entries[4]);
            hl.addToWriteList(entries[4]);
            hl.writeToFile();

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
