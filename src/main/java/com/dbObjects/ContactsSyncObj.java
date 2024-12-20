package com.dbObjects;

public class ContactsSyncObj extends ResultObject {
	
	private Integer user_id;
	private String refresh_token;
	private String provider;
	
	public Integer getUser_id() {
		return user_id;
	}
	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}
	public String getRefresh_token() {
		return refresh_token;
	}
	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	
	@Override
	public String toString() {
		return "ContactsSyncObj [user_id=" + user_id + ", refresh_token=" + refresh_token + ", provider=" + provider
				+ "]";
	}
}
