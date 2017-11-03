package main.embl.rieslab.htSMLM.threads.updaters;

import javax.swing.JTextField;

import main.embl.rieslab.htSMLM.ui.uiproperties.UIProperty;

public class JTextFieldUpdater extends ComponentUpdater<JTextField> {

	public JTextFieldUpdater(JTextField component, UIProperty prop, int idletime) {
		super(component, prop, idletime);
	}

	@Override
	public boolean sanityCheck(UIProperty prop) {
		return true;
	}

	@Override
	public void updateComponent(String val) {
		component_.setText(val);
	}

}
