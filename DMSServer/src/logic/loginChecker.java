package logic;

import java.sql.SQLException;
import DB.AccountParser;
import enums.Code2;
import enums.UserType;
import models.Account;

public class loginChecker {
	public static Account check(String id, String pw) throws ClassNotFoundException, SQLException
	{
		Code2.LoginResult result = AccountParser.check(id, pw);
		return new Account(id, pw, UserType.get(result));
	}
}
