package hr.fer.zemris.nenr.neuralnetwork;

import java.util.Random;
import java.util.function.Function;

import static java.lang.Math.exp;
import static java.lang.Math.sqrt;

/**
 * Models a layer in a feed-forward neural network.
 * 
 * @author Ivan Skorupan
 */
public class NeuralNetworkLayer {

	/**
	 * The sigmoid activation function for neurons.
	 */
	private static final Function<Double, Double> SIGMOID_ACTIVATION_FUNCTION = net -> 1. / (1 + exp(-net));

	/**
	 * A matrix that holds the weights that go into this layer's neurons.
	 */
	private Matrix weights;

	/**
	 * Output values of this layer's neurons.
	 */
	private Matrix outputVector;

	/**
	 * Error values of this layer's neurons.
	 */
	private Matrix errorVector;
	
	/**
	 * Values of partial derivations by each weight in this layer.
	 */
	private Matrix partialDerivations;

	/**
	 * Number of neurons in this layer.
	 */
	private int neurons;

	/**
	 * Constructs a new {@link NeuralNetworkLayer} object.
	 * 
	 * @param neurons - number of neurons in this layer
	 * @param precedingLayerNeurons - number of neurons in the previous layer
	 */
	public NeuralNetworkLayer(int neurons, int precedingLayerNeurons) {
		this.neurons = neurons;
		weights = (precedingLayerNeurons != 0) ? new Matrix(neurons, precedingLayerNeurons) : null;
		outputVector = new Matrix(neurons, 1);
		errorVector = new Matrix(neurons, 1);
		partialDerivations = (precedingLayerNeurons != 0) ? new Matrix(neurons, precedingLayerNeurons) : null;
	}

	/**
	 * Initializes this layer's weights using the Xavier initialization technique.
	 * 
	 * @param inputNeurons - number of input neurons in the neural network
	 * @param outputNeurons - number of output neurons in the neural network
	 * @param random - stochastic object to use for initialization
	 */
	public void initializeWeights(int inputNeurons, int outputNeurons, Random random) {
		if (weights == null) return;

		for (int i = 0; i < weights.getRows(); i++)
			for (int j = 0; j < weights.getColumns(); j++)
				weights.set(i, j, random.nextGaussian() * sqrt((2. / (inputNeurons + outputNeurons))));
	}

	/**
	 * Calculates this layer's output vector.
	 * 
	 * @param precedingLayerOutput - output of preceding layer's neurons
	 */
	public void calculateOutput(Matrix precedingLayerOutput) {
		Matrix net = weights.mul(precedingLayerOutput);
		for (int i = 0; i < outputVector.getRows(); i++)
			outputVector.set(i, 0, SIGMOID_ACTIVATION_FUNCTION.apply(net.get(i, 0)));
	}

	/**
	 * Calculates the error vector of this layer.
	 * 
	 * @param isOutputLayer - if this layer is the output layer
	 * @param label - expected value on output
	 * @param downstream - the succeeding layer
	 */
	public void calculateErrors(boolean isOutputLayer, Matrix label, NeuralNetworkLayer downstream) {
		for (int i = 0; i < errorVector.getRows(); i++) {
			double output = outputVector.get(i, 0);
			double expected = label.get(i, 0);
			
			Matrix downstreamErrors = !isOutputLayer ? downstream.errorVector : null;
			double error;
			
			if (isOutputLayer)
				error = output * (1 - output) * (expected - output);
			else {
				Matrix weightMatrix = downstream.weights.transpose().getRowVector(i).transpose();
				error = output * (1 - output) * weightMatrix.mul(downstreamErrors).get(0, 0);
			}
				
			errorVector.set(i, 0, error);
		}
	}
	
	/**
	 * Updates this layer's weight matrix using the backpropagation weight update rule.
	 * 
	 * @param learningRate - learning rate
	 * @param precedingOutput - outputs of neurons in the preceding layer
	 */
	public void updateWeights(double learningRate, Matrix precedingOutput, Algorithm algorithm) {
		if (algorithm == Algorithm.GROUP || algorithm == Algorithm.MINIBATCH) {
			weights = weights.add(partialDerivations.scalarMul(learningRate));
			partialDerivations.clear();
		} else if (algorithm == Algorithm.ONLINE) {
			for (int i = 0; i < weights.getRows(); i++) {
				for (int j = 0; j < weights.getColumns(); j++) {
					double correction = learningRate * precedingOutput.get(j, 0) * errorVector.get(i, 0);
					double oldWeight = weights.get(i, j);
					
					weights.set(i, j, oldWeight + correction);
				}
			}
		}
	}
	
	/**
	 * Updates the partial derivations matrix by adding terms for each sample.
	 * 
	 * @param precedingOutput - output vector of the preceding layer
	 */
	public void updatePartialDerivations(Matrix precedingOutput) {
		for (int i = 0; i < weights.getRows(); i++) {
			for (int j = 0; j < weights.getColumns(); j++) {
				double oldValue = partialDerivations.get(i, j);
				double newValue = oldValue + errorVector.get(i, 0) * precedingOutput.get(j, 0);
				
				partialDerivations.set(i, j, newValue);
			}
		}
	}
	
	/**
	 * Tests if this layer is the input layer.
	 * 
	 * @param layer - layer to test
	 * @return <code>true</code> if the layer is an input layer, <code>false</code> otherwise
	 */
	public static boolean isInputLayer(NeuralNetworkLayer layer) {
		return layer.weights == null;
	}

	public Matrix getWeights() {
		return weights;
	}

	public void setWeights(Matrix weights) {
		this.weights = weights;
	}

	public Matrix getOutputVector() {
		return outputVector;
	}

	public void setOutputVector(Matrix outputVector) {
		this.outputVector = outputVector;
	}

	public Matrix getErrorVector() {
		return errorVector;
	}

	public void setErrorVector(Matrix errorVector) {
		this.errorVector = errorVector;
	}

	public int getNeurons() {
		return neurons;
	}

	public void setNeurons(int neurons) {
		this.neurons = neurons;
	}

}
