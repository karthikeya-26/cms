package com.dbObjects;

public class UserMailsObj extends ResultObject {

    @Column("mail_id")
    private Integer mailId;

    @Column("user_id")
    private Integer userId;

    @Column("mail")
    private String mail;

    @Column("is_primary")
    private Integer isPrimary;

    @Column("created_at")
    private Long createdAt;

    @Column("modified_at")
    private Long modifiedAt;

    public Integer getMailId() {
        return mailId;
    }

    public void setMailId(Integer mailId) {
        this.mailId = mailId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Integer getIsPrimary() {
        return isPrimary;
    }

    public void setIsPrimary(Integer isPrimary) {
        this.isPrimary = isPrimary;
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
        return "UserMailsObj [mailId=" + mailId + ", userId=" + userId + ", mail=" + mail + ", isPrimary="
                + isPrimary + ", createdAt=" + createdAt + ", modifiedAt=" + modifiedAt + "]";
    }
}
