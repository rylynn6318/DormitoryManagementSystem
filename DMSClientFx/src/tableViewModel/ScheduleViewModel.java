package tableViewModel;

import java.util.Date;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import models.Schedule;

//관리자 - 선발 일정 조회 및 관리 테이블뷰 모델
public class ScheduleViewModel
{
	private StringProperty scheduleIdStr;
	private StringProperty codeStr;
	private StringProperty startDateStr;
	private StringProperty endDateStr;
	private StringProperty descriptionStr;
	
	public ScheduleViewModel(int scheduleId, String toDo, Date startDate, Date endDate, String description)
	{
		scheduleIdStr = new SimpleStringProperty(String.valueOf(scheduleId));
		codeStr = new SimpleStringProperty(toDo);
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