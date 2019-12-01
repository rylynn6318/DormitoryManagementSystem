package controller.administrator;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import application.IOHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

//선발 일정 조회 및 관리
public class ScheduleManageTabController implements Initializable 
{
	@FXML
    private Button check_button;

    @FXML
    private TableView<?> check_schedule_tableview;

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

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		System.out.println("선발 일정 조회 및 관리 새로고침됨");
		
		//네트워킹 후 유형을 서버에서 가져와야한다.
		insert_type_combobox.getItems().addAll("생활관 입사 신청", "생활관 관배정 및 합격자 발표", "생활관비 납부", 
				"결핵진단서 제출 기간", "생활관 추가합격자 발표", "생활관 추가합격자 생활관비 납부");
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

    private void checkSchdules()
    {
    	//네트워킹해서 스케쥴 테이블 쫙 긁어와야됨.
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
		boolean isSucceed = true;
		if(isSucceed)
		{
			IOHandler.getInstance().showAlert("일정 삭제에 성공하였습니다.");
			
			//선택한 항목들 클리어
			delete_id_textfield.setText(null);
		}
		else
		{
			IOHandler.getInstance().showAlert("일정 삭제에 실패하였습니다.");
		}
    }
    
    private void insertSchedule()
    {
    	String type = insert_type_combobox.getSelectionModel().getSelectedItem();
    	LocalDate startDate = insert_startDate_datepicker.getValue();
    	LocalDate endDate = insert_endDate_datepicker.getValue();
    	String etc = insert_etc_textfield.getText();
    	
    	if(type == null || type.isEmpty())
    	{
    		//유형 비어있음
    		IOHandler.getInstance().showAlert("유형이 비어있습니다.");
    		return;
    	}
    	else if(startDate == null || startDate.toString().equals(""))
    	{
    		//시작일 비어있음
    		IOHandler.getInstance().showAlert("시작일이 비어있습니다.");
    		return;
    	}
    	else if(endDate == null || endDate.toString().equals(""))
    	{
    		//종료일이 없음
    		IOHandler.getInstance().showAlert("종료일이 비어있습니다.");
    		return;
    	}
    	
    	//서버에 등록 쿼리 요청 후 성공/실패여부 메시지로 알려주자.
		boolean isSucceed = true;
		if(isSucceed)
		{
			IOHandler.getInstance().showAlert("일정 등록에 성공하였습니다.");
			
			//선택한 항목들 클리어
			insert_type_combobox.getSelectionModel().select(-1);
			insert_startDate_datepicker.setValue(null);
			insert_endDate_datepicker.setValue(null);
			insert_etc_textfield.setText("");
		}
		else
		{
			IOHandler.getInstance().showAlert("일정 등록에 실패하였습니다.");
		}
    }
}
