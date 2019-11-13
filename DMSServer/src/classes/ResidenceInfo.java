package classes;

import java.util.Date;

//거주 정보
public class ResidenceInfo
{
	private String studentId;			//학생의 학번(외래키)
	private int roomId;					//방의 고유 아이디(외래키)
	private int semesterCode;			//학기코드
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
	
	public int getSemesterCode()
	{
		return semesterCode;
	}
	public void setSemesterCode(int semesterCode)
	{
		this.semesterCode = semesterCode;
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
