package com.tables;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum Servers implements Columns {
    SERVER_NAME("server_name", String.class),
    PORT("port", String.class);

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

    public static Servers getCol(String colName) {
        Servers server = LOOKUP_MAP.get(colName);
        if (server == null) {
            throw new IllegalArgumentException("Column name " + colName + " does not exist.");
        }
        return server;
    }
    
    public static List<Columns> getAllCols(){
    	return Arrays.asList(Servers.values());
    }
}
