package controller.student;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;

import javax.swing.filechooser.FileNameExtensionFilter;

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
import javafx.scene.control.TextArea;
import models.Tuple;

public class SubmitDocumentTabController extends InnerPageController 
{
	@FXML
    private Button submit_button;

    @FXML
    private Label year_label;

    @FXML
    private ComboBox<String> document_type_combobox;

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
		
		//올해 설정
		int thisYear = Calendar.getInstance().get(Calendar.YEAR);
		year_label.setText(String.valueOf(thisYear));
		
		setCombobox();
	}
	
	//---------------------이벤트---------------------

    @FXML
    void on_submit_button_actioned(ActionEvent event) 
    {
    	System.out.println("서류 제출 : 제출 클릭됨");
    	submit();
    }
    
    @FXML
    void on_select_file_button_actioned(ActionEvent event) 
    {
		System.out.println("서류 제출 : 파일선택 클릭됨");
		selectFile();
    }
	
	//---------------------로직---------------------
    
    private void setCombobox()
	{
    	//서버로부터 FileType enum이 담긴 ArrayList를 받는다.
    	ArrayList<Code1.FileType> resultList = Responser.student_submitDocumentPage_onEnter();
		
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
        else
        {
        	//안내사항 표기
        	info_textarea.setText("서류는 10MB를 넘지 않는 jpg 이미지 파일로 올려주시기 바랍니다. 재제출시 파일은 덮어씌워지니 주의하시기 바랍니다.");	//이건 걍 통신안하기로 함
        	
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
    
    private void selectFile()
    {
    	String selectedFileDirectory, currentFileDirectory;
    	FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG", "jpg");		//jpg만 선택할 수 있게 함. 필터 앞 인수는 설명, 뒷 인수는 확장자
    	currentFileDirectory = file_directory_label.getText();							//이전에 파일 선택했었으면, 다시 파일선택 시 그 폴더에서 보여주게하기위함.
    	
    	selectedFileDirectory =	IOHandler.getInstance().selectFile(currentFileDirectory, filter);	//파일선택
    	
    	if(selectedFileDirectory != null)
    		file_directory_label.setText(selectedFileDirectory);						//파일이 선택됬으면 label에 경로 저장
    }
    
    private void submit()
    {
    	String year = year_label.getText();
    	String comboboxItem = document_type_combobox.getSelectionModel().getSelectedItem();
    	String fileDirectory = file_directory_label.getText();
    	
    	if(year.isEmpty() || year.equals("N/A"))
    	{
    		//년도 비어있음
    		IOHandler.getInstance().showAlert("제출년도가 비어있습니다.");
    		return;
    	}
    	else if(comboboxItem == null || comboboxItem.isEmpty())
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
			sendFile(comboboxItem, file);
			IOHandler.getInstance().showAlert("서류가 제출되었습니다.");    			
		}
		else
		{
			IOHandler.getInstance().showAlert("파일이 존재하지 않습니다!");
		}
    }
    
    private void sendFile(String selectedItemStr, File file)
    {
    	Code1.FileType fileType = stringToFileType(selectedItemStr);
    	
    	//TODO : 손, 이거 file이 Serializable 되는지 에러 안뜨는데, 확인바람 -명근
		// TODO 성공 실패만 반환하게 함.
		Bool result = null;
		try {
			result = Responser.student_submitDocumentPage_onSubmit(fileType, file);

			if(result.bool)
				IOHandler.getInstance().showAlert("파일 전송 완료");
			else
				IOHandler.getInstance().showAlert("파일 전송 실패");
		} catch (Exception e) {
			IOHandler.getInstance().showAlert("서버에 연결할 수 없습니다.");
			e.printStackTrace();
		}
    }

}
