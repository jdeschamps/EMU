package main.embl.rieslab.htSMLM.configuration.ui;

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

import main.embl.rieslab.htSMLM.configuration.Configuration;
import main.embl.rieslab.htSMLM.micromanager.properties.MMProperties;
import main.embl.rieslab.htSMLM.ui.uiproperties.SingleStateUIProperty;
import main.embl.rieslab.htSMLM.ui.uiproperties.MultiStateUIProperty;
import main.embl.rieslab.htSMLM.ui.uiproperties.TwoStateUIProperty;
import main.embl.rieslab.htSMLM.ui.uiproperties.UIProperty;
import main.embl.rieslab.htSMLM.util.StringSorting;


public class PropertiesTable extends JPanel {

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

	/**
	 * Constructor called when no configuration exists. Creates a table with non-allocated fields/
	 * 
	 * @param uipropertySet Map of the UI properties of the user interface.
	 * @param mmproperties Object containing the device properties from the current Micro-manager session. 
	 * @param help Help window.
	 */
	public PropertiesTable(HashMap<String, UIProperty> uipropertySet, MMProperties mmproperties, HelpWindow help) {

		uipropertySet_ = uipropertySet;
		mmproperties_ = mmproperties;
		help_ = help;


		// Combobox holding the devices name sorted alphabetically
		devices = new JComboBox();
		String[] strarray = StringSorting.sort(mmproperties_.getDevicesList());
		devices.addItem(Configuration.KEY_UNALLOCATED);
		for (int k = 0; k < strarray.length; k++) {
			devices.addItem(strarray[k]);
		}

		// Extracts uiproperties name from the UI map and sort them alphabetically
		String[] temp = new String[uipropertySet_.size()];
		uipropkeys_ = StringSorting.sort(uipropertySet_.keySet().toArray(temp));

		// Defines table model
		DefaultTableModel model = getDefaultModel();

		// For each property of the UI
		for (int i = 0; i < uipropkeys_.length; i++) {
			// adds a row corresponding to the allocation of the UI property to a MM device property
			model.addRow(new Object[] { uipropkeys_[i], Configuration.KEY_UNALLOCATED, Configuration.KEY_UNALLOCATED });
			
			if (uipropertySet.get(uipropkeys_[i]).isTwoState()) {
				// if property is a toggle property, adds a line for the on and off values respectively.
				model.addRow(new Object[] {uipropkeys_[i] + TwoStateUIProperty.getOnStateName(),"", Configuration.KEY_ENTERVALUE });
				model.addRow(new Object[] {uipropkeys_[i] + TwoStateUIProperty.getOffStateName(),"", Configuration.KEY_ENTERVALUE });
				
			} else if (uipropertySet.get(uipropkeys_[i]).isSingleState()) {
				// if property is a single value property, adds a line for the value the property must take
				model.addRow(new Object[] {uipropkeys_[i] + SingleStateUIProperty.getValueName(),"", Configuration.KEY_ENTERVALUE });
				
			} else if (uipropertySet.get(uipropkeys_[i]).isMultiState()) {
				// if multiple values property, adds a line for each of the value to be allocated
				int numpos = ((MultiStateUIProperty) uipropertySet.get(uipropkeys_[i])).getNumberOfStates();
				for(int j=0;j<numpos;j++){
					model.addRow(new Object[] {uipropkeys_[i] + MultiStateUIProperty.getStateName(j),"", Configuration.KEY_ENTERVALUE });
				}
			} 
		}

		// creates JTable
		createTable(model);

		JScrollPane sc = new JScrollPane(table);

		this.add(sc);
	}

