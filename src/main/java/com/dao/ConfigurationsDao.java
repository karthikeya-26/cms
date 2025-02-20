package com.dao;

import java.util.List;


import com.dbObjects.ConfigurationsObj;
import com.dbObjects.ResultObject;
import com.enums.Configurations;
import com.enums.Operators;
import com.enums.Table;
import com.queryLayer.Delete;
import com.queryLayer.Insert;
import com.queryLayer.QueryException;
import com.queryLayer.Select;
import com.queryLayer.Update;


public class ConfigurationsDao {

    // Select
    public List<ResultObject> getAppConfig() throws DaoException {
        try {
            Select s = new Select();
            s.table(Table.Configurations);
            return s.executeQuery(ConfigurationsObj.class);
        } catch (QueryException e) {
            throw new DaoException("Failed to fetch application configurations", e);
        }
    }

    public ConfigurationsObj getConfigWithId(Integer id) throws DaoException {
        try {
            Select s = new Select();
            s.table(Table.Configurations)
             .condition(Configurations.ID, Operators.Equals, id.toString());
            return (ConfigurationsObj) s.executeQuery(ConfigurationsObj.class).get(0);
        } catch (QueryException e) {
            throw new DaoException("Failed to fetch configuration with ID: " + id, e);
        }
    }

    // Insert
    public boolean addAppConfig(String name, String value) throws DaoException {
        try {
            Insert i = new Insert();
            i.table(Table.Configurations);
            i.columns(Configurations.NAME, Configurations.VALUE).values(name, value);
            return i.executeUpdate() > 0;
        } catch (QueryException e) {
            throw new DaoException("Failed to add application configuration with name: " + name, e);
        }
    }

    // Update Config
    public boolean updateConfig(String name, String value) throws DaoException {
        try {
            Update u = new Update();
            u.table(Table.Configurations)
             .columns(Configurations.NAME, Configurations.VALUE)
             .values(name, value)
             .condition(Configurations.NAME, Operators.Equals, name);
            return u.executeUpdate() >= 0;
        } catch (QueryException e) {
            throw new DaoException("Failed to update configuration with name: " + name, e);
        }
    }

    // Delete Config
    public boolean deleteConfig(Integer id) throws DaoException {
        try {
            Delete d = new Delete();
            d.table(Table.Configurations)
             .condition(Configurations.ID, Operators.Equals, id.toString());
            return d.executeUpdate() > 0;
        } catch (QueryException e) {
            throw new DaoException("Failed to delete configuration with ID: " + id, e);
        }
    }
}
