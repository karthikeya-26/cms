package com.dbObjects;

public class PasswordsObj extends ResultObject {

    @Column("user_id")
    private Integer userId;

    @Column("password")
    private String password;

    @Column("password_version")
    private Integer passwordVersion;

    @Column("created_at")
    private Long createdAt;

    @Column("modified_at")
    private Long modifiedAt;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getPasswordVersion() {
        return passwordVersion;
    }

    public void setPasswordVersion(Integer passwordVersion) {
        this.passwordVersion = passwordVersion;
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
        return "PasswordsObj [userId=" + userId + ", password=" + password + ", passwordVersion=" + passwordVersion
                + ", createdAt=" + createdAt + ", modifiedAt=" + modifiedAt + "]";
    }
}
