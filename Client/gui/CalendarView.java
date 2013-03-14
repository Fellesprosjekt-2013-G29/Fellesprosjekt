package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;
import javax.swing.text.DateFormatter;

import model.Event;

public class CalendarView extends JFrame {

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
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CalendarView window = new CalendarView();
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public CalendarView() {

		setSize(800, 600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		
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
				for(int i = 1; i < COLLUMNS; i++) {
					g.drawLine(i * (getWidth() / COLLUMNS), 0, i * (getWidth() / COLLUMNS), getHeight());
					for(int j = 1; j < ROWS + 1; j++) {
						g.drawLine(0, j * (getHeight() / ROWS), getWidth(), j * (getHeight() / ROWS));
					}
				}
				super.paintChildren(g);
			}
		};
		panel.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		panel.setPreferredSize(new Dimension(600, 800));
		panel.setSize(panel.getPreferredSize());
		panel.setLayout(null);

		yearLabel = new JLabel("Year: " + year);
		yearLabel.setBounds(340, 30, 100, 20);
		getContentPane().add(yearLabel);
		
		weekLabel = new JLabel("Week: " + week);
		weekLabel.setBounds(340, 50, 100, 20);
		getContentPane().add(weekLabel);
		
		gridSizeX = panel.getWidth() / COLLUMNS;
		gridSizeY = panel.getHeight() / ROWS;
		
		JViewport viewPort = new JViewport();
		viewPort.setView(panel);
		viewPort.setViewPosition(new Point(0, 8 * gridSizeY));
		
		calendarScroller = new JScrollPane();
		calendarScroller.setBounds(100, 100, 620, 400);
		calendarScroller.setViewport(viewPort);
		getContentPane().add(calendarScroller);
		
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

		Event meeting1 = new Event("2013-02-07, 16:00", "2013-02-07, 18:00");
		addEvent(meeting1);
		
		Event meeting2 = new Event("2013-02-03, 08:00", "2013-02-03, 10:00");
		addEvent(meeting2);
		
		Date test = new Date();
		
		for(int i = 0; i < 7; i++) {
//			try {
//				test = (new SimpleDateFormat("w u")).parse((i + 1) + " " + i);
//			} catch (ParseException e) {
//				e.printStackTrace();
//			}
		}
		
		JButton nextWeek = new JButton("Next Week");
		nextWeek.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				week++;
				updateDates();
			}
		});
		nextWeek.setVisible(true);
		nextWeek.setBounds(getWidth() - 320, 10, 300, 50);
		getContentPane().add(nextWeek);

		JButton previousWeek = new JButton("Previous Week");
		previousWeek.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				week--;
				updateDates();
			}
		});
//		previousWeek.setVisible(true);
		previousWeek.setBounds(10, 10, 300, 50);
		getContentPane().add(previousWeek);
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
						dates = (new SimpleDateFormat("yyyy w u")).parse(year + " " + week + " " + (i % 8));
					}
					else {
						dates = (new SimpleDateFormat("yyyy w u")).parse(year + " " + (week + 1) + " " + (i % 8));
					}
					System.out.println(i + " " +(i % 8) + " " + (new SimpleDateFormat("M u d w")).format(dates));
				} catch (ParseException e) {
					e.printStackTrace();
				}
//				date = Integer.parseInt((new SimpleDateFormat("d")).format(dates));
//				month = Integer.parseInt((new SimpleDateFormat("M")).format(dates));
				JLabel dateLabel = new JLabel(new SimpleDateFormat("d/M").format(dates));
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
		yearLabel.setText("Year: " + (new SimpleDateFormat("yyyy")).format(dates));
		weekLabel.setText("Week: " + (new SimpleDateFormat("w")).format(dates));
		
		calendarScroller.setColumnHeaderView(dayLine);
	}
	
	public void addToCalendar(Component comp, double posX, double posY, double width, double height) {
		comp.setBounds((int)(posX * gridSizeX), (int)(posY * gridSizeY), (int)(gridSizeX * width), (int)(gridSizeY * height));
		panel.add(comp);
	}
	
	public void addEvent(Event event) {
		String start = (new SimpleDateFormat("MM HH mm u")).format(event.getStart());
		String end = (new SimpleDateFormat("MM HH mm u")).format(event.getEnd());
		int day = Integer.parseInt(start.substring(9, 10));
		int hour = Integer.parseInt(start.substring(3, 5));
		int duration = Integer.parseInt(end.substring(3, 5)) - Integer.parseInt(start.substring(3, 5));
		addToCalendar(event, day, hour, 1, duration);
	}
}
