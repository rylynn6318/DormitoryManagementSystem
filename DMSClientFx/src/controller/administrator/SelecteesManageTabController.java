package controller.administrator;

import java.net.URL;
import java.util.ResourceBundle;

import application.IOHandler;
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
    	selection();
    }
	
	@FXML
    void on_check_application_button_actioned(ActionEvent event) 
	{
		System.out.println("입사 선발자 조회 및 관리 : 조회 클릭됨");
		checkApplications();
    }

    @FXML
    void on_delete_button_clicked(ActionEvent event) 
    {
    	System.out.println("입사 선발자 조회 및 관리 : 삭제 클릭됨");
    	deleteSelectee();
    }

	//---------------------로직---------------------

    private void selection()
    {
    	//서버에 입사자 선발 쿼리 요청
    }
    
    private void checkApplications()
    {
    	//서버에서 신청테이블->이번학기->객체 배열 쫙 긁어와서 tableview에 보여줌
    }
    
    private void deleteSelectee()
    {
    	String id = delete_id_textfield.getText();
    	String dormName = delete_dormName_textfield.getText();
    	String gender = delete_gender_combobox.getSelectionModel().getSelectedItem();
    	String semester = delete_semester_textfield.getText();
    	String choice = delete_choice_textfield.getText();
    	
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
    	else if(choice == null || choice.isEmpty())
    	{
    		//지망이 비어있음
    		IOHandler.getInstance().showAlert("지망이 비어있습니다.");
    		return;
    	}
    	
    	//서버에 Update 쿼리 요청 후 성공/실패여부 메시지로 알려주자.
		boolean isSucceed = true;
		if(isSucceed)
		{
			IOHandler.getInstance().showAlert("입사 선발자 삭제에 성공하였습니다.");
			
			//선택한 항목들 클리어
			delete_id_textfield.setText(null);
			delete_dormName_textfield.setText(null);
			delete_gender_combobox.getSelectionModel().select(-1);
			delete_semester_textfield.setText(null);
			delete_choice_textfield.setText(null);
		}
		else
		{
			IOHandler.getInstance().showAlert("입사 선발자 삭제에 실패하였습니다.");
		}
    }
}
