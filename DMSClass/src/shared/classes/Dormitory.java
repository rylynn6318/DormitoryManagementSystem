package shared.classes;
//생활관 정보
public class Dormitory
{
	//키
	private String dormitoryName;			//생활관명
	private char gender;					//성별
	private int semesterCode;				//학기코드
	
	//키가 아닌 컬럼
	private int capacity;					//수용인원
	private boolean isMealDuty;				//식사의무			식사가 의무이면 true, 의무가 아니면 false
	private int mealCost5;					//5일치 식비, 번역 못해서 임시로 mealCost5로 해둠.
	private int mealCost7;					//7일치 식비, 번역 못해서 임시로 mealCost7로 해둠.
	private String boardingFees;			//기숙사비
	
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
	
	public int getCapacity()
	{
		return capacity;
	}
	public void setCapacity(int capacity)
	{
		this.capacity = capacity;
	}
	
	public boolean isMealDuty()
	{
		return isMealDuty;
	}
	public void setMealDuty(boolean isMealDuty)
	{
		this.isMealDuty = isMealDuty;
	}
	
	public int getMealCost5()
	{
		return mealCost5;
	}
	public void setMealCost5(int mealCost5)
	{
		this.mealCost5 = mealCost5;
	}
	
	public int getMealCost7()
	{
		return mealCost7;
	}
	public void setMealCost7(int mealCost7)
	{
		this.mealCost7 = mealCost7;
	}
	
	public String getBoardingFees()
	{
		return boardingFees;
	}
	public void setBoardingFees(String boardingFees)
	{
		this.boardingFees = boardingFees;
	}
}
