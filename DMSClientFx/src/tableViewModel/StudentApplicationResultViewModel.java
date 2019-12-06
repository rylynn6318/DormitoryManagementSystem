package tableViewModel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

//학생 - 생활관 신청 조회 - 생활관 선발 결과 테이블뷰 모델
public class StudentApplicationResultViewModel extends StudentApplicationViewModel
{
	private StringProperty isPassedStr;
	private StringProperty isPaidStr;
	
	public StudentApplicationResultViewModel(int choice, String dormitoryName, int mealType)
	{
		super(choice, dormitoryName, mealType);
	}
	
	public StudentApplicationResultViewModel(int choice, String dormitoryName, int mealType, boolean isPassed, boolean isPaid)
	{
		super(choice, dormitoryName, mealType);
		isPassedStr = isPassed ? new SimpleStringProperty("합격") : new SimpleStringProperty("불합격");
		isPaidStr = isPaid ? new SimpleStringProperty("납부") : new SimpleStringProperty("미납");
	}
	
	public StringProperty isPassedProperty()
	{
		return isPassedStr;
	}
	
	public StringProperty isPaidProperty()
	{
		return isPaidStr;
	}
	
}