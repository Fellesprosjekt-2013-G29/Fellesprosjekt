package client;

import gui.CalendarView;
import gui.LoginWindow;
import model.User;

public class Program {
	
	private ConnectionManager connectionManager;
	private ClientConnection conn;
	private LoginWindow loginWindow;
	private User user = new User();
	
	public Program() {
		connectionManager = new ConnectionManager(this);
		loginWindow = new LoginWindow(this);
	}	
	
	public void login(String hoststr, String username, String password){
		String hostname = "";
		int port = 0;
		
		System.out.println("u: " + username + " p: " + password);
		
		
		// Client side validation
		
		
		// check for sane host string
//		if(hoststr.contains(":") && hoststr.replaceAll("[^:]", "").length() == 1){ // make sure we contain 1 and only 1 colon character.
//			hostname = hoststr.split(":")[0];
//			port = Integer.parseInt(hoststr.split(":")[1]);
//			
//			
//			System.out.println(hostname);
//			System.out.println(port);
//		} else{
//			loginWindow.alert("Invalid hostname. <ip>:<port>");
//			System.out.println(hostname.contains(":"));
//			System.out.println( hostname.replaceAll("[^:]", "").length());
//			return;
//		}
		
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
