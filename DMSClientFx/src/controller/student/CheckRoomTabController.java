package controller.student;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class CheckRoomTabController implements Initializable 
{
	@FXML
    private Button check_button;

    @FXML
    private Label select_result_label;

    @FXML
    private Label isPaid_label;

    @FXML
    private Label mealType_label;

    @FXML
    private Label dorm_label;

    @FXML
    private Label roomType_label;

    @FXML
    private Label roomAndBed_label;

    @FXML
    private TextArea info_textarea;


	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		System.out.println("생활관 호실 조회 새로고침됨");
		
		//네트워킹
		info_textarea.setText("서버에서 받아온 안내사항입니다.");
	}
	
	//---------------------이벤트---------------------
	
    @FXML
    void on_check_button_actioned(ActionEvent event) 
    {
    	System.out.println("생활관 호실 조회 : 조회 클릭됨");
    }
	
	//---------------------로직---------------------

}
