package controller.administrator;

import java.io.Serializable;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import models.*;
import tableViewModel.PlacementHistoryViewModel;

//입사자 조회 및 관리
public class BoarderManageTabController extends InnerPageController 
{

	@FXML
    private Button allocate_button;

    @FXML
    private Button check_button;

    @FXML
    private TableView<PlacementHistoryViewModel> check_placementHistory_tableview;
    
    @FXML
    private TableColumn<PlacementHistoryViewModel, String> check_placementHistory_column_studentId;

    @FXML
    private TableColumn<PlacementHistoryViewModel, String> check_placementHistory_column_roomNumber;

    @FXML
    private TableColumn<PlacementHistoryViewModel, String> check_placementHistory_column_semester;

    @FXML
    private TableColumn<PlacementHistoryViewModel, String> check_placementHistory_column_dormName;

    @FXML
    private TableColumn<PlacementHistoryViewModel, String> check_placementHistory_column_seat;

    @FXML
    private TableColumn<PlacementHistoryViewModel, String> check_placementHistory_column_checkout;

    @FXML
    private Button delete_button;

    @FXML
    private TextField delete_id_textfield;

    @FXML
    private TextField delete_semester_textfield;

    @FXML
    private TextField delete_roomNumber_textfield;

    @FXML
    private TextField delete_dormName_textfield;

    @FXML
    private Button insert_button;

    @FXML
    private TextField insert_id_textfield;

    @FXML
    private TextField insert_roomNumber_textfield;

    @FXML
    private TextField insert_semester_textfield;

    @FXML
    private TextField insert_seat_textfield;

    @FXML
    private TextField insert_mealType_textfield;

    @FXML
    private TextField insert_dormName_textfield;

    @FXML
    private DatePicker insert_checkout_datepicker;

