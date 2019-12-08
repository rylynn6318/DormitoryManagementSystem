package controller.administrator;

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
    	scheduleCodeList = Responser.admin_scheduleManagePage_onEnter();
    	
    	//서버랑 통신이 됬는가?
        if(scheduleCodeList == null)
        {
        	IOHandler.getInstance().showAlert("서버에 연결할 수 없습니다.");
        	if(!IOHandler.getInstance().showDialog("디버그", "계속 진행하시겠습니까?"))
        	{
        		//여기서 페이지 닫게 해주자.
        		close();
        		return;
        	}
        	return;
        }
        
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

    private void checkSchdules()
    {
    	//네트워킹해서 스케쥴 테이블 쫙 긁어와야됨.
    	//긁어올때 스케쥴 할일 코드 테이블도 긁어와야됨.
    	ArrayList<Schedule> resultList = Responser.admin_scheduleManagePage_onCheck();
    	
    	//서버랑 통신이 됬는가?
        if(scheduleCodeList == null)
        {
        	IOHandler.getInstance().showAlert("서버에 연결할 수 없습니다.");
        	return;
        }
        
        if(scheduleCodeList != null)
        {
        	ObservableList<ScheduleViewModel> scheduleViewModels = FXCollections.observableArrayList();
        	
        	for(Schedule sc : resultList)
        	{
        		scheduleViewModels.add(scheduleToViewModel(sc));
        	}
        	
        	setTableView(scheduleViewModels);
        }
    }
    
    //스케쥴 객체를 뷰모델로 바꾸는 메소드
    private ScheduleViewModel scheduleToViewModel(Schedule schedule)
    {
    	return new ScheduleViewModel(schedule.scheduleId, codeToTodoStr(schedule, scheduleCodeList), 
    			schedule.startDate, schedule.endDate, schedule.description);
    }
    
    //인자로 받은 스케쥴의 유형 이름을 얻어내는 메소드(code->문자)
    //서버에서 받은 유형 코드를 유형 문자로 바꾸어 tableView에 보여주기 위함임.
    private String codeToTodoStr(Schedule schedule, ArrayList<ScheduleCode> scList)
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
    private int todoStrToCode(String todoStr, ArrayList<ScheduleCode> scList)
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
    
    private void setTableView(ObservableList<ScheduleViewModel> scheduleViewModels)
    {
    	check_schedule_column_id.setCellValueFactory(cellData -> cellData.getValue().scheduleIdProperty());
    	check_schedule_column_type.setCellValueFactory(cellData -> cellData.getValue().codeProperty());
    	check_schedule_column_startDay.setCellValueFactory(cellData -> cellData.getValue().startDateProperty());
    	check_schedule_column_endDay.setCellValueFactory(cellData -> cellData.getValue().endDateProperty());
    	check_schedule_column_description.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
    	check_schedule_tableview.setItems(scheduleViewModels);
    }
    
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
    	Tuple<Bool, String> resultList = Responser.admin_scheduleManagePage_onDelete(id);
		
		if(resultList == null)
		{
			IOHandler.getInstance().showAlert("서버에 연결할 수 없습니다.");
        	return;
		}
		
		if(resultList != null)
		{
			if(resultList.obj1 == Bool.TRUE)
			{
				//입력했던 항목들 클리어
				delete_id_textfield.setText(null);
			}
			
			//성공/실패 메시지 표시
			IOHandler.getInstance().showAlert(resultList.obj2);
		}
    }
    
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
    	Schedule schedule = new Schedule("-1", code, startDate, endDate, etc);
    	
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
    			clearInserts();
    		}
    		//성공/실패 메시지 표시
    		IOHandler.getInstance().showAlert(resultTuple.obj2);
    	}
    }
    
    private Date localDateToDate(LocalDate local)
    {
    	Instant instant = Instant.from(local.atStartOfDay(ZoneId.systemDefault()));
    	Date date = Date.from(instant);
    	return date;
    }
    
    private void clearInserts()
    {
    	//선택한 항목들 클리어
		insert_type_combobox.getSelectionModel().select(-1);
		insert_startDate_datepicker.setValue(null);
		insert_endDate_datepicker.setValue(null);
		insert_etc_textfield.setText("");
    }
}


