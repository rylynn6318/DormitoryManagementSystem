package models;

import java.io.Serializable;
import java.util.Date;

import enums.Seat;

//배정내역
public final class PlacementHistory implements Serializable
{
	//키
	public final String studentId;			//학생의 학번(외래키)
	public final String roomId;					//방의 고유 아이디(외래키)
	public final int semester;				//학기(외래키)
	public final String dormitoryName;		//생활관명(외래키)
	
	//키가 아닌 컬럼
	public final Seat seat;					//자리
	public final Date checkout;				//퇴사 예정일

	public PlacementHistory(String studentId, String roomId, int semester, String dormitoryName, Seat seat, Date checkout) 
	{
		this.studentId = studentId;
		this.roomId = roomId;
		this.semester = semester;
		this.dormitoryName = dormitoryName;
		this.seat = seat;
		this.checkout = checkout;
	}
	
	//키값으로만 구성된 생성자. 클라이언트에서 삭제요청할때 사용함.
	public PlacementHistory(String studentId, String roomId, int semester, String dormitoryName) 
	{
		this.studentId = studentId;
		this.roomId = roomId;
		this.semester = semester;
		this.dormitoryName = dormitoryName;
		seat = null;
		checkout = null;
	}
	
	public PlacementHistory(String roomId, Seat seat)
	{
		this.studentId = null;
		this.semester = -1;
		this.dormitoryName = null;
		this.checkout = null;
		this.roomId = roomId;
		this.seat = seat;
	}
}
