package test.java.de.embl.rieslab.emu.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import main.java.de.embl.rieslab.emu.ui.uiparameters.IntegerUIParameter;
import main.java.de.embl.rieslab.emu.ui.uiparameters.UIParameter;
import test.java.de.embl.rieslab.emu.dummyclasses.TestConfigurableMainFrame;
import test.java.de.embl.rieslab.emu.dummyclasses.TestConfigurablePanel;

public class UIParameterTest {

	@Test
	public void testUIParameter() {
		// creates a simple configurable panel with a single UIProperty
		TestConfigurablePanel cp = new TestConfigurablePanel("myPanel");
		
		// tests that the UIParameter exists
		assertNotNull(cp.getPublicUIParameter(UIParameter.getHash(cp, cp.INTPARAM)));
		
		// wraps cp in a configurablemainframe
		TestConfigurableMainFrame cmf = new TestConfigurableMainFrame() {

			@Override
			protected void initComponents() {
				this.add(cp);
			}
		};
	
		// grabs the UIParameter
		IntegerUIParameter param = (IntegerUIParameter) cp.getPublicUIParameter(UIParameter.getHash(cp, cp.INTPARAM));
		
		// changes its value
		int value = 2;
		param.setStringValue(String.valueOf(value));
		
		// updates all
		cmf.updateAllConfigurablePanels();
		
		// waits to let the other thread finish
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// checks that the parameter has changed
		assertEquals(value, cp.intParamVal);
	}
}
