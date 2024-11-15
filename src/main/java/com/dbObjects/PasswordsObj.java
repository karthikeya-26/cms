package com.dbObjects;

public class PasswordsObj extends ResultObject{
	private Integer id;
	private Integer user_id;
	private String password;
	private Integer pw_version;
	private Long modified_at;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getUser_id() {
		return user_id;
	}
	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Integer getPw_version() {
		return pw_version;
	}
	public void setPw_version(Integer pw_version) {
		this.pw_version = pw_version;
	}
	public Long getModified_at() {
		return modified_at;
	}
	public void setModified_at(Long modified_at) {
		this.modified_at = modified_at;
	}

}
