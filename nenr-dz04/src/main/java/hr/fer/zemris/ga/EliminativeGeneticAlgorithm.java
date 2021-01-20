package hr.fer.zemris.ga;

import static java.lang.Math.round;

import java.util.ArrayList;
import java.util.List;

import static hr.fer.zemris.ga.Util.*;

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
	public EliminativeGeneticAlgorithm(int populationSize, int generationLimit, LossFunction lossFunction, boolean verbose, double blxAlpha) {
		super(populationSize, generationLimit, lossFunction, verbose);
		this.blxAlpha = blxAlpha;
	}

	@Override
	public Individual run() {
		Individual[] population = initializePopulation(populationSize, rand);

		for (Individual individual : population)
			individual.setFitness(1. / lossFunction.valueAt(individual.getBetas()));

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
			
			Individual child = blxAlphaCrossover(blxAlpha, parents.get(0), parents.get(1), rand);
			normalMutate(child, rand, 0);
			child.setFitness(1. / lossFunction.valueAt(child.getBetas()));
			
			if (verbose)
				System.out.println("Generation: " + generation + "; Generated new child: " + child);
			
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

	/**
	 * Getter for the alpha parameter in BLX Alpha recombination method.
	 * 
	 * @return alpha parameter for BLX alpha
	 */
	public double getBlxAlpha() {
		return blxAlpha;
	}

	/**
	 * Getter for the alpha parameter in BLX Alpha recombination method.
	 * 
	 * @param blxAlpha - alpha parameter for BLX alpha
	 */
	public void setBlxAlpha(double blxAlpha) {
		this.blxAlpha = blxAlpha;
	}

}
