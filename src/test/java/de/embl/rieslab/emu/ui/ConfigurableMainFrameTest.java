package de.embl.rieslab.emu.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JPanel;

import org.junit.Test;

import de.embl.rieslab.emu.exceptions.IncorrectInternalPropertyTypeException;
import de.embl.rieslab.emu.exceptions.UnknownInternalPropertyException;
import de.embl.rieslab.emu.exceptions.UnknownUIParameterException;
import de.embl.rieslab.emu.ui.internalproperties.IntegerInternalProperty;
import de.embl.rieslab.emu.ui.uiparameters.StringUIParameter;
import de.embl.rieslab.emu.ui.uiparameters.UIParameter;
import de.embl.rieslab.emu.ui.uiproperties.UIProperty;

public class ConfigurableMainFrameTest {

	@Test
	public void testCollectionConfigurablePanels() throws UnknownUIParameterException, IncorrectInternalPropertyTypeException, UnknownInternalPropertyException {
		final String[] panels = {"Pane1", "Pane2", "Pane2"};
		
		final ConfigurableTestPanel cp1 = new ConfigurableTestPanel(panels[0]);

		final ConfigurableTestPanel cp2 = new ConfigurableTestPanel(panels[1]);

		final ConfigurableTestPanel cp3 = new ConfigurableTestPanel(panels[2]);
	
		ConfigurableTestFrame cf = new ConfigurableTestFrame("MyFrame") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void initComponents() {
				this.add(cp1);
				
				JPanel pane = new JPanel();
				pane.add(cp2);
				
				JPanel subpane = new JPanel();
				subpane.add(cp3);
				pane.add(subpane);
				this.add(pane);
			}
		};
	
