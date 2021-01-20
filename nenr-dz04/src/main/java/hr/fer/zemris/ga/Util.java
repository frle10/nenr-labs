package hr.fer.zemris.ga;

import java.util.Random;

import static java.lang.Math.*;

/**
 * A class containing public constants used throughout this package for
 * genetic algorithms implementation and also some methods used in the
 * algorithm.
 * 
 * @author Ivan Skorupan
 */
public class Util {
	
	/**
	 * The lower bound for b0, b1, b2, b3 and b4 function parameters.
	 */
	public static final int BETA_LOWER_BOUND = -4;
	
	/**
	 * The upper bound for b0, b1, b2, b3 and b4 function parameters.
	 */
	public static final int BETA_UPPER_BOUND = 4;
	
	/**
	 * Number of components in one individual.
	 */
	public static final int NUMBER_OF_PARAMETERS = 5;
	
	/**
	 * Initializes the starting population with <code>populationSize</code> individuals
	 * by randomly generating each individual's components (beta parameters)
	 * using <code>rand</code>.
	 * 
	 * @param populationSize - number of individuals to generate
	 * @param rand - an object used to generate random individual components
	 * @return the generated population
	 */
	public static Individual[] initializePopulation(int populationSize, Random rand) {
		Individual[] population = new Individual[populationSize];
		for (int i = 0; i < population.length; i++) {
			double[] betas = new double[NUMBER_OF_PARAMETERS];
			for (int j = 0; j < betas.length; j++) {
				double iBeta = rand.nextDouble() * (BETA_UPPER_BOUND - BETA_LOWER_BOUND) + BETA_LOWER_BOUND;
				betas[j] = iBeta;
			}
			
			population[i] = new Individual(betas);
		}
		
		return population;
	}
	
	/**
	 * Given a <code>population</code>, finds and returns the best individual.
	 * The best individual is one with the best fitness value.
	 * 
	 * @param population - an array of individuals to search from
	 * @return the best individual in the <code>population</code>
	 */
	public static Individual findBest(Individual[] population) {
		Individual bestIndividual = population[0];
		double bestFitness = bestIndividual.getFitness();
		
		for (int i = 1; i < population.length; i++) {
			Individual current = population[i];
			double fitness = current.getFitness();
			
			if (fitness > bestFitness) {
				bestIndividual = current;
				bestFitness = fitness;
			}
		}
		
		return bestIndividual;
	}
	
	/**
	 * Mutates the genetic material of given <code>individual</code> using
	 * normal distribution as a stochastic property.
	 * 
	 * @param individual - individual to mutate
	 * @param rand - an object to use for generating random mutation values
	 */
	public static void normalMutate(Individual individual, Random rand, double stdDev) {
		double[] geneticMaterial = individual.getBetas();
		for (int i = 0; i < geneticMaterial.length; i++) {
			double mutationValue = rand.nextGaussian() * stdDev;
			geneticMaterial[i] += mutationValue;
		}
	}
	
	/**
	 * Implements the BLX alpha crossover algorithm to generate a child individual from given
	 * parents <code>parent1</code> and <code>parent2</code>.
	 * 
	 * @param alpha - alpha parameter for BLX crossover
	 * @param parent1 - the first parent
	 * @param parent2 - the second parent
	 * @param rand - an object to use for implementing stochastic properties of this algorithm
	 * @return the generated child individual
	 */
	public static Individual blxAlphaCrossover(double alpha, Individual parent1, Individual parent2, Random rand) {
		double[] childGenes = new double[NUMBER_OF_PARAMETERS];
		double[] parent1Genes = parent1.getBetas();
		double[] parent2Genes = parent2.getBetas();
		
		for (int i = 0; i < childGenes.length; i++) {
			double ciMin = min(parent1Genes[i], parent2Genes[i]);
			double ciMax = max(parent1Genes[i], parent2Genes[i]);
			double iI = ciMax - ciMin;
			
			double lowerBound = ciMin - iI * alpha;
			double upperBound = ciMax + iI * alpha;
			
			childGenes[i] = rand.nextDouble() * (upperBound - lowerBound) + lowerBound;
		}
		
		return new Individual(childGenes);
	}
	
	/**
	 * Implements a roulette wheel selection of parents that will be used for recombination.
	 * Individuals with better fitness have a higher chance of being chosen.
	 * 
	 * @param population - population of individuals to choose from
	 * @param rand - object to use for implementing the algorithm's stochastic properties
	 * @param howMany - number of parents to choose from the population
	 * @return an array of individuals containing the chosen parents
	 */
	public static Individual[] rouletteWheelSelection(Individual[] population, Random rand, int howMany) {
		Individual[] parents = new Individual[howMany];
		
		double sum = 0;
		for (int i = 0; i < population.length; i++)
			sum += population[i].getFitness();
		
		for (int parentIndex = 0; parentIndex < howMany; parentIndex++) {
			double limit = rand.nextDouble() * sum;
			int chosen = 0;
			double upperLimit = population[chosen].getFitness();
			
			while (upperLimit < limit && chosen < population.length - 1) {
				chosen++;
				upperLimit += population[chosen].getFitness();
			}
			
			parents[parentIndex] = population[chosen];
		}
		
		return parents;
	}
	
}
