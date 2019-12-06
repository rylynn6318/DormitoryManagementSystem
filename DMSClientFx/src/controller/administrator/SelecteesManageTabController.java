package controller.administrator;

import java.net.URL;
import java.util.ResourceBundle;

import application.IOHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import tableViewModel.ApplicationViewModel;

//입사 선발자 조회 및 관리
public class SelecteesManageTabController implements Initializable 
{
	@FXML
    private Button selection_button;

    @FXML
    private Button check_application_button;

    @FXML
    private TableView<ApplicationViewModel> check_application_tableview;

    @FXML
    private TableColumn<ApplicationViewModel, String> check_application_column_id;

    @FXML
    private TableColumn<ApplicationViewModel, String> check_application_column_dormName;

    @FXML
    private TableColumn<ApplicationViewModel, String> check_application_column_semester;

    @FXML
    private TableColumn<ApplicationViewModel, String> check_application_column_choice;

    @FXML
    private TableColumn<ApplicationViewModel, String> check_application_column_mealType;

    @FXML
    private TableColumn<ApplicationViewModel, String> check_application_column_isPaid;

    @FXML
    private TableColumn<ApplicationViewModel, String> check_application_column_isPassed;

    @FXML
    private TableColumn<ApplicationViewModel, String> check_application_column_isLastPassed;

    @FXML
    private TableColumn<ApplicationViewModel, String> check_application_column_isSnore;

    @FXML
    private Button delete_button;

    @FXML
    private TextField delete_choice_textfield;

    @FXML
    private TextField delete_id_textfield;

    @FXML
    private TextField delete_dormName_textfield;

    @FXML
    private TextField delete_semester_textfield;
    
    ObservableList<ApplicationViewModel> applicationList;
    	
	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		System.out.println("입사 선발자 조회 및 관리 새로고침됨");
	}
	
	//---------------------이벤트---------------------
	
	@FXML
    void on_selection_button_actioned(ActionEvent event) 
    {
    	System.out.println("입사 선발자 조회 및 관리 : 선발 클릭됨");
    	selection();
    }
	
	@FXML
    void on_check_application_button_actioned(ActionEvent event) 
	{
		System.out.println("입사 선발자 조회 및 관리 : 조회 클릭됨");
		checkApplications();
    }

    @FXML
    void on_delete_button_clicked(ActionEvent event) 
    {
    	System.out.println("입사 선발자 조회 및 관리 : 삭제 클릭됨");
    	deleteSelectee();
    }

	//---------------------로직---------------------

    private void selection()
    {
    	//서버에 입사자 선발 쿼리 요청
    }
    
    private void checkApplications()
    {
    	//서버에서 신청테이블->이번학기->객체 배열 쫙 긁어와서 tableview에 보여줌
    	
    	//서버랑 통신했다 치고 Application 객체 받아옴.
    	//물론 실제로 받아왔을때 받아온 Application 배열목록을 ApplicationViewModel로 변환하고 넣어야됨.
    	applicationList = FXCollections.observableArrayList(
    			new ApplicationViewModel("20161234", "오름관 2동", 201901, 1, 7, true, true, true, true),
    			new ApplicationViewModel("20161235", "오름관 3동", 201901, 2, 5, true, true, true, false),
    			new ApplicationViewModel("20161236", "푸름관 2동", 201901, 0, 7, false, true, false, true),
    			new ApplicationViewModel("20161237", "오름관 2동", 201901, 1, 5, false, true, true, true)
        		);
    	
    	//서버에서 받아온거 표시하게 만듬.
    	check_application_column_id.setCellValueFactory(cellData -> cellData.getValue().studentIdProperty());
    	check_application_column_dormName.setCellValueFactory(cellData -> cellData.getValue().dormNameProperty());
    	check_application_column_semester.setCellValueFactory(cellData -> cellData.getValue().semesterProperty());
    	check_application_column_choice.setCellValueFactory(cellData -> cellData.getValue().choiceProperty());
    	check_application_column_mealType.setCellValueFactory(cellData -> cellData.getValue().mealTypeProperty());
    	check_application_column_isPaid.setCellValueFactory(cellData -> cellData.getValue().isPaidProperty());
    	check_application_column_isPassed.setCellValueFactory(cellData -> cellData.getValue().isPassedProperty());
    	check_application_column_isLastPassed.setCellValueFactory(cellData -> cellData.getValue().isLastPassedProperty());
    	check_application_column_isSnore.setCellValueFactory(cellData -> cellData.getValue().isSnoreProperty());
    	check_application_tableview.setItems(applicationList);
    }
    
    private void deleteSelectee()
    {
    	String id = delete_id_textfield.getText();
    	String dormName = delete_dormName_textfield.getText();
    	String semester = delete_semester_textfield.getText();
    	String choice = delete_choice_textfield.getText();
    	
    	if(id == null || id.isEmpty())
    	{
    		//학번 비어있음
    		IOHandler.getInstance().showAlert("학번이 비어있습니다.");
    		return;
    	}
    	else if(dormName == null || dormName.isEmpty())
    	{
    		//생활관명 비어있음
    		IOHandler.getInstance().showAlert("생활관명이 비어있습니다.");
    		return;
    	}
    	else if(semester == null || semester.isEmpty())
    	{
    		//학기가 없음
    		IOHandler.getInstance().showAlert("학기가 비어있습니다.");
    		return;
    	}
    	else if(choice == null || choice.isEmpty())
    	{
    		//지망이 비어있음
    		IOHandler.getInstance().showAlert("지망이 비어있습니다.");
    		return;
    	}
    	
    	//서버에 Update 쿼리 요청 후 성공/실패여부 메시지로 알려주자.
		boolean isSucceed = true;
		if(isSucceed)
		{
			IOHandler.getInstance().showAlert("입사 선발자 삭제에 성공하였습니다.");
			
			//선택한 항목들 클리어
			delete_id_textfield.setText(null);
			delete_dormName_textfield.setText(null);
			delete_semester_textfield.setText(null);
			delete_choice_textfield.setText(null);
		}
		else
		{
			IOHandler.getInstance().showAlert("입사 선발자 삭제에 실패하였습니다.");
		}
    }
}