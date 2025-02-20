package com.queryBuilder;

import java.util.StringJoiner;
import com.queryLayer.Insert;

public class SqlInsertQueryBuilder implements Builder {
    private final Insert insertObj;

    public SqlInsertQueryBuilder(Insert insert) {
        this.insertObj = insert;
    }

    public String build() throws BuildException {
        if (insertObj.getColumns().isEmpty() && insertObj.getValues().isEmpty()) {
            throw new BuildException("Insufficient data of columns and values");
        }

        StringBuilder query = new StringBuilder("INSERT INTO ")
                .append(insertObj.getTableName().value()).append(" ");

        if (insertObj.getColumns().isEmpty()) {
            StringJoiner valueJoiner = new StringJoiner(",", "(", ")");
            insertObj.getValues().forEach(valueJoiner::add);
            query.append("VALUES ").append(valueJoiner);
        } else {
            if (insertObj.getColumns().size() != insertObj.getValues().size()) {
                throw new BuildException("Column and values size didn't match, please check your query");
            }

            StringJoiner columnJoiner = new StringJoiner(",", "(", ")");
            insertObj.getColumns().forEach(col -> columnJoiner.add(col.value()));

            StringJoiner valueJoiner = new StringJoiner(",", "(", ")");
            insertObj.getValues().forEach(val -> valueJoiner.add(
                    (CheckDataType.isFloat(val) || CheckDataType.isInt(val) || CheckDataType.isLong(val)) ? val : "'" + val + "'"));

            query.append(columnJoiner).append(" VALUES ").append(valueJoiner);
        }
        return query.append(";").toString();
    }
}
