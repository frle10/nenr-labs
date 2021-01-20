package hr.fer.zemris.ga;

import java.util.List;

import static java.lang.Math.*;

/**
 * Models a loss function based on the mean squares error. The lower
 * the mean squares error, the lower the loss for that set of parameters.
 * 
 * @author Ivan Skorupan
 */
public class MeanSquaresLossFunction implements LossFunction {
	
	/**
	 * Measurements that are used when calculating the loss function value.
	 */
	private List<Measurement> measurements;
	
	/**
	 * Constructs a new {@link MeanSquaresLossFunction} object and initializes it
	 * with given <code>measurements</code>.
	 * 
	 * @param measurements - measurements to use for mean squares
	 */
	public MeanSquaresLossFunction(List<Measurement> measurements) {
		this.measurements = measurements;
	}
	
	@Override
	public double valueAt(double... params) {
		double b0 = params[0], b1 = params[1], b2 = params[2], b3 = params[3], b4 = params[4];
		double sum = 0;
		
		int numOfMeasurements = measurements.size();
		for (int i = 0; i < numOfMeasurements; i++) {
			Measurement m = measurements.get(i);
			double x1 = m.getX1();
			double x2 = m.getX2();
			
			double systemFunctionValue = sin(b0 + b1 * x1);
			systemFunctionValue += b2 * cos(x1 * (b3 + x2)) * (1. / (1 + exp((x1 - b4) * (x1 - b4))));
			
			double y = m.getY();
			double residual = systemFunctionValue - y;
			sum += residual * residual;
		}
		
		return sum / numOfMeasurements;
	}
	
}
