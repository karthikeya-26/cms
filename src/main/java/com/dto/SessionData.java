package com.dto;

public class SessionData {

	private Integer user_id;
	private Long created_time;
	private Long last_accessed_time;
	private Long expires_at;

	public SessionData() {

	}

	public SessionData(Integer user_id, Long created_at, Long last_accessed_at, Long expires_at) {
		this.user_id = user_id;
		this.created_time = created_at;
		this.last_accessed_time = last_accessed_at;
		this.expires_at = expires_at;
	}

	public Long getCreated_time() {
		return created_time;
	}

	public void setCreated_time(Long created_time) {
		this.created_time = created_time;
	}

	public Long getLast_accessed_time() {
		return last_accessed_time;
	}

	public void setLast_accessed_at(Long last_accessed_at) {
		this.last_accessed_time = last_accessed_at;
	}

	public Long getExpires_at() {
		return expires_at;
	}

	public void setExpires_at(Long expires_at) {
		this.expires_at = expires_at;
	}

	public Integer getUser_id() {
		return user_id;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	@Override
	public String toString() {
		return "user_id :" + user_id + ", created_at :" + created_time + ", last_accessed_at :" + last_accessed_time
				+ ", expires_at :" + expires_at;
	}
}
