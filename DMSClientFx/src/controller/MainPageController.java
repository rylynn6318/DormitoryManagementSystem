package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class MainPageController implements Initializable 
{

    @FXML
    private ListView<String> NavigationListView;
    
    @FXML
    private TabPane MainTabPane;
    
	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		//생성자 확인
		System.out.println("메인 페이지 생성됨");
		
		//네비게이션 탭 초기화
		initializeNavigationTabs();
		
		
	}
	
	private void initializeNavigationTabs()
	{
		//디버깅용 네비게이션 탭 객체 설정
		NavigationListView.setItems(FXCollections.observableArrayList());
		
		//디버깅용 네비게이션 탭 객체 추가
		NavigationListView.getItems().add("생활관 입사 신청");
		NavigationListView.getItems().add("생활관 호실 확인");
		NavigationListView.getItems().add("생활관 신청확인");
		NavigationListView.getItems().add("생활관 고지서 출력");
		NavigationListView.getItems().add("결핵진단서 제출");
		NavigationListView.getItems().add("결핵진단서 조회");
		
		//리스트뷰 더블클릭 이벤트 처리. 메뉴타입이 클래스로 바뀌면 String을 클래스명으로 바꾸셈.
		NavigationListView.setOnMouseClicked(new EventHandler<MouseEvent>() 
		{
		    @Override
		    public void handle(MouseEvent click) 
		    {
		        if (click.getClickCount() == 2) 
		        {
		        	//이번에 선택된 아이템을 찾는다.
		        	String currentItemSelected = NavigationListView.getSelectionModel().getSelectedItem();
		        	
		        	// Main Tab Pane에 추가할 Tab 생성
		        	Tab curTab = new Tab(currentItemSelected);
		        	
		        	try
					{
						VBox a = FXMLLoader.load(getClass().getResource("/page/CheckApplicationTab.fxml"));
						curTab.setContent(a);
					} 
		        	catch (IOException e)
					{
		        		System.out.println(e.getMessage());
					}
		        	MainTabPane.getTabs().add(curTab);
		        	NavigationListView.getSelectionModel().select(curTab.getText());
		        }
		    }
		});
	}
}

class NavigationBarClickEventHanlder implements EventHandler<MouseEvent>
{
	@Override
	public void handle(MouseEvent event)
	{
		// TODO 네비게이션 바에서 이벤트 핸들러를 별개로 구성할거면 여기에다 코드를 넣어라.
		
	}
}

class NavigationTab
{
	private String title;
	private TabType tabtype;
	
	public NavigationTab(String title, TabType tabType)
	{
		this.title = title;
		this.tabtype = tabType;
	}
}

enum TabType
{
	SubmitApplication(0), CheckApplication(1);
	
	private final int value;
	private TabType(int value)
	{
		this.value = value;
	}
}