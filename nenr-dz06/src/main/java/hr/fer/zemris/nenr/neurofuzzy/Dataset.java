package hr.fer.zemris.nenr.neurofuzzy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * Models a dataset as a list of examples, where each example
 * has a list of features and a label.
 * 
 * @author Ivan Skorupan
 */
public class Dataset implements Iterable<Example> {
	
	/**
	 * List of examples contained in this dataset.
	 */
	List<Example> examples;
	
	/**
	 * Constructs a new {@link Dataset} object and initializes
	 * it with given <code>examples</code>.
	 * 
	 * @param examples - examples to store in this dataset
	 */
	public Dataset(List<Example> examples) {
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
	public void addExample(Example example) {
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
	public Example getExample(int index) {
		if (index < 0 || index >= examples.size())
			throw new IllegalArgumentException("Example index is out of bounds.");
		
		return examples.get(index);
	}
	
	/**
	 * Exports this dataset to a file so it can be graphed with
	 * tools such as gnuplot.
	 * 
	 * @param file - path to export file
	 * @throws IOException if there is an error while writing to <code>file</code>
	 */
	public void exportToFile(Path file) throws IOException {
		List<String> lines = new ArrayList<>();
		lines.add("# X, Y, f(X, Y)");
		
		String output;
		for (Example example : this) {
			output = example.getX() + " " + example.getY() + " " + example.getLabel();
			lines.add(output);
		}
		
		Files.write(file, lines);
	}

	public List<Example> getExamples() {
		return examples;
	}

	public void setExamples(List<Example> examples) {
		this.examples = examples;
	}

	@Override
	public Iterator<Example> iterator() {
		return examples.iterator();
	}
	
}
