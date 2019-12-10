package controller.administrator;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import application.IOHandler;
import application.Responser;
import controller.InnerPageController;
import enums.Bool;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import models.PlacementHistory;
import models.Tuple;
import tableViewModel.PlacementHistoryViewModel;

public class StudentCheckTabController extends InnerPageController
{

    @FXML
    private Button check_button;

    @FXML
    private TableView<?> check_student_tableview;

    @FXML
    private TableColumn<?, ?> check_student_column_studentId;

    @FXML
    private TableColumn<?, ?> check_student_column_name;

    @FXML
    private TableColumn<?, ?> check_student_column_gender;

    @FXML
    private TableColumn<?, ?> check_student_column_departmentName;

    @FXML
    private TableColumn<?, ?> check_student_column_year;

    @FXML
    private TableColumn<?, ?> check_student_column_rrn;

    @FXML
    private TableColumn<?, ?> check_student_column_contact;

    @FXML
    private TableColumn<?, ?> check_student_column_parentZipCode;

    @FXML
    private TableColumn<?, ?> check_student_column_parentAddress;
    
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
        
        Tuple<Bool, ArrayList<PlacementHistory>> resultTuple = (Tuple<Bool, ArrayList<PlacementHistory>>) result;
        ArrayList<PlacementHistory> historyList = resultTuple.obj2;
        if(historyList != null)
        {
        	//객체를 테이블뷰 모델로 변환
        	ObservableList<PlacementHistoryViewModel> historyModels = FXCollections.observableArrayList();
        	
        	for(PlacementHistory history : historyList)
        	{
        		historyModels.add(placementHistoryToViewModel(history));
        	}
        	
            //테이블뷰에 추가
//            setPlacementHistoryTableView(historyModels);
        }
    }
}