	/**
	 * Constructor called when modifying an existing configuration. Produces a table filled with preset fields. If the fields are not found, then creates non-allocated ones.
	 * 
	 * @param uipropertySet UI properties map of the current UI.
	 * @param mmproperties MM properties found in the current Micro-manager configuration.
	 * @param propertymapping Mapping of UI and MM properties as read from a configuration.
	 * @param help Help window.
	 */
	public PropertiesTable(HashMap<String, UIProperty> uipropertySet, MMProperties mmproperties, HashMap<String, String> propertymapping, HelpWindow help) {

		uipropertySet_ = uipropertySet;
		mmproperties_ = mmproperties;
		help_ = help;

		// Combobox holding the devices name sorted alphabetically
		devices = new JComboBox();
		String[] strarray = StringSorting.sort(mmproperties_.getDevicesList());
		devices.addItem(Configuration.KEY_UNALLOCATED);
		for (int k = 0; k < strarray.length; k++) {
			devices.addItem(strarray[k]);
		}

		// Extracts uiproperties name from the UI map and sort them alphabetically
		String[] temp = new String[uipropertySet_.size()];
		uipropkeys_ = StringSorting.sort(uipropertySet_.keySet().toArray(temp));

		// Defines table model
		DefaultTableModel model = getDefaultModel();

		// Defines string for extraction of the values set for each property in the configuration
		String mmprop, uion, uioff, uisingle, uitemp;
		
		// For each property of the UI
		for (int i = 0; i < uipropkeys_.length; i++) {
			// gets the MM property corresponding to the UI property in the configuration
			mmprop = propertymapping.get(uipropkeys_[i]);
		
			// if the configuration indeed contains the property and is mapped in the configuration with an existing MM property
			if (propertymapping.containsKey(uipropkeys_[i]) && mmproperties.isProperty(mmprop)) { 
				// adds the UI property, its corresponding device and device property
				model.addRow(new Object[] { uipropkeys_[i], mmproperties.getProperty(mmprop).getDeviceName(), mmprop });
				
				// if the property is an instance of SingleState, TwoState or MultiState property, then looks for the assigned values
				if (uipropertySet.get(uipropkeys_[i]).isTwoState()) { // if two state
					// gets values corresponding to the on and off states
					uion = propertymapping.get(uipropkeys_[i]+ TwoStateUIProperty.getOnStateName());
					uioff = propertymapping.get(uipropkeys_[i]+ TwoStateUIProperty.getOffStateName());
					
					// if null then sets to default
					if(uion == null){
						uion = Configuration.KEY_ENTERVALUE;
					}
					if(uioff == null){
						uioff = Configuration.KEY_ENTERVALUE;
					}
					
					// adds a row for each with the preset state value
					model.addRow(new Object[] {uipropkeys_[i] + TwoStateUIProperty.getOnStateName(),"", uion });
					model.addRow(new Object[] {uipropkeys_[i]+ TwoStateUIProperty.getOffStateName(), "", uioff });
				} else if (uipropertySet.get(uipropkeys_[i]).isSingleState()) { // if single value property
					// gets the value of the state and adds the corresponding row
					uisingle = propertymapping.get(uipropkeys_[i]+ SingleStateUIProperty.getValueName());
					if(uisingle == null){
						uion = Configuration.KEY_ENTERVALUE;
					}
					model.addRow(new Object[] {	uipropkeys_[i] + SingleStateUIProperty.getValueName(), "", uisingle });
				} else if (uipropertySet.get(uipropkeys_[i]).isMultiState()) { // if multiple values property
					// gets the number of positions and extracts all position values. If null then sets to default. Finally creates the corresponding row.
					int numpos = ((MultiStateUIProperty) uipropertySet.get(uipropkeys_[i])).getNumberOfStates();
					for(int j=0;j<numpos;j++){
						uitemp = propertymapping.get(uipropkeys_[i]+ MultiStateUIProperty.getStateName(j));

						if(uitemp == null){
							uitemp = Configuration.KEY_ENTERVALUE;
						}
						
						model.addRow(new Object[] {	uipropkeys_[i] +  MultiStateUIProperty.getStateName(j), "", uitemp });
					}
				} 
			} else {
				// if it is not found in the configuration
				model.addRow(new Object[] {uipropkeys_[i],Configuration.KEY_UNALLOCATED,Configuration.KEY_UNALLOCATED });
				
				// if the property is an instance of SingleState, TwoState or MultiState property, creates rows for the states value
				if (uipropertySet.get(uipropkeys_[i]).isTwoState()) {
					model.addRow(new Object[] {uipropkeys_[i] + TwoStateUIProperty.getOnStateName(),"", Configuration.KEY_ENTERVALUE });
					model.addRow(new Object[] {uipropkeys_[i]+ TwoStateUIProperty.getOffStateName(), "",Configuration.KEY_ENTERVALUE });
				} else if (uipropertySet.get(uipropkeys_[i]).isSingleState()) {
					model.addRow(new Object[] {uipropkeys_[i]+ SingleStateUIProperty.getValueName(), "",Configuration.KEY_ENTERVALUE });
				} else if (uipropertySet.get(uipropkeys_[i]).isMultiState()) { // if multiple values property
					int numpos = ((MultiStateUIProperty) uipropertySet.get(uipropkeys_[i])).getNumberOfStates();
					for(int j=0;j<numpos;j++){
						model.addRow(new Object[] {uipropkeys_[i]+ MultiStateUIProperty.getStateName(j), "",Configuration.KEY_ENTERVALUE });
					}
				} 
			}
		}

		// Creates table
		createTable(model);

		JScrollPane sc = new JScrollPane(table);
		this.add(sc);
	}
	
	private DefaultTableModel getDefaultModel(){
		return new DefaultTableModel(new Object[] {"UI property", "Device", "Property" }, 0);
	}
	
