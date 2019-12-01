package controller.administrator;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javax.swing.filechooser.FileNameExtensionFilter;

import application.IOHandler;
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
		//네트워크 통신해서 신청테이블->이번학기->객체 배열 가져와서 보여줘라.
    }

    @FXML
    void on_update_button_actioned(ActionEvent event) 
    {
    	System.out.println("납부 여부 조회 및 관리 : UPDATE 클릭됨");
    	updatePayment();
    }
    
    @FXML
    void on_file_select_button_actioned(ActionEvent event) 
    {
    	System.out.println("납부 여부 조회 및 관리 : 파일 선택 클릭됨");
    	selectFile();
    }

    @FXML
    void on_upload_button_actioned(ActionEvent event) 
    {
    	System.out.println("납부 여부 조회 및 관리 : 업로드 클릭됨");
    	uploadPaymentFile();
    }
    
    
	
	//---------------------로직---------------------
    
    private void updatePayment()
    {
    	String id = update_id_textfield.getText();
    	String dormName = update_dormName_textfield.getText();
    	String gender = update_gender_combobox.getSelectionModel().getSelectedItem();
    	String semester = update_semester_textfield.getText();
    	String isPaid = update_isPaid_combobox.getSelectionModel().getSelectedItem();
    	
    	if(id == null || id.isEmpty())
    	{
    		//학번 비어있음
    		IOHandler.getInstance().showAlert("학번이 비어있습니다.");
    		return;
    	}
    	else if(dormName == null || dormName.isEmpty())
    	{
    		//생활관명 비어있음
    		IOHandler.getInstance().showAlert("생활관명이 비어있습니다.");
    		return;
    	}
    	else if(gender == null || gender.isEmpty())
    	{
    		//성별 비어있음
    		IOHandler.getInstance().showAlert("성별이 비어있습니다.");
    		return;
    	}
    	else if(semester == null || semester.isEmpty())
    	{
    		//학기가 없음
    		IOHandler.getInstance().showAlert("학기가 비어있습니다.");
    		return;
    	}
    	else if(isPaid == null || isPaid.isEmpty())
    	{
    		//성별 비어있음
    		IOHandler.getInstance().showAlert("납부여부가 비어있습니다.");
    		return;
    	}
    	
    	//서버에 Update 쿼리 요청 후 성공/실패여부 메시지로 알려주자.
		boolean isSucceed = true;
		if(isSucceed)
		{
			IOHandler.getInstance().showAlert("납부여부 갱신에 성공하였습니다.");
			
			//선택한 항목들 클리어
			update_id_textfield.setText(null);
			update_dormName_textfield.setText(null);
			update_gender_combobox.getSelectionModel().select(-1);
			update_semester_textfield.setText(null);
			update_isPaid_combobox.getSelectionModel().select(-1);
		}
		else
		{
			IOHandler.getInstance().showAlert("납부여부 갱신에 실패하였습니다.");
		}
    }
    
    private void selectFile()
    {
    	String selectedFileDirectory, currentFileDirectory;
    	FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV", "csv");		//csv만 선택할 수 있게 함. 필터 앞 인수는 설명, 뒷 인수는 확장자
    	currentFileDirectory = file_directory_label.getText();							//이전에 파일 선택했었으면, 다시 파일선택 시 그 폴더에서 보여주게하기위함.
    	
    	selectedFileDirectory =	IOHandler.getInstance().selectFile(currentFileDirectory, filter);	//파일선택
    	
    	if(selectedFileDirectory != null)
    		file_directory_label.setText(selectedFileDirectory);						//파일이 선택됬으면 label에 경로 저장
    }
    
    private void uploadPaymentFile()
    {
    	String fileDirectory = file_directory_label.getText();
    	
    	if(fileDirectory == null || fileDirectory.equals("N/A"))
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
			boolean isSucceed = true;
			if(isSucceed)
			{
				IOHandler.getInstance().showAlert("은행 파일이 업로드 되었습니다.");
				file_directory_label.setText("N/A");
			}
			else
			{
				IOHandler.getInstance().showAlert("은행 파일 업로드에 실패하였습니다.");
			}
			    			
		}
		else
		{
			IOHandler.getInstance().showAlert("파일이 존재하지 않습니다!");
		}
    }

}
