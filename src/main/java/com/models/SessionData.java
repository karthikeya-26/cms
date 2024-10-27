package com.models;

import java.time.LocalDateTime;

public class SessionData  {
	
	private int user_id;
	private long created_time;
	private long last_accessed_at;
	private long expires_at;
	
	public SessionData() {
		
	}
	
	public SessionData(int user_id, long created_at, long last_accessed_at, long expires_at) {
		this.user_id = user_id;
		this.created_time = created_at;
		this.last_accessed_at = last_accessed_at;
		this.expires_at = expires_at;
	}
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
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	@Override
	public String toString() {
		return "user_id :"+user_id+", created_at :"+created_time+", last_accessed_at :"+last_accessed_at+", expires_at :"+expires_at;
	}
}
