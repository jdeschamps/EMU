package main.embl.rieslab.htSMLM.controller.uiwizard;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.HashMap;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import main.embl.rieslab.htSMLM.controller.Configuration;
import main.embl.rieslab.htSMLM.micromanager.properties.MMProperties;
import main.embl.rieslab.htSMLM.ui.uiparameters.UIParameter;
import main.embl.rieslab.htSMLM.ui.uiproperties.UIProperty;


// This class has been adapted from a java document example
public class WizardComboTable extends JPanel {

    /**
	 * 
	 */
	private static final long serialVersionUID = 4663110661135902728L;
	
	private JComboBox devices;
	private JTable table;
		
	private HashMap<String, UIProperty> uipropertySet_;
	@SuppressWarnings("rawtypes")
	private HashMap<String, UIParameter> uiparameterSet_;
	private MMProperties mmproperties_;
	private String[] uipropkeys_;
	
	@SuppressWarnings("rawtypes")
	public WizardComboTable(HashMap<String, UIProperty> uipropertySet,
			HashMap<String, UIParameter> uiparameterSet, MMProperties mmproperties) {
		
		uipropertySet_ = uipropertySet; 
		uiparameterSet_ = uiparameterSet;
		mmproperties_ = mmproperties;
		
		// Combobox holding the device names
		devices = new JComboBox();
		String[] strarray = StringSorting.sort(mmproperties_.getDevicesList());
		devices.addItem(Configuration.KEY_UNALLOCATED);
		for(int k=0; k<strarray.length;k++){
			devices.addItem(strarray[k]);
		}
		        		
		// Extract uipropertie names
		uipropkeys_ = new String[uipropertySet_.size()];
		String[] temp = new String[uipropertySet_.size()]; 
		uipropkeys_ = StringSorting.sort(uipropertySet_.keySet().toArray(temp));
		
		// Define table
		DefaultTableModel model = new DefaultTableModel(new Object[] {
				"UI property", "Device", "Property" }, 0);
		
		for(int i=0;i<uipropkeys_.length;i++){
			model.addRow(new Object[] {uipropkeys_[i], Configuration.KEY_UNALLOCATED, Configuration.KEY_UNALLOCATED});
		}

		table = new JTable(model) {
			/**
			 * 
			 */
			private static final long serialVersionUID = -7528102943663023952L;

			@Override
			public TableCellRenderer getCellRenderer(int row, int column) {
				switch(column){
				case 0:
					return new BoldTableCellRenderer();
				case 1:
					return new DefaultTableCellRenderer();
				case 2:
					return new DefaultTableCellRenderer();
				default:
					return super.getCellRenderer(row, column);
				}
			}

			@Override
			public TableCellEditor getCellEditor(int row, int column) {
				switch(column){
				case 0:
					return super.getCellEditor(row, column);
				case 1:
					return new DefaultCellEditor(devices);
				case 2:
					return new DefaultCellEditor(getDeviceProperties((String) getValueAt(row, 1)));
				default:
					return super.getCellEditor(row, column);
				}
			}
			
			@Override
	        public boolean isCellEditable(int row, int col) { // only second column is editable
	            if (col < 1) {
	                return false;
	            } else {
	                return true;
	            }
	        }
		};
		table.setAutoCreateRowSorter(false);
		table.setRowHeight(23); 
		table.getColumnModel().getColumn(0).setMaxWidth(160);
		table.getColumnModel().getColumn(1).setMaxWidth(120);
		table.getColumnModel().getColumn(2).setMaxWidth(160);
		
		JScrollPane sc = new JScrollPane(table);
		sc.setPreferredSize(new Dimension(440,590));
		this.add(sc);
	}
	
	
	private JComboBox getDeviceProperties(String device) {
		JComboBox cb = new JComboBox();
		cb.addItem(Configuration.KEY_UNALLOCATED);

		if (!device.equals(Configuration.KEY_UNALLOCATED)) {
			String[] props = mmproperties_.getDevice(device).getPropertiesHash();
			for (int i = 0; i < props.length; i++) {
				cb.addItem(props[i]);
			}
		}
		return cb;

	}

	/**
	 * Renders cell text with a bold font. Adapted from: https://stackoverflow.com/questions/22325138/cellrenderer-making-text-bold
	 */
	class BoldTableCellRenderer extends DefaultTableCellRenderer {

		/**
		 * 
		 */
		private static final long serialVersionUID = 7284712630858433079L;

		public Component getTableCellRendererComponent(JTable table,
                Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {
             Component compo = super.getTableCellRendererComponent(table, 
                   value, isSelected, hasFocus, row, column);
             if (column == 0) {
            	 compo.setFont(compo.getFont().deriveFont(Font.BOLD));
            } else {  
            	compo.setFont(compo.getFont().deriveFont(Font.PLAIN));
            }

             return compo;
          }
	}
	
}