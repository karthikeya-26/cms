package com.dbObjects;

public class SessionsObj extends ResultObject{
	
	public String session_id;
	public Integer user_id;
	public Long created_time;
	public Long last_accessed_time;
	
	public String getSession_id() {
		return session_id;
	}
	public void setSession_id(String session_id) {
		this.session_id = session_id;
	}
	public Integer getUser_id() {
		return user_id;
	}
	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}
	public Long getCreated_at() {
		return created_time;
	}
	public void setCreated_at(Long created_at) {
		this.created_time = created_at;
	}
	public Long getLast_accessed_at() {
		return last_accessed_time;
	}
	public void setLast_accessed_at(Long last_accessed_at) {
		this.last_accessed_time = last_accessed_at;
	}
	@Override
	public String toString() {
		return "SessionObj [session_id=" + session_id + ", user_id=" + user_id + ", created_time=" + created_time
				+ ", last_accessed_time=" + last_accessed_time + "]";
	}
	
	

}