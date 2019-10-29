package de.embl.rieslab.emu.ui.swingslisteners;

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
import de.embl.rieslab.emu.ui.swingslisteners.triggers.Trigger;
import de.embl.rieslab.emu.ui.swingslisteners.triggers.UnparametrizedTrigger;
import de.embl.rieslab.emu.ui.uiproperties.SingleStateUIProperty;
import de.embl.rieslab.emu.ui.uiproperties.TwoStateUIProperty;
import de.embl.rieslab.emu.ui.uiproperties.UIPropertyType;
import de.embl.rieslab.emu.utils.EmuUtils;
import de.embl.rieslab.emu.utils.exceptions.IncorrectUIPropertyTypeException;

/**
 * This class holds static standard methods to link Swing components with UIProperties. The methods trigger a UIProperty change when
 * the user interacts with the JComponents.
 * 
 * @author Joran Deschamps
 *
 */
public class SwingUIListeners {

	/**
	 * Adds a Swing action listener to a JComboBox, causing the UIProperty {@code propertyKey} from {@code cp} to be updated
	 * with the JCombobox current value.
	 * 
	 * @param cp ConfigurablePanel that owns the property.
	 * @param propertyKey UIProperty to modify when the JComboBox changes.
	 * @param cbx JCombobox holding the different UIproperty states.
	 */
	public static void addActionListenerOnStringValue(final ConfigurablePanel cp, final String propertyKey, final JComboBox<String> cbx) {
		if(cp == null) {
			throw new NullPointerException("The ConfigurablePanel cannot be null.");
		}
		if(propertyKey == null) {
			throw new NullPointerException("The UIProperty's label cannot be null.");
		}
		if(cbx == null) {
			throw new NullPointerException("The JCombobox cannot be null.");
		}
		
		cbx.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){
	    		String val = String.valueOf(cbx.getSelectedItem());
	    		cp.setUIPropertyValue(propertyKey,val);
	    	}
        });
	}
	
	/**
	 * Adds a Swing action listener to a JTextField value, causing the UIProperty {@code propertyKey} from {@code cp} to be updated
	 * with the JTextField value upon pressing the enter key.
	 * 
	 * @param cp ConfigurablePanel that owns the property.
	 * @param propertyKey UIProperty to modify when the JTextField changes.
	 * @param txtf JTextField the user interacts with.
	 */
	public static void addActionListenerOnStringValue(final ConfigurablePanel cp, final String propertyKey, final JTextField txtf) {
		if(cp == null) {
			throw new NullPointerException("The ConfigurablePanel cannot be null.");
		}
		if(propertyKey == null) {
			throw new NullPointerException("The UIProperty's label cannot be null.");
		}
		if(txtf == null) {
			throw new NullPointerException("The JTextField cannot be null.");
		}
		
		txtf.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){
	    		cp.setUIPropertyValue(propertyKey,txtf.getText());
	    	}
        });
	}

	/**
	 * Adds a Swing action listener to a JComboBox, causing the UIProperty {@code propertyKey} from {@code cp} to be updated
	 * with the JCombobox currently selected index.
	 * 
	 * @param cp ConfigurablePanel that owns the property.
	 * @param propertyKey UIProperty to modify when the JComboBox changes.
	 * @param cbx JCombobox.
	 */
	public static void addActionListenerOnSelectedIndex(final ConfigurablePanel cp, final String propertyKey, final JComboBox<?> cbx) {
		if(cp == null) {
			throw new NullPointerException("The ConfigurablePanel cannot be null.");
		}
		if(propertyKey == null) {
			throw new NullPointerException("The UIProperty's label cannot be null.");
		}
		if(cbx == null) {
			throw new NullPointerException("The JCombobox cannot be null.");
		}
		
		cbx.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){
	    		String val = String.valueOf(cbx.getSelectedIndex());
	    		cp.setUIPropertyValue(propertyKey,val);
	    	}
        });
	}
	
	/**
	 * Adds a Swing action listener to a ButtonGroup, causing the UIProperty {@code propertyKey} from {@code cp} to be updated
	 * with the ButtonGroup currently selected button index.
	 * 
	 * @param cp ConfigurablePanel that owns the property.
	 * @param propertyKey UIProperty to modify when the ButtonGroup selected button changes.
	 * @param group ButtonGroup containing the different buttons.
	 */
	public static void addActionListenerOnSelectedIndex(final ConfigurablePanel cp, final String propertyKey, final ButtonGroup group) {
		if(cp == null) {
			throw new NullPointerException("The ConfigurablePanel cannot be null.");
		}
		if(propertyKey == null) {
			throw new NullPointerException("The UIProperty's label cannot be null.");
		}
		if(group == null) {
			throw new NullPointerException("The ButtonGroup cannot be null.");
		}
		
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
	
	/**
	 * Adds a Swing action listener to a ButtonGroup, causing the UIProperty {@code propertyKey} from {@code cp} to be updated
	 * with the entry of {@code values} whose index equals the ButtonGroup currently selected button index.
	 * 
	 * @param cp ConfigurablePanel that owns the property.
	 * @param propertyKey UIProperty to modify when the ButtonGroup selected button changes.
	 * @param group ButtonGroup containing the different buttons.
	 * @param values String array containing the values to set the UIProperty to.
	 */
	public static void addActionListenerOnSelectedIndex(final ConfigurablePanel cp, final String propertyKey, final ButtonGroup group, String[] values) {
		if(cp == null) {
			throw new NullPointerException("The ConfigurablePanel cannot be null.");
		}
		if(propertyKey == null) {
			throw new NullPointerException("The UIProperty's label cannot be null.");
		}
		if(group == null) {
			throw new NullPointerException("The ButtonGroup cannot be null.");
		}
		if(values == null) {
			throw new NullPointerException("The values array cannot be null.");
		}
		for(String s: values) {
			if(s==null) {
				throw new NullPointerException("Null values are not allowed.");
			}
		}
		
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
	
	/**
	 * Adds a Swing action listener to a JCombobox, causing the UIProperty {@code propertyKey} from {@code cp} to be updated
	 * with the entry of {@code values} whose index equals the JCombobox currently selected index.
	 * 
	 * @param cp ConfigurablePanel that owns the property.
	 * @param propertyKey UIProperty to modify when the JComboBox changes.
	 * @param cbx JComboBox.
	 * @param values String array containing the values to set the UIProperty to.
	 */
	public static void addActionListenerOnSelectedIndex(final ConfigurablePanel cp, final String propertyKey, final JComboBox<?> cbx, String[] values) {
		if(cp == null) {
			throw new NullPointerException("The ConfigurablePanel cannot be null.");
		}
		if(propertyKey == null) {
			throw new NullPointerException("The UIProperty's label cannot be null.");
		}
		if(cbx == null) {
			throw new NullPointerException("The JCombobox cannot be null.");
		}
		if(values == null) {
			throw new NullPointerException("The values array cannot be null.");
		}
		for(String s: values) {
			if(s==null) {
				throw new NullPointerException("Null values are not allowed.");
			}
		}
		
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

	/**
	 * Adds a Swing action listener to a JTextField value, causing the UIProperty {@code propertyKey} from {@code cp} to be updated
	 * with the JTextField (integer) value within an allowed range defined by {@code min} and {@code max}. Non-integer values are ignored.
	 * 
	 * @param cp ConfigurablePanel that owns the UIProperty {@code propertyKey}
	 * @param propertyKey Label of the UIProperty to update.
	 * @param txtf JTextField.
	 * @param min Minimum accepted value. 
	 * @param max Maximum accepted value.
	 */
	public static void addActionListenerOnIntegerValue(final ConfigurablePanel cp, final String propertyKey, final JTextField txtf, int min, int max) {
		if(cp == null) {
			throw new NullPointerException("The ConfigurablePanel cannot be null.");
		}
		if(propertyKey == null) {
			throw new NullPointerException("The UIProperty's label cannot be null.");
		}
		if(txtf == null) {
			throw new NullPointerException("The JTextField cannot be null.");
		}
		
		txtf.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {	
				String s = txtf.getText();
				if (EmuUtils.isInteger(s)) {
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
				if (EmuUtils.isInteger(s)) {
					int val = Integer.parseInt(s);
					if(val >= min && val <= max) {
						cp.setUIPropertyValue(propertyKey, s);
					}
				}
			}
		});
	}

	/**
	 * Adds a Swing action listener to a JTextField value, causing the UIProperty {@code propertyKey} from {@code cp} to be updated
	 * with the JTextField (double) value within an allowed range defined by {@code min} and {@code max}. Non-double values are ignored.
	 * 
	 * @param cp ConfigurablePanel that owns the UIProperty {@code propertyKey}
	 * @param propertyKey Label of the UIProperty to update.
	 * @param txtf JTextField.
	 * @param min Minimum accepted value. 
	 * @param max Maximum accepted value.
	 */
	public static void addActionListenerOnDoubleValue(final ConfigurablePanel cp, final String propertyKey, final JTextField txtf, double min, double max) {
		if(cp == null) {
			throw new NullPointerException("The ConfigurablePanel cannot be null.");
		}
		if(propertyKey == null) {
			throw new NullPointerException("The UIProperty's label cannot be null.");
		}
		if(txtf == null) {
			throw new NullPointerException("The JTextField cannot be null.");
		}
		
		txtf.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {	
				String s = txtf.getText().replaceAll(",",".");
				if (EmuUtils.isNumeric(s)) {
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
				if (EmuUtils.isNumeric(s)) {
					double val = Double.parseDouble(s);
					if(Double.compare(val,min) >= 0 && Double.compare(val,max) <= 0) {
						cp.setUIPropertyValue(propertyKey, s);
					}
				}
			}
		});
	}

	/**
	 * Adds a Swing action listener to a JTextField value, causing the UIProperty {@code propertyKey} from {@code cp} to be updated
	 * with the JTextField (integer) value. Non-integer values are ignored.
	 * 
	 * @param cp ConfigurablePanel that owns the UIProperty {@code propertyKey}
	 * @param propertyKey Label of the UIProperty to update.
	 * @param txtf JTextField.
	 */
	public static void addActionListenerOnIntegerValue(final ConfigurablePanel cp, final String propertyKey, final JTextField txtf) {
		if(cp == null) {
			throw new NullPointerException("The ConfigurablePanel cannot be null.");
		}
		if(propertyKey == null) {
			throw new NullPointerException("The UIProperty's label cannot be null.");
		}
		if(txtf == null) {
			throw new NullPointerException("The JTextField cannot be null.");
		}
		
		txtf.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				String s = txtf.getText();
				if (EmuUtils.isInteger(s)) {
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
				if (EmuUtils.isInteger(s)) {
					cp.setUIPropertyValue(propertyKey, s);
				}
			}
		});
	}

	/**
	 * Adds a Swing action listener to a JTextField value, causing the UIProperty
	 * {@code propertyKey} from {@code cp} to be updated with the JTextField
	 * (double) value. Non-double values are ignored.
	 * 
	 * @param cp          ConfigurablePanel that owns the UIProperty {@code propertyKey}
	 * @param propertyKey Label of the UIProperty to update.
	 * @param txtf        JTextField.
	 */
	public static void addActionListenerOnDoubleValue(final ConfigurablePanel cp, final String propertyKey, final JTextField txtf) {
		if(cp == null) {
			throw new NullPointerException("The ConfigurablePanel cannot be null.");
		}
		if(propertyKey == null) {
			throw new NullPointerException("The UIProperty's label cannot be null.");
		}
		if(txtf == null) {
			throw new NullPointerException("The JTextField cannot be null.");
		}
		
		txtf.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				String s = txtf.getText().replaceAll(",",".");
				if (EmuUtils.isNumeric(s)) {
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
				if (EmuUtils.isNumeric(s)) {
					cp.setUIPropertyValue(propertyKey, s);
				}
			}
		});
	}

	public static void addActionListenerToDoubleTrigger(final Trigger<Double> action, final JTextField txtf) {

		if(action == null) {
			throw new NullPointerException("The Trigger cannot be null.");
		}
		if(txtf == null) {
			throw new NullPointerException("The JTextField cannot be null.");
		}
		
		txtf.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				String s = txtf.getText().replaceAll(",",".");
				if (EmuUtils.isNumeric(s)) {
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
				if (EmuUtils.isNumeric(s)) {
					action.performAction(Double.valueOf(s));
				}
			}
		});
	}
	
	public static void addActionListenerToDoubleTrigger(final Trigger<Double> action, final JTextField txtf, double min, double max) {
		if(action == null) {
			throw new NullPointerException("The Trigger cannot be null.");
		}
		if(txtf == null) {
			throw new NullPointerException("The JTextField cannot be null.");
		}
		
		txtf.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				String s = txtf.getText().replaceAll(",",".");
				if (EmuUtils.isNumeric(s)) {
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
				if (EmuUtils.isNumeric(s)) {
					double val = Double.valueOf(s);
					if(Double.compare(val, min) >= 0 && Double.compare(val, max) <= 0) {
						action.performAction(Double.valueOf(s));
					}
				}
			}
		});
	}
	
	public static void addActionListenerToStringTrigger(final Trigger<String> action, final JTextField txtf) {
		if(action == null) {
			throw new NullPointerException("The Trigger cannot be null.");
		}
		if(txtf == null) {
			throw new NullPointerException("The JTextField cannot be null.");
		}
		
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
		if(action == null) {
			throw new NullPointerException("The Trigger cannot be null.");
		}
		if(txtf == null) {
			throw new NullPointerException("The JTextField cannot be null.");
		}
		
		txtf.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				String s = txtf.getText();
				if (EmuUtils.isInteger(s)) {
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
				if (EmuUtils.isInteger(s)) {
					action.performAction(Integer.valueOf(s));
				}
			}
		});
	}

	public static void addActionListenerOnIntegerValue(final ConfigurablePanel cp, final String propertyKey, final JSlider sld) {
		if(cp == null) {
			throw new NullPointerException("The ConfigurablePanel cannot be null.");
		}
		if(propertyKey == null) {
			throw new NullPointerException("The UIProperty's label cannot be null.");
		}
		if(sld == null) {
			throw new NullPointerException("The JSlider cannot be null.");
		}
		
		sld.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				int val = sld.getValue();
				cp.setUIPropertyValue(propertyKey, String.valueOf(val));
			}
		});
	}
	
	public static void addActionListenerOnIntegerValue(final ConfigurablePanel cp, final String propertyKey, final JTextField txtf, final JSlider sld) {
		if(cp == null) {
			throw new NullPointerException("The ConfigurablePanel cannot be null.");
		}
		if(propertyKey == null) {
			throw new NullPointerException("The UIProperty's label cannot be null.");
		}
		if(txtf == null) {
			throw new NullPointerException("The JTextField cannot be null.");
		}
		if(sld == null) {
			throw new NullPointerException("The JSlider cannot be null.");
		}
		
		txtf.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {	
				String s = txtf.getText();
				if (EmuUtils.isInteger(s)) {
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
				if (EmuUtils.isInteger(s)) {
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
		if(cp == null) {
			throw new NullPointerException("The ConfigurablePanel cannot be null.");
		}
		if(propertyKey == null) {
			throw new NullPointerException("The UIProperty's label cannot be null.");
		}
		if(txtf == null) {
			throw new NullPointerException("The JTextField cannot be null.");
		}
		if(sld == null) {
			throw new NullPointerException("The JSlider cannot be null.");
		}
		
		sld.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				String strval = String.valueOf(sld.getValue());
				txtf.setText(strval);
				cp.setUIPropertyValue(propertyKey, strval);
			}
		});
	}


	public static void addActionListenerOnIntegerValue(final ConfigurablePanel cp, final String propertyKey, final JSlider sld, final JLabel lbl) {
		if(cp == null) {
			throw new NullPointerException("The ConfigurablePanel cannot be null.");
		}
		if(propertyKey == null) {
			throw new NullPointerException("The UIProperty's label cannot be null.");
		}
		if(lbl == null) {
			throw new NullPointerException("The JLabel cannot be null.");
		}
		if(sld == null) {
			throw new NullPointerException("The JSlider cannot be null.");
		}
		
		sld.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				String val = String.valueOf(sld.getValue());
				lbl.setText(val);
				cp.setUIPropertyValue(propertyKey, val);
			}
		});
	}
	
	public static void addActionListenerOnIntegerValue(final ConfigurablePanel cp, final String propertyKey, final JSlider sld, final JLabel lbl, final String prefix, final String suffix) {
		if(cp == null) {
			throw new NullPointerException("The ConfigurablePanel cannot be null.");
		}
		if(propertyKey == null) {
			throw new NullPointerException("The UIProperty's label cannot be null.");
		}
		if(lbl == null) {
			throw new NullPointerException("The JLabel cannot be null.");
		}
		if(sld == null) {
			throw new NullPointerException("The JSlider cannot be null.");
		}
		if(prefix == null) {
			throw new NullPointerException("The prefix cannot be null.");
		}
		if(suffix == null) {
			throw new NullPointerException("The suffix cannot be null.");
		}
		
		sld.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				String val = String.valueOf(sld.getValue());
				lbl.setText(prefix+val+suffix);
				cp.setUIPropertyValue(propertyKey, val);
			}
		});
	}

	public static void addActionListenerToTwoState(final ConfigurablePanel cp, final String propertyKey, final JToggleButton tglb) throws IncorrectUIPropertyTypeException {
		if(cp == null) {
			throw new NullPointerException("The ConfigurablePanel cannot be null.");
		}
		if(propertyKey == null) {
			throw new NullPointerException("The UIProperty's label cannot be null.");
		}
		if(tglb == null) {
			throw new NullPointerException("The JToggleButton cannot be null.");
		}
		if(!cp.getUIPropertyType(propertyKey).equals(UIPropertyType.TWOSTATE)) {
			throw new IncorrectUIPropertyTypeException(UIPropertyType.TWOSTATE.getTypeValue(), cp.getUIPropertyType(propertyKey).getTypeValue());
		}
		
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
		if(action == null) {
			throw new NullPointerException("The Trigger cannot be null.");
		}
		if(tglb == null) {
			throw new NullPointerException("The JToggleButton cannot be null.");
		}
		
		tglb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				AbstractButton abstractButton = (AbstractButton) actionEvent.getSource();
				boolean selected = abstractButton.getModel().isSelected();
				action.performAction(selected);
			}
		});
	}
	
	public static void addActionListenerToIntegerTrigger(final Trigger<Integer> action, final JSlider sldr) {
		if(action == null) {
			throw new NullPointerException("The Trigger cannot be null.");
		}
		if(sldr == null) {
			throw new NullPointerException("The JSlider cannot be null.");
		}
		
		sldr.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				action.performAction(sldr.getValue());
			}
		});
	}
	
	public static void addActionListenerToUnparametrizedTrigger(final UnparametrizedTrigger action, final AbstractButton btn) {
		if(action == null) {
			throw new NullPointerException("The Trigger cannot be null.");
		}
		if(btn == null) {
			throw new NullPointerException("The AbstractButton cannot be null.");
		}
		
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				action.performAction();
			}
		});
	}

	public static void addActionListenerToSingleState(final ConfigurablePanel cp, final String propertyKey, final AbstractButton btn) throws IncorrectUIPropertyTypeException {
		if(cp == null) {
			throw new NullPointerException("The ConfigurablePanel cannot be null.");
		}
		if(propertyKey == null) {
			throw new NullPointerException("The UIProperty's label cannot be null.");
		}
		if(btn == null) {
			throw new NullPointerException("The AbstractButton cannot be null.");
		}
		if(!cp.getUIPropertyType(propertyKey).equals(UIPropertyType.SINGLESTATE)) {
			throw new IncorrectUIPropertyTypeException(UIPropertyType.SINGLESTATE.getTypeValue(), cp.getUIPropertyType(propertyKey).getTypeValue());
		}
		
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cp.setUIPropertyValue(propertyKey, SingleStateUIProperty.getValueName());
			}
		});
	}

	public static void addChangeListenerOnIntegerValue(final ConfigurablePanel cp, final String propertyKey, final JSpinner spnr) {
		if(cp == null) {
			throw new NullPointerException("The ConfigurablePanel cannot be null.");
		}
		if(propertyKey == null) {
			throw new NullPointerException("The UIProperty's label cannot be null.");
		}
		if(spnr == null) {
			throw new NullPointerException("The JSpinner cannot be null.");
		}
		
		spnr.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				cp.setUIPropertyValue(propertyKey, String.valueOf((int) spnr.getValue()));
			}
		});
	}

	public static void addChangeListenerOnDoubleValue(final ConfigurablePanel cp, final String propertyKey, final JSpinner spnr) {
		if(cp == null) {
			throw new NullPointerException("The ConfigurablePanel cannot be null.");
		}
		if(propertyKey == null) {
			throw new NullPointerException("The UIProperty's label cannot be null.");
		}
		if(spnr == null) {
			throw new NullPointerException("The JSpinner cannot be null.");
		}
		
		spnr.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				cp.setUIPropertyValue(propertyKey, String.valueOf((double) spnr.getValue()));
			}
		});
	}

}
