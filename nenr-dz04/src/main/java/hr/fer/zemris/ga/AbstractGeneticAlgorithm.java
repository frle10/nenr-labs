package hr.fer.zemris.ga;

import java.util.Random;

/**
 * Models an abstract genetic algorithm. Each genetic algorithm, regardless
 * of implementation, will contain basic parameters such as population size,
 * generation limit, a loss function and an object to use for implementing
 * stochastic properties.
 * 
 * @author Ivan Skorupan
 */
public abstract class AbstractGeneticAlgorithm implements GeneticAlgorithm {
	
	/**
	 * Size of the population.
	 */
	protected int populationSize;
	
	/**
	 * Maximum number of generations to generate (number of iterations of the algorithm).
	 */
	protected int generationLimit;
	
	/**
	 * A loss function to be used for calculating fitness.
	 */
	protected LossFunction lossFunction;
	
	/**
	 * An object used for implementing stochastic properties in the algorithm.
	 */
	protected Random rand = new Random();
	
	/**
	 * Verbose output flag.
	 */
	protected boolean verbose;
	
	/**
	 * Constructs a new {@link AbstractGeneticAlgorithm} object and intializes
	 * the necessary parameters.
	 * 
	 * @param populationSize - size of the population
	 * @param generationLimit - number of iterations
	 * @param lossFunction - function to use for calculating fitness
	 * @param verbose - verbose output flag
	 */
	public AbstractGeneticAlgorithm(int populationSize, int generationLimit, LossFunction lossFunction, boolean verbose) {
		this.populationSize = populationSize;
		this.generationLimit = generationLimit;
		this.lossFunction = lossFunction;
		this.verbose = verbose;
	}
	
	/**
	 * Getter for population size.
	 * 
	 * @return size of the population
	 */
	public int getPopulationSize() {
		return populationSize;
	}

	/**
	 * Setter for population size.
	 * 
	 * @param populationSize - value of new population size
	 */
	public void setPopulationSize(int populationSize) {
		this.populationSize = populationSize;
	}

	/**
	 * Getter for maximum number of generations.
	 * 
	 * @return number of iterations for genetic algorithm
	 */
	public int getGenerationLimit() {
		return generationLimit;
	}

	/**
	 * Setter for generation limit.
	 * 
	 * @param generationLimit - new maximum number of iterations for the algorithm
	 */
	public void setGenerationLimit(int generationLimit) {
		this.generationLimit = generationLimit;
	}

	/**
	 * Getter for the loss function.
	 * 
	 * @return loss function used for fitness calculation
	 */
	public LossFunction getLossFunction() {
		return lossFunction;
	}

	/**
	 * Setter for the loss function.
	 * 
	 * @param lossFunction - new loss function to use
	 */
	public void setLossFunction(LossFunction lossFunction) {
		this.lossFunction = lossFunction;
	}

	/**
	 * Getter for object with stochastic properties.
	 * 
	 * @return a {@link Random} instance
	 */
	public Random getRand() {
		return rand;
	}

	/**
	 * Setter for object with stochastic properties.
	 * 
	 * @param rand - a {@link Random} instance
	 */
	public void setRand(Random rand) {
		this.rand = rand;
	}

	/**
	 * Getter for verbose output flag.
	 * 
	 * @return verbose output flag value
	 */
	public boolean isVerbose() {
		return verbose;
	}

	/**
	 * Setter for verbose output flag.
	 * 
	 * @param verbose - verbose output flag value
	 */
	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}
	
}
