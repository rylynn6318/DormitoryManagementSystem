package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class MainPageController implements Initializable 
{

    @FXML
    private ListView<String> SideListView;

    @FXML
    private AnchorPane SubAnchorPane;

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		System.out.println("메인 페이지 로드 시작");
		
		//observableArrayList형태로 메뉴 배열로 관리 가능.
		//private ListView<String> SideListView; 의 <> 타입을 클래스로 사용할 수 있을듯.
		SideListView.setItems(FXCollections.observableArrayList());
		SideListView.getItems().add("생활관 입사 신청");
		SideListView.getItems().add("생활관 호실 확인");
		SideListView.getItems().add("생활관 신청확인");
		SideListView.getItems().add("생활관 고지서 출력");
		SideListView.getItems().add("결핵진단서 제출");
		SideListView.getItems().add("결핵진단서 조회");
		
		//리스트뷰 더블클릭 이벤트 처리. 메뉴타입이 클래스로 바뀌면 String을 클래스명으로 바꾸셈.
		SideListView.setOnMouseClicked(new EventHandler<MouseEvent>() 
		{
		    @Override
		    public void handle(MouseEvent click) 
		    {
		        if (click.getClickCount() == 2) 
		        {
		        	String currentItemSelected = SideListView.getSelectionModel().getSelectedItem();
		        	//필요한 메소드 호출
		        	System.out.println(currentItemSelected);
		        }
		    }
		});
	}

}
