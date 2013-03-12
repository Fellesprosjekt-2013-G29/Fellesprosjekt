package gui;

import javax.swing.*;

public class CalendarView extends JFrame{
	private JTextArea  textArea = new JTextArea();
	private JScrollPane pane;
	
	public CalendarView(){
		super("CalendarClient");
		setSize(600, 300);
	    pane = new JScrollPane(textArea);
	    getContentPane().add(pane);
	    setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}
	
	
	public void log(String string){
		textArea.append("\n" + string);
	}
}
