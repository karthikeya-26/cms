package com.dbconn;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TableCreator {
		
	public static void CreateDbTables() {
	
		
		InputStream is = TableCreator.class.getClassLoader().getResourceAsStream("schema.json");
		
		if(is == null) {
			System.out.println("File Not Found");
			return;
		}
		JSONObject dbSchema = null;
		
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))){
			StringBuilder jsonBUilder = new StringBuilder();
			String line;
			while((line =reader.readLine())!=null) {
				jsonBUilder.append(line);
			}
			
			dbSchema = new JSONObject(jsonBUilder.toString());
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		JSONObject cms = dbSchema.getJSONObject("cms");
		
		for(String tableName : cms.keySet()) {
			StringBuilder createTable = new StringBuilder("CREATE TABLE IF NOT EXISTS "+tableName+" (");
			JSONArray cols = cms.getJSONObject(tableName).getJSONArray("columns");
			
			for(int i=0; i<cols.length(); i++) {
				JSONObject column = cols.getJSONObject(i);
				String name = column.getString("name");
				String type = column.getString("type");
				String isNull = column.getString("null").equals("NO")?"NOT NULL" : "NULL";
				String key = column.optString("key","");
				String extra = column.optString("extra", "");
				
				String defaultval = key.equals("PRI") ? "PRIMARY KEY" : key.equals("MUL") ? "INDEX" : "";
				createTable.append(name).append(" ").append(type).append(" ").append(isNull);
				if (!defaultval.isEmpty()) createTable.append(" ").append(defaultval);
				if (!extra.isEmpty()) createTable.append(" ").append(extra);
				if (i<cols.length()-1) {
					createTable.append(", ");
				}
			}
			createTable.append(");");
			
			System.out.println(createTable.toString());
		}
	}
	
	public static void main(String[] args) {
		CreateDbTables();
//		CREATE TABLE IF NOT EXISTS contact_mails (contact_id int NOT NULL PRIMARY KEY, mail varchar(30) NOT NULL PRIMARY KEY);
//		CREATE TABLE IF NOT EXISTS sessions (session_id varchar(255) NOT NULL PRIMARY KEY, user_id int NOT NULL INDEX, created_time bigint NOT NULL, last_accessed_time bigint NOT NULL);
//		CREATE TABLE IF NOT EXISTS contactsO (contact_id int NOT NULL PRIMARY KEY auto_increment, first_name varchar(30) NOT NULL, last_name varchar(30) NOT NULL, user_id int NULL INDEX, address varchar(50) NULL, created_at bigint NOT NULL);
//		CREATE TABLE IF NOT EXISTS contact_mobile_numbers (contact_id int NOT NULL PRIMARY KEY, number varchar(20) NOT NULL PRIMARY KEY);
//		CREATE TABLE IF NOT EXISTS user_details (user_id int NOT NULL PRIMARY KEY auto_increment, user_name varchar(30) NOT NULL, password varchar(72) NOT NULL, first_name varchar(30) NOT NULL, last_name varchar(30) NOT NULL, contact_type varchar(10) NOT NULL);
//		CREATE TABLE IF NOT EXISTS group_contacts (group_id int NOT NULL PRIMARY KEY auto_increment, contact_id int NOT NULL PRIMARY KEY);
//		CREATE TABLE IF NOT EXISTS user_mails (mail_id int NOT NULL PRIMARY KEY auto_increment, mail varchar(30) NOT NULL, user_id int NULL INDEX, is_primary tinyint NULL);
//		CREATE TABLE IF NOT EXISTS user_groups (group_id int NOT NULL PRIMARY KEY auto_increment, group_name varchar(30) NOT NULL, user_id int NULL INDEX);

	}
	
	
}
