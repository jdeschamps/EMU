package main.embl.rieslab.emu.uiexamples.htsmlm.acquisitions.ui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import main.embl.rieslab.emu.ui.uiproperties.FixedStateUIProperty;
import main.embl.rieslab.emu.ui.uiproperties.MultiStateUIProperty;
import main.embl.rieslab.emu.ui.uiproperties.SingleStateUIProperty;
import main.embl.rieslab.emu.ui.uiproperties.TwoStateUIProperty;
import main.embl.rieslab.emu.ui.uiproperties.UIProperty;
import main.embl.rieslab.emu.ui.uiproperties.filters.AllocatedPropertyFilter;
import main.embl.rieslab.emu.ui.uiproperties.filters.AntiFlagPropertyFilter;
import main.embl.rieslab.emu.ui.uiproperties.filters.FlagPropertyFilter;
import main.embl.rieslab.emu.ui.uiproperties.filters.PropertyFilter;
import main.embl.rieslab.emu.ui.uiproperties.filters.ReadOnlyPropertyFilter;
import main.embl.rieslab.emu.ui.uiproperties.filters.TwoStatePropertyFilter;
import main.embl.rieslab.emu.uiexamples.htsmlm.acquisitions.Acquisition;
import main.embl.rieslab.emu.uiexamples.htsmlm.acquisitions.AcquisitionFactory;
import main.embl.rieslab.emu.uiexamples.htsmlm.flags.FilterWheelFlag;
import main.embl.rieslab.emu.uiexamples.htsmlm.flags.FocusLockFlag;
import main.embl.rieslab.emu.uiexamples.htsmlm.flags.FocusStabFlag;
import main.embl.rieslab.emu.uiexamples.htsmlm.flags.LaserFlag;

