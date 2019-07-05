package test.java.de.embl.rieslab.emu.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import main.java.de.embl.rieslab.emu.ui.uiproperties.MultiStateUIProperty;
import main.java.de.embl.rieslab.emu.ui.uiproperties.PropertyPair;
import main.java.de.embl.rieslab.emu.ui.uiproperties.SingleStateUIProperty;
import main.java.de.embl.rieslab.emu.ui.uiproperties.TwoStateUIProperty;
import test.java.de.embl.rieslab.emu.dummyclasses.TestableConfigurablePanel;
import test.java.de.embl.rieslab.emu.dummyclasses.TestableMMProperty;

public class SwingUIActionsTest {

	@Test
	public void testActionOnComponent() {
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

	}
}
