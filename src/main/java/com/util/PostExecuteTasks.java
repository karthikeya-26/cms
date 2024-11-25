package com.util;


import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;

import com.enums.*;
import com.google.gson.JsonObject;
import com.queryLayer.*;

public class PostExecuteTasks {
	static Set<Table> auditTables;
	static Set<Table> serverNotifications;
	
	static {
		auditTables = new HashSet<>();
		serverNotifications = new HashSet<>();
		//from config populate this 
	}

	public void audit(Query query, HashMap<String, Object> resultMap) throws Exception {
//		if(!(auditTables.contains(query.getTableName()))){
//			return;
//		}
		if (query instanceof Insert) {
			Insert insertQuery = (Insert) query;
			String table = insertQuery.getTableName().value();
			List<Columns> cols = insertQuery.getColumns();
			List<String> vals = insertQuery.getValues();

			Insert audit = new Insert();
			audit.table(Table.ChangeLog).columns(ChangeLog.TABLE_NAME, ChangeLog.REQ_TYPE, ChangeLog.NEW_VAL);

		
			JsonObject newData = new JsonObject();
			for (int i = 0; i < cols.size(); i++) {
				newData.addProperty(cols.get(i).value(), vals.get(i));
			}

			audit.values(table, "INSERT", newData.toString());
			audit.executeUpdate();
			return;
			
		} else if (query instanceof Update) {
			System.out.println("Inside  audit update");
			Update updateQuery = (Update) query;
			String table = query.getTableName().value();
			List<Columns> cols = updateQuery.getColumns();
			List<String> vals = updateQuery.getValues();
			List<Condition> conditions = updateQuery.getConditions();
			
			@SuppressWarnings("unchecked")
			HashMap<Columns, Object> refData = (HashMap<Columns, Object>) resultMap.get("getRefData");

			// create old data Json from refData
			JsonObject oldData = new JsonObject();

			for (Map.Entry<Columns, Object> row : refData.entrySet()) {
			    String key = row.getKey().value();
			    Object value = row.getValue();

			    if (value != null) {
			        oldData.addProperty(key, value.toString());
			    } else {
			        oldData.addProperty(key, (String) null);
			    }
			}

			for (Condition c : conditions) {
				oldData.addProperty(c.getColumn().value(), c.getValue());
			}

			// newData Json
			JsonObject newData = new JsonObject();
			for (int i = 0; i < cols.size(); i++) {
				newData.addProperty(cols.get(i).value(), vals.get(i));
			}
			for (Condition c : conditions) {
				newData.addProperty(c.getColumn().value(), c.getValue());
			}

			Insert audit = new Insert();
			audit.table(Table.ChangeLog)
			.columns(ChangeLog.TABLE_NAME, ChangeLog.REQ_TYPE, ChangeLog.OLD_VAL, ChangeLog.NEW_VAL)
			.values(table, "UPDATE", oldData.toString(), newData.toString());
			audit.executeUpdate();
		} else if (query instanceof Delete) {
			Delete deleteQuery = (Delete) query;
			String table = deleteQuery.getTableName().value();
			
			@SuppressWarnings("unchecked")
			HashMap<Columns, Object> refData = (HashMap<Columns, Object>) resultMap.get("getRefData");

			JsonObject oldData = new JsonObject();

			for (Map.Entry<Columns, Object> row : refData.entrySet()) {
			    String key = row.getKey().value();
			    Object value = row.getValue();

			    if (value != null) {
			        oldData.addProperty(key, value.toString());
			    } else {
			        oldData.addProperty(key, (String) null);
			    }
			}

			Insert audit = new Insert();
			audit.table(Table.ChangeLog).columns(ChangeLog.TABLE_NAME,ChangeLog.REQ_TYPE,ChangeLog.OLD_VAL)
			.values(table,"DELETE",oldData.toString());	
			audit.executeUpdate();
			
		}
	}
	
	public void notifyServers(Query query) {
		if(!(serverNotifications.contains(query.getTableName()))){
			return;
		}
		if(query instanceof Update) {
			Update updateQuery = (Update) query;
			List<Condition> conditions =  updateQuery.getConditions();
			if(conditions.isEmpty()) {
				return;
			}
			RequestSender rs = new RequestSender();
			StringJoiner paramsAndvals = new StringJoiner("&");
			for(Condition c : conditions) {
				paramsAndvals.add( c.getColumn().value()+"="+c.getValue());
			}
			rs.send(paramsAndvals.toString());
			return;
			
		}
	}
}
