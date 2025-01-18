package com.dbObjects;

public class RedirectUrisObj extends ResultObject {
	private Integer uri_id;
	private String client_id;
	private String uri;
	private Long created_at;
	private Long modified_at;
	public Integer getUri_id() {
		return uri_id;
	}
	public void setUri_id(Integer uri_id) {
		this.uri_id = uri_id;
	}
	public String getClient_id() {
		return client_id;
	}
	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public Long getCreated_at() {
		return created_at;
	}
	public void setCreated_at(Long created_at) {
		this.created_at = created_at;
	}
	public Long getModified_at() {
		return modified_at;
	}
	public void setModified_at(Long modified_at) {
		this.modified_at = modified_at;
	}
	@Override
	public String toString() {
		return "RedirectUrisObj [uri_id=" + uri_id + ", client_id=" + client_id + ", uri=" + uri + ", created_at="
				+ created_at + ", modified_at=" + modified_at + "]";
	}
	
}
