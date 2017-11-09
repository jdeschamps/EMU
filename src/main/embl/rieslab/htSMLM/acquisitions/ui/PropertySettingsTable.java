package main.embl.rieslab.htSMLM.acquisitions.ui;

import java.awt.Component;
import java.awt.Font;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import main.embl.rieslab.htSMLM.ui.uiproperties.MultiStateUIProperty;
import main.embl.rieslab.htSMLM.ui.uiproperties.SingleStateUIProperty;
import main.embl.rieslab.htSMLM.ui.uiproperties.TwoStateUIProperty;
import main.embl.rieslab.htSMLM.ui.uiproperties.UIProperty;
import main.embl.rieslab.htSMLM.util.StringSorting;

public class PropertySettingsTable extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1610076898248090467L;
	
	private JTable table;
		
	private HashMap<String, UIProperty> uipropertySet_;
	private String[] uipropkeys_;
	private HashMap<String,String> friendlynames_;

	public PropertySettingsTable(HashMap<String, UIProperty> uipropertySet) {
		
		uipropertySet_ = uipropertySet;
		friendlynames_ = new HashMap<String,String>();

		// Filter out non allocated and read-only
		Iterator<String> it = uipropertySet_.keySet().iterator();
		String s;
		while(it.hasNext()){
			s = it.next();
			
			if(uipropertySet_.get(s).isAllocated() && !uipropertySet_.get(s).isMMPropertyReadOnly()){
				friendlynames_.put(uipropertySet_.get(s).getFriendlyName(), s);
			}
		}
		
		// Extracts uiproperties name from the map and sort them alphabetically
		uipropkeys_ = StringSorting.sort(friendlynames_.values().toArray(uipropkeys_));

		// Defines table model
		DefaultTableModel model = getDefaultModel();

		// For each property of the UI
		for (int i = 0; i < uipropkeys_.length; i++) {	
			if (uipropertySet_.get(uipropkeys_[i]).isTwoState()) {
				// if property is a toggle property, adds a line for the on and off values respectively.
				model.addRow(new Object[] {uipropertySet_.get(uipropkeys_[i]).getFriendlyName(),((TwoStateUIProperty) uipropertySet_.get(uipropkeys_[i])).getStatesName()[0] });
			} else if (uipropertySet_.get(uipropkeys_[i]).isSingleState()) {
				// if property is a single value property, adds a line for the value the property must take
				model.addRow(new Object[] {uipropertySet_.get(uipropkeys_[i]).getFriendlyName(),((SingleStateUIProperty) uipropertySet_.get(uipropkeys_[i])).getStateValue() });
			} else if (uipropertySet_.get(uipropkeys_[i]).isMultiState()) {
				model.addRow(new Object[] {uipropertySet_.get(uipropkeys_[i]).getFriendlyName(),((MultiStateUIProperty) uipropertySet_.get(uipropkeys_[i])).getStatesName()[0] });
			} else {
				model.addRow(new Object[] {uipropertySet_.get(uipropkeys_[i]).getFriendlyName(),uipropertySet_.get(uipropkeys_[i]).getPropertyValue() });
			}
		}

		// creates JTable
		createTable(model);

		JScrollPane sc = new JScrollPane(table);

		this.add(sc);
	}

	
	private DefaultTableModel getDefaultModel(){
		return new DefaultTableModel(new Object[] {"Property", "Value" }, 0);
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
				default:
					return super.getCellRenderer(row, column);
				}
			}

			@Override
			public TableCellEditor getCellEditor(int row, int column) {
				String s = (String) table.getValueAt(row, 0);
				
				if(column == 1){
					if (uipropertySet_.get(friendlynames_.get(s)).isTwoState()) {
						return new DefaultCellEditor(new JComboBox(((TwoStateUIProperty) uipropertySet_.get(friendlynames_.get(s))).getStatesName()));
					} else if (uipropertySet_.get(friendlynames_.get(s)).isMultiState()) { 
						return new DefaultCellEditor(new JComboBox(((MultiStateUIProperty) uipropertySet_.get(friendlynames_.get(s))).getStatesName()));
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