public class AcquisitionTab extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7966565586677957738L;

	private AcquisitionWizard wizard_;
	private AcquisitionFactory factory_;
	private JPanel acqcard_;
	private JPanel[] acqpanes_, acqpanels_;
	private JComboBox acqtype_;
	private String[] acqtypes_;
	private int currind;
	private HashMap<String, UIProperty> props_;
	private HashMap<String, String> propsfriendlyname_;
	
	public AcquisitionTab(AcquisitionWizard wizard, AcquisitionFactory factory) {
		factory_ = factory;
		wizard_ = wizard;
		
		// Get the array of acquisition types and create a JComboBox
		acqtypes_ = factory_.getAcquisitionTypeList();
		acqtype_ = new JComboBox(acqtypes_);
		acqtype_.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
		acqtype_.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){
	            changeAcquisition((String) acqtype_.getSelectedItem());
	    	}
	    });
		
		// Set the current tab acquisition
		currind = 0;
		this.setName(acqtypes_[currind]);
		
		// Filter out read-only properties from the system properties
		props_ = new AllocatedPropertyFilter(new ReadOnlyPropertyFilter()).filterProperties(wizard_.getController().getPropertiesMap());
		propsfriendlyname_ = new HashMap<String, String>();
		Iterator<String> it = props_.keySet().iterator();
		String s;
		while(it.hasNext()){
			s = it.next();
			propsfriendlyname_.put(props_.get(s).getFriendlyName(), s); // get each property friendly name
		}
		
		// Create acquisition panels
	    acqcard_ = new JPanel(new CardLayout());
		acqpanes_ = new JPanel[acqtypes_.length];
		acqpanels_ = new JPanel[acqtypes_.length];
		for(int i=0;i<acqtypes_.length;i++){
			acqpanels_[i] = factory_.getAcquisition(acqtypes_[i]).getPanel();
			acqpanes_[i] = createPanel(acqpanels_[i],factory_.getAcquisition(acqtypes_[i]).getPropertyFilter());
			acqcard_.add(acqpanes_[i],acqtypes_[i]);			
		}
		
		setUpPanel();
	}


	public AcquisitionTab(AcquisitionWizard wizard, AcquisitionFactory factory, Acquisition acquisition) {
		factory_ = factory;
		wizard_ = wizard;
		
		// Get the array of acquisition types and create a JComboBox
		acqtypes_ = factory_.getAcquisitionTypeList();
		acqtype_ = new JComboBox(acqtypes_);
		acqtype_.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){
	            changeAcquisition((String) acqtype_.getSelectedItem());
	    	}
	    });
		
		// Set current acquisition to the acquisition passed as parameter
		currind = 0;
		for(int i=0;i<acqtypes_.length;i++){
			if(acqtypes_[i].equals(acquisition.getType())){
				currind = i;
				break;
			}
		}
		this.setName(acqtypes_[currind]);
		acqtype_.setSelectedIndex(currind);
		
		// Filter out read-only properties from the system properties
		props_ = new AllocatedPropertyFilter(new ReadOnlyPropertyFilter()).filterProperties(wizard_.getController().getPropertiesMap());
		propsfriendlyname_ = new HashMap<String, String>();
		Iterator<String> it = props_.keySet().iterator();
		String s;
		while(it.hasNext()){
			s = it.next();
			propsfriendlyname_.put(props_.get(s).getFriendlyName(), s);
		}
		
		// Create acquisition panels and set the property values for the current acquisition tab
	    acqcard_ = new JPanel(new CardLayout());
		acqpanes_ = new JPanel[acqtypes_.length];
		acqpanels_ = new JPanel[acqtypes_.length];
		for(int i=0;i<acqtypes_.length;i++){
			if(i==currind){
				acqpanels_[i] = acquisition.getPanel();
				acqpanes_[i] = createPanel(acqpanels_[i],acquisition.getPropertyFilter(),acquisition.getPropertyValues());
				acqcard_.add(acqpanes_[i],acqtypes_[i]);
			} else {
				acqpanels_[i] = factory_.getAcquisition(acqtypes_[i]).getPanel();
				acqpanes_[i] = createPanel(acqpanels_[i],factory_.getAcquisition(acqtypes_[i]).getPropertyFilter());
				acqcard_.add(acqpanes_[i],acqtypes_[i]);	
			}
		}
		
		setUpPanel();
		
	    CardLayout cl = (CardLayout)(acqcard_.getLayout());
	    cl.show(acqcard_, acqtypes_[currind]);
	}

	private JPanel createPanel(JPanel acqpane, PropertyFilter filter) {
		JPanel pane = new JPanel();
		pane.setLayout(new BoxLayout(pane, BoxLayout.PAGE_AXIS));

		pane.add(Box.createVerticalStrut(10));
		
		// acquisition panel
		pane.add(acqpane);	
		pane.add(Box.createVerticalStrut(10));
		
		// get properties
		HashMap<String, UIProperty> props = filter.filterProperties(props_);
		String[] temp;
		PropertyFilter filt;
		
		// focus stabilization
		filt = new FlagPropertyFilter(new FocusStabFlag());
		temp = filt.filterStringProperties(props);
		props = filt.filteredProperties(props);
		
		if(temp.length>0){
		    JPanel focstab = createTable(temp,true);
		    focstab.setBorder(BorderFactory.createTitledBorder(null, "Focus stabilization", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, new Color(0,0,0)));
			((TitledBorder) focstab.getBorder()).setTitleFont(((TitledBorder) focstab.getBorder()).getTitleFont().deriveFont(Font.BOLD, 12));
			pane.add(focstab);
			
			pane.add(Box.createVerticalStrut(10));
		}

		// filterwheel
		filt = new FlagPropertyFilter(new FilterWheelFlag());
		temp = filt.filterStringProperties(props);
		props = filt.filteredProperties(props);
		

		if(temp.length>0){
		    JPanel fw = createTable(temp,false);
		    fw.setBorder(BorderFactory.createTitledBorder(null, "Filter wheel", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, new Color(0,0,0)));
			((TitledBorder) fw.getBorder()).setTitleFont(((TitledBorder) fw.getBorder()).getTitleFont().deriveFont(Font.BOLD, 12));
			pane.add(fw);	
	
			pane.add(Box.createVerticalStrut(10));
		}

		// lasers
		filt = new FlagPropertyFilter(new LaserFlag());
		temp = filt.filterStringProperties(props);
		props = filt.filteredProperties(props);
		
		///////////////////////////////////////////////////////////////// This works on the assumption that all lasers are called "Laser #"
		if(temp.length>0){
			
			// fine laser number of the first file
			int ind = 0;
			for(int i=0;i<temp[0].length()-1;i++){
				if(Character.isDigit(temp[0].charAt(i)) && Character.isDigit(temp[0].charAt(i+1))){
					ind = Integer.valueOf(temp[0].substring(i, i+1));
					break;
				} else if(Character.isDigit(temp[0].charAt(i))){
					ind = Integer.valueOf(temp[0].substring(i, i+1));
					break;
				}
			}
			
			JPanel lasertab = new JPanel();
		    lasertab.setBorder(BorderFactory.createTitledBorder(null, "Lasers", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, new Color(0,0,0)));
		    lasertab.setLayout(new BoxLayout(lasertab, BoxLayout.PAGE_AXIS));
			((TitledBorder) lasertab.getBorder()).setTitleFont(((TitledBorder) lasertab.getBorder()).getTitleFont().deriveFont(Font.BOLD, 12));
		    
			ArrayList<String> templaser = new ArrayList<String>();
			templaser.add(temp[0]);

			for(int j=1; j<temp.length;j++){
				int ind2 = 0;
				
				for(int i=0;i<temp[j].length()-1;i++){
					if(Character.isDigit(temp[j].charAt(i)) && Character.isDigit(temp[j].charAt(i+1))){
						ind2 = Integer.valueOf(temp[j].substring(i, i+1));
						break;
					} else if(Character.isDigit(temp[j].charAt(i))){
						ind2 = Integer.valueOf(temp[j].substring(i, i+1));
						break;
					}
				}

				if(ind2 == ind && j < temp.length-1){
					templaser.add(temp[j]);
				} else if(ind2 == ind && j == temp.length-1){
					// create a jpanel
					templaser.add(temp[j]);
				    JPanel subpanel = createTable(templaser.toArray(new String[0]),false);
				    lasertab.add(subpanel);
				}else if(ind2 != ind && j == temp.length-1){
					// create a jpanel
				    JPanel subpanel = createTable(templaser.toArray(new String[0]),false);
				    lasertab.add(subpanel);
				    
				    templaser = new ArrayList<String>();
					templaser.add(temp[j]);
					
					subpanel = createTable(templaser.toArray(new String[0]),false);
				    lasertab.add(subpanel);
				} else {
					ind = ind2;
					
					// create a jpanel
				    JPanel subpanel = createTable(templaser.toArray(new String[0]),false);
				    lasertab.add(subpanel);
				    
				    templaser = new ArrayList<String>();
					templaser.add(temp[j]);
				}
			}
			
			pane.add(lasertab);
			pane.add(Box.createVerticalStrut(10));
		}

		// Two-state
		filt = new TwoStatePropertyFilter(new AntiFlagPropertyFilter(new FocusLockFlag()));
		temp = filt.filterStringProperties(props);
		props = filt.filteredProperties(props);
		
		if(temp.length>0){
		    JPanel twostate = createTable(temp,false);
		    twostate.setBorder(BorderFactory.createTitledBorder(null, "Two-state", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, new Color(0,0,0)));
			((TitledBorder) twostate.getBorder()).setTitleFont(((TitledBorder) twostate.getBorder()).getTitleFont().deriveFont(Font.BOLD, 12));
			pane.add(twostate);

			pane.add(Box.createVerticalStrut(10));
		}

		// others
		/*filt = new NoPropertyFilter();
		temp = filt.filterStringProperties(props);
		
		if(temp.length>0){
		    JPanel others = createTable(temp,false);
		    others.setBorder(BorderFactory.createTitledBorder(null, "Other", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, new Color(0,0,0)));
			((TitledBorder) others.getBorder()).setTitleFont(((TitledBorder) others.getBorder()).getTitleFont().deriveFont(Font.BOLD, 12));
			pane.add(others);
		}*/
		
		pane.add(new JPanel());
		
		return pane;
	}

	private JPanel createPanel(JPanel acqpane, PropertyFilter filter, HashMap<String, String> propertyValues) {
		JPanel pane = new JPanel();
		pane.setLayout(new BoxLayout(pane, BoxLayout.PAGE_AXIS));

		pane.add(Box.createVerticalStrut(10));
		
		// acquisition panel
		pane.add(acqpane);
		
		pane.add(Box.createVerticalStrut(10));

		
		// get properties
		HashMap<String, UIProperty> props = filter.filterProperties(props_);
		String[] temp;
		PropertyFilter filt;
		
		// focus stabilization
		filt = new FlagPropertyFilter(new FocusStabFlag());
		temp = filt.filterStringProperties(props);
		props = filt.filteredProperties(props);
		
		if(temp.length>0){
		    JPanel focstab = createTable(temp,true,propertyValues);
		    focstab.setBorder(BorderFactory.createTitledBorder(null, "Focus stabilization", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, new Color(0,0,0)));
			((TitledBorder) focstab.getBorder()).setTitleFont(((TitledBorder) focstab.getBorder()).getTitleFont().deriveFont(Font.BOLD, 12));
			pane.add(focstab);
			
			pane.add(Box.createVerticalStrut(10));
		}

		// filterwheel
		filt = new FlagPropertyFilter(new FilterWheelFlag());
		temp = filt.filterStringProperties(props);
		props = filt.filteredProperties(props);
		

		if(temp.length>0){
		    JPanel fw = createTable(temp,false,propertyValues);
		    fw.setBorder(BorderFactory.createTitledBorder(null, "Filter wheel", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, new Color(0,0,0)));
			((TitledBorder) fw.getBorder()).setTitleFont(((TitledBorder) fw.getBorder()).getTitleFont().deriveFont(Font.BOLD, 12));
			pane.add(fw);	
	
			pane.add(Box.createVerticalStrut(10));
		}

		// lasers
		filt = new FlagPropertyFilter(new LaserFlag());
		temp = filt.filterStringProperties(props);		
		props = filt.filteredProperties(props);
		
		///////////////////////////////////////////////////////////////// This works on the assumption that all lasers are called "Laser #"
		if(temp.length>0){
			
			// fine laser number of the first file
			int ind = 0;
			for(int i=0;i<temp[0].length()-1;i++){
				if(Character.isDigit(temp[0].charAt(i)) && Character.isDigit(temp[0].charAt(i+1))){
					ind = Integer.valueOf(temp[0].substring(i, i+1));
					break;
				} else if(Character.isDigit(temp[0].charAt(i))){
					ind = Integer.valueOf(temp[0].substring(i, i+1));
					break;
				}
			}
			
			JPanel lasertab = new JPanel();
		    lasertab.setBorder(BorderFactory.createTitledBorder(null, "Lasers", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, new Color(0,0,0)));
		    lasertab.setLayout(new BoxLayout(lasertab, BoxLayout.PAGE_AXIS));
			((TitledBorder) lasertab.getBorder()).setTitleFont(((TitledBorder) lasertab.getBorder()).getTitleFont().deriveFont(Font.BOLD, 12));
		    
			ArrayList<String> templaser = new ArrayList<String>();
			templaser.add(temp[0]);

			for(int j=1; j<temp.length;j++){
				int ind2 = 0;
				
				for(int i=0;i<temp[j].length()-1;i++){
					if(Character.isDigit(temp[j].charAt(i)) && Character.isDigit(temp[j].charAt(i+1))){
						ind2 = Integer.valueOf(temp[j].substring(i, i+1));
						break;
					} else if(Character.isDigit(temp[j].charAt(i))){
						ind2 = Integer.valueOf(temp[j].substring(i, i+1));
						break;
					}
				}

				if(ind2 == ind && j < temp.length-1){
					templaser.add(temp[j]);
				} else if(ind2 == ind && j == temp.length-1){
					// create a jpanel
					templaser.add(temp[j]);
				    JPanel subpanel = createTable(templaser.toArray(new String[0]),false,propertyValues);
				    lasertab.add(subpanel);
				}else if(ind2 != ind && j == temp.length-1){
					// create a jpanel
				    JPanel subpanel = createTable(templaser.toArray(new String[0]),false,propertyValues);
				    lasertab.add(subpanel);
				    
				    templaser = new ArrayList<String>();
					templaser.add(temp[j]);
					
					subpanel = createTable(templaser.toArray(new String[0]),false,propertyValues);
				    lasertab.add(subpanel);
				} else {
					ind = ind2;
					
					// create a jpanel
				    JPanel subpanel = createTable(templaser.toArray(new String[0]),false,propertyValues);
				    lasertab.add(subpanel);
				    
				    templaser = new ArrayList<String>();
					templaser.add(temp[j]);
				}
			}
			
			pane.add(lasertab);
			pane.add(Box.createVerticalStrut(10));
		}

		// Two-state
		filt = new TwoStatePropertyFilter(new AntiFlagPropertyFilter(new FocusLockFlag()));
		temp = filt.filterStringProperties(props);
		props = filt.filteredProperties(props);
		
		if(temp.length>0){
		    JPanel twostate = createTable(temp,false,propertyValues);
		    twostate.setBorder(BorderFactory.createTitledBorder(null, "Two-state", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, new Color(0,0,0)));
			((TitledBorder) twostate.getBorder()).setTitleFont(((TitledBorder) twostate.getBorder()).getTitleFont().deriveFont(Font.BOLD, 12));
			pane.add(twostate);

			pane.add(Box.createVerticalStrut(10));
		}

		// others
		/*filt = new NoPropertyFilter();
		temp = filt.filterStringProperties(props);
		
		if(temp.length>0){
		    JPanel others = createTable(temp,false);
		    others.setBorder(BorderFactory.createTitledBorder(null, "Other", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, new Color(0,0,0)));
			((TitledBorder) others.getBorder()).setTitleFont(((TitledBorder) others.getBorder()).getTitleFont().deriveFont(Font.BOLD, 12));
			pane.add(others);
		}*/
		
		pane.add(new JPanel());
		
		return pane;
	}

	private void setUpPanel() {
		JPanel contentpane = new JPanel();
		
		contentpane.setLayout(new BoxLayout(contentpane, BoxLayout.PAGE_AXIS));
		 
		contentpane.add(Box.createVerticalStrut(5));
		
		JPanel combopanel = new JPanel(new GridLayout(0,4));
		combopanel.add(acqtype_);
		contentpane.add(combopanel);

		contentpane.add(acqcard_);
		
		this.add(new JScrollPane(contentpane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));

	}
	
	/**
	 * Creates a JPanel holding a JTable of the filtered properties with default values
	 * 
	 * @param filteredProperties Properties to add to the table
	 * @param twostatedefault_ Default value for TwoStateProperties
	 * @return
	 */
	private JPanel createTable(String[] filteredProperties, boolean twostatedefault_) {
		JPanel pane = new JPanel();
		
		// Defines table model
		DefaultTableModel model = new DefaultTableModel(new Object[] {"Property", "Value" }, 0);

		// For each property of the UI
		for (int i = 0; i < filteredProperties.length; i++) {	
			UIProperty prop = props_.get(filteredProperties[i]);
			if (prop.isTwoState()) {
				model.addRow(new Object[] {prop.getFriendlyName(), twostatedefault_ });
			} else if (prop.isSingleState()) {
				model.addRow(new Object[] {prop.getFriendlyName(),((SingleStateUIProperty) prop).getStateValue() });
			} else if (prop.isMultiState()) {
				model.addRow(new Object[] {prop.getFriendlyName(),((MultiStateUIProperty) prop).getStatesName()[0] });
			} else if (prop.isFixedState()) {
				model.addRow(new Object[] {prop.getFriendlyName(),((FixedStateUIProperty) prop).getStatesName()[0] });
			} else {
				model.addRow(new Object[] {prop.getFriendlyName(),prop.getPropertyValue() });
			}
		}
				
		JTable table = new JTable(model) {
			private static final long serialVersionUID = -7528102943663023952L;

			@Override
			public TableCellRenderer getCellRenderer(int row, int column) {
				switch (column) {
				case 0:
					return new BoldTableCellRenderer(); // first column is written in bold font
				case 1:
					if(getValueAt(row, column) instanceof Boolean){
						return super.getDefaultRenderer(Boolean.class);
					}
					return new DefaultTableCellRenderer(); // column 1 takes a default renderer 
				default:
					return super.getCellRenderer(row, column);
				}
			}

			@Override
			public TableCellEditor getCellEditor(int row, int column) {
				String s = (String) this.getValueAt(row, 0);

				if(column == 1){
					if (getValueAt(row, column) instanceof Boolean) {
						return super.getDefaultEditor(Boolean.class);
					} else if (props_.get(propsfriendlyname_.get(s)).isMultiState()) { 
						return new DefaultCellEditor(new JComboBox(((MultiStateUIProperty) props_.get(propsfriendlyname_.get(s))).getStatesName()));
					} else if (props_.get(propsfriendlyname_.get(s)).isFixedState()) { 
						return new DefaultCellEditor(new JComboBox(((FixedStateUIProperty) props_.get(propsfriendlyname_.get(s))).getStatesName()));
					} else if (props_.get(propsfriendlyname_.get(s)).hasMMPropertyAllowedValues()){
						return new DefaultCellEditor(new JComboBox(props_.get(propsfriendlyname_.get(s)).getAllowedValues()));
					} else {
						super.getCellEditor(row, column);
					}
				} 

				return super.getCellEditor(row, column);
			}

			@Override
			public boolean isCellEditable(int row, int col) { // first column is non-editable and second as well if it is a field value row
				if(col == 0){
					return false;
				}
				return true;
			}

		};
		
		table.setAutoCreateRowSorter(false);	
		table.setRowHeight(23);
		table.setBorder(BorderFactory.createLineBorder(Color.black,1));
		table.setRowSelectionAllowed(false);
		pane.setLayout(new GridLayout());
		pane.add(table);
		
		return pane;
	}
	/**
	 * Creates a JPanel holding a JTable of the filtered properties with the current values of the acquisition properties.
	 * 
	 * @param filteredProperties Properties to add to the table
	 * @param twostatedefault_ Default value for TwoStateProperties
	 * @param propertyValues Property values of the current acquisition
	 * @return
	 */
	private JPanel createTable(String[] filteredProperties, boolean twostatedefault_, HashMap<String, String> propertyValues) {
		JPanel pane = new JPanel();
		
		// Defines table model
		DefaultTableModel model = new DefaultTableModel(new Object[] {"Property", "Value" }, 0);
		
		// For each property of the UI
		for (int i = 0; i < filteredProperties.length; i++) {	
			UIProperty prop = props_.get(filteredProperties[i]);
			if(propertyValues.containsKey(filteredProperties[i])){ // if the property is found in the acquisition properties
				if (prop.isTwoState()) {
					if(propertyValues.get(filteredProperties[i]).equals(TwoStateUIProperty.getOnStateName())){
						model.addRow(new Object[] {prop.getFriendlyName(), true });
					} else {
						model.addRow(new Object[] {prop.getFriendlyName(), false });
					}
				} else if (prop.isSingleState()) {
					model.addRow(new Object[] {prop.getFriendlyName(), propertyValues.get(filteredProperties[i])});
				} else if (prop.isMultiState()) {
					model.addRow(new Object[] {prop.getFriendlyName(),propertyValues.get(filteredProperties[i])});
				} else if (prop.isFixedState()) {
					model.addRow(new Object[] {prop.getFriendlyName(),propertyValues.get(filteredProperties[i])});
				} else {
					model.addRow(new Object[] {prop.getFriendlyName(),propertyValues.get(filteredProperties[i])});
				}
			} else { // if not, set by default value
				if (prop.isTwoState()) {
					model.addRow(new Object[] {prop.getFriendlyName(), twostatedefault_ });
				} else if (prop.isSingleState()) {
					model.addRow(new Object[] {prop.getFriendlyName(),((SingleStateUIProperty) prop).getStateValue() });
				} else if (prop.isMultiState()) {
					model.addRow(new Object[] {prop.getFriendlyName(),((MultiStateUIProperty) prop).getStatesName()[0] });
				} else if (prop.isFixedState()) {
					model.addRow(new Object[] {prop.getFriendlyName(),((FixedStateUIProperty) prop).getStatesName()[0] });
				} else {
					model.addRow(new Object[] {prop.getFriendlyName(),prop.getPropertyValue() });
				}
			}
		}
				
		JTable table = new JTable(model) {
			private static final long serialVersionUID = -7528102943663023952L;

			@Override
			public TableCellRenderer getCellRenderer(int row, int column) {
				switch (column) {
				case 0:
					return new BoldTableCellRenderer(); // first column is written in bold font
				case 1:
					if(getValueAt(row, column) instanceof Boolean){
						return super.getDefaultRenderer(Boolean.class);
					}
					return new DefaultTableCellRenderer(); // column 1 takes a default renderer 
				default:
					return super.getCellRenderer(row, column);
				}
			}

			@Override
			public TableCellEditor getCellEditor(int row, int column) {
				String s = (String) this.getValueAt(row, 0);
				
				if(column == 1){
					if (getValueAt(row, column) instanceof Boolean) {
						return super.getDefaultEditor(Boolean.class);
					} else if (props_.get(propsfriendlyname_.get(s)).isMultiState()) { 
						return new DefaultCellEditor(new JComboBox(((MultiStateUIProperty) props_.get(propsfriendlyname_.get(s))).getStatesName()));
					} else if (props_.get(propsfriendlyname_.get(s)).isFixedState()) { 
						return new DefaultCellEditor(new JComboBox(((FixedStateUIProperty) props_.get(propsfriendlyname_.get(s))).getStatesName()));
					} else if (props_.get(propsfriendlyname_.get(s)).hasMMPropertyAllowedValues()){
						return new DefaultCellEditor(new JComboBox(props_.get(propsfriendlyname_.get(s)).getAllowedValues()));
					} else {
						super.getCellEditor(row, column);
					}
				} 

				return super.getCellEditor(row, column);
			}

			@Override
			public boolean isCellEditable(int row, int col) { // first column is non-editable and second as well if it is a field value row
				if(col == 0){
					return false;
				}
				return true;
			}

		};
		
		table.setAutoCreateRowSorter(false);	
		table.setRowHeight(23);
		table.setBorder(BorderFactory.createLineBorder(Color.black,1));
		table.setRowSelectionAllowed(false);
		pane.setLayout(new GridLayout());
		pane.add(table);
		
		return pane;
	}
	

	private HashMap<String, String> getProperties(JPanel pane){ 
		HashMap<String, String> props = new HashMap<String, String>(); // <PropertyName, value>
		
		// find panels within and tables
		Component[] comps = pane.getComponents();
		for(int i=0;i<comps.length;i++){
			if(comps[i] instanceof JPanel && comps[i].getName() == null){ // get JPanel which have not been given a name
				Component[] subcomps = ((JPanel) comps[i]).getComponents();
				for(int j=0;j<subcomps.length;j++){	// loop over all their subcomponents
					if(subcomps[j] instanceof JTable){	// if find a JTable
						if (((JTable) subcomps[j]).isEditing()) ((JTable) subcomps[j]).getCellEditor().stopCellEditing();
						TableModel model = ((JTable) subcomps[j]).getModel();
						int nrow = model.getRowCount(); 
						for(int k=0;k<nrow;k++){	// loop through the rows
							if(!(model.getValueAt(k, 1) instanceof Boolean)){ // if second row is not a boolean property 
								props.put(propsfriendlyname_.get((String) model.getValueAt(k, 0)), (String) model.getValueAt(k, 1)); 
								System.out.println(propsfriendlyname_.get((String) model.getValueAt(k, 0))+" and "+(String) model.getValueAt(k, 1));
							} else {
								if((Boolean) model.getValueAt(k, 1)){
									props.put(propsfriendlyname_.get((String) model.getValueAt(k, 0)), TwoStateUIProperty.getOnStateName()); 
								} else {
									props.put(propsfriendlyname_.get((String) model.getValueAt(k, 0)), TwoStateUIProperty.getOffStateName());
								}
							}
						}
					} else if(subcomps[j] instanceof JPanel){
						Component[] subsubcomps = ((JPanel) subcomps[j]).getComponents();
						for(int l=0;l<subsubcomps.length;l++){	// loop over all their subcomponents
							if(subsubcomps[l] instanceof JTable){	// if find a JTable
								if (((JTable) subsubcomps[l]).isEditing()) ((JTable) subsubcomps[l]).getCellEditor().stopCellEditing();
								TableModel model = ((JTable) subsubcomps[l]).getModel();
								int nrow = model.getRowCount(); 
								for(int k=0;k<nrow;k++){	// loop through the rows
									if(!(model.getValueAt(k, 1) instanceof Boolean)){ // if second row is not a boolean property 
										props.put(propsfriendlyname_.get((String) model.getValueAt(k, 0)), (String) model.getValueAt(k, 1)); 
										System.out.println(propsfriendlyname_.get((String) model.getValueAt(k, 0))+" and "+(String) model.getValueAt(k, 1));
									} else {
										if((Boolean) model.getValueAt(k, 1)){
											System.out.println(propsfriendlyname_.get((String) model.getValueAt(k, 0))+" and "+TwoStateUIProperty.getOnStateName());
											props.put(propsfriendlyname_.get((String) model.getValueAt(k, 0)), TwoStateUIProperty.getOnStateName()); 
										} else {
											System.out.println(propsfriendlyname_.get((String) model.getValueAt(k, 0))+" and "+TwoStateUIProperty.getOffStateName());
											props.put(propsfriendlyname_.get((String) model.getValueAt(k, 0)), TwoStateUIProperty.getOffStateName());
										}
									}
								}
							}
						}
					}
				}
			}
		}
		
		return props;
	}

	public String getTypeName(){
		return acqtypes_[currind];
	}
	
	private void changeAcquisition(String type){
		int temp = currind;
		for(int i=0;i<acqtypes_.length;i++){
			if(acqtypes_[i].equals(type)){
				currind = i;
				break;
			}
		}
		
		if(temp == currind){
			return;
		}
	
		this.setName(type);
		wizard_.changeName(this);

	    CardLayout cl = (CardLayout)(acqcard_.getLayout());
	    cl.show(acqcard_, type);
	}
	
	
	public Acquisition getAcquisition() {
		// get acquisition from factory with the right type
		Acquisition acq = factory_.getAcquisition(acqtypes_[currind]);
		
		// set properties value in the acquisition
		acq.setProperties(getProperties(acqpanes_[currind]));
		
		// read out the JPanel related to the acquisition
		acq.readOutParameters(acqpanels_[currind]);
		
		return acq;
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