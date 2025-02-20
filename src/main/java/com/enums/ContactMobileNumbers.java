package com.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@TableName("contact_mobile_numbers")
public enum ContactMobileNumbers implements Columns {
	CONTACT_ID("contact_id", Integer.class), NUMBER("number", String.class), CREATED_AT("created_at", Long.class),
	MODIFIED_AT("modified_at", Long.class), ALL_COLS("*", null);

	private static final Map<String, ContactMobileNumbers> LOOKUP_MAP = new HashMap<>();

	static {
		// Populate the lookup map
		for (ContactMobileNumbers contactMobileNumber : ContactMobileNumbers.values()) {
			LOOKUP_MAP.put(contactMobileNumber.columnName, contactMobileNumber);
		}
	}

	private final String columnName;
	private final Class<?> dataType;

	ContactMobileNumbers(String columnName, Class<?> dataType) {
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

	public static ContactMobileNumbers getCol(String colName) {
		ContactMobileNumbers contactMobileNumber = LOOKUP_MAP.get(colName);
		if (contactMobileNumber == null) {
			throw new IllegalArgumentException("Column name " + colName + " does not exist.");
		}
		return contactMobileNumber;
	}

	public static List<Columns> getAllCols() {
		return Arrays.asList(ContactMobileNumbers.values());
	}
}
