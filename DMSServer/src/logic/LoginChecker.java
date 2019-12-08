package logic;

import java.sql.SQLException;
import DB.AccountParser;
import enums.Code2;
import enums.UserType;
import models.Account;

public class LoginChecker {
	public static Account check(Account account)
	{
		String id = account.accountId;
		String pw = account.password;
		Code2.LoginResult result = null;
		try {
			result = AccountParser.check(id, pw);
		} catch (SQLException e) {
			e.printStackTrace();
			result = Code2.LoginResult.FAIL;
		}
		return new Account(id, pw, UserType.get(result));
	}
}
