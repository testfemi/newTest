package unit_tests;

import helper.TestConfig;

import org.testng.Assert;
import org.testng.annotations.Test;

public class TestConfigTest extends UnitTest {

    /*@Test
    public void findPropertyFromConfigTest(){
        // Happy path which successfully returns the test value
        String value = "i_am_a_test_value";
        Assert.assertEquals(value, TestConfig.getPropertyValue("test_value"));
    }

    @Test
    public void findBlankEmptyPropertyFromConfigTest(){
        // Returns an empty/blank String when the property is found
        Assert.assertTrue(TestConfig.getPropertyValue("test_empty_blank_value").isEmpty());
        Assert.assertTrue(TestConfig.getPropertyValue("test_empty_blank_value").isBlank());
    }

    @Test
    public void failWithPropertyEqualToNull(){
        // Returns null when the property param supplied is null
        Assert.assertThrows(NullPointerException.class,
                () -> TestConfig.getPropertyValue(null));
    }

    @Test
    public void failToFindPropertyFromConfigTest() {
        // Fails to find property from config file(s)
        Assert.assertThrows(NullPointerException.class,
                () -> TestConfig.getPropertyValue("does_not_exist"));
    }*/

}