package main.embl.rieslab.htSMLM.controller.uiwizard;

import java.awt.Component;
import java.util.Map;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

/**
 * Renders icon within a list. Adapted from http://helpdesk.objects.com.au. The text is not displayed, 
 * and the icons are set to ColorIcon.
 */
public class IconListRenderer extends DefaultListCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4250151427157236056L;
	private Map<Object, ColorIcon> icons = null;

	public IconListRenderer(Map<Object, ColorIcon> icons) {
		this.icons = icons;
	}

	@Override
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {

		// Get the renderer component from parent class

		JLabel label = (JLabel) super.getListCellRendererComponent(list,
				"", index, isSelected, cellHasFocus);

		// Get icon to use for the list item value

		ColorIcon icon = icons.get(value);

		// Set icon to display for value

		label.setIcon(icon);
		return label;
	}
}