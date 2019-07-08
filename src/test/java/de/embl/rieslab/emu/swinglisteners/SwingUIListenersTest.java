package test.java.de.embl.rieslab.emu.swinglisteners;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import main.java.de.embl.rieslab.emu.ui.ConfigurablePanel;
import main.java.de.embl.rieslab.emu.ui.uiproperties.MultiStateUIProperty;
import main.java.de.embl.rieslab.emu.ui.uiproperties.PropertyPair;
import main.java.de.embl.rieslab.emu.ui.uiproperties.SingleStateUIProperty;
import main.java.de.embl.rieslab.emu.ui.uiproperties.TwoStateUIProperty;
import main.java.de.embl.rieslab.emu.ui.uiproperties.UIProperty;
import test.java.de.embl.rieslab.emu.testableclasses.TestableConfigurableMainFrame;
import test.java.de.embl.rieslab.emu.testableclasses.TestableConfigurablePanel;
import test.java.de.embl.rieslab.emu.testableclasses.TestableMMProperty;

public class SwingUIListenersTest {

	@Test
	public void testActionOnComponents() {
		// creates a testing configurable panel
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
		String offst = "My off state";
		String onst = "My on state";
		twost.setOnStateValue(onst);
		twost.setOffStateValue(offst);
		
		// creates a configurablemainframe, in order to call the configureAllListeners method
		TestableConfigurableMainFrame cmf = new TestableConfigurableMainFrame() {

			@Override
			protected void initComponents() {
				this.add(cp);
			}
		};
		cmf.addAllListeners(); // method usually automatically when loading a configuration
		
		// check that the singlestateUIproperty state is singleVal
		assertEquals(singleVal, sing.getStateValue());
		assertEquals(offst, twost.getOffStateValue());
		assertEquals(onst, twost.getOnStateValue());
		
		// test that the default value of mmprop4 is not singleVal
		assertNotEquals(singleVal, mmprop4.getValue());
		assertNotEquals(onst, mmprop5.getValue());
		
		// click on the JButton
		cp.button.doClick();
		cp.toggle.doClick();
		int indexcombo = 2;
		cp.combobox.setSelectedIndex(indexcombo);

		// waits to let the other thread finish
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// test that the new values have been changed
		assertEquals(singleVal, mmprop4.getValue());
		assertEquals(onst, mmprop5.getValue());
		assertEquals(vals[indexcombo], mmprop3.getValue());
		
		// click off the togglebutton
		cp.toggle.doClick();

		// waits to let the other thread finish
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// test the off value
		assertEquals(offst, mmprop5.getValue());
	}
}
