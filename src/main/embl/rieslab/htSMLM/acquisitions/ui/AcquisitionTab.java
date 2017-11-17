package main.embl.rieslab.htSMLM.acquisitions.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

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

import main.embl.rieslab.htSMLM.acquisitions.Acquisition;
import main.embl.rieslab.htSMLM.acquisitions.AcquisitionFactory;
import main.embl.rieslab.htSMLM.acquisitions.AcquisitionType;
import main.embl.rieslab.htSMLM.ui.uiproperties.MultiStateUIProperty;
import main.embl.rieslab.htSMLM.ui.uiproperties.PropertyFlag;
import main.embl.rieslab.htSMLM.ui.uiproperties.SingleStateUIProperty;
import main.embl.rieslab.htSMLM.ui.uiproperties.UIProperty;

public class AcquisitionTab extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7966565586677957738L;

	private AcquisitionWizard wizard_;
	private AcquisitionFactory factory_;
	private JPanel card_;
	private JPanel[] panes_;
	private JComboBox acqtype_;
	private String[] acqtypes_;
	private int currind;
	private HashMap<String, UIProperty> props_;
	
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
		this.setName(acqtypes_[0]);
		
	    card_ = new JPanel(new CardLayout());
		panes_ = new JPanel[acqtypes_.length];
		for(int i=0;i<acqtypes_.length;i++){
			panes_[i] = factory_.getAcquisition(acqtypes_[i]).getPanel();
			card_.add(panes_[i],acqtypes_[i]);
		}
		
		props_ = new HashMap<String, UIProperty>();
		
		setUpPanel();
	}


	private void setUpPanel() {
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		 
	    this.add(Box.createVerticalStrut(5));
		
		JPanel combopanel = new JPanel(new GridLayout(0,4));
		combopanel.add(acqtype_);
		this.add(combopanel);

	    this.add(Box.createVerticalStrut(10));
	    
	    this.add(card_);

	    this.add(Box.createVerticalStrut(10));
	    
	    JPanel focstab = createTab(wizard_.getController().getFilteredProperties(PropertyFlag.FOCUSSTAB.getDeviceType()));
	    focstab.setBorder(BorderFactory.createTitledBorder(null, "Focus stabilization", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, new Color(0,0,0)));
		((TitledBorder) focstab.getBorder()).setTitleFont(((TitledBorder) focstab.getBorder()).getTitleFont().deriveFont(Font.BOLD, 12));
		this.add(focstab);

	    this.add(Box.createVerticalStrut(10));

	    JPanel fw = createTab(wizard_.getController().getFilteredProperties(PropertyFlag.FILTERWHEEL.getDeviceType()));
	    fw.setBorder(BorderFactory.createTitledBorder(null, "Filter wheel", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, new Color(0,0,0)));
		((TitledBorder) fw.getBorder()).setTitleFont(((TitledBorder) fw.getBorder()).getTitleFont().deriveFont(Font.BOLD, 12));
		this.add(fw);

	    this.add(Box.createVerticalStrut(10));
	    
	    JPanel lasertab = createTab(wizard_.getController().getFilteredProperties(PropertyFlag.LASER.getDeviceType()));
	    lasertab.setBorder(BorderFactory.createTitledBorder(null, "Lasers", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, new Color(0,0,0)));
		((TitledBorder) lasertab.getBorder()).setTitleFont(((TitledBorder) lasertab.getBorder()).getTitleFont().deriveFont(Font.BOLD, 12));
		this.add(lasertab);

	    this.add(Box.createVerticalStrut(10));


	}
	
	private JPanel createTab(String[] filteredProperties) {
		JPanel pane = new JPanel();
		
		// Defines table model
		DefaultTableModel model = new DefaultTableModel(new Object[] {"Property", "Value" }, 0);

		// For each property of the UI
		for (int i = 0; i < filteredProperties.length; i++) {	
			UIProperty prop = wizard_.getController().getProperty(filteredProperties[i]);
			if(!prop.isAllocated() || prop.isMMPropertyReadOnly()){
				// do nothing
			}else if (prop.isTwoState()) {
				props_.put(prop.getFriendlyName(),prop);
				model.addRow(new Object[] {prop.getFriendlyName(), false });
			} else if (prop.isSingleState()) {
				props_.put(prop.getFriendlyName(),prop);
				model.addRow(new Object[] {prop.getFriendlyName(),((SingleStateUIProperty) prop).getStateValue() });
			} else if (prop.isMultiState()) {
				props_.put(prop.getFriendlyName(),prop);
				model.addRow(new Object[] {prop.getFriendlyName(),((MultiStateUIProperty) prop).getStatesName()[0] });
			} else {
				props_.put(prop.getFriendlyName(),prop);
				model.addRow(new Object[] {prop.getFriendlyName(),prop.getPropertyValue() });
			}
		}
		
		System.out.println("size: "+props_.size());
		
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
					} else if (props_.get(s).isMultiState()) { 
						return new DefaultCellEditor(new JComboBox(((MultiStateUIProperty) props_.get(s)).getStatesName()));
					} else if (props_.get(s).hasMMPropertyAllowedValues()){
						return new DefaultCellEditor(new JComboBox(props_.get(s).getAllowedValues()));
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


	private HashMap<String, String> getProperties(){
		HashMap<String, String> props = new HashMap<String, String>();
		
		// read out
		
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
		
	    CardLayout cl = (CardLayout)(card_.getLayout());
	    cl.show(card_, type);
	}
	
	
	public Acquisition getAcquisition() {
		Acquisition acq = factory_.getAcquisition(acqtypes_[currind]);
		acq.setProperties(getProperties());
		acq.readOutParameters(panes_[currind]);
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
