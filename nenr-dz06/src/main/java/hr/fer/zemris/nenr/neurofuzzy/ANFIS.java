package hr.fer.zemris.nenr.neurofuzzy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static hr.fer.zemris.nenr.neurofuzzy.Util.*;

/**
 * Models an ANFIS (Adaptive Neuro-Fuzzy Inference System).
 * <p>
 * In this version of the system, all antecedents are of form "x is Ai and y is Bi"
 * and all consequents are of form "z = pi * x + qi * y + ri".
 * <p>
 * The implemented system works with any number of rules, as long as
 * they are of the described form.
 * 
 * @author Ivan Skorupan
 */
public class ANFIS {
	
	/**
	 * Default number of rules for ANFIS if not specified by the user.
	 */
	private static final int DEFAULT_NUMBER_OF_RULES = 2;
	
	/**
	 * Maximum number of epochs for the training process.
	 */
	private static final int MAX_EPOCHS = 50000;
	
	/**
	 * Maximum mean squared error that ANFIS can have in order to stop the training process.
	 */
	private static final double MEAN_SQUARED_ERROR_THRESHOLD = 10e-6;
	
	/**
	 * The learning rate for parameters pi, qi and ri.
	 */
	private static final double ETA_PQR = 8e-4;
	
	/**
	 * The learning rate for parameters ai, bi, ci and di.
	 */
	private static final double ETA_AB = 1e-4;
	
	/**
	 * Membership functions of fuzzy sets Ai.
	 */
	private List<MembershipFunction> fuzzySetsA = new ArrayList<>();
	
	/**
	 * Membership functions of fuzzy sets Bi.
	 */
	private List<MembershipFunction> fuzzySetsB = new ArrayList<>();
	
	/**
	 * Consequent functions that belong to this ANFIS.
	 */
	private List<ConsequentFunction> consequents = new ArrayList<>();
	
	/**
	 * Number of fuzzy rules in this ANFIS. The default value is 2.
	 */
	private int numberOfRules;
	
	/**
	 * Array of partial derivations of the error function by each parameter.
	 * There are 7 parameters in this system and {@link #numberOfRules} rules,
	 * so there are 7 * {@link #numberOfRules} partial derivations in this
	 * array.
	 */
	private double[] partialDerivations;
	
	/**
	 * Constructs a new {@link ANFIS} object with <code>numberOfRules</code>
	 * rules and initializes the system with stochastic initial parameters.
	 * 
	 * @param numberOfRules - number of rules in this ANFIS
	 * @throws IllegalArgumentException if <code>numberOfRules</code> is not a positive integer
	 */
	public ANFIS(int numberOfRules) {
		if (numberOfRules < 1)
			throw new IllegalArgumentException("The number of rules must be a positive integer.");
		
		this.numberOfRules = numberOfRules;
		partialDerivations = new double[7 * numberOfRules];
		initializeSystem();
	}
	
	/**
	 * Constructs a new {@link ANFIS} object with
	 * default number of rules.
	 * <p>
	 * Calls {@link #ANFIS(int)} constructor with parameter
	 * {@link #DEFAULT_NUMBER_OF_RULES}.
	 */
	public ANFIS() {
		this(DEFAULT_NUMBER_OF_RULES);
	}
	
	/**
	 * Initializes this ANFIS with stochastically generated initial values
	 * for parameters.
	 */
	private void initializeSystem() {
		Random random = new Random();
		for (int i = 0; i < numberOfRules; i++) {
			double ai = random.nextGaussian();
			double bi = random.nextGaussian();
			MembershipFunction muAi = new MembershipFunction(ai, bi);
			fuzzySetsA.add(muAi);
			
			double ci = random.nextGaussian();
			double di = random.nextGaussian();
			MembershipFunction muBi = new MembershipFunction(ci, di);
			fuzzySetsB.add(muBi);
			
			double pi = random.nextGaussian();
			double qi = random.nextGaussian();
			double ri = 0;
			ConsequentFunction zi = new ConsequentFunction(pi, qi, ri);
			consequents.add(zi);
		}
	}
	
