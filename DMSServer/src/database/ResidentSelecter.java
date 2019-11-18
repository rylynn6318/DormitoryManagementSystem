package database;

import java.util.TreeSet;
import classes.Application;

public class ResidentSelecter 
{
	public TreeSet<Application> pass(Application[] apps) 
	{
		//로컬 변수명이 대문자로 시작하는거 꼬와서 SortedApp을 sortedApp으로 수정함.
		//대괄호 자바식으로 되어있던거 꼬와서 수정함.
		TreeSet<Application> sortedApp = new TreeSet<Application>();
		
		//TreeSet은 자동으로 정렬을 해주니까 등록을 전부 가져와서 때려박으면 성적으로 정렬해줌
		for(int i = 0; i < apps.length; i++) 
		{	
			sortedApp.add(apps[i]);
		}
		
		return sortedApp;
	}

}