package com.dbObjects;

public class ServersObj extends ResultObject {
    private Integer serverId;
    private String name;
    private String port;
    private Long createdAt;
    private Long modifiedAt;
    private Integer status;

    public Integer getServerId() {
        return serverId;
    }
    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPort() {
        return port;
    }
    public void setPort(String port) {
        this.port = port;
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
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ServersObj [serverId=" + serverId + ", name=" + name + ", port=" + port +
               ", createdAt=" + createdAt + ", modifiedAt=" + modifiedAt + ", status=" + status + "]";
    }
}
