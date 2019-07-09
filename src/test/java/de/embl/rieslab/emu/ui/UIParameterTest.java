package de.embl.rieslab.emu.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import de.embl.rieslab.emu.ui.uiparameters.BoolUIParameter;
import de.embl.rieslab.emu.ui.uiparameters.ColorUIParameter;
import de.embl.rieslab.emu.ui.uiparameters.ComboUIParameter;
import de.embl.rieslab.emu.ui.uiparameters.DoubleUIParameter;
import de.embl.rieslab.emu.ui.uiparameters.IntegerUIParameter;
import de.embl.rieslab.emu.ui.uiparameters.StringUIParameter;
import de.embl.rieslab.emu.ui.uiparameters.UIParameter;
import de.embl.rieslab.emu.ui.uiparameters.UIPropertyParameter;
import de.embl.rieslab.emu.utils.ColorRepository;
import de.embl.rieslab.emu.testableclasses.TestableConfigurableMainFrame;
import de.embl.rieslab.emu.testableclasses.TestableConfigurablePanel;

public class UIParameterTest {

	@Test
	public void testUIParameter() {
		// creates a simple configurable panel with a single UIProperty
		TestableConfigurablePanel cp = new TestableConfigurablePanel("myPanel");
		
		// tests that the UIParameters exist
		assertNotNull(cp.getPublicUIParameter(UIParameter.getHash(cp, cp.BOOLPARAM)));
		assertNotNull(cp.getPublicUIParameter(UIParameter.getHash(cp, cp.COLORPARAM)));
		assertNotNull(cp.getPublicUIParameter(UIParameter.getHash(cp, cp.COMBOPARAM)));
		assertNotNull(cp.getPublicUIParameter(UIParameter.getHash(cp, cp.DOUBLEPARAM)));
		assertNotNull(cp.getPublicUIParameter(UIParameter.getHash(cp, cp.INTPARAM)));
		assertNotNull(cp.getPublicUIParameter(UIParameter.getHash(cp, cp.STRINGPARAM)));
		assertNotNull(cp.getPublicUIParameter(UIParameter.getHash(cp, cp.PROPPARAM)));
		
		// wraps cp in a configurablemainframe
		TestableConfigurableMainFrame cmf = new TestableConfigurableMainFrame() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 156087489999419759L;

			@Override
			protected void initComponents() {
				this.add(cp);
			}
		};
	
		// grabs the UIParameters
		BoolUIParameter boolparam = (BoolUIParameter) cp.getPublicUIParameter(UIParameter.getHash(cp, cp.BOOLPARAM));
		ColorUIParameter colorparam = (ColorUIParameter) cp.getPublicUIParameter(UIParameter.getHash(cp, cp.COLORPARAM));
		ComboUIParameter comboparam = (ComboUIParameter) cp.getPublicUIParameter(UIParameter.getHash(cp, cp.COMBOPARAM));
		DoubleUIParameter doubleparam = (DoubleUIParameter) cp.getPublicUIParameter(UIParameter.getHash(cp, cp.DOUBLEPARAM));
		IntegerUIParameter intparam = (IntegerUIParameter) cp.getPublicUIParameter(UIParameter.getHash(cp, cp.INTPARAM));
		StringUIParameter strparam = (StringUIParameter) cp.getPublicUIParameter(UIParameter.getHash(cp, cp.STRINGPARAM));
		UIPropertyParameter propparam = (UIPropertyParameter) cp.getPublicUIParameter(UIParameter.getHash(cp, cp.PROPPARAM));
		

		// checks that their values is right
		assertEquals(cp.boolParamVal, boolparam.getValue());
		assertEquals(ColorRepository.getStringColor(cp.colorParamVal), colorparam.getStringValue());
		assertEquals(cp.comboParamVal, comboparam.getValue());
		assertEquals(cp.doubleParamVal, doubleparam.getValue(), 1E-20);
		assertEquals(cp.intParamVal, (int) intparam.getValue());
		assertEquals(cp.stringParamVal, strparam.getValue());
		assertEquals(cp.propParamVal, propparam.getValue());
		
		
		// changes their values
		boolean boolval = true;
		String colorval = ColorRepository.strgreen;
		String comboval = cp.STATE3;
		double doubleval = 45.015;
		int intval = 99;
		String strval = "Bonjour";
		String uipropval = cp.MULTPROP;
		
		boolparam.setStringValue(String.valueOf(boolval));
		colorparam.setStringValue(colorval);
		comboparam.setStringValue(comboval);
		doubleparam.setStringValue(String.valueOf(doubleval));
		intparam.setStringValue(String.valueOf(intval));
		strparam.setStringValue(strval);
		propparam.setStringValue(uipropval);
		
		// check that the values of the parameters have changed
		assertEquals(boolval, boolparam.getValue());
		assertEquals(colorval, colorparam.getStringValue());
		assertEquals(comboval, comboparam.getValue());
		assertEquals(doubleval, doubleparam.getValue(), 1E-20);
		assertEquals(intval, (int) intparam.getValue());
		assertEquals(strval, strparam.getValue());
		assertEquals(uipropval, propparam.getValue());
		
		// updates all
		cmf.updateAllConfigurablePanels();
		
		// waits to let the other thread finish
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// checks that the parameter has changed
		assertEquals(boolval, cp.boolParamVal);
		assertEquals(colorval, ColorRepository.getStringColor(cp.colorParamVal));
		assertEquals(comboval, cp.comboParamVal);
		assertEquals(doubleval, cp.doubleParamVal, 1E-20);
		assertEquals(intval, cp.intParamVal);
		assertEquals(strval, cp.stringParamVal);
		assertEquals(uipropval, cp.propParamVal);
	}
}
