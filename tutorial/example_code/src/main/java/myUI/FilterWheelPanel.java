package main.java.myUI;

import javax.swing.JPanel;

import main.java.embl.rieslab.emu.ui.ConfigurablePanel;
import main.java.embl.rieslab.emu.ui.uiparameters.StringUIParameter;
import main.java.embl.rieslab.emu.ui.uiproperties.MultiStateUIProperty;
import main.java.embl.rieslab.emu.ui.uiproperties.flag.NoFlag;
import main.java.embl.rieslab.emu.utils.ColorRepository;

import javax.swing.border.TitledBorder;
import javax.swing.JToggleButton;
import javax.swing.ButtonGroup;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class FilterWheelPanel extends ConfigurablePanel {
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JToggleButton filterButton0;
	private JToggleButton filterButton1;
	private JToggleButton filterButton2;
	private JToggleButton filterButton3;
	private JToggleButton filterButton4;
	private JToggleButton filterButton5;


	//////// Properties
	public static String FW_POSITION = "Filterwheel position";
	
	//////// Parameters
	public final static String PARAM_NAMES = " names";
	public final static String PARAM_COLORS = " colors";
	
	//////// Initial parameter
	public final static int NUM_POS = 6;
	
	/**
	 * Create the panel.
	 */
	public FilterWheelPanel(String label) {
		super(label);
		setBorder(new TitledBorder(null, "Filter wheel", TitledBorder.LEFT, TitledBorder.TOP, null, null));
		setLayout(null);
		
		filterButton0 = new JToggleButton("Filter0");
		filterButton0.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==ItemEvent.SELECTED){
					String value = ((MultiStateUIProperty) getUIProperty(getLabel()+FW_POSITION)).getStateValue(0);
					setUIPropertyValue(getLabel()+FW_POSITION,value);				
				} 
			}
		});
		buttonGroup.add(filterButton0);
		filterButton0.setBounds(10, 21, 63, 23);
		add(filterButton0);
		
		filterButton1 = new JToggleButton("Filter1");
		filterButton1.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==ItemEvent.SELECTED){
					String value = ((MultiStateUIProperty) getUIProperty(getLabel()+FW_POSITION)).getStateValue(1);
					setUIPropertyValue(getLabel()+FW_POSITION,value);				
				} 
			}
		});
		buttonGroup.add(filterButton1);
		filterButton1.setBounds(72, 21, 63, 23);
		add(filterButton1);
		
		filterButton2 = new JToggleButton("Filter2");
		filterButton2.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==ItemEvent.SELECTED){
					String value = ((MultiStateUIProperty) getUIProperty(getLabel()+FW_POSITION)).getStateValue(2);
					setUIPropertyValue(getLabel()+FW_POSITION,value);				
				} 
			}
		});
		buttonGroup.add(filterButton2);
		filterButton2.setBounds(134, 21, 63, 23);
		add(filterButton2);
		
		filterButton3 = new JToggleButton("Filter3");
		filterButton3.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==ItemEvent.SELECTED){
					String value = ((MultiStateUIProperty) getUIProperty(getLabel()+FW_POSITION)).getStateValue(3);
					setUIPropertyValue(getLabel()+FW_POSITION,value);				
				} 
			}
		});
		buttonGroup.add(filterButton3);
		filterButton3.setBounds(196, 21, 63, 23);
		add(filterButton3);
		
		filterButton4 = new JToggleButton("Filter4");
		filterButton4.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==ItemEvent.SELECTED){
					String value = ((MultiStateUIProperty) getUIProperty(getLabel()+FW_POSITION)).getStateValue(4);
					setUIPropertyValue(getLabel()+FW_POSITION,value);				
				} 
			}
		});
		buttonGroup.add(filterButton4);
		filterButton4.setBounds(258, 21, 63, 23);
		add(filterButton4);
		
		filterButton5 = new JToggleButton("Filter5");
		filterButton5.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==ItemEvent.SELECTED){
					String value = ((MultiStateUIProperty) getUIProperty(getLabel()+FW_POSITION)).getStateValue(5);
					setUIPropertyValue(getLabel()+FW_POSITION,value);				
				} 
			}
		});
		buttonGroup.add(filterButton5);
		filterButton5.setBounds(320, 21, 63, 23);
		add(filterButton5);
	}

	@Override
	protected void initializeProperties() {
		addUIProperty(new MultiStateUIProperty(this, getLabel()+FW_POSITION,"Filter wheel position property.", new NoFlag(), NUM_POS));			
	}

	@Override
	protected void initializeInternalProperties() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initializeParameters() {
		// Build initial values
		String names = "None";
		String colors = "grey";
		for (int i = 0; i < NUM_POS - 1; i++) {
			names += "," + "None";
			colors += "," + "grey";
		}

		String helpNames = "Filter names displayed by the UI. The entry should be written as \"name1,name2,name3,None,None,None\". The names should be separated by a comma. "
						+ "The maximum number of filters name is " + NUM_POS
						+ ", beyond that the names will be ignored. If the comma are not present, then the entry will be set as the name of the first filter.";
		
		String helpColors = "Filter colors displayed by the UI. The entry should be written as \"color1,color2,color3,grey,grey,grey\". The names should be separated by a comma. "
						+ "The maximum number of filters color is " + NUM_POS
						+ ", beyond that the colors will be ignored. If the comma are not present, then no color will be allocated. The available colors are:\n"
						+ ColorRepository.getColorsInOneColumn();
				
		
		addUIParameter(new StringUIParameter(this, PARAM_NAMES, helpNames, names));
		addUIParameter(new StringUIParameter(this, PARAM_COLORS, helpColors, colors));
	}

	@Override
	public void internalpropertyhasChanged(String propertyName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void propertyhasChanged(String propertyName, String newvalue) {
		if(propertyName.equals(getLabel()+FW_POSITION)){
			int pos = ((MultiStateUIProperty) getUIProperty(getLabel()+FW_POSITION)).getStatePositionNumber(newvalue);
			if(pos == 0){
				filterButton0.setSelected(true);
			} else if(pos == 1){
				filterButton1.setSelected(true);
			} else if(pos == 2){
				filterButton2.setSelected(true);
			} else if(pos == 3){
				filterButton3.setSelected(true);
			} else if(pos == 4){
				filterButton4.setSelected(true);
			} else if(pos == 5){
				filterButton5.setSelected(true);
			}
		}
	}

	@Override
	protected void parameterhasChanged(String parameterName) {
		if(parameterName.equals(PARAM_NAMES)){
			setNames(getStringUIParameterValue(PARAM_NAMES));
		} else if(parameterName.equals(PARAM_COLORS)){
			setColors(getStringUIParameterValue(PARAM_COLORS));
		}
	}

	@Override
	public void shutDown() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	protected JToggleButton getFilterButton1() {
		return filterButton0;
	}
	protected JToggleButton getFilterButton2() {
		return filterButton1;
	}
	protected JToggleButton getFilterButton3() {
		return filterButton2;
	}
	protected JToggleButton getFilterButton4() {
		return filterButton3;
	}
	protected JToggleButton getFilterButton5() {
		return filterButton4;
	}
	protected JToggleButton getFilterButton6() {
		return filterButton5;
	}
	
	private void setNames(String names){
		// split names using the comma
		String[] astr = names.split(",");
		
		// select the maximum between NUM_POS and the length of astr
		// if astr is shorter than NUM_POS, then we take the length of astr and ignore the other indices
		// if astr is longer than NUM_POS then the user entered too any values, we only care for the NUM_POS first ones
		int maxind = NUM_POS > astr.length ? astr.length : NUM_POS-1;
		
		// For each button whose index is contained in astr, set the text according to the element in astr
		if(maxind >= 0) {
			filterButton0.setText(astr[0]);
		}
		
		if(maxind >= 1) {
			filterButton1.setText(astr[1]);
		}
		
		if(maxind >= 2) {
			filterButton2.setText(astr[2]);
		}
		
		if(maxind >= 3) {
			filterButton3.setText(astr[3]);
		}
		
		if(maxind >= 4) {
			filterButton4.setText(astr[4]);
		}
		
		if(maxind >= 5) {
			filterButton5.setText(astr[5]);
		}
	}
	
	private void setColors(String colors){
		String[] astr = colors.split(",");
		int maxind = NUM_POS > astr.length ? astr.length : NUM_POS-1;
		
		
		if(maxind >= 0) {
			filterButton0.setForeground(ColorRepository.getColor(astr[0]));
		}
		
		if(maxind >= 1) {
			filterButton1.setForeground(ColorRepository.getColor(astr[1]));
		}
		
		if(maxind >= 2) {
			filterButton2.setForeground(ColorRepository.getColor(astr[2]));
		}
		
		if(maxind >= 3) {
			filterButton3.setForeground(ColorRepository.getColor(astr[3]));
		}
		
		if(maxind >= 4) {
			filterButton4.setForeground(ColorRepository.getColor(astr[4]));
		}
		
		if(maxind >= 5) {
			filterButton5.setForeground(ColorRepository.getColor(astr[5]));
		}
		
	}
}
