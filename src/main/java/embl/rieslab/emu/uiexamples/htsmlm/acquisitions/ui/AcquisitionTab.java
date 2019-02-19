package main.java.embl.rieslab.emu.uiexamples.htsmlm.acquisitions.ui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
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

import main.java.embl.rieslab.emu.micromanager.configgroups.MMConfigurationGroupsRegistry;
import main.java.embl.rieslab.emu.ui.uiproperties.FixedStateUIProperty;
import main.java.embl.rieslab.emu.ui.uiproperties.MultiStateUIProperty;
import main.java.embl.rieslab.emu.ui.uiproperties.SingleStateUIProperty;
import main.java.embl.rieslab.emu.ui.uiproperties.TwoStateUIProperty;
import main.java.embl.rieslab.emu.ui.uiproperties.UIProperty;
import main.java.embl.rieslab.emu.ui.uiproperties.filters.AllocatedPropertyFilter;
import main.java.embl.rieslab.emu.ui.uiproperties.filters.AntiFlagPropertyFilter;
import main.java.embl.rieslab.emu.ui.uiproperties.filters.FlagPropertyFilter;
import main.java.embl.rieslab.emu.ui.uiproperties.filters.NoneConfigGroupPropertyFilter;
import main.java.embl.rieslab.emu.ui.uiproperties.filters.PropertyFilter;
import main.java.embl.rieslab.emu.ui.uiproperties.filters.ReadOnlyPropertyFilter;
import main.java.embl.rieslab.emu.ui.uiproperties.filters.TwoStatePropertyFilter;
import main.java.embl.rieslab.emu.uiexamples.htsmlm.acquisitions.AcquisitionFactory;
import main.java.embl.rieslab.emu.uiexamples.htsmlm.acquisitions.acquisitiontypes.Acquisition;
import main.java.embl.rieslab.emu.uiexamples.htsmlm.flags.FilterWheelFlag;
import main.java.embl.rieslab.emu.uiexamples.htsmlm.flags.FocusLockFlag;
import main.java.embl.rieslab.emu.uiexamples.htsmlm.flags.FocusStabFlag;
import main.java.embl.rieslab.emu.uiexamples.htsmlm.flags.LaserFlag;

