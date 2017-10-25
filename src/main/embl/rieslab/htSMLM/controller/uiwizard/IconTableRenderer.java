package main.embl.rieslab.htSMLM.controller.uiwizard;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import main.embl.rieslab.htSMLM.util.ColorRepository;

public class IconTableRenderer implements TableCellRenderer{

	@Override
	public Component getTableCellRendererComponent(JTable table,
			Object value, boolean isSelected, boolean hasFocus, int row,
			int column) {
		return new IconAdapter(new ColorIcon(ColorRepository.getColor((String) value)));
	}
	
}