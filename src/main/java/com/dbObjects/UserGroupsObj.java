package com.dbObjects;

public class UserGroupsObj extends ResultObject {

    @Column("group_id")
    private Integer groupId;

    @Column("group_name")
    private String groupName;

    @Column("user_id")
    private Integer userId;

    @Column("created_at")
    private Long createdAt;

    @Column("modified_at")
    private Long modifiedAt;

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Long modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    @Override
    public String toString() {
        return "UserGroupsObj [groupId=" + groupId + ", groupName=" + groupName + ", userId=" + userId
                + ", createdAt=" + createdAt + ", modifiedAt=" + modifiedAt + "]";
    }
}