	/**
	 * Trains this ANFIS using examples and labels from the given
	 * <code>dataset</code>.
	 * 
	 * @param dataset - dataset with training examples
	 * @param batch - if <code>true</code>, batch training method is used, otherwise
	 * the online training method is used
	 * @param verbose - verbose output
	 * @param exportError - if mean squared error in each epoch should be exported to <code>file</code>
	 * @param file - export file path
	 * @throws IOException if there is an error while writing to <code>file</code>
	 * @throws NullPointerException if <code>dataset</code> is <code>null</code>
	 */
	public void fit(Dataset dataset, boolean batch, boolean verbose, boolean exportError, Path file) throws IOException {
		Objects.requireNonNull(dataset);
		
		int epoch = 1;
		double meanSquaredError = meanSquaredError(dataset);
		List<Double> errors = new ArrayList<>();
		
		double minError = meanSquaredError;
		double maxError = meanSquaredError;
		
		while (meanSquaredError > MEAN_SQUARED_ERROR_THRESHOLD && epoch <= MAX_EPOCHS) {
			for (Example example : dataset) {
				// Prepare necessary data.
				double x = example.getX();
				double y = example.getY();
				double label = example.getLabel();
				
				double[] alphas = new double[numberOfRules];
				double[] fis = new double[numberOfRules];
				
				// Calculate alphas and fis for the current example.
				for (int i = 0; i < numberOfRules; i++) {
					MembershipFunction muAi = fuzzySetsA.get(i);
					MembershipFunction muBi = fuzzySetsB.get(i);
					double alpha = muAi.apply(x) * muBi.apply(y);
					
					ConsequentFunction consequent = consequents.get(i);
					double fi = consequent.apply(x, y);
					
					alphas[i] = alpha;
					fis[i] = fi;
				}
				
				// Make the predictions and calculate some values that we can in advance.
				double prediction = predict(alphas, fis);
				double delta = label - prediction;
				
				double alphaSum = 0;
				for (double alpha : alphas)
					alphaSum += alpha;
				
				// Partial derivations are calculated in this loop.
				for (int i = 0; i < numberOfRules; i++) {
					// Prepare necessary data for calculating partial derivations.
					ConsequentFunction consequent = consequents.get(i);
					MembershipFunction muAi = fuzzySetsA.get(i);
					MembershipFunction muBi = fuzzySetsB.get(i);
					
					double muAiX = muAi.apply(x);
					double muBiY = muBi.apply(y);
					
					double pi = consequent.getP();
					double qi = consequent.getQ();
					double ri = consequent.getR();
					
					double ai = muAi.getA();
					double bi = muAi.getB();
					
					double ci = muBi.getA();
					double di = muBi.getB();
					
					// First calculate the most complex partial derivation (partial output by T-norm).
					double outputByTNorm = 1. / (alphaSum * alphaSum);
					double nominatorSum = 0;
					for (int j = 0; j < numberOfRules; j++)
						nominatorSum += alphas[j] * (fis[i] - fis[j]);
					
					outputByTNorm *= nominatorSum;
					
					// Calculate partial derivation of output by consequent value.
					double outputByConsequent = alphas[i] / alphaSum;
					
					// Now that data is nicely prepared, calculate full partial derivation
					// for each parameter that needs to be learned.
					double piPartial = -delta * outputByConsequent * x;
					double qiPartial = -delta * outputByConsequent * y;
					double riPartial = -delta * outputByConsequent;
					
					double aiPartial = -delta * outputByTNorm * muBiY * bi * (1 - muAiX) * muAiX;
					double biPartial = delta * outputByTNorm * muBiY * (x - ai) * muAiX * (1 - muAiX);
					
					double ciPartial = -delta * outputByTNorm * muAiX * di * (1 - muBiY) * muBiY;
					double diPartial = delta * outputByTNorm * muAiX * (x - ci) * muBiY * (1 - muBiY);
					
					if (!batch) {
						// If stochastic gradient descent is used, update the parameters immediately
						// based on these approximate partial derivations.
						consequent.setP(pi - ETA_PQR * piPartial);
						consequent.setQ(qi - ETA_PQR * qiPartial);
						consequent.setR(ri - ETA_PQR * riPartial);
						
						muAi.setA(ai - ETA_AB * aiPartial);
						muAi.setB(bi - ETA_AB * biPartial);
						
						muBi.setA(ci - ETA_AB * ciPartial);
						muBi.setB(di - ETA_AB * diPartial);
					} else {
						// If batch gradient descent is used, sum all partial derivation for current
						// example into an array of partial derivations that will later be used
						// to update the parameters.
						int offset = i * 7;
						
						partialDerivations[offset] += piPartial;
						partialDerivations[offset + 1] += qiPartial;
						partialDerivations[offset + 2] += riPartial;
						
						partialDerivations[offset + 3] += aiPartial;
						partialDerivations[offset + 4] += biPartial;
						
						partialDerivations[offset + 5] += ciPartial;
						partialDerivations[offset + 6] += diPartial;
					}
				}
			}
			
			// If batch gradient descent is used, update the parameters here, when partial
			// derivations for all examples have been calculated and summed up.
			if (batch) {
				for (int i = 0; i < numberOfRules; i++) {
					// Prepare necessary references for updating parameters.
					ConsequentFunction consequent = consequents.get(i);
					MembershipFunction muAi = fuzzySetsA.get(i);
					MembershipFunction muBi = fuzzySetsB.get(i);
					
					double pi = consequent.getP();
					double qi = consequent.getQ();
					double ri = consequent.getR();
					
					double ai = muAi.getA();
					double bi = muAi.getB();
					
					double ci = muBi.getA();
					double di = muBi.getB();
					
					// Update the parameters.
					int offset = i * 7;
					consequent.setP(pi - ETA_PQR * partialDerivations[offset]);
					consequent.setQ(qi - ETA_PQR * partialDerivations[offset + 1]);
					consequent.setR(ri - ETA_PQR * partialDerivations[offset + 2]);
					
					muAi.setA(ai - ETA_AB * partialDerivations[offset + 3]);
					muAi.setB(bi - ETA_AB * partialDerivations[offset + 4]);
					
					muBi.setA(ci - ETA_AB * partialDerivations[offset + 5]);
					muBi.setB(di - ETA_AB * partialDerivations[offset + 6]);
				}
			}
			
			if (verbose)
				System.out.println("Epoch: " + epoch + ", Error: " + meanSquaredError);
			
			// Recalculate the mean squared error after updating the parameters and
			// increase epoch count by one. Also, reset the partial derivations array.
			meanSquaredError = meanSquaredError(dataset);
			epoch++;
			Arrays.fill(partialDerivations, 0);
			
			if (meanSquaredError > maxError)
				maxError = meanSquaredError;
			if (meanSquaredError < minError)
				minError = meanSquaredError;
			
			errors.add(meanSquaredError);
		}
		
		if (exportError) {
			List<String> lines = new ArrayList<>();
			lines.add("# Epoch, Mean Squared Error");
			
			for (int i = 1; i <= errors.size(); i++) {
				double error = errors.get(i - 1);
				// double normalized = (error - minError) / (maxError - minError);
				
				lines.add(i + " " + error);
			}
			
			Files.write(file, lines);
		}
	}
	
