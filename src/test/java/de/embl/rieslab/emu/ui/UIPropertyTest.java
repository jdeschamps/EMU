package test.java.de.embl.rieslab.emu.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import main.java.de.embl.rieslab.emu.ui.uiproperties.PropertyPair;
import test.java.de.embl.rieslab.emu.dummyclasses.TestConfigurablePanel;
import test.java.de.embl.rieslab.emu.dummyclasses.TestMMProperty;

public class UIPropertyTest {

	final String NEWVALUE = "NewValue";
	final String NEWVALUE2 = "NewValue2";
	
	@Test
	public void testUIProperty() {
		// creates a simple configurable panel with a single UIProperty
		TestConfigurablePanel cp = new TestConfigurablePanel("myPanel");
		
		// tests that the UIProperty exists
		assertNotNull(cp.getPublicUIProperty(cp.UIPROP));
		
		// creates a dummy MMProperty
		TestMMProperty mmprop = new TestMMProperty();
		
		// pairs UIProperty and MMProperty
		PropertyPair.pair(cp.getPublicUIProperty(cp.UIPROP), mmprop);
				
		// sets the UIProperty value
		cp.setUIPropertyValue(cp.UIPROP, NEWVALUE);
		
		// waits to let the other thread finish
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// checks that the value has been set in mmprop
		assertEquals(NEWVALUE, mmprop.getStringValue());
		
		// sets the value of the mmprop
		mmprop.setStringValue(NEWVALUE2, null);
		
		// waits to let the other thread finish
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// checks that the value has been set in the cp
		assertEquals(NEWVALUE2, cp.uiPropValue);
	}
		
}
