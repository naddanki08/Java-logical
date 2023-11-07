/**
 *
 */


import java.util.HashMap;
import java.util.Map;

/**
 * @author naddanki
 *
 */
public class SubirachsMagicSquare {
      private static final int[][] MAGIC_SQUARE = { { 1, 14, 14, 4 }, { 11, 7, 6, 9 }, { 8, 10, 10, 5 },
                  { 13, 2, 3, 15 } };

      private static final int TARGET_SUM = 33;

      private static boolean checkCombinationSum(final int[] indices) {
            int sum = 0;

            for (int index : indices) {
                  System.out.println("MAGIC_SQUARE[" + index / MAGIC_SQUARE.length + "][" + index % MAGIC_SQUARE.length
                              + "] :" + MAGIC_SQUARE[index / MAGIC_SQUARE.length][index % MAGIC_SQUARE.length]);

                  sum += MAGIC_SQUARE[index / MAGIC_SQUARE.length][index % MAGIC_SQUARE.length];
            }
            return sum == TARGET_SUM;
      }

      private static void countAllCombinations() {
            int count = 0;
            int n = MAGIC_SQUARE.length * MAGIC_SQUARE.length;
            // Use bit manipulation to generate all possible subsets
            for (int i = 0; i < (1 << n); i++) {
                  int sum = 0;
                  for (int j = 0; j < n; j++) {
                        // Check if j-th element is included in this subset
                        if ((i & (1 << j)) > 0) {
                              sum += MAGIC_SQUARE[j / MAGIC_SQUARE.length][j % MAGIC_SQUARE.length];
                        }
                  }
                  if (sum == TARGET_SUM) {
                        count++;
                  }
            }
            System.out.println("Total combinations with sum " + TARGET_SUM + ": " + count);
      }

      private static void countAllSums() {
            // This map will hold all possible sums and the number of ways they can
            // be achieved.
            Map<Integer, Integer> sumCounts = new HashMap<>();
            // Initialize the map with the base case: there's one way to achieve a
            // sum of 0 (by choosing no elements).
            sumCounts.put(0, 1);

            // Iterate over every cell in the magic square to build up the sum
            // combinations.
            for (int i = 0; i < MAGIC_SQUARE.length; i++) {
                  for (int j = 0; j < MAGIC_SQUARE[i].length; j++) {
                        // Temporary map to store the new sums formed by adding the
                        // current cell's value.
                        Map<Integer, Integer> newSums = new HashMap<>();

                        // Update the sum combinations with the current cell's value.
                        for (Map.Entry<Integer, Integer> entry : sumCounts.entrySet()) {
                              int newSum = entry.getKey() + MAGIC_SQUARE[i][j];
                              // The number of ways to achieve the new sum is increased by
                              // the number of ways to achieve the current sum.
                              newSums.put(newSum, newSums.getOrDefault(newSum, 0) + entry.getValue());
                        }

                        // Merge the new sums with the existing sums in the sumCounts
                        // map.
                        for (Map.Entry<Integer, Integer> entry : newSums.entrySet()) {
                              sumCounts.put(entry.getKey(), sumCounts.getOrDefault(entry.getKey(), 0) + entry.getValue());
                        }
                  }
            }

            // Output the sumCounts map or further process it as needed.
            sumCounts.forEach((sum, count) -> System.out.println("Sum " + sum + " can be made in " + count + " ways."));

            // Find the sum with the maximum number of combinations.
            int maxCount = 0;
            int maxSum = 0;
            for (Map.Entry<Integer, Integer> entry : sumCounts.entrySet()) {
                  if (entry.getValue() > maxCount) {
                        maxCount = entry.getValue();
                        maxSum = entry.getKey();
                  }
            }

            System.out.println("The sum that can be created with the greatest number of combinations is: " + maxSum
                        + ", which can be created in " + maxCount + " ways.");
      }

      private static void countFourElementCombinations() {
            int count = 0;
            int size = MAGIC_SQUARE.length;
            // Iterating over all possible 4-element combinations
            for (int i = 0; i < size * size; i++) {
                  for (int j = i + 1; j < size * size; j++) {
                        for (int k = j + 1; k < size * size; k++) {
                              for (int l = k + 1; l < size * size; l++) {
                                    if (checkCombinationSum(new int[] { i, j, k, l })) {
                                          count++;
                                    }
                              }
                        }
                  }
            }
            System.out.println("Total 4-element combinations with sum " + TARGET_SUM + ": " + count);
      }

      public static void main(final String[] args) {
            countFourElementCombinations(); // For counting 4-element combinations.
            countAllCombinations(); // For counting all combinations.
            countAllSums(); // For counting all possible sums.
      }

}
