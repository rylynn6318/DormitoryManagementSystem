package models;

import java.io.Serializable;
import java.util.Date;

import enums.Bool;
import enums.Code1;

//서류제출
public final class Document implements Serializable {
	// 키
	public final String studentId; // 학생 학번(외래키)
	public final Code1.FileType documentType; // 서류 유형(키)
	public final Date submissionDate; // 제출일(키)

	// 키가 아닌 컬럼
	public final Date diagnosisDate; // 진단일
	public final String documentStoragePath; // 서류저장경로
	public final Bool isValid; // 유효여부

	public Document(String studentId, Code1.FileType documentType, Date submissionDate, Date diagnosisDate,
			String documentStoragePath, Bool isValid) {
		this.studentId = studentId;
		this.documentType = documentType;
		this.submissionDate = submissionDate;
		this.diagnosisDate = diagnosisDate;
		this.documentStoragePath = documentStoragePath;
		this.isValid = isValid;
	}
	
	public Document(String studentId, Code1.FileType documentType, Date submissionDate) {
		this.studentId = studentId;
		this.documentType = documentType;
		this.submissionDate = submissionDate;
		this.diagnosisDate = null;
		this.documentStoragePath = null;
		this.isValid = null;
	}
}
