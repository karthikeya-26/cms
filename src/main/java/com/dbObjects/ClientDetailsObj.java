package com.dbObjects;

import java.util.HashSet;
import java.util.Set;

public class ClientDetailsObj extends ResultObject {

    @Column("client_id")
    private String clientId;

    @Column("user_id")
    private Integer userId;

    @Column("client_name")
    private String clientName;

    @Column("client_type")
    private String clientType;

    @Column("client_secret")
    private String clientSecret;

    @Column("scopes")
    private String scopes;

    @Column("created_at")
    private Long createdAt;

    @Column("modified_at")
    private Long modifiedAt;

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

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public void setScopes(int... scopeIds) {
        StringBuilder s = new StringBuilder();
        for (int id : scopeIds) {
            if (s.length() > 0) {
                s.append(",");
            }
            s.append(id);
        }
        scopes = s.toString();
    }

    public Set<Integer> getScopes() {
        Set<Integer> scopeIds = new HashSet<>();
        if (scopes != null && !scopes.isEmpty()) {
            String[] ids = scopes.split(",");
            for (String id : ids) {
                scopeIds.add(Integer.parseInt(id.trim()));
            }
        }
        return scopeIds;
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
        return "ClientDetailsObj [clientId=" + clientId + ", userId=" + userId + ", clientName=" + clientName
                + ", clientType=" + clientType + ", clientSecret=" + clientSecret + ", scopes=" + scopes
                + ", createdAt=" + createdAt + ", modifiedAt=" + modifiedAt + "]";
    }
}
