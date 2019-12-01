package controller.administrator;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import application.IOHandler;
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
    	selectFile();
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
    
    private void selectFile()
    {
    	String selectedFileDirectory, currentFileDirectory;
    	FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG", "jpg");		//jpg만 선택할 수 있게 함. 필터 앞 인수는 설명, 뒷 인수는 확장자
    	currentFileDirectory = upload_fileDirectory_label.getText();					//이전에 파일 선택했었으면, 다시 파일선택 시 그 폴더에서 보여주게하기위함.
    	
    	selectedFileDirectory =	IOHandler.getInstance().selectFile(currentFileDirectory, filter);	//파일선택
    	
    	if(selectedFileDirectory != null)
    		upload_fileDirectory_label.setText(selectedFileDirectory);					//파일이 선택됬으면 label에 경로 저장
    }
    
}
