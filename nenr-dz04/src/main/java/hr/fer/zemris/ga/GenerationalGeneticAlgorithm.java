package hr.fer.zemris.ga;

import static hr.fer.zemris.ga.Util.*;

/**
 * Models a generational genetic algorithm.
 * 
 * @author Ivan Skorupan
 */
public class GenerationalGeneticAlgorithm extends AbstractGeneticAlgorithm {
	
	/**
	 * Flag that controls whether elitism is used or not.
	 */
	private boolean elitism;
	
	/**
	 * Alpha parameter for BLX alpha recombination method.
	 */
	private double blxAlpha = 0.5;
	
	/**
	 * Constructs a new {@link GenerationalGeneticAlgorithm} object and intializes the given
	 * parameters.
	 * 
	 * @param populationSize - size of the population
	 * @param generationLimit - number of iterations
	 * @param lossFunction - function to use for calculating fitness
	 * @param verbose - verbose output flag
	 * @param elitism - flag that controls elitism usage
	 * @param blxAlpha - alpha parameter for the BLX alpha method of recombination
	 */
	public GenerationalGeneticAlgorithm(int populationSize, int generationLimit, LossFunction lossFunction, boolean verbose, boolean elitism, double blxAlpha) {
		super(populationSize, generationLimit, lossFunction, verbose);
		this.elitism = elitism;
		this.blxAlpha = blxAlpha;
	}
	
	@Override
	public Individual run() {
		Individual[] population = initializePopulation(populationSize, rand);
		
		for (Individual individual : population)
			individual.setFitness(1. / lossFunction.valueAt(individual.getBetas()));
		
		for (int generation = 1; generation <= generationLimit; generation++) {
			Individual[] nextPopulation = new Individual[populationSize];
			
			if (elitism) {
				nextPopulation[0] = findBest(population);
				if (verbose)
					System.out.println("Generation: " + generation + "; New best: " + nextPopulation[0]);
			}
			
			for (int i = elitism ? 1 : 0; i < populationSize; i++) {
				Individual[] parents = rouletteWheelSelection(population, rand, 2);
				Individual child = blxAlphaCrossover(blxAlpha, parents[0], parents[1], rand);
				normalMutate(child, rand, 0);
				
				child.setFitness(1. / lossFunction.valueAt(child.getBetas()));
				nextPopulation[i] = child;
			}
			
			population = nextPopulation;
		}
		
		return findBest(population);
	}
	
	/**
	 * Getter for elitism flag.
	 * 
	 * @return <code>true</code> if elitism is used, <code>false</code> otherwise
	 */
	public boolean isElitism() {
		return elitism;
	}
	
	/**
	 * Getter for elitism flag.
	 * 
	 * @param elitism - flag indicating if elitism should be used
	 */
	public void setElitism(boolean elitism) {
		this.elitism = elitism;
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
