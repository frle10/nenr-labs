package hr.fer.zemris.ga.demo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import hr.fer.zemris.ga.EliminativeGeneticAlgorithm;
import hr.fer.zemris.ga.GenerationalGeneticAlgorithm;
import hr.fer.zemris.ga.GeneticAlgorithm;
import hr.fer.zemris.ga.Individual;
import hr.fer.zemris.ga.LossFunction;
import hr.fer.zemris.ga.MeanSquaresLossFunction;
import hr.fer.zemris.ga.Measurement;

/**
 * This program reads the dataset file with measurements, asks
 * the user to input the neccessary genetic algorithm parameters
 * and then performs the algorithm and prints out the result.
 * 
 * @author Ivan Skorupan
 */
public class Demo {
	
	/**
	 * Path to the dataset .txt file containing the measurements.
	 */
	private static final String DATASET_PATH_STRING = "zad4-dataset2.txt";
	
	/**
	 * The starting point for this program's execution.
	 * 
	 * @param args - command line arguments (not used)
	 */
	public static void main(String[] args) {
		Path datasetPath = Paths.get(DATASET_PATH_STRING);
		List<String> measurementStrings;
		
		try {
			measurementStrings = Files.readAllLines(datasetPath);
		} catch (IOException e) {
			System.err.println("There was an error while reading the dataset file.");
			e.printStackTrace();
			return;
		}
		
		List<Measurement> measurements = new ArrayList<>();
		measurementStrings.forEach(ms -> {
			String[] tokens = ms.split("\\s+");
			Double[] values = new Double[3];
			
			for (int i = 0; i < tokens.length; i++)
				values[i] = Double.parseDouble(tokens[i]);
			
			measurements.add(new Measurement(values[0], values[1], values[2]));
		});
		
		Scanner scanner = new Scanner(System.in);
		
		System.out.print("Enter the population size: ");
		int populationSize = scanner.nextInt();
		
		System.out.print("Enter the maximum number of iterations: ");
		int maxIterations = scanner.nextInt();
		
		LossFunction lossFunction = new MeanSquaresLossFunction(measurements);
		// GeneticAlgorithm gga = new GenerationalGeneticAlgorithm(populationSize, maxIterations, lossFunction, true, true, 0.5);
		GeneticAlgorithm ega = new EliminativeGeneticAlgorithm(populationSize, maxIterations, lossFunction, true,  0.5);
		
		System.out.println();
		
		Individual best = ega.run();
		
		System.out.println();
		System.out.println("Best solution: " + best);
		System.out.println();
		
		scanner.close();
	}

}
