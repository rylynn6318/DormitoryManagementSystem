package controller;

import java.net.URL;
import java.util.ResourceBundle;

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

}
