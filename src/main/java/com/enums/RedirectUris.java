package com.enums;

import java.util.*;
@TableName("redirect_uris")
public enum RedirectUris implements Columns {
	URI_ID("uri_id", Integer.class), CLIENT_ID("client_id", String.class), URI("uri", String.class),
	CREATED_AT("created_at", Long.class), MODIFIED_AT("modified_at", Long.class), ALL_COLS("*", null);

	private static final Map<String, RedirectUris> LOOKUP_MAP = new HashMap<>();

	static {
		for (RedirectUris ruri : RedirectUris.values()) {
			LOOKUP_MAP.put(ruri.value(), ruri);
		}
	}

	private final String colName;
	private final Class<?> dataType;

	RedirectUris(String colName, Class<?> dataType) {
		this.colName = colName;
		this.dataType = dataType;
	}

	@Override
	public String value() {
		return this.colName;
	}

	@Override
	public Class<?> getDataType() {
		return this.dataType;
	}

	public static RedirectUris getCol(String colName) {
		RedirectUris ruri = LOOKUP_MAP.get(colName);
		if (ruri == null) {
			throw new IllegalArgumentException("Column name " + colName + " does not exist.");
		}
		return ruri;
	}

	public static List<Columns> getAllCols() {
		return Arrays.asList(RedirectUris.values());
	}
}
