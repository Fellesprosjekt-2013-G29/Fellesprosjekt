package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import model.Event;
import model.Invitation;
import model.Room;
import model.User;
import client.Program;

public class NewAppointment extends JPanel {
	
	private Program program;
	
	private Event model;
	private User owner;
	private JFrame thisFrame;
	private CalendarView parent; //<----------- fjern comment
	private NewAppointment child;
	
	private DateFormat eventDate = new SimpleDateFormat("HH-mm-dd-MM-yyyy");
	private Date startDate;
	private Date endDate;
	private String startHour = "00", startMinute = "00", endHour = "00", endMinute = "00";
	private String endDateString, startDateString;
	private String startDateTot;
	private String endDateTot;
	
	private Dimension labelDimension = new Dimension(120, 20);
	private Dimension smallButtDim = new Dimension(150, 20);
	private Dimension bigButtDim = new Dimension(100, 50);
	private Dimension dateButtDim = new Dimension(150, 30);
	
	private JTextField nameField; private JTextField fromDateField; private JTextField toDateField; private JTextField description;
	
	private JList userList;
	private DefaultListModel userListModel;
	private JScrollPane listScroller;
	private ArrayList<User> participantsList;
	
	private JComboBox fromHourBox;
	private JComboBox fromMinBox;
	private JComboBox toHourBox;
	private JComboBox toMinBox;
	private JComboBox alarmBox;
	private JComboBox roomBox;
	
	private ArrayList<Room> roomList = new ArrayList<Room>();
	private String[] minutesList = { "00", "15", "30", "45" };
	private String[] hoursList = { "00", "01", "02", "03", "04",  "05", "06", "07", "08", "09", "10", "11", "12",
			 "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"};
	private JButton addUsersButton;
	private JButton saveButton;
	private JButton cancelButton;
	private JButton startDateButton;
	private JButton endDateButton;
	
	private GridBagConstraints constr;
	
	public NewAppointment(Program program, User owner, CalendarView parent) { 
		
		this.program = program;
		this.parent = parent;
		this.child = this;
		this.owner = owner;
		model = new Event();
		
		JFrame frame = new JFrame("Opprett ny hendelse");
		frame.setVisible(true);
		this.setBackground(Color.WHITE);
		thisFrame = frame;
		
		participantsList = new ArrayList<User>();
		userListModel = new DefaultListModel();
		userList = new JList(userListModel);
		listScroller = new JScrollPane(userList);
		listScroller.setPreferredSize(new Dimension(250, 80));
		listScroller.setFocusable(false);
		
		nameField = new JTextField();
		fromDateField = new JTextField();
		toDateField = new JTextField();
		
		fromHourBox = new JComboBox();
		toHourBox = new JComboBox();
		fromMinBox = new JComboBox();
		toMinBox = new JComboBox();
		alarmBox = new JComboBox();
		roomBox = new JComboBox();
		userList = new JList();
		
		addUsersButton = new JButton("Legg til deltakere");
		saveButton =  new JButton("Lagre");
		cancelButton = new JButton("Abryt");
		startDateButton = new JButton("Velg startdato");
		endDateButton = new JButton("Velg sluttdato");
		
		description = new JTextField();
		constr = new GridBagConstraints();
		
		createGraphics();
		
		addListeners(thisFrame);
		
		frame.setContentPane(this);
		frame.pack();
	}

