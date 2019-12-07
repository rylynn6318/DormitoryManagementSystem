package models;

import java.io.Serializable;

import enums.Bool;
import enums.Gender;

//생활관 정보
public final class Dormitory implements Serializable {
	// 키
	public final String dormitoryName; // 생활관명
	public final Gender gender; // 성별
	public final int semesterCode; // 학기코드

	// 키가 아닌 컬럼
	public final int capacity; // 수용인원
	public final Bool isMealDuty; // 식사의무 식사가 의무이면 true, 의무가 아니면 false
	public final int mealCost5; // 5일치 식비, 번역 못해서 임시로 mealCost5로 해둠.
	public final int mealCost7; // 7일치 식비, 번역 못해서 임시로 mealCost7로 해둠.
	public final int boardingFees; // 기숙사비

	public Dormitory(String dormitoryName, Gender gender, int semesterCode, int capacity, Bool isMealDuty,
		int mealCost5, int mealCost7, int boardingFees) {
		this.dormitoryName = dormitoryName;
		this.gender = gender;
		this.semesterCode = semesterCode;
		this.capacity = capacity;
		this.isMealDuty = isMealDuty;
		this.mealCost5 = mealCost5;
		this.mealCost7 = mealCost7;
		this.boardingFees = boardingFees;
	}
}
