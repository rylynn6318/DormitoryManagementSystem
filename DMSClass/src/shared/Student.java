package shared;
//학생
public class Student
{
	private String studentId;					//학번
	private String name;						//성명
	private char gender;						//성별
	private String departmentId;				//학과코드
	private String departmentName;				//학과명
	private int year;							//학년
	private String rrn;							//주민등록번호(resident registration number)
	private String contact;						//학생전화번호
	private String parentZipCode;				//보호자 우편번호
	private String parentAddress;				//보호자주소
	private String medicalCertificatePath;		//(결핵)진단서 경로
	private boolean medicalCertificateCheck;	//(결핵)진단서 확인여부
	
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
	
	public String getDepartmentId()
	{
		return departmentId;
	}
	public void setDepartmentId(String departmentId)
	{
		this.departmentId = departmentId;
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
	
	public String getMedicalCertificatePath()
	{
		return medicalCertificatePath;
	}
	public void setMedicalCertificatePath(String medicalCertificatePath)
	{
		this.medicalCertificatePath = medicalCertificatePath;
	}
	
	public boolean isMedicalCertificateCheck()
	{
		return medicalCertificateCheck;
	}
	public void setMedicalCertificateCheck(boolean medicalCertificateCheck)
	{
		this.medicalCertificateCheck = medicalCertificateCheck;
	}
}
