package controller;

import java.net.URL;
import java.util.ResourceBundle;

import enums.Code1;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

//Login, Main을 제외한 페이지가 공통적으로 가지는 메소드를 가짐.
public class InnerPageController implements Initializable 
{
	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		
	}
	
	public void close()
    {
    	TabPane mainTabPane = MainPageController._MainTabPane;
    	Tab currentTab = mainTabPane.getSelectionModel().getSelectedItem();
    	mainTabPane.getTabs().remove(currentTab);
    }

	
	public Code1.FileType stringToFileType(String str)
    {
    	switch(str)
    	{
    	case "결핵진단서":
    		return Code1.FileType.MEDICAL_REPORT;
    	case "서약서":
    		return Code1.FileType.OATH;
    	default:
    		System.out.println("알 수 없는 파일 유형입니다!");
    		return null;
    	}
    }
    
	public String fileTypeToString(Code1.FileType fileType)
    {
    	switch(fileType)
		{
		case MEDICAL_REPORT:
			return "결핵진단서";
		case OATH:
			return "서약서";
		default:
			System.out.println("알 수 없는 파일 유형입니다!");
			return null;
		}
    }
}
