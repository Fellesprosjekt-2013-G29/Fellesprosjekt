package client;

import gui.LoginWindow;

import javax.swing.JFrame;

public class Program {
	private ClientConnection conn;
	private LoginWindow loginWindow;
	Program(){
		loginWindow = new LoginWindow(this);
	}	
	
	public void login(String hoststr, String username, String password){
		String hostname = "";
		int port = 0;
		
		
		// Client side validation
		
		
		// check for sane host string
		if(hoststr.contains(":") && hoststr.replaceAll("[^:]", "").length() == 1){ // make sure we contain 1 and only 1 colon character.
			hostname = hoststr.split(":")[0];
			port = Integer.parseInt(hoststr.split(":")[1]);
			
			
			System.out.println(hostname);
			System.out.println(port);
		} else{
			loginWindow.alert("Invalid hostname. <ip>:<port>");
			System.out.println(hostname.contains(":"));
			System.out.println( hostname.replaceAll("[^:]", "").length());
			return;
		}
		
		// Make sure password isn't empty
		if(password.length()==0){
			loginWindow.alert("Please type in your password");
			return;
		}
		
		// ...
		loginWindow.alert("Login success");
		
	}
	
	public static void main(String[] args) {
		new Program();	
	}
	
}
