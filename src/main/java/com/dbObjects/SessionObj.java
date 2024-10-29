package com.dbObjects;

public class SessionObj extends ResultObject{
	
	public String session_id;
	public Integer user_id;
	public Long created_at;
	public Long last_accessed_at;
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
		return created_at;
	}
	public void setCreated_at(Long created_at) {
		this.created_at = created_at;
	}
	public Long getLast_accessed_at() {
		return last_accessed_at;
	}
	public void setLast_accessed_at(Long last_accessed_at) {
		this.last_accessed_at = last_accessed_at;
	}
	
	

}
