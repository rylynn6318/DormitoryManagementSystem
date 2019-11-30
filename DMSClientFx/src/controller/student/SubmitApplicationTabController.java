package controller.student;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

public class SubmitApplicationTabController implements Initializable 
{

    @FXML
    private VBox SubmitApplicationTab;

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		System.out.println("생활관 입사 신청 새로고침됨");
		
	}

}
