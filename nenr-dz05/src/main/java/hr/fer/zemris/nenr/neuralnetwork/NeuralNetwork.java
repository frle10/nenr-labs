package hr.fer.zemris.nenr.neuralnetwork;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import hr.fer.zemris.nenr.datasetfactory.Symbol;

import static hr.fer.zemris.nenr.neuralnetwork.NeuralNetworkLayer.*;
import static hr.fer.zemris.nenr.datasetfactory.Gesture.*;
import static java.lang.Math.round;

/**
 * Models a feed-forward neural network.
 * 
 * @author Ivan Skorupan
 */
public class NeuralNetwork {

	/**
	 * Maximum number of iterations for the fit method.
	 */
	private static final int MAX_ITER = 1000;

	/**
	 * Target error value for the fit method.
	 */
	private static final double ERROR_THRESHOLD = 1e-4;

	/**
	 * List of neural network layers, including the input layer, hidden layers and the output layer.
	 */
	private List<NeuralNetworkLayer> layers = new ArrayList<>();

	/**
	 * Backpropagation algorithm version to use.
	 */
	private Algorithm algorithm = Algorithm.GROUP;

	/**
	 * Matrix that contains the training data.
	 */
	private Matrix trainingData;

	/**
	 * Matrix that contains the training labels.
	 */
	private Matrix trainingLabels;
	
	/**
	 * Matrix that contains the validation data.
	 */
	private Matrix validationData;
	
	/**
	 * Matrix that contains the validation labels.
	 */
	private Matrix validationLabels;

	/**
	 * The learning rate for this neural network.
	 */
	private double learningRate = 0.5;

	/**
	 * Constructs a new {@link NeuralNetwork} object.
	 * 
	 * @param algorithm - version of backpropagation to use
	 * @param architecture - number of neurons per each layer
	 * @param datasetPath - path to the dataset file
	 * @param validationPath - path to the validation dataset file
	 */
	public NeuralNetwork(Algorithm algorithm, int[] architecture, Path datasetPath, Path validationPath) {
		this.algorithm = algorithm;
		for (int i = 0; i < architecture.length; i++) {
			NeuralNetworkLayer layer = new NeuralNetworkLayer(architecture[i], (i == 0) ? 0 : architecture[i - 1]);
			layers.add(layer);
		}

		List<String> datasetLines = null;
		try {
			datasetLines = Files.readAllLines(datasetPath);
		} catch (IOException e) {
			System.err.println("There was an error when reading the dataset.");
			e.printStackTrace();
			System.exit(1);
		}

		trainingData = new Matrix(datasetLines.size(), architecture[0]);
		trainingLabels = new Matrix(datasetLines.size(), architecture[architecture.length - 1]);
		for (int i = 0; i < datasetLines.size(); i++) {
			String line = datasetLines.get(i);
			String[] lineTokens = line.split(" ");
			for (int j = 0; j < architecture[0]; j++)
				trainingData.set(i, j, Double.parseDouble(lineTokens[j]));
			for (int j = architecture[0], k = 0; j < lineTokens.length; j++, k++)
				trainingLabels.set(i, k, Double.parseDouble(lineTokens[j]));
		}
		
//		List<String> validationLines = null;
//		try {
//			validationLines = Files.readAllLines(validationPath);
//		} catch (IOException e) {
//			System.err.println("There was an error when reading the validation dataset.");
//			e.printStackTrace();
//			System.exit(1);
//		}
		
//		validationData = new Matrix(validationLines.size(), architecture[0]);
//		validationLabels = new Matrix(validationLines.size(), architecture[architecture.length - 1]);
//		for (int i = 0; i < validationLines.size(); i++) {
//			String line = validationLines.get(i);
//			String[] lineTokens = line.split(" ");
//			for (int j = 0; j < architecture[0]; j++)
//				validationData.set(i, j, Double.parseDouble(lineTokens[j]));
//			for (int j = architecture[0], k = 0; j < lineTokens.length; j++, k++)
//				validationLabels.set(i, k, Double.parseDouble(lineTokens[j]));
//		}
	}

