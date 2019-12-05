package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import shared.classes.Document;

public class documentManager 
{
	static final String DRIVER_NAME = "mysql";
	static final String HOSTNAME = "wehatejava.czztgstzacsv.us-east-1.rds.amazonaws.com";
	static final String PORT = "3306";
	static final String DB_NAME = "Prototype";													//DB이름
	static final String USER_NAME = "admin"; 													//DB에 접속할 사용자 이름을 상수로 정의
	static final String PASSWORD = "En2i3oHKLGh9UlnbYFP1"; 									//사용자의 비밀번호를 상수로 정의
	static final String DB_URL = 
					"jdbc:" + 
					DRIVER_NAME + "://" + 
					HOSTNAME + ":" + 
					PORT + "/" + 
					DB_NAME + "?user=" + 
					USER_NAME + "&password=" + 
					PASSWORD; 
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException
	{
		
	}
	
	public static void lookUpDocuments() throws ClassNotFoundException, SQLException	//FXCollection 몰라서 일단 띵근이가 써놓은거 따라했는데 사용법 아는사람 있으면 확인점
	{
		Connection conn = null;
		Statement state = null;
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);		
		state = conn.createStatement();
		
		String callAllDocuments = "SELECT * FROM 서류";
		ResultSet documents = state.executeQuery(callAllDocuments);
		
		ObservableList<Document> documentList = FXCollections.observableArrayList();
		
		while(documents.next())
		{
			java.util.Date submissionDate = new java.util.Date(documents.getDate("제출일").getTime());
			java.util.Date diagnosisDate = new java.util.Date(documents.getDate("진단일").getTime());
			Document temp = new Document(documents.getString("학번"), documents.getInt("서류유형"), submissionDate, diagnosisDate, documents.getString("서류저장경로"), documents.getString("유효여부"));
			documentList.add(temp);
		}
	}
	
	public static void deleteDocument(String studentId, int documentType, java.util.Date submissionDate) throws ClassNotFoundException, SQLException
	{
		Connection conn = null;
		Statement state = null;
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);		
		state = conn.createStatement();
		
		@SuppressWarnings("deprecation")
		java.sql.Date date = new java.sql.Date(submissionDate.getYear(), submissionDate.getMonth(), submissionDate.getDay());	//취소선 그어진거 잘 안쓰는 함수 쓰고있다고 경고 주는건데 대체 기능 없길래 일단 씀

		String deleteDocumentQuery = "DELETE FROM 서류 WHERE 학번=" + studentId + " AND 서류유형=" + documentType + " AND 제출일=" + date;
		state.execute(deleteDocumentQuery);
	}
	
	public static void uploadDocument(String studentId, int documentType, String filePath)	//파일 업로드는 어케 해야하지??
	{
		
	}
	
	public static void updateDocument(String studentId, int documentType, java.util.Date submissionDate, java.util.Date diagnosisDate, boolean isValid) throws ClassNotFoundException, SQLException
	{
		Connection conn = null;
		Statement state = null;
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);		
		state = conn.createStatement();
		
		String sIsValid;
		if(isValid)
			sIsValid = "T";
		else
			sIsValid = "F";
		
		@SuppressWarnings("deprecation")
		java.sql.Date submissionSqlDate = new java.sql.Date(submissionDate.getYear(), submissionDate.getMonth(), submissionDate.getDay());
		@SuppressWarnings("deprecation")
		java.sql.Date diagnosisSqlDate = new java.sql.Date(diagnosisDate.getYear(), diagnosisDate.getMonth(), diagnosisDate.getDay());
		
		String deleteDocumentQuery = "UPDATE 서류 SET 유효여부=" + sIsValid + " WHERE 제출일 = " + submissionSqlDate + " AND 진단일=" + diagnosisSqlDate + " AND 서류유형=" + sIsValid;
		state.execute(deleteDocumentQuery);
	}
}
