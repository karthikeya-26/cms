package com.dbObjects;

public class PasswordsObj extends ResultObject {
    private Integer user_id;
    private String password;
    private Integer password_version;
    private Long created_at;
    private Long modified_at;

    public Integer getUserId() {
        return user_id;
    }
    public void setUserId(Integer userId) {
        this.user_id = userId;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public Integer getPasswordVersion() {
        return password_version;
    }
    public void setPasswordVersion(Integer passwordVersion) {
        this.password_version = passwordVersion;
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
		return "PasswordsObj [user_id=" + user_id + ", password=" + password + ", password_version=" + password_version
				+ ", created_at=" + created_at + ", modified_at=" + modified_at + "]";
	}
	

}