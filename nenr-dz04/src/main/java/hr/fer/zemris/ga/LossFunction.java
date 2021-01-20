package hr.fer.zemris.ga;

/**
 * This interface describes what a loss function used for genetic algorithms should provide
 * in terms of features. Every loss function needs to be able to return its value based
 * on given input parameters for which the loss is calculated.
 * 
 * @author Ivan Skorupan
 */
public interface LossFunction {

	/**
	 * Calculates the loss for given <code>params</code>.
	 * 
	 * @param params - parameters to calculate the loss for
	 * @return the loss for <code>params</code>
	 */
	double valueAt(double... params);
	
}
