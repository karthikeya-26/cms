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
import com.enums.Table;
import com.loggers.AppLogger;
import com.queryBuilder.BuildException;
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

    public String build() throws BuildException{
        return null;
    }

    public List<ResultObject> executeQuery(Query query, Class<? extends ResultObject> clazz) throws QueryException {
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
        } catch (SQLException e) {
            AppLogger.ApplicationLog(e);
            throw new QueryException("Database error occurred while executing the query: " + e.getMessage(), e);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            AppLogger.ApplicationLog(e);
            throw new QueryException("Error occurred while instantiating the result object class: " + clazz.getName(), e);
        } catch (ReflectiveOperationException e) {
            AppLogger.ApplicationLog(e);
            throw new QueryException("Reflection error occurred while mapping result set to object: " + e.getMessage(), e);
        } catch (Exception e) {
            AppLogger.ApplicationLog(e);
            throw new QueryException("An unexpected error occurred: " + e.getMessage(), e);
        }

        return resultList;
    }


    public List<HashMap<Columns, Object>> executeQuery(Query query, HashMap<Columns, Class<?>> fields) throws QueryException {
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
                    try {
                        if (fieldType == Integer.class ||  fieldType == int.class) {
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
                    } catch (SQLException e) {
                        throw new QueryException("Error retrieving value for column: " + colName.value(), e);
                    }
                }
                resultObj.add(row);
            }
        } catch (SQLException e) {
            AppLogger.ApplicationLog(e);
            throw new QueryException("Database error occurred while executing the query: " + e.getMessage(), e);
        } catch (Exception e) {
            AppLogger.ApplicationLog(e);
            throw new QueryException("An unexpected error occurred: " + e.getMessage(), e);
        }

        return resultObj;
    }


    public int executeUpdate(Query query) throws QueryException {
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
                        throw new QueryException("Error executing pre-tasks for query: " + query.getTableName(), e);
                    }
                }
            }
        }

        int status;
        String sqlStatement;

        try {
            sqlStatement = query.build(); // Convert BuildException to QueryException here.
        } catch (BuildException e) {
            AppLogger.ApplicationLog(e);
            throw new QueryException("Error building query for table: " + query.getTableName(), e);
        }

        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sqlStatement)) {
            status = ps.executeUpdate();
        } catch (SQLException e) {
            AppLogger.ApplicationLog(e);
            throw new QueryException("Database error occurred while executing the query for table: " + query.getTableName() + " | Query: " + sqlStatement, e);
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
                    throw new QueryException("Error executing post-tasks for query for table: " + query.getTableName() + " | Query: " + sqlStatement, e);
                }
            }
        }

        return status;
    }



    public int executeUpdate(Query query, boolean returnGeneratedKey) throws QueryException {
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
                        throw new QueryException("Error executing pre-tasks for query: " + query.getTableName(), e);
                    }
                }
            }
        }

        int genKey = -1;
        String sqlStatement;

        // Handle query.build() and convert BuildException to QueryException
        try {
            sqlStatement = query.build();
        } catch (BuildException e) {
            AppLogger.ApplicationLog(e);
            throw new QueryException("Error building query for table: " + query.getTableName(), e);
        }

        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sqlStatement, PreparedStatement.RETURN_GENERATED_KEYS)) {

            int success = ps.executeUpdate();

            // Retrieve generated key if applicable
            if (success >= 0 && returnGeneratedKey) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        genKey = rs.getInt(1);
                    }
                } catch (SQLException e) {
                    AppLogger.ApplicationLog(e);
                    throw new QueryException("Error retrieving generated key for query: " + query.getTableName() + " | Query: " + sqlStatement, e);
                }
            }

        } catch (SQLException e) {
            AppLogger.ApplicationLog(e);
            throw new QueryException("Database error occurred while executing update for query: " + query.getTableName() + " | Query: " + sqlStatement, e);
        }

        // Log pre-tasks results if available
//        if (pretasks != null) {
//            System.out.println(pretasks.getResultMap());
//        }

        // Execute post-tasks if query succeeded
        if (genKey >= 0 && !(query.getTableName().equals(Table.ChangeLog))) {
            PostExecuteTasks posttasks = new PostExecuteTasks();
            for (Method m : posttasks.getClass().getDeclaredMethods()) {
                m.setAccessible(true);
                try {
                    m.invoke(posttasks, query, pretasks.getResultMap());
                } catch (IllegalAccessException | InvocationTargetException e) {
                    AppLogger.ApplicationLog(e);
                    throw new QueryException("Error executing post-tasks for query: " + query.getTableName() + " | Query: " + sqlStatement, e);
                }
            }
        }

        return genKey;
    }


    
}
