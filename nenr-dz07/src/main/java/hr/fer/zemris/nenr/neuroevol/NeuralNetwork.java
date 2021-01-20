package hr.fer.zemris.nenr.neuroevol;

import static java.lang.Math.abs;
import static java.lang.Math.exp;

import java.util.Arrays;

/**
 * This class is an implementation of a neural network.
 * 
 * @author Ivan Skorupan
 */
public class NeuralNetwork {
	
	private int[] architecture;
	
	private double[] outputMemory;
	
	public NeuralNetwork(String architecture) {
		String[] architectureTokens = architecture.split("x");
		this.architecture = new int[architectureTokens.length];
		
		int totalNeurons = 0;
		this.architecture[0] = Integer.parseInt(architectureTokens[0]);
		
		for (int i = 1; i < architectureTokens.length; i++) {
			int layerNeurons = Integer.parseInt(architectureTokens[i]);
			this.architecture[i] = layerNeurons;
			totalNeurons += layerNeurons;
		}
		
		outputMemory = new double[totalNeurons];
		
		System.out.println("Architecture: " + Arrays.toString(this.architecture));
		System.out.println();
		System.out.println("Output memory length: " + outputMemory.length);
	}
	
	public double calcError(Dataset dataset, double[] parameters) {
		double error = 1. / dataset.size();
		double sum = 0;
		
		for (int i = 0; i < dataset.size(); i++) {
			double[] example = dataset.getExample(i);
			
			double x1 = example[0];
			double x2 = example[1];
			
			double t1 = example[2];
			double t2 = example[3];
			double t3 = example[4];
			
			double[] output = calcOutput(parameters, new double[] {x1, x2});
			
			sum += (t1 - output[0]) * (t1 - output[0]);
			sum += (t2 - output[1]) * (t2 - output[1]);
			sum += (t3 - output[2]) * (t3 - output[2]);
		}
		
		error *= sum;
		return error;
	}
	
	public double[] calcOutput(double[] parameters, double[] input) {
		int type1Neurons = architecture[1];
		double x1 = input[0];
		double x2 = input[1];
		
		// Calculate output of neurons of type 1
		for (int i = 0; i < type1Neurons; i++) {
			double w1 = parameters[4 * i];
			double s1 = parameters[4 * i + 1];
			double w2 = parameters[4 * i + 2];
			double s2 = parameters[4 * i + 3];
			
			double output = 1. / (1 + abs(x1 - w1) / abs(s1) + abs(x2 - w2) / abs(s2));
			outputMemory[i] = output;
		}
		
		// Calculate output of the rest of the neurons
		int offset = 4 * type1Neurons;
		int outputOffset = 0;
		
		for (int i = 2; i < architecture.length; i++) {
			int currentLayerNeurons = architecture[i];
			int prevLayerNeurons = architecture[i - 1];
			
			// How many parameters for one neuron in this layer.
			int params = prevLayerNeurons + 1;
			
			for (int j = 0; j < currentLayerNeurons; j++) {
				double neuronOutput = 0;
				
				// Add the bias first.
				double bias = parameters[offset];
				neuronOutput += bias;
				
				// Add other weighted sum members.
				for (int k = 1; k < params; k++)
					neuronOutput += parameters[offset + k] * outputMemory[outputOffset + k - 1];
				
				// Calculate sigmoid function and save the output.
				neuronOutput = 1. / (1 + exp(-neuronOutput));
				outputMemory[outputOffset + prevLayerNeurons + j] = neuronOutput;
				
				// Update offset.
				offset += params;
			}
			
			// Update output offset.
			outputOffset += prevLayerNeurons;
		}
		
		// Isolate the output.
		int outputNeurons = architecture[architecture.length - 1];
		double[] output = new double[outputNeurons];
		
		offset = outputMemory.length - outputNeurons;
		for (int i = 0; i < outputNeurons; i++)
			output[i] = outputMemory[offset + i];
		
		return output;
	}
	
	public int numberOfParameters() {
		int inputNeurons = architecture[0];
		int type1Neurons = architecture[1];
		
		int parameters = type1Neurons * inputNeurons * 2;
		for (int i = 2; i < architecture.length; i++) {
			int currentLayerNeurons = architecture[i];
			int previousLayerNeurons = architecture[i - 1];
			
			parameters += (currentLayerNeurons * (previousLayerNeurons + 1));
		}
		
		return parameters;
	}

	public int[] getArchitecture() {
		return architecture;
	}

	public void setArchitecture(int[] architecture) {
		this.architecture = architecture;
	}
	
}
