
//호실정보
public class Room
{
	private int roomId;					//방의 고유 아이디
	private String number;				//호실 번호, 몇호실
	private int capacity;				//몇인실
	private String dormitoryName;		//생활관명(외래키)
	private char gender;				//성별(외래키)
	private int semesterCode;			//학기코드(외래키)
	
	public int getRoomId()
	{
		return roomId;
	}
	public void setRoomId(int roomId)
	{
		this.roomId = roomId;
	}
	
	public String getNumber()
	{
		return number;
	}
	public void setNumber(String number)
	{
		this.number = number;
	}
	
	public int getCapacity()
	{
		return capacity;
	}
	public void setCapacity(int capacity)
	{
		this.capacity = capacity;
	}
	
	public String getDormitoryName()
	{
		return dormitoryName;
	}
	public void setDormitoryName(String dormitoryName)
	{
		this.dormitoryName = dormitoryName;
	}
	
	public char getGender()
	{
		return gender;
	}
	public void setGender(char gender)
	{
		this.gender = gender;
	}
	
	public int getSemesterCode()
	{
		return semesterCode;
	}
	public void setSemesterCode(int semesterCode)
	{
		this.semesterCode = semesterCode;
	}
}
