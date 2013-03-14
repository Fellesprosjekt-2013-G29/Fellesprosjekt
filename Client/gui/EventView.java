package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JComponent;

import model.Event;

public class EventView extends JComponent {

	private Event model;
	
	public EventView(Event model) {
		this.model = model;
	}
	
	public Event getModel() {
		return model;
	}

	public void setModel(Event model) {
		this.model = model;
	}

	@Override
	public void paint(Graphics gr) {
		Graphics2D g = (Graphics2D)gr;

		g.setColor(Color.LIGHT_GRAY);
		g.fillRoundRect(2, 1, getBounds().width - 3, getBounds().height - 1, 10, 10);
		g.setColor(Color.BLACK);
		g.drawString("Testmøte", 4, g.getFont().getSize() + 4);
		g.drawString("Testmøte", 4, g.getFont().getSize() * 2 + 4);
	}
}
