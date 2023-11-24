
public class GaussianElimination {

    public static void main(String[] args) {
        int n = 9;
        double[][] coefficients = {
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 122},
                {1, 1, 1, 1, 1, -1, -1, -1, -1, -88},
                {1, -1, 1, -1, 1, -1, 1, -1, 1, 32},
                {1, 1, 0, 0, 0, 0, 0, 0, 0, 3},
                {0, 0, 1, 1, 0, 0, 0, 0, 0, 7},
                {0, 0, 0, 0, 1, 1, 0, 0, 0, 18},
                {0, 0, 0, 0, 0, 0, 0, 1, 1, 76},
                {1, 1, -1, 1, 1, -1, 1, 1, -1, 0},
                {9, -8, 7, -6, 5, -4, 3, -2, 1, 41}
        };

        double[][] lowerTriangularMatrix = new double[n][n + 1];
        double[][] upperTriangularMatrix = solveLinearSystem(coefficients, n, lowerTriangularMatrix);

        // Display the lower triangular matrix
        System.out.println("Lower Triangular Matrix:");
        printMatrix(lowerTriangularMatrix);

        // Display the upper triangular matrix
        System.out.println("\nUpper Triangular Matrix:");
        printMatrix(upperTriangularMatrix);

        // Solve both upper and lower triangular matrices
        double[][] solutionMatrix = solveBothMatrices(upperTriangularMatrix, lowerTriangularMatrix, n);

        // Display the final matrix with the solution in the diagonal
        System.out.println("\nFinal Matrix with Solution in Diagonal:");
        printMatrix(solutionMatrix);

        // Display the diagonal elements as x1, x2, ..., x9
        System.out.println("\nDiagonal Elements (Solution):");
        printDiagonal(solutionMatrix);
    }

    public static double[][] solveLinearSystem(double[][] coefficients, int n, double[][] lowerTriangularMatrix) {
        double[][] augmentedMatrix = new double[n][n + 1];

        // Copy coefficients to augmented matrix
        for (int i = 0; i < n; i++) {
            System.arraycopy(coefficients[i], 0, augmentedMatrix[i], 0, n + 1);
        }

        // Perform Gaussian elimination with partial pivoting
        for (int i = 0; i < n - 1; i++) {
            int pivotRow = i;

            for (int j = i + 1; j < n; j++) {
                if (Math.abs(augmentedMatrix[j][i]) > Math.abs(augmentedMatrix[pivotRow][i])) {
                    pivotRow = j;
                }
            }

            // Swap the current row with the pivot row
            swapRows(augmentedMatrix, i, pivotRow);
            swapRows(lowerTriangularMatrix, i, pivotRow);

            for (int j = i + 1; j < n; j++) {
                double ratio = augmentedMatrix[j][i] / augmentedMatrix[i][i];

                // Update the lower triangular matrix
                lowerTriangularMatrix[j][i] = ratio;

                for (int k = i; k <= n; k++) {
                    augmentedMatrix[j][k] -= ratio * augmentedMatrix[i][k];
                }
            }
        }

        // Back-substitution
        for (int i = 0; i < n; i++) {
            lowerTriangularMatrix[i][n] = augmentedMatrix[i][n];
        }

        return augmentedMatrix;
    }

    public static double[][] solveBothMatrices(double[][] upperMatrix, double[][] lowerMatrix, int n) {
        double[][] solutionMatrix = new double[n][n + 1];

        // Solve upper triangular matrix
        double[] upperSolution = backSubstitution(upperMatrix, n);

        // Solve lower triangular matrix
        for (int i = 0; i < n; i++) {
            solutionMatrix[i][n] = lowerMatrix[i][n];
            for (int j = 0; j < i; j++) {
                solutionMatrix[i][n] -= lowerMatrix[i][j] * solutionMatrix[j][n];
            }

            double diagonalValue = lowerMatrix[i][i];
            solutionMatrix[i][n] /= (diagonalValue != 0) ? diagonalValue : 1; // Avoid divide by zero
        }

        // Fill the diagonal with the solutions
        for (int i = 0; i < n; i++) {
            solutionMatrix[i][i] = upperSolution[i];
        }

        return solutionMatrix;
    }

    public static double[] backSubstitution(double[][] augmentedMatrix, int n) {
        double[] solution = new double[n];

        for (int i = n - 1; i >= 0; i--) {
            solution[i] = augmentedMatrix[i][n];

            for (int j = i + 1; j < n; j++) {
                solution[i] -= augmentedMatrix[i][j] * solution[j];
            }

            double pivot = augmentedMatrix[i][i];
            if (pivot != 0) {
                solution[i] /= pivot;
            }
        }

        return solution;
    }

    public static void swapRows(double[][] matrix, int row1, int row2) {
        double[] temp = matrix[row1];
        matrix[row1] = matrix[row2];
        matrix[row2] = temp;
    }

    public static void printMatrix(double[][] matrix) {
        for (double[] row : matrix) {
            for (double value : row) {
                System.out.print(value + "\t");
            }
            System.out.println();
        }
    }

    public static void printDiagonal(double[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            System.out.println("x" + (i + 1) + " = " + matrix[i][i]);
        }
    }
}



