package com.nagesh.dsa.linkedlist;

public class SecondProblem {

        public static void main(String[] args) {
            int n = 5; // Order of the Gray Code
            generateCompleteTableWithSequence(n);
        }

        private static void generateCompleteTableWithSequence(int n) {
            String[] musicians = {"Silent Stage", "Alfie", "Berty", "Crizz", "Dietz", "Elmer"};
            int prevMusicianIndex = 0;

            // Initialize StringBuilder to store the sequence
            StringBuilder sequence = new StringBuilder();

            // Print table header
            System.out.format("%-6s%-10s%-20s%-20s%-20s%-30s\n", "Index", "Gray Code", "Players", "Playing", "Action", "Sequence");

            // Loop through the Gray Code sequence
            for (int i = 0; i < (1 << n); i++) {
                int grayCode = i ^ (i >> 1);
                int diff = grayCode ^ prevMusicianIndex;

                StringBuilder players = new StringBuilder();
                StringBuilder playingAction = new StringBuilder();

                // Iterate through the musicians involved in the transition
                while (diff > 0) {
                    int leastSignificantBit = diff & -diff;
                    int newMusicianIndex = Integer.numberOfTrailingZeros(leastSignificantBit);

                    players.append(musicians[newMusicianIndex]).append(", ");
                    playingAction.append(musicians[newMusicianIndex]).append(" Joins, ");

                    // Update the sequence
                    sequence.append(musicians[newMusicianIndex]).append(", ");

                    diff -= leastSignificantBit;
                    prevMusicianIndex = newMusicianIndex;
                }

                System.out.format("%-6d%-10s%-20s%-20s%-20s%-30s\n", i, Integer.toBinaryString(grayCode),
                        players.toString(), playingAction.toString(), "Silent Stage", sequence.toString());
            }
        }
    }

