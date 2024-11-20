package com.dbObjects;

public class ServersObj extends ResultObject {
	private String server_name;
	private String port;
	public String getServer_name() {
		return server_name;
	}
	public void setServer_name(String server_name) {
		this.server_name = server_name;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	@Override
	public String toString() {
		return "ServersObj [server_name=" + server_name + ", port=" + port + "]";
	}
}
