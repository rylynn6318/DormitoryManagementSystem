package models;
import java.io.Serializable;
import java.util.Date;

//스케쥴
public final class Schedule implements Serializable
{
	//키
	public final String scheduleId;			//스케쥴 고유 아이디
	public final int code;					//스케쥴 할일 코드(외래키)
	
	//키가 아닌 컬럼
	public final Date startDate;				//시작일
	public final Date endDate;				//종료일
	public final String description;			//비고, 설명
	
	public Schedule(String scheduleId, int code, Date startDate, Date endDate, String description)
	{
		this.scheduleId = scheduleId;
		this.code = code;
		this.startDate = startDate;
		this.endDate = endDate;
		this.description = description;
	}
}
