package shared.enums;

import java.io.Serializable;

//사용자 타입, enum타입임. 학생 0, 관리자 1, 선생님 2.
public enum UserType implements Serializable
{
	STUDENT(0), ADMINISTRATOR(1), TEACHER(2);
	
	private final int type;
	
	UserType(int type)
	{
		this.type = type;
	}
	
	//DB에 넣기위해 int로 받으려면 이거 호출하면됨.
	public int getTypeAsInt()
	{
		return this.type;
	}
}
