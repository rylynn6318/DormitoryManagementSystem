package models;

import java.io.Serializable;

import enums.Grade;

//성적
public final class Score implements Serializable {
	// 키
	public final String studentId; // 학생의 학번(외래키)
	public final String subjectName; // 교과목명
	public final int semesterCode; // 학기 코드

	// 키가 아닌 컬럼
	public final int credit; // 학점 (이수 단위)
	public final Grade grade; // 성적 등급(A+, A, B+, B, ...)

	public Score(String studentId, String subjectName, int semesterCode, int credit, Grade grade) {
		this.studentId = studentId;
		this.subjectName = subjectName;
		this.semesterCode = semesterCode;
		this.credit = credit;
		this.grade = grade;
	}
}
