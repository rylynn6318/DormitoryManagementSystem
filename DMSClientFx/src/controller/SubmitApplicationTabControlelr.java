package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

public class SubmitApplicationTabControlelr implements Initializable 
{

    @FXML
    private VBox SubmitApplicationTab;

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		System.out.println("신청 페이지 생성됨");
		
	}

}