	private void createTable(DefaultTableModel model){
		// Creates table
		table = new JTable(model) {
			private static final long serialVersionUID = -7528102943663023952L;

			@Override
			public TableCellRenderer getCellRenderer(int row, int column) {
				switch (column) {
				case 0:
					return new BoldTableCellRenderer(); // first column is written in bold font
				case 1:
					return new DefaultTableCellRenderer(); // column 1 takes a default renderer 
				case 2:
					return new DefaultTableCellRenderer(); // column 2 takes a default renderer
				default:
					return super.getCellRenderer(row, column);
				}
			}

			@Override
			public TableCellEditor getCellEditor(int row, int column) {
				String s = (String) table.getValueAt(row, 0);
				if (column == 2 && isStateValue(s)) { 
					// if in the last column and corresponds to a field value, returns a textfield cell editor
					return new DefaultCellEditor(new JTextField(Configuration.KEY_ENTERVALUE));
				} else {
					// if not a field value or not in third column
					switch (column) {
					case 0:	// in the first column return default cell editor
						return super.getCellEditor(row, column);
					case 1: // in the second column return a JComboBox cell editor with the devices name
						return new DefaultCellEditor(devices);
					case 2: // in the last column return a Jcombobox cell editor with the properties name corresponding to the selected device
						return new DefaultCellEditor(getDevicePropertiesComboBox((String) getValueAt(row, 1)));
					default:
						return super.getCellEditor(row, column);
					}
				}
			}

			@Override
			public boolean isCellEditable(int row, int col) { // first column is non-editable and second as well if it is a field value row
				String s = (String) table.getValueAt(row, 0);
				if (col < 1) {
					return false;
				} else if (col == 1 && isStateValue(s)) {
					return false;
				} else {
					return true;
				}
			}
			
			/**
			 * Tests if the string has been generated by a SingelStateUIProperty, a TwoStateUIProperty or a MultiStateUIProperty.
			 * 
			 * @param s String to test, value in the first column of the table
			 * @return True if corresponds to a field value.
			 */
			public boolean isStateValue(String s){
				if (s.contains(SingleStateUIProperty.getValueName())
						|| s.contains(TwoStateUIProperty.getOnStateName()) 
						|| s.contains(TwoStateUIProperty.getOffStateName())
						|| s.matches(".*"+MultiStateUIProperty.getGenericStateName()+".*")){
					return true;
				}
				return false;
			}
		};
		
		table.setAutoCreateRowSorter(false);

		table.setRowHeight(23);

		// adds mouse listener to update the helper window
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
	}
	
	/**
	 * Creates a JComboBox containing all the Micro-manager device properties corresponding to the device.
	 * 
	 * @param device Device to extract the properties from.
	 * @return JCombobx with all the corresponding properties.
	 */
	private JComboBox getDevicePropertiesComboBox(String device) {
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
	 * Displays or hides the help window.
	 * 
	 * @param b True if the help window is to be displayed.
	 */
	public void showHelp(boolean b){
		help_.showHelp(b);
		updateHelper(table.getSelectedRow());
	}

	/**
	 * Updates the helper window text with the description of the UI property represented by the selected row in the table.
	 * 
	 * @param row Selected row.
	 */
	private void updateHelper(int row){
		String s = (String) table.getValueAt(row, 0);
	
		if(s.matches(MultiStateUIProperty.getGenericStateName())){
			int nmb = 0;
			for(int i=row;i>=1;i--){
				String str = (String) table.getValueAt(i-1, 0);
				if(!str.matches(MultiStateUIProperty.getGenericStateName())){
					nmb = i;
				}
			}
			s = (String) table.getValueAt(nmb, 0);
			help_.update("Enter the value for this specific state.\n\n"+s+":\n"+uipropertySet_.get(s).getDescription());
		} else if (s.contains(TwoStateUIProperty.getOnStateName())){
			s = (String) table.getValueAt(row-1, 0);
			help_.update("Enter the value sent to the device when set to ON state.\n\n"+s+":\n"+uipropertySet_.get(s).getDescription());
		} else if (s.contains(TwoStateUIProperty.getOffStateName())){
			s = (String) table.getValueAt(row-2, 0);
			help_.update("Enter the value sent to the device when set to OFF state.\n\n"+s+":\n"+uipropertySet_.get(s).getDescription());
		} else if (s.contains(SingleStateUIProperty.getValueName())){
			s = (String) table.getValueAt(row-1, 0);
			help_.update("Enter the constant value sent to the device.\n\n"+s+":\n"+uipropertySet_.get(s).getDescription());
		} else if (s.contains(SingleStateUIProperty.getValueName())){
			s = (String) table.getValueAt(row-1, 0);
			help_.update("Enter the constant value sent to the device.\n\n"+s+":\n"+uipropertySet_.get(s).getDescription());
		} else if(uipropertySet_.containsKey(s)){
			help_.update(s+":\n\n"+uipropertySet_.get(s).getDescription());
		}
	}
	
	/**
	 * Extracts the fields filled by the user. 
	 * 
	 * @return HashMap of the UI properties and the corresponding MM properties and states value.
	 */
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