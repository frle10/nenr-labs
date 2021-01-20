package hr.fer.zemris.nenr.neuralnetwork;

public class Example {
	
	public Matrix features;
	
	public Matrix label;

	public Example(Matrix features, Matrix label) {
		this.features = features;
		this.label = label;
	}
	
}
