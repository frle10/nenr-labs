package hr.fer.zemris.nenr.neuroevol;

import java.util.Arrays;

/**
 * Models one individual in a genetic algorithm. Each individual
 * contains its genetic components (here this is an array of real numbers)
 * and the fitness value indicating how good this individual is.
 * 
 * @author Ivan Skorupan
 */
public class Individual {
	
	/**
	 * An array of genetic components (function's beta parameters).
	 */
	private double[] parameters;
	
	/**
	 * The fitness value.
	 */
	private double fitness;
	
	/**
	 * Constructs a new {@link Individual} object initializing it with
	 * given <code>betas</code>.
	 * 
	 * @param parameters - this individual's genetic components
	 */
	public Individual(double[] parameters) {
		this.parameters = parameters;
	}
	
	/**
	 * Getter for this individual's genetic material.
	 * 
	 * @return this individual's genetic components
	 */
	public double[] getParameters() {
		return parameters;
	}

	/**
	 * Setter for this individual's genetic material.
	 * 
	 * @param parameters - genetic components to set on this individual
	 */
	public void setParameters(double[] parameters) {
		this.parameters = parameters;
	}

	/**
	 * Getter for the fitness value of this individual.
	 * 
	 * @return - the fitness value
	 */
	public double getFitness() {
		return fitness;
	}

	/**
	 * Setter for the fitness value of this individual.
	 * 
	 * @param fitness - this individual's fitness value
	 */
	public void setFitness(double fitness) {
		this.fitness = fitness;
	}

	@Override
	public String toString() {
		return "[parameters = " + Arrays.toString(parameters) + ", fitness = " + (1. / fitness) + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(parameters);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Individual))
			return false;
		Individual other = (Individual) obj;
		return Arrays.equals(parameters, other.parameters);
	}
	
}
