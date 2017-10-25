package main.embl.rieslab.htSMLM.controller.uiwizard;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.JComponent;


/**
 * Simple monochromatic rectangle component. Adapted from java2s.com.  
 */
class IconAdapter extends JComponent {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5107995774783472214L;

	public IconAdapter(Icon icon) {
		this.icon = icon;
	}

	public void paintComponent(Graphics g) {
		icon.paintIcon(this, g, 10, 3);
	}

	public Dimension getPreferredSize() {
		return new Dimension(icon.getIconWidth(), icon.getIconHeight());
	}

	private Icon icon;
}