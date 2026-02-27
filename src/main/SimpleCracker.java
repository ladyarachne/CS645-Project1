import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleCracker {
    
    // Convert byte array to hexadecimal string
    public static String toHex(byte[] bytes) {
        BigInteger bi = new BigInteger(1, bytes);
        return String.format("%0" + (bytes.length << 1) + "X", bi);
    }
    
    // Compute MD5 hash of a string
    public static String md5Hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            return toHex(messageDigest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static void main(String[] args) {
        // Data structures to store shadow file information
        Map<String, String> userSalts = new HashMap<>();
        Map<String, String> userHashes = new HashMap<>();
        List<String> passwords = new ArrayList<>();
        
        // Read shadow-simple file
        try (BufferedReader br = new BufferedReader(new FileReader("shadow-simple"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 3) {
                    String username = parts[0];
                    String salt = parts[1];
                    String hash = parts[2];
                    userSalts.put(username, salt);
                    userHashes.put(username, hash);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading shadow-simple file: " + e.getMessage());
            return;
        }
        
        // Read common-passwords.txt file
        try (BufferedReader br = new BufferedReader(new FileReader("common-passwords.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                passwords.add(line.trim());
            }
        } catch (IOException e) {
            System.err.println("Error reading common-passwords.txt file: " + e.getMessage());
            return;
        }
        
        // Perform dictionary attack
        for (String username : userSalts.keySet()) {
            String salt = userSalts.get(username);
            String storedHash = userHashes.get(username);
            
            for (String password : passwords) {
                // Concatenate salt with password and compute hash
                String testString = salt + password;
                String computedHash = md5Hash(testString);
                
                // Check if computed hash matches stored hash
                if (computedHash.equals(storedHash)) {
                    System.out.println(username + ":" + password);
                    break;
                }
            }
        }
    }
}
