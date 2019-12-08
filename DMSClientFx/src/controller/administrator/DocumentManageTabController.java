package controller.administrator;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;

import javax.swing.filechooser.FileNameExtensionFilter;

import application.IOHandler;
import controller.InnerPageController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import tableViewModel.*;

//서류 조회 및 제출
public class DocumentManageTabController extends InnerPageController 
{
	@FXML
    private Button check_button;

    @FXML
    private TableView<DocumentViewModel> check_document_tableview;
    
    @FXML
    private TableColumn<DocumentViewModel, String> check_document_column_studentId;

    @FXML
    private TableColumn<DocumentViewModel, String> check_document_column_docType;

    @FXML
    private TableColumn<DocumentViewModel, String> check_document_column_submissionDate;

    @FXML
    private TableColumn<DocumentViewModel, String> check_document_column_diagnosisDate;

    @FXML
    private TableColumn<DocumentViewModel, String> check_document_column_docStoragePath;

    @FXML
    private TableColumn<DocumentViewModel, String> check_document_column_isValid;

    @FXML
    private Button delete_button;

    @FXML
    private TextField delete_id_textfield;

    @FXML
    private ComboBox<String> delete_documentType_combobox;

    @FXML
    private DatePicker delete_date_datepicker;

    @FXML
    private Button select_file_button;

    @FXML
    private Button upload_button;

    @FXML
    private ComboBox<String> upload_documentType_combobox;

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
    private ComboBox<String> update_documentType_combobox;

    @FXML
    private DatePicker update_submitDate_datepicker;
    
	private final String[] comboboxItem_boolean = {null, "T", "F"};
	
	private ObservableList<DocumentViewModel> documentList;
	
	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		System.out.println("서류 조회 및 제출 새로고침됨");
		
		update_isValid_combobox.getItems().addAll(comboboxItem_boolean);
		
