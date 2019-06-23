package test;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import org.tonyhsu17.utilities.StringUtils;



public class StringUtilsTest {

    @Test
    public void testSanitizeFilename() {
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(StringUtils.santizeFileName("Hello$?!*/\\:*?<>|", ""), "Hello$!");
        softAssert.assertAll();
    }
}
