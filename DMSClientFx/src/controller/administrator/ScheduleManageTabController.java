package controller.administrator;

import java.net.URL;
import java.util.ResourceBundle;

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
    private ComboBox<?> insert_type_combobox;

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
		
	}
	
	//---------------------이벤트---------------------
	
	@FXML
    void on_check_button_actioned(ActionEvent event) 
	{
		System.out.println("선발 일정 조회 및 관리 : 조회 클릭됨");
    }

    @FXML
    void on_delete_button_actioned(ActionEvent event) 
    {
    	System.out.println("선발 일정 조회 및 관리 : 삭제 클릭됨");
    }

    @FXML
    void on_insert_button_actioned(ActionEvent event) 
    {
    	System.out.println("선발 일정 조회 및 관리 : 등록 클릭됨");
    }
	
	//---------------------로직---------------------

}
