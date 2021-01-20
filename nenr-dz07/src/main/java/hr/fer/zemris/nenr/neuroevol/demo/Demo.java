package hr.fer.zemris.nenr.neuroevol.demo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hr.fer.zemris.nenr.neuroevol.Dataset;
import hr.fer.zemris.nenr.neuroevol.EliminativeGeneticAlgorithm;
import hr.fer.zemris.nenr.neuroevol.Individual;
import hr.fer.zemris.nenr.neuroevol.NeuralNetwork;

import static java.lang.Math.round;
import static java.lang.Math.abs;

public class Demo {

	public static void main(String[] args) {
		double pm1 = Double.parseDouble(args[0]);
		double pm2 = Double.parseDouble(args[1]);
		double pm3 = Double.parseDouble(args[2]);
		
		double t1 = Double.parseDouble(args[3]);
		double t2 = Double.parseDouble(args[4]);
		double t3 = Double.parseDouble(args[5]);
		
		double tSum = t1 + t2 + t3;
		double v1 = t1 / tSum;
		double v2 = t2 / tSum;
		double v3 = t3 / tSum;
		
		System.out.println("Input parameters: " + pm1 + " " + pm2 + " " + pm3 + " " + v1 + " " + v2 + " " + v3);
		System.out.println();
		
		List<String> lines = null;
		try {
			lines = Files.readAllLines(Paths.get("zad7-dataset.txt"));
		} catch (IOException e) {
			System.err.println("There was a problem while reading from file.");
			return;
		}
		
		List<double[]> examples = new ArrayList<>();
		for (int i = 0; i < lines.size(); i++) {
			String line = lines.get(i);
			String[] tokens = line.split("\\s+");
			double[] example = new double[tokens.length];
			
			double x1 = Double.parseDouble(tokens[0]);
			double x2 = Double.parseDouble(tokens[1]);
			
			double o1 = Double.parseDouble(tokens[2]);
			double o2 = Double.parseDouble(tokens[3]);
			double o3 = Double.parseDouble(tokens[4]);
			
			example[0] = x1;
			example[1] = x2;
			
			example[2] = o1;
			example[3] = o2;
			example[4] = o3;
			
			examples.add(example);
			
			System.out.println("Parsed example " + i + ": " + Arrays.toString(example));
		}
		
		Dataset dataset = new Dataset(examples);
		
		String architecture = "2x8x3";
		
		System.out.println();
		NeuralNetwork nn = new NeuralNetwork(architecture);
		
		System.out.println();
		System.out.println("Total parameters for neural network: " + nn.numberOfParameters());
		System.out.println();
		
		EliminativeGeneticAlgorithm ga = new EliminativeGeneticAlgorithm(100, 2000000, nn, dataset);
		
		System.out.println("Starting genetic algorithm...");
		System.out.println();
		
		Individual individual = ga.run(pm1, pm2, pm3, v1, v2, v3);
		System.out.println(Arrays.toString(individual.getParameters()));
		
		System.out.println();
		System.out.println("Genetic algorithm finished.");
		System.out.println("Best MSE: " + nn.calcError(dataset, individual.getParameters()));
		System.out.println();
		
		List<double[]> correctlyClassified = new ArrayList<>();
		List<double[]> incorrectlyClassified = new ArrayList<>();
		
		for (int i = 0; i < dataset.size(); i++) {
			double[] example = dataset.getExample(i);
			double output[] = nn.calcOutput(individual.getParameters(), new double[] {example[0], example[1]});
			
			for (int j = 0; j < output.length; j++)
				output[j] = round(output[j]);
			
			boolean isCorrectlyClassified = true;
			if (abs(output[0] - example[2]) > 1e-6)
				isCorrectlyClassified = false;
			if (abs(output[1] - example[3]) > 1e-6)
				isCorrectlyClassified = false;
			if (abs(output[2] - example[4]) > 1e-6)
				isCorrectlyClassified = false;
			
			if (isCorrectlyClassified)
				correctlyClassified.add(example);
			else
				incorrectlyClassified.add(example);
			
			System.out.print(example[2] + " " + example[3] + " " + example[4] + " | ");
			System.out.println(output[0] + " " + output[1] + " " + output[2] + ", " + isCorrectlyClassified);
		}
		
		System.out.println();
		System.out.println("Correctly classified: " + correctlyClassified.size());
		System.out.println("Incorrectly classified: " + incorrectlyClassified.size());
		
		lines = new ArrayList<>();
		int[] arch = nn.getArchitecture();
		double[] parameters = individual.getParameters();
		
		lines.add("# w1, s1, w2, s2");
		for (int i = 0; i < arch[1]; i++) {
			double w1 = parameters[4 * i];
			double s1 = parameters[4 * i + 1];
			double w2 = parameters[4 * i + 2];
			double s2 = parameters[4 * i + 3];
			
			lines.add(w1 + " " + s1 + " " + w2 + " " + s2);
		}
		
		try {
			Files.write(Paths.get("export.dat"), lines);
		} catch (IOException e) {
			System.err.println("RIP");
		}
	}

}
