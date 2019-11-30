package shared.classes;
//학생
public class Student
{
	//키
	private String studentId;					//학번
	
	//키가 아닌 컬럼
	private String name;						//성명
	private char gender;						//성별
	private String departmentName;				//학과명
	private int year;							//학년
	private String rrn;							//주민등록번호(resident registration number)
	private String contact;						//학생전화번호
	private String parentZipCode;				//보호자 우편번호
	private String parentAddress;				//보호자주소

	//로컬 변수
	private double avrScore;					//직전 2학기 평균 학점
	
	public void setAvrScore(Score[] myScore) // (등급 환산 점수 * 이수 단위) 모두 합한 것 / 총 이수 단위 = 평균 학점
	{
		double sumOfTakenCredit = 0;
		double sumOfTakenGrade = 0;
		String tempGrade;
		for(int i = 0; i < myScore.length; i++)
		{
			sumOfTakenCredit += myScore[i].getCredit();
			
			tempGrade = String.valueOf(myScore[i].getGrade());	//Score의 grade가 char형 배열이라 switch에 넣기 위해 string으로 변환함
			switch(tempGrade) {
			case "A+":
				sumOfTakenGrade += 4.5 * myScore[i].getCredit();
				break;
			case "A":
				sumOfTakenGrade += 4 * myScore[i].getCredit();
				break;
			case "B+":
				sumOfTakenGrade += 3.5 * myScore[i].getCredit();
				break;
			case "B":
				sumOfTakenGrade += 3 * myScore[i].getCredit();
				break;
			case "C+":
				sumOfTakenGrade += 2.5 * myScore[i].getCredit();
				break;
			case "C":
				sumOfTakenGrade += 2 * myScore[i].getCredit();
				break;
			case "D+":
				sumOfTakenGrade += 1.5 * myScore[i].getCredit();
				break;
			case "D":
				sumOfTakenGrade += 1 * myScore[i].getCredit();
				break;
			case "F":
				break;
			}
		}
		
		avrScore = sumOfTakenGrade / sumOfTakenCredit;
	}
	
	public double getAvrScore()
	{
		return Math.round(avrScore * 100) / 100;	// 소수점 셋째 자리수에서 반올림 ex)4.1666 -> 416.6 -> 417 -> 4.17
	}
	
	public String getStudentId()
	{
		return studentId;
	}
	public void setStudentId(String studentId)
	{
		this.studentId = studentId;
	}
	
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	
	public char getGender()
	{
		return gender;
	}
	public void setGender(char gender)
	{
		this.gender = gender;
	}
	
	public String getDepartmentName()
	{
		return departmentName;
	}
	public void setDepartmentName(String departmentName)
	{
		this.departmentName = departmentName;
	}
	
	public int getYear()
	{
		return year;
	}
	public void setYear(int year)
	{
		this.year = year;
	}
	
	public String getRrn()
	{
		return rrn;
	}
	public void setRrn(String rrn)
	{
		this.rrn = rrn;
	}
	
	public String getContact()
	{
		return contact;
	}
	public void setContact(String contact)
	{
		this.contact = contact;
	}
	
	public String getParentZipCode()
	{
		return parentZipCode;
	}
	public void setParentZipCode(String parentZipCode)
	{
		this.parentZipCode = parentZipCode;
	}
	
	public String getParentAddress()
	{
		return parentAddress;
	}
	public void setParentAddress(String parentAddress)
	{
		this.parentAddress = parentAddress;
	}

}
