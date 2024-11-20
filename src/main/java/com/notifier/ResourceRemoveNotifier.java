//package com.notifier;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import com.loggers.AppLogger;
//import com.mysql.cj.protocol.x.SyncFlushDeflaterOutputStream;
//import com.queryLayer.Condition;
//import com.queryLayer.Query;
//import com.queryLayer.Select;
//import com.session.SessionDataManager;
//
//public class ResourceRemoveNotifier {
//	private static List<String> cached_tables = new ArrayList<String>();
//	
////	public static void removeResource(Query query) {
////		if(query.getClass().getName().contains("Delete") && query.getTableName().equals("Sessions")){
////			AppLogger.ApplicationLog("BEFORE DELETING :"+SessionDataManager.session_data.toString());
////			List<Condition> conditions = query.getConditions();
////			String session_id = null;
////			for(Condition condition : conditions) {
////				if(condition.getColumn().contains("session_id")) {
////					session_id = condition.getValue();
////				}
////			}
////			System.out.println("execute uodate :session_id -_>"+session_id);
////			SessionmapUpdateNotifier.removeSession(session_id);
////			AppLogger.ApplicationLog("Removing sessiondata of" +session_id+"  from other servers");
////		}
////		else if(query.getClass().getName().contains("Update") && query.getTableName().equals("UserDetails")) {
////			List<String> columns = query.getColumns();
////			int user_id_index = columns.indexOf("user_id");
////			String user_id = query.getValues().get(user_id_index);
////			UserDetailsUpdateNotifier.removeUserFromCache(user_id);
////		}
////	}
//	
//	public static void sendNotification(Query query) {
//		Select s  =  (Select) query;
//		cached_tables.add("Sessions");
//		cached_tables.add("UserDetails");
//		boolean cached = false;
//		String tableName = s.getTable().value();
//		if(cached_tables.contains(tableName)) {
//			cached= true;
//		}
//		
//		if(cached) {
//			switch (tableName) {
//			case "Sessions":
//				if(query.getClass().getSimpleName().equals("Delete")) {
//					List<Condition> conditions = s.getConditions();
//					String session_id = null;
//					for(Condition condition : conditions) {
//						if(condition.getColumn().value().equals("session_id")) {
//							session_id = condition.getValue();
//						}
//					}
//					SessionmapUpdateNotifier.removeSession(session_id);
//				}
//				else if (query.getClass().getSimpleName().equals("Insert") || query.getClass().getSimpleName().equals("Update")) {
//					return;
//				}
//				break;
//			case "UserDetails":
//				if(query.getClass().getSimpleName().equals("Delete") || query.getClass().getSimpleName().equals("Update")) {
//					List<Condition> conditions = s.getConditions();
//					String user_id = null;
//					for(Condition condition : conditions) {
//						if(condition.getColumn().value().equals("user_id")) {
//							user_id = condition.getValue();
//						}
//					}
//					UserDetailsUpdateNotifier.removeUserFromCache(user_id);
//				}
//				else if (query.getClass().getSimpleName().equals("Insert") ) {
////					List<String> columns = query.getColumns();
////					String user_id = null;
////					int user_id_index = columns.indexOf("user_id");
////					user_id = query.getValues().get(user_id_index);
////					UserDetailsUpdateNotifier.removeUserFromCache(user_id);
//					return ;
//				}
//				break;
//		
//			default:
//				break; 
//			}
//		}
//		
//	}
//}
