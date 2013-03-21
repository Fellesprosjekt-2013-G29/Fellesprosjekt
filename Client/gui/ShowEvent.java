package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import model.Event;
import model.Invitation;
import model.InvitationAnswer;
import model.Room;
import model.User;
import client.Program;

public class ShowEvent extends JPanel {

	private Program program;

	private GridBagConstraints constr;
	private Dimension labelDimension = new Dimension(120, 20);
	private Dimension dateDimension = new Dimension(150, 20);
	private Dimension bigButtDim = new Dimension(80, 30);

	private Event model;
	private JFrame parent;

	private JComboBox alarmBox;
	private JComboBox answerBox;

	private JList userList;
	private DefaultListModel userListModel;
	private JScrollPane listScroller;

	private JTextArea description;
	private JButton saveButton;
	private JButton cancelButton;

	public ShowEvent(Program program, Event model) {
		JFrame frame = new JFrame("Informasjon om hendelse");
		frame.setVisible(true);
		this.setBackground(Color.WHITE);
		parent = frame;

		this.program = program;

		this.model = model;
		constr = new GridBagConstraints();

		description = new JTextArea();
		alarmBox = new JComboBox();
		answerBox = new JComboBox();

		userListModel = new DefaultListModel();
		userList = new JList(userListModel);
		listScroller = new JScrollPane(userList);
		listScroller.setPreferredSize(new Dimension(250, 80));
		listScroller.setFocusable(false);

		saveButton = new JButton("Lagre");
		cancelButton = new JButton("Avbryt");

		this.createGraphics();

		addListeners();

		frame.setContentPane(this);
		frame.pack();
	}

	private void createGraphics() {
		this.setLayout(new GridBagLayout());
		this.setBorder(new EmptyBorder(10, 10, 10, 10));
		Insets timeIns = new Insets(0, 0, 0, 0);
		constr.insets = timeIns;

		constr.fill = GridBagConstraints.NORTHEAST;
		// row 1
		constr.gridy = 0;
		constr.anchor = GridBagConstraints.CENTER;
		constr.gridx = 0;
		constr.gridwidth = 2;
		addLabel(model.getTitle());
		constr.gridx = 1;
		timeIns.top = 8;
		// row 2
		constr.gridy = 1;
		constr.anchor = GridBagConstraints.LINE_START;
		constr.gridx = 0;
		constr.gridwidth = 1;
		addLabel("Dato:");
		constr.gridx = 1;
		addLabelDate(model.getStart(), model.getEnd());
		// row 3
		constr.gridy = 2;
		constr.gridx = 0;
		addLabel("Tid:");
		constr.gridx = 1;
		addLabelTime(model.getStart(), model.getEnd());
		// row 4
		constr.gridy = 3;
		constr.gridx = 0;
		addLabel("Sted:");
		constr.gridx = 1;
		addLabelRoom(model.getRoom());
		// row 5
		constr.gridy = 4;
		constr.gridx = 0;
		addLabel("Alarm(ant min f�r):");
		constr.gridx = 1;
		addDDList(alarmBox);
		// row 6
		constr.gridy = 5;
		constr.gridx = 0;
		addLabel("Din status:");
		constr.gridx = 1;
		addDDList(answerBox);
		// row 7
		constr.gridwidth = 2;
		constr.gridy = 6;
		constr.gridx = 0;
		addLabel("Deltakere:");
		// row 8
		constr.gridy = 7;
		constr.gridx = 0;
		this.add(listScroller, constr);
		addUsersToList(model.getParticipants());
		// row 9
		constr.gridy = 8;
		constr.gridx = 0;
		addLabel("Beskrivelse:");
		// row 10
		constr.gridy = 9;
		constr.gridx = 0;
		addTextArea(description, false, 250, 150, model.getDescription());

		// row 11
		constr.gridy = 10;
		constr.gridx = 1;
		constr.gridx = 0;
		addButton(saveButton);
		constr.anchor = GridBagConstraints.SOUTHEAST;
		constr.gridx = 1;
		addButton(cancelButton);
	}

	private void addLabel(String text) {
		JLabel label = new JLabel(text);
		label.setPreferredSize(labelDimension);
		this.add(label, constr);
	}

	private void addLabelDate(Date dStart, Date dEnd) {
		;
		JLabel label = new JLabel();
		label.setPreferredSize(dateDimension);
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String start = dateFormat.format(dStart).toString();
		String end = dateFormat.format(dEnd).toString();
		if (dStart.equals(dEnd)) {
			label.setText(start);
		} else {
			label.setText(start + " - " + end);
		}
		this.add(label, constr);
	}

	private void addLabelTime(Date dStart, Date dEnd) {
		JLabel label = new JLabel();
		label.setPreferredSize(dateDimension);
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
		String start = dateFormat.format(dStart).toString();
		String end = dateFormat.format(dEnd).toString();
		if (dStart.equals(dEnd)) {
			label.setText(start);
		} else {
			label.setText(start + " - " + end);
		}
		this.add(label, constr);
	}

	private void addLabelRoom(Room room) {
		JLabel label = new JLabel("");
		if(room != null) {
			int room1 = room.getRoomNumber();
			String loc = room.getLocation();
			label.setText(room1 + " i " + loc);
		}
		label.setPreferredSize(dateDimension);
		this.add(label, constr);
	}

	private void addUsersToList(ArrayList<Invitation> inviteList) {
		if (inviteList == null || inviteList.isEmpty()) {
			return;
		} else {
			for (int i = 0; i < inviteList.size(); i++) {
				Invitation invite = inviteList.get(i);
				User pers = invite.getTo();
				userListModel.addElement(pers.getName());
			}
		}
	}

	private void addTextArea(JTextArea area, boolean edit, int y, int x,
			String txt) {
		area.setPreferredSize(new Dimension(y, x));
		area.setBorder(new MatteBorder(1, 1, 1, 1, Color.BLACK));
		area.setText(txt);
		if (edit == false) {
			area.setEditable(false);
		} else {
			constr.gridwidth = 1;
		}
		this.add(area, constr);
	}

	private void addButton(JButton button) {
		Dimension dim = bigButtDim;
		button.setPreferredSize(dim);
		this.add(button, constr);
	}

	private void addDDList(JComboBox box) {
		int count = 0;
		if (box == alarmBox) {
			for (int i = 0; i < 12; i++) {
				box.addItem(count);
				count += 5;
			}
		} else {
			for (int i = 2; i >= 0; i--) {
				InvitationAnswer answer = InvitationAnswer.values()[i];
				box.addItem(answer);
			}
		}
		this.add(box, constr);
	}

	private void addListeners() {
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// int timeBefore = (int) alarmBox.getSelectedItem();
				// if (timeBefore > 0) {
				// model.setAlarm(true);
				// model.setAlarmBefore(timeBefore);
				// }
				// else {model.setAlarm(false);}

				// storeEvent(Event model);? <--------- implementer
				// storeAlarm();? <------------ implementer

				// storeInvitationAnswer();? <----- implementer

				if (answerBox.getSelectedItem().equals("Decline")) {
					// removeEventFromCalender(model); <------- kanskje
					// implementer?
				}
				parent.dispose();
			}
		});
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parent.dispose();
			}
		});
	}
}
