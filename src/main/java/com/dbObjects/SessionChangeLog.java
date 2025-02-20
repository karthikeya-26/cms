package com.dbObjects;

public class SessionChangeLog {
    @Column("id")
    private Long id;
    @Column("session_id")
    private String sessionId;
    @Column("user_id")
    private int userId;
    @Column("url")
    private String url;
    @Column("ip_address")
    private String ipAddress;
    @Column("accessed_at")
    private Long accessedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Long getAccessedAt() {
        return accessedAt;
    }

    public void setAccessedAt(Long accessedAt) {
        this.accessedAt = accessedAt;
    }

    @Override
    public String toString() {
        return "SessionChangeLog [id=" + id + ", sessionId=" + sessionId + ", userId=" + userId + ", url=" + url
                + ", ipAddress=" + ipAddress + ", accessedAt=" + accessedAt + "]";
    }
}
