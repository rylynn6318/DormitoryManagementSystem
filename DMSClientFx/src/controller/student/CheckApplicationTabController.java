package controller.student;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import shared.classes.Application;

public class CheckApplicationTabController implements Initializable
{
	@FXML
    private Button check_button;

    @FXML
    private TableView<ApplicationViewModel> application_history_tableview;

    @FXML
    private TableColumn<ApplicationViewModel, String> application_history_column_choice;

    @FXML
    private TableColumn<ApplicationViewModel, String> application_history_column_dormName;

    @FXML
    private TableColumn<ApplicationViewModel, String> application_history_column_mealType;

    @FXML
    private TableView<?> selection_result_tableview;

    @FXML
    private TableColumn<?, ?> selection_result_column_choice;

    @FXML
    private TableColumn<?, ?> selection_result_column_dormName;

    @FXML
    private TableColumn<?, ?> selection_result_column_mealType;

    @FXML
    private TableColumn<?, ?> selection_result_column_result;

    @FXML
    private TextArea info_textarea;
    
    ObservableList<ApplicationViewModel> appHistory;
    
	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		System.out.println("생활관 신청 조회 새로고침됨");

		//네트워킹
		info_textarea.setText("서버에서 받아온 안내사항입니다.");
		
		//테스트용 객체 배열, 배열 받아왔다는 가정 하에 만듬.
		//서버에서 Application 객체를 가져오면, ApplicationViewModel로 형변환해서 써야할듯. 
		appHistory = FXCollections.observableArrayList(
				new ApplicationViewModel(1, "오름관2동", 5),
				new ApplicationViewModel(2, "푸름관1동", 7),
				new ApplicationViewModel(3, "푸름관4동", 0)
				);
				
	}
	
	//---------------------이벤트---------------------
	
    @FXML
    void on_check_button_actioned(ActionEvent event) 
    {
    	System.out.println("생활관 신청 조회 : 조회 클릭됨");
    	checkApplication();
    }
    
    //---------------------로직---------------------
    
    private void checkApplication()
    {
    	//여기는 뭐 검사할 필요없이 바로 서버로 요청날림.
    	//서버로 요청날리고 받아온 후 refreshApplicationTable 호출
    	
    	refreshApplicationTable();
    }
    
    private void refreshApplicationTable()
    {
    	application_history_column_choice.setCellValueFactory(cellData -> cellData.getValue().choiceProperty());
    	application_history_column_dormName.setCellValueFactory(cellData -> cellData.getValue().dormNameProperty());
    	application_history_column_mealType.setCellValueFactory(cellData -> cellData.getValue().mealTypeProperty());
    	application_history_tableview.setItems(appHistory);
    }
}

class ApplicationViewModel extends Application
{
	StringProperty choiceStr;
	StringProperty dormNameStr;
	StringProperty mealTypeStr;
	
	public ApplicationViewModel(String studentId, String dormitoryName, String gender, int semesterCode, int choice)
	{
		super(studentId, dormitoryName, gender, semesterCode, choice);
	}
	
	//클라이언트 학생모드에서 '생활관 신청 조회' 페이지 테이블에서 사용함.
	public ApplicationViewModel(int choice, String dormitoryName, int mealType)
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
