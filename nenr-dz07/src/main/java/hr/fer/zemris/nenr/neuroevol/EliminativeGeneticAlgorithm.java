package hr.fer.zemris.nenr.neuroevol;

import static java.lang.Math.round;

import java.util.ArrayList;
import java.util.List;

import static hr.fer.zemris.nenr.neuroevol.Util.*;

/**
 * Models an eliminative genetic algorithm using 3-tournament selection.
 * 
 * @author Ivan Skorupan
 */
public class EliminativeGeneticAlgorithm extends AbstractGeneticAlgorithm {

	/**
	 * Alpha parameter for BLX alpha recombination method.
	 */
	private double blxAlpha = 0.5;
	
	private NeuralNetwork nn;
	
	private Dataset dataset;

	/**
	 * Constructs a new {@link EliminativeGeneticAlgorithm} object and intializes the given
	 * parameters.
	 * 
	 * @param populationSize - size of the population
	 * @param generationLimit - number of iterations
	 * @param lossFunction - function to use for calculating fitness
	 * @param verbose - verbose output flag
	 * @param blxAlpha - alpha parameter for the BLX alpha method of recombination
	 */
	public EliminativeGeneticAlgorithm(int populationSize, int generationLimit, NeuralNetwork nn, Dataset dataset) {
		super(populationSize, generationLimit);
		this.nn = nn;
		this.dataset = dataset;
	}
	
	public Individual run(double pm1, double pm2, double pm3, double v1, double v2, double v3) {
		Individual[] population = initializePopulation(populationSize, nn.numberOfParameters(), rand);

		for (Individual individual : population)
			individual.setFitness(1. / nn.calcError(dataset, individual.getParameters()));
		
//		System.out.println("Printing initial population...");
//		System.out.println();
//		for (int i = 0; i < population.length; i++) {
//			Individual individual = population[i];
//			System.out.println("Individual " + i + ": " + individual);
//		}

		int[] tournamentIndices = new int[3];
		for (int generation = 1; generation <= generationLimit; generation++) {
			// Perform 3-tournament selection
			for (int i = 0; i < tournamentIndices.length; i++) {
				int index = (int)round(rand.nextDouble() * (population.length - 1));
				tournamentIndices[i] = index;
			}
			
			int worstIndex = findWorstIndex(tournamentIndices, population);
			
			List<Individual> parents = new ArrayList<>();
			for (int i = 0; i < tournamentIndices.length; i++)
				if (i != worstIndex)
					parents.add(population[tournamentIndices[i]]);
			
			Individual child;
			int crossoverDecision = rand.nextInt(3);
			crossoverDecision = 0;
			
			if (crossoverDecision == 0)
				child = blxAlphaCrossover(blxAlpha, parents.get(0), parents.get(1), rand);
			else if (crossoverDecision == 1)
				child = arithmeticCrossover(parents.get(0), parents.get(1), rand);
			else
				child = onePointCrossover(parents.get(0), parents.get(1), rand);
			
			double mutationDecision = rand.nextDouble();
			
			if (mutationDecision < v1)
				normalMutate(child, rand, pm1, 0.002);
			else if (mutationDecision < v1 + v2)
				normalMutate(child, rand, pm2, 0.005);
			else
				normalResetMutate(child, rand, pm3, 0.4);
			
			double error = nn.calcError(dataset, child.getParameters());
			double fitness = 1. / error;
			child.setFitness(fitness);
			
			if (error <= 1e-7)
				return child;
			
			System.out.println("Generation: " + generation + ", error = " + error);
			
			population[tournamentIndices[worstIndex]] = child;
		}

		return findBest(population);
	}
	
	/**
	 * Finds the index of the index of the worst individual in the tournament.
	 * 
	 * @param tournamentIndices - population indices of randomly selected individuals
	 * @param population - population of individuals
	 * @return index of the index of the worst individual in the tournament
	 */
	private int findWorstIndex(int[] tournamentIndices, Individual[] population) {
		int worstIndex = 0;
		double worstFitness = population[tournamentIndices[0]].getFitness();
		
		for (int i = 1; i < tournamentIndices.length; i++) {
			int currentIndividualIndex = tournamentIndices[i];
			double currentFitness = population[currentIndividualIndex].getFitness();
			
			if (currentFitness < worstFitness) {
				worstIndex = i;
				worstFitness = currentFitness;
			}
		}
		
		return worstIndex;
	}

}
