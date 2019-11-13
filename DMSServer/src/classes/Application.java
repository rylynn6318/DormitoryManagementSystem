package classes;
//신청
public class Application
{
	private int applicationId;		//고유 아이디
	private String studentId;		//학생 학번(외래키)
	private int choice;				//지망
	private int mealType;			//몇일식				0이면 안함, 5이면 5일식, 7이면 7일식
	private boolean isPaid;			//납부여부
	private boolean isPassed;		//합격여부	
	private boolean isLastPassed;	//결과, 최종합격여부
	private String dormitoryName;	//생활관명(외래키)
	private char gender;			//성별(외래키)
	private int semesterCode;		//학기코드(외래키)
	
	public int getApplicationId()
	{
		return applicationId;
	}
	public void setApplicationId(int applicationId)
	{
		this.applicationId = applicationId;
	}
	
	public String getStudentId()
	{
		return studentId;
	}
	public void setStudentId(String studentId)
	{
		this.studentId = studentId;
	}
	
	public int getChoice()
	{
		return choice;
	}
	public void setChoice(int choice)
	{
		this.choice = choice;
	}
	
	public int getMealType()
	{
		return mealType;
	}
	public void setMealType(int mealType)
	{
		this.mealType = mealType;
	}
	
	public boolean isPaid()
	{
		return isPaid;
	}
	public void setPaid(boolean isPaid)
	{
		this.isPaid = isPaid;
	}
	
	public boolean isPassed()
	{
		return isPassed;
	}
	public void setPassed(boolean isPassed)
	{
		this.isPassed = isPassed;
	}
	
	public boolean isLastPassed()
	{
		return isLastPassed;
	}
	public void setLastPassed(boolean isLastPassed)
	{
		this.isLastPassed = isLastPassed;
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
