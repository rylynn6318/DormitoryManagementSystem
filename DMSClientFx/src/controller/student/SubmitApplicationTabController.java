package controller.student;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class SubmitApplicationTabController implements Initializable 
{
 	@FXML
    private Button application_button;

    @FXML
    private Button cancel_button;

    @FXML
    private ComboBox<?> oneYear_dorm_combobox;

    @FXML
    private ComboBox<?> oneYear_period_combobox;

    @FXML
    private ComboBox<?> oneYear_meal_combobox;

    @FXML
    private ComboBox<?> firstChoice_dorm_combobox;

    @FXML
    private ComboBox<?> firstChoice_period_combobox;

    @FXML
    private ComboBox<?> firstChoice_meal_combobox;

    @FXML
    private ComboBox<?> secondChoice_dorm_combobox;

    @FXML
    private ComboBox<?> secondChoice_period_combobox;

    @FXML
    private ComboBox<?> secondChoice_meal_combobox;

    @FXML
    private ComboBox<?> thirdChoice_dorm_combobox;

    @FXML
    private ComboBox<?> thirdChoice_period_combobox;

    @FXML
    private ComboBox<?> thirdChoice_meal_combobox;
    
    @FXML
    private Label oneYear_cost_label;

    @FXML
    private Label firstChoice_cost_label;

    @FXML
    private Label secondChoice_cost_label;

    @FXML
    private Label thirdChoice_cost_label;

    @FXML
    private TextField contact_textfield;

    @FXML
    private CheckBox isSnore_checkbox;

    @FXML
    private TextArea info_textarea;

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		System.out.println("생활관 입사 신청 새로고침됨");
		
		//네트워킹
		info_textarea.setText("서버에서 받아온 안내사항입니다.");
		
	}
	
	//---------------------이벤트---------------------
	
	@FXML
    void on_application_button_actioned(ActionEvent event) 
    {
		System.out.println("생활관 입사 신청 : 신청 클릭됨");
		sendApplication();
    }

    @FXML
    void on_cancel_button_actioned(ActionEvent event) 
    {
    	System.out.println("생활관 입사 신청 : 취소 클릭됨");
    	cancelApplication();
    }
    
    //---------------------로직---------------------
    
    private void sendApplication()
    {
    	//대충 신청 전송하는 메소드
    	
    }
    
    private void cancelApplication()
    {
    	//대충 취소 전송하는 메소드
    }

}
