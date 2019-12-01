package controller.student;

import java.io.File;
import java.net.URL;
import java.util.Calendar;
import java.util.ResourceBundle;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import application.IOHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;

public class SubmitDocumentTabController implements Initializable 
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
    
    private final long MAXFILESIZE = 10485760;	//10MB = 10485760 byte(in 바이너리)


	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		System.out.println("서류 제출 새로고침됨");
		
		//올해 설정
		int thisYear = Calendar.getInstance().get(Calendar.YEAR);
		year_label.setText(String.valueOf(thisYear));
		
		//TODO 네트워킹
		info_textarea.setText("서류는 10MB를 넘지 않는 jpg 이미지 파일로 올려주시기 바랍니다. 재제출시 파일은 덮어씌워지니 주의하시기 바랍니다.");
		
		//TODO 테스트용, 서류 콤보박스 추가. 나중에 서류구분 ENUM을 쓰든 String을 쓰든, 네트워크에서 받아와 처리해야됨.
		document_type_combobox.getItems().addAll("결핵진단서", "서약서");
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
    
    private void selectFile()
    {
    	JFileChooser jfc;
    	
    	//이전에 파일을 열었다면, 그 파일이 있는 폴더에서 열 수 있게 함.
    	if(file_directory_label.getText().equals("N/A"))
    	{
    		jfc = new JFileChooser();
    	}
    	else
    	{
    		jfc = new JFileChooser(file_directory_label.getText());
    	}
    	
    	jfc.setFileFilter(new FileNameExtensionFilter("JPG", "jpg"));		//jpg만 선택할 수 있게 함. 필터 앞 인수는 설명, 뒷 인수는 확장자
    	
		int returnVal = jfc.showOpenDialog(null);
		if(returnVal == 0)
		{
			//파일선택 선택됨
			try
			{
				File file = jfc.getSelectedFile();
				long fileSize = file.length();
				
				if(fileSize <= MAXFILESIZE)
				{
					file_directory_label.setText(file.getPath());
				}
				else
				{
					IOHandler.getInstance().showAlert("파일의 크기가 10MB보다 큽니다.");
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			//파일선택 취소됨
			IOHandler.getInstance().showAlert("파일 선택이 취소되었습니다.");
		}
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
    	}
    	else if(comboboxItem == null || comboboxItem.isEmpty())
    	{
    		//제출서류구분 비어있음
    		IOHandler.getInstance().showAlert("제출서류구분이 비어있습니다.");
    	}
    	else if(fileDirectory == null || fileDirectory.equals("N/A"))
    	{
    		//파일 경로가 없음
    		IOHandler.getInstance().showAlert("파일을 선택해주세요.");
    	}
    	else
    	{
    		//전송
    		
    		//전송하기전에 파일이 존재하는지 체크
    		File file = new File(fileDirectory);
    		if(file.exists())
    		{
    			//파일전송하는 프로토콜
    			IOHandler.getInstance().showAlert("서류가 제출되었습니다.");    			
    		}
    		else
    		{
    			IOHandler.getInstance().showAlert("파일이 존재하지 않습니다!");
    		}
    	}
    }

}
