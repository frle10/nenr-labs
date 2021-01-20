package hr.fer.zemris.nenr.datasetfactory;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

/**
 * Models a custom component on which a user can draw gestures with the mouse.
 * <p>
 * The gesture is drawn by holding the left mouse button and dragging. When the
 * user is finished drawing, releasing the left mouse button will trigger the
 * internal feature extractor that will process the data from the gesture that
 * was just drawn.
 * <p>
 * After releasing the left mouse button, the canvas is also cleared so that there
 * is space for drawing more gestures.
 * 
 * @author Ivan Skorupan
 */
public class GestureCanvas extends JComponent {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * A reference to a feature extractor that takes the mouse input data
	 * from this canvas.
	 */
	private FeatureExtractor featureExtractor;
	
	/**
	 * A symbol currently selected to draw by the user.
	 */
	private Symbol currentSymbol = Symbol.ALPHA;
	
	/**
	 * Reference to the gesture that the user is currently drawing.
	 * This will be <code>null</code> if no gesture is being drawn.
	 */
	private Gesture currentGesture;

	/**
	 * Constructs a new {@link GestureCanvas} object.
	 * 
	 * @param M - number of representative points for gestures
	 * @param datasetFilePath - path to dataset file
	 */
	public GestureCanvas(int M, Path datasetFilePath) {
		featureExtractor = new FeatureExtractor(M, datasetFilePath);
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				System.out.println("New gesture created.");
				currentGesture = new Gesture(currentSymbol);
				currentGesture.addPoint(new Point(e.getX(), e.getY()));
				
				System.out.println("Added point: (" + e.getX() + ", " + e.getY() + ")");
				repaint();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				featureExtractor.addGesture(currentGesture);
				currentGesture = null;
				
				System.out.println("Finished drawing gesture.");
				System.out.println();
				repaint();
			}
		});
		
		addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				currentGesture.addPoint(new Point(e.getX(), e.getY()));
				
				System.out.println("Added point: (" + e.getX() + ", " + e.getY() + ")");
				repaint();
			}
		});
	}
	
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		List<Point> points = (currentGesture != null) ? currentGesture.getPoints() : new ArrayList<>();
		
		for (Point point : points)
			g2d.drawLine(point.x, point.y, point.x, point.y);
	}

	public FeatureExtractor getFeatureExtractor() {
		return featureExtractor;
	}

	public void setFeatureExtractor(FeatureExtractor featureExtractor) {
		this.featureExtractor = featureExtractor;
	}

	public Gesture getCurrentGesture() {
		return currentGesture;
	}

	public void setCurrentGesture(Gesture currentGesture) {
		this.currentGesture = currentGesture;
	}

	public Symbol getCurrentSymbol() {
		return currentSymbol;
	}

	public void setCurrentSymbol(Symbol currentSymbol) {
		this.currentSymbol = currentSymbol;
	}
	
}
