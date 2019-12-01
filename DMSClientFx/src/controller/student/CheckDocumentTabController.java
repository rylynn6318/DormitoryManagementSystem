package controller.student;

import java.net.URL;
import java.util.ResourceBundle;

import application.IOHandler;
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
    private ComboBox<String> document_type_combobox;

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
		
		//TODO 테스트용, 서류 콤보박스 추가. 나중에 서류구분 ENUM을 쓰든 String을 쓰든, 네트워크에서 받아와 처리해야됨.
		document_type_combobox.getItems().addAll("결핵진단서", "서약서");
	}

	//---------------------이벤트---------------------
	
	@FXML
    void on_check_button_actioned(ActionEvent event) 
    {
		System.out.println("서류 조회 : 조회 클릭됨");
		checkDocument();
    }

    @FXML
    void on_download_button_actioned(ActionEvent event) 
    {
    	System.out.println("서류 조회 : 다운로드 클릭됨");
    	downloadDocument();
    }
	
	//---------------------로직---------------------
    
    private void checkDocument()
    {
		String comboboxItem = document_type_combobox.getSelectionModel().getSelectedItem();
		
		if(comboboxItem == null || comboboxItem.isEmpty())
    	{
    		//서류유형 비어있음
    		IOHandler.getInstance().showAlert("서류유형이 비어있습니다.");
    		return;
    	}
		
		//TODO 네트워킹해서 파일 조회해라.
		//제출일시, 진단일시(결핵진단서만), 파일명 가져와서 표시해야됨.
    }
    
    private void downloadDocument()
    {
    	String fileDirectory = file_directory_label.getText();
    	
    	//파일이 조회됬는지 체크함. 조회되었으면 파일경로 혹은 파일명이 표시됨.
    	if(fileDirectory == null || fileDirectory.equals("N/A") || fileDirectory.isEmpty())
    	{
    		IOHandler.getInstance().showAlert("파일경로가 비어있습니다.");
    		return;
    	}
    	
    	//TODO 네트워킹해서 파일 다운로드해라.
    	//저장위치는 대충 바탕화면에 저장하셈.
    }
}
