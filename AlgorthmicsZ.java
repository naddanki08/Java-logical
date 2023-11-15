package com.nagesh.dsa.linkedlist;

public class AlgorthmicsZ {

    public static void main(String[] args) {
        int n = 5; // Order of the Gray Code
        generateCompleteTable(n);
    }

    private static void generateCompleteTable(int n) {
        String[] musicians = {"Silent Stage", "Alfie", "Berty", "Crizz", "Dietz", "Elmer"};
        int prevMusicianIndex = 0;

        // Print table header
        System.out.format("%-6s%-10s%-20s%-20s%-20s\n", "Index", "Gray Code", "Players", "Playing", "Action");

        // Loop through the Gray Code sequence
        for (int i = 0; i < (1 << n); i++) {
            // Generate the Gray Code
            int grayCode = i ^ (i >> 1);

            // Calculate the difference between the current and previous musician index
            int diff = grayCode ^ prevMusicianIndex;

            // Initialize StringBuilder to store players and playing actions
            StringBuilder players = new StringBuilder();
            StringBuilder playingAction = new StringBuilder();

            // Iterate through the musicians involved in the transition
            while (diff > 0) {
                // Find the least significant bit in the difference
                int leastSignificantBit = diff & -diff;

                // Determine the new musician index
                int newMusicianIndex = Integer.numberOfTrailingZeros(leastSignificantBit);

                // Append players and playing actions to StringBuilder
                players.append(musicians[newMusicianIndex]).append(", ");
                playingAction.append(musicians[newMusicianIndex]).append(" Joins, ");

                // Update the difference and previous musician index
                diff -= leastSignificantBit;
                prevMusicianIndex = newMusicianIndex;
            }

            // Print the table row
            System.out.format("%-6d%-10s%-20s%-20s%-20s\n", i, Integer.toBinaryString(grayCode),
                    players.toString(), playingAction.toString(), "Silent Stage");
        }
    }
}
