package com.dbObjects;

public class GroupContactsObj extends ResultObject {

    @Column("group_id")
    private Integer groupId;

    @Column("contact_id")
    private Integer contactId;

    @Column("created_at")
    private Long createdAt;

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getContactId() {
        return contactId;
    }

    public void setContactId(Integer contactId) {
        this.contactId = contactId;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "GroupContactsObj [groupId=" + groupId + ", contactId=" + contactId + ", createdAt=" + createdAt + "]";
    }

}
