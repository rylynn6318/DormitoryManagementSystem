package application;

import models.*;

//클라이언트에서 Singleton으로 사용할 UserInfo객체. 사실상 Account랑 동일함. 
//근데 다른 정보를 저장할지도 모르니 일단 만들어둠. 그리고 Account와 다르게 싱글톤임.
public class UserInfo
{
	//타입 = 학생 0, 관리자 1, 선생님 2 이런식으로 들어가게 될 것임.

	static public Account account;
	static private UserInfo _instance;
	
	static public UserInfo getInstance()
	{
		if(_instance == null)
		{
			_instance = new UserInfo();
		}
		return _instance;
	}
}
