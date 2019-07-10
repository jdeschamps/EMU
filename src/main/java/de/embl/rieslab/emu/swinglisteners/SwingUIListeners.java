package de.embl.rieslab.emu.swinglisteners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.embl.rieslab.emu.ui.ConfigurablePanel;
import de.embl.rieslab.emu.ui.uiproperties.SingleStateUIProperty;
import de.embl.rieslab.emu.ui.uiproperties.TwoStateUIProperty;
import de.embl.rieslab.emu.utils.utils;

public class SwingUIListeners {

	/**
	 * Adds a Swing action listener to a JComboBox. The action listener cause the property corresponding to {@code propertyKey} in the  
	 * ConfigurablePanel {@code cp} to be set to the item selected in the JCombobox cbx every time the user interact with it.
	 * 
	 * @param cp ConfigurablePanel owning the property.
	 * @param propertyKey UIProperty to modify when the JComboBox changes.
	 * @param cbx JCombobox holding the different UIproperty states.
	 */
	public static void addActionListenerOnStringValue(final ConfigurablePanel cp, final String propertyKey, final JComboBox<String> cbx) {
		cbx.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){
	    		String val = String.valueOf(cbx.getSelectedItem());
	    		cp.setUIPropertyValue(propertyKey,val);
	    	}
        });
	}
	
	public static void addActionListenerOnStringValue(final ConfigurablePanel cp, final String propertyKey, final JTextField txtf) {
		txtf.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){
	    		cp.setUIPropertyValue(propertyKey,txtf.getText());
	    	}
        });
	}

	public static void addActionListenerOnSelectedIndex(final ConfigurablePanel cp, final String propertyKey, final JComboBox<?> cbx) {
		cbx.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){
	    		String val = String.valueOf(cbx.getSelectedIndex());
	    		cp.setUIPropertyValue(propertyKey,val);
	    	}
        });
	}

	public static void addActionListenerOnSelectedIndex(final ConfigurablePanel cp, final String propertyKey, final ButtonGroup group) {
		
		Enumeration<AbstractButton> enm = group.getElements();
		int counter = 0;
		while(enm.hasMoreElements()) {
			final int pos = counter;
			
			AbstractButton btn = enm.nextElement();		
			btn.addActionListener(new ActionListener(){
		    	public void actionPerformed(ActionEvent e){
		    		cp.setUIPropertyValue(propertyKey,String.valueOf(pos));
		    	}
	        });
			
			counter++;
		}
	}
	
	public static void addActionListenerOnSelectedIndex(final ConfigurablePanel cp, final String propertyKey, final ButtonGroup group, String[] values) {
		
		Enumeration<AbstractButton> enm = group.getElements();
		int counter = 0;
		while(enm.hasMoreElements()) {
			final int pos = counter;
			
			AbstractButton btn = enm.nextElement();		
			btn.addActionListener(new ActionListener(){
		    	public void actionPerformed(ActionEvent e){
		    		cp.setUIPropertyValue(propertyKey,values[pos]);
		    	}
	        });
			
			counter++;
		}
	}
	
	public static void addActionListenerOnSelectedIndex(final ConfigurablePanel cp, final String propertyKey, final JComboBox<?> cbx, String[] values) {
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

	public static void addActionListenerOnIntegerValue(final ConfigurablePanel cp, final String propertyKey, final JTextField txtf, int min, int max) {
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

	public static void addActionListenerOnDoubleValue(final ConfigurablePanel cp, final String propertyKey, final JTextField txtf, double min, double max) {
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


	public static void addActionListenerOnIntegerValue(final ConfigurablePanel cp, final String propertyKey, final JTextField txtf) {
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

	public static void addActionListenerOnDoubleValue(final ConfigurablePanel cp, final String propertyKey, final JTextField txtf) {
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

	public static void addActionListenerToDoubleTrigger(final Trigger<Double> action, final JTextField txtf) {
		txtf.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				String s = txtf.getText().replaceAll(",",".");
				if (utils.isNumeric(s)) {
					action.performAction(Double.valueOf(s));
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
					action.performAction(Double.valueOf(s));
				}
			}
		});
	}
	
	public static void addActionListenerToDoubleTrigger(final Trigger<Double> action, final JTextField txtf, double min, double max) {
		txtf.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				String s = txtf.getText().replaceAll(",",".");
				if (utils.isNumeric(s)) {
					double val = Double.valueOf(s);
					if(Double.compare(val, min) >= 0 && Double.compare(val, max) <= 0) {
						action.performAction(Double.valueOf(s));
					}
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
					double val = Double.valueOf(s);
					if(Double.compare(val, min) >= 0 && Double.compare(val, max) <= 0) {
						action.performAction(Double.valueOf(s));
					}
				}
			}
		});
	}
	
	public static void addActionListenerToStringTrigger(final Trigger<String> action, final JTextField txtf) {
		txtf.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				action.performAction(txtf.getText());
			}
		});

		txtf.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent ex) {
			}

			@Override
			public void focusLost(FocusEvent ex) {
				action.performAction(txtf.getText());
			}
		});
	}
	
	public static void addActionListenerToIntegerTrigger(final Trigger<Integer> action, final JTextField txtf) {
		txtf.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				String s = txtf.getText();
				if (utils.isInteger(s)) {
					action.performAction(Integer.valueOf(s));
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
					action.performAction(Integer.valueOf(s));
				}
			}
		});
	}

	public static void addActionListenerOnIntegerValue(final ConfigurablePanel cp, final String propertyKey, final JSlider sld) {
		sld.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				int val = sld.getValue();
				cp.setUIPropertyValue(propertyKey, String.valueOf(val));
			}
		});
	}
	
	public static void addActionListenerOnIntegerValue(final ConfigurablePanel cp, final String propertyKey, final JTextField txtf, final JSlider sld) {
		txtf.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {	
				String s = txtf.getText();
				if (utils.isInteger(s)) {
					int val = Integer.parseInt(s);
					if(val >= sld.getMinimum() && val <= sld.getMaximum()) {
						sld.setValue(val);
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
					if(val >= sld.getMinimum() && val <= sld.getMaximum()) {
						sld.setValue(val);
						cp.setUIPropertyValue(propertyKey, s);
					}
				}
			}
		});
	}

	public static void addActionListenerOnIntegerValue(final ConfigurablePanel cp, final String propertyKey, final JSlider sld, final JTextField txtf) {
		sld.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				String strval = String.valueOf(sld.getValue());
				txtf.setText(strval);
				cp.setUIPropertyValue(propertyKey, strval);
			}
		});
	}


	public static void addActionListenerOnIntegerValue(final ConfigurablePanel cp, final String propertyKey, final JSlider sld, final JLabel lbl) {
		sld.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				String val = String.valueOf(sld.getValue());
				lbl.setText(val);
				cp.setUIPropertyValue(propertyKey, val);
			}
		});
	}
	
	public static void addActionListenerOnIntegerValue(final ConfigurablePanel cp, final String propertyKey, final JSlider sld, final JLabel lbl, final String prefix, final String suffix) {
		sld.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				String val = String.valueOf(sld.getValue());
				lbl.setText(prefix+val+suffix);
				cp.setUIPropertyValue(propertyKey, val);
			}
		});
	}

	public static void addActionListenerToTwoState(final ConfigurablePanel cp, final String propertyKey, final JToggleButton tglb) {
		tglb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				AbstractButton abstractButton = (AbstractButton) actionEvent.getSource();
				boolean selected = abstractButton.getModel().isSelected();
				if (selected) {
					cp.setUIPropertyValue(propertyKey, TwoStateUIProperty.getOnStateName());
				} else {
					cp.setUIPropertyValue(propertyKey, TwoStateUIProperty.getOffStateName());
				}
			}
		});
	}

	public static void addActionListenerToBooleanTrigger(final Trigger<Boolean> action, final JToggleButton tglb) {
		tglb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				AbstractButton abstractButton = (AbstractButton) actionEvent.getSource();
				boolean selected = abstractButton.getModel().isSelected();
				action.performAction(selected);
			}
		});
	}
	
	public static void addActionListenerToIntegerTrigger(final Trigger<Integer> action, final JSlider sldr) {
		sldr.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				action.performAction(sldr.getValue());
			}
		});
	}
	
	public static void addActionListenerToUnparametrizedTrigger(final UnparametrizedTrigger action, final AbstractButton btn) {
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				action.performAction();
			}
		});
	}

	public static void addActionListenerToSingleState(final ConfigurablePanel cp, final String propertyKey, final AbstractButton btn) {
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cp.setUIPropertyValue(propertyKey, SingleStateUIProperty.getValueName());
			}
		});
	}

	public static void addChangeListenerOnIntegerValue(final ConfigurablePanel cp, final String propertyKey, final JSpinner spnr) {
		spnr.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				cp.setUIPropertyValue(propertyKey, String.valueOf((int) spnr.getValue()));
			}
		});
	}

	public static void addChangeListenerOnDoubleValue(final ConfigurablePanel cp, final String propertyKey, final JSpinner spnr) {
		spnr.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				cp.setUIPropertyValue(propertyKey, String.valueOf((double) spnr.getValue()));
			}
		});
	}

}
