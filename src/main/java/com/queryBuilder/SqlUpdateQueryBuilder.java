package com.queryBuilder;

import java.util.StringJoiner;

import com.queryLayer.Condition;
import com.queryLayer.Update;

public class SqlUpdateQueryBuilder implements Builder {
	Update updateObj;

	public SqlUpdateQueryBuilder(Update update) {
		this.updateObj = update;
	}

	public String build() throws BuildException {
		String query = "UPDATE ";

		// tableName
		query += updateObj.getTableName().value() + " SET ";

		if (this.updateObj.getColumns().isEmpty() && this.updateObj.getValues().isEmpty()) {
			throw new BuildException("insufficient column and values data");
		} else if (this.updateObj.getColumns().size() != this.updateObj.getValues().size()) {
			throw new BuildException("unequal columns and values");
		}

		else {
			StringJoiner colAndValueJoiner = new StringJoiner(", ");
			for (int i = 0; i < this.updateObj.getColumns().size(); i++) {
				colAndValueJoiner.add(this.updateObj.getColumns().get(i).value() + " = " +
						(CheckDataType.isFloat(this.updateObj.getValues().get(i)) ||
								CheckDataType.isInt(this.updateObj.getValues().get(i)) ||
								CheckDataType.isLong(this.updateObj.getValues().get(i))
										? this.updateObj.getValues().get(i)
										: "'" + this.updateObj.getValues().get(i) + "'"));
			}
			query += colAndValueJoiner.toString();

		}

		if (!this.updateObj.getConditions().isEmpty()) {
			StringJoiner conditionJoiner = new StringJoiner(" AND ");
			for (Condition c : this.updateObj.getConditions()) {
				conditionJoiner.add(String.format("%s %s %s", c.column.value(), c.operator.value(),
						((CheckDataType.isFloat(c.value) || CheckDataType.isInt(c.value)
								|| CheckDataType.isLong(c.value)) ? c.value : "'" + c.value + "'")));
			}
			query += " WHERE " + conditionJoiner.toString();

		}

		return query + ";";
	}

}
