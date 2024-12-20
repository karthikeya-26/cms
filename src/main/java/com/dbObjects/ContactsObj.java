package com.dbObjects;

public class ContactsObj extends ResultObject {
	private Integer contact_id ;
	private String first_name ;
	private String last_name ;
	private Integer user_id ;
	private Long created_at ;
	private Long modified_at;
	private String ref_id;
	private String refresh_token;
	
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
	public String getRefId() {
		return ref_id;
	}
	public String getRefreshToken() {
		return refresh_token;
	}
	@Override
	public String toString() {
		return "ContactsObj [contact_id=" + contact_id + ", first_name=" + first_name + ", last_name=" + last_name
				+ ", user_id=" + user_id  + ", created_at=" + created_at + ", modified_at="
				+ modified_at + ", ref_id=" + ref_id + ", refresh_token=" + refresh_token + "]";
	}
	
	
}