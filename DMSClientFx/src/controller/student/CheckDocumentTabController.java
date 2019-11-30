package controller.student;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

public class CheckDocumentTabController implements Initializable 
{

	@FXML
    private Button check_button;

    @FXML
    private ComboBox<?> document_type_combobox;

    @FXML
    private Label submit_date_label;

    @FXML
    private Label diagnosis_date_label;

    @FXML
    private Label file_directory_label;

    @FXML
    private Button download_button;

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		System.out.println("서류 조회 새로고침됨");
	}

	//---------------------이벤트---------------------
	
	@FXML
    void on_check_button_actioned(ActionEvent event) 
    {
		System.out.println("서류 조회 : 조회 클릭됨");
    }

    @FXML
    void on_download_button_actioned(ActionEvent event) 
    {
    	System.out.println("서류 조회 : 다운로드 클릭됨");
    }
	
	//---------------------로직---------------------
}
