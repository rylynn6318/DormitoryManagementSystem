package models;
import java.io.Serializable;

//스케쥴 할일 코드
public final class ScheduleCode implements Serializable
{
	//키
	public final int code;					//스케쥴 할일 코드
	public final String name;				//스케쥴 이름
	
	
	public ScheduleCode(int code, String name)
	{
		this.code = code;
		this.name = name;
	}
}
