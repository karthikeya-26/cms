package com.dbObjects;

public class RefreshTokensObj extends ResultObject {
	
	private Integer reftoken_id;
	private String refresh_token;
	private String client_id;
	private Integer user_id;
	private String scopes;
	private Long created_at;
	public Integer getReftoken_id() {
		return reftoken_id;
	}
	public void setReftoken_id(Integer reftoken_id) {
		this.reftoken_id = reftoken_id;
	}
	public String getRefresh_token() {
		return refresh_token;
	}
	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}
	public String getClient_id() {
		return client_id;
	}
	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}
	public Integer getUser_id() {
		return user_id;
	}
	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}
	public String getScopes() {
		return scopes;
	}
	public void setScopes(String scopes) {
		this.scopes = scopes;
	}
	public Long getCreated_at() {
		return created_at;
	}
	public void setCreated_at(Long created_at) {
		this.created_at = created_at;
	}
	@Override
	public String toString() {
		return "RefreshTokensObj [reftoken_id=" + reftoken_id + ", refresh_token=" + refresh_token + ", client_id="
				+ client_id + ", user_id=" + user_id + ", scopes=" + scopes + ", created_at=" + created_at + "]";
	}
	
}
