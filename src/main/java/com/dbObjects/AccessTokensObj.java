package com.dbObjects;

import java.util.ArrayList;
import java.util.List;

public class AccessTokensObj extends ResultObject {
    
    @Column("accesstoken_id")
    private Integer accessTokenId;

    @Column("access_token")
    private String accessToken;

    @Column("scopes")
    private String scopes;

    @Column("created_at")
    private Long createdAt;

    @Column("user_id")
    private Integer userId;

    @Column("client_id")
    private String clientId;

    public Integer getAccessTokenId() {
        return accessTokenId;
    }

    public void setAccessTokenId(Integer accessTokenId) {
        this.accessTokenId = accessTokenId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public List<Integer> getScopes() {
        List<Integer> numbers = new ArrayList<>();
        String scopeStr = this.scopes.substring(1, this.scopes.length());
        for (String scope : scopeStr.split(",")) {
            numbers.add(Integer.valueOf(scope.trim()));
        }
        return numbers;
    }

    public void setScopes(List<Integer> scopeIds) {
        this.scopes = scopeIds.toString();
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public String toString() {
        return "AccessTokensObj [accessTokenId=" + accessTokenId + ", accessToken=" + accessToken + ", scopes=" + scopes
                + ", createdAt=" + createdAt + ", userId=" + userId + ", clientId=" + clientId + "]";
    }
}
