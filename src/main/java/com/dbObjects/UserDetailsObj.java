package com.dbObjects;

public class UserDetailsObj  extends ResultObject{
	private  Integer user_id ;
	private  String user_name ;
	private  String password ;
	private String first_name ;
	private  String last_name ;
	private  String contact_type ;
	private Long created_at;
	private Long modified_at;
	private Integer pw_version;
	private Long pw_last_changed_at;
	
	@Override
	public String toString() {
		return "UserDetailsObj [user_id=" + user_id + ", user_name=" + user_name + ", password=" + password
				+ ", first_name=" + first_name + ", last_name=" + last_name + ", contact_type=" + contact_type + "]";
	}
	public Integer getUser_id() {
		return user_id;
	}
	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getFirst_name() {
		return first_name;
	}
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	public String getLast_name() {
		return last_name;
	}
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	public String getContact_type() {
		return contact_type;
	}
	public void setContact_type(String contact_type) {
		this.contact_type = contact_type;
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
	public Integer getPw_version() {
		return pw_version;
	}
	public void setPw_version(Integer pw_version) {
		this.pw_version = pw_version;
	}
	public Long getPw_last_changed_at() {
		return pw_last_changed_at;
	}
	public void setPw_last_changed_at(Long pw_last_changed_at) {
		this.pw_last_changed_at = pw_last_changed_at;
	}
	
	
}
