package hr.fer.zemris.nenr.neuroevol;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Models a dataset as a list of examples, where each example
 * has a list of features and a label.
 * 
 * @author Ivan Skorupan
 */
public class Dataset {
	
	/**
	 * List of examples contained in this dataset.
	 */
	List<double[]> examples;
	
	/**
	 * Constructs a new {@link Dataset} object and initializes
	 * it with given <code>examples</code>.
	 * 
	 * @param examples - examples to store in this dataset
	 */
	public Dataset(List<double[]> examples) {
		this.examples = examples;
	}
	
	/**
	 * Constructs a new {@link Dataset} object with no
	 * examples stored initially.
	 */
	public Dataset() {
		examples = new ArrayList<>();
	}
	
	/**
	 * Returns the number of examples in this dataset.
	 * 
	 * @return number of examples in this dataset
	 */
	public int size() {
		return examples.size();
	}
	
	/**
	 * Adds the given <code>example</code> to this dataset.
	 * 
	 * @param example - example to add
	 * @throws NullPointerException if <code>example</code> is <code>null</code>
	 */
	public void addExample(double[] example) {
		Objects.requireNonNull(example);
		examples.add(example);
	}
	
	/**
	 * Removes the example at given <code>index</code>.
	 * 
	 * @param index - index of example to remove
	 * @throws IllegalArgumentException if <code>index</code> is out of bounds
	 */
	public void removeExample(int index) {
		if (index < 0 || index >= examples.size())
			throw new IllegalArgumentException("Example index is out of bounds.");
		
		examples.remove(index);
	}
	
	/**
	 * Returns the example at the given <code>index</code> in this dataset.
	 * 
	 * @param index - index of example to fetch
	 * @return the example at <code>index</code>
	 * @throws IllegalArgumentException if <code>index</code> is out of bounds
	 */
	public double[] getExample(int index) {
		if (index < 0 || index >= examples.size())
			throw new IllegalArgumentException("Example index is out of bounds.");
		
		return examples.get(index);
	}

	public List<double[]> getExamples() {
		return examples;
	}

	public void setExamples(List<double[]> examples) {
		this.examples = examples;
	}
	
}
