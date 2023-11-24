
public static void main(String[] args) {
        // Define the size of the system of equations
        int n = 9;

        // Define the coefficient matrix augmented with the right-hand side values

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

        // Initialize matrices for lower triangular and upper triangular forms
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

    // Function to solve the linear system using Gaussian elimination with partial pivoting
    public static double[][] solveLinearSystem(double[][] coefficients, int n, double[][] lowerTriangularMatrix) {
        // Initialize the augmented matrix
        double[][] augmentedMatrix = new double[n][n + 1];

        // Copy coefficients to augmented matrix
        for (int i = 0; i < n; i++) {
            for (int j = 0; j <= n; j++) {
                augmentedMatrix[i][j] = coefficients[i][j];
            }
        }

        // Perform Gaussian elimination with partial pivoting
        for (int i = 0; i < n - 1; i++) {
            // Find the pivot row
            int pivotRow = i;
            for (int j = i + 1; j < n; j++) {
                if (Math.abs(augmentedMatrix[j][i]) > Math.abs(augmentedMatrix[pivotRow][i])) {
                    pivotRow = j;
                }
            }

            // Swap the current row with the pivot row
            swapRows(augmentedMatrix, i, pivotRow);
            swapRows(lowerTriangularMatrix, i, pivotRow);

            // Update the lower triangular matrix and perform row operations
            for (int j = i + 1; j < n; j++) {
                double ratio = augmentedMatrix[j][i] / augmentedMatrix[i][i];
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

    // Function to solve both upper and lower triangular matrices
    public static double[][] solveBothMatrices(double[][] upperMatrix, double[][] lowerMatrix, int n) {
        // Initialize the solution matrix
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

    // Function for back-substitution
    public static double[] backSubstitution(double[][] augmentedMatrix, int n) {
        // Initialize the solution array
        double[] solution = new double[n];

        // Iterate through rows in reverse order
        for (int i = n - 1; i >= 0; i--) {
            solution[i] = augmentedMatrix[i][n];

            for (int j = i + 1; j < n; j++) {
                solution[i] -= augmentedMatrix[i][j] * solution[j];
            }

            // Avoid divide by zero
            double pivot = augmentedMatrix[i][i];
            if (pivot != 0) {
                solution[i] /= pivot;
            }
        }

        return solution;
    }

    // Function to swap two rows in a matrix
    public static void swapRows(double[][] matrix, int row1, int row2) {
        double[] temp = matrix[row1];
        matrix[row1] = matrix[row2];
        matrix[row2] = temp;
    }

    // Function to print a matrix
    public static void printMatrix(double[][] matrix) {
        for (double[] row : matrix) {
            for (double value : row) {
                System.out.print(value + "\t");
            }
            System.out.println();
        }
    }

    // Function to print the diagonal elements of the matrix as x1, x2, ..., x9
    public static void printDiagonal(double[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            System.out.println("x" + (i + 1) + " = " + matrix[i][i]);
        }
    }
}



