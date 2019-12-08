package controller;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

import enums.Code1;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import models.Application;
import models.Document;
import models.PlacementHistory;
import models.Schedule;
import models.ScheduleCode;
import tableViewModel.ApplicationViewModel;
import tableViewModel.DocumentViewModel;
import tableViewModel.PlacementHistoryViewModel;
import tableViewModel.ScheduleViewModel;

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
	
    //-----------------------------------------------------------------
	
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
	
    //-----------------------------------------------------------------
	
	//인자로 받은 스케쥴의 유형 이름을 얻어내는 메소드(code->문자)
    //서버에서 받은 유형 코드를 유형 문자로 바꾸어 tableView에 보여주기 위함임.
    public String codeToTodoStr(Schedule schedule, ArrayList<ScheduleCode> scList)
    {
    	//스케쥴 할일 목록에서, code와 이 스케쥴의 code가 같으면, 해당 스케쥴 할일 목록의 이름을 가져온다.
    	String toDoString = "알 수 없음";
    	for(ScheduleCode sc : scList)
    	{
    		if(sc.code == schedule.code)
    		{
    			toDoString = sc.name;
    			break;
    		}
    	}
    	return toDoString;
    }
    
    //인자로 받은 스케쥴 유형 이름을 코드로 바꿔주는 메소드(문자->code)
    //사용자가 입력한 유형(String)을 Code로 바꾸어 서버로 보내기 위함임.
    public int todoStrToCode(String todoStr, ArrayList<ScheduleCode> scList)
    {
    	//사용자가 선택한 콤보박스가 스케쥴 할일 목록의 이름과 같으면 해당 코드 반환
    	int code = -1;
    	for(ScheduleCode sc : scList)
    	{
    		if(sc.name.equals(todoStr))
    		{
    			code = sc.code;
    			break;
    		}
    	}
    	return code;
    }
    
    public void setComboboxByFileType(ComboBox<String> combobox, ArrayList<Code1.FileType> fileTypeList)
    {
    	for(Code1.FileType fileType : fileTypeList)
    	{
    		combobox.getItems().add(fileTypeToString(fileType));
    	}
    }
    
    //-----------------------------------------------------------------
	
	public ApplicationViewModel applicationToViewModel(Application application)
    {
    	return new ApplicationViewModel(application.getStudentId(), application.getDormitoryName(), application.getSemesterCode(), 
    			application.getChoice(), application.getMealType(), application.isPaid(), application.isPassed(), 
    			application.isLastPassed(), application.isSnore());
    }
	
	public DocumentViewModel documentToViewModel(Document document)
    {
    	return new DocumentViewModel(document.studentId, document.documentType, document.submissionDate, document.diagnosisDate,
    			document.documentStoragePath, document.isValid);
    }
	
	//스케쥴 객체를 뷰모델로 바꾸는 메소드
    public ScheduleViewModel scheduleToViewModel(Schedule schedule, ArrayList<ScheduleCode> scheduleCodeList)
    {
    	return new ScheduleViewModel(schedule.scheduleId, codeToTodoStr(schedule, scheduleCodeList), 
    			schedule.startDate, schedule.endDate, schedule.description);
    }
    
    public PlacementHistoryViewModel placementHistoryToViewModel(PlacementHistory history)
    {
    	return new PlacementHistoryViewModel(history.studentId, history.roomId, history.semester, 
    			history.dormitoryName, history.seat, history.checkout);
    }
    
    
}
