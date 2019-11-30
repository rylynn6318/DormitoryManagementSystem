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

//입사 선발자 조회 및 관리
public class SelecteesManageTabController implements Initializable 
{
	@FXML
    private Button selection_button;

    @FXML
    private Button check_application_button;

    @FXML
    private TableView<?> check_application_tableview;

    @FXML
    private Button delete_button;

    @FXML
    private TextField delete_choice_textfield;

    @FXML
    private TextField delete_id_textfield;

    @FXML
    private TextField delete_dormName_textfield;

    @FXML
    private TextField delete_semester_textfield;
    
    @FXML
	private ComboBox<String> delete_gender_combobox;
	
	private final String[] comboboxItem_gender = {"M", "F"}; 
	
	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		System.out.println("입사 선발자 조회 및 관리 새로고침됨");
		
		delete_gender_combobox.getItems().addAll(comboboxItem_gender);
	}
	
	//---------------------이벤트---------------------
	
	@FXML
    void on_selection_button_actioned(ActionEvent event) 
    {
    	System.out.println("입사 선발자 조회 및 관리 : 선발 클릭됨");
    }
	
	@FXML
    void on_check_application_button_actioned(ActionEvent event) 
	{
		System.out.println("입사 선발자 조회 및 관리 : 조회 클릭됨");
    }

    @FXML
    void on_delete_button_clicked(ActionEvent event) 
    {
    	System.out.println("입사 선발자 조회 및 관리 : 삭제 클릭됨");
    }

	//---------------------로직---------------------

}
