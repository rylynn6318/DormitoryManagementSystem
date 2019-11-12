package classes;
//성적
public class Score
{
	private String subjectName;		//교과목명
	private int semesterCode;		//학기 코드
	private int credit;				//학점 (이수 단위)
	private char grade;				//성적 등급(A+, A, B+, B, ...)
	private String studentId;		//학생의 학번(외래키)
	
	public String getSubjectName()
	{
		return subjectName;
	}
	public void setSubjectName(String subjectName)
	{
		this.subjectName = subjectName;
	}
	
	public int getSemesterCode()
	{
		return semesterCode;
	}
	public void setSemesterCode(int semesterCode)
	{
		this.semesterCode = semesterCode;
	}
	
	public int getCredit()
	{
		return credit;
	}
	public void setCredit(int credit)
	{
		this.credit = credit;
	}
	
	public char getGrade()
	{
		return grade;
	}
	public void setGrade(char grade)
	{
		this.grade = grade;
	}
	
	public String getStudentId()
	{
		return studentId;
	}
	public void setStudentId(String studentId)
	{
		this.studentId = studentId;
	}
}
