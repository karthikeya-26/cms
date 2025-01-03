package com.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum GroupContacts implements Columns {
    GROUP_ID("group_id", Integer.class),
    CONTACT_ID("contact_id", Integer.class),
    ADDED_AT("added_at", Long.class),
    ALL_COLS("*", null);

    private static final Map<String, GroupContacts> LOOKUP_MAP = new HashMap<>();

    static {
        // Populate the lookup map
        for (GroupContacts groupContact : GroupContacts.values()) {
            LOOKUP_MAP.put(groupContact.columnName, groupContact);
        }
    }

    private final String columnName;
    private final Class<?> dataType;

    GroupContacts(String columnName, Class<?> dataType) {
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
    	return GROUP_ID.value();
    }

    public static GroupContacts getCol(String colName) {
        GroupContacts groupContact = LOOKUP_MAP.get(colName);
        if (groupContact == null) {
            throw new IllegalArgumentException("Column name " + colName + " does not exist.");
        }
        return groupContact;
    }

	public static List<Columns> getAllCols() {
		return Arrays.asList(GroupContacts.values());
	}
}
