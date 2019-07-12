package de.embl.rieslab.emu.ui.uiproperties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import de.embl.rieslab.emu.ui.ConfigurablePanel;
import de.embl.rieslab.emu.ui.uiproperties.flag.PropertyFlag;

public class UIPropertyTest {

	@Test
	public void testUIProperty() {
		UIPropertyTestPanel cp = new UIPropertyTestPanel("MyPanel");

		assertEquals(cp.PROP, cp.property.getLabel());
		assertEquals(cp.DESC, cp.property.getDescription());
		assertFalse(cp.property.isAssigned());
	}

	@Test
	public void testUIPropertyFlag() {
		UIPropertyTestPanel cp = new UIPropertyTestPanel("MyPanel") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void initializeProperties() {
				flag = new PropertyFlag("MyFlag") {};
				property = new UIProperty(this, PROP, DESC, flag);
			}
		};
		
		assertEquals(cp.flag, cp.property.getFlag());
	}
	
	@Test
	public void testUIPropertyFriendlyName() {
		UIPropertyTestPanel cp = new UIPropertyTestPanel("MyPanel");
		
		final String s = "MyFriendlyName";
		cp.property.setFriendlyName(s);
		assertEquals(s, cp.property.getFriendlyName());
	}

	@Test
	public void testUIPropertyNotification() {
		UIPropertyTestPanel cp = new UIPropertyTestPanel("MyPanel") {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void propertyhasChanged(String propertyName, String newvalue) {
				if(propertyName.equals(PROP)) {
					value = newvalue;
				}
			}
		};

		String s  = "NewValue";
		cp.property.mmPropertyHasChanged(s);

		// waits to let the other thread finish
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		assertEquals(s, cp.value);
	}
	
	private class UIPropertyTestPanel extends ConfigurablePanel{

		private static final long serialVersionUID = 1L;
		public UIProperty property;
		public final String PROP = "MyProp"; 
		public final String DESC = "MyDescription"; 
		public PropertyFlag flag;
		public String value = "";
	 	
		public UIPropertyTestPanel(String label) {
			super(label);
		}
		
		@Override
		protected void initializeProperties() {
			property = new UIProperty(this, PROP, DESC);
		}
		
		@Override
		protected void initializeParameters() {}
		
		@Override
		protected void parameterhasChanged(String parameterName) {}
		
		@Override
		protected void initializeInternalProperties() {}

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
