package tableViewModel;

import java.util.Date;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import shared.classes.PlacementHistory;

//관리자 - 입사자 조회 및 관리 테이블뷰 모델
public class PlacementHistoryViewModel extends PlacementHistory
{
	private StringProperty studentIdStr;
	private StringProperty roomIdStr;
	private StringProperty semesterStr;
	private StringProperty dormitoryNameStr;
	private StringProperty seatStr;
	private StringProperty checkoutStr;
	
	public PlacementHistoryViewModel(String studentId, int roomId, int semester, String dormitoryName, char seat, Date checkout)
	{
		
		super.setStudentId(studentId);
		super.setRoomId(roomId);
		super.setSemester(semester);
		super.setDormitoryName(dormitoryName);
		super.setSeat(seat);
		super.setCheckout(checkout);
		
		this.studentIdStr = new SimpleStringProperty(studentId);
		this.roomIdStr = new SimpleStringProperty(Integer.toString(roomId));
		this.semesterStr = new SimpleStringProperty(Integer.toString(semester));
		this.dormitoryNameStr = new SimpleStringProperty(dormitoryName);
		this.seatStr = new SimpleStringProperty(String.valueOf(seat));
		this.checkoutStr = new SimpleStringProperty(checkout.toString());
	}
	
	public StringProperty studentIdProperty()
	{
		return studentIdStr;
	}
	
	public StringProperty roomIdProperty()
	{
		return roomIdStr;
	}
	
	public StringProperty semesterProperty()
	{
		return semesterStr;
	}
	
	public StringProperty dormitoryNameProperty()
	{
		return dormitoryNameStr;
	}
	
	public StringProperty seatProperty()
	{
		return seatStr;
	}
	
	public StringProperty checkoutProperty()
	{
		return checkoutStr;
	}
}
