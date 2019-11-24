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
		NavigationListView.setCellFactory(param -> new ListCell<NavigationTab>() 
		{
		    @Override
		    protected void updateItem(NavigationTab item, boolean empty) {
		        super.updateItem(item, empty);

		        if (empty || item == null || item.getText() == null) 
		        {
		            setText(null);
		        } 
		        else 
		        {
		            setText(item.getText());
		        }
		    }
		});
		
		//테스트용 네비게이션 탭 객체 추가
		NavigationTab tab1 = new NavigationTab("생활관 입사 신청", TabType.SubmitApplication);
		NavigationTab tab2 = new NavigationTab("생활관 신청 조회", TabType.CheckApplication);
		NavigationTab tab3 = new NavigationTab("생활관 고지서 조회", TabType.CheckBill);
		NavigationTab tab4 = new NavigationTab("생활관 호실 조회", TabType.CheckRoom);
		NavigationTab tab5 = new NavigationTab("서류 제출", TabType.SubmitDocument);
		NavigationTab tab6 = new NavigationTab("서류 조회", TabType.CheckDocument);
		
		NavigationTab tab7 = new NavigationTab("선발 일정 조회 및 관리", TabType.ScheduleManage);
		NavigationTab tab8 = new NavigationTab("생활관 조회 및 관리", TabType.DormitoryManage);
		NavigationTab tab9 = new NavigationTab("입사 선발자 조회 및 관리", TabType.SelecteesManage);
		NavigationTab tab10 = new NavigationTab("입사자 조회 및 관리", TabType.BoarderManage);
		NavigationTab tab11 = new NavigationTab("납부 여부 조회 및 관리", TabType.PaymentManage);
		NavigationTab tab12 = new NavigationTab("서류 조회 및 제출", TabType.DocumentManage);
		
		NavigationListView.getItems().add(tab1);
		NavigationListView.getItems().add(tab2);
		NavigationListView.getItems().add(tab3);
		NavigationListView.getItems().add(tab4);
		NavigationListView.getItems().add(tab5);
		NavigationListView.getItems().add(tab6);
		
		NavigationListView.getItems().add(tab7);
		NavigationListView.getItems().add(tab8);
		NavigationListView.getItems().add(tab9);
		NavigationListView.getItems().add(tab10);
		NavigationListView.getItems().add(tab11);
		NavigationListView.getItems().add(tab12);
		
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
		        	
		        	//탭에 맞는 UI 불러오기
		        	try
					{
		        		String res = currentItemSelected.getResource();
						Parent root = FXMLLoader.load(getClass().getResource(res));
						currentItemSelected.setContent(root);
					} 
		        	catch (IOException e)
					{
		        		System.out.println(e.getMessage());
					}
		        	
		        	//TabPane에 선택된 탭 추가하기. 추가하기전에 이미 MainTabPane에 기존 탭이 존재하는지 체크해야됨.
		        	MainTabPane.getTabs().add(currentItemSelected);
		        	NavigationListView.getSelectionModel().select(currentItemSelected);

		        }
		    }
		});
	}
}

class NavigationTab extends Tab
{
	private TabType tabtype;
	
	public NavigationTab(String title, TabType tabType)
	{
		super.setText(title);
		this.tabtype = tabType;
	}

	public TabType getTabtype()
	{
		return tabtype;
	}
	
	public String getResource()
	{
		switch(tabtype)
		{
		case SubmitApplication:
			return "/page/SubmitApplicationTab.fxml";
		case CheckApplication:
			return "/page/CheckApplicationTab.fxml";
		case CheckBill:
			return "/page/CheckBillTab.fxml";
		case CheckRoom:
			return "/page/CheckRoomTab.fxml";
		case SubmitDocument:
			return "/page/SubmitDocumentTab.fxml";
		case CheckDocument:
			return "/page/CheckDocumentTab.fxml";
		case ScheduleManage:
			return "/page/ScheduleManageTab.fxml";
		case DormitoryManage:
			return "/page/DormitoryManageTab.fxml";
		case SelecteesManage:
			return "/page/SelecteesManageTab.fxml";
		case BoarderManage:
			return "/page/BoarderManageTab.fxml";
		case PaymentManage:
			return "/page/PaymentManageTab.fxml";
		case DocumentManage:
			return "/page/DocumentManageTab.fxml";
		default:
			return null;
		}
	}
}
// application layer protocol 에서 big file send 할때 tcp에서 잘라주는데 buffer 크기 고려해서 우리가 짤라줘야하나
// 
enum TabType
{
	SubmitApplication(0), CheckApplication(1), CheckBill(2), CheckRoom(3), SubmitDocument(4), CheckDocument(5),
	ScheduleManage(6), DormitoryManage(7), SelecteesManage(8), BoarderManage(9), PaymentManage(10), DocumentManage(11);
	
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