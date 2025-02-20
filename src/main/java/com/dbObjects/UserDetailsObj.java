package com.dbObjects;

public class UserDetailsObj extends ResultObject {
    @Column("user_id")
    private Integer userId;

    @Column("user_name")
    private String userName;

    @Column("first_name")
    private String firstName;

    @Column("last_name")
    private String lastName;

    @Column("contact_type")
    private String contactType;

    @Column("created_at")
    private Long createdAt;

    @Column("modified_at")
    private Long modifiedAt;

    @Column("provider_ac_id")
    private String providerAcId;
    
    public UserDetailsObj() {
    	
    }
    
    public UserDetailsObj(String userName, String firstName, String lastName, String contactType) {
    	this.userName = userName;
    	this.firstName = firstName;
    	this.lastName = lastName;
    	this.contactType = contactType;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getContactType() {
        return contactType;
    }

    public void setContactType(String contactType) {
        this.contactType = contactType;
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

    public String getProviderAcId() {
        return providerAcId;
    }

    public void setProviderAcId(String providerAcId) {
        this.providerAcId = providerAcId;
    }

    @Override
    public String toString() {
        return "UserDetailsObj [userId=" + userId + ", userName=" + userName + ", firstName=" + firstName
                + ", lastName=" + lastName + ", contactType=" + contactType + ", createdAt=" + createdAt
                + ", modifiedAt=" + modifiedAt + ", providerAcId=" + providerAcId + "]";
    }

    public static void main(String[] args) {
        UserDetailsObj a = new UserDetailsObj();
        System.out.println(a);
    }
}
