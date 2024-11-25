package com.dbObjects;

public class ContactMailsObj extends ResultObject {
    private Integer contact_id;
    private String mail;
    private Long created_at;
    private Long modified_at;
    
    public Integer getContactId() {
        return contact_id;
    }
    public void setContactId(Integer contactId) {
        this.contact_id = contactId;
    }
    public String getEmail() {
        return mail;
    }
    public void setEmail(String email) {
        this.mail = email;
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
		return "ContactMailsObj [contact_id=" + contact_id + ", mail=" + mail + ", created_at=" + created_at
				+ ", modified_at=" + modified_at + "]";
	}
	
   
}
