package controller.student;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

public class CheckApplicationTabController implements Initializable
{
    @FXML
    private VBox CheckApplicationTab;

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		System.out.println("생활관 신청 조회 새로고침됨");
		
	}

}
