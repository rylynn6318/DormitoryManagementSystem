package classes;
import java.io.Serializable;
import java.util.Date;

//스케쥴
public class Schedule implements Serializable
{
	//키
	private String scheduleId;			//스케쥴 고유 아이디
	private int code;					//스케쥴 할일 코드(외래키)
	
	//키가 아닌 컬럼
	private Date startDate;				//시작일
	private Date endDate;				//종료일
	private String description;			//비고, 설명
	
	public Schedule()
	{
		
	}
	
	public Schedule(String scheduleId, int code, Date startDate, Date endDate, String description)
	{
		this.scheduleId = scheduleId;
		this.code = code;
		this.startDate = startDate;
		this.endDate = endDate;
		this.description = description;
	}
	
	public String getScheduleId()
	{
		return scheduleId;
	}
	public void setScheduleId(String scheduleId)
	{
		this.scheduleId = scheduleId;
	}
	
	public int getCode()
	{
		return code;
	}
	public void setCode(int code)
	{
		this.code = code;
	}
	
	public Date getStartDate()
	{
		return startDate;
	}
	public void setStartDate(Date startDate)
	{
		this.startDate = startDate;
	}
	
	public Date getEndDate()
	{
		return endDate;
	}
	public void setEndDate(Date endDate)
	{
		this.endDate = endDate;
	}
	
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
}
