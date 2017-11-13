package main.embl.rieslab.htSMLM.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JToggleButton;
import javax.swing.border.TitledBorder;

import main.embl.rieslab.htSMLM.ui.uiparameters.StringUIParameter;
import main.embl.rieslab.htSMLM.ui.uiproperties.MultiStateUIProperty;
import main.embl.rieslab.htSMLM.ui.uiproperties.PropertyFlag;
import main.embl.rieslab.htSMLM.util.ColorRepository;

public class FiltersPanel extends PropertyPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8562433353787092702L;

	//////// Components
	private JToggleButton[] togglebuttons_;
	
	//////// Properties
	public static String FW_POSITION = "Filter wheel position";
	
	//////// Parameters
	public static String PARAM_NAMES = "Filters name";
	public static String PARAM_COLORS = "Filters color";
	
	//////// Initial parameters
	public static int NUM_POS = 6;
	public static String NAME_EMPTY = "None";
	public static String COLOR_EMPTY = ColorRepository.strgray;
	String names_, colors_; 


	public FiltersPanel(String label) {
		super(label);
	}
	
	@Override
	public void setupPanel() {
		this.setLayout(new GridBagLayout());
		this.setBorder(BorderFactory.createTitledBorder(null, getLabel(), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, new Color(0,0,0)));
		((TitledBorder) this.getBorder()).setTitleFont(((TitledBorder) this.getBorder()).getTitleFont().deriveFont(Font.BOLD, 12));

		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weighty = 0.9;
		c.weightx = 0.7;
		c.gridy = 0;
		c.insets = new Insets(2,0,2,0);

		ButtonGroup group=new ButtonGroup();

		togglebuttons_ = new JToggleButton[NUM_POS];
		for(int i=0;i<togglebuttons_.length;i++){
			togglebuttons_[i] = new JToggleButton();
			
			c.gridx = i;
			this.add(togglebuttons_[i], c);
			
			group.add(togglebuttons_[i]);
			
			togglebuttons_[i].addItemListener(new ItemListener(){
				@Override
				public void itemStateChanged(ItemEvent e) {
					if(e.getStateChange()==ItemEvent.SELECTED){
						int pos = getSelectedButtonNumber();
						if(pos>=0 && pos<togglebuttons_.length){
							changeProperty(FW_POSITION,getValueFromPosition(pos));
						}				
					} 
				}
	        });
		}  
		setNames();
		setColors();
	}

	protected int getSelectedButtonNumber() {
		int val=-1;
		
		for(int i=0;i<togglebuttons_.length;i++){
			if(togglebuttons_[i].isSelected()){
				return i;
			}
		}
		return val;
	}
	
	private void setNames(){
		String[] astr = names_.split(",");
		int maxind = togglebuttons_.length > astr.length ? astr.length : togglebuttons_.length;
		for(int i=0;i<maxind;i++){
			togglebuttons_[i].setText(astr[i]);
		}
		((MultiStateUIProperty) getUIProperty(FW_POSITION)).setStatesName(astr);
	}
	
	private void setColors(){
		String[] astr = colors_.split(",");
		int maxind = togglebuttons_.length > astr.length ? astr.length : togglebuttons_.length;
		for(int i=0;i<maxind;i++){
			togglebuttons_[i].setForeground(ColorRepository.getColor(astr[i]));
		}
	}
	
	
	protected String getValueFromPosition(int pos){
		return ((MultiStateUIProperty) getUIProperty(FW_POSITION)).getStateValue(pos);
	}
	
	@Override
	protected void initializeProperties() {
		addUIProperty(new MultiStateUIProperty(this, FW_POSITION,"Filter wheel position property.", PropertyFlag.FILTERWHEEL,NUM_POS));		
	}

	@Override
	protected void initializeParameters() {
		names_ = NAME_EMPTY;
		colors_ = COLOR_EMPTY;
		for(int i=0;i<NUM_POS-1;i++){
			names_ += ","+NAME_EMPTY; 
			colors_ += ","+COLOR_EMPTY; 
		}
		
		addUIParameter(new StringUIParameter(this, PARAM_NAMES,"Filter names displayed by the UI. The entry should be written as \"name1,name2,name3,None,None,None\". The names should be separated by a comma. "
				+ "The maximum number of filters name is "+NUM_POS+", beyond that the names will be ignored. If the comma are not present, then the entry will be set as the name of the first filter.",names_));
		addUIParameter(new StringUIParameter(this, PARAM_COLORS,"Filter colors displayed by the UI. The entry should be written as \"color1,color2,color3,grey,grey,grey\". The names should be separated by a comma. "
				+ "The maximum number of filters color is "+NUM_POS+", beyond that the colors will be ignored. If the comma are not present, then no color will be allocated. The available colors are:\n"+ColorRepository.getColorsInOneColumn(),colors_));
	}
	
	@Override
	protected void changeProperty(String name, String value) {
		if(name.equals(FW_POSITION)){
			getUIProperty(name).setPropertyValue(value);
		}		
	}

	@Override
	public void propertyhasChanged(String name, String newvalue) {
		if(name.equals(FW_POSITION)){
			int pos = ((MultiStateUIProperty) getUIProperty(FW_POSITION)).getStatePositionNumber(newvalue);
			if(pos<togglebuttons_.length){
				togglebuttons_[pos].setSelected(true);
			}
		}
	}

	@Override
	public void parameterhasChanged(String label) {
		if(label.equals(PARAM_NAMES)){
			names_ = ((StringUIParameter) getUIParameter(PARAM_NAMES)).getValue();
			setNames();
		} else if(label.equals(PARAM_COLORS)){
			colors_ = ((StringUIParameter) getUIParameter(PARAM_COLORS)).getValue();
			setColors();
		}
	}

	@Override
	public void shutDown() {
		// do nothing
	}

	@Override
	public String getDescription() {
		return "The "+getLabel()+" panel should be liked to te filterwheel and allows the contol of up to "+NUM_POS+" filters. The colors and names can bu customized from the configuration menu.";
	}

	@Override
	protected void initializeInternalProperties() {
		// Do nothing
	}

	@Override
	protected void changeInternalProperty(String name, String value) {
		// Do nothing
	}

	@Override
	public void internalpropertyhasChanged(String label) {
		// Do nothing
	}

}
