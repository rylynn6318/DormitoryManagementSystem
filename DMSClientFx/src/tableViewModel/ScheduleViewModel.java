package tableViewModel;

import java.util.Date;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import models.Schedule;

//관리자 - 선발 일정 조회 및 관리 테이블뷰 모델
public class ScheduleViewModel
{
	public final Schedule schedule;

	private StringProperty scheduleIdStr;
	private StringProperty codeStr;
	private StringProperty startDateStr;
	private StringProperty endDateStr;
	private StringProperty descriptionStr;
	
	public ScheduleViewModel(String scheduleId, int code, Date startDate, Date endDate, String description)
	{
		schedule = new Schedule(scheduleId, code, startDate, endDate, description);
		scheduleIdStr = new SimpleStringProperty(scheduleId);
		codeStr = new SimpleStringProperty(String.valueOf(code));			//이거 스케쥴 할일 코드 가져와서 String 보여줘야됨.
		startDateStr = new SimpleStringProperty(startDate.toString());
		endDateStr = new SimpleStringProperty(endDate.toString());
		descriptionStr = new SimpleStringProperty(description);
	}
	
	public StringProperty scheduleIdProperty()
	{
		return scheduleIdStr;
	}
	
	public StringProperty codeProperty()
	{
		return codeStr;
	}
	
	public StringProperty startDateProperty()
	{
		return startDateStr;
	}
	
	public StringProperty endDateProperty()
	{
		return endDateStr;
	}
	
	public StringProperty descriptionProperty()
	{
		return descriptionStr;
	}
}