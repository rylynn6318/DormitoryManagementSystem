package shared.classes;

import java.io.Serializable;

//학생
public class Student implements Serializable
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
