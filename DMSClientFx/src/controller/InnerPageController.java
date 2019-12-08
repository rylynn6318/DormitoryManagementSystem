package controller;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

import enums.Code1;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import models.Application;
import tableViewModel.ApplicationViewModel;

//Login, Main을 제외한 페이지가 공통적으로 가지는 메소드를 가짐.
public class InnerPageController implements Initializable 
{
	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		
	}
	
	public void close()
    {
    	TabPane mainTabPane = MainPageController._MainTabPane;
    	Tab currentTab = mainTabPane.getSelectionModel().getSelectedItem();
    	mainTabPane.getTabs().remove(currentTab);
    }

	
	public Code1.FileType stringToFileType(String str)
    {
    	switch(str)
    	{
    	case "결핵진단서":
    		return Code1.FileType.MEDICAL_REPORT;
    	case "서약서":
    		return Code1.FileType.OATH;
    	default:
    		System.out.println("알 수 없는 파일 유형입니다!");
    		return null;
    	}
    }
    
	public String fileTypeToString(Code1.FileType fileType)
    {
    	switch(fileType)
		{
		case MEDICAL_REPORT:
			return "결핵진단서";
		case OATH:
			return "서약서";
		default:
			System.out.println("알 수 없는 파일 유형입니다!");
			return null;
		}
    }
	
	public Date localDateToDate(LocalDate local)
    {
    	Instant instant = Instant.from(local.atStartOfDay(ZoneId.systemDefault()));
    	Date date = Date.from(instant);
    	return date;
    }
	
	public String mealTypeIntToStr(int mealType)
	{
		switch(mealType)
    	{
    	case 0:
    		return "식사안함";
    	case 5:
    		return "5일식";
    	case 7:
    		return "7일식";
		default:
    		return null;
    	}
	}
	
	public int mealTypeStrToInt(String mealType)
	{
		switch(mealType)
    	{
    	case "식사안함":
    		return 0;
    	case "5일식":
    		return 5;
    	case "7일식":
    		return 7;
		default:
    		return -1;
    	}
	}
	
	public ApplicationViewModel applicationToViewModel(Application application)
    {
    	return new ApplicationViewModel(application.getStudentId(), application.getDormitoryName(), application.getSemesterCode(), 
    			application.getChoice(), application.getMealType(), application.isPaid(), application.isPassed(), 
    			application.isLastPassed(), application.isSnore());
    }
}
