package com.dbObjects;

public class GroupContactsObj extends ResultObject {
    private Integer group_id;
    private Integer contact_id;
    private Long created_at;

    public Integer getGroupId() {
        return group_id;
    }
    public void setGroupId(Integer groupId) {
        this.group_id = groupId;
    }
    public Integer getContactId() {
        return contact_id;
    }
    public void setContactId(Integer contactId) {
        this.contact_id = contactId;
    }
    public Long getCreatedAt() {
        return created_at;
    }
    public void setCreatedAt(Long createdAt) {
        this.created_at = createdAt;
    }
	@Override
	public String toString() {
		return "GroupContactsObj [group_id=" + group_id + ", contact_id=" + contact_id + ", created_at=" + created_at
				+ "]";
	}
    
}
