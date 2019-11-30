package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.UserInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import shared.enums.UserType;

public class MainPageController implements Initializable 
{

    @FXML
    private ListView<NavigationTab> NavigationListView;
    
    @FXML
    private TabPane MainTabPane;
    
    @FXML
    private Button logoutBtn;
    
    @FXML
    private Label UserInfoLabel;
    
	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		//생성자 확인
		System.out.println("메인 페이지 생성됨");
		
		//네비게이션 탭 초기화
		initializeNavigationTabs();
		
		//환영합니다 + ID 문구 설정
		initializeWelcomeLabel();
		
		//샌즈 추가
		addWelcomeTab();
	}
	
	@FXML
    void On_LogoutBtn_Clicked(ActionEvent event) 
	{
		//TODO : 로그아웃. 코드 임시로 추가함.
		System.out.println("로그아웃 버튼 클릭됨");
		try
		{
			Stage primaryStage = (Stage)logoutBtn.getScene().getWindow(); // 기본 스테이지 가져오기
			Parent root = FXMLLoader.load(getClass().getResource("/page/LoginPage.fxml"));
			Scene scene = new Scene(root);
	        primaryStage.setScene(scene);
	        primaryStage.show();
		}
		catch (IOException e)
		{
			System.out.println(e.getMessage());
		}
    }
	
	
	//테스트용 학생용 네비게이션 탭 객체 추가
	private void addStudentNavigationTabs()
	{
		NavigationTab tab1 = new NavigationTab("생활관 입사 신청", TabType.SubmitApplication);
		NavigationTab tab2 = new NavigationTab("생활관 신청 조회", TabType.CheckApplication);
		NavigationTab tab3 = new NavigationTab("생활관 고지서 조회", TabType.CheckBill);
		NavigationTab tab4 = new NavigationTab("생활관 호실 조회", TabType.CheckRoom);
		NavigationTab tab5 = new NavigationTab("서류 제출", TabType.SubmitDocument);
		NavigationTab tab6 = new NavigationTab("서류 조회", TabType.CheckDocument);
		
		NavigationListView.getItems().addAll(tab1, tab2, tab3, tab4, tab5, tab6);
	}
	
	//테스트용 관리자용 네비게이션 탭 객체 추가
	private void addAdminNavigationTabs()
	{
		NavigationTab tab1 = new NavigationTab("선발 일정 조회 및 관리", TabType.ScheduleManage);
		NavigationTab tab2 = new NavigationTab("생활관 조회 및 관리", TabType.DormitoryManage);
		NavigationTab tab3 = new NavigationTab("입사 선발자 조회 및 관리", TabType.SelecteesManage);
		NavigationTab tab4 = new NavigationTab("입사자 조회 및 관리", TabType.BoarderManage);
		NavigationTab tab5 = new NavigationTab("납부 여부 조회 및 관리", TabType.PaymentManage);
		NavigationTab tab6 = new NavigationTab("서류 조회 및 제출", TabType.DocumentManage);
		
		NavigationListView.getItems().addAll(tab1, tab2, tab3, tab4, tab5, tab6);
	}
	
	private void initializeNavigationTabs()
	{
		//네비게이션 탭(좌측 탭) 객체 설정
		NavigationListView.setCellFactory(param -> new ListCell<NavigationTab>() 
		{
		    @Override
		    protected void updateItem(NavigationTab tab, boolean empty) {
		        super.updateItem(tab, empty);

		        if (empty || tab == null || tab.getText() == null) 
		        {
		            setText(null);
		        } 
		        else 
		        {
		        	//네비게이션 탭 이름 설정
		            setText(tab.getText());
		        }
		    }
		});
		
		//네비게이션 탭 더블클릭 이벤트 처리.
		NavigationListView.setOnMouseClicked(new EventHandler<MouseEvent>() 
		{
		    @Override
		    public void handle(MouseEvent click) 
		    {
		        if (click.getClickCount() == 2) 
		        {
		        	//네비게이션탭에서 이번에 선택된 아이템을 찾는다.
		        	NavigationTab currentItemSelected = NavigationListView.getSelectionModel().getSelectedItem();
		        	
		        	if(currentItemSelected != null)
		        	{
		        		//탭이 열려있지 않다면 탭을 만든다.
			        	if(!MainTabPane.getTabs().contains(currentItemSelected))
			        	{
			        		//선택된 탭을 오른쪽 MainTabPane에 추가
				        	MainTabPane.getTabs().add(currentItemSelected);
			        	}
			        	MainTabPane.getSelectionModel().select(currentItemSelected);			//열린 페이지를 선택한다(MainTabPane에서 Select)
			        	
			        	//탭에 맞는 UI 불러오기
			        	//이거 근데 페이지 이미 열려있을때도 새로 로드하게되는거같음.
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
		        	}
		        }
		    }
		});
		
		//사용자 타입에 따른 메뉴 설정
		switch(UserInfo.getInstance().getUserType())
		{
		case STUDENT:
			addStudentNavigationTabs();
			break;
		case ADMINISTRATOR:
			addAdminNavigationTabs();
			break;
		case TEACHER:
			addAdminNavigationTabs();
			break;
		}
	}
	
	private void initializeWelcomeLabel()
	{
		String userStr = "";
		switch(UserInfo.getInstance().getUserType())
		{
		case STUDENT:
			userStr = "학생";
			break;
		case ADMINISTRATOR:
			userStr = "관리자";	
			break;
		case TEACHER:
			userStr = "선생님";
			break;
		}
		
		UserInfoLabel.setText("환영합니다! " + userStr + " " + UserInfo.getInstance().getAccountId() + " 님!");	
	}
	
	private void addWelcomeTab()
	{
		//선택된 탭을 오른쪽 MainTabPane에 추가
		Tab welcomeTab = new Tab("환영합니다!");
    	MainTabPane.getTabs().add(welcomeTab);
	
    	MainTabPane.getSelectionModel().select(welcomeTab);	
    	
    	try
		{
    		String res = "/page/WelcomeTab.fxml";
			Parent root = FXMLLoader.load(getClass().getResource(res));
			welcomeTab.setContent(root);
		} 
    	catch (IOException e)
		{
    		System.out.println(e.getMessage());
		}
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