		//네트워킹해서 서류유형 콤보박스 아이템 받아와라.
		delete_documentType_combobox.getItems().addAll("결핵진단서", "서약서");
		upload_documentType_combobox.getItems().addAll("결핵진단서", "서약서");
		update_documentType_combobox.getItems().addAll("결핵진단서", "서약서");
	}
	
	//---------------------이벤트---------------------
	
	@FXML
    void on_check_button_actioned(ActionEvent event) 
	{
		System.out.println("서류 조회 및 제출 : 조회 클릭됨");
		//네트워킹해서 받은 객체배열 역직렬화->tableview에 뿌려라
		checkDocuments();
    }

    @FXML
    void on_delete_button_actioned(ActionEvent event) 
    {
    	System.out.println("서류 조회 및 제출 : 삭제 클릭됨");
    	deleteDocument();
    }

    @FXML
    void on_select_file_button_actioned(ActionEvent event) 
    {
    	System.out.println("서류 조회 및 제출 : 파일 선택 클릭됨");
    	selectFile();
    }
    
    @FXML
    void on_upload_button_actioned(ActionEvent event) 
    {
    	System.out.println("서류 조회 및 제출 : 업로드 클릭됨");
    	uploadFile();
    }

    @FXML
    void on_update_button_actioned(ActionEvent event) 
    {
    	System.out.println("서류 조회 및 제출 : UPDATE 클릭됨");
    	updateValid();
    }
    
    //---------------------로직---------------------
    
    private void checkDocuments()
    {
    	//네트워킹으로 서류 조회
    	documentList = FXCollections.observableArrayList(
    			new DocumentViewModel("20161234", 0, new Date(2019,7,1), new Date(2019,8,1), "C:\\와샌즈1", true),
    			new DocumentViewModel("20161235", 1, new Date(2019,7,1), new Date(2019,8,1), "C:\\와샌즈2", false),
    			new DocumentViewModel("20161236", 2, new Date(2019,7,1), new Date(2019,8,1), "C:\\와샌즈3", true),
    			new DocumentViewModel("20161236", 3, new Date(2019,7,1), new Date(2019,8,1), "C:\\와샌즈4", false)
        		);
    	
    	//서버에서 받아온거 표시하게 만듬.
    	check_document_column_studentId.setCellValueFactory(cellData -> cellData.getValue().studentIdProperty());
    	check_document_column_docType.setCellValueFactory(cellData -> cellData.getValue().documentTypeProperty());
    	check_document_column_submissionDate.setCellValueFactory(cellData -> cellData.getValue().submissionDateProperty());
    	check_document_column_diagnosisDate.setCellValueFactory(cellData -> cellData.getValue().diagnosisDateProperty());
    	check_document_column_docStoragePath.setCellValueFactory(cellData -> cellData.getValue().documentStoragePathProperty());
    	check_document_column_isValid.setCellValueFactory(cellData -> cellData.getValue().isValidProperty());
    	
    	check_document_tableview.setItems(documentList);
    }
    
    private void deleteDocument()
    {
    	String id = delete_id_textfield.getText();
    	String documentType = delete_documentType_combobox.getSelectionModel().getSelectedItem();
    	LocalDate submitDate = delete_date_datepicker.getValue();
    	
    	if(id == null || id.isEmpty())
    	{
    		//학번 비어있음
    		IOHandler.getInstance().showAlert("학번이 비어있습니다.");
    		return;
    	}
    	else if(documentType == null || documentType.isEmpty())
    	{
    		//서류유형 비어있음
    		IOHandler.getInstance().showAlert("서류유형이 비어있습니다.");
    		return;
    	}
    	else if(submitDate == null || submitDate.toString().equals(""))
    	{
    		//제출일이 없음
    		IOHandler.getInstance().showAlert("제출일이 비어있습니다.");
    		return;
    	}
    	
    	//서버에 삭제 쿼리 요청 후 성공/실패여부 메시지로 알려주자.
		boolean isSucceed = true;
		if(isSucceed)
		{
			IOHandler.getInstance().showAlert("서류 삭제에 성공하였습니다.");
			
			//선택한 항목들 클리어
			delete_id_textfield.setText(null);
			delete_documentType_combobox.getSelectionModel().select(-1);
			delete_date_datepicker.setValue(null);
		}
		else
		{
			IOHandler.getInstance().showAlert("서류 삭제에 실패하였습니다.");
		}
    }
    
    private void selectFile()
    {
    	String selectedFileDirectory, currentFileDirectory;
    	FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG", "jpg");		//jpg만 선택할 수 있게 함. 필터 앞 인수는 설명, 뒷 인수는 확장자
    	currentFileDirectory = upload_fileDirectory_label.getText();					//이전에 파일 선택했었으면, 다시 파일선택 시 그 폴더에서 보여주게하기위함.
    	
    	selectedFileDirectory =	IOHandler.getInstance().selectFile(currentFileDirectory, filter);	//파일선택
    	
    	if(selectedFileDirectory != null)
    		upload_fileDirectory_label.setText(selectedFileDirectory);					//파일이 선택됬으면 label에 경로 저장
    }
    
    private void uploadFile()
    {
    	String id = upload_id_textfield.getText();
    	String documentType = upload_documentType_combobox.getSelectionModel().getSelectedItem();
    	String fileDirectory = upload_fileDirectory_label.getText();
    	
    	if(id == null || id.isEmpty())
    	{
    		//년도 비어있음
    		IOHandler.getInstance().showAlert("학번이 비어있습니다.");
    		return;
    	}
    	else if(documentType == null || documentType.isEmpty())
    	{
    		//서류유형 비어있음
    		IOHandler.getInstance().showAlert("서류유형이 비어있습니다.");
    		return;
    	}
    	else if(fileDirectory == null || fileDirectory.equals("N/A"))
    	{
    		//파일 경로가 없음
    		IOHandler.getInstance().showAlert("파일을 선택해주세요.");
    		return;
    	}
    	
		//전송
		
		//전송하기전에 파일이 존재하는지 체크
		File file = new File(fileDirectory);
		if(file.exists())
		{
			//파일전송하는 프로토콜
			IOHandler.getInstance().showAlert("서류가 제출되었습니다.");
			
			boolean isSucceed = true;
			if(isSucceed)
			{
				//성공했으면 입력한 값 비워준다.
				upload_id_textfield.setText("");
				upload_documentType_combobox.getSelectionModel().select(-1);
				upload_fileDirectory_label.setText("N/A");;
			}
			else
			{
				//실패했으면
				IOHandler.getInstance().showAlert("파일 업로드에 실패했습니다.");
			}
			
		}
		else
		{
			IOHandler.getInstance().showAlert("파일이 존재하지 않습니다!");
		}
	
    }
    
    private void updateValid()
    {
    	String id = update_id_textfield.getText();
    	String documentType = update_documentType_combobox.getSelectionModel().getSelectedItem();
    	LocalDate submitDate = update_submitDate_datepicker.getValue();
    	LocalDate diagnosisDate = update_diagnosisDate_datepicker.getValue();
    	String isValid = update_isValid_combobox.getSelectionModel().getSelectedItem();
    	
    	if(id == null || id.isEmpty())
    	{
    		//학번 비어있음
    		IOHandler.getInstance().showAlert("학번이 비어있습니다.");
    		return;
    	}
    	else if(documentType == null || documentType.isEmpty())
    	{
    		//서류유형 비어있음
    		IOHandler.getInstance().showAlert("서류유형이 비어있습니다.");
    		return;
    	}
    	else if(submitDate == null || submitDate.toString().equals(""))
    	{
    		//제출일이 없음
    		IOHandler.getInstance().showAlert("제출일이 비어있습니다.");
    		return;
    	}
    	else if(isValid == null || isValid.toString().equals(""))
    	{
    		//유효여부가 없음
    		IOHandler.getInstance().showAlert("유효여부가 비어있습니다.");
    		return;
    	}
    	
    	//결핵진단서인지 체크해라. 나중에 enum으로 쓰면 비교하는것도 고쳐주셈.
    	if(documentType.equals("결핵진단서"))
    	{
    		if(diagnosisDate == null || diagnosisDate.toString().equals(""))
    		{
    			IOHandler.getInstance().showAlert("진단일이 비어있습니다.");
        		return;
    		}
    	}
    	
    	//서버에 삭제 쿼리 요청 후 성공/실패여부 메시지로 알려주자.
		boolean isSucceed = true;
		if(isSucceed)
		{
			IOHandler.getInstance().showAlert("유효여부 갱신에 성공하였습니다.");
			
			//선택한 항목들 클리어
			update_id_textfield.setText(null);
			update_documentType_combobox.getSelectionModel().select(-1);
			update_submitDate_datepicker.setValue(null);
			update_diagnosisDate_datepicker.setValue(null);
			update_isValid_combobox.getSelectionModel().select(-1);
		}
		else
		{
			IOHandler.getInstance().showAlert("유효여부 갱신에 실패하였습니다.");
		}
    }
    
}
