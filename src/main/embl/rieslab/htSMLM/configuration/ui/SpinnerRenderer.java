package main.embl.rieslab.htSMLM.configuration.ui;

import java.awt.Component;

import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerModel;
import javax.swing.table.TableCellRenderer;

/**
 * Spinner cell renderer
 */
public class SpinnerRenderer extends JSpinner implements TableCellRenderer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4236114754335086999L;

	public SpinnerRenderer() {
		setOpaque(true);
	}

	public Component getTableCellRendererComponent(JTable table,
			Object value, boolean isSelected, boolean hasFocus, int row,
			int column) {
		setModel((SpinnerModel) value);

		return this;
	}
}