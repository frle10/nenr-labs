package hr.fer.zemris.nenr.neurofuzzy;

/**
 * Models a single example in a dataset.
 * <p>
 * Every example has features and a label. Since
 * we know our examples will contain only two features,
 * they will be separate fields instead of in a list.
 * 
 * @author Ivan Skorupan
 */
public class Example {
	
	/**
	 * Feature x;
	 */
	private double x;
	
	/**
	 * Feature y.
	 */
	private double y;
	
	/**
	 * Goal function value in (x, y).
	 */
	private double label;

	/**
	 * Constructs a new {@link Example} object with given
	 * features <code>x</code> and <code>y</code> and
	 * a <code>label</code>.
	 * 
	 * @param x - feature x
	 * @param y - feature y
	 * @param label - goal function value in (x, y)
	 */
	public Example(double x, double y, double label) {
		this.x = x;
		this.y = y;
		this.label = label;
	}
	
	/**
	 * Constructs a new {@link Example} object.
	 */
	public Example() {}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getLabel() {
		return label;
	}

	public void setLabel(double label) {
		this.label = label;
	}
	
}
