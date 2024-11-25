package com.dbObjects;

public class ConfigurationsObj extends ResultObject{
    private Integer id;
    private String name;
    private String value;
    private Long created_at;
    private Long modified_at;
    
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public Long getCreatedAt() {
        return created_at;
    }
    public void setCreatedAt(Long createdAt) {
        this.created_at= createdAt;
    }
    public Long getModifiedAt() {
        return modified_at;
    }
    public void setModifiedAt(Long modifiedAt) {
        this.modified_at = modifiedAt;
    }
    @Override
    public String toString() {
        return "ConfigurationsObj [id=" + id + ", name=" + name + ", value=" + value + 
               ", createdAt=" + created_at + ", modifiedAt=" + modified_at + "]";
    }
}
