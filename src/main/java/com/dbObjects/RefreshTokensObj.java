package com.dbObjects;

public class RefreshTokensObj extends ResultObject {

    @Column("reftoken_id")
    private Integer refTokenId;

    @Column("refresh_token")
    private String refreshToken;

    @Column("client_id")
    private String clientId;

    @Column("user_id")
    private Integer userId;

    @Column("scopes")
    private String scopes;

    @Column("created_at")
    private Long createdAt;

    public Integer getRefTokenId() {
        return refTokenId;
    }

    public void setRefTokenId(Integer refTokenId) {
        this.refTokenId = refTokenId;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getScopes() {
        return scopes;
    }

    public void setScopes(String scopes) {
        this.scopes = scopes;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "RefreshTokensObj [refTokenId=" + refTokenId + ", refreshToken=" + refreshToken + ", clientId=" + clientId
                + ", userId=" + userId + ", scopes=" + scopes + ", createdAt=" + createdAt + "]";
    }
}
