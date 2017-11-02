package main.embl.rieslab.htSMLM.controller.uiwizard;

import java.awt.Component;
import java.awt.Font;
import java.util.HashMap;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import main.embl.rieslab.htSMLM.controller.Configuration;
import main.embl.rieslab.htSMLM.micromanager.properties.MMProperties;
import main.embl.rieslab.htSMLM.ui.uiproperties.SingleValueUIProperty;
import main.embl.rieslab.htSMLM.ui.uiproperties.ToggleUIProperty;
import main.embl.rieslab.htSMLM.ui.uiproperties.UIProperty;
import main.embl.rieslab.htSMLM.util.StringSorting;


public class PropertyComboTable extends JPanel {

    /**
	 * 
	 */
	private static final long serialVersionUID = 4663110661135902728L;
	
	private JComboBox devices;
	private JTable table;
		
	private HashMap<String, UIProperty> uipropertySet_;
	private MMProperties mmproperties_;
	private String[] uipropkeys_;
	private HelpWindow help_;

	public PropertyComboTable(HashMap<String, UIProperty> uipropertySet,
			MMProperties mmproperties, HelpWindow help) {

		uipropertySet_ = uipropertySet;
		mmproperties_ = mmproperties;

		// Combobox holding the device names
		devices = new JComboBox();
		String[] strarray = StringSorting.sort(mmproperties_.getDevicesList());
		devices.addItem(Configuration.KEY_UNALLOCATED);
		for (int k = 0; k < strarray.length; k++) {
			devices.addItem(strarray[k]);
		}

		// Extract uiproperty names
		uipropkeys_ = new String[uipropertySet_.size()];
		String[] temp = new String[uipropertySet_.size()];
		uipropkeys_ = StringSorting.sort(uipropertySet_.keySet().toArray(temp));

		// Define table
		DefaultTableModel model = new DefaultTableModel(new Object[] {
				"UI property", "Device", "Property" }, 0);

		for (int i = 0; i < uipropkeys_.length; i++) {
			if (uipropertySet.get(uipropkeys_[i]).isToggle()) {
				model.addRow(new Object[] { uipropkeys_[i],
						Configuration.KEY_UNALLOCATED,
						Configuration.KEY_UNALLOCATED });
				model.addRow(new Object[] {
						uipropkeys_[i] + ToggleUIProperty.getToggleOnName(),
						"", Configuration.KEY_ENTERVALUE });
				model.addRow(new Object[] {
						uipropkeys_[i] + ToggleUIProperty.getToggleOffName(),
						"", Configuration.KEY_ENTERVALUE });
			} else if (uipropertySet.get(uipropkeys_[i]).isSingleValue()) {
				model.addRow(new Object[] { uipropkeys_[i],
						Configuration.KEY_UNALLOCATED,
						Configuration.KEY_UNALLOCATED });
				model.addRow(new Object[] {
						uipropkeys_[i] + SingleValueUIProperty.getValueName(),
						"", Configuration.KEY_ENTERVALUE });
			} else {
				model.addRow(new Object[] { uipropkeys_[i],
						Configuration.KEY_UNALLOCATED,
						Configuration.KEY_UNALLOCATED });
			}
		}

		table = new JTable(model) {
			/**
		 * 
		 */
			private static final long serialVersionUID = -7528102943663023952L;

			@Override
			public TableCellRenderer getCellRenderer(int row, int column) {
				switch (column) {
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
				String s = (String) table.getValueAt(row, 0);
				if ((s.contains(SingleValueUIProperty.getValueName())
						|| s.contains(ToggleUIProperty.getToggleOnName()) || s
							.contains(ToggleUIProperty.getToggleOffName()))
						&& column > 1) {
					return new DefaultCellEditor(new JTextField(
							Configuration.KEY_ENTERVALUE));
				} else {
					switch (column) {
					case 0:
						return super.getCellEditor(row, column);
					case 1:
						return new DefaultCellEditor(devices);
					case 2:
						return new DefaultCellEditor(
								getDeviceProperties((String) getValueAt(row, 1)));
					default:
						return super.getCellEditor(row, column);
					}
				}
			}

			@Override
			public boolean isCellEditable(int row, int col) { // only second
																// column is
																// editable
				String s = (String) table.getValueAt(row, 0);
				if (col < 1) {
					return false;
				} else if ((s.contains(SingleValueUIProperty.getValueName())
						|| s.contains(ToggleUIProperty.getToggleOnName()) || s
							.contains(ToggleUIProperty.getToggleOffName()))
						&& col == 1) {
					return false;
				} else {
					return true;
				}
			}
		};
		table.setAutoCreateRowSorter(false);
		table.setRowHeight(23);
		// table.getColumnModel().getColumn(0).setMaxWidth(160);
		// table.getColumnModel().getColumn(1).setMaxWidth(120);
		// table.getColumnModel().getColumn(2).setMaxWidth(160);

		table.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				int row = table.rowAtPoint(evt.getPoint());
				int col = table.columnAtPoint(evt.getPoint());
				if (col == 0) {
					updateHelper(row);
				}
			}
		});

		help_ = help;

		JScrollPane sc = new JScrollPane(table);
		// sc.setPreferredSize(new Dimension(440,590));
		this.add(sc);
	}

	public PropertyComboTable(HashMap<String, UIProperty> uipropertySet, MMProperties mmproperties, HashMap<String, String> propertymapping, HelpWindow help) {

		uipropertySet_ = uipropertySet;
		mmproperties_ = mmproperties;

		// Combobox holding the device names
		devices = new JComboBox();
		String[] strarray = StringSorting.sort(mmproperties_.getDevicesList());
		devices.addItem(Configuration.KEY_UNALLOCATED);
		for (int k = 0; k < strarray.length; k++) {
			devices.addItem(strarray[k]);
		}

		// Extract uiproperty names
		uipropkeys_ = new String[uipropertySet_.size()];
		String[] temp = new String[uipropertySet_.size()];
		uipropkeys_ = StringSorting.sort(uipropertySet_.keySet().toArray(temp));

		// Define table
		DefaultTableModel model = new DefaultTableModel(new Object[] {
				"UI property", "Device", "Property" }, 0);

		String mmprop, uion, uioff, uisingle;
		for (int i = 0; i < uipropkeys_.length; i++) {
			mmprop = propertymapping.get(uipropkeys_[i]);
		
			if (propertymapping.containsKey(uipropkeys_[i]) && mmproperties.isProperty(mmprop)) { // if exists in the current configuration
				model.addRow(new Object[] { uipropkeys_[i], mmproperties.getProperty(mmprop).getDeviceName(), mmprop });
				if (uipropertySet.get(uipropkeys_[i]).isToggle()) { // if toggle
					uion = propertymapping.get(uipropkeys_[i]+ ToggleUIProperty.getToggleOnName());
					uioff = propertymapping.get(uipropkeys_[i]+ ToggleUIProperty.getToggleOffName());
					model.addRow(new Object[] {uipropkeys_[i] + ToggleUIProperty.getToggleOnName(),"", uion });
					model.addRow(new Object[] {uipropkeys_[i]+ ToggleUIProperty.getToggleOffName(), "",	uioff });
				} else if (uipropertySet.get(uipropkeys_[i]).isSingleValue()) { // if single value property
					uisingle = propertymapping.get(uipropkeys_[i]+ SingleValueUIProperty.getValueName());
					model.addRow(new Object[] {	uipropkeys_[i] + SingleValueUIProperty.getValueName(), "", uisingle });
				} 
			} else {
				model.addRow(new Object[] {uipropkeys_[i],Configuration.KEY_UNALLOCATED,Configuration.KEY_UNALLOCATED });
				if (uipropertySet.get(uipropkeys_[i]).isToggle()) {
					model.addRow(new Object[] {uipropkeys_[i] + ToggleUIProperty.getToggleOnName(),"", Configuration.KEY_ENTERVALUE });
					model.addRow(new Object[] {uipropkeys_[i]+ ToggleUIProperty.getToggleOffName(), "",Configuration.KEY_ENTERVALUE });
				} else if (uipropertySet.get(uipropkeys_[i]).isSingleValue()) {
					model.addRow(new Object[] {uipropkeys_[i]+ SingleValueUIProperty.getValueName(), "",Configuration.KEY_ENTERVALUE });
				} 
			}
		}

		table = new JTable(model) {
			/**
			 * 
			 */
			private static final long serialVersionUID = -7528102943663023952L;

			@Override
			public TableCellRenderer getCellRenderer(int row, int column) {
				switch (column) {
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
				String s = (String) table.getValueAt(row, 0);
				if ((s.contains(SingleValueUIProperty.getValueName())
						|| s.contains(ToggleUIProperty.getToggleOnName()) || s
							.contains(ToggleUIProperty.getToggleOffName()))
						&& column > 1) {
					return new DefaultCellEditor(new JTextField(
							Configuration.KEY_ENTERVALUE));
				} else {
					switch (column) {
					case 0:
						return super.getCellEditor(row, column);
					case 1:
						return new DefaultCellEditor(devices);
					case 2:
						return new DefaultCellEditor(
								getDeviceProperties((String) getValueAt(row, 1)));
					default:
						return super.getCellEditor(row, column);
					}
				}
			}

			@Override
			public boolean isCellEditable(int row, int col) { // only second
																// column is
																// editable
				String s = (String) table.getValueAt(row, 0);
				if (col < 1) {
					return false;
				} else if ((s.contains(SingleValueUIProperty.getValueName())
						|| s.contains(ToggleUIProperty.getToggleOnName()) || s
							.contains(ToggleUIProperty.getToggleOffName()))
						&& col == 1) {
					return false;
				} else {
					return true;
				}
			}
		};
		table.setAutoCreateRowSorter(false);
		table.setRowHeight(23);
		// table.getColumnModel().getColumn(0).setMaxWidth(160);
		// table.getColumnModel().getColumn(1).setMaxWidth(120);
		// table.getColumnModel().getColumn(2).setMaxWidth(160);

		table.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				int row = table.rowAtPoint(evt.getPoint());
				int col = table.columnAtPoint(evt.getPoint());
				if (col == 0) {
					updateHelper(row);
				}
			}
		});

		help_ = help;

		JScrollPane sc = new JScrollPane(table);
		// sc.setPreferredSize(new Dimension(440,590));
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
	
	public void showHelp(boolean b){
		help_.showHelp(b);
		updateHelper(table.getSelectedRow());
	}

	private void updateHelper(int row){
		String s = (String) table.getValueAt(row, 0);
	
		if (s.contains(ToggleUIProperty.getToggleOnName())){
			s = (String) table.getValueAt(row-1, 0);
			help_.update("Enter the value sent to the device when set to ON state.\n\n"+s+":\n\n"+uipropertySet_.get(s).getDescription());
		}else if (s.contains(ToggleUIProperty.getToggleOffName())){
			s = (String) table.getValueAt(row-2, 0);
			help_.update("Enter the value sent to the device when set to OFF state.\n\n"+s+":\n"+uipropertySet_.get(s).getDescription());
		} else if (s.contains(SingleValueUIProperty.getValueName())){
			s = (String) table.getValueAt(row-1, 0);
			help_.update("Enter the constant value sent to the device.\n\n"+s+":\n"+uipropertySet_.get(s).getDescription());
		} else if(uipropertySet_.containsKey(s)){
			help_.update(s+":\n\n"+uipropertySet_.get(s).getDescription());
		}
	}
	
	public void disposeHelp(){
		help_.disposeHelp();
	}
	
	public HashMap<String,String> getSettings(){
		HashMap<String,String> settings = new HashMap<String,String>();
		
		TableModel model = table.getModel();
		int nrow = model.getRowCount();
		
		for(int i=0;i<nrow;i++){
			settings.put((String) model.getValueAt(i, 0), (String) model.getValueAt(i, 2));
		}
		
		return settings;
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