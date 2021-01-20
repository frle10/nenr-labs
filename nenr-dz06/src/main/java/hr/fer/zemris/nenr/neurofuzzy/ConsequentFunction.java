package hr.fer.zemris.nenr.neurofuzzy;

import java.util.function.BiFunction;

/**
 * This class models a consequence function in our desired
 * ANFIS system. The i-th consequence function zi(x, y) is
 * defined as:
 * <p>
 * zi(x, y) = pi * x + qi * y + ri
 * <p>
 * There are three consequent parameters p, q and r that the
 * ANFIS system needs to learn for each fuzzy rule.
 * 
 * @author Ivan Skorupan
 */
public class ConsequentFunction {
	
	/**
	 * Consequent function parameter that multiplies x.
	 */
	private double p;
	
	/**
	 * Consequent function parameter that multiplies y.
	 */
	private double q;
	
	/**
	 * Stand-alone consequent function parameter.
	 */
	private double r;
	
	/**
	 * The consequent function itself.
	 */
	private BiFunction<Double, Double, Double> consequentFunction = (x, y) -> p * x + q * y + r;

	/**
	 * Constructs a new {@link ConsequentFunction} object given
	 * parameters <code>p</code>, <code>q</code> and <code>r</code>. 
	 * 
	 * @param p - first parameter (multiplies x)
	 * @param q - second parameter (multiplies y)
	 * @param r - third, stand-alone parameter
	 */
	public ConsequentFunction(double p, double q, double r) {
		this.p = p;
		this.q = q;
		this.r = r;
	}
	
	/**
	 * Constructs a new {@link ConsequentFunction} object with
	 * default parameters (p = 0, q = 0, r = 0).
	 */
	public ConsequentFunction() {}
	
	/**
	 * Applies the given example's features (x and y) to the
	 * {@link #consequentFunction}.
	 * 
	 * @param example - example whose features to apply to the consequent function
	 * @return consequent function value for given <code>example</code>
	 */
	public double apply(Example example) {
		return consequentFunction.apply(example.getX(), example.getY());
	}
	
	/**
	 * Applies the given <code>x</code> and <code>y</code> parameters to the
	 * {@link #consequentFunction}.
	 * 
	 * @param x - first argument to apply to the consequent function
	 * @param y - second argument to apply to the consequent function
	 * @return consequent function value in <code>(x, y)</code>
	 */
	public double apply(double x, double y) {
		return consequentFunction.apply(x, y);
	}

	public double getP() {
		return p;
	}

	public void setP(double p) {
		this.p = p;
	}

	public double getQ() {
		return q;
	}

	public void setQ(double q) {
		this.q = q;
	}

	public double getR() {
		return r;
	}

	public void setR(double r) {
		this.r = r;
	}

	public BiFunction<Double, Double, Double> getConsequentFunction() {
		return consequentFunction;
	}

	public void setConsequentFunction(BiFunction<Double, Double, Double> consequentFunction) {
		this.consequentFunction = consequentFunction;
	}
	
}
