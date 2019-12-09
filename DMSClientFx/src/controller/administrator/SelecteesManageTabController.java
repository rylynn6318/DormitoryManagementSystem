package controller.administrator;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import application.IOHandler;
import application.Responser;
import controller.InnerPageController;
import enums.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import models.*;
import tableViewModel.ApplicationViewModel;

//입사 선발자 조회 및 관리
public class SelecteesManageTabController extends InnerPageController 
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
    private TableColumn<ApplicationViewModel, String> check_application_column_gender;

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
    private TextField delete_gender_textfield;

    @FXML
    private TextField delete_semester_textfield;
    
    
    	
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
    	Tuple<Bool, String> resultTuple = Responser.admin_selecteesManagePage_onSelection();
    	
    	//서버랑 통신이 됬는가?
        if(resultTuple == null)
        {
        	IOHandler.getInstance().showAlert("서버에 연결할 수 없습니다.");
        	return;
        }
        
        if(resultTuple != null)
        {
        	//이건 성공/실패 여부에 따른 동작이 똑같음.
        	IOHandler.getInstance().showAlert(resultTuple.obj2);
        }
    }
    
    //-----------------------------------------------------------------

	//서버에서 신청테이블->이번학기->객체 배열 쫙 긁어와서 tableview에 보여줌
    private void checkApplications()
    {
    	//서버에 쿼리 요청.
    	ArrayList<Application> resultList = Responser.admin_selecteesManagePage_onCheck();
    	
    	//서버랑 통신이 됬는가?
        if(resultList == null)
        {
        	IOHandler.getInstance().showAlert("서버에 연결할 수 없습니다.");
        	return;
        }
        
        if(resultList != null)
        {
        	ObservableList<ApplicationViewModel> applicationViewModels = FXCollections.observableArrayList();
        	
        	for(Application app : resultList)
        	{
        		applicationViewModels.add(applicationToViewModel(app));
        	}
        	
        	setApplicationTableView(applicationViewModels);
        }
    	
    }
    
    private void setApplicationTableView(ObservableList<ApplicationViewModel> applicationViewModels)
    {
    	//서버에서 받아온거 표시하게 만듬.
    	check_application_column_id.setCellValueFactory(cellData -> cellData.getValue().studentIdProperty());
    	check_application_column_dormName.setCellValueFactory(cellData -> cellData.getValue().dormNameProperty());
    	check_application_column_gender.setCellValueFactory(cellData -> cellData.getValue().genderProperty());
    	check_application_column_semester.setCellValueFactory(cellData -> cellData.getValue().semesterProperty());
    	check_application_column_choice.setCellValueFactory(cellData -> cellData.getValue().choiceProperty());
    	check_application_column_mealType.setCellValueFactory(cellData -> cellData.getValue().mealTypeProperty());
    	check_application_column_isPaid.setCellValueFactory(cellData -> cellData.getValue().isPaidProperty());
    	check_application_column_isPassed.setCellValueFactory(cellData -> cellData.getValue().isPassedProperty());
    	check_application_column_isLastPassed.setCellValueFactory(cellData -> cellData.getValue().isLastPassedProperty());
    	check_application_column_isSnore.setCellValueFactory(cellData -> cellData.getValue().isSnoreProperty());
    	check_application_tableview.setItems(applicationViewModels);
    }
    
    //-----------------------------------------------------------------
    
    private void deleteSelectee()
    {
    	String id = delete_id_textfield.getText();
    	String dormName = delete_dormName_textfield.getText();
    	String gender = delete_gender_textfield.getText();
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
    	else if(gender == null || gender.isEmpty())
    	{
    		//성별 비어있음
    		IOHandler.getInstance().showAlert("성별이 비어있습니다.");
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
    	
    	//서버에 쿼리 요청.
    	Application data = new Application(id, dormName, Integer.parseInt(semester), Integer.parseInt(choice));
    	data.setGender(gender.charAt(0));
    	Tuple<Bool, String> resultTuple = Responser.admin_selecteesManagePage_onDelete(data);
    	
    	//서버랑 통신이 됬는가?
        if(resultTuple == null)
        {
        	IOHandler.getInstance().showAlert("서버에 연결할 수 없습니다.");
        	return;
        }
        
        if(resultTuple != null)
        {
        	if(resultTuple.obj1 == Bool.TRUE)
        	{
        		clearDeleteInfo();
        	}
        	IOHandler.getInstance().showAlert(resultTuple.obj2);
        }
    }
    
    private void clearDeleteInfo()
    {
    	//선택한 항목들 클리어
		delete_id_textfield.setText(null);
		delete_dormName_textfield.setText(null);
		delete_semester_textfield.setText(null);
		delete_choice_textfield.setText(null);
    }
}