	/**
	 * Performs training of this neural network on the given <code>dataset</code>
	 * using one of the flavors of the backpropagation algorithm (group, stochastic
	 * or mini-batch).
	 * 
	 * @param trainingData - dataset with learning examples
	 * @param trainingLabels - matrix of example labels
	 */
	public void fit() {
		Group dataGroups = makeGroups();
		
		int iteration = 1;
		
		initializeWeights();
		
		double meanSquaredError = meanSquaredError(trainingData, trainingLabels);
		// double lastError = meanSquaredError + 1;
		
		while (meanSquaredError > ERROR_THRESHOLD && iteration <= MAX_ITER) {
			// lastError = meanSquaredError;
			for (int k = 0; k < dataGroups.groups.size(); k++) {
				Matrix group = dataGroups.groups.get(k);
				Matrix labels = dataGroups.groupLabels.get(k);
				
				for (int i = 0; i < group.getRows(); i++) {
					Matrix inputVector = group.getRowVector(i);
					predictExample(inputVector);

					getOutputLayer().calculateErrors(true, labels.getRowVector(i), null);
					for (int j = layers.size() - 2; j > 0; j--) {
						NeuralNetworkLayer current = layers.get(j);
						NeuralNetworkLayer downstream = layers.get(j + 1);

						current.calculateErrors(false, labels, downstream);
					}


					for (int j = 0; j < layers.size(); j++) {
						NeuralNetworkLayer layer = layers.get(j);
						if (isInputLayer(layer))
							continue;
						
						if (algorithm == Algorithm.ONLINE)
							layer.updateWeights(learningRate, layers.get(j - 1).getOutputVector(), algorithm);
						else if (algorithm == Algorithm.GROUP || algorithm == Algorithm.MINIBATCH)
							layer.updatePartialDerivations(layers.get(j - 1).getOutputVector());
					}
				}
				
				if (algorithm == Algorithm.GROUP || algorithm == Algorithm.MINIBATCH) {
					for (int i = 0; i < layers.size(); i++) {
						NeuralNetworkLayer layer = layers.get(i);
						if (isInputLayer(layer))
							continue;
						
						layer.updateWeights(learningRate, layers.get(i - 1).getOutputVector(), algorithm);
					}
				}
			}
			
			iteration++;
			meanSquaredError = meanSquaredError(trainingData, trainingLabels);
			System.out.println("Mean squared error on training set: " + meanSquaredError(trainingData, trainingLabels) + ", iteration: " + iteration);
			// System.out.println("Mean squared error on validation set: " + meanSquaredError + ", iteration: " + iteration);
		}
	}
	
	private Group makeGroups() {
		List<Matrix> groups = new ArrayList<>();
		List<Matrix> groupLabels = new ArrayList<>();
		
		if (algorithm == Algorithm.GROUP || algorithm == Algorithm.ONLINE) {
			groups.add(trainingData);
			groupLabels.add(trainingLabels);
			return new Group(groups, groupLabels);
		}
		
		List<Example> alphas = new ArrayList<>();
		List<Example> betas = new ArrayList<>();
		List<Example> gammas = new ArrayList<>();
		List<Example> deltas = new ArrayList<>();
		List<Example> epsilons = new ArrayList<>();
		
		for (int i = 0; i < trainingData.getRows(); i++) {
			Matrix sample = trainingData.getRowVector(i).transpose();
			Matrix label = trainingLabels.getRowVector(i).transpose();
			
			Example example = new Example(sample, label);
			
			Symbol symbol = convertLabelToSymbol(label);
			if (symbol == Symbol.ALPHA) alphas.add(example);
			else if (symbol == Symbol.BETA) betas.add(example);
			else if (symbol == Symbol.GAMMA) gammas.add(example);
			else if (symbol == Symbol.DELTA) deltas.add(example);
			else if (symbol == Symbol.EPSILON) epsilons.add(example);
		}
		
		for (int i = 0; i < alphas.size() - 1; i += 2) {
			Matrix group = new Matrix(10, trainingData.getColumns());
			Matrix groupLabel = new Matrix(10, trainingLabels.getColumns());
			
			group.setRow(0, alphas.get(i).features.getRow(0));
			group.setRow(1, alphas.get(i + 1).features.getRow(0));
			groupLabel.setRow(0, alphas.get(i).label.getRow(0));
			groupLabel.setRow(1, alphas.get(i + 1).label.getRow(0));
			
			group.setRow(2, betas.get(i).features.getRow(0));
			group.setRow(3, betas.get(i + 1).features.getRow(0));
			groupLabel.setRow(0, betas.get(i).label.getRow(0));
			groupLabel.setRow(1, betas.get(i + 1).label.getRow(0));
			
			group.setRow(4, gammas.get(i).features.getRow(0));
			group.setRow(5, gammas.get(i + 1).features.getRow(0));
			groupLabel.setRow(0, gammas.get(i).label.getRow(0));
			groupLabel.setRow(1, gammas.get(i + 1).label.getRow(0));
			
			group.setRow(6, deltas.get(i).features.getRow(0));
			group.setRow(7, deltas.get(i + 1).features.getRow(0));
			groupLabel.setRow(0, deltas.get(i).label.getRow(0));
			groupLabel.setRow(1, deltas.get(i + 1).label.getRow(0));
			
			group.setRow(8, epsilons.get(i).features.getRow(0));
			group.setRow(9, epsilons.get(i + 1).features.getRow(0));
			groupLabel.setRow(0, epsilons.get(i).label.getRow(0));
			groupLabel.setRow(1, epsilons.get(i + 1).label.getRow(0));
			
			groups.add(group);
			groupLabels.add(groupLabel);
		}
		
		return new Group(groups, groupLabels);
	}
	
