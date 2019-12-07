package controller.student;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import application.IOHandler;
import application.Responser;
import enums.Bool;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import models.*;
import utils.*;

public class SubmitApplicationTabController implements Initializable 
{
 	@FXML
    private Button application_button;

    @FXML
    private Button cancel_button;

    @FXML
    private ComboBox<String> oneYear_dorm_combobox;

    @FXML
    private ComboBox<String> oneYear_meal_combobox;

    @FXML
    private ComboBox<String> firstChoice_dorm_combobox;

    @FXML
    private ComboBox<String> firstChoice_meal_combobox;

    @FXML
    private ComboBox<String> secondChoice_dorm_combobox;

    @FXML
    private ComboBox<String> secondChoice_meal_combobox;

    @FXML
    private ComboBox<String> thirdChoice_dorm_combobox;

    @FXML
    private ComboBox<String> thirdChoice_meal_combobox;
    
    @FXML
    private CheckBox isSnore_checkbox;

    @FXML
    private TextArea info_textarea;

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		System.out.println("생활관 입사 신청 새로고침됨");
		
		//네트워킹
		Tuple<String, ArrayList<Dormitory>> resultTuple = Responser.student_submitApplicationPage_onEnter();
        
		//서버랑 통신이 됬는가?
        if(resultTuple == null)
        {
        	IOHandler.getInstance().showAlert("서버에 연결할 수 없습니다.");
        	//여기서 페이지 닫게 해주자.
        	//return;
        }
        else
        {
        	//스케쥴 체크가 됬는가?
        	//스케쥴 때문에 진입 불가인 경우 tuple의 두번째 항목이 null로 반환된다.
            if(resultTuple.obj2 == null)
            {
            	IOHandler.getInstance().showAlert(resultTuple.obj1);
            	//여기서 페이지 닫게 해주자.
            	//return;
            }
        }
        
        //안내사항 표시
        if(resultTuple != null)
        	info_textarea.setText(resultTuple.obj1);
		
