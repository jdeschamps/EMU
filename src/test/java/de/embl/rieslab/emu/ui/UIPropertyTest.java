package test.java.de.embl.rieslab.emu.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import main.java.de.embl.rieslab.emu.ui.uiproperties.MultiStateUIProperty;
import main.java.de.embl.rieslab.emu.ui.uiproperties.PropertyPair;
import main.java.de.embl.rieslab.emu.ui.uiproperties.SingleStateUIProperty;
import main.java.de.embl.rieslab.emu.ui.uiproperties.TwoStateUIProperty;
import test.java.de.embl.rieslab.emu.dummyclasses.TestableConfigurablePanel;
import test.java.de.embl.rieslab.emu.dummyclasses.TestableMMProperty;

public class UIPropertyTest {

	final String NEWVALUE = "NewValue";
	final String NEWVALUE2 = "NewValue2";
	final String NEWVALUE3 = "NewValue3";
	final String NEWVALUE4 = "NewValue4";
	final String NEWVALUE5 = "NewValue5";
	
	@Test
	public void testCreateUIProperty() {
		// creates a simple configurable panel with a single UIProperty
		TestableConfigurablePanel cp = new TestableConfigurablePanel("myPanel");
		
		// tests that the UIProperties exist
		assertNotNull(cp.getPublicUIProperty(cp.UIPROP));
		assertNotNull(cp.getPublicUIProperty(cp.UIPROP2));
		assertNotNull(cp.getPublicUIProperty(cp.MULTPROP));
		assertNotNull(cp.getPublicUIProperty(cp.SINGPROP));
		assertNotNull(cp.getPublicUIProperty(cp.TWOSTPROP));
	}
	
	@Test
	public void testModifyUIPropertyFromCP() {
		// creates a simple configurable panel with a single UIProperty
		TestableConfigurablePanel cp = new TestableConfigurablePanel("myPanel");
				
		// creates dummy MMProperties
		TestableMMProperty mmprop1 = new TestableMMProperty("Prop1");
		TestableMMProperty mmprop2 = new TestableMMProperty("Prop2");
		TestableMMProperty mmprop3 = new TestableMMProperty("Prop3");
		TestableMMProperty mmprop4 = new TestableMMProperty("Prop4");
		TestableMMProperty mmprop5 = new TestableMMProperty("Prop5");
		
		// pairs UIProperty and MMProperty and set states
		PropertyPair.pair(cp.getPublicUIProperty(cp.UIPROP), mmprop1);
		PropertyPair.pair(cp.getPublicUIProperty(cp.UIPROP2), mmprop2);
		
		PropertyPair.pair(cp.getPublicUIProperty(cp.MULTPROP), mmprop3);
		MultiStateUIProperty multi = (MultiStateUIProperty) cp.getPublicUIProperty(cp.MULTPROP);
		int n = multi.getNumberOfStates();
		assertEquals(cp.sizeMultiProp,n);
		String[] vals = new String[n];
		for(int i=0;i<n;i++) {
			vals[i] = "MyState "+i;
		}
		multi.setStateValues(vals);
		
		PropertyPair.pair(cp.getPublicUIProperty(cp.SINGPROP), mmprop4);
		SingleStateUIProperty sing = (SingleStateUIProperty) cp.getPublicUIProperty(cp.SINGPROP);
		String singleVal = "My Single value";
		sing.setStateValue(singleVal);
		
		PropertyPair.pair(cp.getPublicUIProperty(cp.TWOSTPROP), mmprop5);
		TwoStateUIProperty twost = (TwoStateUIProperty) cp.getPublicUIProperty(cp.TWOSTPROP);
		String offst = "My on state";
		String onst = "My off state";
		twost.setOnStateValue(onst);
		twost.setOffStateValue(offst);

		String defmulti = mmprop3.getValue();
		String deftwost = mmprop5.getValue();
				
		// sets the UIProperty value from the cp directly
		cp.setUIPropertyValue(cp.UIPROP, NEWVALUE);
		cp.setUIPropertyValue(cp.UIPROP2, NEWVALUE2);
		cp.setUIPropertyValue(cp.SINGPROP, NEWVALUE3);
		cp.setUIPropertyValue(cp.MULTPROP, NEWVALUE4);
		cp.setUIPropertyValue(cp.TWOSTPROP, NEWVALUE5);
		
		// waits to let the other thread finish
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// checks that the value has been set in mmprop
		assertEquals(NEWVALUE, mmprop1.getStringValue());
		assertEquals(NEWVALUE2, mmprop2.getStringValue());
		
		// check that single value was set to its single level
		assertEquals(singleVal, mmprop4.getStringValue());
		
		// check that the values were not set for invalid states
		assertEquals(defmulti, mmprop3.getStringValue());
		assertEquals(deftwost, mmprop5.getStringValue());
		
		// now set the multi and two state to states values
		cp.setUIPropertyValue(cp.MULTPROP, vals[1]);
		cp.setUIPropertyValue(cp.TWOSTPROP, onst);

		// waits to let the other thread finish
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// check that the values were set correctly
		assertEquals(vals[1], mmprop3.getStringValue());
		assertEquals(onst, mmprop5.getStringValue());
	}

