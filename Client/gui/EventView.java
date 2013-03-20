package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.PaintContext;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;
import java.text.SimpleDateFormat;

import javax.swing.JComponent;
import javax.swing.border.StrokeBorder;

import model.Event;

public class EventView extends JComponent {

	private Event model;
	private Color color;
	private boolean selected;

	public EventView() {
		init();
	}
	
	public EventView(Event model) {
		this.model = model;
		init();
	}
	
	public void init() {
		color = Color.LIGHT_GRAY;
		selected = false;
	}
	
	public Event getModel() {
		return model;
	}

	public void setModel(Event model) {
		this.model = model;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	@Override
	public void paint(Graphics gr) {
		Graphics2D g = (Graphics2D)gr;

		g.setColor(color);
		g.fillRoundRect(2, 1, getWidth() - 3, getHeight() - 1, 10, 10);
		g.setColor(Color.BLACK);
		if(selected) {
			g.drawRoundRect(0, 0, getWidth(), getHeight() - 1, 10, 10);
			g.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 3, 10, 10);
		}
		if(getHeight() < g.getFont().getSize() * 6)
			g.setFont(new Font("Default", Font.PLAIN, getHeight() / 6));
		int pos = 1;
		g.drawString(model.getTitle(), 4, g.getFont().getSize() * pos++);
		if(model.getRoom() != null) {
			g.drawString("Rom: " + model.getRoom().getRoomNumber(), 4, g.getFont().getSize() * pos++);
		}
		g.drawString(" ", 4, g.getFont().getSize() * pos++);
		g.drawString(new SimpleDateFormat("HH:mm").format(model.getStart()), 4, g.getFont().getSize() * pos++);
		g.drawString(" - ", 4, g.getFont().getSize() * pos++);
		g.drawString(new SimpleDateFormat("HH:mm").format(model.getEnd()), 4, g.getFont().getSize() * pos++);
	}
}
