package main.embl.rieslab.htSMLM.controller.uiwizard;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractCellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import main.embl.rieslab.htSMLM.controller.SystemConstants;
import main.embl.rieslab.htSMLM.micromanager.properties.MMProperties;
import main.embl.rieslab.htSMLM.util.ColorRepository;


// This class has been adapted from a java document example
public class ComboTable extends JPanel {

    /**
	 * 
	 */
	private static final long serialVersionUID = 4663110661135902728L;

	private MMProperties mmproperties_;
	
	private JComboBox behaviour, color, devices, properties;
	private SpinnerNumberModel spinmodel;
	private int numlasers=0;
	private JTable table;
	private boolean powerenabled = false;
	
	final private String behaviourString = "Default behaviour";
	final private String colorString = "Colour ";
	final private String nameString = "name";
	final private String deviceString = "Device label in MM";
	final private String powerPropString = "Power % property";
	final private String operationPropString = "On/off property";
	
	public ComboTable() {
		
		// Color combobox
		Map<Object, ColorIcon> icons = new HashMap<Object, ColorIcon>();
		color = new JComboBox();
		String[] strarray = ColorRepository.getColors();
		for(int k=0; k<strarray.length;k++){
			color.addItem(strarray[k]);
			icons.put(strarray[k], new ColorIcon(ColorRepository.getColor(strarray[k])));
		}
		color.setRenderer(new IconListRenderer(icons));
		
		// Behaviour combobox
		behaviour = new JComboBox();
		strarray = SystemConstants.FPGA_BEHAVIOURS;
		for(int k=0; k<strarray.length;k++){
			behaviour.addItem(strarray[k]);
		}
		
		// 
		devices = new JComboBox();
		strarray = SystemConstants.FPGA_BEHAVIOURS;
		for(int k=0; k<strarray.length;k++){
			behaviour.addItem(strarray[k]);
		}
        
		//spinmodel = new SpinnerNumberModel(numlasers, 0,Configuration.maxLasers, 1);
		
		// Define table
		DefaultTableModel model = new DefaultTableModel(new Object[] {
				"UI property", "Device", "Property" }, 0);
		model.addRow(new Object[] { "Enable power settings", Boolean.FALSE });
		//model.addRow(new Object[] { "Number of lasers", spinmodel });

		table = new JTable(model) {
			/**
			 * 
			 */
			private static final long serialVersionUID = -7528102943663023952L;

			@Override
			public TableCellRenderer getCellRenderer(int row, int column) {
				if (getValueAt(row, column) instanceof Boolean) { // if checkbox
					return super.getDefaultRenderer(Boolean.class);
				} else if (getValueAt(row, column) instanceof SpinnerNumberModel){ // if spinner
					return new SpinnerRenderer();
				} else if (column ==1 && ((String) getValueAt(row, 0)).contains(colorString)){ // if combobox corresponding to color
					return new IconTableRenderer(); // monochromatic icons instead of text 
				} else if (column ==1 && ((String) getValueAt(row, 0)).contains(behaviourString)){ // behaviour combobox
					return new DefaultTableCellRenderer(); // default
				} else if (column == 0 && ((String) getValueAt(row,column)).contains(nameString)){ // if the cell is the laser name property then bold
					return new BoldTableCellRenderer();
				}else {
					return super.getCellRenderer(row, column);
				}
			}

			@Override
			public TableCellEditor getCellEditor(int row, int column) {
				if (getValueAt(row, column) instanceof Boolean) {
					return super.getDefaultEditor(Boolean.class);
				} else if (getValueAt(row, column) instanceof SpinnerNumberModel){
					return new SpinnerEditor();
				} else if (column ==1 && ((String) getValueAt(row, 0)).contains(colorString)){
					return new DefaultCellEditor(color);
				} else if (column ==1 && ((String) getValueAt(row, 0)).contains(behaviourString)){
					return new DefaultCellEditor(behaviour);
				} else {
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
		table.getColumnModel().getColumn(1).setMaxWidth(70);
		table.getModel().addTableModelListener(new CheckBoxModelListener());
		
		
		JScrollPane sc = new JScrollPane(table);
		sc.setPreferredSize(new Dimension(230,590));
		this.add(sc);
	}
	
	/**
	 * Returns the number of lasers.
	 */
	public int getCountLaser(){
		return numlasers;
	}
	

	/**
	 * Add rows to the table corresponding to one laser.
	 */
	public void addLaser(){
		numlasers ++;
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		int number = numlasers-1;
		if(!powerenabled){
			model.addRow(new Object[] { "Laser"+number+" "+nameString, "Laser "+number });
			model.addRow(new Object[] { colorString, (String) color.getItemAt(2*numlasers-2)});
			model.addRow(new Object[] { behaviourString, (String) behaviour.getItemAt(0) });
		} else {
			model.addRow(new Object[] { "Laser"+number+" "+nameString, "Laser "+number });
			model.addRow(new Object[] { colorString, (String) color.getItemAt(2*numlasers-2)});
			model.addRow(new Object[] { behaviourString, (String) behaviour.getItemAt(0) });
			model.addRow(new Object[] { deviceString, "" });
			model.addRow(new Object[] { powerPropString, "" });
			model.addRow(new Object[] { operationPropString, "" });
		}
	}
	
	/**
	 * Remove the rows corresponding to the last laser.
	 */
	public void removeLaser(){
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		if(!powerenabled){
			if(model.getRowCount()==2+3*numlasers){
				model.removeRow(2+3*numlasers-1);
				model.removeRow(2+3*numlasers-2);
				model.removeRow(2+3*numlasers-3);
				numlasers --;
			}
		} else {
			if(model.getRowCount()==2+6*numlasers){
				model.removeRow(2+5*numlasers-1);
				model.removeRow(2+5*numlasers-2);
				model.removeRow(2+5*numlasers-3);
				model.removeRow(2+5*numlasers-4);
				model.removeRow(2+5*numlasers-5);
				model.removeRow(2+5*numlasers-6);
				numlasers --;
			}
		}
	}

	/**
	 * Add the device label, power property and on/off property fields as rows to all lasers.
	 */
	public void enablePower(){
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		
		grabLaserSettings();
		
		int start = model.getRowCount()-1;
		for(int i=start;i>1;i--){
			model.removeRow(i);
		}

		powerenabled = true;
		
		for(int i=0;i<numlasers;i++){
			model.addRow(new Object[] { "Laser"+i+" "+nameString, lasersettings.get(i).getName() });
			model.addRow(new Object[] { colorString, lasersettings.get(i).getColor()});
			model.addRow(new Object[] { behaviourString, lasersettings.get(i).getDefaultBehaviour()});
			model.addRow(new Object[] { deviceString, lasersettings.get(i).getDeviceLabel()});
			model.addRow(new Object[] { powerPropString, lasersettings.get(i).getPowerPercProp()});
			model.addRow(new Object[] { operationPropString, lasersettings.get(i).getOperationProp()});
		}
		
	}
	
	/**
	 * Remove the device label, power property and on/off property rows.
	 */
	public void disablePower(){
		powerenabled = false;
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		int maxcol= model.getRowCount()-1;
		for(int i=maxcol;i>=0;i--){
			if(((String) table.getValueAt(i, 0)).contains(deviceString) || ((String) table.getValueAt(i, 0)).contains(powerPropString)  ||
					((String) table.getValueAt(i, 0)).contains(operationPropString)){
				model.removeRow(i);
			}
		}
	}
	
	private 

	/**
	 * Spinner cell editor with a changed listener to monitor the number of lasers
	 */
	class SpinnerEditor extends AbstractCellEditor implements TableCellEditor {
		/**
		 * 
		 */
		private static final long serialVersionUID = -3697144687586881324L;
		protected JSpinner spinner;

		public SpinnerEditor() {
			spinner = new JSpinner();
			spinner.addChangeListener(new ChangeListener() {
		        @Override
		        public void stateChanged(ChangeEvent e) {
		            if((Integer) spinner.getValue()>getCountLaser()){
		            	addLaser();
		            } else if((Integer) spinner.getValue()<getCountLaser()){
		            	removeLaser();
		            }
		        }
		    });
		}

		public Component getTableCellEditorComponent(JTable table,
				Object value, boolean isSelected, int row, int column) {
			spinner.setModel((SpinnerModel) value);

			return spinner;
		}

		public Object getCellEditorValue() {
			SpinnerModel sm = spinner.getModel();
			return sm;
		}

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
             if (((String) table.getValueAt(row, 0)).contains(nameString)) {
            	 compo.setFont(compo.getFont().deriveFont(Font.BOLD));
            } else {  
            	compo.setFont(compo.getFont().deriveFont(Font.PLAIN));
            }

             return compo;
          }
	}
	
	/**
	 * Custom checkbox listener for the jtable cell.
	 */
    public class CheckBoxModelListener implements TableModelListener {

        public void tableChanged(TableModelEvent e) {
            int row = e.getFirstRow();
            int column = e.getColumn();
            if (column == 1 && row == 0) {
                TableModel model = (TableModel) e.getSource();
                Boolean checked = (Boolean) model.getValueAt(row, column);
                if (checked) {
                	enablePower();
                } else {
                	disablePower();
                }
            }
        }
    }
}