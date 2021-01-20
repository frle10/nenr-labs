package hr.fer.zemris.nenr.datasetfactory;

import static hr.fer.zemris.nenr.datasetfactory.DoublePoint.distance;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * Models a gesture that the user can draw on {@link GestureCanvas}.
 * <p>
 * Each gesture is represented as a collection of points that the user
 * went through with the mouse while drawing the gesture. Since these
 * points are pixels, they have integer coordinates.
 * <p>
 * Also, since the neural network implemented in this homework will
 * support only five different gesture symbols, the information about
 * the symbol currently selected to be drawn by the user is saved here. 
 * 
 * @author skoro
 *
 */
public class Gesture {
	
	/**
	 * List of points that make up this gesture.
	 */
	private List<Point> points = new ArrayList<>();
	
	/**
	 * A symbol that this gesture represents.
	 */
	private Symbol symbol;
	
	/**
	 * Constructs a new {@link Gesture} object with
	 * given <code>symbol</code>.
	 * 
	 * @param symbol - a symbol that this gesture represents
	 */
	public Gesture(Symbol symbol) {
		this.symbol = symbol;
	}
	
	/**
	 * Calculates the approximated length of the gesture represented by
	 * <code>points</code>.
	 * <p>
	 * The given points are in scaled and centered form and not in the form
	 * of pixel coordinates.
	 * 
	 * @param points - scaled and centered points that represent a gesture
	 * @return length of the gesture represented by <code>points</code>
	 */
	public static double gestureLength(List<DoublePoint> points) {
		double length = 0;
		for (int i = 0; i < points.size() - 1; i++)
			length += distance(points.get(i), points.get(i + 1));
		
		return length;
	}
	
	/**
	 * Constructs a string that contains the x and y coordinates
	 * of points in <code>samples</code> in one line and at the
	 * end of the line contains the code for the gesture's symbol.
	 * 
	 * @param samples - representative points for this gesture
	 * @return a feature string
	 */
	public String featureString(List<DoublePoint> samples) {
		StringBuilder sb = new StringBuilder();
		for (DoublePoint point : samples)
			sb.append(point.getX() + " " + point.getY() + " ");
		sb.append(convertSymbolToCode(symbol));
		
		return sb.toString();
	}
	
	/**
	 * Converts the given <code>symbol</code> into a code string.
	 * 
	 * @param symbol - symbol to convert to its code
	 * @return symbol code as a string
	 * @throws IllegalArgumentException if <code>symbol</code> is not supported
	 */
	public static String convertSymbolToCode(Symbol symbol) {
		if (symbol.equals(Symbol.ALPHA)) return "1 0 0 0 0";
		else if (symbol.equals(Symbol.BETA)) return "0 1 0 0 0";
		else if (symbol.equals(Symbol.GAMMA)) return "0 0 1 0 0";
		else if (symbol.equals(Symbol.DELTA)) return "0 0 0 1 0";
		else if (symbol.equals(Symbol.EPSILON)) return "0 0 0 0 1";
		else throw new IllegalArgumentException("Symbol not supported.");
	}
	
	/**
	 * Converts the code to a symbol.
	 * 
	 * @param code - code to convert
	 * @return corresponding symbol
	 * @throws IllegalArgumentException if code is not supported
	 */
	public static Symbol convertCodeToSymbol(String code) {
		if (code.equals("1 0 0 0 0")) return Symbol.ALPHA;
		else if (code.equals("0 1 0 0 0")) return Symbol.BETA;
		else if (code.equals("0 0 1 0 0")) return Symbol.GAMMA;
		else if (code.equals("0 0 0 1 0")) return Symbol.DELTA;
		else if (code.equals("0 0 0 0 1")) return Symbol.EPSILON;
		else throw new IllegalArgumentException("The code is not supported");
	}
	
	/**
	 * Adds a point to this gesture.
	 * 
	 * @param point - point to add to this gesture
	 */
	public void addPoint(Point point) {
		points.add(point);
	}
	
	/**
	 * Returns a point at the specified <code>index</code> in this gesture.
	 * 
	 * @param index - index of the point to return
	 * @return point at the given <code>index</code>
	 */
	public Point getPoint(int index) {
		return points.get(index);
	}

	public Symbol getSymbol() {
		return symbol;
	}

	public void setSymbol(Symbol symbol) {
		this.symbol = symbol;
	}

	public List<Point> getPoints() {
		return points;
	}

	public void setPoints(List<Point> points) {
		this.points = points;
	}
	
}
