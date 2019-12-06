package tableViewModel;

import java.util.Date;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import shared.classes.Document;

public class DocumentViewModel extends Document
{
	private StringProperty studentIdStr;
	private StringProperty documentTypeStr;
	private StringProperty submissionDateStr;
	private StringProperty diagnosisDateStr;
	private StringProperty documentStoragePathStr;
	private StringProperty isValidStr;
	
	public DocumentViewModel(String studentId, int documentType, Date submissionDate, Date diagnosisDate, String documentStoragePath, boolean isValid)
	{
		super.setStudentId(studentId);
		super.setDocumentType(documentType);
		super.setSubmissionDate(submissionDate);
		super.setDiagnosisDate(diagnosisDate);
		super.setDocumentStoragePath(documentStoragePath);
		super.setIsValid(isValid);
		
		this.studentIdStr = new SimpleStringProperty(studentId);
		this.documentTypeStr = new SimpleStringProperty(convertDocumentType(documentType));
		this.submissionDateStr = new SimpleStringProperty(submissionDate.toString());
		this.diagnosisDateStr = new SimpleStringProperty(diagnosisDate.toString());
		this.documentStoragePathStr = new SimpleStringProperty(documentStoragePath);
		this.isValidStr = new SimpleStringProperty(isValid ? "T" : "F");
	}
	
	//파일 유형의 인덱스가 1, 2, 3이면 switch 고치세요.
	private String convertDocumentType(int type)
	{
		switch(type)
		{
		case 0:
			return "결핵진단서";
		case 1:
			return "서약서";
		case 2:
			return "은행 파일";
		default:
			return "알 수 없음";
		}
	}
	
	public StringProperty studentIdProperty()
	{
		return studentIdStr;
	}
	
	public StringProperty documentTypeProperty()
	{
		return documentTypeStr;
	}
	
	public StringProperty submissionDateProperty()
	{
		return submissionDateStr;
	}
	
	public StringProperty diagnosisDateProperty()
	{
		return diagnosisDateStr;
	}
	
	public StringProperty documentStoragePathProperty()
	{
		return documentStoragePathStr;
	}
	
	public StringProperty isValidProperty()
	{
		return isValidStr;
	}
}
