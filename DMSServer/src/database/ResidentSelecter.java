package database;

import java.util.Iterator;
import java.util.TreeSet;
import shared.classes.*;

public class ResidentSelecter 
{
	
	
	public void pass(Application[] apps, int num)	//apps == 정렬되기 전의 신청 배열, num == 기숙사의 수용인원 - 현재 합격된 인원 
	{
		//로컬 변수명이 대문자로 시작하는거 꼬와서 SortedApp을 sortedApp으로 수정함.
		//대괄호 자바식으로 되어있던거 꼬와서 수정함.
		TreeSet<Application> sortedApp = new TreeSet<Application>();
		
		//TreeSet은 자동으로 정렬을 해주니까 등록을 전부 가져와서 때려박으면 성적으로 정렬해줌
		for(int i = 0; i < apps.length; i++) 
		{	
			sortedApp.add(apps[i]);
		}
		
		Iterator<Application> iterator = sortedApp.iterator();	//sortedApp에 순차적 접근을 하기 위한 반복자
		
		for(int i = 0; i < num; i++)	// 신청의 isPassed를 true로 바꾸는 작업을 필요한 수만큼 시행
		{
			iterator.next().setPassed(true);
		}
	}

}