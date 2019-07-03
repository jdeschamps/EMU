package main.java.de.embl.rieslab.emu.utils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractButton;
import javax.swing.JComboBox;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import main.java.de.embl.rieslab.emu.ui.ConfigurablePanel;
import main.java.de.embl.rieslab.emu.ui.uiproperties.TwoStateUIProperty;

public class SwingUIActions {

	/**
	 * Adds a Swing action listener to a JComboBox. The action listener cause the property corresponding to {@code propertyKey} in the  
	 * ConfigurablePanel {@code cp} to be set to the item selected in the JCombobox cbx everytime the user interact with it.
	 * 
	 * @param cp ConfigurablePanel owning the property.
	 * @param propertyKey UIProperty to modify when the JComboBox changes.
	 * @param cbx JCombobox holding the different UIproperty states.
	 */
	public static void addStringValueAction(final ConfigurablePanel cp, final String propertyKey, final JComboBox<?> cbx) {
		cbx.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){
	    		String val = String.valueOf(cbx.getSelectedItem());
	    		cp.setUIPropertyValue(propertyKey,val);
	    	}
        });
	}
	
	public static void addIndexValueAction(final ConfigurablePanel cp, final String propertyKey, final JComboBox<?> cbx) {
		cbx.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){
	    		String val = String.valueOf(cbx.getSelectedIndex());
	    		cp.setUIPropertyValue(propertyKey,val);
	    	}
        });
	}
	
	public static void addIndexValueAction(final ConfigurablePanel cp, final String propertyKey, final JComboBox<?> cbx, String[] values) {
		cbx.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){
	    		int ind = cbx.getSelectedIndex();
	    		if(ind < values.length && ind >= 0) {
	    			String val = values[ind];
	    			cp.setUIPropertyValue(propertyKey,val);
	    		}
	    	}
        });
	}

	public static void addIntegerValueAction(final ConfigurablePanel cp, final String propertyKey, final JTextField txtf, int min, int max) {
		txtf.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {	
				String s = txtf.getText();
				if (utils.isInteger(s)) {
					int val = Integer.parseInt(s);
					if(val >= min && val <= max) {
						cp.setUIPropertyValue(propertyKey, s);
					}
				}
	         }
	    });
		
		txtf.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent ex) {}

			@Override
			public void focusLost(FocusEvent ex) {
				String s = txtf.getText();
				if (utils.isInteger(s)) {
					int val = Integer.parseInt(s);
					if(val >= min && val <= max) {
						cp.setUIPropertyValue(propertyKey, s);
					}
				}
			}
		});
	}

	public static void addDoubleValueAction(final ConfigurablePanel cp, final String propertyKey, final JTextField txtf, double min, double max) {
		txtf.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {	
				String s = txtf.getText().replaceAll(",",".");
				if (utils.isNumeric(s)) {
					double val = Double.parseDouble(s);
					if(Double.compare(val,min) >= 0 && Double.compare(val,max) <= 0) {
						cp.setUIPropertyValue(propertyKey, s);
					}
				}
	         }
	    });
		
		txtf.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent ex) {}

			@Override
			public void focusLost(FocusEvent ex) {
				String s = txtf.getText().replaceAll(",",".");
				if (utils.isNumeric(s)) {
					double val = Double.parseDouble(s);
					if(Double.compare(val,min) >= 0 && Double.compare(val,max) <= 0) {
						cp.setUIPropertyValue(propertyKey, s);
					}
				}
			}
		});
	}


	public static void addIntegerValueAction(final ConfigurablePanel cp, final String propertyKey, final JTextField txtf) {
		txtf.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				String s = txtf.getText();
				if (utils.isInteger(s)) {
					cp.setUIPropertyValue(propertyKey, s);
				}
			}
		});

		txtf.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent ex) {
			}

			@Override
			public void focusLost(FocusEvent ex) {
				String s = txtf.getText();
				if (utils.isInteger(s)) {
					cp.setUIPropertyValue(propertyKey, s);
				}
			}
		});
	}

	public static void addDoubleValueAction(final ConfigurablePanel cp, final String propertyKey, final JTextField txtf) {
		txtf.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				String s = txtf.getText().replaceAll(",",".");
				if (utils.isNumeric(s)) {
					cp.setUIPropertyValue(propertyKey, s);
				}
			}
		});

		txtf.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent ex) {
			}

			@Override
			public void focusLost(FocusEvent ex) {
				String s = txtf.getText().replaceAll(",",".");
				if (utils.isNumeric(s)) {
					cp.setUIPropertyValue(propertyKey, s);
				}
			}
		});
	}

	public static void addIntegerValueAction(final ConfigurablePanel cp, final String propertyKey, final JSlider sld, int min, int max) {
		sld.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				int val = sld.getValue();
				if (val >= min && val <= max) {
					cp.setUIPropertyValue(propertyKey, String.valueOf(val));
				}
			}
		});
	}

	public static void addIntegerValueAction(final ConfigurablePanel cp, final String propertyKey, final JSlider sld, int min, int max, final JTextField txtf) {
		sld.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				int val = sld.getValue();
				if (val >= min && val <= max) {
					String strval = String.valueOf(val);
					txtf.setText(strval);
					cp.setUIPropertyValue(propertyKey, strval);
				}
			}
		});
	}

	public static void addBooleanValueAction(final ConfigurablePanel cp, final String propertyKey, final JToggleButton tglb) {
		tglb.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					cp.setUIPropertyValue(propertyKey, TwoStateUIProperty.getOnStateName());
				} else if (e.getStateChange() == ItemEvent.DESELECTED) {
					cp.setUIPropertyValue(propertyKey, TwoStateUIProperty.getOffStateName());
				}
			}
		});
	}

	public static void addSingleValueAction(final ConfigurablePanel cp, final String propertyKey, final AbstractButton btn, String value) {
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cp.setUIPropertyValue(propertyKey, value);
			}
		});
	}

}
