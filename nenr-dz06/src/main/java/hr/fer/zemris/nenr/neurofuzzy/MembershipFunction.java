package hr.fer.zemris.nenr.neurofuzzy;

import java.util.function.Function;

import static java.lang.Math.exp;

/**
 * This class models the membership function of neurons
 * that represent fuzzy sets in the ANFIS system.
 * <p>
 * In this homework, all input neurons have the same form
 * of the membership function (a sigmoid), but with
 * different parameters.
 * <p>
 * Form of sigmoid function that is used:
 * <p>
 * f(x) = 1 / (1 + e^(b * (x - a)))
 * 
 * @author Ivan Skorupan
 */
public class MembershipFunction {

	/**
	 * Parameter a of the sigmoid function.
	 */
	private double a;

	/**
	 * Parameter b of the sigmoid function.
	 */
	private double b;

	/**
	 * The sigmoid membership function itself.
	 */
	private Function<Double, Double> membershipFunction = x -> 1. / (1 + exp(b * (x - a)));

	/**
	 * Constructs a new {@link MembershipFunction} object given sigmoid
	 * function parameters <code>a</code> and <code>b</code>.
	 * 
	 * @param a - sigmoid function parameter a
	 * @param b - sigmoid function parameter b
	 */
	public MembershipFunction(double a, double b) {
		this.a = a;
		this.b = b;
	}
	
	/**
	 * Constructs a new {@link MembershipFunction} object with default field values
	 * (a = 0, b = 0).
	 */
	public MembershipFunction() {}
	
	/**
	 * Applies the given <code>x</code> parameter to the
	 * {@link #membershipFunction};
	 * 
	 * @param x - value to apply to the membership function
	 * @return membership function value in <code>x</code>
	 */
	public double apply(double x) {
		return membershipFunction.apply(x);
	}

	public double getA() {
		return a;
	}

	public void setA(double a) {
		this.a = a;
	}

	public double getB() {
		return b;
	}

	public void setB(double b) {
		this.b = b;
	}

	public Function<Double, Double> getMembershipFunction() {
		return membershipFunction;
	}

	public void setMembershipFunction(Function<Double, Double> membershipFunction) {
		this.membershipFunction = membershipFunction;
	}

}
