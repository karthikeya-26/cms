package com.dbObjects;

public class AuthorizationCodesObj extends ResultObject {
	private Integer authcode_id;
	private String authorization_code;
	private String client_id;
	private Integer user_id;
	private Long created_at;
	private String scopes;
	private String access_type;
	
	public Integer getAuthCodeId() {
		return authcode_id;
	}
	public void setAuthCodeId(Integer codeId) {
		authcode_id = codeId;
	}
	public String getAuthorizationCode() {
		return authorization_code;
	}
	public void setCode(String code) {
		this.authorization_code = code;
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
	public Long getCreated_at() {
		return created_at;
	}
	public void setCreated_at(Long created_at) {
		this.created_at = created_at;
	}
	public String getScopes() {
		return scopes;
	}
	public void setScopes(String scopes) {
		this.scopes = scopes;
	}
	public String getAccess_type() {
		return this.access_type;
	}
	public void setAccess_type(String accessType) {
		this.access_type = accessType;
	}
	@Override
	public String toString() {
		return "AuthorizationCodesObj [authcode_id= "+authcode_id+" authorization_code=" + authorization_code + ", client_id=" + client_id + ", user_id=" + user_id
				+ ", created_at=" + created_at + ", scopes=" + scopes + ", access_type = "+access_type+"]";
	}
	
}
