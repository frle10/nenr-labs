package hr.fer.zemris.nenr.neuralnetwork;

import java.awt.BorderLayout;
import java.awt.Container;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * This is a GUI program that enables the user to train an artificial
 * neural network to recognize specific gestures. First, the neural
 * network is trained using a file with gesture features and afterwards
 * the user is presented with a GUI where they can draw more signature
 * that the trained neural network will then classify.
 * <p>
 * The supported gestures are small Greek letters: alpha, beta, gamma, delta and epsilon.
 * <p>
 * The program supports the following command line arguments:
 * <ul>
 * 	<li>path to file with learning examples</li>
 * 	<li>neural network architecture (in format axbxcx...x)</li>
 * 	<li>parameter M, number of gesture representative points</li>
 * 	<li>mark of the learning algorithm (group, online or mini-batch, represented by numbers 1, 2 and 3 respectively)</li>
 * </ul>
 * 
 * @author Ivan Skorupan
 */
public class GestureDetector extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Reference to a canvas that the user draws gestures on.
	 */
	private PredictionGestureCanvas canvas;
	
	/**
	 * Constructs a new {@link GestureDetector} object.
	 * 
	 * @param M - number of representative points for gestures
	 * @param nn - neural network to use for classifying user gestures
	 */
	public GestureDetector(int M, NeuralNetwork nn) {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Gesture Detector");
		setSize(800, 600);
		setLocationRelativeTo(null);
		initGUI(M, nn);
	}
	
	/**
	 * Initializes and places GUI components on the window.
	 * 
	 * @param M - number of representative points for gestures
	 * @param datasetFilePath - path to dataset file
	 */
	private void initGUI(int M, NeuralNetwork nn) {
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
		
		canvas = new PredictionGestureCanvas(M, nn);
		cp.add(canvas, BorderLayout.CENTER);
	}

	/**
	 * Starting point of this program's execution.
	 * 
	 * @param args - command line arguments
	 */
	public static void main(String[] args) {
		if (args.length != 5) {
			System.err.println("Five arguments are expected: dataset path, validation path, neural network architecture, M and learning algorithm code .");
			return ;
		}
		
		Path datasetFilePath;
		try {
			datasetFilePath = Paths.get(args[0]);
		} catch(InvalidPathException ex) {
			System.err.println("The path to export file is not valid.");
			return;
		}
		
		Path validationFilePath;
		try {
			validationFilePath = Paths.get(args[1]);
		} catch(InvalidPathException ex) {
			System.err.println("The path to export file is not valid.");
			return;
		}
		
		String[] architectureTokens = args[2].split("x");
		int[] architecture = new int[architectureTokens.length];
		for (int i = 0; i < architectureTokens.length; i++) {
			try {
				architecture[i] = Integer.parseInt(architectureTokens[i]);
			} catch(NumberFormatException ex) {
				System.err.println("The architecture string is invalid.");
				return;
			}
		}
		
		Algorithm algorithm = Algorithm.GROUP;
		try {
			int code = Integer.parseInt(args[4]);
			
			if (code < 1 || code > 3)
				throw new NumberFormatException();
			
			algorithm = convertCodeToAlgorithm(code);
		} catch(NumberFormatException ex) {
			System.err.println("The algorithm code must be an integer that's 1, 2 or 3.");
			return;
		}
		
		int m = 0;
		try {
			m = Integer.parseInt(args[3]);
		} catch(NumberFormatException ex) {
			System.err.println("The first argument must be a valid integer that represents the M parameter.");
			return;
		}
		
		if (architecture[0] != 2 * m || architecture[architecture.length - 1] != 5) {
			System.err.println("The number of input neurons must be equal to 2 * M and the number of output neurons must be equal to 5.");
			return;
		}
		
		final int M = m;
		
		NeuralNetwork nn = new NeuralNetwork(algorithm, architecture, datasetFilePath, validationFilePath);
		nn.fit();
		
		SwingUtilities.invokeLater(() -> new GestureDetector(M, nn).setVisible(true));
	}
	
	/**
	 * Converts an integer code to a corresponding training algorithm.
	 * 
	 * @param code - integer code to convert
	 * @return algorithm to use for training
	 * @throw IllegalArgumentException if <code>code</code> is not supported
	 */
	private static Algorithm convertCodeToAlgorithm(int code) {
		if (code == 1) return Algorithm.GROUP;
		else if (code == 2) return Algorithm.ONLINE;
		else if (code == 3) return Algorithm.MINIBATCH;
		else throw new IllegalArgumentException("The given code is not supported.");
	}
	
}
