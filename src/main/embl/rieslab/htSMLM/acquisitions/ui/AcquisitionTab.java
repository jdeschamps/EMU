package main.embl.rieslab.htSMLM.acquisitions.ui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import main.embl.rieslab.htSMLM.acquisitions.Acquisition;
import main.embl.rieslab.htSMLM.acquisitions.AcquisitionFactory;
import main.embl.rieslab.htSMLM.acquisitions.AcquisitionType;
import main.embl.rieslab.htSMLM.ui.uiproperties.MultiStateUIProperty;
import main.embl.rieslab.htSMLM.ui.uiproperties.PropertyFlag;
import main.embl.rieslab.htSMLM.ui.uiproperties.SingleStateUIProperty;
import main.embl.rieslab.htSMLM.ui.uiproperties.TwoStateUIProperty;
import main.embl.rieslab.htSMLM.ui.uiproperties.UIProperty;
import main.embl.rieslab.htSMLM.ui.uiproperties.filters.AllocatedPropertyFilter;
import main.embl.rieslab.htSMLM.ui.uiproperties.filters.FlagPropertyFilter;
import main.embl.rieslab.htSMLM.ui.uiproperties.filters.PropertyFilter;
import main.embl.rieslab.htSMLM.ui.uiproperties.filters.ReadOnlyPropertyFilter;
import main.embl.rieslab.htSMLM.ui.uiproperties.filters.TwoStatePropertyFilter;

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
		
		acqtypes_ = AcquisitionType.getList();
		acqtype_ = new JComboBox(acqtypes_);
		acqtype_.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){
	            changeAcquisition((String) acqtype_.getSelectedItem());
	    	}
	    });
		
		currind = 0;
		this.setName(acqtypes_[currind]);
		
		props_ = new AllocatedPropertyFilter(new ReadOnlyPropertyFilter()).filterProperties(wizard_.getController().getPropertiesMap());
		propsfriendlyname_ = new HashMap<String, String>();
		Iterator<String> it = props_.keySet().iterator();
		String s;
		while(it.hasNext()){
			s = it.next();
			propsfriendlyname_.put(props_.get(s).getFriendlyName(), s);
		}
		
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
		
		acqtypes_ = AcquisitionType.getList();
		acqtype_ = new JComboBox(acqtypes_);
		acqtype_.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){
	            changeAcquisition((String) acqtype_.getSelectedItem());
	    	}
	    });
		
		currind = 0;
		for(int i=0;i<acqtypes_.length;i++){
			if(acqtypes_[i].equals(acquisition.getType())){
				currind = i;
				break;
			}
		}
		this.setName(acqtypes_[currind]);
		acqtype_.setSelectedIndex(currind);
		
		props_ = new AllocatedPropertyFilter(new ReadOnlyPropertyFilter()).filterProperties(wizard_.getController().getPropertiesMap());
		propsfriendlyname_ = new HashMap<String, String>();
		Iterator<String> it = props_.keySet().iterator();
		String s;
		while(it.hasNext()){
			s = it.next();
			propsfriendlyname_.put(props_.get(s).getFriendlyName(), s);
		}
		
	    acqcard_ = new JPanel(new CardLayout());
		acqpanes_ = new JPanel[acqtypes_.length];
		acqpanels_ = new JPanel[acqtypes_.length];
		for(int i=0;i<acqtypes_.length;i++){
			if(i==currind){
				System.out.println("currind = "+i);
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
		filt = new FlagPropertyFilter(PropertyFlag.FOCUSSTAB);
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
		filt = new FlagPropertyFilter(PropertyFlag.FILTERWHEEL);
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
		filt = new FlagPropertyFilter(PropertyFlag.LASER);
		temp = filt.filterStringProperties(props);
		props = filt.filteredProperties(props);
		
		if(temp.length>0){
		    JPanel lasertab = createTable(temp,false);
		    lasertab.setBorder(BorderFactory.createTitledBorder(null, "Lasers", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, new Color(0,0,0)));
			((TitledBorder) lasertab.getBorder()).setTitleFont(((TitledBorder) lasertab.getBorder()).getTitleFont().deriveFont(Font.BOLD, 12));
			pane.add(lasertab);

			pane.add(Box.createVerticalStrut(10));
		}

		// Two-state
		filt = new TwoStatePropertyFilter();
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
		filt = new FlagPropertyFilter(PropertyFlag.FOCUSSTAB);
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
		filt = new FlagPropertyFilter(PropertyFlag.FILTERWHEEL);
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
		filt = new FlagPropertyFilter(PropertyFlag.LASER);
		temp = filt.filterStringProperties(props);
		props = filt.filteredProperties(props);
		
		if(temp.length>0){
		    JPanel lasertab = createTable(temp,false,propertyValues);
		    lasertab.setBorder(BorderFactory.createTitledBorder(null, "Lasers", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, new Color(0,0,0)));
			((TitledBorder) lasertab.getBorder()).setTitleFont(((TitledBorder) lasertab.getBorder()).getTitleFont().deriveFont(Font.BOLD, 12));
			pane.add(lasertab);

			pane.add(Box.createVerticalStrut(10));
		}

		// Two-state
		filt = new TwoStatePropertyFilter();
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

	private JPanel createTable(String[] filteredProperties, boolean twostatedefault_, HashMap<String, String> propertyValues) {
		JPanel pane = new JPanel();
		
		// Defines table model
		DefaultTableModel model = new DefaultTableModel(new Object[] {"Property", "Value" }, 0);

		// For each property of the UI
		for (int i = 0; i < filteredProperties.length; i++) {	
			UIProperty prop = props_.get(filteredProperties[i]);
			if(propertyValues.containsKey(prop.getFriendlyName())){
				if (prop.isTwoState()) {
					if(propertyValues.get(prop.getFriendlyName()).equals(TwoStateUIProperty.ON)){
						model.addRow(new Object[] {prop.getFriendlyName(), true });
					} else {
						model.addRow(new Object[] {prop.getFriendlyName(), false });
					}
				} else if (prop.isSingleState()) {
					model.addRow(new Object[] {prop.getFriendlyName(), propertyValues.get(prop.getFriendlyName()) });
				} else if (prop.isMultiState()) {
					model.addRow(new Object[] {prop.getFriendlyName(),propertyValues.get(prop.getFriendlyName()) });
				} else {
					model.addRow(new Object[] {prop.getFriendlyName(),propertyValues.get(prop.getFriendlyName())});
				}
			} else {
				if (prop.isTwoState()) {
					model.addRow(new Object[] {prop.getFriendlyName(), twostatedefault_ });
				} else if (prop.isSingleState()) {
					model.addRow(new Object[] {prop.getFriendlyName(),((SingleStateUIProperty) prop).getStateValue() });
				} else if (prop.isMultiState()) {
					model.addRow(new Object[] {prop.getFriendlyName(),((MultiStateUIProperty) prop).getStatesName()[0] });
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
		HashMap<String, String> props = new HashMap<String, String>();
		
		// find panels within and tables
		Component[] comps = pane.getComponents();
		for(int i=0;i<comps.length;i++){
			if(comps[i] instanceof JPanel && comps[i].getName() == null){
				Component[] subcomps = ((JPanel) comps[i]).getComponents();
				for(int j=0;j<subcomps.length;j++){
					if(subcomps[j] instanceof JTable){
						TableModel model = ((JTable) subcomps[j]).getModel();
						int nrow = model.getRowCount(); 
						for(int k=0;k<nrow;k++){
							if(!(model.getValueAt(k, 1) instanceof Boolean)){
								props.put((String) model.getValueAt(k, 0), (String) model.getValueAt(k, 1)); 
							} else {
								if((Boolean) model.getValueAt(k, 1)){
									props.put((String) model.getValueAt(k, 0), TwoStateUIProperty.ON); 
								} else {
									props.put((String) model.getValueAt(k, 0), TwoStateUIProperty.OFF);
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
