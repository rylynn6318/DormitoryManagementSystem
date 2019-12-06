package tableViewModel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import shared.classes.Application;


//관리자 - 입사 선발자 조회 및 관리, 납부 여부 조회 및 관리 테이블뷰 모델
public class ApplicationViewModel extends Application
{
	private StringProperty studentIdStr;
	private StringProperty dormNameStr;
	private StringProperty semesterStr;
	private StringProperty choiceStr;
	private StringProperty mealTypeStr;
	private StringProperty isPaidStr;
	private StringProperty isPassedStr;
	private StringProperty isLastPassedStr;
	private StringProperty isSnoreStr;
	
	public ApplicationViewModel(String studentId, String dormNameStr, int semester, int choice, int mealType, 
			boolean isPaid, boolean isPassed, boolean isLastPassed, boolean isSnore)
	{
		//생성자로 묶을수있을거같은데, 일단 보류
		super.setStudentId(studentId);
		super.setDormitoryName(dormNameStr);
		super.setSemesterCode(semester);
		super.setChoice(choice);
		super.setMealType(mealType);
		super.setPaid(isPaid);
		super.setPassed(isPassed);
		super.setLastPassed(isLastPassed);
		super.setSnore(isSnore);
		
		this.studentIdStr = new SimpleStringProperty(studentId);
		this.dormNameStr = new SimpleStringProperty(dormNameStr);
		this.semesterStr = new SimpleStringProperty(Integer.toString(semester));
		this.choiceStr = new SimpleStringProperty(Integer.toString(choice));
		this.mealTypeStr = new SimpleStringProperty(Integer.toString(mealType));
		this.isPaidStr = new SimpleStringProperty(isPaid ? "T" : "F");
		this.isPassedStr = new SimpleStringProperty(isPassed ? "T" : "F");
		this.isLastPassedStr = new SimpleStringProperty(isLastPassed ? "T" : "F");
		this.isSnoreStr = new SimpleStringProperty(isSnore ? "T" : "F");
	}
	
	public StringProperty studentIdProperty()
	{
		return studentIdStr;
	}
	
	public StringProperty dormNameProperty()
	{
		return dormNameStr;
	}
	
	public StringProperty semesterProperty()
	{
		return semesterStr;
	}
	
	public StringProperty choiceProperty()
	{
		return choiceStr;
	}
	
	public StringProperty mealTypeProperty()
	{
		return mealTypeStr;
	}
	
	public StringProperty isPaidProperty()
	{
		return isPaidStr;
	}
	
	public StringProperty isPassedProperty()
	{
		return isPassedStr;
	}
	
	public StringProperty isLastPassedProperty()
	{
		return isLastPassedStr;
	}
	
	public StringProperty isSnoreProperty()
	{
		return isSnoreStr;
	}
}
