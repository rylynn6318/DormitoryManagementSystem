package tableViewModel;

import enums.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import models.Dormitory;

//관리자 - 생활관 조회 및 관리 테이블뷰 모델
public class DormitoryViewModel
{
	public final Dormitory dormitory;

	private StringProperty dormNameStr;
	private StringProperty genderStr;
	private StringProperty semesterStr;
	private StringProperty capacityStr;
	private StringProperty mealDutyStr;
	private StringProperty meal5Str;
	private StringProperty meal7Str;
	private StringProperty boardingFee;
	
	public DormitoryViewModel(String dormitoryName, Gender gender, int semesterCode, int capacity, Bool isMealDuty, int mealCost5, int mealCost7, int boardingFees)
	{
		dormitory = new Dormitory(dormitoryName, gender, semesterCode, capacity, isMealDuty, mealCost5, mealCost7, boardingFees);
		
		this.dormNameStr = new SimpleStringProperty(dormitoryName);
		this.genderStr = new SimpleStringProperty(gender == Gender.Male ? "M" : "F");
		this.semesterStr = new SimpleStringProperty(Integer.toString(semesterCode));
		this.capacityStr = new SimpleStringProperty(Integer.toString(capacity));
		this.mealDutyStr = new SimpleStringProperty(isMealDuty.bool ? "필수" : "선택");
		this.meal5Str = new SimpleStringProperty(Integer.toString(mealCost5));
		this.meal7Str = new SimpleStringProperty(Integer.toString(mealCost7));
		this.boardingFee = new SimpleStringProperty(Integer.toString(boardingFees));
	}
	
	public StringProperty dormNameProperty()
	{
		return dormNameStr;
	}
	
	public StringProperty genderProperty()
	{
		return genderStr;
	}
	
	public StringProperty semesterProperty()
	{
		return semesterStr;
	}
	
	public StringProperty capacityProperty()
	{
		return capacityStr;
	}
	
	public StringProperty mealDutyProperty()
	{
		return mealDutyStr;
	}
	
	public StringProperty meal5Property()
	{
		return meal5Str;
	}
	
	public StringProperty meal7Property()
	{
		return meal7Str;
	}
	
	public StringProperty boardingProperty()
	{
		return boardingFee;
	}
}
