package hr.fer.zemris.nenr.neuralnetwork;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import static java.lang.Math.abs;

/**
 * This class models a matrix and many operations for matrix manipulation, such as
 * adding, subtracting, multiplying and transposing matrices.
 * 
 * @author Ivan Skorupan
 */
public class Matrix {

	/**
	 * A constant that defines the tolerance when comparing floating point values.
	 */
	private static final double DELTA = 1e-6;

	/**
	 * A regular expression that parses whitespace.
	 */
	private static final String WHITESPACE_REGEX = "\\s+";

	/**
	 * A two-dimensional array of double values that internally models the {@link Matrix} object.
	 */
	private double[][] matrix;

	/**
	 * Internally stored rows dimension of the matrix.
	 */
	private int rows;

	/**
	 * Internally stored columns dimension of the matrix.
	 */
	private int cols;

	/**
	 * Constructs a new {@link Matrix} object with the given number of rows and columns.
	 * 
	 * @param rows - number of rows
	 * @param columns - number of columns
	 * @throws IllegalArgumentException if <code>rows</code> or <code>columns</code> is less than 1
	 */
	public Matrix(int rows, int cols) {
		if (rows < 1 || cols < 1)
			throw new IllegalArgumentException("Number of rows or columns in a matrix has to be greater than 0.");

		this.rows = rows;
		this.cols = cols;
		matrix = new double[rows][cols];
	}

	/**
	 * Constructs a new {@link Matrix} object with the data model given by <code>matrix</code>. 
	 * 
	 * @param matrix - a two-dimensional double array with matrix values
	 * @throws IllegalArgumentException if <code>matrix</code> has any dimension less than 1
	 */
	public Matrix(double[][] matrix) {
		if (matrix.length < 1)
			throw new IllegalArgumentException("The model 2D array has to have a positive number of rows.");
		else if (matrix[0].length < 1) {
			throw new IllegalArgumentException("The model 2D array has to have a positive number of columns.");
		}

		this.matrix = matrix.clone();
		this.rows = matrix.length;
		this.cols = matrix[0].length;
	}

	/**
	 * Performs scalar multiplication on this matrix using the value <code>scalar</code>
	 * and returns the multiplication result.
	 * 
	 * @param scalar - real number to multiply this matrix with
	 * @return the product as a new {@link Matrix} object
	 */
	public Matrix scalarMul(double scalar) {
		Matrix product = this.copy();
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < cols; j++)
				product.matrix[i][j] *= scalar;

