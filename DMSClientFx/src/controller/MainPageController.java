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
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class MainPageController implements Initializable 
{

    @FXML
    private ListView<NavigationTab> NavigationListView;
    
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
		//NavigationListView.setItems(FXCollections.observableArrayList());			//가장 기본적은 설정법인데, NavigationListView<String>밖에 안됨.
		
		NavigationListView.setCellFactory(param -> new ListCell<NavigationTab>() 
		{
		    @Override
		    protected void updateItem(NavigationTab item, boolean empty) {
		        super.updateItem(item, empty);

		        if (empty || item == null || item.getTitle() == null) 
		        {
		            setText(null);
		        } 
		        else 
		        {
		            setText(item.getTitle());
		        }
		    }
		});
		
		//테스트용 네비게이션 탭 객체 추가
		NavigationTab tab1 = new NavigationTab("생활관 입사 신청", TabType.SubmitApplication);
		NavigationTab tab2 = new NavigationTab("생활관 호실 확인", TabType.CheckApplication);
		NavigationTab tab3 = new NavigationTab("생활관 신청확인", TabType.SubmitApplication);
		NavigationTab tab4 = new NavigationTab("생활관 고지서 출력", TabType.SubmitApplication);
		NavigationTab tab5 = new NavigationTab("결핵진단서 제출", TabType.CheckApplication);
		NavigationTab tab6 = new NavigationTab("결핵진단서 조회", TabType.SubmitApplication);
		
		NavigationListView.getItems().add(tab1);
		NavigationListView.getItems().add(tab2);
		NavigationListView.getItems().add(tab3);
		
		//리스트뷰 더블클릭 이벤트 처리. 메뉴타입이 클래스로 바뀌면 String을 클래스명으로 바꾸셈.
		NavigationListView.setOnMouseClicked(new EventHandler<MouseEvent>() 
		{
		    @Override
		    public void handle(MouseEvent click) 
		    {
		        if (click.getClickCount() == 2) 
		        {
		        	//이번에 선택된 아이템을 찾는다.
		        	NavigationTab currentItemSelected = NavigationListView.getSelectionModel().getSelectedItem();
		        	
		        	try
					{
						VBox a = FXMLLoader.load(getClass().getResource("/page/CheckApplicationTab.fxml"));
						currentItemSelected.setContent(a);
					} 
		        	catch (IOException e)
					{
		        		System.out.println(e.getMessage());
					}
		        	MainTabPane.getTabs().add(currentItemSelected);
		        	NavigationListView.getSelectionModel().select(currentItemSelected);
		        	
		        	//테스트
		        	Tab a = new Tab("Aa");
		        	MainTabPane.getTabs().add(a);
		        }
		    }
		});
	}
}

class NavigationTab extends Tab
{
	private String title;
	private TabType tabtype;
	
	public NavigationTab(String title, TabType tabType)
	{
		this.title = title;
		this.tabtype = tabType;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public TabType getTabtype()
	{
		return tabtype;
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

class NavigationBarClickEventHanlder implements EventHandler<MouseEvent>
{
	@Override
	public void handle(MouseEvent event)
	{
		// TODO 네비게이션 바에서 이벤트 핸들러를 별개로 구성할거면 여기에다 코드를 넣어라.
		
	}
}