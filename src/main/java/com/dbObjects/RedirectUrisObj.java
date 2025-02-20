package com.dbObjects;

public class RedirectUrisObj extends ResultObject {

    @Column("uri_id")
    private Integer uriId;

    @Column("client_id")
    private String clientId;

    @Column("uri")
    private String uri;

    @Column("created_at")
    private Long createdAt;

    @Column("modified_at")
    private Long modifiedAt;

    public Integer getUriId() {
        return uriId;
    }

    public void setUriId(Integer uriId) {
        this.uriId = uriId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
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
        return "RedirectUrisObj [uriId=" + uriId + ", clientId=" + clientId + ", uri=" + uri + ", createdAt="
                + createdAt + ", modifiedAt=" + modifiedAt + "]";
    }
}
