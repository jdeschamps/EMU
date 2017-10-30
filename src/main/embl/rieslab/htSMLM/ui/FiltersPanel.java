package main.embl.rieslab.htSMLM.ui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JToggleButton;

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
	public static String PARAM_NPOS = "XY max";
	public static String PARAM_COLORS = "XY max";

	
	//////// Default parameters
	private int idle_, xymax_, zmax_; 
	
	public FiltersPanel(String label) {
		super(label);
	}
	
	@Override
	public void setupPanel() {
		this.setLayout(new GridBagLayout());
		this.setBorder(BorderFactory.createTitledBorder(null, getParameter("Label").getValues()[0], javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, new Color(0,0,0)));
	
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weighty = 0.9;
		c.weightx = 0.7;
		c.gridy = 0;
		c.insets = new Insets(2,0,2,0);

		ButtonGroup group=new ButtonGroup();

		togglebuttons_ = new JToggleButton[Integer.parseInt(getParameter("Button number").getValues()[0])];
		for(int i=0;i<togglebuttons_.length;i++){
			togglebuttons_[i] = new JToggleButton();
			togglebuttons_[i].setForeground(Colors.getColor(getParameter("Color").getValues()[i]));
			togglebuttons_[i].setText(getParameter("Button name").getValues()[i]);
			
			c.gridx = i;
			this.add(togglebuttons_[i], c);
			
			group.add(togglebuttons_[i]);
			
			togglebuttons_[i].addItemListener(new ItemListener(){
				@Override
				public void itemStateChanged(ItemEvent e) {
					if(e.getStateChange()==ItemEvent.SELECTED){
						int pos = getSelectedButtonNumber();
						if(pos>=0 && pos<togglebuttons_.length){
							setProperty("Position",getParameter("Position").getValues()[pos]);
						}				
					} 
				}
	        });
		}  
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
	

	@Override
	public HashMap<String, Parameter> buildDefaultParameters() {
		HashMap<String,Parameter> defparam = new HashMap<String,Parameter>();

		String[] defaultlabelval = {"Filter wheel"};
		defparam.put("Label", new Parameter("Label",defaultlabelval,PropertyType.String,true));
		String[] defaultbuttonsnumberval = {"6"};
		defparam.put("Button number", new Parameter("Button number",defaultbuttonsnumberval,PropertyType.Integer,true));
		String[] defaultcolorval = {"cyan","green","red","gray","yellow","blue"};
		defparam.put("Color", new Parameter("Color",defaultcolorval,PropertyType.String,false));
		String[] defaultbuttonsnameval = {"F1","F2","F2","F2","F2","F2"};
		defparam.put("Button name",new Parameter("Button name",defaultbuttonsnameval,PropertyType.String,false));
		String[] defaultposition = {"100","200","300","400","500","600"};
		defparam.put("Position",new Parameter("Filter position",defaultposition,PropertyType.Integer,false));
		
		return defparam;
	}

	@Override
	public boolean checkParameters(HashMap<String, Parameter> param) {
		if(param.size() == 5){
			int n = 0;
			if(param.containsKey("Button number")){
				if(!getParameter("Button number").isValueAllowed(param.get("Button number").getValues())){
					return false;
				}
				n = Integer.valueOf((param.get("Button number").getValues())[0]);
			} else {
				return false;
			}
			
		    Iterator<Entry<String, Parameter>> it = getParameters().entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry<String, Parameter> pair = (Map.Entry<String, Parameter>)it.next();
				if(!Parameter.testProperty(param,pair.getValue(),pair.getKey(),n)){
					return false;
				}
		        it.remove(); // avoids a ConcurrentModificationException
		    }
		    return true;
		}
		return false;
	}

	@Override
	public void propertyChanged(String ID) {
		if(ID.equals("Position")){
			String val  = getProperty("Position").getValue();
			String[] positions = getParameter("Position").getValues();
			for(int i=0;i<positions.length;i++){
				if(positions[i].equals(val)){
					togglebuttons_[i].setSelected(true);
				}
			}
		}
	}

	
	@Override
	protected void initializeProperties() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initializeParameters() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void changeProperty(String name, String value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propertyhasChanged(String name, String newvalue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void parameterhasChanged(String label) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void shutDown() {
		// TODO Auto-generated method stub
		
	}

}
