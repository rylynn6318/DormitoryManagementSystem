package shared;
//호실정보
public class RoomInfo
{
	//키
	private String dormitoryName;		//생활관명(외래키)
	private int semesterCode;			//학기코드(외래키)
	private String roomNumber;			//호, 몇호실
	private char gender;				//성별(외래키)
	
	//키가 아닌 컬럼
	private int capacity;				//몇인실
	
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

}
