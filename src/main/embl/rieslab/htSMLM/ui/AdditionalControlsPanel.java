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

import main.de.embl.MicroInterface.dataflow.IntPropertyInterface;
import main.de.embl.MicroInterface.dataflow.UIPanel;
import main.de.embl.MicroInterface.dataflow.Parameter;
import main.de.embl.MicroInterface.utils.Colors;
import mmcorej.PropertyType;

public class AdditionalControlsPanel extends PropertyPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6559849050710175913L;
	
	
	@Override
	public void initComponents() {
		this.setLayout(new GridBagLayout());
		this.setBorder(BorderFactory.createTitledBorder(null, getParameter("Label").getValues()[0], javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, new Color(0,0,0)));
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weighty = 0.9;
		c.weightx = 0.7;
		c.gridx = 0;
		c.insets = new Insets(2,0,2,0);
	
		ButtonGroup group=new ButtonGroup();
	
		togglebuttons_ = new JToggleButton[Integer.parseInt(getParameter("Button number").getValues()[0])];
		for(int i=0;i<togglebuttons_.length;i++){
			togglebuttons_[i] = new JToggleButton();
			togglebuttons_[i].setForeground(Colors.getColor(getParameter("Color").getValues()[i]));
			togglebuttons_[i].setText(getParameter("Button name").getValues()[i]);
			
			c.gridy = i;
			this.add(togglebuttons_[i], c);
			
			group.add(togglebuttons_[i]);
			
			togglebuttons_[i].addItemListener(new ItemListener(){
				@Override
				public void itemStateChanged(ItemEvent e) {
					if(e.getStateChange()==ItemEvent.SELECTED){
						int pos = getButtonNumber((JToggleButton) e.getItem());
						if(pos>=0 && pos<togglebuttons_.length){
							setProperty(getParameter("Button name").getValues()[pos],getParameter("On position").getValues()[pos]);
						}
					} else if (e.getStateChange()==ItemEvent.DESELECTED){
						int pos = getButtonNumber((JToggleButton) e.getItem());
						if(pos>=0 && pos<togglebuttons_.length){
							setProperty(getParameter("Button name").getValues()[pos],getParameter("Off position").getValues()[pos]);
						}
					} 
				}
	       });
		}  
	}
	
	private int getButtonNumber(JToggleButton button){
		int pos = -1;
		for(int i=0; i<togglebuttons_.length;i++){
			if(togglebuttons_[i].equals(button)){
				pos = i;
			}
		}
		return pos;
	}
	
	//////// Components
	private JToggleButton[] togglebuttons_;

	@Override
	public HashMap<String, Parameter> buildDefaultParameters() {
		HashMap<String, Parameter> defparam = new HashMap<String, Parameter>();

		String[] defaultlabelval = {"Servos"};
		defparam.put("Label",new Parameter("Label",defaultlabelval,PropertyType.String,true));
		String[] defaultbuttonsnumberval = {"2"};
		defparam.put("Button number",new Parameter("Button number",defaultbuttonsnumberval,PropertyType.Integer,false));
		String[] defaultbuttonsnameval = {"BFP","3DA"};
		defparam.put("Button names",new Parameter("Button name",defaultbuttonsnameval,PropertyType.String,true));
		String[] defaultcolorval = {"blue","green"};
		defparam.put("Color",new Parameter("Color",defaultcolorval,PropertyType.String,false));
		String[] defaultpositionOn = {"100","100"};
		defparam.put("ON position",new Parameter("ON position",defaultpositionOn,PropertyType.Integer,false));
		String[] defaultpositionOff = {"10","10"};
		defparam.put("OFF position",new Parameter("OFF position",defaultpositionOff,PropertyType.Integer,false));
		
		return defparam;
	}


	@Override
	public void propertyChanged(String ID) {
		String[] names = getParameterValue("Button name");
		for(int i=0;i<names.length;i++){
			if(ID.equals(names[i])){
				int val  = ((IntPropertyInterface) getProperty(ID)).getCastValue();
				if(val == 1){
					togglebuttons_[i].setSelected(true);
				} else{
					togglebuttons_[i].setSelected(false);
				}
			}	
		}	
	}

	@Override
	protected void createProperties() {
		String[] names = getParameterValue("Button name");
		for(int i=0;i<names.length;i++){
			addProperty(new IntPropertyInterface(names[i],names[i],this));
		}
	}

	@Override
	public boolean checkParameters(HashMap<String, Parameter> param) {
		if(param.size() == 6){
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
}