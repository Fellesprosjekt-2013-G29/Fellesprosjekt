package client;

import gui.CalendarView;
import gui.LoginWindow;
import model.User;

public class Program {
	
	private ConnectionManager connectionManager;
	private ClientConnection conn;
	private LoginWindow loginWindow;
	private User user;
	
	public Program() {
		connectionManager = new ConnectionManager(this);
		loginWindow = new LoginWindow(this);
	}	
	
	public void login(String hoststr, String username, String password){
		String hostname = "";
		int port = 0;
		
		System.out.println("u: " + username + " p: " + password);
		
		// Make sure password isn't empty
		if(password.length()==0){
			loginWindow.alert("Please type in your password");
			return;
		}
		
		if(!connectionManager.login(username, password)) {
			loginWindow.alert("Username or password is wrong!");
			return;
		}
		
		connectionManager.attachSocket();
		
		// ...
		loginWindow.alert("Login success");
		
		loginWindow.dispose();
		CalendarView calendar = new CalendarView(this);
		
	}
	
	public ConnectionManager getConnectionManager() {
		return connectionManager;
	}

	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}

	public static void main(String[] args) {
		new Program();	
	}
}
