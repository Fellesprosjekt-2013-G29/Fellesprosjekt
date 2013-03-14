package model;

public class Person {
	private int userId = 0;
	private String name = "";
	private String email = "";
	private String password = "";
	
	public Person(){
		
	}
	
	
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}
	
	
	public void setUserId(int id){
		this.userId = id;
	}
	
	public int getUserId(){
		return userId;
	}
}