public class AcquisitionTab extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7966565586677957738L;

	public final static String KEY_IGNORED = "Ignored";
	private final static String KEY_MMCONF = "Configuration settings";
	
	private AcquisitionWizard wizard_;
	private AcquisitionFactory factory_;
	private JPanel acqcard_;
	private JPanel[] acqpanes_, acqpanels_;
	private JComboBox<String> acqtype_;
	private String[] acqtypes_;
	private int currind;
	private HashMap<String, UIProperty> props_;
	private HashMap<String, String> propsfriendlyname_;
	
	public AcquisitionTab(AcquisitionWizard wizard, AcquisitionFactory factory) {
		factory_ = factory;
		wizard_ = wizard;
		
		// Get the array of acquisition types and create a JComboBox
		acqtypes_ = factory_.getAcquisitionTypeList();
		acqtype_ = new JComboBox<String>(acqtypes_);
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
		props_ = (new NoneConfigGroupPropertyFilter(new AllocatedPropertyFilter(new ReadOnlyPropertyFilter()))).filterProperties(wizard_.getPropertiesMap());
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

			Acquisition acq = factory_.getAcquisition(acqtypes_[i]);
			JPanel pane = acq.getPanel();
			
			pane.setBorder(BorderFactory.createTitledBorder(null, "Acquisition Settings", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, new Color(0,0,0)));
			((TitledBorder) pane.getBorder()).setTitleFont(((TitledBorder) pane.getBorder()).getTitleFont().deriveFont(Font.BOLD, 12));
			
			acqpanels_[i] = pane;
			acqpanes_[i] = createPanel(acqpanels_[i],acq.getPropertyFilter(), new HashMap<String,String>(), new HashMap<String,String>());
			acqcard_.add(acqpanes_[i],acqtypes_[i]);			
		}
		
		setUpPanel();
	}


	public AcquisitionTab(AcquisitionWizard wizard, AcquisitionFactory factory, Acquisition acquisition) {
		factory_ = factory;
		wizard_ = wizard;
		
		// Get the array of acquisition types and create a JComboBox
		acqtypes_ = factory_.getAcquisitionTypeList();
		acqtype_ = new JComboBox<String>(acqtypes_);
		acqtype_.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){
	            changeAcquisition((String) acqtype_.getSelectedItem());
	    	}
	    });
		
		// Set current acquisition to the acquisition passed as parameter
		currind = 0;
		for(int i=0;i<acqtypes_.length;i++){
			if(acqtypes_[i].equals(acquisition.getType().getTypeValue())){
				currind = i;
				break;
			}
		}
		this.setName(acqtypes_[currind]);
		acqtype_.setSelectedIndex(currind);
		
		// Filter out read-only properties from the system properties
		props_ = (new NoneConfigGroupPropertyFilter(new AllocatedPropertyFilter(new ReadOnlyPropertyFilter()))).filterProperties(wizard_.getPropertiesMap());
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
				JPanel pane = acquisition.getPanel();
				
				pane.setBorder(BorderFactory.createTitledBorder(null, "Acquisition Settings", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, new Color(0,0,0)));
				((TitledBorder) pane.getBorder()).setTitleFont(((TitledBorder) pane.getBorder()).getTitleFont().deriveFont(Font.BOLD, 12));
				acqpanels_[i] = pane;
				acqpanes_[i] = createPanel(acqpanels_[i],acquisition.getPropertyFilter(), 
						acquisition.getParameters().getMMConfigurationGroupValues(), acquisition.getParameters().getPropertyValues());
				acqcard_.add(acqpanes_[i],acqtypes_[i]);
			} else {
				acqpanels_[i] = factory_.getAcquisition(acqtypes_[i]).getPanel();
				acqpanes_[i] = createPanel(acqpanels_[i],factory_.getAcquisition(acqtypes_[i]).getPropertyFilter(), new HashMap<String,String>(), new HashMap<String,String>());
				acqcard_.add(acqpanes_[i],acqtypes_[i]);	
			}
		}
		
		setUpPanel();
		
	    CardLayout cl = (CardLayout)(acqcard_.getLayout());
	    cl.show(acqcard_, acqtypes_[currind]);
	   
	}

	private JPanel createPanel(JPanel acqpane, PropertyFilter filter, HashMap<String, String> mmconfigGroups, HashMap<String, String> propertyValues) {
		JPanel pane = new JPanel();
		pane.setLayout(new BoxLayout(pane, BoxLayout.PAGE_AXIS));

		pane.add(Box.createVerticalStrut(10));
		
		// acquisition panel
		pane.add(acqpane);
		
		pane.add(Box.createVerticalStrut(10));

		// MM config groups		
	    JPanel mmconfig = createMMConfigTable(wizard_.getMMConfigurationGroups(), mmconfigGroups);   
	    mmconfig.setBorder(BorderFactory.createTitledBorder(null, KEY_MMCONF, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, new Color(0,0,0)));
		((TitledBorder) mmconfig.getBorder()).setTitleFont(((TitledBorder) mmconfig.getBorder()).getTitleFont().deriveFont(Font.BOLD, 12));
		pane.add(mmconfig);
		
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
		    JPanel focstab = createPropertyTable(temp,true,propertyValues);
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
		    JPanel fw = createPropertyTable(temp,false,propertyValues);
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
		///////////////////////////////////////////////////////////////// to mark a border between them
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
				    JPanel subpanel = createPropertyTable(templaser.toArray(new String[0]),false,propertyValues);
				    lasertab.add(subpanel);
				}else if(ind2 != ind && j == temp.length-1){
					// create a jpanel
				    JPanel subpanel = createPropertyTable(templaser.toArray(new String[0]),false,propertyValues);
				    lasertab.add(subpanel);
				    
				    templaser = new ArrayList<String>();
					templaser.add(temp[j]);
					
					subpanel = createPropertyTable(templaser.toArray(new String[0]),false,propertyValues);
				    lasertab.add(subpanel);
				} else {
					ind = ind2;
					
					// create a jpanel
				    JPanel subpanel = createPropertyTable(templaser.toArray(new String[0]),false,propertyValues);
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
		    JPanel twostate = createPropertyTable(temp,false,propertyValues);
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
	
	private JPanel createMMConfigTable(MMConfigurationGroupsRegistry mmconfigreg, HashMap<String,String> mmconfigGroups) {
		JPanel pane = new JPanel();
		
		// Defines table model
		DefaultTableModel model = new DefaultTableModel(new Object[] {"Property", "Value" }, 0);

		final HashMap<String, String[]> map = mmconfigreg.getMMConfigurationChannels();
		String[] keys = map.keySet().toArray(new String[0]);
		Arrays.sort(keys);
		
		// For each configuration group
		for (int i = 0; i < keys.length; i++) {	
			if(map.get(keys[i]) != null && map.get(keys[i]).length > 0){
				String s = mmconfigreg.getCurrentMMConfigurationChannel(keys[i]);
				if(s.length() > 0){
					model.addRow(new Object[] {keys[i], s });
				} else if(s != null) {
					if(mmconfigGroups.containsKey(keys[i]) && 
							mmconfigreg.getMMConfigurationGroups().get(keys[i]).hasConfiguration(mmconfigGroups.get(keys[i]))){
						model.addRow(new Object[] {keys[i], mmconfigGroups.get(keys[i]) });
					} else {
						model.addRow(new Object[] {keys[i], KEY_IGNORED });
					}
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
					return new DefaultTableCellRenderer(); // column 1 takes a default renderer 
				default:
					return super.getCellRenderer(row, column);
				}
			}

			@Override
			public TableCellEditor getCellEditor(int row, int column) {
				String s = (String) this.getValueAt(row, 0);

				if(column == 1){
					String[] states = map.get(s);
					String[] states_ig = new String[states.length+1];
					states_ig[0] = KEY_IGNORED;
					for(int i=0; i<states.length; i++){
						states_ig[i+1] = states[i];
					}
					
					return new DefaultCellEditor(new JComboBox<String>(states_ig));
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
		table.setName(KEY_MMCONF);
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
	 * @return JPanel with the filtered UIProperties JTable.
	 */
	private JPanel createPropertyTable(String[] filteredProperties, boolean twostatedefault_, HashMap<String, String> propertyValues) {
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
						return new DefaultCellEditor(new JComboBox<String>(((MultiStateUIProperty) props_.get(propsfriendlyname_.get(s))).getStatesName()));
					} else if (props_.get(propsfriendlyname_.get(s)).isFixedState()) { 
						return new DefaultCellEditor(new JComboBox<String>(((FixedStateUIProperty) props_.get(propsfriendlyname_.get(s))).getStatesName()));
					} else if (props_.get(propsfriendlyname_.get(s)).hasMMPropertyAllowedValues()){
						return new DefaultCellEditor(new JComboBox<String>(props_.get(propsfriendlyname_.get(s)).getAllowedValues()));
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
	
	private HashMap<String, String> registerProperties(Component c, HashMap<String, String> properties){
		if(c instanceof JTable && (c.getName() == null || !c.getName().equals(KEY_MMCONF))){
			if (((JTable) c).isEditing()) {
				((JTable) c).getCellEditor().stopCellEditing();
			}
			TableModel model = ((JTable) c).getModel();  
			int nrow = model.getRowCount(); 
			for(int k=0;k<nrow;k++){	// loop through the rows
				String s = (String) model.getValueAt(k, 0);
				if(!(model.getValueAt(k, 1) instanceof Boolean)){ // if second row is not a boolean property 
					properties.put(propsfriendlyname_.get(s), (String) model.getValueAt(k, 1)); 
				} else {
					if((Boolean) model.getValueAt(k, 1)){
						properties.put(propsfriendlyname_.get(s), TwoStateUIProperty.getOnStateName()); 
					} else {
						properties.put(propsfriendlyname_.get(s), TwoStateUIProperty.getOffStateName());
					}
				}
			}
		} else if(c instanceof JPanel){
			Component[] subcomps = ((JPanel) c).getComponents();
			for(int l=0;l<subcomps.length;l++){
				registerProperties(subcomps[l], properties);
			}
		}
		return properties;
	}

	private HashMap<String, String> registerMMConfGroups(Component c, HashMap<String, String> confgroups){
		if(c instanceof JTable && c.getName() != null && c.getName().equals(KEY_MMCONF)){
			if (((JTable) c).isEditing()) {
				((JTable) c).getCellEditor().stopCellEditing();
			}
			TableModel model = ((JTable) c).getModel();  
			int nrow = model.getRowCount(); 
			for(int k=0;k<nrow;k++){	// loop through the rows
				String group = (String) model.getValueAt(k, 0);
				String val = (String) model.getValueAt(k, 1);

				if(!val.equals(KEY_IGNORED)){
					confgroups.put(group, val);
				}
			}
		} else if(c instanceof JPanel){
			Component[] subcomps = ((JPanel) c).getComponents();
			for(int l=0;l<subcomps.length;l++){
				registerMMConfGroups(subcomps[l], confgroups);
			}
		}
		return confgroups;
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
		
		// set mm configuration groups
		acq.getParameters().setMMConfigurationGroupValues(registerMMConfGroups(acqpanes_[currind], new HashMap<String, String>()));
		
		// set properties value in the acquisition
		acq.getParameters().setPropertyValues(registerProperties(acqpanes_[currind], new HashMap<String, String>()));
		
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
