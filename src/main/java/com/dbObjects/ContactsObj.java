package com.dbObjects;

public class ContactsObj extends ResultObject {
	private Integer contact_id ;
	private String first_name ;
	private String last_name ;
	private Integer user_id ;
	private String address ;
	private Long created_at ;
	
	public Integer getContact_id() {
		return contact_id;
	}
	public void setContact_id(Integer contact_id) {
		this.contact_id = contact_id;
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
	public Integer getUser_id() {
		return user_id;
	}
	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Long getCreated_at() {
		return created_at;
	}
	public void setCreated_at(Long created_at) {
		this.created_at = created_at;
	}
	@Override
	public String toString() {
		return "ContactsObj [contact_id=" + contact_id + ", first_name=" + first_name + ", last_name=" + last_name
				+ ", user_id=" + user_id + ", address=" + address + ", created_at=" + created_at + "]";
	}
}