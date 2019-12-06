package classes;

import enums.*;
import java.io.Serializable;

//호실정보
public final class RoomInfo implements Serializable
{
	//키
	public final String dormitoryName;		//생활관명(외래키)
	public final int semesterCode;			//학기코드(외래키)
	public final String roomNumber;			//호, 몇호실
	public final Gender gender;				//성별(외래키)
	
	//키가 아닌 컬럼
	public final int capacity;				//몇인실
	
}
