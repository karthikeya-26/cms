package com.models;

import java.io.Serializable;

public class SessionUserData implements Serializable {
	
	private User user;
	private long created_time;
	private long last_accessed_at;
	private long expires_at;
	
	public long getCreated_time() {
		return created_time;
	}
	public void setCreated_time(long created_time) {
		this.created_time = created_time;
	}
	public long getLast_accessed_at() {
		return last_accessed_at;
	}
	public void setLast_accessed_at(long last_accessed_at) {
		this.last_accessed_at = last_accessed_at;
	}
	public long getExpires_at() {
		return expires_at;
	}
	public void setExpires_at(long expires_at) {
		this.expires_at = expires_at;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	@Override
	public String toString() {
		return "user_ob :"+user+", created_at :"+created_time+", last_accessed_at :"+last_accessed_at+", expires_at :"+expires_at;
	}
}
