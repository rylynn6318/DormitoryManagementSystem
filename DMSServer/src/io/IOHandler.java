package io;

import java.util.Scanner;

//IO 패키지의 필요성
//서버 콘솔에서의 입출력을 담당할 클래스가 필요,
//클라이언트에게 파일을 송/수신 할 때 이를 수행하는 기능이 필요.
//예를들어 결핵진단서의 경우 클라이언트로부터 다운받으면, 경로를 지정해 저장해주는 클래스가 필요하다.
//DB에는 파일을 다운로드한 경로를 저장하도록 한다.

public class IOHandler
{
	public void showMenu()
	{
		System.out.println("");
		System.out.println("---------- 메인메뉴 ----------");
		System.out.println("1. 서버 실행");
		System.out.println("2. 디버그 모드로 실행");
		System.out.println("3. 서버 종료");
		System.out.println("------------------------------");
	}
	
	public MenuType getMenu()
	{
		//숫자가 아닌 값을 입력받은 경우 예외처리함
		int userInput = MenuType.UNKNOWN.ordinal();
		try
		{
			userInput = Integer.parseInt(getUserInput("입력 : "));
		}
		catch(Exception e)
		{
			System.out.println("ERROR) 숫자를 입력해주세요.");
		}
		
		switch(userInput)
		{
		case 1:
			return MenuType.RUN;
		case 2:
			return MenuType.DEBUG;
		case 3:
			return MenuType.SHUTDOWN;
		default:
			return MenuType.UNKNOWN;
		}
	}
	
	public String getUserInput(String message)
	{
		System.out.println(message);
		Scanner scn = new Scanner(System.in);
		String output = scn.nextLine();
		return output;
	}
}
