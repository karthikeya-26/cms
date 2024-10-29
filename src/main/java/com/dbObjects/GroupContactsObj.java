package com.dbObjects;


public class GroupContactsObj  extends ResultObject{
	private Integer group_id ;
	private Integer contact_id ;
    
	public Integer getGroup_id() {
		return group_id;
	}
	public void setGroup_id(Integer group_id) {
		this.group_id = group_id;
	}
	public Integer getContact_id() {
		return contact_id;
	}
	public void setContact_id(Integer contact_id) {
		this.contact_id = contact_id;
	}
	@Override
	public String toString() {
		return "GroupContactsObj [group_id=" + group_id + ", contact_id=" + contact_id + "]";
	}

}
