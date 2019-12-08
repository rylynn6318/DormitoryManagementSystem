package controller.student;

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
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import models.Application;
import models.Dormitory;
import models.Tuple;
import tableViewModel.*;

public class CheckApplicationTabController extends InnerPageController
{
	@FXML
    private Button check_button;

    @FXML
    private TableView<StudentApplicationViewModel> application_history_tableview;

    @FXML
    private TableColumn<StudentApplicationViewModel, String> application_history_column_choice;

    @FXML
    private TableColumn<StudentApplicationViewModel, String> application_history_column_dormName;

    @FXML
    private TableColumn<StudentApplicationViewModel, String> application_history_column_mealType;

    @FXML
    private TableView<StudentApplicationResultViewModel> selection_result_tableview;

    @FXML
    private TableColumn<StudentApplicationResultViewModel, String> selection_result_column_choice;

    @FXML
    private TableColumn<StudentApplicationResultViewModel, String> selection_result_column_dormName;

    @FXML
    private TableColumn<StudentApplicationResultViewModel, String> selection_result_column_mealType;
    
    @FXML
    private TableColumn<StudentApplicationResultViewModel, String> selection_result_column_isPassed;

    @FXML
    private TableColumn<StudentApplicationResultViewModel, String> selection_result_column_isPaid;

    @FXML
    private TextArea info_textarea;
    
	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		System.out.println("생활관 신청 조회 새로고침됨");

		//네트워킹
		checkSchedule();
	}
	
	//---------------------이벤트---------------------
	
    @FXML
    void on_check_button_actioned(ActionEvent event) 
    {
    	System.out.println("생활관 신청 조회 : 조회 클릭됨");
    	checkApplication();
    }
    
    //---------------------로직---------------------
    
    private void checkSchedule()
    {
    	//서버와 통신해보고, Bool이 True이면 String에는 안내사항이, Bool이 False이면 String에는 진입불가원인이 나온다.
    	Tuple<Bool, String> resultTuple = Responser.student_CheckApplicationPage_onEnter();
        
		//서버랑 통신이 됬는가?
        if(resultTuple == null)
        {
        	IOHandler.getInstance().showAlert("서버에 연결할 수 없습니다.");
        	if(!IOHandler.getInstance().showDialog("디버그", "계속 진행하시겠습니까?"))
        	{
        		//여기서 페이지 닫게 해주자.
        		close();
        		return;
        	}
        }
        
        if(resultTuple != null)
        {
        	//스케쥴 체크가 됬는가?
        	//스케쥴 때문에 진입 불가인 경우 tuple의 첫번째 항목이 false로 반환된다.
            if(resultTuple.obj1 == Bool.FALSE)
            {
            	IOHandler.getInstance().showAlert(resultTuple.obj2);
            	//여기서 페이지 닫게 해주자.
            	//return;
            }
            else
            {
            	//안내사항 표시
                if(resultTuple.obj2 != null)
                	info_textarea.setText(resultTuple.obj2);
            }
        }
    }
    
    private void checkApplication()
    {
    	//여기는 뭐 검사할 필요없이 바로 서버로 요청날림.
    	//서버로 요청날리고 받아온 후 refreshApplicationTable 호출
    	Tuple<ArrayList<Application>, ArrayList<Application>> resultTuple = Responser.student_CheckApplicationPage_onCheck();
    	
    	//서버랑 통신이 됬는가?
        if(resultTuple == null)
        {
        	IOHandler.getInstance().showAlert("서버에 연결할 수 없습니다.");
        	return;
        }
        
        //서버에서 받아온 데이터
        ArrayList<Application> receivedApplicationList1 = resultTuple.obj1;		//생활관 입사지원 내역(지망, 생활관명, 식사구분만 받아옴)
        ArrayList<Application> receivedApplicationList2 = resultTuple.obj2;		//생활관 선발결과 내역(지망, 생활관명, 식비구분, 합격여부, 납부여부 받아옴)
        
    	//아래에서 서버와 통신하여 appHistory 배열과, selections 배열을 객체로 채웠다..
		//서버에서 Application 객체를 가져오면, StudentApplicationViewModel로 형변환해서 써야할듯. 
        ObservableList<StudentApplicationViewModel> appHistory = appListToAppViewModel(receivedApplicationList1);
		
		//이건 생활관 선발 결과 객체 배열
		//지망, 생활관명, 식비구분, 합격여부, 납부여부를 받아와야한다.
        ObservableList<StudentApplicationResultViewModel> selections = appListToResultViewModel(receivedApplicationList2);
    	
    	setApplicationTable(appHistory);
    	setApplicationResultTable(selections);
    }
    
    //서버에서 Application 객체를 가져오면, StudentApplicationViewModel로 형변환해서 반환 
    private ObservableList<StudentApplicationViewModel> appListToAppViewModel(ArrayList<Application> appList)
    {
    	ObservableList<StudentApplicationViewModel> appHistory = FXCollections.observableArrayList();
    	for(Application app : appList)
    	{
    		appHistory.add(new StudentApplicationViewModel(app.getChoice(), app.getDormitoryName(), app.getMealType()));
    	}
    	return appHistory;
    }
    
    //서버에서 Application 객체를 가져오면, StudentApplicationResultViewModel로 형변환해서 반환 
    private ObservableList<StudentApplicationResultViewModel> appListToResultViewModel(ArrayList<Application> appList)
    {
    	ObservableList<StudentApplicationResultViewModel> selections = FXCollections.observableArrayList();
    	for(Application app : appList)
    	{
    		selections.add(new StudentApplicationResultViewModel(app.getChoice(), app.getDormitoryName(), app.getMealType(), app.isPassed(), app.isPaid()));
    	}
    	return selections;
    }
    
    private void setApplicationTable(ObservableList<StudentApplicationViewModel> appHistory)
    {
    	application_history_column_choice.setCellValueFactory(cellData -> cellData.getValue().choiceProperty());
    	application_history_column_dormName.setCellValueFactory(cellData -> cellData.getValue().dormNameProperty());
    	application_history_column_mealType.setCellValueFactory(cellData -> cellData.getValue().mealTypeProperty());
    	application_history_tableview.setItems(appHistory);
    }
    
    private void setApplicationResultTable(ObservableList<StudentApplicationResultViewModel> selections)
    {
    	selection_result_column_choice.setCellValueFactory(cellData -> cellData.getValue().choiceProperty());
    	selection_result_column_dormName.setCellValueFactory(cellData -> cellData.getValue().dormNameProperty());
    	selection_result_column_mealType.setCellValueFactory(cellData -> cellData.getValue().mealTypeProperty());
    	selection_result_column_isPassed.setCellValueFactory(cellData -> cellData.getValue().isPassedProperty());
    	selection_result_column_isPaid.setCellValueFactory(cellData -> cellData.getValue().isPaidProperty());
    	selection_result_tableview.setItems(selections);
    }
}