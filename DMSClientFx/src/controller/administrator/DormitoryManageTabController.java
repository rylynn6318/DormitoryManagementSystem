package controller.administrator;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

//생활관 조회 및 관리
public class DormitoryManageTabController implements Initializable 
{
	@FXML
    private Button check_button;

    @FXML
    private TableView<?> check_dormitory_tableview;

    @FXML
    private Button delete_button;

    @FXML
    private TextField delete_dormName_textfield;

    @FXML
    private ComboBox<String> delete_gender_combobox;

    @FXML
    private TextField delete_semester_textfield;

    @FXML
    private Button insert_button;

    @FXML
    private TextField insert_dormName_textfield;

    @FXML
    private ComboBox<String> insert_gender_combobox;

    @FXML
    private TextField insert_semester_textfield;

    @FXML
    private TextField insert_capacity_textfield;

    @FXML
    private TextField insert_mealCost5_textfield;

    @FXML
    private TextField insert_mealCost7_textfield;

    @FXML
    private TextField insert_boradingFees_textfield;

    @FXML
    private ComboBox<?> insert_mealDuty_combobox;

    private final String[] comboboxItem_gender = {"M", "F"}; 
    
	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		System.out.println("생활관 조회 및 관리 새로고침됨");
		
		delete_gender_combobox.getItems().addAll(comboboxItem_gender);
		insert_gender_combobox.getItems().addAll(comboboxItem_gender);
	}
	
	//---------------------이벤트---------------------
	
	@FXML
    void on_check_button_actioned(ActionEvent event) 
    {
		System.out.println("생활관 조회 및 관리 : 조회 클릭됨");
    }

    @FXML
    void on_delete_button_actioned(ActionEvent event) 
    {
    	System.out.println("생활관 조회 및 관리 : 삭제 클릭됨");
    }

    @FXML
    void on_insert_button_actioned(ActionEvent event) 
    {
    	System.out.println("생활관 조회 및 관리 : 등록 클릭됨");
    }
	
	//---------------------로직---------------------

}
