package com.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum Contacts implements Columns {
    CONTACT_ID("contact_id", Integer.class),
    FIRST_NAME("first_name", String.class),
    LAST_NAME("last_name", String.class),
    USER_ID("user_id", Integer.class),
    CREATED_AT("created_at", Long.class),
    MODIFIED_AT("modified_at", Long.class),
    REF_ID("ref_id", String.class),
    REFRESH_TOKEN("refresh_token", String.class),
    ALL_COLS("*", null);

    private static final Map<String, Contacts> LOOKUP_MAP = new HashMap<>();

    static {
        // Populate the lookup map
        for (Contacts contact : Contacts.values()) {
            LOOKUP_MAP.put(contact.columnName, contact);
        }
    }

    private final String columnName;
    private final Class<?> dataType;

    Contacts(String columnName, Class<?> dataType) {
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

    public static Contacts getCol(String colName) {
        Contacts contact = LOOKUP_MAP.get(colName);
        if (contact == null) {
            throw new IllegalArgumentException("Column name " + colName + " does not exist.");
        }
        return contact;
    }

	
	public static List<Columns> getAllCols() {
		return Arrays.asList(Contacts.values());
	}
}
