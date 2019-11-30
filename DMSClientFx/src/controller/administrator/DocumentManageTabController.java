package controller.administrator;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

//서류 조회 및 제출
public class DocumentManageTabController implements Initializable 
{
	@FXML
    private Button check_button;

    @FXML
    private TableView<?> check_document_tableview;

    @FXML
    private Button delete_button;

    @FXML
    private TextField delete_id_textfield;

    @FXML
    private ComboBox<?> delete_documentType_combobox;

    @FXML
    private DatePicker delete_date_datepicker;

    @FXML
    private Button select_file_button;

    @FXML
    private Button upload_button;

    @FXML
    private DatePicker upload_date_datepicker;

    @FXML
    private ComboBox<?> upload_documentType_combobox;

    @FXML
    private TextField upload_id_textfield;

    @FXML
    private Label upload_fileDirectory_label;

    @FXML
    private TextField update_id_textfield;

    @FXML
    private ComboBox<String> update_isValid_combobox;

    @FXML
    private Button update_button;

    @FXML
    private DatePicker update_diagnosisDate_datepicker;

    @FXML
    private ComboBox<?> update_documentType_combobox;

    @FXML
    private DatePicker update_submitDate_datepicker;
	
	private final String[] comboboxItem_boolean = {"T", "F"};
	
	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		System.out.println("서류 조회 및 제출 새로고침됨");
		
		update_isValid_combobox.getItems().addAll(comboboxItem_boolean);
	}
	
	//---------------------이벤트---------------------
	
	@FXML
    void on_check_button_actioned(ActionEvent event) 
	{
		System.out.println("서류 조회 및 제출 : 조회 클릭됨");
    }

    @FXML
    void on_delete_button_actioned(ActionEvent event) 
    {
    	System.out.println("서류 조회 및 제출 : 삭제 클릭됨");
    }

    @FXML
    void on_select_file_button_actioned(ActionEvent event) 
    {
    	System.out.println("서류 조회 및 제출 : 파일 선택 클릭됨");
    }

    @FXML
    void on_update_button_actioned(ActionEvent event) 
    {
    	System.out.println("서류 조회 및 제출 : UPDATE 클릭됨");
    }

    @FXML
    void on_upload_button_actioned(ActionEvent event) 
    {
    	System.out.println("서류 조회 및 제출 : 업로드 클릭됨");
    }
    
  //---------------------로직---------------------

}
