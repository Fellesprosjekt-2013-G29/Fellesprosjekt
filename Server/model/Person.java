package model;

import java.util.List;
import java.util.ArrayList;

public class Person implements GroupMember{
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


	@Override
	public List<Person> allMembers() {
		List<Person> theList = new ArrayList<Person>();
		theList.add(this);
		return theList;
	}
}
