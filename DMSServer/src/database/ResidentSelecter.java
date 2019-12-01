package database;

import java.util.Iterator;
import java.util.TreeSet;
import shared.classes.*;

public class ResidentSelecter 
{
	public void temp()
	{
//		알고리즘
//		SQL문 SELECT * FROM 신청 WHERE 생활관명=오름3 && 지망=1 && 학기=201901 && 합격여부=N 를 통해 현재 학기에서 생활관명, 지망이 동일하고 합격여부가 Y인 것들만 뽑아낸 테이블과 생활관.getCapacity()를 pass 함수에 넣음
//		SELECT COUNT(*) FROM (SELECT * FROM 배정내역 WHERE 생활관명=찾을 생활관 명 AND 학기=현재학기)의 결과가 생활관.getCapacity()보다 작으면 생활관.getCapacity에서 앞 SQL문의 결과로 나온 값만큼 빼서 pass를 다시 돌림
//		SELECT COUNT(*) FROM (SELECT * FROM 배정내역 WHERE 생활관명=찾을 생활관 명 AND 학기=현재학기)의 결과가 생활관.getCapacity()과 같아질 때 까지 반복
//		위 3단계를 각 생활관별로 시행한다.
	}
	//SQL문 SELECT * FROM 신청 WHERE 생활관명=오름3 && 지망=1 && 학기=201901 이런 식으로 현재 학기에서 생활관명, 지망이 동일한 것들만 뽑아낸 테이블을 넣어줘야함
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
			Application temp = iterator.next();
//			temp.setPassed(true);
//			UPDATE 신청 SET 합격여부=Y WHERE 학번=temp.getStudentId() && 지망=temp.getChoice && 학기=temp.getSemesterCode;
//			정렬을 하기 위해 테이블 가져와서 만든 객체는 딱히 통과여부를 true로 만들어줄 필요가 없는건가??? 일단 주석 처리 해둠
		}
	}
	
	
	public void deleteApplication(String studentID, String dormitoryName, int semesterCode, int choice)	//입사 선발자 조회 및 관리 페이지에서 삭제 버튼을 누르면 사용할 SQL
	{
//			DELETE FROM 신청 WHERE 학번=studentID && 생활관정보_기숙사명=dormitoryName && 생활관정보_학기=semesterCode && 지망=choice;
	}
	
}