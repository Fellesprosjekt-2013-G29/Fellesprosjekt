package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;

import model.Event;

public class CalendarPane extends JPanel {

	private static final int ROWS = 24;
	private static final int COLLUMNS = 8;

	private final String[] days = {"Tid", "Mandag", "Tirsdag", "Onsdag", "Torsdag", "Fredag", "Lørdag", "Søndag"};
	private final String[] hours = {"00:00", "01:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:00", 
			"08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", 
			"17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00"};
	
	private JPanel dayLine;
	private JPanel panel;
	private JScrollPane calendarScroller;
	
	private JLabel yearLabel;
	private JLabel weekLabel;
	private Date currentDate;
	private int year;
	private int week;
	
	private int gridSizeX;
	private int gridSizeY;

	/**
	 * Create the application.
	 */
	public CalendarPane() {

		setSize(800, 400);
		setLayout(null);
		
		currentDate = new Date();
		year = Integer.parseInt((new SimpleDateFormat("yyyy")).format(currentDate));
		week = Integer.parseInt((new SimpleDateFormat("w")).format(currentDate));
		
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		panel = new JPanel() {
			@Override
			public void paint(Graphics g) {
				super.paint(g);
				for(int j = 1; j < ROWS + 1; j++) {
					if(j > 0) {
						g.setColor(Color.LIGHT_GRAY);
					}
					g.drawLine(0, j * (getHeight() / ROWS), getWidth(), j * (getHeight() / ROWS));
				}
				g.setColor(Color.BLACK);
				for(int i = 1; i < COLLUMNS; i++) {
					g.drawLine(i * (getWidth() / COLLUMNS), 0, i * (getWidth() / COLLUMNS), getHeight());
				}
				super.paintChildren(g);
			}
		};
		panel.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		panel.setPreferredSize(new Dimension(600, 800));
		panel.setSize(panel.getPreferredSize());
		panel.setLayout(null);

		gridSizeX = panel.getWidth() / COLLUMNS;
		gridSizeY = panel.getHeight() / ROWS;
		
		JViewport viewPort = new JViewport();
		viewPort.setView(panel);
		viewPort.setViewPosition(new Point(0, 8 * gridSizeY));
		
		calendarScroller = new JScrollPane();
		calendarScroller.setSize(panel.getWidth() + 20, 350);
		calendarScroller.setLocation(getWidth() / 2 - calendarScroller.getWidth() / 2, getHeight() - calendarScroller.getHeight());
		calendarScroller.setViewport(viewPort);
		add(calendarScroller);

//		yearLabel = new JLabel("Year: " + year);
//		yearLabel.setBounds(340, 30, 100, 20);
//		add(yearLabel);
		
		weekLabel = new JLabel("Uke: " + week);
		weekLabel.setFont(new Font(null, Font.BOLD, 20));
		weekLabel.setBounds(getWidth() / 2 - calendarScroller.getWidth() / 2, getHeight() - calendarScroller.getHeight() - 40, 100, 50);
		add(weekLabel);
		
		dayLine = new JPanel() {
			@Override
			public void paint(Graphics g) {
				super.paint(g);
				for(int i = 1; i < COLLUMNS; i++) {
					g.drawLine(i * (getWidth() / COLLUMNS), 0, i * (getWidth() / COLLUMNS), getHeight());
				}
				super.paintChildren(g);
			}
		};
		dayLine.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		dayLine.setPreferredSize(new Dimension(panel.getWidth(), (int)(1.3 * gridSizeY)));
		dayLine.setSize(dayLine.getPreferredSize());
		dayLine.setLayout(null);
		
		updateDates();
		
		for(int i = 0; i < hours.length; i++) {
			JLabel label = new JLabel(hours[i]);
			label.setHorizontalAlignment(SwingConstants.CENTER);
			label.setVerticalAlignment(SwingConstants.BOTTOM);
			addToCalendar(label, 0, i, 1, 1);
		}

		EventView meeting1 = new EventView(new Event("2013-02-07, 16:00", "2013-02-07, 18:00"));
		addEvent(meeting1);
		
		EventView meeting2 = new EventView(new Event("2013-02-03, 08:00", "2013-02-03, 10:00"));
		addEvent(meeting2);
		
		Date test = new Date();
		
		for(int i = 0; i < 7; i++) {
//			try {
//				test = (new SimpleDateFormat("w u")).parse((i + 1) + " " + i);
//			} catch (ParseException e) {
//				e.printStackTrace();
//			}
		}
		
		final JLabel nextWeek = new JLabel();
		nextWeek.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {
				nextWeek.setIcon(new ImageIcon("resources/ArrowRight.png"));
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
				nextWeek.setIcon(new ImageIcon("resources/ArrowRightSelected.png"));
				
			}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				week++;
				updateDates();
			}
		});
		nextWeek.setIcon(new ImageIcon("resources/ArrowRight.png"));
		nextWeek.setVisible(true);
		nextWeek.setBounds(getWidth() - 80, getHeight() / 2 - 25, 70, 50);
		add(nextWeek);

		final JLabel previousWeek = new JLabel();
		previousWeek.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {
				previousWeek.setIcon(new ImageIcon("resources/ArrowLeft.png"));
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
				previousWeek.setIcon(new ImageIcon("resources/ArrowLeftSelected.png"));
			}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				week--;
				updateDates();
			}
		});
		previousWeek.setIcon(new ImageIcon("resources/ArrowLeft.png"));
		previousWeek.setBounds(10, getHeight() / 2 - 25, 70, 50);
		add(previousWeek);
	}
	
	public void updateDates() {

		dayLine.removeAll();
		Date dates = null;
		DateFormat df = new SimpleDateFormat("yyyy w u");
		int date = 0;
		int month = 0;
		
		for(int i = 0; i < days.length; i++) {
			JLabel weekDay = new JLabel(days[i]);
			weekDay.setBounds(i * gridSizeX, 0, gridSizeX, gridSizeY);
			weekDay.setHorizontalAlignment(SwingConstants.CENTER);
			dayLine.add(weekDay);
			if(i > 0) {
				try {
					if(i != 7) {
						dates = df.parse(year + " " + week + " " + (i % 8));
					}
					else {
						dates = df.parse(year + " " + (week + 1) + " " + (i % 8));
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
//				date = Integer.parseInt((new SimpleDateFormat("d")).format(dates));
//				month = Integer.parseInt((new SimpleDateFormat("M")).format(dates));
				JLabel dateLabel = new JLabel(new SimpleDateFormat("d/M yy").format(dates));
				dateLabel.setBounds(i * gridSizeX, dateLabel.getFont().getSize(), gridSizeX, gridSizeY);
				dateLabel.setHorizontalAlignment(SwingConstants.CENTER);
				dayLine.add(dateLabel);
			}
		}

		try {
			dates = (new SimpleDateFormat("yyyy w u")).parse(year + " " + week + " " + 4);
		} catch (ParseException e) {
			e.printStackTrace();
		}
//		yearLabel.setText("Year: " + (new SimpleDateFormat("yyyy")).format(dates));
		weekLabel.setText("Uke: " + (new SimpleDateFormat("w")).format(dates));
		
		calendarScroller.setColumnHeaderView(dayLine);
	}
	
	public void addToCalendar(Component comp, double posX, double posY, double width, double height) {
		comp.setBounds((int)(posX * gridSizeX), (int)(posY * gridSizeY), (int)(gridSizeX * width), (int)(gridSizeY * height));
		panel.add(comp);
	}
	
	public void addEvent(EventView event) {
		String start = (new SimpleDateFormat("MM HH mm u")).format(event.getModel().getStart());
		String end = (new SimpleDateFormat("MM HH mm u")).format(event.getModel().getEnd());
		int day = Integer.parseInt(start.substring(9, 10));
		int hour = Integer.parseInt(start.substring(3, 5));
		int duration = Integer.parseInt(end.substring(3, 5)) - Integer.parseInt(start.substring(3, 5));
		addToCalendar(event, day, hour, 1, duration);
	}
}