	public Symbol predictionToSymbol(Matrix prediction) {
		double maxValue = prediction.get(0, 0);
		double maxIndex = 0;
		
		for (int i = 1; i < prediction.getRows(); i++) {
			double value = prediction.get(i, 0);
			if (value > maxValue) {
				maxValue = value;
				maxIndex = i;
			}
		}
		
		if (maxIndex == 0) return Symbol.ALPHA;
		else if (maxIndex == 1) return Symbol.BETA;
		else if (maxIndex == 2) return Symbol.GAMMA;
		else if (maxIndex == 3) return Symbol.DELTA;
		else return Symbol.EPSILON;
	}
	
	private Symbol convertLabelToSymbol(Matrix label) {
		double[] labelTokens = label.getRow(0);
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < labelTokens.length; i++)
			sb.append((int)round(labelTokens[i]) + " ");
		
		return convertCodeToSymbol(sb.toString().trim());
	}

	/**
	 * Calculates the mean squared error on the training dataset.
	 * 
	 * @param dataset - training dataset
	 * @param labels - labels for learning examples
	 * @return mean squared error on <code>dataset</code>
	 */
	private double meanSquaredError(Matrix dataset, Matrix labels) {
		Matrix predictions = predict(dataset);
		double error = 1. / dataset.getRows();
		double sum = 0;

		for (int i = 0; i < dataset.getRows(); i++) {
			Matrix output = predictions.getRowVector(i);
			for (int j = 0; j < output.getRows(); j++) {
				double expected = labels.get(i, j);
				double prediction = output.get(j, 0);
				sum += (expected - prediction) * (expected - prediction);
			}
		}

		error *= sum;
		return error;
	}

	/**
	 * Makes predictions for all examples in matrix <code>data</code>.
	 * 
	 * @param data - matrix with examples
	 * @return matrix of predictions
	 */
	public Matrix predict(Matrix data) {
		Matrix predictions = new Matrix(data.getRows(), getOutputLayer().getNeurons());
		for (int i = 0; i < data.getRows(); i++) {
			Matrix example = data.getRowVector(i);
			Matrix prediction = predictExample(example);

			predictions.setRow(i, prediction.transpose().getRow(0));
		}

		return predictions;
	}

	/**
	 * Feeds the given example <code>input</code> to the neural net.
	 * 
	 * @param input - the input vector to feed to the neural net
	 * @return the output vector
	 */
	public Matrix predictExample(Matrix inputVector) {
		if (inputVector.getRows() != getInputLayer().getNeurons())
			throw new IllegalArgumentException("The input vector's dimension must be equal to the number of neurons in the input layer.");

		getInputLayer().setOutputVector(inputVector);
		for (int i = 1; i < layers.size(); i++) {
			NeuralNetworkLayer current = layers.get(i);
			current.calculateOutput(layers.get(i - 1).getOutputVector());
		}

		return getOutputLayer().getOutputVector();
	}

	/**
	 * Initializes all weights in the neural network using Xavier initialization technique.
	 */
	public void initializeWeights() {
		Random random = new Random();
		for (int i = 1; i < layers.size(); i++) {
			NeuralNetworkLayer layer = layers.get(i);
			layer.initializeWeights(layers.get(i - 1).getNeurons(), layer.getNeurons(), random);
		}
	}

	/**
	 * Returns the input layer of this neural network.
	 * 
	 * @return input layer
	 */
	public NeuralNetworkLayer getInputLayer() {
		return layers.get(0);
	}

	/**
	 * Returns the output layer of this neural network.
	 * 
	 * @return output layer
	 */
	public NeuralNetworkLayer getOutputLayer() {
		return layers.get(layers.size() - 1);
	}

	public List<NeuralNetworkLayer> getLayers() {
		return layers;
	}

	public void setLayers(List<NeuralNetworkLayer> layers) {
		this.layers = layers;
	}

	public Algorithm getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(Algorithm algorithm) {
		this.algorithm = algorithm;
	}

	public Matrix getTrainingData() {
		return trainingData;
	}

	public void setTrainingData(Matrix trainingData) {
		this.trainingData = trainingData;
	}

	public Matrix getTrainingLabels() {
		return trainingLabels;
	}

	public void setTrainingLabels(Matrix trainingLabels) {
		this.trainingLabels = trainingLabels;
	}

	public Matrix getValidationData() {
		return validationData;
	}

	public void setValidationData(Matrix validationData) {
		this.validationData = validationData;
	}

	public Matrix getValidationLabels() {
		return validationLabels;
	}

	public void setValidationLabels(Matrix validationLabels) {
		this.validationLabels = validationLabels;
	}

	public double getLearningRate() {
		return learningRate;
	}

	public void setLearningRate(double learningRate) {
		this.learningRate = learningRate;
	}

}
