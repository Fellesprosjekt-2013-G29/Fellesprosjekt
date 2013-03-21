package client;

import gui.CalendarView;
import gui.LoginWindow;
import model.User;

public class Program 
{
	
	private ConnectionManager connectionManager;
	private LoginWindow loginWindow;
	private User user;
	
	private String host = "localhost";
	private int port = 4447;
	
	public Program() {
		connectionManager = new ConnectionManager(this, host, port);
		loginWindow = new LoginWindow(this);
	}	
	
	public void login(String hoststr, String username, String password)
	{	
		loginWindow.alert("logging inn...");
		if(password.length()==0){
			loginWindow.alert("Skriv inn ditt passord");
			return;
		}
		
		String connectionResult = connectionManager.login(username, password);
		
		loginWindow.alert(connectionResult);

		if(!connectionResult.equals("Login ok"))
			return;
		
		connectionManager.attachSocket();
		
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
