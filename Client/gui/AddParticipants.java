package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import model.User;
import client.Program;

public class AddParticipants extends JPanel{
	
	private Program program;
	
	private JFrame thisFrame;
	private ChangeEvent parentChange = null;
	private NewEvent parentNew = null;
	
	private GridBagConstraints constr;
	
	private JList userList1;
	private JList userList2;
	private DefaultListModel userListModel1;
	private DefaultListModel userListModel2;
	private JScrollPane listScroller1;
	private JScrollPane listScroller2;
	
	private ArrayList<User> notAddedList;
	private ArrayList<User> addedList;
	
	private Dimension smallButtDim = new Dimension(90, 30);
	private Dimension bigButtDim = new Dimension(100, 50);
	private Dimension labelDimension = new Dimension(120, 20);
	private Dimension removeButtDim = new Dimension(200, 30);
	private Dimension scrollDim = new Dimension(250, 150);
	
	private JButton saveButton;
	private JButton cancelButton;
	private JButton searchButton;
	private JButton addAllButton;
	private JButton addButton;
	private JButton removeButton;
	
	private JTextField searchField;
	
	public AddParticipants(Program program, ArrayList<User> oldList, Object parent) { 

		if(parent instanceof NewEvent) {
			this.parentNew = (NewEvent) parent;
		}
		else if(parent instanceof ChangeEvent) {
			this.parentChange = (ChangeEvent) parent;
		}
		
		this.program = program;

		JFrame frame = new JFrame("Valg av deltakere");
		frame.setVisible(true);
		this.setBackground(Color.WHITE);
		thisFrame = frame;
		constr = new GridBagConstraints();

		userListModel1 = new DefaultListModel();
		userListModel2 = new DefaultListModel();
		userList1 = new JList(userListModel1);
		userList2 = new JList(userListModel2);
		
		notAddedList = new ArrayList<User>();
		addedList = new ArrayList<User>();
		
		listScroller1 = new JScrollPane(userList1);
		listScroller1.setPreferredSize(scrollDim);
		listScroller1.setFocusable(false);
		
		listScroller2 = new JScrollPane(userList2);
		listScroller2.setPreferredSize(scrollDim);
		listScroller2.setFocusable(false);
		
		saveButton = new JButton("Lagre");
		cancelButton = new JButton("Abryt");
		searchButton = new JButton("S�k");
		addAllButton = new JButton("Velg alle");
		addButton = new JButton("Legg til");
		removeButton = new JButton("Fjern");
		searchField = new JTextField();;
		
		createGraphics();

		// Gets all users from DB
		ArrayList<User> tempUsers = program.getConnectionManager().getUsers();
		for(User u : tempUsers) {
			if(u.getUserId() == program.getUser().getUserId()) {
				tempUsers.remove(u);
				break;
			}
		}
		addUsersToList(oldList, tempUsers);
		
		addListeners();
		
		frame.setContentPane(this);
		frame.pack();

	}
	
	public void init(Program program) {

	}
	
