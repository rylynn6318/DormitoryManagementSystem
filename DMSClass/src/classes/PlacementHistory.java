package shared.classes;

import java.io.Serializable;
import java.util.Date;

//배정내역
public class PlacementHistory implements Serializable
{
	//키
	private String studentId;			//학생의 학번(외래키)
	private int roomId;					//방의 고유 아이디(외래키)
	private int semester;				//학기(외래키)
	private String dormitoryName;		//생활관명(외래키)
	
	//키가 아닌 컬럼
	private char seat;					//자리
	private Date checkout;				//퇴사 예정일
	
	public String getStudentId()
	{
		return studentId;
	}
	public void setStudentId(String studentId)
	{
		this.studentId = studentId;
	}
	
	public int getRoomId()
	{
		return roomId;
	}
	public void setRoomId(int roomId)
	{
		this.roomId = roomId;
	}
	
	public int getSemester()
	{
		return semester;
	}
	public void setSemester(int semester)
	{
		this.semester = semester;
	}
	
	public String getDormitoryName()
	{
		return dormitoryName;
	}
	public void setDormitoryName(String dormitoryName)
	{
		this.dormitoryName = dormitoryName;
	}
	
	public char getSeat()
	{
		return seat;
	}
	public void setSeat(char seat)
	{
		this.seat = seat;
	}
	
	public Date getCheckout()
	{
		return checkout;
	}
	public void setCheckout(Date checkout)
	{
		this.checkout = checkout;
	}
}
