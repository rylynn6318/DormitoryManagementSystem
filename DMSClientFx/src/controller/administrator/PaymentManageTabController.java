package controller.administrator;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

//납부 여부 조회 및 관리
public class PaymentManageTabController implements Initializable 
{
	
	@FXML
    private Button check_button;

    @FXML
    private TableView<?> check_application_tableview;

    @FXML
    private Button update_button;

    @FXML
    private TextField update_id_textfield;

    @FXML
    private TextField update_dormName_textfield;

    @FXML
    private TextField update_semester_textfield;

    @FXML
    private Label file_directory_label;

    @FXML
    private Button file_select_button;

    @FXML
    private Button upload_button;

    @FXML
    private ComboBox<String> update_gender_combobox;
	
	@FXML
    private ComboBox<String> update_isPaid_combobox;
	
	private final String[] comboboxItem_gender = {"M", "F"};
	private final String[] comboboxItem_boolean = {"T", "F"};
	
	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		System.out.println("납부 여부 조회 및 관리 새로고침됨");
		
		update_gender_combobox.getItems().addAll(comboboxItem_gender);
		update_isPaid_combobox.getItems().addAll(comboboxItem_boolean);
	}
	
	//---------------------이벤트---------------------
	
	@FXML
    void on_check_button_actioned(ActionEvent event) 
	{
		System.out.println("납부 여부 조회 및 관리 : 조회 클릭됨");
    }

    @FXML
    void on_file_select_button_actioned(ActionEvent event) 
    {
    	System.out.println("납부 여부 조회 및 관리 : 파일 선택 클릭됨");
    }

    @FXML
    void on_update_button_actioned(ActionEvent event) 
    {
    	System.out.println("납부 여부 조회 및 관리 : UPDATE 클릭됨");
    }

    @FXML
    void on_upload_button_actioned(ActionEvent event) 
    {
    	System.out.println("납부 여부 조회 및 관리 : 업로드 클릭됨");
    }
    
    
	
	//---------------------로직---------------------

}
