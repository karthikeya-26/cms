package com.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@TableName("servers")
public enum Servers implements Columns {
	SERVER_ID("server_id", Integer.class), NAME("name", String.class), PORT("port", String.class),
	CREATED_AT("created_at", Long.class), MODIFIED_AT("modified_at", Long.class), STATUS("status", int.class);

	private static final Map<String, Servers> LOOKUP_MAP = new HashMap<>();

	static {
		// Populate the lookup map
		for (Servers server : Servers.values()) {
			LOOKUP_MAP.put(server.columnName, server);
		}
	}

	private final String columnName;
	private final Class<?> dataType;

	Servers(String columnName, Class<?> dataType) {
		this.columnName = columnName;
		this.dataType = dataType;
	}

	@Override
	public String value() {
		return columnName;
	}

	@Override
	public Class<?> getDataType() {
		return dataType;
	}

	public String getPrimaryKey() {
		return NAME.value();
	}

	public static Servers getCol(String colName) {
		Servers server = LOOKUP_MAP.get(colName);
		if (server == null) {
			throw new IllegalArgumentException("Column name " + colName + " does not exist.");
		}
		return server;
	}

	public static List<Columns> getAllCols() {
		return Arrays.asList(Servers.values());
	}
}
