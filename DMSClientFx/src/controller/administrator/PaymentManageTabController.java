package controller.administrator;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

//납부 여부 조회 및 관리
public class PaymentManageTabController implements Initializable 
{
	@FXML
    private ComboBox<String> update_gender_combobox;
	
	@FXML
    private ComboBox<String> update_isPaid_combobox;
	
	private final String[] comboboxItem_gender = {"M", "F"};
	private final String[] comboboxItem_boolean = {"T", "F"};
	
	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		System.out.println("납부 여부 조회 및 관리 새로고침됨");
		
		update_gender_combobox.getItems().addAll(comboboxItem_gender);
		update_isPaid_combobox.getItems().addAll(comboboxItem_boolean);
	}

}
