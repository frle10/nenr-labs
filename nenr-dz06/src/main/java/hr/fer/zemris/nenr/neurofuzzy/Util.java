package hr.fer.zemris.nenr.neurofuzzy;

import java.util.function.BiFunction;

import static java.lang.Math.cos;

/**
 * Contains some constants and references that are used
 * throughout the implementation.
 * 
 * @author Ivan Skorupan
 */
public class Util {
	
	/**
	 * Lower bound for feature values.
	 */
	public static final int FEATURE_LOWER_BOUND = -4;
	
	/**
	 * Upper bound for feature values.
	 */
	public static final int FEATURE_UPPER_BOUND = 4;
	
	/**
	 * The goal function we are going to try to learn using the ANFIS system.
	 */
	public static final BiFunction<Double, Double, Double> f = (x, y) -> {
		double result = cos(x / 5);
		result *= result;
		
		double sum = (x - 1) * (x - 1);
		sum += (y + 2) * (y + 2);
		sum -= 5 * x * y;
		sum += 3;
		
		result *= sum;
		return result;
	};
	
	/**
	 * Generates a demo dataset that the ANFIS system will train on.
	 * 
	 * @return the generated dataset reference
	 */
	public static Dataset generateDemoTrainingDataset() {
		Dataset trainingSet = new Dataset();
		for (int i = FEATURE_LOWER_BOUND; i <= FEATURE_UPPER_BOUND; i++) {
			for (int j = FEATURE_LOWER_BOUND; j <= FEATURE_UPPER_BOUND; j++) {
				double goalFunctionValue = f.apply((double)i, (double)j);
				Example example = new Example(i, j, goalFunctionValue);
				trainingSet.addExample(example);
			}
		}
		
		return trainingSet;
	}
	
}