	/**
	 * Calculates the ANFIS output with given <code>alphas</code>
	 * and <code>fis</code>. These arrays are calculated
	 * for an example in the fit method and are then used
	 * for calculating the prediction.
	 * <p>
	 * This method is used instead of {@link #predict(Example)} in {@link #fit(Dataset, boolean)}
	 * because the alphas and fis are needed for further calculations and the public predict
	 * method loses this information.
	 * 
	 * @param alphas - array of T-norms of Ai and Bi
	 * @param fis - array of consequent function values
	 * @return prediction based on <code>alphas</code> and <code>fis</code>
	 */
	private double predict(double[] alphas, double[] fis) {
		double numerator = 0;
		double denominator = 0;
		
		for (int i = 0; i < numberOfRules; i++) {
			numerator += alphas[i] * fis[i];
			denominator += alphas[i];
		}
		
		return numerator / denominator;
	}
	
	/**
	 * Feeds the given <code>dataset</code> to ANFIS and calculates
	 * the output of the system which is then returned to the user.
	 * <p>
	 * These predictions are made based on the current state of
	 * parameters in ANFIS, so it should first be trained with a
	 * training set before calling this method on other examples
	 * so that the parameters are optimized for generalization.
	 * <p>
	 * This method calls {@link #predict(Example)} for every
	 * example in <code>dataset</code>, storing each prediction
	 * in a double array which is then returned to the user when
	 * all examples have been predicted.
	 * 
	 * @param dataset - dataset containing the examples whose labels
	 * are to be predicted
	 * @return predicted labels for all examples in <code>dataset</code>
	 * @throws NullPointerException if <code>dataset</code> is <code>null</code>
	 */
	public double[] predict(Dataset dataset) {
		Objects.requireNonNull(dataset);
		
		double[] predictions = new double[dataset.size()];
		for (int i = 0; i < dataset.size(); i++)
			predictions[i] = predict(dataset.getExample(i));
		
		return predictions;
	}
	
