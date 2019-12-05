package shared.classes;

import java.io.Serializable;
import java.util.Date;

//서류제출
public class Document implements Serializable
{
	//키
	private String studentId;			//학생 학번(외래키)
	private int documentType;			//서류 유형(키)
	private Date submissionDate;		//제출일(키)
	
	//키가 아닌 컬럼
	private Date diagnosisDate;			//진단일
	private String documentStoragePath;	//서류저장경로
	private boolean isValid;			//유효여부
	
	public Document(String studentId, int documentType, Date submissionDate, Date diagnosisDate, String documentStoragePath, String isValid)
	{
		this.studentId = studentId;
		this.documentType = documentType;
		this.submissionDate = submissionDate;
		this.diagnosisDate = diagnosisDate;
		this.documentStoragePath = documentStoragePath;
		if(isValid.equals("T"))
			this.isValid = true;
		else
			this.isValid = false;
	}
	
	public String getStudentId()
	{
		return studentId;
	}
	public void setStudentId(String studentId)
	{
		this.studentId = studentId;
	}
	
	public int getDocumentType()
	{
		return documentType;
	}
	public void setDocumentType(int documentType)
	{
		this.documentType = documentType;
	}
	public Date getSubmissionDate()
	{
		return submissionDate;
	}
	public void setSubmissionDate(Date submissionDate)
	{
		this.submissionDate = submissionDate;
	}
	public Date getDiagnosisDate()
	{
		return diagnosisDate;
	}
	public void setDiagnosisDate(Date diagnosisDate)
	{
		this.diagnosisDate = diagnosisDate;
	}
	public String getDocumentStoragePath()
	{
		return documentStoragePath;
	}
	public void setDocumentStoragePath(String documentStaragePath)
	{
		this.documentStoragePath = documentStaragePath;
	}
	public boolean getIsValid()
	{
		return isValid;
	}
	public void setIsValid(boolean isValid)
	{
		this.isValid = isValid;
	}
	
}
