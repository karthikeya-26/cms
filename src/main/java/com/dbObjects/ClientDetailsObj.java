package com.dbObjects;

public class ClientDetailsObj {
	private String client_id;
	private Integer user_id;
	private String client_name;
	private String client_type;
	private Long created_at;
	private Long modified_at;
	public String getClient_id() {
		return client_id;
	}
	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}
	public Integer getUser_id() {
		return user_id;
	}
	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}
	public String getClient_name() {
		return client_name;
	}
	public void setClient_name(String client_name) {
		this.client_name = client_name;
	}
	public String getClient_type() {
		return client_type;
	}
	public void setClient_type(String client_type) {
		this.client_type = client_type;
	}
	public Long getCreated_at() {
		return created_at;
	}
	public void setCreated_at(Long created_at) {
		this.created_at = created_at;
	}
	public Long getModified_at() {
		return modified_at;
	}
	public void setModified_at(Long modified_at) {
		this.modified_at = modified_at;
	}
	@Override
	public String toString() {
		return "ClientDetailsObj [client_id=" + client_id + ", user_id=" + user_id + ", client_name=" + client_name
				+ ", client_type=" + client_type + ", created_at=" + created_at + ", modified_at=" + modified_at + "]";
	}
	
}
