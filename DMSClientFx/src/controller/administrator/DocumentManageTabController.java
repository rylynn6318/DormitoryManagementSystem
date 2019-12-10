package controller.administrator;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

import javax.swing.filechooser.FileNameExtensionFilter;

import application.IOHandler;
import application.Responser;
import controller.InnerPageController;
import enums.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import models.*;
import tableViewModel.*;
import utils.Protocol;
import utils.ProtocolHelper;

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
	
	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		System.out.println("서류 조회 및 제출 새로고침됨");
		
		update_isValid_combobox.getItems().addAll(comboboxItem_boolean);
		
		//네트워킹해서 서류유형 콤보박스 아이템 받아와라.
		onEnter();
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
    
    private void onEnter()
    {
    	ArrayList<Code1.FileType> resultList = Responser.admin_documentManagePage_onEnter();
    	
    	//서버랑 통신이 됬는가?
        if(resultList == null)
        {
        	IOHandler.getInstance().showAlert("서버에 연결할 수 없습니다.");
        	if(!IOHandler.getInstance().showDialog("디버그", "계속 진행하시겠습니까?"))
        	{
        		//여기서 페이지 닫게 해주자.
        		close();
        		return;
        	}
        }
        
        if(resultList != null)
        {
        	setComboboxByFileType(delete_documentType_combobox, resultList);
        	setComboboxByFileType(upload_documentType_combobox, resultList);
        	setComboboxByFileType(update_documentType_combobox, resultList);
        }
    }
    
    //-----------------------------------------------------------------
    
    private void checkDocuments()
    {
    	//서버에서 신청테이블->이번학기->객체 배열 쫙 긁어와서 tableview에 보여줌
    	Serializable result = Responser.admin_documentManagePage_onCheck();
    	
    	//서버랑 통신이 됬는가?
        if(result == null)
        {
        	IOHandler.getInstance().showAlert("서버에 연결할 수 없습니다.");
        	return;
        }
        
        Tuple<Bool, String> checkTuple = (Tuple<Bool, String>) result;
        
        if(checkTuple.obj1 == Bool.FALSE)
        {
        	IOHandler.getInstance().showAlert(checkTuple.obj2);
        	return;
        }
        
        
        Tuple<Bool, ArrayList<Document>> resultTuple = (Tuple<Bool, ArrayList<Document>>) result;
        ArrayList<Document> documentList = resultTuple.obj2;
        
        if(documentList != null)
        {
        	//객체를 테이블뷰 모델로 변환
        	ObservableList<DocumentViewModel> documentModels = FXCollections.observableArrayList();
        	
        	for(Document document : documentList)
        	{
        		documentModels.add(documentToViewModel(document));
        	}
        	
            //테이블뷰에 추가
        	setDocumentTableView(documentModels);
        }
    }
    
    private void setDocumentTableView(ObservableList<DocumentViewModel> documentList)
    {
    	//서버에서 받아온거 표시하게 만듬.
    	check_document_column_studentId.setCellValueFactory(cellData -> cellData.getValue().studentIdProperty());
    	check_document_column_docType.setCellValueFactory(cellData -> cellData.getValue().documentTypeProperty());
    	check_document_column_submissionDate.setCellValueFactory(cellData -> cellData.getValue().submissionDateProperty());
    	check_document_column_diagnosisDate.setCellValueFactory(cellData -> cellData.getValue().diagnosisDateProperty());
    	check_document_column_docStoragePath.setCellValueFactory(cellData -> cellData.getValue().documentStoragePathProperty());
    	check_document_column_isValid.setCellValueFactory(cellData -> cellData.getValue().isValidProperty());
    	
    	check_document_tableview.setItems(documentList);
    	documentTableviewSetEvent(check_document_tableview);
    }
    
    private void documentTableviewSetEvent(TableView<DocumentViewModel> check_document_tableview)
    {
    	check_document_tableview.setOnMouseClicked(new EventHandler<MouseEvent>() 
    	{
    	     @Override
    	     public void handle(MouseEvent event) 
    	     {
    	          if(event.getClickCount() == 2) 
    	          {
    	        	  //더블클릭한 항목 잡음.
    	        	  DocumentViewModel documentViewModel = check_document_tableview.getSelectionModel().getSelectedItem();
    	        	  if(documentViewModel != null)
    	        		  downloadDocument(documentViewModel.document);
    	          }
    	     }
    	});
    }

    // 학생 쪽 코드랑 거의 같음
    private void downloadDocument(Document document)
    {
    	if (IOHandler.getInstance().showDialog("다운로드", "다운로드하시겠습니까?"))
        {
			Protocol result = null;
			try {
				result = Responser.student_checkDocumentPage_onDownlaod(document);
			} catch (Exception e) {
				e.printStackTrace();
			}

			//서버랑 통신이 됬는가?
			if(result == null) {
				IOHandler.getInstance().showAlert("서버에 연결할 수 없습니다.");
			}else if(result.code2 == Code2.FileCode.FAIL){
				try {
					IOHandler.getInstance().showAlert((String) ProtocolHelper.deserialization(result.getBody()));
				} catch (ClassNotFoundException | IOException e) {
					IOHandler.getInstance().showAlert("알 수 없는 이유로 다운로드에 실패했습니다.");
				}
			}else{
				Code1.FileType type = (Code1.FileType) result.code1;
				Path downloadpath = Paths.get(type.name(),document.studentId + type.extension);
				IOHandler.getInstance().write(downloadpath, result.getBody());
				IOHandler.getInstance().showAlert("\"" + downloadpath.toFile().getAbsolutePath() + "\" 에 저장되었습니다.");
			}
        }
    }
    
    //-----------------------------------------------------------------
    
    private void deleteDocument()
    {
    	String id = delete_id_textfield.getText();
    	String documentType = delete_documentType_combobox.getSelectionModel().getSelectedItem();
    	
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
    	
    	//서버에 삭제 쿼리 요청 후 성공/실패여부 메시지로 알려주자.
    	Document data = new Document(id, stringToFileType(documentType), null);
    	Tuple<Bool, String> resultTuple = Responser.admin_documentManagePage_onDelete(data);
    	
    	//서버랑 통신이 됬는가?
        if(resultTuple == null)
        {
        	IOHandler.getInstance().showAlert("서버에 연결할 수 없습니다.");
        	return;
        }
    	
    	//서버에 삭제 쿼리 요청 후 성공/실패여부 메시지로 알려주자.
        if(resultTuple != null)
        {
        	if(resultTuple.obj1 == Bool.TRUE)
        	{
        		clearDeleteInfo();
        		checkDocuments();
        	}
        	IOHandler.getInstance().showAlert(resultTuple.obj2);
        }
        

    }
    
    private void clearDeleteInfo()
    {
		//선택한 항목들 클리어
		delete_id_textfield.setText(null);
		delete_documentType_combobox.getSelectionModel().select(-1);
    }
    
    //-----------------------------------------------------------------
    
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
		if (file.exists()) {
			//TODO 여기서 파일전송해라!!!! 파일전송하는 프로토콜
			try {
				if (Responser.admin_documentManagePage_onUpload(id, stringToFileType(documentType), file).bool) {
					IOHandler.getInstance().showAlert("서류가 제출되었습니다.");
					clearUploadInfo();
				} else IOHandler.getInstance().showAlert("서버 측 오류로 파일 업로드에 실패했습니다.");
			} catch (Exception e) {
				IOHandler.getInstance().showAlert("파일 업로드에 실패했습니다.");
				e.printStackTrace();
			}
		} else {
			IOHandler.getInstance().showAlert("파일이 존재하지 않습니다!");
		}
	
    }
    
    private void clearUploadInfo()
    {
    	//성공했으면 입력한 값 비워준다.
		upload_id_textfield.setText("");
		upload_documentType_combobox.getSelectionModel().select(-1);
		upload_fileDirectory_label.setText("N/A");;
    }
    
    //-----------------------------------------------------------------
    
    private void updateValid()
    {
    	String id = update_id_textfield.getText();
    	String documentType = update_documentType_combobox.getSelectionModel().getSelectedItem();
    	LocalDate submitDate_l = update_submitDate_datepicker.getValue();
    	LocalDate diagnosisDate_l = update_diagnosisDate_datepicker.getValue();
    	String isValidStr = update_isValid_combobox.getSelectionModel().getSelectedItem();
    	
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
    	else if(submitDate_l == null || submitDate_l.toString().equals(""))
    	{
    		//제출일이 없음
    		IOHandler.getInstance().showAlert("제출일이 비어있습니다.");
    		return;
    	}
    	else if(isValidStr == null || isValidStr.toString().equals(""))
    	{
    		//유효여부가 없음
    		IOHandler.getInstance().showAlert("유효여부가 비어있습니다.");
    		return;
    	}
    	
    	//결핵진단서인지 체크해라.
    	if(documentType.equals("결핵진단서"))
    	{
    		if(diagnosisDate_l == null || diagnosisDate_l.toString().equals(""))
    		{
    			IOHandler.getInstance().showAlert("진단일이 비어있습니다.");
        		return;
    		}
    	}
    	
    	Date submitDate = localDateToDate(submitDate_l);
    	Date diagnosisDate = localDateToDate(diagnosisDate_l);
    	Bool isValid = isValidStr.equals("T") ? Bool.TRUE : Bool.FALSE;
    	
    	Document data = new Document(id, stringToFileType(documentType), submitDate, diagnosisDate, null, isValid);
    	Tuple<Bool, String> resultTuple = Responser.admin_documentManagePage_onUpdate(data);
    	
    	//서버랑 통신이 됬는가?
        if(resultTuple == null)
        {
        	IOHandler.getInstance().showAlert("서버에 연결할 수 없습니다.");
        	return;
        }
    	
    	//서버에 삭제 쿼리 요청 후 성공/실패여부 메시지로 알려주자.
        if(resultTuple != null)
        {
        	if(resultTuple.obj1 == Bool.TRUE)
        	{
        		clearUpdateInfo();	
        		checkDocuments();
        	}
        	IOHandler.getInstance().showAlert(resultTuple.obj2);
        }
    }
    
    private void clearUpdateInfo()
	{
		//선택한 항목들 클리어
		update_id_textfield.setText(null);
		update_documentType_combobox.getSelectionModel().select(-1);
		update_submitDate_datepicker.setValue(null);
		update_diagnosisDate_datepicker.setValue(null);
		update_isValid_combobox.getSelectionModel().select(-1);
	}
    
}
