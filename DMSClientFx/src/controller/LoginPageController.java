package controller;

import java.net.URL;
import java.util.ResourceBundle;

import application.IOHandler;
import application.SocketHandler;
import application.UserInfo;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import models.*;
import enums.*;
import ultils.*;

public class LoginPageController implements Initializable {
    @FXML
    private TextField IDField;

    @FXML
    private PasswordField PWField;

    @FXML
    private Button LoginBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("로그인 페이지 생성됨");

    }

    //---------------------------------------------------------------------------
    //이벤트 발생

    //아이디 필드에서 엔터 입력 시
    @FXML
    void On_IDField_Typed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER || event.getCharacter().equals("\r")) {
            PWField.requestFocus();
        }
    }

    //비밀번호 필드에서 엔터 입력 시
    @FXML
    void On_PWField_Typed(KeyEvent event) throws Exception {
        if (event.getCode() == KeyCode.ENTER || event.getCharacter().equals("\r")) {
            tryLogin();
        }
    }

    @FXML
    void On_LoginBtn_Clicked(MouseEvent event) throws Exception {
        //로그인 시도
        tryLogin();

    }

    //---------------------------------------------------------------------------
    //로직

    //서버와 통신한다. (통신이 미구현임으로 여기서 대충 처리한다)
    private void tryLogin() throws Exception {
        //사용자가 입력한 아이디, 비밀번호를 가져온다.
        String inputUserId = IDField.getText();
        String inputUserPw = PWField.getText();

        if (inputUserId.isEmpty()) {
            IOHandler.getInstance().showAlert("아이디가 비어있습니다");
            IDField.requestFocus();
            return;
        } else if (inputUserPw.isEmpty()) {
            IOHandler.getInstance().showAlert("비밀번호가 비어있습니다");
            PWField.requestFocus();
            return;
        }

        //네트워킹 여기서 해라
        Code2.LoginResult loginResult = Code2.LoginResult.FAIL;
        Account account = new Account(inputUserId, inputUserPw, UserType.STUDENT);

        try {
            //테스트용으로 네트워킹 주석처리하고 패스함.
            // loginResult = networking(account);
            loginResult = Code2.LoginResult.ADMIN;

            switch (loginResult){
                case FAIL:
                    IOHandler.getInstance().showAlert("아이디 혹은 비밀번호가 틀렸습니다.");
                    IDField.requestFocus();
                    break;
                case STUDENT:
                    UserInfo.getInstance().account = new Account(account.accountId, account.password, UserType.STUDENT);
                    IOHandler.getInstance().showAlert("로그인 성공");
                    break;
                case ADMIN:
                    UserInfo.getInstance().account = new Account(account.accountId, account.password, UserType.ADMIN);
                    IOHandler.getInstance().showAlert("로그인 성공");
                    break;
            }
        }
//        catch(ConnectException ce)
//        {
//        	IOHandler.getInstance().showAlert("서버 연결에 실패했습니다.");
//        }
//        catch(IOException ie)
//        {
//        	IOHandler.getInstance().showAlert("서버와의 연결이 끊여졌습니다.");
//        }
        catch (Exception e) {
            IOHandler.getInstance().showAlert("알 수 없는 이유로 로그인에 실패했습니다.");
        }
    }

    //테스트용 네트워킹
    private Code2.LoginResult login(Account account) throws Exception {
        // To_Server 일때 code1, code2는 머가 드가든 상관 없음.
        Protocol login = new Protocol.Builder(ProtocolType.LOGIN, Direction.TO_SERVER, Code1.NULL, Code2.NULL)
                .body(ProtocolHelper.serialization(account)).build();

        login = SocketHandler.INSTANCE.request(login);

        return (Code2.LoginResult) login.code2;
    }

    //메인 화면으로 이동
    private void moveToMain() {
        try {
            //-------------아래 방법은 Scene을 교체하는 방법---------------//

            //메인페이지 열기
            Stage primaryStage = (Stage) LoginBtn.getScene().getWindow(); // 기본 스테이지 가져오기
            Parent root = FXMLLoader.load(getClass().getResource("/page/MainPage.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
