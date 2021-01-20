package hr.fer.zemris.ga;

/**
 * A simple data class representing a measurement of x1 and x2 variables
 * and the corresponding system output y.
 * 
 * @author Ivan Skorupan
 */
public class Measurement {
	
	/**
	 * Measurement value for x1.
	 */
	private double x1;
	
	/**
	 * Measurement value for x2.
	 */
	private double x2;
	
	/**
	 * Measurement value for system output y.
	 */
	private double y;

	/**
	 * Constructs a new {@link Measurement} object and initializes
	 * it with given x1, x2 and y values.
	 * 
	 * @param x1 - measurement for input x1
	 * @param x2 - measurement for input x2
	 * @param y - measurement for output y
	 */
	public Measurement(double x1, double x2, double y) {
		this.x1 = x1;
		this.x2 = x2;
		this.y = y;
	}

	/**
	 * Getter for x1 value.
	 * 
	 * @return x1 value
	 */
	public double getX1() {
		return x1;
	}

	/**
	 * Getter for x2 value.
	 * 
	 * @return x2 value
	 */
	public double getX2() {
		return x2;
	}

	/**
	 * Getter for x3 value.
	 * 
	 * @return x3 value
	 */
	public double getY() {
		return y;
	}
	
	@Override
	public String toString() {
		return "(x1 = " + String.format("%.5f", x1) + ", x2 = " + String.format("%.5f", x2) + ", y = " + String.format("%.5f", y) + ")";
	}
	
}
