package tableViewModel;

import java.util.Date;

import enums.Bool;
import enums.Code1;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import models.Document;

public class DocumentViewModel
{
	public final Document document;

	private StringProperty studentIdStr;
	private StringProperty documentTypeStr;
	private StringProperty submissionDateStr;
	private StringProperty diagnosisDateStr;
	private StringProperty documentStoragePathStr;
	private StringProperty isValidStr;
	
	public DocumentViewModel(String studentId, Code1.FileType documentType, Date submissionDate, Date diagnosisDate, String documentStoragePath, Bool isValid)
	{
		document = new Document(studentId, documentType, submissionDate, diagnosisDate, documentStoragePath, isValid);

		this.studentIdStr = new SimpleStringProperty(studentId);
		this.documentTypeStr = new SimpleStringProperty(convertDocumentType(documentType));
		this.submissionDateStr = new SimpleStringProperty(submissionDate.toString());
		this.diagnosisDateStr = new SimpleStringProperty(diagnosisDate.toString());
		this.documentStoragePathStr = new SimpleStringProperty(documentStoragePath);
		this.isValidStr = new SimpleStringProperty(isValid == Bool.TRUE ? "T" : "F");
	}
	
	//파일 유형의 인덱스가 1, 2, 3이면 switch 고치세요.
	private String convertDocumentType(Code1.FileType type)
	{
		switch(type)
		{
		case MEDICAL_REPORT:
			return "결핵진단서";
		case OATH:
			return "서약서";
		case CSV:
			return "은행 파일";
		default:
			return "알수없음";
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
