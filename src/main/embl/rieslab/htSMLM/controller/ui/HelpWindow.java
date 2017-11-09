package main.embl.rieslab.htSMLM.controller.ui;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

public class HelpWindow {

	private JTextArea txtarea;
	private JFrame frame;
	private JPanel pan;
	private String defaulttext;

	public HelpWindow(String def) {
		defaulttext = def;
		txtarea = new JTextArea(5, 40);
		txtarea.setEditable(false);
		txtarea.setText(defaulttext);

		pan = new JPanel(new BorderLayout());
		pan.setBorder(new EmptyBorder(2, 3, 2, 3));

		txtarea.setFont(new Font("Serif", Font.PLAIN, 16));
		txtarea.setLineWrap(true);
		txtarea.setWrapStyleWord(true);
		
		pan.add(new JScrollPane(txtarea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));

		frame = new JFrame("Help window");
		frame.add(pan);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(false);

	}

	public void showHelp(boolean b){
		if(frame.isDisplayable()){
			frame.setVisible(b);
		} else {
			txtarea.setText(defaulttext);
			frame = new JFrame("Help window");
			frame.add(pan);
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.pack();
			frame.setVisible(b);
		}
	}
	
	public void disposeHelp(){
		frame.dispose();
	}

	public void update(String newtext){
		txtarea.setText(newtext);
	}
}