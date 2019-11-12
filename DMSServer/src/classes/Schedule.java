package classes;
import java.util.Date;

//스케쥴
public class Schedule
{
	private String scheduleId;			//스케쥴 고유 아이디
	private Date startDate;				//시작일
	private Date endDate;				//종료일
	private String description;			//비고, 설명
	private int code;					//스케쥴 할일 코드(외래키)
	
	public String getScheduleId()
	{
		return scheduleId;
	}
	public void setScheduleId(String scheduleId)
	{
		this.scheduleId = scheduleId;
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
	
	public int getCode()
	{
		return code;
	}
	public void setCode(int code)
	{
		this.code = code;
	}
}
