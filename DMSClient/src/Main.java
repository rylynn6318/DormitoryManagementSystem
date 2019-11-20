import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

public class Main 
{

	public static void main(String[] args) 
	{
		User user = new User();
		ContentPage contentPage = new ContentPage();
		contentPage.setVisible(false);
		LoginPage login = new LoginPage();
		login.setVisible(true);
		login.loginBtn.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
					if(login.isUser(user.getId() , user.getPw()) == true )
				{
					login.setVisible(false);
					contentPage.setVisible(true);
				}//유저가 아니면 알림창 띄우기, 맞으면 학생 신청페이지
				else
					JOptionPane.showConfirmDialog(null, "아이디 비밀번호를 다시 한번 확인해 주세요.");
			}
		});	
	}	 
}

