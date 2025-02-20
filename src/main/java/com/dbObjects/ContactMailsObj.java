package com.dbObjects;

public class ContactMailsObj extends ResultObject {

    @Column("contact_id")
    private Integer contactId;

    @Column("mail")
    private String email;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
        return "ContactMailsObj [contactId=" + contactId + ", email=" + email + ", createdAt=" + createdAt
                + ", modifiedAt=" + modifiedAt + "]";
    }
}
