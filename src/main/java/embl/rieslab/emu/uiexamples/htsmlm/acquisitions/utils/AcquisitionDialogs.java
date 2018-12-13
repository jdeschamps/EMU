package main.java.embl.rieslab.emu.uiexamples.htsmlm.acquisitions.utils;

import javax.swing.JOptionPane;

public class AcquisitionDialogs {

	public static void showNoPathMessage() {
		String title = "No path";
		String message = "The path has not been specified.";
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
	}	
	
	public static void showNoNameMessage() {
		String title = "No name";
		String message = "The experiment name has not been specified.";
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
	}	
	
	public static void showNoAcqMessage() {
		String title = "No Acquisition";
		String message = "The acquisition list is empty.";
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
	}
}
