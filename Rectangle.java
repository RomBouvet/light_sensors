import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * The Class Rectangle.
 */
public class Rectangle extends JPanel {
	
	/** The color. */
	private Color myColor;
	
	/**
	 * Paints the component.
	 *
	 * @param g the graphic component
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(myColor);
		g.fillRect(-5, -5, 20, 20);
	}

	/**
	 * Sets the color.
	 *
	 * @param c the new color
	 */
	public void setColor(Color c) {
		myColor = c;
	}
}