package gui;

import java.awt.Color;
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
		g.fillRoundRect(2, 1, getBounds().width - 3, getBounds().height - 1, 10, 10);
		g.setColor(Color.BLACK);
		if(selected) {
			g.drawRoundRect(0, 0, getBounds().width, getBounds().height - 1, 10, 10);
			g.drawRoundRect(1, 1, getBounds().width - 2, getBounds().height - 3, 10, 10);
		}
		int pos = 1;
		g.drawString(model.getTitle(), 4, g.getFont().getSize() * pos++);
		g.drawString("Rom: " + model.getRoom().getRoomNumber(), 4, g.getFont().getSize() * pos++);
		g.drawString("Start: " + new SimpleDateFormat("HH:mm").format(model.getStart()), 4, g.getFont().getSize() * pos++);
		g.drawString("Slutt: " + new SimpleDateFormat("HH:mm").format(model.getEnd()), 4, g.getFont().getSize() * pos++);
	}
}
