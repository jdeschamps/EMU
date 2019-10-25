package de.embl.rieslab.emu.configuration.ui.tables;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import de.embl.rieslab.emu.configuration.data.GlobalConfiguration;

public class ConfigurationTable extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1454570033895628485L;
	private JTable table;
	private String currentConfiguration;
	
	public ConfigurationTable(GlobalConfiguration conf) {
		
		currentConfiguration = conf.getCurrentConfigurationName();
		
		// Defines table
		DefaultTableModel model = new DefaultTableModel(new Object[] {"Configuration", "Plugin" }, 0);
		
		for(int i=0;i<conf.getPluginConfigurations().size();i++){
			model.addRow(new Object[] {conf.getPluginConfigurations().get(i).getConfigurationName(), conf.getPluginConfigurations().get(i).getConfigurationName()});
		}

		createTable(model);

		JScrollPane sc = new JScrollPane(table);
		this.add(sc);
	}

	private void createTable(DefaultTableModel model) {
		table = new JTable(model) {

			@Override
			public TableCellRenderer getCellRenderer(int row, int column) {
				String s = (String) table.getValueAt(row, 0);
				if (s.equals(currentConfiguration)) {
					return new ColorCellRenderer(Color.blue);
				} else {
					return super.getCellRenderer(row, column);
				}
			}

			@Override
			public TableCellEditor getCellEditor(int row, int column) {
				return super.getCellEditor(row, column);
			}
			
			@Override
	        public boolean isCellEditable(int row, int col) { // only first column is editable
	            if (col > 0 ) {
	                return false;
	            } else {
	                return true;
	            }
	        }
		};
	}
	
	private class ColorCellRenderer extends DefaultTableCellRenderer {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 8620307070695657755L;
		private Color color; 
		
		public ColorCellRenderer(Color c) {
			color = c;
		}
		
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			
			Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			c.setBackground(color);
			return c;
		}
	}
}