	@Test
	public void testModifyUIPropertyFromMMProperty() {
		// creates a simple configurable panel with a single UIProperty
		TestableConfigurablePanel cp = new TestableConfigurablePanel("myPanel");

		// creates dummy MMProperties
		TestableMMProperty mmprop1 = new TestableMMProperty("Prop1");
		TestableMMProperty mmprop2 = new TestableMMProperty("Prop2");
		TestableMMProperty mmprop3 = new TestableMMProperty("Prop3");
		TestableMMProperty mmprop4 = new TestableMMProperty("Prop4");
		TestableMMProperty mmprop5 = new TestableMMProperty("Prop5");

		// pairs UIProperty and MMProperty and set states
		PropertyPair.pair(cp.getPublicUIProperty(cp.UIPROP), mmprop1);
		PropertyPair.pair(cp.getPublicUIProperty(cp.UIPROP2), mmprop2);

		PropertyPair.pair(cp.getPublicUIProperty(cp.MULTPROP), mmprop3);
		MultiStateUIProperty multi = (MultiStateUIProperty) cp.getPublicUIProperty(cp.MULTPROP);
		int n = multi.getNumberOfStates();
		assertEquals(cp.sizeMultiProp, n);
		String[] vals = new String[n];
		for (int i = 0; i < n; i++) {
			vals[i] = "MyState " + i;
		}
		multi.setStateValues(vals);

		PropertyPair.pair(cp.getPublicUIProperty(cp.SINGPROP), mmprop4);
		SingleStateUIProperty sing = (SingleStateUIProperty) cp.getPublicUIProperty(cp.SINGPROP);
		String singleVal = "My Single value";
		sing.setStateValue(singleVal);

		PropertyPair.pair(cp.getPublicUIProperty(cp.TWOSTPROP), mmprop5);
		TwoStateUIProperty twost = (TwoStateUIProperty) cp.getPublicUIProperty(cp.TWOSTPROP);
		String offst = "My on state";
		String onst = "My off state";
		twost.setOnStateValue(onst);
		twost.setOffStateValue(offst);

		// sets the value of the mmprop directly
		mmprop1.setStringValue(NEWVALUE, null);
		mmprop2.setStringValue(NEWVALUE2, null);
		mmprop3.setStringValue(NEWVALUE3, null);
		mmprop4.setStringValue(NEWVALUE4, null);
		mmprop5.setStringValue(NEWVALUE5, null);

		// waits to let the other thread finish
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// checks that the value has been set in the cp
		assertEquals(NEWVALUE, cp.uiPropValue);
		assertEquals(NEWVALUE2, cp.uiPropValue2);
		assertEquals(NEWVALUE3, cp.multiPropValue);
		assertEquals(NEWVALUE4, cp.singlePropValue);
		assertEquals(NEWVALUE5, cp.twoPropValue);
	}
		
}
