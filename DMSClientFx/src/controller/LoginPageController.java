package controller;

import java.net.URL;
import java.util.ResourceBundle;

import application.IOHandler;
import application.UserInfo;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import shared.enums.UserType;

public class LoginPageController implements Initializable
{
	@FXML
    private TextField IDField;

    @FXML
    private PasswordField PWField;

    @FXML
    private Button LoginBtn;
    
	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		System.out.println("로그인 페이지 생성됨");
		
	}
    
    //---------------------------------------------------------------------------
    //이벤트 발생
    
    //아이디 필드에서 엔터 입력 시
    @FXML
    void On_IDField_Typed(KeyEvent event) 
    {
    	if(event.getCode() == KeyCode.ENTER || event.getCharacter().equals("\r"))
    	{
    		PWField.requestFocus();
    	}
    }
    
    //비밀번호 필드에서 엔터 입력 시
    @FXML
    void On_PWField_Typed(KeyEvent event) 
    {
    	if(event.getCode() == KeyCode.ENTER || event.getCharacter().equals("\r"))
    	{
    		tryLogin();
    	}
    }
    
    @FXML
    void On_LoginBtn_Clicked(MouseEvent event) 
    {
    	//로그인 시도
    	tryLogin();
    	
    }
    
    //---------------------------------------------------------------------------
    //로직
    
    //서버와 통신한다. (통신이 미구현임으로 여기서 대충 처리한다)
    private void tryLogin()
    {
    	//사용자가 입력한 아이디, 비밀번호를 가져온다.
    	String inputUserId = IDField.getText();
    	String inputUserPw = PWField.getText();
    	
    	if(inputUserId.isEmpty())
    	{
    		IOHandler.getInstance().showAlert("아이디가 비어있습니다");
    		IDField.requestFocus();
    		return;
    	}
    	else if(inputUserPw.isEmpty())
    	{
    		IOHandler.getInstance().showAlert("비밀번호가 비어있습니다");
    		PWField.requestFocus();
    		return;
    	}
    	
    	
    	//네트워킹 여기서 해라
    	if(networking(inputUserId, inputUserPw))
    	{
    		IOHandler.getInstance().showAlert("로그인 성공");
    		moveToMain();
    	}
    	else
    	{
    		IOHandler.getInstance().showAlert("아이디 혹은 비밀번호가 틀렸습니다.");
    		IDField.requestFocus();
    	}
    }
    
    //테스트용 네트워킹
    private boolean networking(String id, String pw)
    {
    	if(id.equals("stu") && pw.equals("pass"))
    	{
    		//로그인 성공
    		setUserInfo(id, pw, UserType.STUDENT);
    		return true;
    	}
    	else if(id.equals("admin") && pw.equals("pass"))
    	{
    		//관리자 로그인 성공
    		setUserInfo(id, pw, UserType.ADMINISTRATOR);
    		return true;
    	}
    	else
    	{
			//로그인 실패
    		return false;
    	}
    }
    
    //UserInfo 싱글톤 설정
    private void setUserInfo(String id, String pw, UserType userType)
    {
    	UserInfo.getInstance().setAccountId(id);
    	UserInfo.getInstance().setPassword(pw);
    	UserInfo.getInstance().setUserType(userType);
    }
    
    //메인 화면으로 이동
    private  void moveToMain()
    {
    	try
    	{   
    	    //-------------아래 방법은 Scene을 교체하는 방법---------------//
    		
    		//메인페이지 열기
    		Stage primaryStage = (Stage)LoginBtn.getScene().getWindow(); // 기본 스테이지 가져오기
            Parent root = FXMLLoader.load(getClass().getResource("/page/MainPage.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
    	    
    	} 
    	catch(Exception e)
    	{
    		System.out.println(e.getMessage());
    	}
    }

}
