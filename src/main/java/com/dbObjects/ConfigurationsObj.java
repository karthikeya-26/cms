package com.dbObjects;

public class ConfigurationsObj extends ResultObject {

    @Column("id")
    private Integer id;

    @Column("name")
    private String name;

    @Column("value")
    private String value;

    @Column("created_at")
    private Long createdAt;

    @Column("modified_at")
    private Long modifiedAt;

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
        return "ConfigurationsObj [id=" + id + ", name=" + name + ", value=" + value 
                + ", createdAt=" + createdAt + ", modifiedAt=" + modifiedAt + "]";
    }
}
