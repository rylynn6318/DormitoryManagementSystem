package controller;

import application.UserInfo;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class LoginPageController
{
	@FXML
    private TextField IDField;

    @FXML
    private PasswordField PWField;

    @FXML
    private Button LoginBtn;
    
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
    private boolean tryLogin()
    {
    	//사용자가 입력한 아이디, 비밀번호를 가져온다.
    	String inputUserId = IDField.getText();
    	String inputUserPw = PWField.getText();
    	
    	if(inputUserId.isEmpty())
    	{
    		showAlert("아이디가 비어있습니다");
    		IDField.requestFocus();
    		return false;
    	}
    	else if(inputUserPw.isEmpty())
    	{
    		showAlert("비밀번호가 비어있습니다");
    		PWField.requestFocus();
    		return false;
    	}
    	
    	if(inputUserId.equals("stu") && inputUserPw.equals("pass"))
    	{
    		//로그인 성공
    		showAlert("학생 로그인 성공");
    		setUserInfo(inputUserId, inputUserPw, 0);			//TODO : 나중에 사용자 타입을 Enum으로 고쳐서 통일할것.
    		moveToMain();
    		return true;
    	}
    	else if(inputUserId.equals("admin") && inputUserPw.equals("pass"))
    	{
    		showAlert("관리자 로그인 성공");
    		setUserInfo(inputUserId, inputUserPw, 1);			//TODO : 나중에 사용자 타입을 Enum으로 고쳐서 통일할것.
    		moveToMain();
    		return true;
    	}
    	else
    	{
			//로그인 실패
    		showAlert("아이디 혹은 비밀번호가 틀렸습니다.");
    		IDField.requestFocus();
    		return false;
    	}
    }
    
    //UserInfo 싱글톤 설정
    private void setUserInfo(String id, String pw, int type)
    {
    	UserInfo.getInstance().setAccountId(id);
    	UserInfo.getInstance().setPassword(pw);
    	System.out.println("DEBUG : setUserInfo에서 Type이 " + type + "으로 설정됨");
    	UserInfo.getInstance().setType(type);
    }
    
    //경고창 띄우는 메소드
    private void showAlert(String s)
    {
    	Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("information");
		alert.setHeaderText(null);
		alert.setContentText(s);
		alert.showAndWait();
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
