package tableViewModel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import shared.classes.Application;

//학생 - 생활관 신청 조회 - 생활관 입사지원 내역 테이블뷰 모델
public class StudentApplicationViewModel extends Application
{
	private StringProperty choiceStr;
	private StringProperty dormNameStr;
	private StringProperty mealTypeStr;
	
	public StudentApplicationViewModel(String studentId, String dormitoryName, String gender, int semesterCode, int choice)
	{
		super(studentId, dormitoryName, gender, semesterCode, choice);
	}
	
	//클라이언트 학생모드에서 '생활관 신청 조회' 페이지 테이블에서 사용함.
	public StudentApplicationViewModel(int choice, String dormitoryName, int mealType)
	{
		super(choice, dormitoryName, mealType);
		
		choiceStr = convertChoice(choice);
		dormNameStr = convertDormName(dormitoryName);
		mealTypeStr = convertMealType(mealType);
	}
	
	public StringProperty choiceProperty()
	{
		return choiceStr;
	}
	
	public StringProperty dormNameProperty()
	{
		return dormNameStr;
	}
	
	public StringProperty mealTypeProperty()
	{
		return mealTypeStr;
	}
	
	private StringProperty convertChoice(int choice)
	{
		return new SimpleStringProperty(Integer.toString(choice));
	}
	
	private StringProperty convertDormName(String dormitoryName)
	{
		return new SimpleStringProperty(dormitoryName);
	}
	
	private StringProperty convertMealType(int mealType)
	{
		switch(mealType)
		{
		case 0:
			return new SimpleStringProperty("식사 안함");
		case 5:
			return new SimpleStringProperty("5일식");
		case 7:
			return new SimpleStringProperty("7일식");
		default:
			return new SimpleStringProperty("알 수 없음");
		}
	}
}