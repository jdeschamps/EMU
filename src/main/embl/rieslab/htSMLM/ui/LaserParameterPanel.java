package main.embl.rieslab.htSMLM.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import main.de.embl.MicroInterface.dataflow.BooleanParameter;
import main.de.embl.MicroInterface.dataflow.IntPropertyInterface;
import main.de.embl.MicroInterface.dataflow.UIPanel;
import main.de.embl.MicroInterface.dataflow.Parameter;
import main.de.embl.MicroInterface.utils.Colors;
import main.de.embl.MicroInterface.utils.utils;
import mmcorej.PropertyType;

public class LaserParameterPanel extends UIPanel {

	private static final long serialVersionUID = -6553153910855055671L;
	
	@Override
	public void initComponents() {
		
		this.setLayout(new GridBagLayout());
		this.setBorder(BorderFactory.createTitledBorder(null, (getParameter("Label").getValues())[0], javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, 
				null, Colors.getColor(getParameter("Color").getValues()[0])));
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.weighty = 0.9;
		c.weightx = 0.7;
		c.insets = new Insets(2,15,2,15);
		
		///////////////////////////////////////////////////////////////////////// User input text field
		//TODO
	}
	
	private String getUserInput(){
		return null;
	}
	
	//////// Components
	private JLabel labelbehaviour_;
	private JLabel labelpulselength_;
	private JLabel labelmaxpower_;	
	private JComboBox combobehaviour_;	
	private JTextField textfieldpulselength_;
	private JTextField textfieldmaxpower_;
	private JTextField textfieldsequence_;
	private JSlider sliderpulse_;


	@Override
	public HashMap<String, Parameter> buildDefaultParameters() {
		HashMap<String, Parameter> defparam = new HashMap<String, Parameter>();
		
		String[] defaultlabelval = {"Laser"};
		defparam.put("Label",new Parameter("Label",defaultlabelval,PropertyType.String,true));
		String[] defaultcolorval = {"40","58","150"};
		defparam.put("Color",new Parameter("Color",defaultcolorval,PropertyType.String,true));
		String[] defaultenablemaxpower = {"0"};
		defparam.put("Enable max power", new BooleanParameter("Enable max power field",defaultenablemaxpower,true));
		String[] defaultmaxpowerval = {"300"};
		defparam.put("Max power", new BooleanParameter("Max power value",defaultmaxpowerval,true));
		
		return defparam;
	}



	@Override
	public void propertyChanged(String ID) {
		
	}

	@Override
	protected void createProperties() {
		addProperty(new IntPropertyInterface("Laser Power","Laser Power",this));
		addProperty(new IntPropertyInterface("Laser On/Off","Laser On/Off",this));
		addProperty(new IntPropertyInterface("Maximum power","Maximum power",this));
	}

	@Override
	public boolean checkParameters(HashMap<String, Parameter> param) {
		if(param.size() == 4){
			//TODO
		}
		return false;
	}
}
