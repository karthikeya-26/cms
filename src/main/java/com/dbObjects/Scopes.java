package com.dbObjects;

public class Scopes extends ResultObject {
	Integer scope_id;
	String scope;
	Long created_at;
	Long modified_at;
	
	public Integer getScope_id() {
		return scope_id;
	}
	public void setScope_id(Integer scope_id) {
		this.scope_id = scope_id;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
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
		return "Scopes [scope_id=" + scope_id + ", scope=" + scope + ", created_at=" + created_at + ", modified_at="
				+ modified_at + "]";
	}
	
	
}
