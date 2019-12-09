package controller.administrator;

import java.io.Serializable;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

import application.IOHandler;
import application.Responser;
import controller.InnerPageController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import models.*;
import enums.*;
import tableViewModel.ScheduleViewModel;

//선발 일정 조회 및 관리
public class ScheduleManageTabController extends InnerPageController 
{
	@FXML
    private Button check_button;

    @FXML
    private TableView<ScheduleViewModel> check_schedule_tableview;
    
    @FXML
    private TableColumn<ScheduleViewModel, String> check_schedule_column_id;

    @FXML
    private TableColumn<ScheduleViewModel, String> check_schedule_column_type;

    @FXML
    private TableColumn<ScheduleViewModel, String> check_schedule_column_startDay;

    @FXML
    private TableColumn<ScheduleViewModel, String> check_schedule_column_endDay;

    @FXML
    private TableColumn<ScheduleViewModel, String> check_schedule_column_description;

    @FXML
    private Button delete_button;

    @FXML
    private TextField delete_id_textfield;

    @FXML
    private Button insert_button;

    @FXML
    private ComboBox<String> insert_type_combobox;

    @FXML
    private DatePicker insert_startDate_datepicker;

    @FXML
    private DatePicker insert_endDate_datepicker;

    @FXML
    private TextField insert_etc_textfield;
    
    ArrayList<ScheduleCode> scheduleCodeList = null;

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		System.out.println("선발 일정 조회 및 관리 새로고침됨");
		
