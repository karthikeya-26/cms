package com.queryLayer;
import java.util.Properties;

import com.dbconn.Database;
import com.queryBuilder.*;

public abstract class Query {
	static Builder builder;

	static Properties prop = Database.prop;
	
	public abstract String build ();
	
	
}
