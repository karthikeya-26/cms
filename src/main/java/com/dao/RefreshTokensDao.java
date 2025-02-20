package com.dao;

import java.util.ArrayList;
import java.util.List;

import com.dbObjects.RefreshTokensObj;
import com.dbObjects.ResultObject;
import com.enums.Operators;
import com.enums.RefreshTokens;
import com.enums.Table;
import com.queryLayer.Delete;
import com.queryLayer.Insert;
import com.queryLayer.QueryException;
import com.queryLayer.Select;

public class RefreshTokensDao {
	// SELECT

	public RefreshTokensObj getRefTokenObj(String refreshToken) throws QueryException {
		Select getToken = new Select();
		getToken.table(Table.RefreshTokens).condition(RefreshTokens.REFRESH_TOKEN, Operators.Equals, refreshToken);
		List<ResultObject> resultSet = getToken.executeQuery(RefreshTokensObj.class);
		return resultSet.size() > 0 ? (RefreshTokensObj) resultSet.get(0) : null;
	}

	public List<RefreshTokensObj> getUserRefreshTokens(Integer userId) throws QueryException {
		List<RefreshTokensObj> tokens = null;

		Select getUserTokens = new Select();
		getUserTokens.table(Table.RefreshTokens).condition(RefreshTokens.USER_ID, Operators.Equals, userId.toString());
		List<ResultObject> resultSet = getUserTokens.executeQuery(RefreshTokensObj.class);
		if (resultSet != null)
			tokens = new ArrayList<RefreshTokensObj>();
		for (ResultObject r : resultSet) {

			tokens.add((RefreshTokensObj) r);
		}
		return tokens;
	}

	// INSERT

	public int addRefreshToken(String refreshToken, String clientId, Integer userId, String scopes)
			throws QueryException {
		Insert addToken = new Insert();
		addToken.table(Table.RefreshTokens).columns(RefreshTokens.REFRESH_TOKEN, RefreshTokens.CLIENT_ID,
				RefreshTokens.USER_ID, RefreshTokens.SCOPES)
				.values(refreshToken, clientId.toString(), userId.toString(), scopes);
		return addToken.executeUpdate(true);
	}

	// DELETE

	public boolean deleteRefreshToken(String refreshToken) throws QueryException {
		Delete deleteToken = new Delete();
		deleteToken.table(Table.RefreshTokens).condition(RefreshTokens.REFRESH_TOKEN, Operators.Equals, refreshToken);
		return deleteToken.executeUpdate() > 0;
	}

	// DELETE -> all tokens of a user
	public boolean deleteUserRefreshTokens(Integer userId) throws QueryException {
		Delete deleteUserTokens = new Delete();
		deleteUserTokens.table(Table.RefreshTokens).condition(RefreshTokens.USER_ID, Operators.Equals,
				userId.toString());
		return deleteUserTokens.executeUpdate() > 0;
	}
}
