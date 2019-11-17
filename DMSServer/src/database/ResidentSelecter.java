package database;

import java.util.TreeSet;
import classes.Application;

public class ResidentSelecter {
	public TreeSet<Application> pass(Application[] apps) {
		TreeSet<Application> SortedApp = new TreeSet<Application>();
		
		for(int i = 0; i < apps.length; i++) {	//TreeSet은 자동으로 정렬을 해주니까 등록을 전부 가져와서 때려박으면 성적으로 정렬해줌
			SortedApp.add(apps[i]);
		}
		
		return SortedApp;
	}

}