package hr.fer.zemris.nenr.neuroevol;

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
	 * Initializes the starting population with <code>populationSize</code> individuals
	 * by randomly generating each individual's components (beta parameters)
	 * using <code>rand</code>.
	 * 
	 * @param populationSize - number of individuals to generate
	 * @param rand - an object used to generate random individual components
	 * @return the generated population
	 */
	public static Individual[] initializePopulation(int populationSize, int numberOfParameters, Random rand) {
		Individual[] population = new Individual[populationSize];
		
		for (int i = 0; i < population.length; i++) {
			double[] parameters = new double[numberOfParameters];
			
			for (int j = 0; j < parameters.length; j++) {
				double parameter = rand.nextDouble() * 2 - 1;
				parameters[j] = parameter;
			}
			
			population[i] = new Individual(parameters);
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
	 * @param mutationProbability
	 * @param stdDev
	 */
	public static void normalMutate(Individual individual, Random rand, double mutationProbability, double stdDev) {
		double[] geneticMaterial = individual.getParameters();
		
		for (int i = 0; i < geneticMaterial.length; i++) {
			double decision = rand.nextDouble();
			
			if (decision <= mutationProbability) {
				double mutationValue = rand.nextGaussian() * stdDev;
				geneticMaterial[i] += mutationValue;
			}
		}
	}
	
	public static void normalResetMutate(Individual individual, Random rand, double mutationProbability, double stdDev) {
		double[] geneticMaterial = individual.getParameters();
		
		for (int i = 0; i < geneticMaterial.length; i++) {
			double decision = rand.nextDouble();
			
			if (decision <= mutationProbability) {
				double mutationValue = rand.nextGaussian() * stdDev;
				geneticMaterial[i] = mutationValue;
			}
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
		double[] childGenes = new double[parent1.getParameters().length];
		double[] parent1Genes = parent1.getParameters();
		double[] parent2Genes = parent2.getParameters();
		
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
	
	public static Individual arithmeticCrossover(Individual parent1, Individual parent2, Random rand) {
		double[] childGenes = new double[parent1.getParameters().length];
		double[] parent1Genes = parent1.getParameters();
		double[] parent2Genes = parent2.getParameters();
		
		for (int i = 0; i < childGenes.length; i++) {
			double a = rand.nextDouble();
			childGenes[i] = a * parent1Genes[i] + (1 - a) * parent2Genes[i];
		}
		
		return new Individual(childGenes);
	}
	
	public static Individual onePointCrossover(Individual parent1, Individual parent2, Random rand) {
		double[] childGenes = new double[parent1.getParameters().length];
		double[] parent1Genes = parent1.getParameters();
		double[] parent2Genes = parent2.getParameters();
		
		int point = (int) round(rand.nextDouble() * parent1Genes.length);
		for (int i = 0; i < point; i++)
			childGenes[i] = parent1Genes[i];
		
		for (int i = point; i < childGenes.length; i++)
			childGenes[i] = parent2Genes[i];
		
		return new Individual(childGenes);
	}
	
}
