package controller.administrator;

import java.io.File;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.filechooser.FileNameExtensionFilter;

import application.IOHandler;
import application.Responser;
import controller.InnerPageController;
import enums.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import models.*;
import tableViewModel.*;

//납부 여부 조회 및 관리
public class PaymentManageTabController extends InnerPageController 
{
	
	@FXML
    private Button check_button;

    @FXML
    private TableView<ApplicationViewModel> check_application_tableview;
    
    @FXML
    private TableColumn<ApplicationViewModel, String> check_application_column_id;

    @FXML
    private TableColumn<ApplicationViewModel, String> check_application_column_dormName;
    
    @FXML
    private TableColumn<ApplicationViewModel, String> check_application_column_gender;

    @FXML
    private TableColumn<ApplicationViewModel, String> check_application_column_semester;

    @FXML
    private TableColumn<ApplicationViewModel, String> check_application_column_choice;

    @FXML
    private TableColumn<ApplicationViewModel, String> check_application_column_mealType;

    @FXML
    private TableColumn<ApplicationViewModel, String> check_application_column_isPaid;

    @FXML
    private TableColumn<ApplicationViewModel, String> check_application_column_isPassed;

    @FXML
    private TableColumn<ApplicationViewModel, String> check_application_column_isLastPassed;

    @FXML
    private TableColumn<ApplicationViewModel, String> check_application_column_isSnore;

    @FXML
    private Button update_button;

    @FXML
    private TextField update_id_textfield;

    @FXML
    private TextField update_dormName_textfield;
    
    @FXML
    private TextField update_gender_textfield;

    @FXML
    private TextField update_semester_textfield;

    @FXML
    private Label file_directory_label;

    @FXML
    private Button file_select_button;

    @FXML
    private Button upload_button;
	
	@FXML
    private ComboBox<String> update_isPaid_combobox;
	
	ObservableList<ApplicationViewModel> applicationList;
	
	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		System.out.println("납부 여부 조회 및 관리 새로고침됨");
		
		update_isPaid_combobox.getItems().addAll(comboboxItem_boolean);
	}
	
	//---------------------이벤트---------------------
	
	@FXML
    void on_check_button_actioned(ActionEvent event) 
	{
		System.out.println("납부 여부 조회 및 관리 : 조회 클릭됨");
		//네트워크 통신해서 신청테이블->이번학기->객체 배열 가져와서 보여줘라.
		checkApplications();
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
    
    private void checkApplications()
    {
    	//서버에서 신청테이블->이번학기->객체 배열 쫙 긁어와서 tableview에 보여줌
    	Serializable result = Responser.admin_paymentManagePage_onCheck();
    	
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
        
        Tuple<Bool, ArrayList<Application>> resultTuple = (Tuple<Bool, ArrayList<Application>>) result;
        ArrayList<Application> applicationList = resultTuple.obj2;
        
        if(applicationList != null)
        {
        	//객체를 테이블뷰 모델로 변환
        	ObservableList<ApplicationViewModel> applicationModels = FXCollections.observableArrayList();
        	
        	for(Application application : applicationList)
        	{
        		applicationModels.add(applicationToViewModel(application));
        	}
        	
            //테이블뷰에 추가
        	setApplicationTableView(applicationModels);
        }
    }
    
    private void setApplicationTableView(ObservableList<ApplicationViewModel> applicationList)
    {
    	//서버에서 받아온거 표시하게 만듬.
    	check_application_column_id.setCellValueFactory(cellData -> cellData.getValue().studentIdProperty());
    	check_application_column_dormName.setCellValueFactory(cellData -> cellData.getValue().dormNameProperty());
    	check_application_column_gender.setCellValueFactory(cellData -> cellData.getValue().genderProperty());
    	check_application_column_semester.setCellValueFactory(cellData -> cellData.getValue().semesterProperty());
    	check_application_column_choice.setCellValueFactory(cellData -> cellData.getValue().choiceProperty());
    	check_application_column_mealType.setCellValueFactory(cellData -> cellData.getValue().mealTypeProperty());
    	check_application_column_isPaid.setCellValueFactory(cellData -> cellData.getValue().isPaidProperty());
    	check_application_column_isPassed.setCellValueFactory(cellData -> cellData.getValue().isPassedProperty());
    	check_application_column_isLastPassed.setCellValueFactory(cellData -> cellData.getValue().isLastPassedProperty());
    	check_application_column_isSnore.setCellValueFactory(cellData -> cellData.getValue().isSnoreProperty());
    	check_application_tableview.setItems(applicationList);
    }
    
    //-----------------------------------------------------------------
    
    private void updatePayment()
    {
    	String id = update_id_textfield.getText();
    	String dormName = update_dormName_textfield.getText();
    	String gender = update_gender_textfield.getText();
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
    		//성별이 없음
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
    		//납부여부 비어있음
    		IOHandler.getInstance().showAlert("납부여부가 비어있습니다.");
    		return;
    	}
    	
    	Application data = new Application(id, dormName, Integer.parseInt(semester), -1);
    	data.setGender(gender.charAt(0));
    	data.setPaid(isPaid.equals("T") ? Bool.TRUE : Bool.FALSE);
    	
    	Tuple<Bool, String> resultTuple = Responser.admin_paymentManagePage_onUpdate(data);
    	
    	//서버에 Update 쿼리 요청 후 성공/실패여부 메시지로 알려주자.
    	if(resultTuple == null)
		{
			IOHandler.getInstance().showAlert("서버에 연결할 수 없습니다.");
        	return;
		}
		
		if(resultTuple != null)
		{
			if(resultTuple.obj1 == Bool.TRUE)
			{
				//입력했던 항목들 클리어
				clearUpdateInfo();
				checkApplications();
			}
			
			//성공/실패 메시지 표시
			IOHandler.getInstance().showAlert(resultTuple.obj2);
		}
		
    }
    
    private void clearUpdateInfo()
    {
    	//선택한 항목들 클리어
		update_id_textfield.setText(null);
		update_dormName_textfield.setText(null);
		update_semester_textfield.setText(null);
		update_isPaid_combobox.getSelectionModel().select(-1);
    }
    
    //-----------------------------------------------------------------
    
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
				
				//TODO 여기서 전송해라!
				Responser.admin_paymentManagePage_onUpload();
				
				file_directory_label.setText("N/A");
				checkApplications();
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
