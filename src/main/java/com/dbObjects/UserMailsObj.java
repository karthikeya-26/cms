package com.dbObjects;

public class UserMailsObj extends ResultObject {
    private Integer mail_id;
    private Integer user_id;
    private String mail;
    private Integer is_primary;
    private Long created_at;
    private Long modified_at;

    public Integer getMailId() {
        return mail_id;
    }

    public void setMailId(Integer mailId) {
        this.mail_id = mailId;
    }

    public Integer getUserId() {
        return user_id;
    }

    public void setUserId(Integer userId) {
        this.user_id = userId;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Integer getIsPrimary() {
        return is_primary;
    }

    public void setIsPrimary(Integer isPrimary) {
        this.is_primary = isPrimary;
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
		return "UserMailsObj [mail_id=" + mail_id + ", user_id=" + user_id + ", mail=" + mail + ", is_primary="
				+ is_primary + ", created_at=" + created_at + ", modified_at=" + modified_at + "]";
	}

    
}
