
//계정
public class Account
{
	private String accountId;			//아이디, 학생은 학번이 들어가게 되고, 선생님/관리자는 문자열이 들어갈 수 있음.
	private String password;			//비밀번호
	private int type;					//타입 , 학생 0, 관리자 1, 선생님 2 이런식으로 들어가게 될 것임.
	
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
	
	public int getType()
	{
		return type;
	}
	public void setType(int type)
	{
		this.type = type;
	}
}
