import java.io.*;
import java.util.*;


public class HashTable {
    public String[] hashWord; //saves hash words
    public int[] hashAddresses;
    public final int capacity;
    public static final int C = 123; // constant larger than every ord(ci)
    public static final int M = 997;
    public int longestCollisionLength;
    public String farthestWordFromActualHash;
    public int[] countOfHashStrings;
    public int indexOfCluster;
    public int actualHCodeOfLongestCollision;
    public int maxHashCount;
    public int maxOccuringHash;

    public HashTable(int capacity){
        this.capacity = capacity;
        this.hashWord = new String[this.capacity];
        this.hashAddresses = new int[this.capacity];
        for(int i = 0 ; i < this.capacity ; i++){
            this.hashAddresses[i] = i;
        }
        this.longestCollisionLength = -1;
        this.farthestWordFromActualHash = "-1";
        this.indexOfCluster = 0;
        this.actualHCodeOfLongestCollision = 0;
        this.maxHashCount = 0;
        this.maxOccuringHash = -1;
        this.countOfHashStrings = new int[this.capacity];
        for(int j = 0; j<this.hashWord.length; j++){ //initially making all indices -1 for empty
            this.hashWord[j] = "-1";
            this.countOfHashStrings[j] = 0;
        }
    }

    public void createHashTable(String fileName){
        try {
            BufferedReader reader = new BufferedReader(new FileReader("C:\\dev\\documents\\Moby-Dick-Chapter-1-groomed.txt"));
            String line;
            ArrayList<String> sentences = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                sentences.add(line);
            }
            Set<String> seenWords = new HashSet<String> ();
            for (String sentence : sentences) {
            	String[] words = sentence.split("[^A-Za-z'-]+");
                for (String word : words) {
					
					  if(seenWords.contains(word)){ continue; }
					  seenWords.add(word);
					 

                    if (word.length() == 0) { // to not insert 0
                        continue;
                    }
                    int hCode = hashCode(word);
                    this.countOfHashStrings[hCode]++;
                    if(this.countOfHashStrings[hCode] > maxHashCount){
                        maxHashCount = this.countOfHashStrings[hCode];
                        maxOccuringHash = hCode;
                    }
                    int value = hCode;
                    int curCollisionLength = 1;
                    while(true){
                        if(hashWord[value].equals("-1")){
                            hashWord[value] = word + " " + hCode;
                            break;
                        }
                        curCollisionLength++;
                        value = (value + 1)%this.capacity;
                    }
                    if(curCollisionLength > this.longestCollisionLength){
                        this.longestCollisionLength = curCollisionLength;
                        this.farthestWordFromActualHash = word;
                        this.indexOfCluster = value;
                        this.actualHCodeOfLongestCollision = hCode;
                    }
                }
            }




        } catch (IOException e) {
            System.out.println("File Not Found");
            e.printStackTrace();
        }
    }

    public int hashCode(String str) {
        int hashValue = 0;
        for (int i = 0; i < str.length(); i++) {
            if (isAlphabeticOrApostropheOrHyphen(str.charAt(i))) {
                hashValue = (hashValue * C + getASCIIValue(str.charAt(i))) % M; //generating hash code
            }
        }
        return hashValue;
    }

    public boolean isAlphabeticOrApostropheOrHyphen(char character) { //checks for char within condition
        return (character >= 'A' && character <= 'Z') ||
                (character >= 'a' && character <= 'z') ||
                (character == '\'') ||
                (character == '-');
    }

    public int getASCIIValue(char character) {
        return (int) character;
    }


    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter File Name: ");
        System.out.println("Enter the exact file name as program is Case Sensitive, like DeclarationOfIndependence.txt");
        String fileName = sc.next();
        System.out.println("Enter Hash Table Size: ");
        int hashSize = sc.nextInt();

        HashTable ht = new HashTable(hashSize);
        ht.createHashTable(fileName);

        System.out.println("HASH TABLE: ");
        for(int i = 0; i<ht.hashAddresses.length; i++){
            System.out.println(ht.hashAddresses[i]+"\t\t"+ht.hashWord[i]);
        }

        // finding number of non-empty addresses
        int countAddr = 0;
        for(int k = 0; k<ht.hashWord.length; k++){
            if(!ht.hashWord[k].equals("-1")){
                countAddr++;
            }
        }
        float loadFactor = (float)countAddr/ht.capacity;
        int numberOpenCells = ht.capacity - countAddr;
        System.out.println("Number of non-empty addresses: "+countAddr);
        System.out.println("Number of open cells: "+numberOpenCells);
        System.out.println("Load factor = "+ loadFactor);

        // finding the longest empty area in the table
        int largestLen = -1;
        int startOfLargestGap = -1;
        int endOfLargestGap = -1;
        for(int i = 0 ; i < ht.hashAddresses.length ; i++){
            int curLen = 0;
            int j = i;
            while(j<ht.hashAddresses.length && ht.hashWord[j].equals("-1")){
                curLen++;
                j++;
            }
            if(curLen != 0 && curLen > largestLen){
                largestLen = curLen;
                startOfLargestGap = i;
                endOfLargestGap = j;
            }
        }




        System.out.println("Longest empty area in the table: "+largestLen+" at position "+startOfLargestGap+" (inclusive) to position "+endOfLargestGap+" (exclusive)");


     // Finding the longest (largest) cluster in the table or longest collision length
     // Longest cluster - longest filled rows
        int largestLen1 = -1;
        int startOfLargestCluster = -1;
        int endOfLargestCluster = -1;

        for (int i = 0; i < ht.hashAddresses.length; i++) {
            int curLen = 0;
            int j = i;

            do {
                curLen++;
                j = (j + 1) % ht.capacity;
            } while (j != i && (!ht.hashWord[j].equals("-1")));
            
           // System.out.println("Cluster: " + curLen + " starting at index: " + i + " ending at index: " + (j - 1 + ht.capacity) % ht.capacity);

            // Adjust the ending index to reflect the correct position in the array
            int adjustedEndIndex = (j - 1 + ht.capacity) % ht.capacity;
            
            if (curLen > largestLen1) {
                largestLen1 = curLen;
                startOfLargestCluster = i;
                endOfLargestCluster = adjustedEndIndex;
            }
        }

        
        
        System.out.println("Largest cluster length: "+ largestLen1 + " and the index of largest cluster: " + startOfLargestCluster + " end index: "+endOfLargestCluster);

        
        int maxDistinctWords = 0;
        int hashWithMaxDistinctWords = -1;

        for (int i = 0; i < ht.countOfHashStrings.length; i++) {
            if (ht.countOfHashStrings[i] > maxDistinctWords) {
                maxDistinctWords = ht.countOfHashStrings[i];
                hashWithMaxDistinctWords = i;
            }
        }

        System.out.println("Max occurring Hash: " + hashWithMaxDistinctWords + " and the number of words having this hash value: " + maxDistinctWords);
        

        // finding the word that's placed in the table farthest from its actual hash value
        // loop over hashaddress and at that index in the hashword is contract then save that address
        //   int saveAddr = 0;
        //   for(int i = 0; i<ht.hashAddresses.length; i++){
        //       if(ht.hashWord[i].contains(ht.farthestWordFromActualHash)){
        //           saveAddr = ht.hashAddresses[i];
        //       }
        //   }
        System.out.println("The word that's placed in the table farthest from its actual hash value is: " + ht.farthestWordFromActualHash+ " from hash address " + ht.actualHCodeOfLongestCollision + " all the way to position " + ht.indexOfCluster + " and the length of how far away is it from its actual hash value: " + ht.longestCollisionLength);



    }
}
