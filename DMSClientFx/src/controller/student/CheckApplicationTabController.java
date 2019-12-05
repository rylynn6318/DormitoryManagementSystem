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
    private TableView<SelectionResultViewModel> selection_result_tableview;

    @FXML
    private TableColumn<SelectionResultViewModel, String> selection_result_column_choice;

    @FXML
    private TableColumn<SelectionResultViewModel, String> selection_result_column_dormName;

    @FXML
    private TableColumn<SelectionResultViewModel, String> selection_result_column_mealType;
    
    @FXML
    private TableColumn<SelectionResultViewModel, String> selection_result_column_isPassed;

    @FXML
    private TableColumn<SelectionResultViewModel, String> selection_result_column_isPaid;

    @FXML
    private TextArea info_textarea;
    
    ObservableList<ApplicationViewModel> appHistory;
    ObservableList<SelectionResultViewModel> selections;
    
	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		System.out.println("생활관 신청 조회 새로고침됨");

		//네트워킹
		info_textarea.setText("서버에서 받아온 안내사항입니다.");
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
    	
    	//아래에서 서버와 통신하여 appHistory 배열과, selections 배열을 객체로 채웠다..
		//서버에서 Application 객체를 가져오면, ApplicationViewModel로 형변환해서 써야할듯. 
		appHistory = FXCollections.observableArrayList(
				new ApplicationViewModel(1, "오름관2동", 5),
				new ApplicationViewModel(2, "푸름관1동", 7),
				new ApplicationViewModel(3, "푸름관4동", 0)
				);
		
		//이건 생활관 선발 결과 객체 배열
		//지망, 생활관명, 식비구분, 합격여부, 납부여부를 받아와야한다.
		selections = FXCollections.observableArrayList(
				new SelectionResultViewModel(1, "오름관2동", 5, true, true),
				new SelectionResultViewModel(2, "푸름관1동", 7, true, false),
				new SelectionResultViewModel(3, "푸름관4동", 0, true, false)
				);
    	
    	refreshApplicationTable();
    	refreshApplicationResultTable();
    }
    
    private void refreshApplicationTable()
    {
    	application_history_column_choice.setCellValueFactory(cellData -> cellData.getValue().choiceProperty());
    	application_history_column_dormName.setCellValueFactory(cellData -> cellData.getValue().dormNameProperty());
    	application_history_column_mealType.setCellValueFactory(cellData -> cellData.getValue().mealTypeProperty());
    	application_history_tableview.setItems(appHistory);
    }
    
    private void refreshApplicationResultTable()
    {
    	selection_result_column_choice.setCellValueFactory(cellData -> cellData.getValue().choiceProperty());
    	selection_result_column_dormName.setCellValueFactory(cellData -> cellData.getValue().dormNameProperty());
    	selection_result_column_mealType.setCellValueFactory(cellData -> cellData.getValue().mealTypeProperty());
    	selection_result_column_isPassed.setCellValueFactory(cellData -> cellData.getValue().isPassedProperty());
    	selection_result_column_isPaid.setCellValueFactory(cellData -> cellData.getValue().isPaidProperty());
    	selection_result_tableview.setItems(selections);
    }
}

//---------------------테이블뷰 모델---------------------

class ApplicationViewModel extends Application
{
	private StringProperty choiceStr;
	private StringProperty dormNameStr;
	private StringProperty mealTypeStr;
	
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

class SelectionResultViewModel extends ApplicationViewModel
{
	private StringProperty isPassedStr;
	private StringProperty isPaidStr;
	
	public SelectionResultViewModel(int choice, String dormitoryName, int mealType)
	{
		super(choice, dormitoryName, mealType);
	}
	
	public SelectionResultViewModel(int choice, String dormitoryName, int mealType, boolean isPassed, boolean isPaid)
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