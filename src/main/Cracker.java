///CS 645 - Project 1 - Chris Konstantinidis, Nat Louis, Kleo Mimini

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cracker {
    
    public static void main(String[] args) {
        // Data structures to store shadow file information
        Map<String, String> userSalts = new HashMap<>();
        Map<String, String> userHashes = new HashMap<>();
        List<String> passwords = new ArrayList<>();
        
        // Read shadow file (Linux-style)
        try (BufferedReader br = new BufferedReader(new FileReader("shadow"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length >= 2 && parts[1].startsWith("$1$")) {
                    String username = parts[0];
                    String shash = parts[1];
                    
                    // Parse the shash field: $1$salt$hash
                    String[] hashParts = shash.split("\\$");
                    // hashParts[0] is empty, hashParts[1] is "1", hashParts[2] is salt, hashParts[3] is hash
                    if (hashParts.length >= 4) {
                        String salt = hashParts[2];
                        String hash = hashParts[3];
                        userSalts.put(username, salt);
                        userHashes.put(username, hash);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading shadow file: " + e.getMessage());
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
                // Use MD5Shadow.crypt() to generate the hash
                String computedHash = MD5Shadow.crypt(password, salt);
                
                // Check if computed hash matches stored hash
                if (computedHash.equals(storedHash)) {
                    System.out.println(username + ":" + password);
                    break;
                }
            }
        }
    }
}
