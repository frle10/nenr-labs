package hr.fer.zemris.nenr.datasetfactory;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

/**
 * This is a GUI program that enables the user to draw gestures that will later
 * be used to train a neural network. The user is presented with a canvas in which
 * they can draw the gestures using a mouse.
 * <p>
 * The supported gestures are small Greek letters: alpha, beta, gamma, delta and epsilon.
 * <p>
 * The program supports the following command line arguments:
 * <ul>
 * 	<li>parameter M, number of gesture representative points</li>
 * 	<li>path to file with learning examples</li>
 * </ul>
 * 
 * @author Ivan Skorupan
 */
public class GestureMaker extends JFrame {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Reference to a canvas that the user draws gestures on.
	 */
	private GestureCanvas canvas;

	/**
	 * Constructs a new {@link GestureMaker} object.
	 * 
	 * @param M - number of representative points for gestures
	 * @param datasetFilePath - path to dataset file
	 */
	public GestureMaker(int M, Path datasetFilePath) {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Gesture Maker");
		setSize(800, 600);
		setLocationRelativeTo(null);
		initGUI(M, datasetFilePath);
	}
	
	/**
	 * Starting point of this program's execution.
	 * 
	 * @param args - command line arguments
	 */
	public static void main(String[] args) {
		if (args.length != 2) {
			System.err.println("Two arguments are expected: M and export file path.");
			return ;
		}
		
		int m = 0;
		try {
			m = Integer.parseInt(args[0]);
		} catch(NumberFormatException ex) {
			System.err.println("The first argument must be a valid integer that represents the M parameter.");
			return;
		}
		
		final int M = m;
		Path datasetFilePath;
		try {
			datasetFilePath = Paths.get(args[1]);
		} catch(InvalidPathException ex) {
			System.err.println("The path to export file is not valid.");
			return;
		}
		
		SwingUtilities.invokeLater(() -> new GestureMaker(M, datasetFilePath).setVisible(true));
	}
	
	/**
	 * Initializes and places GUI components on the window.
	 * 
	 * @param M - number of representative points for gestures
	 * @param datasetFilePath - path to dataset file
	 */
	private void initGUI(int M, Path datasetFilePath) {
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
		
		canvas = new GestureCanvas(M, datasetFilePath);
		cp.add(canvas, BorderLayout.CENTER);
		
		JPanel actionButtonsPanel = new JPanel();
		cp.add(actionButtonsPanel, BorderLayout.PAGE_END);
		
		JButton exportButton = new JButton(export);
		actionButtonsPanel.add(exportButton);
		
		JToolBar toolbar = new JToolBar("Tools");
		toolbar.setFloatable(true);
		
		JToggleButton alphaButton = new JToggleButton(alpha);
		JToggleButton betaButton = new JToggleButton(beta);
		JToggleButton gammaButton = new JToggleButton(gamma);
		JToggleButton deltaButton = new JToggleButton(delta);
		JToggleButton epsilonButton = new JToggleButton(epsilon);
		
		ButtonGroup symbolsGroup = new ButtonGroup();
		symbolsGroup.add(alphaButton);
		symbolsGroup.add(betaButton);
		symbolsGroup.add(gammaButton);
		symbolsGroup.add(deltaButton);
		symbolsGroup.add(epsilonButton);
		
		alphaButton.setSelected(true);
		
		toolbar.add(alphaButton);
		toolbar.add(betaButton);
		toolbar.add(gammaButton);
		toolbar.add(deltaButton);
		toolbar.add(epsilonButton);
		
		cp.add(toolbar, BorderLayout.PAGE_START);
		createActions();
	}
	
	/**
	 * Helper method which configures all actions defined in this class.
	 */
	private void createActions() {
		alpha.putValue(Action.NAME, "Alpha");
		alpha.putValue(Action.SHORT_DESCRIPTION, "Draw alpha");
		alpha.setEnabled(true);
		
		beta.putValue(Action.NAME, "Beta");
		beta.putValue(Action.SHORT_DESCRIPTION, "Draw beta");
		beta.setEnabled(true);
		
		gamma.putValue(Action.NAME, "Gamma");
		gamma.putValue(Action.SHORT_DESCRIPTION, "Draw gamma");
		gamma.setEnabled(true);
		
		delta.putValue(Action.NAME, "Delta");
		delta.putValue(Action.SHORT_DESCRIPTION, "Draw delta");
		delta.setEnabled(true);
		
		epsilon.putValue(Action.NAME, "Epsilon");
		epsilon.putValue(Action.SHORT_DESCRIPTION, "Draw epsilon");
		epsilon.setEnabled(true);
		
		export.putValue(Action.NAME, "Export");
		export.putValue(Action.SHORT_DESCRIPTION, "Export generated features");
		export.setEnabled(true);
	}
	
	private final Action export = new AbstractAction() {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			canvas.getFeatureExtractor().export();
		}
	};
	
	private final Action alpha = new AbstractAction() {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			canvas.setCurrentSymbol(Symbol.ALPHA);
		}
	};
	
	private final Action beta = new AbstractAction() {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			canvas.setCurrentSymbol(Symbol.BETA);
		}
	};
	
	private final Action gamma = new AbstractAction() {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			canvas.setCurrentSymbol(Symbol.GAMMA);
		}
	};
	
	private final Action delta = new AbstractAction() {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			canvas.setCurrentSymbol(Symbol.DELTA);
		}
	};
	
	private final Action epsilon = new AbstractAction() {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			canvas.setCurrentSymbol(Symbol.EPSILON);
		}
	};

}
