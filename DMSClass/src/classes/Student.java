package classes;

import java.io.Serializable;

import enums.Gender;

//학생
public final class Student implements Serializable {
	// 키
	public final String studentId; // 학번

	// 키가 아닌 컬럼
	public final String name; // 성명
	public final Gender gender; // 성별
	public final String departmentName; // 학과명
	public final int year; // 학년
	public final String rrn; // 주민등록번호(resident registration number)
	public final String contact; // 학생전화번호
	public final String parentZipCode; // 보호자 우편번호
	public final String parentAddress; // 보호자주소

}
