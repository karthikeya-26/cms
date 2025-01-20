package com.dbObjects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AccessTokensObj extends ResultObject{
	private Integer accesstoken_id;
	private String access_token;
	private String scopes;
	private Long created_at;
	private Integer user_id;
	private String client_id;
	
	public Integer getAccessTokenId() {
		return accesstoken_id;
	}
	public void setAccessTokenId(Integer tokenId) {
		accesstoken_id = tokenId;
	}
	public String getAccess_token() {
		return access_token;
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	public List<Integer> getScopes() {
		List<Integer> numbers = new ArrayList<Integer>();
		String scopes = this.scopes.substring(1, this.scopes.length());
		for(String scope : scopes.split(",")) {
			numbers.add(Integer.valueOf(scope));
		}
		return numbers;
	}
	public void setScopes(List<Integer> scopeIds) {
		this.scopes = scopeIds.toString();
	}
	public Long getCreated_at() {
		return created_at;
	}
	public void setCreated_at(Long created_at) {
		this.created_at = created_at;
	}
	public Integer getUser_id() {
		return user_id;
	}
	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}
	public String getClient_id() {
		return client_id;
	}
	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}
	@Override
	public String toString() {
		return "AccessTokensObj [accesstoken_id="+accesstoken_id +" access_token=" + access_token + ", scopes=" + scopes + ", created_at=" + created_at
				 + ", user_id=" + user_id + ", client_id=" + client_id + "]";
	}
	
}
