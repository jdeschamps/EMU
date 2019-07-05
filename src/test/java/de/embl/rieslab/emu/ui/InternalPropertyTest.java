package test.java.de.embl.rieslab.emu.ui;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import test.java.de.embl.rieslab.emu.dummyclasses.TestableConfigurableMainFrame;
import test.java.de.embl.rieslab.emu.dummyclasses.TestableConfigurablePanel;

public class InternalPropertyTest {

	@Test
	public void testInternalProperty() {
		double d = 3.1415;
		boolean b = true;
		int i = 99;
		
		// creates two configurablepanels
		TestableConfigurablePanel cp1 = new TestableConfigurablePanel("panel1");
		TestableConfigurablePanel cp2 = new TestableConfigurablePanel("panel2");
		
		// creates a configurablemainframe
		TestableConfigurableMainFrame cmf = new TestableConfigurableMainFrame() {

			@Override
			protected void initComponents() {
				this.add(cp1);
				this.add(cp2);
			}
		};
						
		// change the internal property of cp1
		cp1.setPublicInternalProperty(cp1.INTPROP, i);
		cp1.setPublicInternalProperty(cp1.BOOLPROP, b);
		cp1.setPublicInternalProperty(cp1.DOUBLEPROP, d);
		
		// tests that cp2 internal property has changed consequently
		assertEquals(i, cp2.intInternalPropValue);
		assertEquals(d, cp2.doubleInternalPropValue, 1E-20);
		assertEquals(b, cp2.boolInternalPropValue);
	}
}
