public class GaussElimination {

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

        // Solve both upper and lower triangular matrices
        double[][] solutionMatrix = solveBothMatrices(upperTriangularMatrix, lowerTriangularMatrix, n);

        // Display the final matrix with all diagonal values as 1 and the last column as provided values
        System.out.println("\nFinal Matrix with Diagonal as 1's and Last Column as Provided Values:");
        printMatrix(solutionMatrix);

        // Display the last column values separately as x1, x2, ..., x9
        System.out.println("\nLast Column Values (x1, x2, ..., x9):");
        double[] calculatedValues = calculateLastColumnValues(solutionMatrix, n);
        printLastColumn(solutionMatrix, calculatedValues);
    }

    // Function to calculate the last column values manually
    public static double[] calculateLastColumnValues(double[][] solutionMatrix, int n) {
        double[] calculatedValues = new double[n];

        for (int i = 0; i < n; i++) {
            double sum = solutionMatrix[i][n];
            for (int j = 0; j < n; j++) {
                if (j != i) {
                    sum -= solutionMatrix[i][j] * calculatedValues[j];
                }
            }
            calculatedValues[i] = sum;
        }

        return calculatedValues;
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

            // Normalize the diagonal to 1
            solutionMatrix[i][i] = 1;
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

    // Function to print a matrix with last column as x1, x2, ..., x9
    public static void printMatrix(double[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length - 1; j++) {
                System.out.print(matrix[i][j] + "\t");
            }
            System.out.println( matrix[i][matrix[i].length - 1]);
        }
    }

    // Function to print the last column values as x1, x2, ..., x9
    public static void printLastColumn(double[][] matrix, double[] calculatedValues) {
        for (int i = 0; i < matrix.length; i++) {
            System.out.println("x" + (i + 1) + " = " + calculatedValues[i]);
        }
    }
}
