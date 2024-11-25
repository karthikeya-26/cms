package com.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum ContactMails implements Columns {
    CONTACT_ID("contact_id", Integer.class),
    MAIL("mail", String.class),
    CREATED_AT("created_at", Long.class),
    MODIFIED_AT("modified_at", Long.class),
    ALL_COLS("*", null);

    private static final Map<String, ContactMails> LOOKUP_MAP = new HashMap<>();

    static {
        // Populate the lookup map
        for (ContactMails contactMail : ContactMails.values()) {
            LOOKUP_MAP.put(contactMail.columnName, contactMail);
        }
    }
    private final String columnName;
    private final Class<?> dataType;

    ContactMails(String columnName, Class<?> dataType) {
        this.columnName = columnName;
        this.dataType = dataType;
    }

    @Override
    public String value() {
        return columnName;
    }

    @Override
    public Class<?> getDataType() {
        return this.dataType;
    }

    public static ContactMails getCol(String colName) {
        ContactMails contactMail = LOOKUP_MAP.get(colName);
        if (contactMail == null) {
            throw new IllegalArgumentException("Column name " + colName + " does not exist.");
        }
        return contactMail;
    }

	
	public static List<Columns> getAllCols() {
		return Arrays.asList(ContactMails.values());
	}
}