		//네트워킹 후 유형을 서버에서 가져온다.		
		onEnter();
	}
	
	//---------------------이벤트---------------------
	
	@FXML
    void on_check_button_actioned(ActionEvent event) 
	{
		System.out.println("선발 일정 조회 및 관리 : 조회 클릭됨");
		checkSchdules();
    }

    @FXML
    void on_delete_button_actioned(ActionEvent event) 
    {
    	System.out.println("선발 일정 조회 및 관리 : 삭제 클릭됨");
    	deleteSchedule();
    }

    @FXML
    void on_insert_button_actioned(ActionEvent event) 
    {
    	System.out.println("선발 일정 조회 및 관리 : 등록 클릭됨");
    	insertSchedule();
    }
	
	//---------------------로직---------------------
    
    //서버에게서 받아온 스케쥴 할일 코드 목록을 콤보박스에 추가한다.
    private void onEnter()
    {
    	//서버에게서 스케쥴 코드 목록을 받아 저장해둔다.
    	Serializable result = Responser.admin_scheduleManagePage_onEnter();
    	
    	//서버랑 통신이 됬는가?
        if(result == null)
        {
        	IOHandler.getInstance().showAlert("서버에 연결할 수 없습니다.");
        	if(!IOHandler.getInstance().showDialog("디버그", "계속 진행하시겠습니까?"))
        	{
        		//여기서 페이지 닫게 해주자.
        		close();
        		return;
        	}
        }
        
        //제대로 왔는지 체크
        Tuple<Bool, String> checkTuple = (Tuple<Bool, String>) result;
        
        //스케쥴 코드 받아오는데 실패했는가?
        if(checkTuple.obj1 == Bool.FALSE)
        {
        	//받아온 오류메시지 표시
        	IOHandler.getInstance().showAlert(checkTuple.obj2);
        	return;
        }
        
        //성공적으로 받아왔으면 저장.
        Tuple<Bool, ArrayList<ScheduleCode>> resultTuple = (Tuple<Bool, ArrayList<ScheduleCode>>) result; 
        scheduleCodeList = resultTuple.obj2;
        
        if(scheduleCodeList != null)
        {
        	//스케쥴 할일 코드 목록을 유형 콤보박스에 추가
        	setCombobox(insert_type_combobox, scheduleCodeList);
        }
    }
    
    private void setCombobox(ComboBox<String> combobox, ArrayList<ScheduleCode> list)
    {
    	for(ScheduleCode sc : list)
    	{
    		combobox.getItems().add(sc.name);
    	}
    }
    
    //-----------------------------------------------------------------

    private void checkSchdules()
    {
    	//네트워킹해서 스케쥴 테이블 쫙 긁어와야됨.
    	//긁어올때 스케쥴 할일 코드 테이블도 긁어와야됨.
    	Serializable result = Responser.admin_scheduleManagePage_onCheck();
    	
    	//서버랑 통신이 됬는가?
        if(scheduleCodeList == null)
        {
        	IOHandler.getInstance().showAlert("서버에 연결할 수 없습니다.");
        	return;
        }
        
        Tuple<Bool, String> checkTuple = (Tuple<Bool, String>) result;
        
        //성공했는가?
        if(checkTuple.obj1 == Bool.FALSE)
        {
        	//오류메시지 원인 표시
        	IOHandler.getInstance().showAlert(checkTuple.obj2);
        	return;
        }
        
	    Tuple<Bool, ArrayList<Schedule>> resultTuple = (Tuple<Bool, ArrayList<Schedule>>) result;
	    ArrayList<Schedule> scheduleList = resultTuple.obj2;
        
        if(scheduleCodeList != null)
        {
        	ObservableList<ScheduleViewModel> scheduleViewModels = FXCollections.observableArrayList();
        	
        	for(Schedule sc : scheduleList)
        	{
        		scheduleViewModels.add(scheduleToViewModel(sc, scheduleCodeList));
        	}
        	
        	setTableView(scheduleViewModels);
        }
    }
    
    private void setTableView(ObservableList<ScheduleViewModel> scheduleViewModels)
    {
    	check_schedule_column_id.setCellValueFactory(cellData -> cellData.getValue().scheduleIdProperty());
    	check_schedule_column_type.setCellValueFactory(cellData -> cellData.getValue().codeProperty());
    	check_schedule_column_startDay.setCellValueFactory(cellData -> cellData.getValue().startDateProperty());
    	check_schedule_column_endDay.setCellValueFactory(cellData -> cellData.getValue().endDateProperty());
    	check_schedule_column_description.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
    	check_schedule_tableview.setItems(scheduleViewModels);
    }
    
    //-----------------------------------------------------------------
    
    private void deleteSchedule()
    {
    	String id = delete_id_textfield.getText();
    	
    	if(id == null || id.isEmpty())
    	{
    		//ID 비어있음
    		IOHandler.getInstance().showAlert("ID가 비어있습니다.");
    		return;
    	}
    	
    	//서버에 삭제 쿼리 요청 후 성공/실패여부 메시지로 알려주자.
    	Tuple<Bool, String> resultTuple = Responser.admin_scheduleManagePage_onDelete(id);
		
		if(resultTuple == null)
		{
			IOHandler.getInstance().showAlert("서버에 연결할 수 없습니다.");
        	return;
		}
		
		if(resultTuple != null)
		{
			if(resultTuple.obj1 == Bool.TRUE)
			{
				//입력했던 항목들 클리어
				delete_id_textfield.setText(null);
			}
			
			//성공/실패 메시지 표시
			IOHandler.getInstance().showAlert(resultTuple.obj2);
		}
    }
    
    //-----------------------------------------------------------------
    
    private void insertSchedule()
    {
    	String type = insert_type_combobox.getSelectionModel().getSelectedItem();
    	LocalDate startDate_l = insert_startDate_datepicker.getValue();
    	LocalDate endDate_l = insert_endDate_datepicker.getValue();
    	String etc = insert_etc_textfield.getText();
    	
    	if(type == null || type.isEmpty())
    	{
    		//유형 비어있음
    		IOHandler.getInstance().showAlert("유형이 비어있습니다.");
    		return;
    	}
    	else if(startDate_l == null || startDate_l.toString().equals(""))
    	{
    		//시작일 비어있음
    		IOHandler.getInstance().showAlert("시작일이 비어있습니다.");
    		return;
    	}
    	else if(endDate_l == null || endDate_l.toString().equals(""))
    	{
    		//종료일이 없음
    		IOHandler.getInstance().showAlert("종료일이 비어있습니다.");
    		return;
    	}

    	Date startDate = localDateToDate(startDate_l);
    	Date endDate = localDateToDate(endDate_l);
    	
    	//서버에서 받아온 스케쥴 유형 코드 목록이 있어야한다. 없으면 없다고 알려주고 막자.
    	if(scheduleCodeList == null)
    	{
    		IOHandler.getInstance().showAlert("서버에서 받아온 스케쥴 유형이 없습니다. 페이지를 새로고침하고 다시 시도해주세요.");
    		return;
    	}
    	int code = todoStrToCode(type, scheduleCodeList);
    		
    	
    	//스케쥴 객체 생성.
    	Schedule schedule = new Schedule(-1, code, startDate, endDate, etc);
    	
    	//서버에 삭제 쿼리 요청 후 성공/실패여부 메시지로 알려주자.
    	Tuple<Bool, String> resultTuple = Responser.admin_scheduleManagePage_onInsert(schedule);
    	
    	if(resultTuple == null)
    	{
    		IOHandler.getInstance().showAlert("서버에 연결할 수 없습니다.");
        	return;
    	}
    	
    	if(resultTuple != null)
    	{
    		if(resultTuple.obj1 == Bool.TRUE)
    		{
    			//입력했던 항목들 클리어
    			clearInsertInfo();
    		}
    		//성공/실패 메시지 표시
    		IOHandler.getInstance().showAlert(resultTuple.obj2);
    	}
    }
    
    private void clearInsertInfo()
    {
    	//선택한 항목들 클리어
		insert_type_combobox.getSelectionModel().select(-1);
		insert_startDate_datepicker.setValue(null);
		insert_endDate_datepicker.setValue(null);
		insert_etc_textfield.setText("");
    }
}


