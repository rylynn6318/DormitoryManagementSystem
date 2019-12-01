package application;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

//대충 입출력하는 클래스
//showAlert같은거 여러 클래스에서 쓰여서 그냥 싱글톤으로 만듬.
public class IOHandler
{
	private static IOHandler _instance;
	
	public static IOHandler getInstance()
	{
		if(_instance == null)
			_instance = new IOHandler();
		return _instance;
	}
	
	//경고창 띄우는 메소드
	public void showAlert(String s)
	{
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("information");
		alert.setHeaderText(null);
		alert.setContentText(s);
		alert.showAndWait();
	}
}
