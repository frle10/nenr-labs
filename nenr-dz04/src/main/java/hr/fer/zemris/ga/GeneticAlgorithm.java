package hr.fer.zemris.ga;

/**
 * This interface models genetic algorithm features.
 * Every genetic algorithm needs to provide a method
 * that runs it and returns the best solution found.
 * 
 * @author Ivan Skorupan
 */
public interface GeneticAlgorithm {
	
	/**
	 * Runs the genetic algorithm and upon finishing returns
	 * the best solution found.
	 * 
	 * @return the best individual found
	 */
	Individual run();
	
}