		// check that all ConfigurableFrame are found
		ArrayList<ConfigurablePanel> panelist = cf.getConfigurablePanels();
		assertEquals(panels.length, panelist.size());
		assertTrue(panelist.contains(cp1));
		assertTrue(panelist.contains(cp2));
		assertTrue(panelist.contains(cp3));
	}

	@Test
	public void testCollectionUIProperties(){
		final String[] panels = {"Pane1", "Pane2", "Pane2"};
		
		// no collision between UIProperties name
		final String[] props1 = {"Pane1-Prop1", "Pane1-Prop2", "Pane1-Prop3"};
		final String[] props2 = {"Pane2-Prop1", "Pane2-Prop2"};
		final String[] props3 = {"Pane3-Prop1", "Pane3-Prop2", "Pane3-Prop3", "Pane3-Prop4"}; 
		
		final ConfigurableTestPanel cp1 = new ConfigurableTestPanel(panels[0]) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeProperties() {
				for(int i=0; i<props1.length;i++) {
					this.addUIProperty(new UIProperty(this, props1[i], ""));
				}
			}
		};

		final ConfigurableTestPanel cp2 = new ConfigurableTestPanel(panels[1]) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeProperties() {
				for(int i=0; i<props2.length;i++) {
					this.addUIProperty(new UIProperty(this, props2[i], ""));
				}
			}
		};

		final ConfigurableTestPanel cp3 = new ConfigurableTestPanel(panels[2]) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeProperties() {
				for(int i=0; i<props3.length;i++) {
					this.addUIProperty(new UIProperty(this, props3[i], ""));
				}
			}
		};
	
		ConfigurableTestFrame cf = new ConfigurableTestFrame("MyFrame") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void initComponents() {
				this.add(cp1);
				
				JPanel pane = new JPanel();
				pane.add(cp2);
				
				JPanel subpane = new JPanel();
				subpane.add(cp3);
				pane.add(subpane);
				this.add(pane);
			}
		};
		
		// check that all UIProperties are found
		HashMap<String, UIProperty> proplist = cf.getUIProperties();
		int nprop = props1.length+props2.length+props3.length;
		assertEquals(nprop, proplist.size());

		Iterator<String> it = proplist.keySet().iterator();
		while(it.hasNext()) {
			String s = it.next();
			
			boolean b = false;
			for(String sp: props1) {
				if(sp.equals(s)) {
					b = true;
				}
			}
			for(String sp: props2) {
				if(sp.equals(s)) {
					b = true;
				}
			}
			for(String sp: props3) {
				if(sp.equals(s)) {
					b = true;
				}
			}
			assertTrue(b);
		}
	}
	
	@Test
	public void testCollectionUIParameters() throws UnknownUIParameterException {
		final String[] panels = {"Pane1", "Pane2", "Pane2"};
		
		// since panels[1] and panels[2] have the same name, then params2[1] and params3[0] will collide
 		final String[] params1 = {"Pane1-Param1"};
		final String[] params2 = {"Pane2-Param1", "Pane2-Param2"};
		final String[] params3 = {"Pane2-Param2"};
		
		
		final ConfigurableTestPanel cp1 = new ConfigurableTestPanel(panels[0]) {

			private static final long serialVersionUID = 1L;
			@Override
			protected void initializeParameters() {
				for(int i=0; i<params1.length;i++) {
					this.addUIParameter(new StringUIParameter(this, params1[i], "", ""));
				}
			}
		};

		final ConfigurableTestPanel cp2 = new ConfigurableTestPanel(panels[1]) {

			private static final long serialVersionUID = 1L;
			
			@Override
			protected void initializeParameters() {
				for(int i=0; i<params2.length;i++) {
					this.addUIParameter(new StringUIParameter(this, params2[i], "", ""));
				}
			}			
		};

		final ConfigurableTestPanel cp3 = new ConfigurableTestPanel(panels[2]) {

			private static final long serialVersionUID = 1L;
			@Override
			protected void initializeParameters() {
				for(int i=0; i<params3.length;i++) {
					this.addUIParameter(new StringUIParameter(this, params3[i], "", ""));
				}
			}
		};
	
		ConfigurableTestFrame cf = new ConfigurableTestFrame("MyFrame") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void initComponents() {
				this.add(cp1);
				
				JPanel pane = new JPanel();
				pane.add(cp2);
				
				JPanel subpane = new JPanel();
				subpane.add(cp3);
				pane.add(subpane);
				this.add(pane);
			}
		};
	
		// check that all ConfigurableFrame are found
		ArrayList<ConfigurablePanel> panelist = cf.getConfigurablePanels();
		
		// check that all UIParameters are found (params2[1] and params3[0])
		int nparams = params1.length+params2.length+params3.length-1; // since two UIParameter collide
		HashMap<String, UIParameter> paramlist = cf.getUIParameters();
		assertEquals(nparams, paramlist.size());

		Iterator<String> it = paramlist.keySet().iterator();
		while(it.hasNext()) {
			String s = paramlist.get(it.next()).getLabel();
			
			boolean b = false;
			for(String sp: params1) {
				if(sp.equals(s)) {
					b = true;
				}
			}
			for(String sp: params2) {
				if(sp.equals(s)) {
					b = true;
				}
			}
			for(String sp: params3) {
				if(sp.equals(s)) {
					b = true;
				}
			}
			assertTrue(b);
		}
		
		// check that the collision is solved by linking the UIParameter to both ConfigurablePanel
		String collidedparam = panels[1]+" - "+params2[1];
		UIParameter<?> param = paramlist.get(collidedparam);
		final String newval = "MyNewValue";
		param.setStringValue(newval);
		
		for(ConfigurablePanel cp: panelist) {
			if(cp.getLabel().equals(panels[1])) { // they have the same label anyway
				assertEquals(newval, cp.getUIParameter(params2[1]).getStringValue()); // prop is known with same label as well
			}
		}
	}
	
	@Test
	public void testCollectionInternalProperties() throws IncorrectInternalPropertyTypeException, UnknownInternalPropertyException {
		final String[] panels = {"Pane1", "Pane2", "Pane2"};
	
		final String[] intprops = {"IntProp1", "IntProp2"};
		
		final ConfigurableTestPanel cp1 = new ConfigurableTestPanel(panels[0]) {

			private static final long serialVersionUID = 1L;
			
			@Override
			protected void initializeInternalProperties() {
				this.addInternalProperty(new IntegerInternalProperty(this, intprops[0], 1));
			}
		};

		final ConfigurableTestPanel cp2 = new ConfigurableTestPanel(panels[1]) {

			private static final long serialVersionUID = 1L;
			
			@Override
			protected void initializeInternalProperties() {
				this.addInternalProperty(new IntegerInternalProperty(this, intprops[0], 1));
				this.addInternalProperty(new IntegerInternalProperty(this, intprops[1], 1));
			}
		};

		final ConfigurableTestPanel cp3 = new ConfigurableTestPanel(panels[2]) {

			private static final long serialVersionUID = 1L;
			
			@Override
			protected void initializeInternalProperties() {
				this.addInternalProperty(new IntegerInternalProperty(this, intprops[1], 1));
			}
		};
	
		ConfigurableTestFrame cf = new ConfigurableTestFrame("MyFrame") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void initComponents() {
				this.add(cp1);
				
				JPanel pane = new JPanel();
				pane.add(cp2);
				
				JPanel subpane = new JPanel();
				subpane.add(cp3);
				pane.add(subpane);
				this.add(pane);
			}
		};
		
		// check that the InternalProperties have been linked
		assertEquals(1, cp1.getIntegerInternalPropertyValue(intprops[0]));
		assertEquals(1, cp2.getIntegerInternalPropertyValue(intprops[0]));
		assertEquals(1, cp2.getIntegerInternalPropertyValue(intprops[1]));
		assertEquals(1, cp3.getIntegerInternalPropertyValue(intprops[1]));
		
		final int p = 84;
		cp1.setInternalPropertyValue(intprops[0], p);
		assertEquals(p, cp1.getIntegerInternalPropertyValue(intprops[0]));
		assertEquals(p, cp2.getIntegerInternalPropertyValue(intprops[0]));
		assertEquals(1, cp2.getIntegerInternalPropertyValue(intprops[1]));
		assertEquals(1, cp3.getIntegerInternalPropertyValue(intprops[1]));
		
		final int q = -91;
		cp3.setInternalPropertyValue(intprops[1], q);
		assertEquals(p, cp1.getIntegerInternalPropertyValue(intprops[0]));
		assertEquals(p, cp2.getIntegerInternalPropertyValue(intprops[0]));
		assertEquals(q, cp2.getIntegerInternalPropertyValue(intprops[1]));
		assertEquals(q, cp3.getIntegerInternalPropertyValue(intprops[1]));
	}
	

	
	// test updateall
	// test addalllisteners
	// test shutdownall
	
	// test collision between UIProperties
	// test collision between UIParameters
	// makes several tests out of the big one...
	
	private class ConfigurableTestFrame extends ConfigurableMainFrame{

		private static final long serialVersionUID = 1L;
		
		public ConfigurableTestFrame(String title) {
			super(title, null);
		}

		@Override
		protected void initComponents() {}

		@Override
		protected void setUpMenu() {}
	}
	
	private class ConfigurableTestPanel extends ConfigurablePanel{

		private static final long serialVersionUID = 1L;
	 	
		public ConfigurableTestPanel(String label) {
			super(label);
		}
		
		@Override
		protected void initializeProperties() {}
		
		@Override
		protected void initializeParameters() {}
		
		@Override
		protected void initializeInternalProperties() {}
		
		@Override
		protected void parameterhasChanged(String parameterName) {}

		@Override
		public void internalpropertyhasChanged(String propertyName) {}

		@Override
		protected void propertyhasChanged(String propertyName, String newvalue) {}

		@Override
		public void shutDown() {}

		@Override
		protected void addComponentListeners() {}
		
		@Override
		public String getDescription() {return "";}
	}
	
}
