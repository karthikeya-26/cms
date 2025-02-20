package com.dbObjects;

public class ScopesObj extends ResultObject {

    @Column("scope_id")
    private Integer scopeId;

    @Column("scope")
    private String scope;

    @Column("created_at")
    private Long createdAt;

    @Column("modified_at")
    private Long modifiedAt;

    public Integer getScopeId() {
        return scopeId;
    }

    public void setScopeId(Integer scopeId) {
        this.scopeId = scopeId;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
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
        return "Scopes [scopeId=" + scopeId + ", scope=" + scope + ", createdAt=" + createdAt + ", modifiedAt="
                + modifiedAt + "]";
    }
}
