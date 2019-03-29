package main.java.myUI;

import javax.swing.JPanel;

import main.java.embl.rieslab.emu.ui.ConfigurablePanel;
import main.java.embl.rieslab.emu.ui.uiparameters.ColorUIParameter;
import main.java.embl.rieslab.emu.ui.uiparameters.StringUIParameter;
import main.java.embl.rieslab.emu.ui.uiproperties.TwoStateUIProperty;
import main.java.embl.rieslab.emu.ui.uiproperties.UIProperty;
import main.java.embl.rieslab.emu.ui.uiproperties.flag.NoFlag;
import main.java.embl.rieslab.emu.utils.utils;

import javax.swing.border.TitledBorder;
import javax.swing.JLabel;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JToggleButton;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class LaserPanel extends ConfigurablePanel {


	//////// Properties
	public final static String LASER_PERCENTAGE = "power percentage";
	public final static String LASER_OPERATION = "enable";
	private JLabel laserPercentageLabel;
	private JSlider laserPercentageSlider;
	private JToggleButton laserOnOffButton;

	//////// Parameters
	public final static String PARAM_TITLE = "Name";
	public final static String PARAM_COLOR = "Color";	


	/**
	 * Create the panel.
	 */
	public LaserPanel(String label) {
		super(label);
		setBorder(new TitledBorder(null, "Laser", TitledBorder.LEFT, TitledBorder.TOP, null, null));
		setLayout(null);
		
		laserPercentageLabel = new JLabel("50%");
		laserPercentageLabel.setHorizontalAlignment(SwingConstants.CENTER);
		laserPercentageLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		laserPercentageLabel.setBounds(22, 26, 71, 14);
		add(laserPercentageLabel);
		
		laserPercentageSlider = new JSlider();
		laserPercentageSlider.setMajorTickSpacing(20);
		laserPercentageSlider.setPaintTicks(true);
		laserPercentageSlider.setPaintLabels(true);
		laserPercentageSlider.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				// Retrieves the new value of the JSlider
				int val = laserPercentageSlider.getValue();
				
				// Sets the property to this value (remember to use the same name as in the
				// declaration)
				setUIPropertyValue(getLabel() + " " + LASER_PERCENTAGE, String.valueOf(val));

				// let's also modify the JLabel!
				laserPercentageLabel.setText(String.valueOf(val) + "%");

			}
		});
		
		laserPercentageSlider.setOrientation(SwingConstants.VERTICAL);
		laserPercentageSlider.setBounds(22, 51, 71, 163);
		add(laserPercentageSlider);
		
		laserOnOffButton = new JToggleButton("On/Off");
		laserOnOffButton.setFont(new Font("Tahoma", Font.BOLD, 11));
		laserOnOffButton.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if (arg0.getStateChange() == ItemEvent.SELECTED) { // if the JToggleButton was selected

					// sets the UIProperty to the On state (without any prior on what this
					// state is)
					// since we declared the UIProperty as a TwoStateUIProperty, we know
					// this will work out!
					setUIPropertyValue(getLabel() + " " + LASER_OPERATION, TwoStateUIProperty.getOnStateName());
				} else if (arg0.getStateChange() == ItemEvent.DESELECTED) {// if the JToggleButton was deselected
					setUIPropertyValue(getLabel() + " " + LASER_OPERATION, TwoStateUIProperty.getOffStateName());
				}
			}
		});
		laserOnOffButton.setBounds(22, 219, 71, 41);
		add(laserOnOffButton);
	}


	@Override
	protected void initializeProperties() {	

		// description of the LASER_PERCENTAGE property
		// (this will be displayed in the help window of EMU)
		String text1 = "Property changing the percentage of the laser.";

		// description of the LASER_OPERATION property
		String text2 = "Property turning the laser on and off.";

		// Adds a new property called LASER_PERCENTAGE:
		// new UIProperty(<ConfigurablePanel that created it>, <Name of the property>, <Description>, <FLag>)
		// Since we might have several lasers, we want the property to have a unique name,
		// therefore we use the label of the ConfigurablePanel in the name. The NoFlag call
		// is beyond the scope of this tutorial.
		addUIProperty(new UIProperty(this, getLabel() + " " + LASER_PERCENTAGE, text1, new NoFlag()));

		// Using <getLabel()+” ”+LASER_PERCENTAGE> here means that EVERY time we need to call the
		// UIProperty we should use the same expression!

		// We do the same with LASER_OPERATION, albeit with a different type of property.
		// Since on/off is only “on” or “off”, we ensure that this will always be the
		// case by making it a two-state property.
		addUIProperty(new TwoStateUIProperty(this, getLabel() + " " + LASER_OPERATION, text2, new NoFlag()));
	}

	@Override
	protected void initializeInternalProperties() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initializeParameters() {
		addUIParameter(new StringUIParameter(this, PARAM_TITLE, "Panel title.",getLabel()));
		addUIParameter(new ColorUIParameter(this, PARAM_COLOR, "Laser color.",Color.black));

	}

	@Override
	public void internalpropertyhasChanged(String propertyName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void propertyhasChanged(String propertyName, String newvalue) {
		if (propertyName.equals(getLabel() + " " + LASER_PERCENTAGE)) {
			if (utils.isNumeric(newvalue)) {
				// rounds it up
				int val = (int) Double.parseDouble(newvalue);

				if (val >= 0 && val < 100) {
					laserPercentageSlider.setValue(val);
					laserPercentageLabel.setText(String.valueOf(val) + "%");
				}
			}
		} else if (propertyName.equals(getLabel() + " " + LASER_OPERATION)) {

			// get the value of the On state of the LASER_OPERATION UIProperty 
			String onValue = ((TwoStateUIProperty) getUIProperty(getLabel() + " " + LASER_OPERATION)).getOnStateValue();

			if (newvalue.equals(onValue)) {
				laserOnOffButton.setSelected(true);
			} else {
				laserOnOffButton.setSelected(false);
			}
		}
	}

	@Override
	protected void parameterhasChanged(String parameterName) {
		if (parameterName.equals(PARAM_TITLE)) { // if title parameter
			// retrieves the TitledBorder and changes its title (that way our laser can be called anything in the UI!)
			((TitledBorder) this.getBorder()).setTitle(getStringUIParameterValue(PARAM_TITLE));
			this.repaint();
		} else if (parameterName.equals(PARAM_COLOR)) { // if color parameter
			// sets the color of the TitledBorder to make the laser more identifiable
			((TitledBorder) this.getBorder()).setTitleColor(getColorUIParameterValue(PARAM_COLOR));
			this.repaint();
		}

	}

	@Override
	public void shutDown() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Panel controlling the power percentage of a single laser.";
	}
	protected JLabel getLaserPercentageLabel() {
		return laserPercentageLabel;
	}
	protected JSlider getLaserPercentageSlider() {
		return laserPercentageSlider;
	}
	protected JToggleButton getLaserOnOffButton() {
		return laserOnOffButton;
	}
}
