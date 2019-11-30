package controller.administrator;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

//생활관 조회 및 관리
public class DormitoryManageTabController implements Initializable 
{

	@FXML
    private ComboBox<String> delete_gender_combobox;

    @FXML
    private ComboBox<String> enroll_gender_combobox;
    
    private final String[] comboboxItem_gender = {"M", "F"}; 
    
	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		System.out.println("생활관 조회 및 관리 새로고침됨");
		
		delete_gender_combobox.getItems().addAll(comboboxItem_gender);
		enroll_gender_combobox.getItems().addAll(comboboxItem_gender);
	}

}
