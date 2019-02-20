package main.java.embl.rieslab.emu.uiexamples.htsmlm;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.border.TitledBorder;

import main.java.embl.rieslab.emu.ui.ConfigurablePanel;
import main.java.embl.rieslab.emu.ui.uiparameters.StringUIParameter;
import main.java.embl.rieslab.emu.ui.uiproperties.MultiStateUIProperty;
import main.java.embl.rieslab.emu.uiexamples.htsmlm.flags.FilterWheelFlag;
import main.java.embl.rieslab.emu.utils.ColorRepository;

public class AdditionalFiltersPanel extends ConfigurablePanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2425461439930227137L;
	
	//////// Components
	private JToggleButton[] togglebuttons1_;
	private JToggleButton[] togglebuttons2_;
	private TitledBorder border1_;
	private TitledBorder border2_;
	
	//////// Properties
	public static String SLIDER1_POSITION = "Slider 1 position";
	public static String SLIDER2_POSITION = "Slider 2 position";
	
	//////// Parameters
	public final static String PARAM1_NAMES = "Slider 1 names";
	public final static String PARAM1_COLORS = "Slider 1 colors";
	public final static String PARAM2_NAMES = "Slider 2 names";
	public final static String PARAM2_COLORS = "Slider 2 colors";
	public final static String PARAM1_TITLE = "Slider 1 title";
	public final static String PARAM2_TITLE = "Slider 2 title";
	
	//////// Initial parameters
	public final static int NUM_POS = 4;
	public final static String NAME_EMPTY = "None";
	public final static String COLOR_EMPTY = ColorRepository.strgray;
	String names1_, colors1_, names2_, colors2_, title1_, title2_; 


	public AdditionalFiltersPanel(String label) {
		super(label);
	}
	
	@Override
	public void setupPanel() {
		JPanel pane1 = new JPanel();
		pane1.setLayout(new GridBagLayout());
		
		JPanel pane2 = new JPanel();
		pane2.setLayout(new GridBagLayout());
		
		pane1.setLayout(new GridBagLayout());
		pane2.setLayout(new GridBagLayout());

		border1_ = BorderFactory.createTitledBorder(null, title1_, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, Color.black);
		pane1.setBorder(border1_);
		border1_.setTitleFont(border1_.getTitleFont().deriveFont(Font.BOLD, 12));

		border2_ = BorderFactory.createTitledBorder(null, title2_, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, Color.black);
		pane2.setBorder(border2_);
		border2_.setTitleFont(border2_.getTitleFont().deriveFont(Font.BOLD, 12));
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weighty = 0.9;
		c.weightx = 0.7;
		c.gridy = 0;
		c.insets = new Insets(2,0,2,0);

		ButtonGroup group1=new ButtonGroup();
		ButtonGroup group2=new ButtonGroup();

		togglebuttons1_ = new JToggleButton[NUM_POS];
		for(int i=0;i<togglebuttons1_.length;i++){
			togglebuttons1_[i] = new JToggleButton();
			
			c.gridx = i;
			pane1.add(togglebuttons1_[i], c);
			
			group1.add(togglebuttons1_[i]);
			
			togglebuttons1_[i].addItemListener(new ItemListener(){
				@Override
				public void itemStateChanged(ItemEvent e) {
					if(e.getStateChange()==ItemEvent.SELECTED){
						int pos = getSelectedButtonNumber(togglebuttons1_);
						if(pos>=0 && pos<togglebuttons1_.length){
							setUIPropertyValue(SLIDER1_POSITION,getSlider1ValueFromPosition(pos));
						}				
					} 
				}
	        });
		}  

		c.gridy = 1;
		togglebuttons2_ = new JToggleButton[NUM_POS];
		for(int i=0;i<togglebuttons2_.length;i++){
			togglebuttons2_[i] = new JToggleButton();
			
			c.gridx = i;
			pane2.add(togglebuttons2_[i], c);
			
			group2.add(togglebuttons2_[i]);
			
			togglebuttons2_[i].addItemListener(new ItemListener(){
				@Override
				public void itemStateChanged(ItemEvent e) {
					if(e.getStateChange()==ItemEvent.SELECTED){
						int pos = getSelectedButtonNumber(togglebuttons2_);
						if(pos>=0 && pos<togglebuttons2_.length){
							setUIPropertyValue(SLIDER2_POSITION,getSlider2ValueFromPosition(pos));
						}				
					} 
				}
	        });
		}  

		setNames(0);
		setColors(0);
		setNames(1);
		setColors(1);

		////////////////////////////////////////////////////////////////////////////Main panel
		this.setLayout(new GridBagLayout());
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.4;
		c.weighty = 0.2;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		c.gridheight = 1;
		this.add(pane1,c);
		
		c.gridx = 0;
		c.gridy = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(pane2,c);
		
		c.gridx = 0;
		c.gridy = 2;
		c.weighty = 0.8;
		c.fill = GridBagConstraints.BOTH;
		this.add(new JPanel(),c);
	}

	protected int getSelectedButtonNumber(JToggleButton[] togglebuttons) {
		int val=-1;
		
		for(int i=0;i<togglebuttons.length;i++){
			if(togglebuttons[i].isSelected()){
				return i;
			}
		}
		return val;
	}
	
	private void setNames(int j){
		if(j == 0){
			String[] astr = names1_.split(",");
			int maxind = togglebuttons1_.length > astr.length ? astr.length : togglebuttons1_.length;
			for(int i=0;i<maxind;i++){
				togglebuttons1_[i].setText(astr[i]);
			}
			((MultiStateUIProperty) getUIProperty(SLIDER1_POSITION)).setStatesName(astr);
		} else if(j==1){	
			String[] astr = names2_.split(",");
			int maxind = togglebuttons2_.length > astr.length ? astr.length : togglebuttons2_.length;
			for(int i=0;i<maxind;i++){
				togglebuttons2_[i].setText(astr[i]);
			}
			((MultiStateUIProperty) getUIProperty(SLIDER2_POSITION)).setStatesName(astr);
		}
	}
	
	private void setColors(int j){
		if(j == 0){
			String[] astr = colors1_.split(",");
			int maxind = togglebuttons1_.length > astr.length ? astr.length : togglebuttons1_.length;
			for(int i=0;i<maxind;i++){
				togglebuttons1_[i].setForeground(ColorRepository.getColor(astr[i]));
			}
		} else if(j==1){	
			String[] astr = colors2_.split(",");
			int maxind = togglebuttons2_.length > astr.length ? astr.length : togglebuttons2_.length;
			for(int i=0;i<maxind;i++){
				togglebuttons2_[i].setForeground(ColorRepository.getColor(astr[i]));
			}
		}
	}
	

	protected String getSlider1ValueFromPosition(int pos){
		return ((MultiStateUIProperty) getUIProperty(SLIDER1_POSITION)).getStateValue(pos);
	}

	protected String getSlider2ValueFromPosition(int pos){
		return ((MultiStateUIProperty) getUIProperty(SLIDER2_POSITION)).getStateValue(pos);
	}
	
	@Override
	protected void initializeProperties() {
		addUIProperty(new MultiStateUIProperty(this, SLIDER1_POSITION, "Slider1 position property.", new FilterWheelFlag(),NUM_POS));		
		addUIProperty(new MultiStateUIProperty(this, SLIDER2_POSITION, "Slider2 wheel position property.", new FilterWheelFlag(),NUM_POS));		
	}

	@Override
	protected void initializeParameters() {
		names1_ = NAME_EMPTY;
		colors1_ = COLOR_EMPTY;
		names2_ = NAME_EMPTY;
		colors2_ = COLOR_EMPTY;
		for(int i=0;i<NUM_POS-1;i++){
			names1_ += ","+NAME_EMPTY; 
			colors1_ += ","+COLOR_EMPTY; 
			names2_ += ","+NAME_EMPTY; 
			colors2_ += ","+COLOR_EMPTY; 
		}
		title1_ = "Slider 1";
		title2_ = "Slider 2";

		addUIParameter(new StringUIParameter(this, PARAM1_TITLE, "Title of the first set of additional filters.",title1_));
		addUIParameter(new StringUIParameter(this, PARAM2_TITLE, "Title of the second set of additional filters.",title2_));
		addUIParameter(new StringUIParameter(this, PARAM1_NAMES,"Filter names displayed by the UI. The entry should be written as \"name1,name2,name3,None,None,None\". The names should be separated by a comma. "
				+ "The maximum number of filters name is "+NUM_POS+", beyond that the names will be ignored. If the comma are not present, then the entry will be set as the name of the first filter.",names1_));
		addUIParameter(new StringUIParameter(this, PARAM1_COLORS,"Filter colors displayed by the UI. The entry should be written as \"color1,color2,color3,grey,grey,grey\". The names should be separated by a comma. "
				+ "The maximum number of filters color is "+NUM_POS+", beyond that the colors will be ignored. If the comma are not present, then no color will be allocated. The available colors are:\n"+ColorRepository.getColorsInOneColumn(),colors1_));
		addUIParameter(new StringUIParameter(this, PARAM2_NAMES,"Filter names displayed by the UI. The entry should be written as \"name1,name2,name3,None,None,None\". The names should be separated by a comma. "
				+ "The maximum number of filters name is "+NUM_POS+", beyond that the names will be ignored. If the comma are not present, then the entry will be set as the name of the first filter.",names2_));
		addUIParameter(new StringUIParameter(this, PARAM2_COLORS,"Filter colors displayed by the UI. The entry should be written as \"color1,color2,color3,grey,grey,grey\". The names should be separated by a comma. "
				+ "The maximum number of filters color is "+NUM_POS+", beyond that the colors will be ignored. If the comma are not present, then no color will be allocated. The available colors are:\n"+ColorRepository.getColorsInOneColumn(),colors2_));
	}

	@Override
	public void propertyhasChanged(String name, String newvalue) {
		if(name.equals(SLIDER1_POSITION)){
			int pos = ((MultiStateUIProperty) getUIProperty(SLIDER1_POSITION)).getStatePositionNumber(newvalue);
			if(pos<togglebuttons1_.length){
				togglebuttons1_[pos].setSelected(true);
			}
		} else if(name.equals(SLIDER1_POSITION)){
			int pos = ((MultiStateUIProperty) getUIProperty(SLIDER1_POSITION)).getStatePositionNumber(newvalue);
			if(pos<togglebuttons2_.length){
				togglebuttons2_[pos].setSelected(true);
			}
		}
	}

	@Override
	public void parameterhasChanged(String label) {
		if(label.equals(PARAM1_NAMES)){
			names1_ = getStringUIParameterValue(PARAM1_NAMES);
			setNames(0);
		} else if(label.equals(PARAM1_COLORS)){
			colors1_ = getStringUIParameterValue(PARAM1_COLORS);
			setColors(0);
		} else if(label.equals(PARAM2_NAMES)){
			names2_ = getStringUIParameterValue(PARAM2_NAMES);
			setNames(1);
		} else if(label.equals(PARAM2_COLORS)){
			colors2_ = getStringUIParameterValue(PARAM2_COLORS);
			setColors(1);
		} else if(label.equals(PARAM1_TITLE)){
			title1_ = getStringUIParameterValue(PARAM1_TITLE);
			border1_.setTitle(title1_);
			this.repaint();
		} else if(label.equals(PARAM2_TITLE)){
			title2_ = getStringUIParameterValue(PARAM2_TITLE);
			border2_.setTitle(title2_);
			this.repaint();
		}
	}

	@Override
	public void shutDown() {
		// do nothing
	}

	@Override
	public String getDescription() {
		return "The "+getLabel()+" panel should be liked to the sliders and allows the control of up to "+NUM_POS+" filters. The colors and names can bu customized from the configuration menu.";
	}

	@Override
	protected void initializeInternalProperties() {
		// Do nothing
	}

	@Override
	public void internalpropertyhasChanged(String label) {
		// Do nothing
	}

}
