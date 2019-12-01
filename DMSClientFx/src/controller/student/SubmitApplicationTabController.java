package controller.student;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
    private ComboBox<String> oneYear_dorm_combobox;

    @FXML
    private ComboBox<String> oneYear_period_combobox;

    @FXML
    private ComboBox<String> oneYear_meal_combobox;

    @FXML
    private ComboBox<String> firstChoice_dorm_combobox;

    @FXML
    private ComboBox<String> firstChoice_period_combobox;

    @FXML
    private ComboBox<String> firstChoice_meal_combobox;

    @FXML
    private ComboBox<String> secondChoice_dorm_combobox;

    @FXML
    private ComboBox<String> secondChoice_period_combobox;

    @FXML
    private ComboBox<String> secondChoice_meal_combobox;

    @FXML
    private ComboBox<String> thirdChoice_dorm_combobox;

    @FXML
    private ComboBox<String> thirdChoice_period_combobox;

    @FXML
    private ComboBox<String> thirdChoice_meal_combobox;
    
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
		
		
		//TODO 테스트용, 콤보박스 아이템 추가. 나중에 클래스든, String이든, 네트워크에서 받아와 처리해야됨.
		//그리고, 요청받아올때 서버에서 남자인지 여자인지 알아내서 그에 맞는 생활관구분, 기간구분, 식사구분, 생활관비 가져와야댐.
		
		//남자라고 가정, 사실 남자인지 여자인지도 서버에서 알아서 판단해서 클라이언트에게 보냄. 클라이언트는 이게 남자껀지 여자껀지 몰라야함.
		//일반학기라고 가정, 클라이언트는 일반학기인지, 방학인지는 모름. 이것도 서버에서 알아서 판단해서 알아서 보내줌.
		oneYear_dorm_combobox.getItems().addAll("푸름관 1,2동");
		firstChoice_dorm_combobox.getItems().addAll("푸름관 1,2동", "푸름관 4동", "오름관 2,3동");
		secondChoice_dorm_combobox.getItems().addAll("푸름관 1,2동", "푸름관 4동", "오름관 2,3동");
		thirdChoice_dorm_combobox.getItems().addAll("푸름관 1,2동", "푸름관 4동", "오름관 2,3동");
		
		//이건 계절학기때는 전기간, 계절수업으로 나뉨
		oneYear_period_combobox.getItems().addAll("전기간");
		firstChoice_period_combobox.getItems().addAll("전기간");
		secondChoice_period_combobox.getItems().addAll("전기간");
		thirdChoice_period_combobox.getItems().addAll("전기간");
		
		//식사구분은 기숙사에따라서, 식사의무가 T인지 F인지에 따라 서버가 알아서 보내줘야함.
		oneYear_meal_combobox.getItems().addAll("5일식", "7일식", "식사안함");
		firstChoice_meal_combobox.getItems().addAll("5일식", "7일식", "식사안함");
		secondChoice_meal_combobox.getItems().addAll("5일식", "7일식", "식사안함");
		thirdChoice_meal_combobox.getItems().addAll("5일식", "7일식", "식사안함");
		
		//이건 사용자 선택에 따라 결정, 변경되야함. 물론 서버에서 기본 정보는 다 받아오고, 사용자 선택에 맞는 값을 표시해줘야함.
		oneYear_cost_label.setText("만원");
		firstChoice_cost_label.setText("만원");
		secondChoice_cost_label.setText("만원");
		thirdChoice_cost_label.setText("만원");
		
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

//콤보박스 값이 변경되었을때 작동하는 리스너
//사용자가 선택한 콤보박스에 맞게 cost label에 금액이 찍혀야함.
//값이 비어있는 콤보박스가 있으면 안띄워야함.
//서버로부터 사실상 성별로만 구분한 
class ComboboxChangeListener implements ChangeListener<String>
{
	ComboBox<String> dorm; 
	ComboBox<String> period;
	ComboBox<String> meal;
	Label cost;
	
	public ComboboxChangeListener(ComboBox<String> dorm, ComboBox<String> period, ComboBox<String> meal, Label cost)
	{
		
	}
	
	public ComboboxChangeListener(Label cost)
	{
		this.cost = cost;
	}
	
	@Override
	public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
	{
		
	}
	
}