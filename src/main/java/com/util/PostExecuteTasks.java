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

		auditTables.add(Table.Sessions);
		auditTables.add(Table.ChangeLog);

		// from config populate this
	}

	public void audit(Query query, HashMap<String, Object> resultMap) throws Exception {
		if((auditTables.contains(query.getTableName()))){
			System.out.println("Table :"+query.getTableName().value() +", so returning");
			return;
		}
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
//			if(!(auditTables.contains(query.getTableName()))) {
//				return;
//			}
			Update updateQuery = (Update) query;
			String table = query.getTableName().value();
			List<Columns> cols = updateQuery.getColumns();
			List<String> vals = updateQuery.getValues();
			List<Condition> conditions = updateQuery.getConditions();

			@SuppressWarnings("unchecked")
			List<HashMap<Columns, Object>> refData = (List<HashMap<Columns, Object>>) resultMap.get("getRefData");
			if(refData== null) {
				return;
			}
			// create old data Json from refData
			
			for(HashMap<Columns, Object> data : refData) {
				JsonObject oldData = new JsonObject();
				for (Map.Entry<Columns, Object> row : data.entrySet()) {
				    String key = row.getKey().value();
				    Object value = row.getValue();
			
				    if (value != null) {
				        oldData.addProperty(key, value.toString());
				    } else {
				        oldData.addProperty(key, (String) "0");
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
			}

			
		} else if (query instanceof Delete) {
			Delete deleteQuery = (Delete) query;
			String table = deleteQuery.getTableName().value();
			
			@SuppressWarnings("unchecked")
			List<HashMap<Columns, Object>> refData = (List<HashMap<Columns, Object>>) resultMap.get("getRefData");
			System.out.println(refData);
			
			if(refData == null) {
				return;
			}
			for(HashMap<Columns, Object> data : refData) {
				JsonObject oldData = new JsonObject();
				for (Map.Entry<Columns, Object> row : data.entrySet()) {
				    String key = row.getKey().value();
				    Object value = row.getValue();
			
				    if (value != null) {
				        oldData.addProperty(key, value.toString());
				    } else {
				        oldData.addProperty(key, (String) "0");
				    }
				}
				Insert audit = new Insert();
				audit.table(Table.ChangeLog).columns(ChangeLog.TABLE_NAME,ChangeLog.REQ_TYPE,ChangeLog.OLD_VAL)
				.values(table,"DELETE",oldData.toString());	
				System.out.println(audit.build());
				audit.executeUpdate();
			}

			
			
		}
	}

	public void notifyServers(Query query, HashMap<String, Object> resultMap) {
		if (!(serverNotifications.contains(query.getTableName()))) {
			System.out.println("returning from server notify");
			return;
		}
		if (query instanceof Update) {
			Update updateQuery = (Update) query;
			List<Condition> conditions = updateQuery.getConditions();
			if (conditions.isEmpty()) {
				return;
			}
			RequestSender rs = new RequestSender();
			StringJoiner paramsAndvals = new StringJoiner("&");
			for (Condition c : conditions) {
				paramsAndvals.add(c.getColumn().value() + "=" + c.getValue());
			}
			rs.send(paramsAndvals.toString());
			return;

		}
	}
}
