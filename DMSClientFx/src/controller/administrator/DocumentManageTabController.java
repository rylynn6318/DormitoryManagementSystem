package controller.administrator;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

//서류 조회 및 제출
public class DocumentManageTabController implements Initializable 
{
	@FXML
    private ComboBox<String> update_isValid_combobox;
	
	private final String[] comboboxItem_boolean = {"T", "F"};
	
	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		System.out.println("서류 조회 및 제출 새로고침됨");
		
		update_isValid_combobox.getItems().addAll(comboboxItem_boolean);
		
	}

}
