package model;

import java.util.ArrayList;
import java.util.List;

public class Group implements GroupMember {
	private String groupName;
	private ArrayList<GroupMember> members;
	public Group(String groupName){
		this.groupName = groupName;
		this.members = new ArrayList<GroupMember>();
	}
	
	@Override
	public List<Person> allMembers() {
		ArrayList<Person> result = new ArrayList<Person>();
		for (GroupMember child : this.members){
			result.addAll(child.allMembers());
		}
		return result;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public ArrayList<GroupMember> getMembers() {
		return members;
	}

	public void setMembers(ArrayList<GroupMember> members) {
		this.members = members;
	}
}