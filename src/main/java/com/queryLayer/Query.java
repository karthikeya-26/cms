package com.queryLayer;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import com.dbObjects.ResultObject;
import com.dbconn.Database;
import com.enums.Columns;
import com.enums.Operators;
import com.enums.Table;
import com.loggers.AppLogger;
import com.util.PostExecuteTasks;
import com.util.PreExecuteTasks;

public class Query {
    private static List<Table> tablesWithoutTasks;
    
    static {
        tablesWithoutTasks = new ArrayList<>();
        tablesWithoutTasks.add(Table.ChangeLog);
        tablesWithoutTasks.add(Table.Servers);
        tablesWithoutTasks.add(Table.Sessions);
    }
    
    static Properties prop = Database.prop;
    
    public Table getTableName() {
        return null;
    }

    public String build() {
        return null;
    }

    public List<ResultObject> executeQuery(Query query, Class<? extends ResultObject> clazz) throws Exception {
        if (query instanceof Insert || query instanceof Update || query instanceof Delete) {
            executeUpdate(query);
            return null;
        }

        List<ResultObject> resultList = new ArrayList<>();
        
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(query.build());
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ResultObject r = clazz.getDeclaredConstructor().newInstance();
                for (Field f : clazz.getDeclaredFields()) {
                    f.setAccessible(true);
                    Object value = rs.getObject(f.getName());
                    if (value != null) {
                        f.set(r, value);
                    }
                }
                resultList.add(r);
            }
        }
        return resultList;
    }

    public List<HashMap<Columns, Object>> executeQuery(Query query, HashMap<Columns, Class<?>> fields) throws SQLException {
        if (query instanceof Insert || query instanceof Update || query instanceof Delete) {
            executeUpdate(query);
            return null;
        }
        
        List<HashMap<Columns, Object>> resultObj = new ArrayList<>();
        
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(query.build());
             ResultSet resultSet = ps.executeQuery()) {
            while (resultSet.next()) {
                HashMap<Columns, Object> row = new HashMap<>();
                for (Columns colName : fields.keySet()) {
                    Class<?> fieldType = fields.get(colName);
                    if (fieldType == Integer.class) {
                        row.put(colName, resultSet.getInt(colName.value()));
                    } else if (fieldType == String.class) {
                        row.put(colName, resultSet.getString(colName.value()));
                    } else if (fieldType == Boolean.class) {
                        row.put(colName, resultSet.getBoolean(colName.value()));
                    } else if (fieldType == Double.class) {
                        row.put(colName, resultSet.getDouble(colName.value()));
                    } else if (fieldType == Date.class) {
                        row.put(colName, resultSet.getDate(colName.value()));
                    } else if (fieldType == Long.class) {
                        row.put(colName, resultSet.getLong(colName.value()));
                    } else {
                        row.put(colName, resultSet.getObject(colName.value()));
                    }
                }
                resultObj.add(row);
            }
        }
        return resultObj;
    }

    public int executeUpdate(Query query) throws SQLException {
        PreExecuteTasks pretasks = null;
        if (!(query.getTableName().equals(Table.ChangeLog))) {
            pretasks = new PreExecuteTasks();
            for (Method m : pretasks.getClass().getDeclaredMethods()) {
                m.setAccessible(true);
                if (m.getParameterCount() == 1) {
                    try {
                        m.invoke(pretasks, query);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        AppLogger.ApplicationLog(e);
                    }
                }
            }
        }

        int status = -1;
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(query.build())) {
            status = ps.executeUpdate();
        }
        
        if (pretasks != null) {
            System.out.println(pretasks.getResultMap());
        }
        
        if (status >= 0 && !(query.getTableName().equals(Table.ChangeLog))) {
            PostExecuteTasks posttasks = new PostExecuteTasks();
            for (Method m : posttasks.getClass().getDeclaredMethods()) {
                try {
                    m.setAccessible(true);
                    m.invoke(posttasks, query, pretasks.getResultMap());
                } catch (IllegalAccessException | InvocationTargetException e) {
                    AppLogger.ApplicationLog(e);
                    e.printStackTrace();
                }
            }
        }
        return status;
    }

    public int executeUpdate(Query query, boolean returnGeneratedKey) throws SQLException {
        PreExecuteTasks pretasks = null;
        if (!(query.getTableName().equals(Table.ChangeLog))) {
            pretasks = new PreExecuteTasks();
            for (Method m : pretasks.getClass().getDeclaredMethods()) {
                m.setAccessible(true);
                if (m.getParameterCount() == 1) {
                    try {
                        m.invoke(pretasks, query);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        AppLogger.ApplicationLog(e);
                    }
                }
            }
        }

        int genKey = -1;
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(query.build(), PreparedStatement.RETURN_GENERATED_KEYS)) {
            int success = ps.executeUpdate();
            if (success >= 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    genKey = rs.getInt(1);
                }
            }
        }

        if (pretasks != null) {
            System.out.println(pretasks.getResultMap());
        }

        if (genKey >= 0 && !(query.getTableName().equals(Table.ChangeLog))) {
            PostExecuteTasks posttasks = new PostExecuteTasks();
            for (Method m : posttasks.getClass().getDeclaredMethods()) {
                try {
                    m.setAccessible(true);
                    m.invoke(posttasks, query, pretasks.getResultMap());
                } catch (IllegalAccessException | InvocationTargetException e) {
                    AppLogger.ApplicationLog(e);
                    e.printStackTrace();
                }
            }
        }
        return genKey;
    }
}
