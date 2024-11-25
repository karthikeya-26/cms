package com.dbObjects;

public class UserDetailsObj extends ResultObject {
    
    private Integer user_id;
    private String user_name;
    private String first_name;
    private String last_name;
    private String contact_type;
    private Long created_at;
    private Long modified_at;
    

    public Integer getUserId() {
        return user_id;
    }
    public void setUserId(Integer userId) {
        this.user_id = userId;
    }
    public String getUserName() {
        return user_name;
    }
    public void setUserName(String userName) {
        this.user_name = userName;
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
    public String getContactType() {
        return contact_type;
    }
    public void setContactType(String contactType) {
        this.contact_type = contactType;
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
		return "UserDetailsObj [user_id=" + user_id + ", user_name=" + user_name + ", first_name=" + first_name
				+ ", last_name=" + last_name + ", contact_type=" + contact_type + ", created_at=" + created_at
				+ ", modified_at=" + modified_at + "]";
	}
}
