package it.units.assistants;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;

public class PasswordAssistant {
    /*
     * https://www.baeldung.com/java-password-hashing
     * https://dev.to/awwsmm/how-to-encrypt-a-password-in-java-42dh
     */

    private static final SecureRandom numeroCasuale = new SecureRandom();
    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH = 512;
    private static final String ALGORITHM = "PBKDF2WithHmacSHA512";

    public static String generateSalt() {
        byte[] salt = new byte[16];
        numeroCasuale.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    public static String hashPassword(String password, String salt) {

        char[] chars = password.toCharArray();
        byte[] bytes = salt.getBytes();

        PBEKeySpec spec = new PBEKeySpec(chars, bytes, ITERATIONS, KEY_LENGTH);

        Arrays.fill(chars, Character.MIN_VALUE);

        try {
            SecretKeyFactory fac = SecretKeyFactory.getInstance(ALGORITHM);
            byte[] securePassword = fac.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(securePassword);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            System.err.println("Exception encountered in hashPassword()");
            return null;
        } finally {
            spec.clearPassword();
        }
    }

    public static boolean isPasswordWrong(String pswInput, String pswSaved, String salt) {
        String pswInputHash = hashPassword(pswInput, salt);
        return pswInputHash == null || !pswInputHash.equals(pswSaved);
    }
}
