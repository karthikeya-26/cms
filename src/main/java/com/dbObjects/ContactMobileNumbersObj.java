package com.dbObjects;

public class ContactMobileNumbersObj extends ResultObject {

    @Column("contact_id")
    private Integer contactId;

    @Column("number")
    private String number;

    @Column("created_at")
    private Long createdAt;

    @Column("modified_at")
    private Long modifiedAt;

    public Integer getContactId() {
        return contactId;
    }

    public void setContactId(Integer contactId) {
        this.contactId = contactId;
    }

    public String getMobileNumber() {
        return number;
    }

    public void setMobileNumber(String mobileNumber) {
        this.number = mobileNumber;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Long modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    @Override
    public String toString() {
        return "ContactMobileNumbersObj [contactId=" + contactId + ", number=" + number + ", createdAt=" + createdAt
                + ", modifiedAt=" + modifiedAt + "]";
    }
}
