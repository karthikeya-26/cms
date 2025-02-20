package com.dbObjects;

public class AuthorizationCodesObj extends ResultObject {

    @Column("authcode_id")
    private Integer authCodeId;

    @Column("authorization_code")
    private String authorizationCode;

    @Column("client_id")
    private String clientId;

    @Column("user_id")
    private Integer userId;

    @Column("created_at")
    private Long createdAt;

    @Column("scopes")
    private String scopes;

    @Column("access_type")
    private String accessType;

    public Integer getAuthCodeId() {
        return authCodeId;
    }

    public void setAuthCodeId(Integer authCodeId) {
        this.authCodeId = authCodeId;
    }

    public String getAuthorizationCode() {
        return authorizationCode;
    }

    public void setAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
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

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public String getScopes() {
        return scopes;
    }

    public void setScopes(String scopes) {
        this.scopes = scopes;
    }

    public String getAccessType() {
        return accessType;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }

    @Override
    public String toString() {
        return "AuthorizationCodesObj [authCodeId=" + authCodeId + ", authorizationCode=" + authorizationCode
                + ", clientId=" + clientId + ", userId=" + userId + ", createdAt=" + createdAt + ", scopes=" + scopes
                + ", accessType=" + accessType + "]";
    }
}