	private void createGraphics() {
		this.setLayout(new GridBagLayout());
		this.setBorder(new EmptyBorder(10,10,10,10));
		constr.fill = GridBagConstraints.NORTHEAST;
		constr.anchor = GridBagConstraints.LINE_START;
		Insets timeIns = new Insets(0,0,0,0);
		constr.insets = timeIns;
		
		// row1
		constr.gridy = 0;
		constr.gridx = 0; addLabel("Navn:");
		
		constr.gridx = 1; constr.gridwidth = 3;
		addTextField(nameField, true, 250, 30);
		timeIns.top = 10; //set distance top the element that is over
		//row2
		constr.gridwidth = 1;
		constr.gridy = 1;
		constr.gridx = 0; addLabel("Fra:");
		
		constr.gridx = 1; addDDList(fromHourBox);

		timeIns.left = 45;
		addDDList(fromMinBox);
		timeIns.left = 100;
		addTextField(fromDateField, false, 150, 30);
		timeIns.left = 0;
		constr.gridx = 3; addButton(startDateButton, 2);
		//row3
		constr.gridy = 2;
		constr.gridx = 0; addLabel("Til:");
		
		constr.gridx = 1; addDDList(toHourBox); 
		timeIns.left = 45;
		addDDList(toMinBox);
		timeIns.left = 100; 
		addTextField(toDateField, false, 150, 30);
		timeIns.left = 0;
		constr.gridx = 3; addButton(endDateButton, 2);
		//row4
		constr.gridy = 3;
		constr.gridx = 0; addLabel("Deltagere:");
		
		constr.gridx = 1; this.add(listScroller, constr);
		//row5
		constr.gridy = 4;
		constr.gridx = 1; addButton(addUsersButton, 1);
		//row6
		constr.gridy = 5;
		constr.gridx = 0; addLabel("Velg rom:");
		
		constr.gridx = 1; addDDList(roomBox);
		//row7
		constr.gridy = 6;
		constr.gridx = 0; addLabel("Beskrivelse:");
		//row8
		constr.fill = GridBagConstraints.HORIZONTAL;
		constr.gridy = 7;constr.gridwidth = GridBagConstraints.REMAINDER;
		constr.gridx = 0; addTextField(description, true, 250, 150);
		//row9
		constr.fill = GridBagConstraints.NONE;
		constr.gridy = 8; constr.gridwidth = 1;
		constr.gridx = 0; addLabel("Alarm(ant. min f�r):");
		
		constr.gridx = 1; addDDList(alarmBox);
		//row10
		constr.gridy = 9; constr.anchor = GridBagConstraints.SOUTHWEST;
		constr.gridx = 0; addButton(saveButton, 0);
		
		constr.gridwidth = GridBagConstraints.REMAINDER;
		constr.anchor = GridBagConstraints.SOUTHEAST;
		constr.gridx = 1; addButton(cancelButton, 0);
	}
	
	private void addDDList(JComboBox box) {
		int count = 0;
		if (box == alarmBox) {
			for(int i = 0; i < 12; i++) {
				box.addItem(count);
				count += 5;}
		}
		else if(box == toHourBox || box == fromHourBox){
			for(int i = 0; i < hoursList.length; i++) {
				box.addItem(hoursList[i]);}
		}
		else if(box == toMinBox || box == fromMinBox){
			for(int i = 0; i < minutesList.length; i++) {
				box.addItem(minutesList[i]);}
		}
		else {
			box.setPreferredSize(new Dimension(80,20));
		}
		this.add(box, constr);
	}
	private void addLabel(String text) {
		JLabel label = new JLabel(text);
		label.setPreferredSize(labelDimension);
		this.add(label, constr);
	}
	private void addButton(JButton button, int k) {
		Dimension dim;
		if (k == 1) {dim = smallButtDim;} 
		else if(k == 2) {dim = dateButtDim;}
		else {dim = bigButtDim;}
		button.setPreferredSize(dim);
		this.add(button, constr);
	}
	private void addTextField (JTextField field, boolean edit, int y, int x) {
		field.setPreferredSize(new Dimension(y, x));
		if (edit == false) {field.setEditable(false);}
		if (field == description) {constr.gridwidth = GridBagConstraints.REMAINDER;}
		else {constr.gridwidth = 1;}
		this.add(field, constr);
	}
	
