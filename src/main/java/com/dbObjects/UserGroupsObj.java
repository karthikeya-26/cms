package com.dbObjects;

public class UserGroupsObj extends ResultObject {

    private Integer group_id;
    private String group_name;
    private Integer user_id;
    private Long created_at;
    private Long modified_at;

    public Integer getGroupId() {
        return group_id;
    }

    public void setGroupId(Integer groupId) {
        this.group_id = groupId;
    }

    public String getGroupName() {
        return group_name;
    }

    public void setGroupName(String groupName) {
        this.group_name = groupName;
    }

    public Integer getUserId() {
        return user_id;
    }

    public void setUserId(Integer userId) {
        this.user_id = userId;
    }

    public Long getCreatedAt() {
        return created_at;
    }

    public void setCreatedAt(Long createdAt) {
        this.created_at = createdAt;
    }

    public Long getModifiedAt() {
        return modified_at;
    }

    public void setModifiedAt(Long modifiedAt) {
        this.modified_at = modifiedAt;
    }

	@Override
	public String toString() {
		return "UserGroupsObj [group_id=" + group_id + ", group_name=" + group_name + ", user_id=" + user_id
				+ ", created_at=" + created_at + ", modified_at=" + modified_at + "]";
	}

  
}
