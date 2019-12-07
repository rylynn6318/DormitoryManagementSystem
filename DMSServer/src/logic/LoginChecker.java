package logic;

import java.sql.SQLException;
import DB.AccountParser;
import enums.Code2;
import enums.UserType;
import models.Account;

public class LoginChecker {
	public static Account check(Account account) throws ClassNotFoundException, SQLException
	{
		String id = account.accountId;
		String pw = account.password;
		Code2.LoginResult result = AccountParser.check(id, pw);
		return new Account(id, pw, UserType.get(result));
	}
}
