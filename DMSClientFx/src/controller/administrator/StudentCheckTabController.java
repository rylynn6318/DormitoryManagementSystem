package controller.administrator;

import java.io.Serializable;
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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import models.*;
import tableViewModel.ApplicationViewModel;
import tableViewModel.StudentViewModel;

public class StudentCheckTabController extends InnerPageController
{

    @FXML
    private Button check_button;

    @FXML
    private TableView<StudentViewModel> check_student_tableview;

    @FXML
    private TableColumn<StudentViewModel, String> check_student_column_studentId;

    @FXML
    private TableColumn<StudentViewModel, String> check_student_column_name;

    @FXML
    private TableColumn<StudentViewModel, String> check_student_column_gender;

    @FXML
    private TableColumn<StudentViewModel, String> check_student_column_departmentName;

    @FXML
    private TableColumn<StudentViewModel, String> check_student_column_year;

    @FXML
    private TableColumn<StudentViewModel, String> check_student_column_rrn;

    @FXML
    private TableColumn<StudentViewModel, String> check_student_column_contact;

    @FXML
    private TableColumn<StudentViewModel, String> check_student_column_parentZipCode;

    @FXML
    private TableColumn<StudentViewModel, String> check_student_column_parentAddress;
    
    @Override
	public void initialize(URL location, ResourceBundle resources)
	{
		System.out.println("학생 조회 새로고침됨");
	}
	
	//---------------------이벤트---------------------

    @FXML
    void on_check_button_actioned(ActionEvent event) 
    {
    	checkStudents();
    }
    
	//----------------------로직---------------------
    
    private void checkStudents()
    {
    	//배정내역 테이블 조회 요청
    	Serializable result = Responser.admin_studentCheckPage_onCheck();
    	
    	//서버랑 통신이 됬는가?
        if(result == null)
        {
        	IOHandler.getInstance().showAlert("서버에 연결할 수 없습니다.");
        	return;
        }
        
        Tuple<Bool, String> checkTuple = (Tuple<Bool, String>) result;
        
        if(checkTuple.obj1 == Bool.FALSE)
        {
        	IOHandler.getInstance().showAlert(checkTuple.obj2);
        	return;
        }
        
        Tuple<Bool, ArrayList<Student>> resultTuple = (Tuple<Bool, ArrayList<Student>>) result;
        ArrayList<Student> studentList = resultTuple.obj2;
        if(studentList != null)
        {
        	//객체를 테이블뷰 모델로 변환
        	ObservableList<StudentViewModel> studentModels = FXCollections.observableArrayList();
        	
        	for(Student stu : studentList)
        	{
        		studentModels.add(studentToViewModel(stu));
        	}
        	
            //테이블뷰에 추가
            setStudentTableView(studentModels);
        }
    }
    
    private void setStudentTableView(ObservableList<StudentViewModel> studentModels)
    {
    	//서버에서 받아온거 표시하게 만듬.
    	check_student_column_studentId.setCellValueFactory(cellData -> cellData.getValue().idProperty());
    	check_student_column_name.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
    	check_student_column_gender.setCellValueFactory(cellData -> cellData.getValue().genderProperty());
    	check_student_column_departmentName.setCellValueFactory(cellData -> cellData.getValue().departmentProperty());
    	check_student_column_year.setCellValueFactory(cellData -> cellData.getValue().yearProperty());
    	check_student_column_rrn.setCellValueFactory(cellData -> cellData.getValue().rrnProperty());
    	check_student_column_contact.setCellValueFactory(cellData -> cellData.getValue().contactProperty());
    	check_student_column_parentZipCode.setCellValueFactory(cellData -> cellData.getValue().parentZipCodeProperty());
    	check_student_column_parentAddress.setCellValueFactory(cellData -> cellData.getValue().parentAddressProperty());
    	check_student_tableview.setItems(studentModels);
    }
}