	// Handling off adding available rooms
	private void getRooms() throws ParseException {
		// If a date field is empty, do not get a list of rooms
		if (isEmpty()) {
			return;
		}
		startDateTot = startHour + "-" + startMinute + "-"+ startDateString;
		endDateTot = endHour + "-" + endMinute + "-" + endDateString;
		
		startDate = eventDate.parse(startDateTot);
		endDate = eventDate.parse(endDateTot);

		// gets available rooms from DB
		// roomList = findAvailableRooms(startDate, endDate);
		// Adds rooms to combobox
		if (roomList.size() > 0) {
			for(int i = 0; i < roomList.size(); i++) {
				roomBox.addItem(roomList.get(i));}
		}
	}
	// Checks if date field is empty
	private boolean isEmpty() {
		String date1 = fromDateField.getText();
		String date2 = toDateField.getText();
		if(date1.isEmpty() || date2.isEmpty() ) {return true;}
		return false;
	}
	// Receives users from return list from addUserPanel and adds them into the JList
	public void addUsersToList(ArrayList<User> persList) {
		userListModel.clear();
		participantsList = persList;

		for (int i = 0; i < participantsList.size(); i++) {
			User pers = participantsList.get(i);
			userListModel.addElement(pers.getName());
		}
	}
	private void sendModel() {
		parent.addEvent(model); //<------------  fjern commennt
	}
	public Event getModel() {
		return this.model;
	}
	public void setModel(Event model){
		this.model = model;
	}

	private void addListeners(final JFrame thisparent) {

		saveButton.addActionListener(new ActionListener() {
		        public void actionPerformed(ActionEvent e) {
		     
		        	model.setTitle(nameField.getText());
		        	model.setCreatedBy(owner);
		        	model.setStart(new Timestamp(startDate.getTime()));
		        	model.setEnd(new Timestamp(endDate.getTime()));
		            model.setDescription(description.getText());
		            
		            int roomIndex = roomBox.getSelectedIndex();
		            if (roomIndex >= 0) {
		            	model.setRoom(roomList.get(roomIndex));
		            }
		            
		            int timeBefore = (int) alarmBox.getSelectedItem(); 
		            if (timeBefore > 0) {
		            	model.setAlarm(true);
		            	model.setAlarmBefore(timeBefore);
		            }
		            else {model.setAlarm(false);}
		            
		            if(userListModel.getSize() > 0 ) {
			            ArrayList<Invitation> invitationList = new ArrayList<Invitation>();
			            for (int i = 0; i < userListModel.getSize(); i++) {
			            	User user = participantsList.get(i);
			            	Invitation invite = new Invitation();
			            	invite.setFrom(owner);
			            	invite.setTo(user);
			            	invite.setEvent(model);
			            	invitationList.add(invite);
			            }
			            model.setParticipants(invitationList);
		            }
		            
		            // store event in DB 
		            //storeEvent(Event model); <------- implementer
		            
		            //sends event-model to calendar
		            sendModel();
		            thisFrame.dispose();
		        }
			});
			
		cancelButton.addActionListener(new ActionListener() {
		        public void actionPerformed(ActionEvent e) {
		        	thisFrame.dispose();
		        }
			});
		
		fromHourBox.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e){
				startHour = (String)fromHourBox.getSelectedItem();
				try {
					getRooms();
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
			}
		});
		fromMinBox.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e){
				startMinute = (String)fromMinBox.getSelectedItem();
				try {
					getRooms();
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
			}
		});
		toHourBox.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e){
				endHour = (String)toHourBox.getSelectedItem();
				try {
					getRooms();
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
			}
		});
		toMinBox.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e){
				endMinute = (String)toMinBox.getSelectedItem();
				try {
					getRooms();
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
			}
		});
		startDateButton.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	            fromDateField.setText(new DatePicker(thisparent).setPickedDate());
	            startDateString = (String)fromDateField.getText();
	            try {
					getRooms();
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
	        }
		});
		endDateButton.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	            toDateField.setText(new DatePicker(thisparent).setPickedDate());
	            endDateString = (String)toDateField.getText();
	            try {
					getRooms();
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
	        }
		});
		addUsersButton.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) { 	
	        	// AddParticipants addPartPanel = new AddParticipants(participantsList, child);// <-- add comment
	        	AddParticipants addPartPanel = new AddParticipants(participantsList, child); //<---- fjern comment
	        }
		});
	}
}


