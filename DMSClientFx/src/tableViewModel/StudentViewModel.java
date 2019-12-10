package tableViewModel;

import enums.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import models.*;

//학생 - 생활관 신청 조회 - 생활관 입사지원 내역 테이블뷰 모델
public class StudentViewModel
{
	public final Student student;
	
	private StringProperty idStr;
	private StringProperty nameStr;
	private StringProperty genderStr;
	private StringProperty departmentStr;
	private StringProperty yearStr;
	private StringProperty rrnStr;
	private StringProperty contactStr;
	private StringProperty parentZipCodeStr;
	private StringProperty parentAddressStr;
	
	public StudentViewModel(String id, String name, Gender gender, String departmentName, int year, String rrn, String contact, String parentZipCode, String parentAddress)
	{
		this.student = new Student(id, name, gender, departmentName, year, rrn, contact, parentZipCode, parentAddress);
		
		this.idStr = new SimpleStringProperty(id);
		this.nameStr = new SimpleStringProperty(name);
		this.genderStr = new SimpleStringProperty(gender.gender);
		this.departmentStr = new SimpleStringProperty(departmentName);
		this.yearStr = new SimpleStringProperty(String.valueOf(year));
		this.rrnStr = new SimpleStringProperty(rrn);
		this.contactStr = new SimpleStringProperty(contact);
		this.parentZipCodeStr = new SimpleStringProperty(parentZipCode);
		this.parentAddressStr = new SimpleStringProperty(parentAddress);
	}
	
	public StringProperty idProperty()
	{
		return idStr;
	}
	
	public StringProperty nameProperty()
	{
		return nameStr;
	}
	
	public StringProperty genderProperty()
	{
		return genderStr;
	}
	
	public StringProperty departmentProperty()
	{
		return departmentStr;
	}
	
	public StringProperty yearProperty()
	{
		return yearStr;
	}
	
	public StringProperty rrnProperty()
	{
		return rrnStr;
	}
	
	public StringProperty contactProperty()
	{
		return contactStr;
	}
	
	public StringProperty parentZipCodeProperty()
	{
		return parentZipCodeStr;
	}
	
	public StringProperty parentAddressProperty()
	{
		return parentAddressStr;
	}
	
	
}