package gui;

import static java.awt.GridBagConstraints.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import client.ClientConnection;
import client.Program;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.prefs.Preferences;

public class LoginWindow extends JFrame{
	// Default values for host and username
	Preferences prefs;
	Program mainProgram;

	private JLabel alert = new JLabel("Velkommen!");
	private JTextField host = new JTextField();
	private JTextField username = new JTextField();
	private JPasswordField password = new JPasswordField();
	
	private JButton loginButton = new JButton("Login");
	
	
	private JLabel hostLabel = new JLabel("Host:port");
	private JLabel usernameLabel = new JLabel("E-post");
	private JLabel passwordLabel = new JLabel("Password");
	
	private JPanel pane = new JPanel(new GridBagLayout());

	public LoginWindow(Program mainProgram){
		// Call super to create window with title
		super("Login");
		this.mainProgram = mainProgram;
		setVisible(false);
		setSize(300, 180);
	    doTheLayout();
		getContentPane().add(pane);
	    setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Get the screen size  
		GraphicsConfiguration gc = getGraphicsConfiguration();  
		Rectangle bounds = gc.getBounds();  
		
		// Set the Location and Activate  
		Dimension size = getPreferredSize();  
		setLocation((int) ((bounds.width / 2) - (size.getWidth() / 2)),  
		                    (int) ((bounds.height / 2) - (size.getHeight() / 2)));

		setResizable(false);
		setVisible(true);  
		
		loginButton.addActionListener(new buttonListener());
		password.addKeyListener(new buttonListener());
	}
	
	public void alert(String input){
		alert.setText(input);
	}
	
	
	private void doTheLayout(){
	    prefs = Preferences.userRoot().node(this.getClass().getName());
	    host.setText(prefs.get("HOST", ""));
	    username.setText(prefs.get("USERNAME", ""));
		
		
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(3, 7, 3, 7);
		c.gridwidth = 2;
		pane.add(alert, c);
		c.gridwidth = 1;
		c.gridx = 0;
		
		c.anchor = LINE_END;
			c.gridy = 1;
			pane.add(hostLabel, c);

			c.gridy = 2;
			pane.add(usernameLabel, c);

			c.gridy = 3;
			pane.add(passwordLabel, c);
		c.gridx=1;
		c.anchor = LINE_START;
		c.weightx = 1.0d;
		c.fill = HORIZONTAL;
			c.gridy = 1;
			pane.add(host, c);

			c.gridy = 2;
			pane.add(username, c);

			c.gridy = 3;
			pane.add(password,c);
			c.gridy = 4;
			
			c.gridy = 5;
			pane.add(loginButton, c);
	}
	
	// Action Listener for Login button
	class buttonListener implements ActionListener, KeyListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			
			Object src = e.getSource();
			if( src.equals(loginButton)){
				System.out.println("login event");
				// Save host and username
				prefs.put("USERNAME", username.getText());
				prefs.put("HOST", host.getText());
				
				String pass = new String(password.getPassword());
				
				try {
					mainProgram.login(host.getText(), username.getText(), pass);
				} catch (Exception e1) {
					alert(e1.getMessage());
				}
			}	
		}

		@Override
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == 10) {
				loginButton.doClick();
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			
		}

		@Override
		public void keyTyped(KeyEvent e) {
			
		}
	}
}
