package shared.classes;

import java.io.Serializable;

import shared.enums.UserType;

import java.io.Serializable;

//커밋 테스트
//계정
public class Account implements Serializable
{
	//키
	private String accountId;			//아이디, 학생은 학번이 들어가게 되고, 선생님/관리자는 문자열이 들어갈 수 있음.
	
	//키가 아닌 컬럼
	private String password;			//비밀번호
	private UserType userType;			//사용자 타입 , 학생 0, 관리자 1, 선생님 2
	
	public String getAccountId()
	{
		return accountId;
	}
	public void setAccountId(String accountId)
	{
		this.accountId = accountId;
	}
	
	public String getPassword()
	{
		return password;
	}
	public void setPassword(String password)
	{
		this.password = password;
	}
	
	public UserType getUserType()
	{
		return this.userType;
	}
	public void setUserType(UserType userType)
	{
		this.userType = userType;
	}
}
