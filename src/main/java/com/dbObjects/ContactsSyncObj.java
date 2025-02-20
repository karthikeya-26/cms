package com.dbObjects;

public class ContactsSyncObj extends ResultObject {

    @Column("user_id")
    private Integer userId;

    @Column("account_id")
    private String accountId;

    @Column("refresh_token")
    private String refreshToken;

    @Column("provider")
    private String provider;

    @Column("last_updated_at")
    private Long lastUpdatedAt;

    @Column("created_at")
    private Long createdAt;

    @Column("modifed_at")
    private Long modifiedAt;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public Long getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public void setLastUpdatedAt(Long lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
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
        return "ContactsSyncObj [userId=" + userId + ", accountId=" + accountId + ", refreshToken=" + refreshToken
                + ", provider=" + provider + ", lastUpdatedAt=" + lastUpdatedAt + ", createdAt=" + createdAt
                + ", modifiedAt=" + modifiedAt + "]";
    }


}
