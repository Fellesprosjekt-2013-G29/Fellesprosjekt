package gui;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import model.Event;
import model.User;
import client.Program;

public class CalendarView extends JFrame implements ActionListener {

	public static final int WIDTH = 1024;
	public static final int HEIGHT = 576;
	
	private Program program;
	
	private JLabel chooseDateLabel;
	private JTextField dateField;
	private JButton chooseDateButton;
	private JLabel chooseWeekLabel;
	private JComboBox notificationsBox;
	private JButton logOutButton;
	private CalendarPane calendarPane;
	private JButton newEventButton;
	private JButton editEventButton;
	private JButton deleteEventButton;
	private JButton manageCalendarsButton;
	private JComboBox weekNumberBox;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CalendarView window = new CalendarView(new Program());
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
	public CalendarView(Program parent) {
		this.program = parent;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		setBounds(100, 100, WIDTH, HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{69, 66, 64, 12, 103, 68, 43, 234, 97, 109, 0};
		gridBagLayout.rowHeights = new int[]{33, 23, 427, 23, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		
		chooseDateLabel = new JLabel("Velg dato:");
		GridBagConstraints gbc_chooseDateLabel = new GridBagConstraints();
		gbc_chooseDateLabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_chooseDateLabel.insets = new Insets(0, 0, 5, 5);
		gbc_chooseDateLabel.gridx = 1;
		gbc_chooseDateLabel.gridy = 1;
		getContentPane().add(chooseDateLabel, gbc_chooseDateLabel);
		
		dateField = new JTextField();
		dateField.setText(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
		dateField.setEditable(false);
		GridBagConstraints gbc_dateField = new GridBagConstraints();
		gbc_dateField.fill = GridBagConstraints.HORIZONTAL;
		gbc_dateField.insets = new Insets(0, 0, 5, 5);
		gbc_dateField.gridwidth = 2;
		gbc_dateField.gridx = 2;
		gbc_dateField.gridy = 1;
		getContentPane().add(dateField, gbc_dateField);
		dateField.setColumns(10);
		
		chooseDateButton = new JButton("Velg dato");
		chooseDateButton.addActionListener(this);
		GridBagConstraints gbc_chooseDateButton = new GridBagConstraints();
		gbc_chooseDateButton.anchor = GridBagConstraints.NORTH;
		gbc_chooseDateButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_chooseDateButton.insets = new Insets(0, 0, 5, 5);
		gbc_chooseDateButton.gridx = 4;
		gbc_chooseDateButton.gridy = 1;
		getContentPane().add(chooseDateButton, gbc_chooseDateButton);
		
		chooseWeekLabel = new JLabel("eller velg uke:");
		GridBagConstraints gbc_chooseWeekLabel = new GridBagConstraints();
		gbc_chooseWeekLabel.insets = new Insets(0, 0, 5, 5);
		gbc_chooseWeekLabel.gridx = 5;
		gbc_chooseWeekLabel.gridy = 1;
		getContentPane().add(chooseWeekLabel, gbc_chooseWeekLabel);
		
		notificationsBox = new JComboBox();
		notificationsBox.setModel(new DefaultComboBoxModel(new String[] {"Varslinger!"}));
		notificationsBox.addActionListener(this);
		GridBagConstraints gbc_notificationsBox = new GridBagConstraints();
		gbc_notificationsBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_notificationsBox.insets = new Insets(0, 0, 5, 5);
		gbc_notificationsBox.gridx = 8;
		gbc_notificationsBox.gridy = 1;
		getContentPane().add(notificationsBox, gbc_notificationsBox);
		
		logOutButton = new JButton("Logg ut");
		logOutButton.addActionListener(this);
		logOutButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		GridBagConstraints gbc_logOutButton = new GridBagConstraints();
		gbc_logOutButton.anchor = GridBagConstraints.NORTHWEST;
		gbc_logOutButton.insets = new Insets(0, 0, 5, 0);
		gbc_logOutButton.gridx = 9;
		gbc_logOutButton.gridy = 1;
		getContentPane().add(logOutButton, gbc_logOutButton);
		
		calendarPane = new CalendarPane(program);
		GridBagConstraints gbc_calendarPane = new GridBagConstraints();
		gbc_calendarPane.fill = GridBagConstraints.BOTH;
		gbc_calendarPane.insets = new Insets(0, 0, 5, 0);
		gbc_calendarPane.gridwidth = 10;
		gbc_calendarPane.gridx = 0;
		gbc_calendarPane.gridy = 2;
		getContentPane().add(calendarPane, gbc_calendarPane);
		
		newEventButton = new JButton("Opprett hendelse");
		newEventButton.addActionListener(this);
		GridBagConstraints gbc_newEventButton = new GridBagConstraints();
		gbc_newEventButton.anchor = GridBagConstraints.NORTH;
		gbc_newEventButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_newEventButton.insets = new Insets(0, 0, 0, 5);
		gbc_newEventButton.gridwidth = 2;
		gbc_newEventButton.gridx = 1;
		gbc_newEventButton.gridy = 3;
		getContentPane().add(newEventButton, gbc_newEventButton);
		
		editEventButton = new JButton("Detaljer");
		editEventButton.setEnabled(false);
		editEventButton.addActionListener(this);
		GridBagConstraints gbc_editEventButton = new GridBagConstraints();
		gbc_editEventButton.anchor = GridBagConstraints.NORTH;
		gbc_editEventButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_editEventButton.insets = new Insets(0, 0, 0, 5);
		gbc_editEventButton.gridwidth = 2;
		gbc_editEventButton.gridx = 3;
		gbc_editEventButton.gridy = 3;
		getContentPane().add(editEventButton, gbc_editEventButton);
		
		deleteEventButton = new JButton("Slett valgt");
		deleteEventButton.setEnabled(false);
		deleteEventButton.addActionListener(this);
		GridBagConstraints gbc_deleteEventButton = new GridBagConstraints();
		gbc_deleteEventButton.anchor = GridBagConstraints.NORTH;
		gbc_deleteEventButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_deleteEventButton.insets = new Insets(0, 0, 0, 5);
		gbc_deleteEventButton.gridx = 5;
		gbc_deleteEventButton.gridy = 3;
		getContentPane().add(deleteEventButton, gbc_deleteEventButton);

		manageCalendarsButton = new JButton("Administrer kalendere");
		manageCalendarsButton.addActionListener(this);
		GridBagConstraints gbc_manageCalendarsButton = new GridBagConstraints();
		gbc_manageCalendarsButton.anchor = GridBagConstraints.NORTHEAST;
		gbc_manageCalendarsButton.gridwidth = 2;
		gbc_manageCalendarsButton.gridx = 7;
		gbc_manageCalendarsButton.gridy = 3;
		getContentPane().add(manageCalendarsButton, gbc_manageCalendarsButton);

		weekNumberBox = new JComboBox();
		weekNumberBox.setModel(new DefaultComboBoxModel(new String[] {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53"}));
		weekNumberBox.setSelectedIndex(calendarPane.getModel().getWeek() - 1);
		weekNumberBox.addActionListener(this);
		GridBagConstraints gbc_weekNumberBox = new GridBagConstraints();
		gbc_weekNumberBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_weekNumberBox.insets = new Insets(0, 0, 5, 5);
		gbc_weekNumberBox.gridx = 6;
		gbc_weekNumberBox.gridy = 1;
		getContentPane().add(weekNumberBox, gbc_weekNumberBox);
	}

	public JComboBox getNotificationsBox() {
		return notificationsBox;
	}

	public void setNotificationsBox(JComboBox notificationsBox) {
		this.notificationsBox = notificationsBox;
	}

	public JButton getEditEventButton() {
		return editEventButton;
	}

	public void setEditEventButton(JButton editEventButton) {
		this.editEventButton = editEventButton;
	}

	public JButton getDeleteEventButton() {
		return deleteEventButton;
	}

	public void setDeleteEventButton(JButton deleteEventButton) {
		this.deleteEventButton = deleteEventButton;
	}

	public JComboBox getWeekNumberBox() {
		return weekNumberBox;
	}

	public void setWeekNumberBox(JComboBox weekNumberBox) {
		this.weekNumberBox = weekNumberBox;
	}
	
	public void addEvent(Event event) {
		calendarPane.getModel().addEvent(event);
		calendarPane.updateCalendar();
	}
	
	public void changeEvent(Event model) {
		calendarPane.getModel().changeEvent(calendarPane.getSelectedEvent(), model);
		calendarPane.updateCalendar();
	}
	
	public void addUserCalendar(User user) {
		calendarPane.getModel().setAdditionalUser(user);
		calendarPane.updateCalendar();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()) {
		case "comboBoxChanged":
			if(e.getSource().equals(weekNumberBox)) {
				calendarPane.getModel().setWeek(weekNumberBox.getSelectedIndex() + 1);
				calendarPane.updateCalendar();
			}
			else if(e.getSource().equals(notificationsBox)) {
				
			}
			break;
		case "Velg dato":
			String pickedDate = new DatePicker(this).setPickedDate();
			dateField.setText(pickedDate);
			try {
				calendarPane.getModel().setYear(Integer.parseInt(new SimpleDateFormat("yyyy").format(new SimpleDateFormat("dd-MM-yyyy").parse(dateField.getText()))));
				calendarPane.getModel().setWeek(Integer.parseInt(new SimpleDateFormat("w").format(new SimpleDateFormat("dd-MM-yyyy").parse(dateField.getText()))));
				calendarPane.updateCalendar();
			} catch (NumberFormatException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			break;
		case "Logg ut":
			System.exit(0);
			break;
		case "Opprett hendelse":
			new NewEvent(program, program.getUser(), this);
			break;
		case "Administrer kalendere":
			new AddUsersCal(program, this);
			break;
		case "Detaljer":
			if(calendarPane.getSelectedEvent().getModel().getCreatedBy().getUserId() == program.getUser().getUserId()) {
				new ChangeEvent(program, calendarPane.getSelectedEvent().getModel(), this);
			}
			else {
				new ShowEvent(program, calendarPane.getSelectedEvent().getModel());
			}
			
			break;
		case "Slett valgt":
			EventView deleteEvent = calendarPane.getSelectedEvent();
			if(program.getConnectionManager().deleteEvent(deleteEvent.getModel())) {
				calendarPane.getModel().deleteEvent(deleteEvent);
				calendarPane.remove(deleteEvent);
				calendarPane.updateCalendar();
			}
			break;
		}
	}
}
