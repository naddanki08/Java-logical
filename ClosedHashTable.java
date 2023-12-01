import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ClosedHashTable {
    private static final int TABLE_SIZE = 997;

    private String[] hashTable;
    private String[] words;
    private int[] hashValues;

    public ClosedHashTable() {
        hashTable = new String[TABLE_SIZE];
        words = new String[TABLE_SIZE];
        hashValues = new int[TABLE_SIZE];
        for (int i = 0; i < TABLE_SIZE; i++) {
            hashTable[i] = "-1";  // Initialize the hash table with -1 to indicate an empty slot
        }
    }

    
    public static int computeHash(String word) {
        int hash = 0;
        for (char c : word.toCharArray()) {
            hash = 31 * hash + c;
        }
        return Math.abs(hash);
    }

    public void insert(String word, int hashValue) {
        int hashAddress = hashValue % TABLE_SIZE;
        int originalHashAddress = hashAddress;

        // Linear probing to find the next available slot
        while (!hashTable[hashAddress].equals("-1") && !hashTable[hashAddress].equals(word)) {
            hashAddress = (hashAddress + 1) % TABLE_SIZE;
            if (hashAddress == originalHashAddress) {
                // Table is full, handle accordingly (resize, etc.)
                System.out.println("Table is full. Cannot insert: " + word);
                return;
            }
        }

        // Insert the word and its hash value
        hashTable[hashAddress] = String.valueOf(hashAddress);
        words[hashAddress] = word;
        hashValues[hashAddress] = hashValue;
    }

    public void displayTable() {
        for (int i = 0; i < TABLE_SIZE; i++) {
            if (hashTable[i].equals("-1")) {
                System.out.println(i + ", -1, -1");
            } else {
                System.out.println(i + ", " + words[i] + ", " + hashValues[i]);
            }
        }
    }

    public static void main(String[] args) {
        ClosedHashTable closedHashTable = new ClosedHashTable();

        try (BufferedReader reader = new BufferedReader(new FileReader("C:\\dev\\local-folder\\DeclarationOfIndependence.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] words = line.split("\\s+");
                for (String word : words) {
                    int hashValue = computeHash(word);
                    closedHashTable.insert(word, hashValue);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        closedHashTable.displayTable();
    }
}


