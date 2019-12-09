package controller.student;

import java.io.Serializable;
import java.net.URL;
import java.util.ResourceBundle;

import application.IOHandler;
import application.Responser;
import controller.InnerPageController;
import enums.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import models.*;

public class CheckRoomTabController extends InnerPageController
{
	@FXML
    private Button check_button;

    @FXML
    private Label select_result_label;

    @FXML
    private Label isPaid_label;

    @FXML
    private Label mealType_label;

    @FXML
    private Label dorm_label;

    @FXML
    private Label roomType_label;

    @FXML
    private Label roomAndBed_label;

    @FXML
    private TextArea info_textarea;


	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		System.out.println("생활관 호실 조회 새로고침됨");
		
		//네트워킹
		checkSchedule();
	}
	
	//---------------------이벤트---------------------
	
    @FXML
    void on_check_button_actioned(ActionEvent event) 
    {
    	System.out.println("생활관 호실 조회 : 조회 클릭됨");
    	checkRoom();
    }
	
	//---------------------로직---------------------
    
    private void checkSchedule()
	{
		Tuple<Bool, String> resultTuple = Responser.student_checkRoomPage_onEnter();
		
		//서버랑 통신이 됬는가?
        if(resultTuple == null)
        {
        	IOHandler.getInstance().showAlert("서버에 연결할 수 없습니다.");
        	if(!IOHandler.getInstance().showDialog("디버그", "계속 진행하시겠습니까?"))
        	{
        		close();
        		return;
        	}
        }
        
        if(resultTuple != null)
        {
        	//스케쥴 체크가 됬는가?
        	//스케쥴 때문에 진입 불가인 경우 tuple의 첫번째 항목이 false로 반환된다.
            if(resultTuple.obj1 == Bool.FALSE)
            {
            	IOHandler.getInstance().showAlert(resultTuple.obj2);
            	if(!IOHandler.getInstance().showDialog("디버그", "계속 진행하시겠습니까?"))
            	{
            		close();
            		return;
            	}
            }
            else
            {
            	info_textarea.setText(resultTuple.obj2);
            }
        }
	}
    
    private void checkRoom()
    {
    	//여기는 뭐 검사할 필요없이 바로 서버로 요청날림.
    	Serializable result = Responser.student_checkRoomPage_onCheck();

    	//서버랑 통신이 됬는가?
        if(result == null)
        {
        	IOHandler.getInstance().showAlert("서버에 연결할 수 없습니다.");
        	if(!IOHandler.getInstance().showDialog("디버그", "계속 진행하시겠습니까?"))
        	{
        		//여기서 페이지 닫게 해주자.
        		close();
        		return;
        	}
        }
        
    	Tuple<Bool, String> checkTuple = (Tuple<Bool, String>) result;
    	if(checkTuple.obj1 == Bool.FALSE)
    	{
    		IOHandler.getInstance().showAlert(checkTuple.obj2);
    		return;
    	}
        
    	Tuple<Application, PlacementHistory> resultTuple = (Tuple<Application, PlacementHistory>) result;
    	
        //받은 신청, 배정내역 분리 및 명시
        Application application = resultTuple.obj1;
        PlacementHistory placementHistory = resultTuple.obj2;
        
        //UI에 표시
        setApplicationInfo(application);
        setPlacementHistoryInfo(placementHistory);
    }
    
    private void setApplicationInfo(Application application)
    {
    	String lastPassedStr = application.isLastPassed() == Bool.TRUE ? "합격" : "불합격";
    	String isPaidStr = application.isPaid() == Bool.TRUE ? "납부" : "미납";
    	String mealTypeStr = mealTypeIntToStr(application.getMealType());
    	String dormNameStr = application.getDormitoryName();
    	
    	select_result_label.setText(lastPassedStr);
    	isPaid_label.setText(isPaidStr);
    	mealType_label.setText(mealTypeStr);
    	dorm_label.setText(dormNameStr);
    }
    
    private void setPlacementHistoryInfo(PlacementHistory placementHistory)
    {
    	String roomTypeStr = "일반실";
    	String roomIdStr = String.valueOf(placementHistory.roomId);
    	String seatStr = String.valueOf(placementHistory.seat);
    	
    	roomType_label.setText(roomTypeStr);
    	roomAndBed_label.setText(roomIdStr + " / " + seatStr);
    }

}
