package controller.student;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;

public class CheckApplicationTabController implements Initializable
{
	@FXML
    private Button check_button;

    @FXML
    private TableView<?> oneYear_application_history_tableview;

    @FXML
    private TableView<?> oneSemester_application_history_tableview;

    @FXML
    private TableView<?> selection_result_tableview;

    @FXML
    private TextArea info_textarea;
    
	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		System.out.println("생활관 신청 조회 새로고침됨");

		//네트워킹
		info_textarea.setText("서버에서 받아온 안내사항입니다.");
	}
	
	//---------------------이벤트---------------------
	
    @FXML
    void on_check_button_actioned(ActionEvent event) 
    {
    	System.out.println("생활관 신청 조회 : 조회 클릭됨");
    	checkApplication();
    }
    
    //---------------------로직---------------------
    
    private void checkApplication()
    {
    	//여기는 뭐 검사할 필요없이 바로 서버로 요청날림.
    }

}
