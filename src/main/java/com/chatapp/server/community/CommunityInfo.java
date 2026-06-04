package com.chatapp.server.community;

import java.util.List;

import com.chatapp.server.user.User;

public class CommunityInfo {
	
	private CommunitySearchResults info;
	private List<User> members;
	
	public CommunityInfo(CommunitySearchResults info, List<User> members)
	{
		this.info = info;
		this.members = members;
	}
	
	public CommunitySearchResults getInfo() {
		return info;
	}
	public void setInfo(CommunitySearchResults info) {
		this.info = info;
	}
	public List<User> getMembers() {
		return members;
	}
	public void setMembers(List<User> members) {
		this.members = members;
	}
}
