package hr.fer.zemris.nenr.datasetfactory;

import static java.lang.Math.sqrt;

import java.awt.Point;

import static java.lang.Math.abs;

import java.util.List;
import java.util.Objects;

/**
 * A simple model of a two coordinate point in a Cartesian plane,
 * but with double values for x and y.
 * 
 * @author Ivan Skorupan
 */
public class DoublePoint {
	
	/**
	 * Threshold used to determine the tolerance for which
	 * two points are considered equal.
	 */
	private static final double EPSILON = 10e-9;
	
	/**
	 * X coordinate.
	 */
	private double x;
	
	/**
	 * Y coordinate.
	 */
	private double y;

	/**
	 * Constructs a new {@link DoublePoint} object with given
	 * <code>x</code> and <code>y</code> coordinates.
	 * 
	 * @param x - x coordinate
	 * @param y - y coordinate
	 */
	public DoublePoint(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Calculates the centroid of given <code>points</code>.
	 * 
	 * @param points - points to calculate the centroid of
	 * @return the centroid of given <code>points</code>
	 */
	public static DoublePoint centroid(List<Point> points) {
		double x = 0, y = 0;
		for (Point point : points) {
			x += point.x;
			y += point.y;
		}
		
		return new DoublePoint(x / points.size(), y / points.size());
	}
	
	/**
	 * Calculates the Euclidean distance between points <code>p1</code> and
	 * <code>p2</code>.
	 * 
	 * @param p1 - first point
	 * @param p2 - second point
	 * @return Euclidean distance between given points
	 */
	public static double distance(DoublePoint p1, DoublePoint p2) {
		return sqrt((p2.x - p1.x) * (p2.x - p1.x) + (p2.y - p1.y) * (p2.y - p1.y));
	}
	
	/**
	 * Subtracts point <code>p</code> from this point and returns
	 * the result as a new {@link DoublePoint} object.
	 * <p>
	 * Does not change the original point.
	 * 
	 * @param p - point to subtract from this point
	 * @return difference between this point and point <code>p</code>
	 */
	public DoublePoint subtract(DoublePoint p) {
		return new DoublePoint(x - p.x, y - p.y);
	}
	
	/**
	 * Makes a copy of this point and returns it.
	 * 
	 * @return a copy of this point
	 */
	public DoublePoint copy() {
		return new DoublePoint(x, y);
	}
	
	/**
	 * Getter for the x coordinate.
	 * 
	 * @return x coordinate
	 */
	public double getX() {
		return x;
	}
	
	/**
	 * Setter for the x coordinate.
	 * 
	 * @param x - new x coordinate
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * Getter for the y coordinate.
	 * 
	 * @return y coordinate
	 */
	public double getY() {
		return y;
	}

	/**
	 * Setter for the y coordinate.
	 * 
	 * @param y - new y coordinate
	 */
	public void setY(double y) {
		this.y = y;
	}

	@Override
	public String toString() {
		return "(" + String.format("%.5f", x) + "," + String.format("%.5f", y) + ")";
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof DoublePoint))
			return false;
		DoublePoint other = (DoublePoint) obj;
		
		return abs(x - other.x) < EPSILON && abs(y - other.y) < EPSILON;
	}
	
}