		//TODO 테스트용, 콤보박스 아이템 추가. 나중에 클래스든, String이든, 네트워크에서 받아와 처리해야됨.
		//그리고, 요청받아올때 서버에서 남자인지 여자인지 알아내서 그에 맞는 생활관구분, 기간구분, 식사구분, 생활관비 가져와야댐.
		//(추가)그냥 서버에서 생활관정보테이블->이번학기->성별에 맞게-> 다 긁어와서 객체 배열로 만들고, 클라이언트로 전송하면됨.
		setCombobox(resultTuple.obj2);
		
	}
	
	private void debug_comboboxView()
	{
		//남자라고 가정, 사실 남자인지 여자인지도 서버에서 알아서 판단해서 클라이언트에게 보냄. 클라이언트는 이게 남자껀지 여자껀지 몰라야함.
		//일반학기라고 가정, 클라이언트는 일반학기인지, 방학인지는 모름. 이것도 서버에서 알아서 판단해서 알아서 보내줌.
		oneYear_dorm_combobox.getItems().addAll("선택", "푸름관 1,2동");
		firstChoice_dorm_combobox.getItems().addAll("선택", "푸름관 1,2동", "푸름관 4동", "오름관 2,3동");
		secondChoice_dorm_combobox.getItems().addAll("선택", "푸름관 1,2동", "푸름관 4동", "오름관 2,3동");
		thirdChoice_dorm_combobox.getItems().addAll("선택", "푸름관 1,2동", "푸름관 4동", "오름관 2,3동");
		
		//식사구분은 기숙사에따라서, 식사의무가 T인지 F인지에 따라 서버가 알아서 보내줘야함.
		oneYear_meal_combobox.getItems().addAll("선택", "5일식", "7일식", "식사안함");
		firstChoice_meal_combobox.getItems().addAll("선택", "5일식", "7일식", "식사안함");
		secondChoice_meal_combobox.getItems().addAll("선택", "5일식", "7일식", "식사안함");
		thirdChoice_meal_combobox.getItems().addAll("선택", "5일식", "7일식", "식사안함");
		
		//이건 사용자 선택에 따라 결정, 변경되야함. 물론 서버에서 기본 정보는 다 받아오고, 사용자 선택에 맞는 값을 표시해줘야함.
//		oneYear_cost_label.setText("만원");
//		firstChoice_cost_label.setText("만원");
//		secondChoice_cost_label.setText("만원");
//		thirdChoice_cost_label.setText("만원");	
	}
	
	//---------------------이벤트---------------------
	
	@FXML
    void on_application_button_actioned(ActionEvent event) 
    {
		System.out.println("생활관 입사 신청 : 신청 클릭됨");
		submitApplication();
    }

    @FXML
    void on_cancel_button_actioned(ActionEvent event) 
    {
    	System.out.println("생활관 입사 신청 : 취소 클릭됨");
    	cancelApplication();
    }
    
    //---------------------로직---------------------
    
    //사용자가 클릭한 콤보박스로 신청객체 배열을 만들어 반환하는 클래스
    private ArrayList<Application> getApplicationList(Bool isSnore)
    {
    	int choiceCnt = 1;
    	ArrayList<Application> applicationList = new ArrayList<Application>();
    	
    	String oneYearDorm = oneYear_dorm_combobox.getSelectionModel().getSelectedItem();
    	String oneYearMeal = oneYear_meal_combobox.getSelectionModel().getSelectedItem();
    	
    	String firstDorm = firstChoice_dorm_combobox.getSelectionModel().getSelectedItem();
    	String firstMeal = firstChoice_meal_combobox.getSelectionModel().getSelectedItem();
    	
    	String secondDorm = secondChoice_dorm_combobox.getSelectionModel().getSelectedItem();
    	String secondMeal = secondChoice_meal_combobox.getSelectionModel().getSelectedItem();
    	
    	String thirdDorm = thirdChoice_dorm_combobox.getSelectionModel().getSelectedItem();
    	String thirdMeal = thirdChoice_meal_combobox.getSelectionModel().getSelectedItem();
    	
    	if(oneYearDorm != null && oneYearMeal != null)
    	{
    		Application app0 = comboboxToApplication(0, oneYearDorm, oneYearMeal);
    		app0.setSnore(isSnore);
    		applicationList.add(app0);
    	}
    	
    	// 1,2,3 중 2가 비면 3이 2순위됨.
    	if(firstDorm != null && firstMeal != null)
    	{
    		Application app1 = comboboxToApplication(choiceCnt++, firstDorm, firstMeal);
    		app1.setSnore(isSnore);
    		applicationList.add(app1);
    	}
    		
    	if(secondDorm != null && secondMeal != null)
    	{
    		Application app2 = comboboxToApplication(choiceCnt++, secondDorm, secondMeal);
    		app2.setSnore(isSnore);
    		applicationList.add(app2);
    	}
    	
    	if(thirdDorm != null && thirdMeal != null)
    	{
    		Application app3 = comboboxToApplication(choiceCnt++, thirdDorm, thirdMeal);
    		app3.setSnore(isSnore);
    		applicationList.add(app3);
    	}
    	
    	return applicationList;
    }
    
    private Application comboboxToApplication(int choice, String dormName, String mealTypeStr)
    {
    	int mealType = -1;
    	
    	switch(mealTypeStr)
    	{
    	case "식사안함":
    		mealType = 0;
    		break;
    	case "5일식":
    		mealType = 5;
    		break;
    	case "7일식":
    		mealType = 7;
    		break;
    	}
    	
    	return new Application(choice, dormName, mealType);
    }
    
    
    private void submitApplication()
    {
    	//대충 신청 전송하는 메소드
    	Bool isSnore = isSnore_checkbox.isSelected() ? Bool.TRUE : Bool.FALSE;
    	
    	ArrayList<Application> applicationList = getApplicationList(isSnore);
    	String result = Responser.student_submitApplicationPage_onSubmit(applicationList);
    	
    	if(result == null)
        {
        	IOHandler.getInstance().showAlert("서버에 연결할 수 없습니다.");
        	return;
        }
        
        IOHandler.getInstance().showAlert(result);
    }
    
    private void cancelApplication()
    {
    	//대충 취소 전송하는 메소드
    	String result = Responser.student_submitApplicationPage_onCancel();
    	
    	if(result == null)
        {
        	IOHandler.getInstance().showAlert("서버에 연결할 수 없습니다.");
        	return;
        }
        
        IOHandler.getInstance().showAlert(result);
    }
    
    private void setCombobox(ArrayList<Dormitory> dormList)
    {
    	for(Dormitory dorm : dormList)
    	{
    		
    	}
    }

}