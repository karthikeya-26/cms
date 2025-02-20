package com.dbObjects;

public class SessionsObj extends ResultObject {

    @Column("session_id")
    private String sessionId;

    @Column("user_id")
    private Integer userId;

    @Column("created_time")
    private Long createdTime;

    @Column("last_accessed_time")
    private Long lastAccessedTime;

    public SessionsObj() {
    }

    public SessionsObj(String sessionId, Integer userId, Long createdTime, Long lastAccessedTime) {
        this.sessionId = sessionId;
        this.userId = userId;
        this.createdTime = createdTime;
        this.lastAccessedTime = lastAccessedTime;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }

    public Long getLastAccessedTime() {
        return lastAccessedTime;
    }

    public void setLastAccessedTime(Long lastAccessedTime) {
        this.lastAccessedTime = lastAccessedTime;
    }

    @Override
    public String toString() {
        return "SessionsObj [sessionId=" + sessionId + ", userId=" + userId + ", createdTime=" + createdTime
                + ", lastAccessedTime=" + lastAccessedTime + "]";
    }
}
