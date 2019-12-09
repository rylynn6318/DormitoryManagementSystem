package batch;

public class AssignRoomInfo {
	private String seat;
	private String studentId;
	private int checkout;
	private String dormitoryName;		//생활관명(외래키)
	private int semesterCode;			//학기코드(외래키)
	private String roomNumber;			//호, 몇호실
	private char gender;				//성별(외래키)
	private Boolean isNew;
	
	//키가 아닌 컬럼
	private int capacity;				//몇인실
	
	public Boolean getIsNew()
	{
		return isNew;
	}
	public void setIsNew(Boolean isNew)
	{
		this.isNew = isNew;
	}
	
	public String getDormitoryName()
	{
		return dormitoryName;
	}
	public void setDormitoryName(String dormitoryName)
	{
		this.dormitoryName = dormitoryName;
	}

	public int getSemesterCode()
	{
		return semesterCode;
	}
	public void setSemesterCode(int semesterCode)
	{
		this.semesterCode = semesterCode;
	}
	public String getRoomNumber()
	{
		return roomNumber;
	}
	public void setRoomNumber(String roomNumber)
	{
		this.roomNumber = roomNumber;
	}
	
	public char getGender()
	{
		return gender;
	}
	public void setGender(char gender)
	{
		this.gender = gender;
	}

	public int getCapacity()
	{
		return capacity;
	}
	
	public void setCapacity(int capacity)
	{
		this.capacity = capacity;
	}
	
	public String getSeat()
	{
		return seat;
	}
	public void setSeat(String seat)
	{
		this.seat = seat;
	}
	public String getStudentId()
	{
		return studentId;
	}
	public void setStudentId(String studentId)
	{
		this.studentId = studentId;
	}
	public int getCheckOut()
	{
		return checkout;
	}
	public void setCheckout(int checkout)
	{
		this.checkout = checkout;
	}
}
