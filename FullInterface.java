import java.awt.Dimension;

import java.util.ArrayList;

import javax.swing.border.TitledBorder;
import javax.swing.BoxLayout;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * The Class FullInterface.
 */
public class FullInterface extends GraphicInterface {
	
	/** The height of the frame. */
	private final int height = 400;
	
	/** The width of the frame. */
	private final int width = 150;
	
	/** The panes within the frame. */
	private ArrayList<JScrollPane> panes;
	
	/**
	 * Instantiates a new full interface.
	 */
	public FullInterface() {
		super();
		
		this.panes = new ArrayList<JScrollPane>();
		this.panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

		this.frame.setSize(width, height);
		this.frame.pack();
	}

	/**
	 * Adds a new sensor.
	 *
	 * @param n the sensor index
	 */
	public void addSensor(int n) {
		JTextArea text = new JTextArea();
		text.setEditable(false);
		this.textAreas.add(text);
		
		JScrollPane newPane = new JScrollPane(text);
		newPane.setPreferredSize(new Dimension(width, height));
		newPane.setBorder(new TitledBorder("Capteur " + n));
		
		this.panes.add(newPane);
		this.panel.add(newPane);

		this.frame.pack();
		
		ArrayList<ArrayList<Integer>> array = new ArrayList<ArrayList<Integer>>();
		this.values.put(n, array);
	}	

	/**
	 * Adds text.
	 *
	 * @param index the sensor index
	 * @param value the brightness value
	 * @param time the current time
	 */
	public void addText(int index, int value, long time) {
		this.textAreas.get(index).append(time + "s : " + value + " lumen\n");
	}
}