		return product;
	}

	/**
	 * Adds <code>another</code> matrix to this matrix and returns the sum.
	 * 
	 * @param another - matrix to add to this matrix
	 * @return the result of addition as a new {@link Matrix} object
	 * @throws IllegalArgumentException if the matrices don't have the same dimensions
	 */
	public Matrix add(Matrix another) {
		if (another.rows != rows || another.cols != cols)
			throw new IllegalArgumentException("Matrix dimensions need to be equal in order to perform addition.");

		Matrix sum = this.copy();
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < cols; j++)
				sum.matrix[i][j] += another.matrix[i][j];

		return sum;
	}

	/**
	 * Subtracts <code>another</code> matrix from this matrix and returns the difference.
	 * 
	 * @param another - matrix to subtract from this matrix
	 * @return the result of subtraction as a new {@link Matrix} object
	 * @throws IllegalArgumentException if the matrices don't have the same dimensions
	 */
	public Matrix sub(Matrix another) {
		if (another.rows != rows || another.cols != cols)
			throw new IllegalArgumentException("Matrix dimensions need to be equal in order to perform subtraction.");

		Matrix difference = this.copy();
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < cols; j++)
				difference.matrix[i][j] -= another.matrix[i][j];

		return difference;
	}

	/**
	 * Multiplies this matrix by <code>another</code> matrix and returns the product.
	 * 
	 * @param another - matrix to multiply this matrix with
	 * @return the product of matrix multiplication
	 * @throws IllegalArgumentException if the matrices are not chained
	 */
	public Matrix mul(Matrix another) {
		if (cols != another.rows)
			throw new IllegalArgumentException("The matrices need to be chained in order to be able to multiply them: "
					+ cols + " != " + another.rows + ".");

		Matrix product = new Matrix(rows, another.cols);
		for (int i = 0; i < product.rows; i++)
			for (int j = 0; j < product.cols; j++)
				for (int k = 0; k < cols; k++)
					product.matrix[i][j] += matrix[i][k] * another.matrix[k][j];

		return product;
	}

	/**
	 * Transposes this matrix and returns it as a new {@link Matrix} object.
	 * 
	 * @return this matrix transposed
	 */
	public Matrix transpose() {
		Matrix transposed = new Matrix(cols, rows);
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < cols; j++)
				transposed.matrix[j][i] = matrix[i][j];

		return transposed;
	}
	
	/**
	 * Puts zeros in all fields.
	 */
	public void clear() {
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < cols; j++)
				set(i, j, 0);
	}

	/**
	 * Takes a path to a file that contains a matrix and then reads it into
	 * a new {@link Matrix} object.
	 * 
	 * @param file - path to file that contains the matrix
	 * @return a new {@link Matrix} object constructed from the contents of <code>file</code>
	 * @throws IOException if there was a problem while reading the matrix from <code>file</code>
	 */
	public Matrix readMatrixFromFile(Path file) throws IOException {
		List<String> lines = Files.readAllLines(file);
		double matrixArr[][] = null;
		int numOfRows = lines.size();

		int i = 0;
		Iterator<String> it = lines.iterator();
		while (it.hasNext()) {
			String[] tokens = it.next().trim().split(WHITESPACE_REGEX);
			if (i == 0)
				matrixArr = new double[numOfRows][tokens.length];

			for (int j = 0; j < tokens.length; j++)
				matrixArr[i][j] = Double.parseDouble(tokens[j]);

			i++;
		}

		return new Matrix(matrixArr);
	}

	/**
	 * Takes a path to a file and writes this matrix to it.
	 * 
	 * @param file - path to file to write the matrix to
	 * @throws IOException if there was a problem while writing the matrix to the <code>file</code>
	 */
	public void exportMatrixToFile(Path file) throws IOException {
		List<String> lines = new ArrayList<>();
		for (int i = 0; i < rows; i++) {
			StringBuilder sb = new StringBuilder();

			for (int j = 0; j < cols; j++)
				sb.append(matrix[i][j] + (j == cols - 1 ? "" : " "));

			sb.append(i == rows - 1 ? "" : "\n");
			lines.add(sb.toString());
		}

		Files.write(file, lines);
	}

	/**
	 * Makes a copy of this matrix and return it.
	 * 
	 * @return a new {@link Matrix} object identical to <code>this</code>
	 */
	public Matrix copy() {
		double[][] newMatrix = new double[rows][cols];
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < cols; j++)
				newMatrix[i][j] = matrix[i][j];

		return new Matrix(newMatrix);
	}

	/**
	 * Fetches the element at given <code>row</code> and <code>col</code> indices.
	 * 
	 * @param row - row index
	 * @param col - column index
	 * @return the matrix element at the given indices
	 * @throws IndexOutOfBoundsException if any of the indices are out of matrix bounds
	 */
	public double get(int row, int col) {
		if (row >= rows || row < 0 || col >= cols || col < 0)
			throw new IndexOutOfBoundsException("Row and column indices are out of matrix bounds. Indices: " + row + ", " + col + ".");

		return matrix[row][col];
	}

	/**
	 * Sets the element at given <code>row</code> and <code>col</code> indices to
	 * <code>value</code>.
	 * 
	 * @param row - row index
	 * @param col - column index
	 * @param value - real number to set in the given spot
	 * @throws IndexOutOfBoundsException if any of the indices are out of matrix bounds
	 */
	public void set(int row, int col, double value) {
		if (row >= rows || row < 0 || col >= cols || col < 0)
			throw new IndexOutOfBoundsException("Row and column indices are out of matrix bounds.");

		matrix[row][col] = value;
	}

	/**
	 * Fetches the row at the given <code>index</code>.
	 * 
	 * @param index - row index
	 * @return an array of doubles that represents the row at <code>index</code>
	 * @throws IndexOutOfBoundsException if row index is out of matrix bounds
	 */
	public double[] getRow(int index) {
		if (index < 0 || index >= rows)
			throw new IndexOutOfBoundsException("Row index is out of matrix bounds.");

		return matrix[index];
	}
	
	/**
	 * Returns the row at the given <code>index</code> as a matrix vector.
	 * <p>
	 * The row is transposed to be returned as a column vector.
	 * 
	 * @param index - index of row to convert to matrix
	 * @return row as a matrix vector
	 */
	public Matrix getRowVector(int index) {
		double[] row = getRow(index);
		Matrix rowVector = new Matrix(row.length, 1);
		
		for (int i = 0; i < row.length; i++)
			rowVector.set(i, 0, row[i]);
		
		return rowVector;
	}

	/**
	 * Sets the row at the given <code>index</code> to the <code>row</code> array.
	 * 
	 * @param index - row index
	 * @param row - array of doubles to set at the given index
	 * @throws IndexOutOfBoundsException if row index is out of matrix bounds
	 * @throws IllegalArgumentException if the new row doesn't have
	 * the proper length
	 */
	public void setRow(int index, double[] row) {
		if (index < 0 || index >= rows)
			throw new IndexOutOfBoundsException("Row index is out of matrix bounds.");
		else if (row.length != cols)
			throw new IllegalArgumentException("The new row has to have the same number of elements as there are"
					+ " columns in the matrix");

		matrix[index] = row;
	}

	/**
	 * Getter for the internally stored number of rows in this matrix.
	 * 
	 * @return number of rows
	 */
	public int getRows() {
		return rows;
	}

	/**
	 * Getter for the internally stored number of columns in this matrix.
	 * 
	 * @return number of columns
	 */
	public int getColumns() {
		return cols;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				sb.append(String.format("%-10.3f ", matrix[i][j]));
			}
			sb.append("\n");
		}

		return sb.toString();
	}

	/**
	 * Compares this matrix's 2D array with the matrix <code>other</code>'s 2D array,
	 * but uses tolerance {@link DELTA} to compare double values to avoid rounding problems.
	 * 
	 * @param other - matrix to compare this matrix with
	 * @return <code>true</code> if 2D arrays are matching, <code>false</code> otherwise
	 */
	private boolean compareWithTolerance(Matrix other) {
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < cols; j++)
				if (abs(matrix[i][j] - other.matrix[i][j]) > DELTA)
					return false;

		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.deepHashCode(matrix);
		result = prime * result + Objects.hash(cols, rows);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Matrix))
			return false;
		Matrix other = (Matrix) obj;
		return rows == other.rows && cols == other.cols && compareWithTolerance(other);
	}

}
