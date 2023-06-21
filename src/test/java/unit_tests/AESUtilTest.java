
package unit_tests;

import helper.TestConfig;
import utility.AESUtil;

import org.testng.Assert;
import org.testng.annotations.Test;

public class AESUtilTest extends UnitTest {

   /* @Test
    public void encryptDecryptTest() throws Throwable {
        String plainText = "myPassword123";
        // Encrypt plainText
        String cipherText  = AESUtil.encrypt(plainText);
        // Assert that cipherText is different to plainText
        Assert.assertNotEquals(plainText, cipherText);
        // Decrypt cipherText
        String decryptedText = AESUtil.decrypt(cipherText);
        // Assert decryption has worked
        Assert.assertEquals(plainText, decryptedText);
    }

    @Test
    public void encryptTest() throws Throwable {
        String plainText = "this_is_a_secret";
        // Encrypt plainText
        String cipherText  = AESUtil.encrypt(plainText);
        // Assert that cipherText is different to plainText
        Assert.assertNotSame(plainText, cipherText);
    }

    @Test
    public void decryptTest() throws Throwable {
        String plainText = "this_is_a_secret";
        String cipherText = "GKULVnffrPWkrHKN3OX7Ng==:f6D0UNq5xOtL5mRTqSWAANNMRa2BQM2o2384b29I07A=";
        // Decrypt cipherText
        String decryptedText = AESUtil.decrypt(cipherText);
        // Assert decryption has worked
        Assert.assertEquals(plainText, decryptedText);
    }

    @Test
    public void isEncryptedTest(){
        // Returns true for encrypted value
        String encrypted = "ENC(qZVrV8U7aSWCgGetUMcPHA==:E0pgfrMUwfLM1oEWGr0oRg==)";
        Assert.assertTrue(AESUtil.isEncrypted(encrypted));
        // Returns false for non-encrypted value
        String notEncrypted = "qZVrV8U7aSWCgGetUMcPHA==:E0pgfrMUwfLM1oEWGr0oRg==";
        Assert.assertFalse(AESUtil.isEncrypted(notEncrypted));
        // Returns false for null value
        Assert.assertFalse(AESUtil.isEncrypted(null));
    }

    @Test (dependsOnMethods = "isEncryptedTest")
    public void decryptPropertyFromConfigTest() {
        String plainText = "password123";
        // Get decrypted property value
        String decryptedText = TestConfig.getPropertyValue("test_encryption_value");
        // Assert decryption has worked
        Assert.assertEquals(plainText, decryptedText);
    }
*/
}