	/**
	 * Feeds the given <code>example</code> to ANFIS and calculates
	 * the output of the system which is then returned to the user.
	 * <p>
	 * This prediction is made based on the current state of
	 * parameters in ANFIS, so it should first be trained with a
	 * training set before calling this method on another example
	 * so that the parameters are optimized for generalization.
	 * 
	 * @param example - example to predict
	 * @return predicted label of <code>example</code>
	 * @throws NullPointerException if <code>example</code> is <code>null</code>
	 */
	public double predict(Example example) {
		Objects.requireNonNull(example);
		
		double x = example.getX();
		double y = example.getY();
		
		double numerator = 0;
		double denominator = 0;
		for (int i = 0; i < numberOfRules; i++) {
			MembershipFunction muAi = fuzzySetsA.get(i);
			MembershipFunction muBi = fuzzySetsB.get(i);
			double alpha = muAi.apply(x) * muBi.apply(y);
			
			ConsequentFunction consequent = consequents.get(i);
			double fi = consequent.apply(x, y);
			
			numerator += alpha * fi;
			denominator += alpha;
		}
		
		return numerator / denominator;
	}
	
	/**
	 * Calculates the mean squared error of ANFIS on the given
	 * <code>dataset</code>.
	 * <p>
	 * The total error on <code>dataset</code> is calculated as follows:
	 * <p>
	 * <code>E = 0.5 * sum(Ek)</code>, over all examples, where
	 * <code>Ek = (expected - output)^2</code>
	 * 
	 * @param dataset - dataset to evaluate ANFIS error on
	 * @return the ANFIS's mean squared error on <code>dataset</code>
	 * @throws NullPointerException if <code>dataset</code> is <code>null</code>
	 */
	public double meanSquaredError(Dataset dataset) {
		Objects.requireNonNull(dataset);
		
		double totalError = 0;
		for (Example example : dataset) {
			double expected = example.getLabel();
			double prediction = predict(example);
			
			double error = (expected - prediction) * (expected - prediction);
			totalError += error;
		}
		
		return 0.5 * totalError;
	}
	
	/**
	 * Given the <code>dataset</code>, calculates output of this
	 * ANFIS for each example and exports the data points to
	 * the given <code>file</code> so that they can be graphed
	 * using a tool like gnuplot.
	 * 
	 * @param dataset - dataset to evaluate predictions on
	 * @param file - path to export file
	 * @throws IOException if there is an error while writing to <code>file</code>
	 */
	public void exportLearnedFunction(Dataset dataset, Path file) throws IOException {
		List<String> lines = new ArrayList<>();
		lines.add("# X, Y, f(x, y)");
		
		String output;
		for (Example example : dataset) {
			double prediction = predict(example);
			output = example.getX() + " " + example.getY() + " " + prediction;
			lines.add(output);
		}
		
		Files.write(file, lines);
	}
	
	/**
	 * Given the <code>dataset</code>, calculates deviations of this
	 * ANFIS from the true function values for each example and exports
	 * the data points to the given <code>file</code> so that they can
	 * be graphed using a tool like gnuplot.
	 * 
	 * @param dataset - dataset to evaluate deviations on
	 * @param file - path to export file
	 * @throws IOException if there is an error while writing to <code>file</code>
	 */
	public void exportDeviations(Dataset dataset, Path file) throws IOException {
		List<String> lines = new ArrayList<>();
		lines.add("# X, Y, f(x, y)");
		
		String output;
		for (Example example : dataset) {
			double prediction = predict(example);
			double deviation = prediction - example.getLabel();
			
			output = example.getX() + " " + example.getY() + " " + deviation;
			lines.add(output);
		}
		
		Files.write(file, lines);
	}
	
	/**
	 * Calculates learned fuzzy sets' membership functions of this
	 * ANFIS and exports the data points to the given <code>file</code>
	 * so that they can be graphed using a tool like gnuplot.
	 * 
	 * @param file - path to export file
	 * @throws IOException if there is an error while writing to <code>file</code>
	 */
	public void exportFuzzySets(Path file) throws IOException {
		List<String> lines = new ArrayList<>();
		lines.add("# X, A1, B1, A2, B2, ..., Am, Bm");
		
		StringBuilder output = new StringBuilder();
		for (int i = FEATURE_LOWER_BOUND; i <= FEATURE_UPPER_BOUND; i++) {
			output.append(i + " ");
			for (int j = 0; j < numberOfRules; j++) {
				MembershipFunction muAi = fuzzySetsA.get(j);
				MembershipFunction muBi = fuzzySetsB.get(j);
				
				output.append(muAi.apply(i) + " " + muBi.apply(i));
				
				if (i != FEATURE_UPPER_BOUND)
					output.append(" ");
			}
			
			lines.add(output.toString());
			output.delete(0, output.length());
		}
		
		Files.write(file, lines);
	}
	
}
