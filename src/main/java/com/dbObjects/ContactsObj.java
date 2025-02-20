package com.dbObjects;

public class ContactsObj extends ResultObject {

    @Column("contact_id")
    private Integer contactId;

    @Column("first_name")
    private String firstName;

    @Column("last_name")
    private String lastName;

    @Column("user_id")
    private Integer userId;

    @Column("created_at")
    private Long createdAt;

    @Column("modified_at")
    private Long modifiedAt;

    @Column("ref_id")
    private String refId;

    @Column("refresh_token")
    private String refreshToken;

    public Integer getContactId() {
        return contactId;
    }

    public void setContactId(Integer contactId) {
        this.contactId = contactId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public String getRefId() {
        return refId;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    @Override
    public String toString() {
        return "ContactsObj [contactId=" + contactId + ", firstName=" + firstName + ", lastName=" + lastName
                + ", userId=" + userId + ", createdAt=" + createdAt + ", modifiedAt=" + modifiedAt + ", refId="
                + refId + ", refreshToken=" + refreshToken + "]";
    }
}
