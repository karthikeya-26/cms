package com.dbObjects;

public class SessionsObj extends ResultObject {
    
    private String session_id;
    private Integer user_id;
    private Long created_time;
    private Long last_accessed_time;
    
    public SessionsObj() {
    	
    }
    
    public  SessionsObj(String sessionId, Integer userId, Long createdTime, Long lastAccessedTime) {
		this.session_id = sessionId;
		this.user_id = userId;
		this.created_time = createdTime;
		this.last_accessed_time = lastAccessedTime;
	}
    
    public String getSessionId() {
        return session_id;
    }
    public void setSessionId(String sessionId) {
        this.session_id = sessionId;
    }
    public Integer getUserId() {
        return user_id;
    }
    public void setUserId(Integer userId) {
        this.user_id = userId;
    }
    public Long getCreatedTime() {
        return created_time;
    }
    public void setCreatedTime(Long createdTime) {
        this.created_time = createdTime;
    }
    public Long getLastAccessedTime() {
        return last_accessed_time;
    }
    public void setLastAccessedTime(Long lastAccessedTime) {
        this.last_accessed_time = lastAccessedTime;
    }
	@Override
	public String toString() {
		return "SessionsObj [session_id=" + session_id + ", user_id=" + user_id + ", created_time=" + created_time
				+ ", last_accessed_time=" + last_accessed_time + "]";
	}
    
   
}