	private void createGraphics() {
		this.setLayout(new GridBagLayout());
		this.setBorder(new EmptyBorder(10,10,10,10));
		Insets timeIns = new Insets(10,0,0,0);
		constr.insets = timeIns;
		// Row 1
		constr.anchor = GridBagConstraints.CENTER;
		constr.gridy = 0; constr.gridwidth = 4;
		constr.gridx = 0; addLabel("Hvem leter du etter?");
		// Row 2
		constr.anchor = GridBagConstraints.EAST;
		constr.gridy = 1; constr.gridwidth = 3;
		constr.anchor = GridBagConstraints.EAST;
		constr.gridx = 0; constr.anchor = GridBagConstraints.EAST;addTextField(searchField, 200, 20);
		constr.gridwidth = 1;
		constr.anchor = GridBagConstraints.LINE_START;;
		constr.gridx = 3; addButton(searchButton, 1);
		// Row 3
		constr.gridy = 2; constr.gridwidth = 2;
		constr.gridx = 0; addLabel("Brukere:");
		constr.gridx = 2; addLabel("Valgte Deltakere:");
		// Row 4
		constr.gridy = 3;
		timeIns.right = 10;
		constr.gridx = 0; this.add(listScroller1, constr);
		timeIns.right = 0;
		constr.gridx = 2; this.add(listScroller2, constr);
		// Row 5
		constr.gridy = 4; constr.gridwidth = 1;
		timeIns.right = 10;
		constr.gridx = 0; addButton(addAllButton, 1);
		constr.anchor = GridBagConstraints.LINE_END;
		constr.gridx = 1; addButton(addButton, 1);
						  constr.gridwidth = GridBagConstraints.REMAINDER;
						  constr.anchor = GridBagConstraints.CENTER;
		timeIns.right = 0;
		constr.gridx = 2; addButton(removeButton, 2);
		// Row 6
		timeIns.top = 30;
		constr.gridy = 5; constr.gridwidth = 1;
		constr.gridx = 0; addButton(saveButton, 0);
		constr.gridwidth = GridBagConstraints.REMAINDER; 
		constr.anchor = GridBagConstraints.EAST;
		constr.gridx = 3; addButton(cancelButton, 0);
	}
	private void addLabel(String text) {
		JLabel label = new JLabel(text);
		label.setPreferredSize(labelDimension);
		this.add(label, constr);
	}
	private void addTextField (JTextField field, int y, int x) {
		field.setPreferredSize(new Dimension(y, x));
		this.add(field, constr);
	}
	private void addButton(JButton button, int k) {
		Dimension dim;
		if (k == 1) {
			dim = smallButtDim;}
		else if(k == 2) {
			dim = removeButtDim;}
		else {
			dim = bigButtDim;}
		button.setPreferredSize(dim);
		this.add(button, constr);
	}
	
	private void addUsersToList(ArrayList<User> oldList, ArrayList<User> list) {
		notAddedList.clear();
		userListModel1.clear();
		notAddedList = list;
		
		if(oldList.size() > 0) {
			addedList = oldList;
			for (int i = 0; i < addedList.size(); i++) {
				User pers = addedList.get(i);
				userListModel2.addElement(pers.getName());
				for (int k = 0; k < notAddedList.size(); k++) {
					if(pers.getName().equals(notAddedList.get(k).getName())) {
						notAddedList.remove(k);
					}
				}
			}
		}	
		for (int i = 0; i < notAddedList.size(); i++) {
			User pers = notAddedList.get(i);
			userListModel1.addElement(pers.getName());
		}
	}
	private void sendAddedList() {
		if (addedList.size() == 0) {
			return;
		}
		else {
			if (parentNew == null) {
				parentChange.addUsersToList(addedList);
			}
			else if (parentChange == null)
				parentNew.addUsersToList(addedList);
		}
	}
	
	private void addListeners() {
		
		saveButton.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	        	sendAddedList();
	        	thisFrame.dispose();
	        }
		});
		cancelButton.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	        	thisFrame.dispose();
	        }
		});
		searchButton.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	        	// Code to get users from DB and store, to a given criteria..?
	        	// tempList = getUsersFromDB(searchField.getText()); <------------ implementer
	        	// addUsersToList(tempList); <----------- fjern comment
	        }
		});
		addAllButton.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	        	if (userListModel1.size() == 0) {return;} 
	        	int  size = userListModel1.size();
	    		for (int i = 0; i < size; i++) {
	    			User pers = notAddedList.get(i);
		        	addedList.add(pers);
		        	userListModel2.addElement(pers.getName());
	    		}
	    		notAddedList.clear();
	    		userListModel1.removeAllElements();
	        }
		});
		addButton.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	        	if (userList1.getSelectedValue() == null) {return;}
	        	else {
		        	int tempPos = userList1.getSelectedIndex();
		        	User pers = notAddedList.get(tempPos);
		        	notAddedList.remove(tempPos);
		        	addedList.add(pers);
		        	userListModel1.remove(tempPos);
		        	userListModel2.addElement(pers.getName());
	        	}
	        }
		});
		removeButton.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	        	if (userList2.getSelectedValue() == null) {return;}
				else {
		        	int tempPos = userList2.getSelectedIndex();
		        	User pers = addedList.get(tempPos);
		        	addedList.remove(tempPos);
		        	notAddedList.add(pers);
		        	userListModel1.addElement(pers.getName());
		        	userListModel2.remove(tempPos);
				}
	        }
		});
	}
}
