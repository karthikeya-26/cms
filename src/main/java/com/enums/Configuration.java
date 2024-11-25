package com.enums;

public enum Configuration {
    DATABASE_DRIVER("com.mysql.cj.jdbc.Driver"),
    DATABASE_URL("jdbc:mysql://localhost:3306/cms"),
    DATABASE_USERNAME("root"),
    DATABASE_PASSWORD("password"),
    DATABASE_NAME("cms"),
    DATABASE_INITIALPOOLSIZE(5),
    DATABASE_MINPOOLSIZE(2),
    DATABASE_ACQUIREINCREMENT(1),
    DATABASE_MAXPOOLSIZE(10),
    DATABASE_MAXSTATEMENTS(50),
    DATABASE_UNRETURNEDCONNECTIONTIMEOUT(300),
    DATABASE_DEBUGUNRETURNEDSTACKTRACES(true),
    
    PROJECT_NAME("MyProject"),
    
    SESSION_TIMEOUT(30), // in minutes
    
    AUDIT_ONOROFF(true),
    AUDIT_TABLES("users,transactions"),
    
    CACHE_ONOROFF(true),
    CACHED_TABLES("users,products");

    private Object value;

    // Constructor
    Configuration(Object value) {
        this.value = value;
    }

    // Getter methods
    public Object getValue() {
        return value;
    }

    public String getStringValue() {
        return (String) value;
    }

    public Integer getIntegerValue() {
        return (Integer) value;
    }

    public Boolean getBooleanValue() {
        return (Boolean) value;
    }

    public Long getLongValue() {
        return (Long) value;
    }

    // Setter method to allow modification
    public void setValue(Object value) {
        this.value = value;
    }
}
