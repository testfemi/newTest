package utility;

import helper.Log;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Objects;

/**
 * Utility class which provides encryption and decryption functionality within the framework.  As the class name implies,
 * the chosen cipher/algorithm for encrypting values is the AES (Advanced Encryption Standard) cipher, established by the
 * U.S. NIST (National Institute of Standards and Technology), with the CBC (Cipher Block Chaining) variation for defining
 * the mode of operation for this algorithm.
 * <p>
 * The cryptographic key (i.e. secret key) used for encryption/decryption is a password-based AES-192 (i.e. 192 bit key)
 * variation.  Because this is password-based, the AES secret key used for encryption/decryption is derived from a given
 * password.  For generating this password-based key, a PBKDF2 password-based key derivation function and salt value is
 * used for turning the password into a secret key.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Advanced_Encryption_Standard">...</a>
 * @see <a href="https://www.nist.gov/publications/advanced-encryption-standard-aes">...</a>
 *
 * /* TODO - Implement functionality to automatically rotate the SALT and PASSWORD values stored on the system and update currently encrypted values accordingly
 */
public class AESUtil {

    private static final String CIPHER = "AES", // Use this algorithm for performing encryption/decryption operations
                  AES_CIPHER_VARIATION = "AES/CBC/PKCS5Padding", // AES variation, for specifying the mode operation in order to strengthen the effect of the encryption algorithm
              KEY_GENERATION_ALGORITHM = "PBKDF2WithHmacSHA256", // Use this algorithm for generating the AES secret key from a given password
                ENCRYPTED_VALUE_PREFIX = "ENC(",
                ENCRYPTED_VALUE_SUFFIX = ")";
    private static final int ITERATIONS = 65536, // Number of times that the KEY_GENERATION_ALGORITHM applies the transformation function
                             KEY_LENGTH = 192; // Size/length of the key in bits, determines the number of rounds for the algorithm to perform during encryption