    @FXML
    private CheckBox insert_snore_checkbox;
    
	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		System.out.println("입사자 조회 및 관리 새로고침됨");
	}
	
	//---------------------이벤트---------------------
	
	@FXML
    void on_allocate_button_actioned(ActionEvent event) 
	{
		System.out.println("입사자 조회 및 관리 : 입사자 등록(배정) 클릭됨");
		allocate();
    }

    @FXML
    void on_check_button_actioned(ActionEvent event) 
    {
    	System.out.println("입사자 조회 및 관리 : 조회 클릭됨");
    	checkBoarders();
    }

    @FXML
    void on_delete_button_actioned(ActionEvent event) 
    {
    	System.out.println("입사자 조회 및 관리 : 삭제 클릭됨");
    	deleteBoarder();
    }

    @FXML
    void on_insert_button_actioned(ActionEvent event) 
    {
    	System.out.println("입사자 조회 및 관리 : 등록 클릭됨");
    	insertBoarder();
    }
    
	//---------------------로직---------------------

    private void allocate()
    {
    	//입사자 등록(배정) 쿼리 요청
    	Tuple<Bool, String> resultTuple = Responser.admin_boarderManagePage_onAllocate();
    	
    	//서버랑 통신이 됬는가?
        if(resultTuple == null)
        {
        	IOHandler.getInstance().showAlert("서버에 연결할 수 없습니다.");
        	return;
        }
        
        if(resultTuple != null)
        {
        	//성공하면 테이블 조회
        	if(resultTuple.obj1 == Bool.TRUE)
        	{
        		checkBoarders();
        	}
        	//성공/실패 메시지 표시
        	IOHandler.getInstance().showAlert(resultTuple.obj2);
        }
    }
    
    //-------------------------------------------
    
    private void checkBoarders()
    {
    	//배정내역 테이블 조회 요청
    	Serializable result = Responser.admin_boarderManagePage_onCheck();
    	
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
            setPlacementHistoryTableView(historyModels);
        }
    }
    
    private void setPlacementHistoryTableView(ObservableList<PlacementHistoryViewModel> historyList)
    {
    	//서버에서 받아온거 표시하게 만듬.
    	check_placementHistory_column_studentId.setCellValueFactory(cellData -> cellData.getValue().studentIdProperty());
    	check_placementHistory_column_roomNumber.setCellValueFactory(cellData -> cellData.getValue().roomIdProperty());
    	check_placementHistory_column_semester.setCellValueFactory(cellData -> cellData.getValue().semesterProperty());
    	check_placementHistory_column_dormName.setCellValueFactory(cellData -> cellData.getValue().dormitoryNameProperty());
    	check_placementHistory_column_seat.setCellValueFactory(cellData -> cellData.getValue().seatProperty());
    	check_placementHistory_column_checkout.setCellValueFactory(cellData -> cellData.getValue().checkoutProperty());
    	
    	check_placementHistory_tableview.setItems(historyList);
    }
    
    //-----------------------------------------------------------------
    
    private void deleteBoarder()
    {
    	String id = delete_id_textfield.getText();
    	String roomNumber = delete_roomNumber_textfield.getText();
    	String semester = delete_semester_textfield.getText();
    	String dormName = delete_dormName_textfield.getText();
    	
    	if(id == null || id.isEmpty())
    	{
    		//학번 비어있음
    		IOHandler.getInstance().showAlert("학번이 비어있습니다.");
    		return;
    	}
    	else if(roomNumber == null || roomNumber.isEmpty())
    	{
    		//호실 번호 비어있음
    		IOHandler.getInstance().showAlert("호실 번호가 비어있습니다.");
    		return;
    	}
    	else if(semester == null || semester.isEmpty())
    	{
    		//학기 없음
    		IOHandler.getInstance().showAlert("학기가 비어있습니다.");
    		return;
    	}
    	else if(dormName == null || dormName.isEmpty())
    	{
    		//생활관명이 없음
    		IOHandler.getInstance().showAlert("생활관명이 비어있습니다.");
    		return;
    	}

    	PlacementHistory data = new PlacementHistory(id, roomNumber, Integer.parseInt(semester), dormName);
    	Tuple<Bool, String> resultTuple = Responser.admin_boarderManagePage_onDelete(data);
    	
    	//서버랑 통신이 됬는가?
        if(resultTuple == null)
        {
        	IOHandler.getInstance().showAlert("서버에 연결할 수 없습니다.");
        	return;
        }
        
        if(resultTuple != null)
        {
        	//성공하면 테이블 조회
        	if(resultTuple.obj1 == Bool.TRUE)
        	{
        		clearDeleteInfo();
        		checkBoarders();
        	}
        	//성공/실패 메시지 표시
        	IOHandler.getInstance().showAlert(resultTuple.obj2);
        	
        }
    }
    
    private void clearDeleteInfo()
    {
    	//선택한 항목들 클리어
		delete_id_textfield.setText(null);
		delete_roomNumber_textfield.setText(null);
		delete_semester_textfield.setText(null);
		delete_dormName_textfield.setText(null);
    }
    
    //-----------------------------------------------------------------
    
    private void insertBoarder()
    {
    	String id = insert_id_textfield.getText();
    	String roomNumber = insert_roomNumber_textfield.getText();
    	String semester = insert_semester_textfield.getText();
    	String dormName = insert_dormName_textfield.getText();
    	String seat = insert_seat_textfield.getText();
    	LocalDate checkout_l = insert_checkout_datepicker.getValue();
    	String mealType = insert_mealType_textfield.getText();
    	Bool isSnore = insert_snore_checkbox.isSelected() ? Bool.TRUE : Bool.FALSE;
    	
    	if(id == null || id.isEmpty())
    	{
    		//학번 비어있음
    		IOHandler.getInstance().showAlert("학번이 비어있습니다.");
    		return;
    	}
    	else if(roomNumber == null || roomNumber.isEmpty())
    	{
    		//호실 번호 비어있음
    		IOHandler.getInstance().showAlert("호실 번호가 비어있습니다.");
    		return;
    	}
    	else if(semester == null || semester.isEmpty())
    	{
    		//학기 없음
    		IOHandler.getInstance().showAlert("학기가 비어있습니다.");
    		return;
    	}
    	else if(dormName == null || dormName.isEmpty())
    	{
    		//생활관명이 없음
    		IOHandler.getInstance().showAlert("생활관명이 비어있습니다.");
    		return;
    	}
    	else if(seat == null || seat.isEmpty())
    	{
    		//자리가 없음
    		IOHandler.getInstance().showAlert("자리가 비어있습니다.");
    		return;
    	}
    	else if(checkout_l == null || checkout_l.toString().isEmpty())
    	{
    		//퇴사예정일이 없음
    		IOHandler.getInstance().showAlert("퇴사예정일이 비어있습니다.");
    		return;
    	}
    	else if(mealType == null || mealType.isEmpty())
    	{
    		//몇일식이 없음
    		IOHandler.getInstance().showAlert("몇일식이 비어있습니다.");
    		return;
    	}
    	
    	//datePicker에서 나온 localDate를 Date유형으로 변환
    	Date checkout = localDateToDate(checkout_l);
    	
    	//배정내역 객체 생성
    	PlacementHistory history = new PlacementHistory(id, roomNumber, Integer.parseInt(semester), dormName, 
    			Seat.get(seat), checkout);
    	
    	//신청 객체 생성
    	Application application = new Application(Integer.parseInt(mealType), isSnore);
    	
    	Tuple<PlacementHistory, Application> data = new Tuple<PlacementHistory, Application>(history, application);
    	
    	Tuple<Bool, String> resultTuple = Responser.admin_boarderManagePage_onInsert(data);
    	
//    	서버랑 통신이 됬는가?
        if(resultTuple == null)
        {
        	IOHandler.getInstance().showAlert("서버에 연결할 수 없습니다.");
        	return;
        }
        
        if(resultTuple != null)
        {
        	if(resultTuple.obj1 == Bool.TRUE)
        	{
        		clearInsertInfo();
        		checkBoarders();
        	}
        	//성공/실패 메시지 표시
        	IOHandler.getInstance().showAlert(resultTuple.obj2);
        	
        }
    	
    } 
    private void clearInsertInfo()
    {
    	insert_id_textfield.setText(null);
		insert_roomNumber_textfield.setText(null);
		insert_semester_textfield.setText(null);
		insert_dormName_textfield.setText(null);
		insert_seat_textfield.setText(null);
		insert_checkout_datepicker.setValue(null);
		insert_mealType_textfield.setText(null);
    }
}