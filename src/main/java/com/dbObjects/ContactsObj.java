package com.dbObjects;

public class ContactsObj extends ResultObject {
	private Integer contact_id ;
	private String first_name ;
	private String last_name ;
	private Integer user_id ;
	private String address ;
	private Long created_at ;
	private Long modified_at;
	
	public Integer getContactId() {
		return contact_id;
	}
	public void setContactId(Integer contactId) {
		this.contact_id = contactId;
	}
	public String getFirstName() {
		return first_name;
	}
	public void setFirstName(String firstName) {
		this.first_name = firstName;
	}
	public String getLastName() {
		return last_name;
	}
	public void setLastName(String lastName) {
		this.last_name = lastName;
	}
	public Integer getUserId() {
		return user_id;
	}
	public void setUserId(Integer userId) {
		this.user_id = userId;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Long getCreatedAt() {
		return created_at;
	}
	public void setCreatedAt(Long createdAt) {
		this.created_at = createdAt;
	}
	public Long getModifiedAt() {
		return modified_at;
	}
	public void setModifiedAt(Long modifiedAt) {
		this.modified_at = modifiedAt;
	}
	@Override
	public String toString() {
		return "ContactsObj [contact_id=" + contact_id + ", first_name=" + first_name + ", last_name=" + last_name
				+ ", user_id=" + user_id + ", address=" + address + ", created_at=" + created_at + ", modified_at="
				+ modified_at + "]";
	}
	
}