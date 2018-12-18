import java.awt.BorderLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.jfree.ui.RefineryUtilities;

/**
 * The Class GraphicInterface.
 */
public abstract class GraphicInterface {
	
	/** The frame. */
	protected JFrame frame;
	
	/** The text areas. */
	protected ArrayList<JTextArea> textAreas;
	
	/** The panel. */
	protected JPanel panel;	
	
	/** The stored values. */
	protected Map<Integer, ArrayList<ArrayList<Integer>>> values;
	
	/**
	 * Instantiates a new graphic interface.
	 */
	protected GraphicInterface() {
		frame = new JFrame("Tableau de bord");
		textAreas = new ArrayList<JTextArea>();
		panel = new JPanel();
		
		frame.add(panel, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		
		values = new HashMap<Integer, ArrayList<ArrayList<Integer>>>();
	}
	
	/**
	 * Adds the data.
	 *
	 * @param index the sensor index
	 * @param value the brightness value
	 * @param time the current time
	 */
	protected void addData(int index, int value, long time) {
		ArrayList<Integer> data = new ArrayList<Integer>();
		data.add((int) time);
		data.add(value);
		values.get(index).add(data);
	}
	
	/**
	 * Draws a new graph.
	 */
	protected void drawGraph() {
		final Graph graph = new Graph("Graphique", values);
	    graph.pack();
	    graph.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    RefineryUtilities.centerFrameOnScreen(graph);
	    graph.setVisible(true);
	}
	
	/**
	 * Gets the frame.
	 *
	 * @return the frame
	 */
	protected JFrame getFrame() {
		return this.frame;
	}
}
