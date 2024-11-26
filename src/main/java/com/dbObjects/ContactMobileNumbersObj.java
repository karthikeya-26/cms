package com.dbObjects;

public class ContactMobileNumbersObj extends ResultObject {
    private Integer contact_id;
    private String number;
    private Long created_at;
    private Long modified_at;

    public Integer getContactId() {
        return contact_id;
    }
    public void setContactId(Integer contactId) {
        this.contact_id = contactId;
    }
    public String getMobileNumber() {
        return number;
    }
    public void setMobileNumber(String mobileNumber) {
        this.number = mobileNumber;
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
		return "ContactMobileNumbersObj [contact_id=" + contact_id + ", number=" + number + ", created_at=" + created_at
				+ ", modified_at=" + modified_at + "]";
	}

}
