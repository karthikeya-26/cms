package com.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
@TableName("configurations")
public enum Configurations implements Columns {

	ID("id", Integer.class), NAME("name", String.class), VALUE("value", Object.class),
	CREATED_AT("created_at", Long.class), MODIFIED_AT("modified_at", Long.class), ALL_COLS("*", null);

	public final static HashMap<Columns, Object> appConfig = new HashMap<>();

	private final static HashMap<String, Configurations> LOOKUP_MAP = new HashMap<>();

	private final String columnName;
	private final Class<?> dataType;

	static {
		for (Configurations config : Configurations.values()) {
			LOOKUP_MAP.put(config.columnName, config);
		}
	}

	private Configurations(String colName, Class<?> dataType) {
		this.columnName = colName;
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

	public static List<Columns> getAllCols() {
		return Arrays.asList(Configurations.values());
	}

}
