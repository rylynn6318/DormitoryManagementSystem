package controller.student;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class SubmitDocumentTabController implements Initializable 
{
	@FXML
    private Button submit_button;

    @FXML
    private Label year_label;

    @FXML
    private ComboBox<?> document_type_combobox;

    @FXML
    private Button select_file_button;

    @FXML
    private Label file_directory_label;

    @FXML
    private TextArea info_textarea;


	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		System.out.println("서류 제출 새로고침됨");
		
		//네트워킹
		info_textarea.setText("서류는 10MB를 넘지 않는 jpg 이미지 파일로 올려주시기 바랍니다. 재제출시 파일은 덮어씌워지니 주의하시기 바랍니다.");
	}
	
	//---------------------이벤트---------------------

    @FXML
    void on_submit_button_actioned(ActionEvent event) 
    {
    	System.out.println("서류 제출 : 제출 클릭됨");
    }
    
    @FXML
    void on_select_file_button_actioned(ActionEvent event) 
    {
		System.out.println("서류 제출 : 파일선택 클릭됨");
    }
	
	//---------------------로직---------------------

}
