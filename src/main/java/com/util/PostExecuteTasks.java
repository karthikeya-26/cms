package com.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonObject;
import com.queryLayer.*;
import com.tables.*;

public class PostExecuteTasks {

	void audit(Query query, HashMap<Columns, Object> refData) {
		if (query instanceof Insert) {
			Insert insertQuery = (Insert) query;
			String table = insertQuery.getTableName().value();
			List<Columns> cols = insertQuery.getColumns();
			List<String> vals = insertQuery.getValues();

			Insert audit = new Insert();
			audit.table(Table.ChangeLog).columns(ChangeLog.TABLE_NAME, ChangeLog.REQ_TYPE, ChangeLog.NEW_VAL);

			// create json obj
			JsonObject newData = new JsonObject();
			for (int i = 0; i < cols.size(); i++) {
				newData.addProperty(cols.get(i).value(), vals.get(i));
			}

			audit.values(table, "INSERT", newData.toString());
			audit.executeUpdate();
		} else if (query instanceof Update) {
			Update updateQuery = (Update) query;
			String table = query.getTableName().value();
			List<Columns> cols = updateQuery.getColumns();
			List<String> vals = updateQuery.getValues();
			List<Condition> conditions = updateQuery.getConditions();

			// create old data Json from refData
			JsonObject oldData = new JsonObject();

			for (Map.Entry<Columns, Object> row : refData.entrySet()) {

				oldData.addProperty(row.getKey().value(), (String) row.getValue());
			}

			for (Condition c : conditions) {
				oldData.addProperty(c.getColumn().value(), c.getValue());
			}

			// newData Json
			JsonObject newData = new JsonObject();
			for (int i = 0; i < cols.size(); i++) {
				newData.addProperty(cols.get(i).value(), vals.get(i));
			}

			Insert audit = new Insert();
			audit.table(Table.ChangeLog)
			.columns(ChangeLog.TABLE_NAME, ChangeLog.REQ_TYPE, ChangeLog.OLD_VAL, ChangeLog.NEW_VAL)
			.values(table, "UPDATE", oldData.toString(), newData.toString());
		} else if (query instanceof Delete) {
			Delete deleteQuery = (Delete) query;
			String table = deleteQuery.getTableName().value();

			JsonObject oldData = new JsonObject();
			for (Map.Entry<Columns, Object> row : refData.entrySet()) 
			{
				oldData.addProperty(row.getKey().value(), (String) row.getValue());
			}
			
			Insert audit = new Insert();
			audit.table(Table.ChangeLog).columns(ChangeLog.TABLE_NAME,ChangeLog.REQ_TYPE,ChangeLog.OLD_VAL)
			.values(table,"DELETE",oldData.toString());	
		}
	}
	
	void notifyServers(Query query) {
		
	}
}
