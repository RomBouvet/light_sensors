import java.awt.Color;
import java.awt.Dimension;

import java.util.ArrayList;

import javax.swing.border.TitledBorder;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * The Class MiniInterface.
 */
public class MiniInterface extends GraphicInterface {
	
	/** The height of the frame. */
	private final int height = 60;
	
	/** The width of the frame. */
	private final int width = 90;
	
	/** The panes within the frame. */
	private ArrayList<JPanel> panes;
	
	/** The rectangles within the frame. */
	private ArrayList<Rectangle> rectangles;
	
	/**
	 * Instantiates a new mini interface.
	 */
	public MiniInterface() {
		super();
		this.panes = new ArrayList<JPanel>();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		this.rectangles = new ArrayList<Rectangle>();
		
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
		
		JPanel newPane = new JPanel();
		newPane.setPreferredSize(new Dimension(width, height));
		newPane.setBorder(new TitledBorder("Capteur " + n));
		
		Rectangle rect = new Rectangle();
		this.rectangles.add(rect);
		newPane.add(rect);
		newPane.add(text);
		
		this.panes.add(newPane);
		this.panel.add(newPane);

		this.frame.pack();
		
		ArrayList<ArrayList<Integer>> array = new ArrayList<ArrayList<Integer>>();
		this.values.put(n, array);
	}

	/**
	 * Sets the text.
	 *
	 * @param index the sensor index
	 * @param value the brightness value
	 */
	public void setText(int index, int value) {
		this.textAreas.get(index).setText("" + value);
		if(value >= 0 & value < 20) {
			this.rectangles.get(index).setColor(Color.BLACK);
		} else if(value >= 20 & value < 40) {
			this.rectangles.get(index).setColor(Color.DARK_GRAY);
		} else if(value >= 40 & value < 60) {
			this.rectangles.get(index).setColor(Color.GRAY);
		} else if(value >= 60 & value < 80) {
			this.rectangles.get(index).setColor(Color.LIGHT_GRAY);
		} else {
			this.rectangles.get(index).setColor(Color.WHITE);
		}
	}
}
