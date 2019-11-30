package controller.student;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

public class CheckBillTabController implements Initializable 
{
    @FXML
    private Button check_button;

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		System.out.println("생활관 고지서 조회 새로고침됨");
	}
	
	//---------------------이벤트---------------------
	
	@FXML
    void on_check_button_actioned(ActionEvent event) 
    {
		System.out.println("생활관 고지서 조회 : 조회 클릭 됨");
    }
	
	//---------------------로직---------------------

}
