package com.queryBuilder;

import java.util.StringJoiner;
import com.queryLayer.Condition;
import com.queryLayer.Delete;

public class SqlDeleteQueryBuilder {
    private final Delete deleteObj;

    public SqlDeleteQueryBuilder(Delete delete) {
        this.deleteObj = delete;
    }

    public String build() throws BuildException {
        if (deleteObj.getConditions().isEmpty()) {
            throw new BuildException("Insufficient data to build the delete statement");
        }
        
        StringBuilder query = new StringBuilder("DELETE FROM ")
                .append(deleteObj.getTableName().value())
                .append(" WHERE ");

        StringJoiner conditionJoiner = new StringJoiner(" AND ");
        for (Condition condition : deleteObj.getConditions()) {
            String formattedValue = (CheckDataType.isFloat(condition.value) || 
                                     CheckDataType.isInt(condition.value) || 
                                     CheckDataType.isLong(condition.value)) 
                                     ? condition.value 
                                     : "'" + condition.value + "'";
            
            conditionJoiner.add(String.format("%s %s %s", 
                                              condition.column.value(), 
                                              condition.operator.value(), 
                                              formattedValue));
        }
        
        query.append(conditionJoiner);
        return query.append(";").toString();
    }
}
