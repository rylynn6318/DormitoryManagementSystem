package controller.student;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

import application.IOHandler;
import application.Responser;
import controller.InnerPageController;
import enums.Bool;
import enums.Code1;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import models.Document;
import models.Tuple;

public class CheckDocumentTabController extends InnerPageController 
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
    
    private Code1.FileType selectedFileType = null;

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		System.out.println("서류 조회 새로고침됨");
		
		//TODO 테스트용, 서류 콤보박스 추가. 나중에 서류구분 ENUM을 쓰든 String을 쓰든, 네트워크에서 받아와 처리해야됨.
		setCombobox();
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
    
    //처음 들어오면 서류유형 콤보박스를 통신해서 채움
    private void setCombobox()
	{
    	//서버로부터 FileType enum이 담긴 ArrayList를 받는다.
    	ArrayList<Code1.FileType> resultList = Responser.student_checkDocumentPage_onEnter();
		
		//서버랑 통신이 됬는가?
        if(resultList == null)
        {
        	IOHandler.getInstance().showAlert("서버에 연결할 수 없습니다.");
        	if(!IOHandler.getInstance().showDialog("디버그", "계속 진행하시겠습니까?"))
        	{
        		close();
        		return;
        	}
        }
        
        if(resultList != null)
        {
        	//서버에게서 받아온 파일타입 목록을 콤보박스에 추가한다.
        	ArrayList<String> fileTypeList = new ArrayList<String>();
        	for(Code1.FileType fileType : resultList)
        	{
        		String fileTypeStr = fileTypeToString(fileType);
        		fileTypeList.add(fileTypeStr);
        	}
    		document_type_combobox.getItems().addAll(fileTypeList);
        }
	}
    
    //조회 버튼 클릭 시 동작함.
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
		//어느 유형 조회한다고 알려주기위해 콤보박스로 선택한 fileType을 전송(+학번 전달을 위한 Account)
		Code1.FileType fileType = stringToFileType(comboboxItem);
		Serializable result = Responser.student_checkDocumentPage_onCheck(fileType);
		
		//서버랑 통신이 됬는가?
        if(result == null)
        {
        	IOHandler.getInstance().showAlert("서버에 연결할 수 없습니다.");
        	return;
        }
        
        //오류 메시지가 반환됬는지, 문서가 반환됬는지 체크해봄.
        Tuple<Bool, String> checkTuple = (Tuple<Bool, String>) result;
        
    	if(checkTuple.obj1 == Bool.FALSE)
    	{
    		IOHandler.getInstance().showAlert(checkTuple.obj2);
    		return;
    	}
        
    	//문서가 반환된듯 하다.
    	Tuple<Bool, Document> resultTuple = (Tuple<Bool, Document>) result;
        
        //파일 조회됨.
        selectedFileType = fileType;		//현재 조회된 파일의 타입도 저장해둔다. 파일 다운로드 시 파일유형을 알려줘야하기때문.
        setLabels(resultTuple.obj2);
    }
    
    //제출일시, 진단일시, 파일경로를 설정함.
    private void setLabels(Document document)
    {
    	String fileDirectory = document.documentStoragePath;
    	Date submitDate = document.submissionDate;
    	Date diagnosisDate = document.diagnosisDate;
    	
    	file_directory_label.setText(fileDirectory);
    	if(submitDate != null)
    		submit_date_label.setText(submitDate.toString());
    	
    	if(diagnosisDate != null)
    		diagnosis_date_label.setText(diagnosisDate.toString());
    }
    
    //다운로드 클릭 시 호출됨
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
    	File result = Responser.student_checkDocumentPage_onDownlaod(selectedFileType);
    	
    	//서버랑 통신이 됬는가?
    	if(result == null)
    	{
    		IOHandler.getInstance().showAlert("서버에 연결할 수 없습니다.");
        	return;
    	}
    	
    	copyFile(result);
    }
    
    //서버로부터 받은 파일을 바탕화면에 저장한다
    private void copyFile(File orginal)
    {
    	String userPath = System.getProperty("user.home");
		String fileSavePath = userPath + "\\Desktop\\" + "서류.jpg";
		
		File copiedFile = new File(fileSavePath);
		
		try
		{
			FileInputStream fis = new FileInputStream(orginal);
			FileOutputStream fos = new FileOutputStream(copiedFile);
			
			int fileByte = 0;
			
			while((fileByte = fis.read()) != -1)
			{
				fos.write(fileByte);
			}
			
			fis.close();
			fos.close();
			
			IOHandler.getInstance().showAlert("바탕화면에 서류.jpg로 저장되었습니다.");
		}
		catch(FileNotFoundException e)
		{
			System.out.println("파일을 복사하는 도중 파일을 찾을 수 없음");
		}
		catch(IOException e)
		{
			System.out.println("파일을 복사하는 도중 입출력 오류 발생");
		}
		catch(Exception e)
		{
			System.out.println("파일을 복사하는 도중 알 수 없는 오류 발생");
			e.printStackTrace();
		}
    }
    
}
