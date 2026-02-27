# Password Cracking Assignment

This project implements offline dictionary attacks on password shadow files using Java.

## Project Structure

```
Project1CS645/
├── src/main/
│   ├── SimpleCracker.java    # Part 1: Simple shadow file cracker
│   ├── Cracker.java           # Part 2: Linux MD5 shadow cracker
│   └── MD5Shadow.java         # MD5 crypt implementation
├── shadow-simple              # Simplified shadow file (Part 1)
├── shadow                     # Linux-style MD5 shadow file (Part 2)
├── common-passwords.txt       # Dictionary wordlist
└── PART3_REPORT.txt          # Detailed report for Part 3
```

## Requirements

- Java Development Kit (JDK) 8 or higher
- shadow-simple and shadow files in the project root
- common-passwords.txt in the project root

## How to Execute

### Part 1: SimpleCracker (Simplified Shadow File)

This program cracks passwords from a simplified shadow file with format: `username:salt:hash`

```bash
# Compile
javac src/main/SimpleCracker.java

# Run
java -cp src/main SimpleCracker
```

**Expected Output:** All 10 users with their passwords (e.g., `user0:williamsburg`)

### Part 2: Cracker (Linux-style MD5 Shadow)

This program cracks passwords from a real Linux shadow file using MD5 crypt.

```bash
# Compile (need both Cracker and MD5Shadow)
javac src/main/Cracker.java src/main/MD5Shadow.java

# Run
java -cp src/main Cracker
```

**Expected Output:** 5 users with their passwords:
- user0:nepenthe
- user1:zmodem
- user4:darkchocolate
- user5:yellowstone
- user9:anthropogenic

## Part 3: Additional Password Discovery

For Part 3, the RockYou password list was used to discover an additional password not in the original `common-passwords.txt` file.

**Result:** Found `user4:darkchocolate`

**Wordlist Used:** RockYou (https://github.com/brannondorsey/naive-hashcat/releases/download/data/rockyou.txt)
- 14.3+ million real-world passwords from a breach
- Widely used in penetration testing and security research

See `PART3_REPORT.txt` for complete documentation.

## Technical Notes

### Bug Fix in MD5Shadow.java
The original MD5Shadow.java had a bug where line 120 was duplicating and overwriting the hash computation from the loop above it. This line was removed to fix the hash generation.

### Implementation Details
- **SimpleCracker**: Concatenates salt + password, computes MD5, compares with stored hash
- **Cracker**: Uses MD5Shadow.crypt() which implements the Linux MD5-based crypt algorithm
- Both programs are general-purpose and work with any shadow files in the correct format

## Testing

The programs have been tested and successfully crack passwords from both provided shadow files. They are designed to work with additional shadow files without modification.

## Author

Created for CS645 Password Cracking Assignment
