package test.java.de.embl.rieslab.emu.ui;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import test.java.de.embl.rieslab.emu.dummyclasses.TestConfigurableMainFrame;
import test.java.de.embl.rieslab.emu.dummyclasses.TestConfigurablePanel;

public class InternalPropertyTest {

	@Test
	public void testInternalProperty() {
		double d = 3.1415;
		
		// creates two configurablepanels
		TestConfigurablePanel cp1 = new TestConfigurablePanel("panel1");
		TestConfigurablePanel cp2 = new TestConfigurablePanel("panel2");
		
		// creates a configurablemainframe
		TestConfigurableMainFrame cmf = new TestConfigurableMainFrame() {

			@Override
			protected void initComponents() {
				this.add(cp1);
				this.add(cp2);
			}
		};
						
		// change the internal property of cp1
		cp1.setPublicInternalProperty(cp1.INTPROP, d);
		
		// tests that cp2 internal property has changed consequently
		assertEquals(d, cp2.internalPropValue, 1E-20);
	}
}