    /**
     * Generates and returns the password-based secret key for performing encryption/decryption.
     * This secret key is required by both the {@link #encrypt(String)} and {@link #decrypt(String)}
     * subroutines in order to attempt their respective operations.
     *
     * @return the {@link SecretKey} for performing encryption/decryption
     * @throws Throwable if the chosen AES algorithm is not available OR
     * if there was another unknown issue when generating the secret key
     */
    @Contract(" -> new")
    private static @NotNull SecretKey getSecretKey() throws Throwable {
        SecretKeyFactory factory = null;
        KeySpec spec = null;
        try {
            // Generate a password-based secret key (i.e. use local system env variable for producing this)
            factory = SecretKeyFactory.getInstance(KEY_GENERATION_ALGORITHM);
            spec = new PBEKeySpec(
                    System.getenv("AUTOMATION_FRAMEWORK_PASSWORD").toCharArray(),
                    System.getenv("AUTOMATION_FRAMEWORK_SALT").getBytes(),
                    ITERATIONS,
                    KEY_LENGTH
            );
            return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), CIPHER);
        } catch (NoSuchAlgorithmException e) {
            Log.error(e, "The chosen AES encryption algorithm is not available");
        } catch (InvalidKeySpecException e) {
            Log.error(e, "There was an issue creating the AES secret key");
        }
        return new SecretKeySpec(Objects.requireNonNull(factory).generateSecret(spec).getEncoded(), CIPHER);
    }

    /**
     * Evaluates whether the given {{@param value}} is provided as an encrypted/cipher-text value
     * or not. The {@param value} is considered encrypted if it is surrounded by "ENC(...)" (i.e.
     * starts with the prefix "ENC(" and ends with the suffix ")").
     *
     * @param value the encrypted/cipher-text value
     * @return true if the value is surrounded by "ENC(...)", otherwise false
     */
    public static boolean isEncrypted(String value) {
        if (value == null) {
            Log.debug("The data input provided is not recognised as being encrypted: null");
            return false;
        }
        final String trimmed = value.trim();
        boolean isEncrypted = trimmed.startsWith(ENCRYPTED_VALUE_PREFIX) && trimmed.endsWith(ENCRYPTED_VALUE_SUFFIX);
        Log.debug(isEncrypted
                ? "The data input provided was recognised as being encrypted: %s"
                : "The data input provided was not recognised as being encrypted: %s",
                value);
        return isEncrypted;
    }

    /**
     * If the {@param value} provided is recognised as being encrypted (i.e. implicit call
     * made to {@link #isEncrypted(String)} returns true), then the "ENC(" and ")" markers
     * which signify that this string is encrypted will be removed from this {@param value}.
     *
     * If the {@param value} is not recognised as being encrypted, then it will be returned
     * without being modified.
     *
     * @param value the encrypted/cipher-text value
     * @return if recognised as being encrypted, the {@param @value} with the substrings "ENC("
     * and ")" removed from it, otherwise return {@param value} unchanged
     */
    public static String removeEncryptionMarkers(String value){
        if(isEncrypted(value)) {
            Log.debug("Removing encryption markers from the provided data input: %s", value);
            String cleaned = value.substring(ENCRYPTED_VALUE_PREFIX.length(),
                    value.length() - ENCRYPTED_VALUE_SUFFIX.length());
            Log.debug("Encryption markers removed, cleaned input is: %s", cleaned);
            return cleaned;
        }else{
            Log.warn("The data input provided is not encrypted: %s", value);
            Log.debug("Returning value as it was provided");
            return value;
        }
    }

    /**
     * Encrypt the given {@param plainText} value using the AES cipher/algorithm provided
     * by this class utility.  An implicit call is made to {@link #getSecretKey()} in order
     * to acquire the secret key for encryption.
     *
     * @param plainText the plain-text value to encrypt
     * @return the encrypted/cipher-text value for the {@param plainText}
     * @throws Throwable if there is an error/issue with any of the following:
     * <ul>
     * <li>The padding scheme for the configured AES cipher variation is not available</li>
     * <li>The chosen AES encryption algorithm is not available</li>
     * <li>If there is an issue with the AES secret key which was configured for encryption</li>
     * <li>If there was an issue with the AES initialisation vector (IV) which was configured for encryption</li>
     * <li>If the {@param plainText} value fails to/cannot be encrypted</li>
     * <li>If there is an issue with padding bytes when encrypting {@param plainText}</li>
     * </ul>
     */
    public static @NotNull String encrypt(@NotNull String plainText) throws Throwable {
        byte[] iv = null, cipherText = null;
        try{
            Cipher cipher = Cipher.getInstance(AES_CIPHER_VARIATION);
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey());
            AlgorithmParameters params = cipher.getParameters();
            IvParameterSpec ivSpec = params.getParameterSpec(IvParameterSpec.class);
            iv = ivSpec.getIV();
            cipherText = cipher.doFinal(plainText.getBytes());
        } catch (NoSuchPaddingException e) {
            Log.error(e, "The padding scheme for the configured AES cipher variation is not available");
        } catch (NoSuchAlgorithmException e) {
            Log.error(e, "The chosen AES encryption algorithm is not available");
        } catch (InvalidKeyException e) {
            Log.error(e, "There was an issue with the AES secret key which was configured for encryption");
        } catch (InvalidParameterSpecException e){
            Log.error(e, "There was an issue with the AES initialisation vector (IV) which was configured for encryption");
        } catch (IllegalBlockSizeException e) {
            Log.error(e, "Failed to encrypt the data input provided: %s", plainText);
        } catch (BadPaddingException e) {
            Log.error(e, "There was an issue with padding bytes when encrypting the data input provided: %s", plainText);
        }
        return Base64.getEncoder().encodeToString(iv) +
                ":" +
                Base64.getEncoder().encodeToString(cipherText);
    }

    /**
     *
     * @param cipherText
     * @return
     * @throws Throwable
     */
    /**
     * Decrypt the given {@param cipherText} value using the AES cipher/algorithm provided
     * by this class utility.  An implicit call is made to {@link #getSecretKey()} in order
     * to acquire the secret key for decryption.
     *
     * @param cipherText the cipher-text value to decrypt
     * @return the decrypted/plain-text value for the {@param cipherText}
     * @throws Throwable if there is an error/issue with any of the following:
     * <ul>
     * <li>The padding scheme for the configured AES cipher variation is not available</li>
     * <li>The chosen AES decryption algorithm is not available</li>
     * <li>If there is an issue with the AES secret key which was configured for decryption</li>
     * <li>If there was an issue with the AES initialisation vector (IV) which was configured for decryption</li>
     * <li>If the {@param cipherText} value fails to/cannot be decrypted</li>
     * <li>If there is an issue with padding bytes when decrypting {@param cipherText}</li>
     * </ul>
     */
    @Contract("_ -> new")
    public static @NotNull String decrypt(@NotNull String cipherText) throws Throwable {
        byte[] plainText = null;
        try {
            String[] split = cipherText.split(":");
            String iv = split[0];
            String secret = split[1];
            Cipher cipher = Cipher.getInstance(AES_CIPHER_VARIATION);
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), new IvParameterSpec(Base64.getDecoder().decode(iv)));
            plainText = cipher.doFinal(Base64.getDecoder().decode(secret));
        } catch (NoSuchPaddingException e) {
            Log.error(e, "The padding scheme for the configured AES cipher variation is not available");
        } catch (NoSuchAlgorithmException e) {
            Log.error(e, "The chosen AES decryption algorithm is not available");
        } catch (InvalidKeyException e) {
            Log.error(e, "There was an issue with the AES secret key which was configured for decryption");
        } catch (InvalidAlgorithmParameterException e) {
            Log.error(e, "There was an issue with the AES initialisation vector (IV) which was configured for decryption");
        } catch (IllegalBlockSizeException e) {
            Log.error(e, "Failed to decrypt the data input provided: %s", cipherText);
        } catch (BadPaddingException e) {
            Log.error(e, "There was an issue with padding bytes when decrypting the data input provided: %s", cipherText);
        }
        return new String(Objects.requireNonNull(plainText));
    }

    /**
     * Require this main method as entry point into this class for the "encrypt_value.bat" file.
     *
     * @param args {@link String} arguments to pass into this main function
     */
    public static void main(String @NotNull [] args) throws Throwable {
        if (args.length >= 1) {
            System.out.println(encrypt(args[0]));
        } else {
            System.out.println("Command line parameters are missing");
        }
    }

}