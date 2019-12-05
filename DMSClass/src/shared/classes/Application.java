package shared.classes;

import java.io.Serializable;

//신청
public class Application implements Comparable<Application>, Serializable
{
	//키
	private String studentId;		//학생 학번(외래키)
	private String dormitoryName;	//생활관명(외래키)
	private char gender;			//성별(외래키)
	private int semesterCode;		//학기코드(외래키)
	private int choice;				//지망
	
	//키가 아닌 컬럼
	private int mealType;			//몇일식				0이면 안함, 5이면 5일식, 7이면 7일식
	private boolean isPaid;			//납부여부
	private boolean isPassed;		//합격여부
	private boolean isLastPassed;	//결과, 최종합격여부
	private boolean isSnore;		//코골이여부
	
	//로컬 변수
	private double score;			//평균점수 + 가산점
	
	//클라이언트에서 사용함.
	public Application()
	{
		
	}
	
	public Application(String studentId, String dormitoryName, String gender, int semesterCode, int choice)	//합격 알고리즘에 필요한 생성자임 터치 ㄴㄴ해
	{
		this.studentId = studentId;
		this.dormitoryName = dormitoryName;
		this.gender = gender.toCharArray()[0];
		this.semesterCode = semesterCode;
		this.choice = choice;
	}
	
	public Application(int choice, String dormitoryName, int mealType)
	{
		this.choice = choice;
		this.dormitoryName = dormitoryName;
		this.mealType = mealType;
	}
	
	public String getStudentId()
	{
		return studentId;
	}
	public void setStudentId(String studentId)
	{
		this.studentId = studentId;
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
	
	public boolean isSnore()
	{
		return isSnore;
	}
	public void setSnore(boolean isSnore)
	{
		this.isSnore = isSnore;
	}
	
	public double getScore()
	{
		return score;
	}
	public void setScore(double score)
	{
		this.score = score;
	}
	
	@Override
	public int compareTo(Application a) {
		if(this.score > a.getScore())
			return -1;
		else
			return 1;
	}
}
