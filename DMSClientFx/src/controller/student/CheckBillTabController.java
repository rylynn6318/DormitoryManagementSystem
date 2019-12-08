package controller.student;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.util.ResourceBundle;

import application.IOHandler;
import application.Responser;
import controller.InnerPageController;
import enums.Bool;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import models.Tuple;

public class CheckBillTabController extends InnerPageController 
{
    @FXML
    private Button check_button;

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		System.out.println("생활관 고지서 조회 새로고침됨");
		checkSchedule();
	}
	
	//---------------------이벤트---------------------
	
	@FXML
    void on_check_button_actioned(ActionEvent event) 
    {
		System.out.println("생활관 고지서 조회 : 조회 클릭 됨");
		checkBill();
    }
	
	//---------------------로직---------------------
	
	private void checkSchedule()
	{
		Tuple<Bool, String> resultTuple =  Responser.student_CheckBillPage_onEnter();
		
		//서버랑 통신이 됬는가?
        if(resultTuple == null)
        {
        	IOHandler.getInstance().showAlert("서버에 연결할 수 없습니다.");
        	if(!IOHandler.getInstance().showDialog("디버그", "계속 진행하시겠습니까?"))
        	{
        		//여기서 페이지 닫게 해주자.
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
            	//여기서 페이지 닫게 해주자.
            	//return;
            }
        }
	}
	
	private void checkBill()
	{
		//여기는 뭐 검사할 필요없이 바로 서버로 요청날림.
		String result = Responser.student_CheckBillPage_onCheck();
		
		if(result == null)
		{
			IOHandler.getInstance().showAlert("서버에 연결할 수 없습니다.");
			return;
		}
		
		saveToDesktop(result);
		IOHandler.getInstance().showAlert("바탕화면에 고지서 파일이 저장되었습니다.");
	}
	
	private void saveToDesktop(String str)
	{
		try
		{
	        // BufferedWriter 와 FileWriter를 조합하여 사용 (속도 향상)
			String userPath = System.getProperty("user.home");
			String fileSavePath = userPath + "\\Desktop\\" + "고지서.txt";
	        BufferedWriter fw = new BufferedWriter(new FileWriter(fileSavePath, true));
	         
	        // 파일안에 문자열 쓰기
	        fw.write(str);
	        fw.flush();

	        // 객체 닫기
	        fw.close();
	         
	         
	    }catch(Exception e){
	        e.printStackTrace();
	    }
	}
	
	

}
