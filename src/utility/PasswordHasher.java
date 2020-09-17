package utility;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

public class PasswordHasher {

    private SecureRandom random;
    byte salt[];
    KeySpec spec;
    SecretKeyFactory factory;

    public String SHA1hash(String password, int iterasions){
        random = new SecureRandom();
        salt = new byte[16];
        random.nextBytes(salt);
        System.out.println(byteToHex(salt));
        spec = new PBEKeySpec(password.toCharArray(), salt, iterasions, 128);
        try{
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        } catch (NoSuchAlgorithmException nsae){
            nsae.printStackTrace();
        }
        try{
            return byteToHex(factory.generateSecret(spec).getEncoded());
        }catch (InvalidKeySpecException ikse){
            ikse.printStackTrace();
        }
        return null;
    }

    public String SHA1hash(String password, int iterasions, String salt){
        spec = new PBEKeySpec(password.toCharArray(), hexToByte(salt), iterasions, 128);
        try{
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        } catch (NoSuchAlgorithmException nsae){
            nsae.printStackTrace();
        }
        try{
            return byteToHex(factory.generateSecret(spec).getEncoded());
        }catch (InvalidKeySpecException ikse){
            ikse.printStackTrace();
        }
        return null;
    }

    public String byteToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte aByte : bytes) {
            result.append(String.format("%02x", aByte));
        }
        return result.toString();
    }

    public byte[] hexToByte(String hexString) {
        if (hexString.length() % 2 == 1) {
            throw new IllegalArgumentException(
                    "Invalid hexadecimal String supplied.");
        }

        byte[] bytes = new byte[hexString.length() / 2];
        for (int i = 0; i < hexString.length(); i += 2) {
            bytes[i / 2] = toByte(hexString.substring(i, i + 2));
        }
        return bytes;
    }

    private byte toByte(String hexString) {
        int firstDigit = toDigit(hexString.charAt(0));
        int secondDigit = toDigit(hexString.charAt(1));
        return (byte) ((firstDigit << 4) + secondDigit);
    }

    private int toDigit(char hexChar) {
        int digit = Character.digit(hexChar, 16);
        if(digit == -1) {
            throw new IllegalArgumentException(
                    "Invalid Hexadecimal Character: "+ hexChar);
        }
        return digit;
    }
}
