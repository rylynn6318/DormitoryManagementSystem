package assign;

import shared.classes.RoomInfo;
//RoomInfo를 상속받아서 배정에 필요한 정보를 더 넣은 
public class AssignRoomInfo extends RoomInfo  {
	private String seat;
	private boolean isEmpty;
	private String studentId;
	
	public String getSeat()
	{
		return seat;
	}
	public void setSeat(String seat)
	{
		this.seat = seat;
	}
	public boolean getIsEmpty()
	{
		return isEmpty;
	}
	public void setIsEmpty(boolean isEmpty)
	{
		this.isEmpty = isEmpty;
	}
	public String getStudentId()
	{
		return studentId;
	}
	public void setStudentId(String studentId)
	{
		this.studentId = studentId;
	}
}
