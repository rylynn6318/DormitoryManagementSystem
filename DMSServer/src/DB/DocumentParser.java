package DB;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import enums.Bool;
import enums.Code1;
import enums.Code1.FileType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Document;

public class DocumentParser 
{
	public static ObservableList<Document> getAllDocuments() throws SQLException
	{
		String getDocuments = "SELECT * FROM " + DBHandler.DB_NAME + ".서류 ";
		Connection connection = DBHandler.INSTANCE.getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement(getDocuments);
		ResultSet documents = preparedStatement.executeQuery();
		
		ObservableList<Document> documentList = FXCollections.observableArrayList();

        while (documents.next()) 
        {
            java.util.Date submissionDate = new java.util.Date(documents.getDate("제출일").getTime());
            java.util.Date diagnosisDate = new java.util.Date(documents.getDate("진단일").getTime());
            Document temp = new Document(documents.getString("학번"), Code1.FileType.get((byte)documents.getInt("서류유형")), submissionDate, diagnosisDate, documents.getString("서류저장경로"), Bool.get( documents.getString("유효여부")));
            documentList.add(temp);
        }

        preparedStatement.close();
        DBHandler.INSTANCE.returnConnection(connection);
        
        return documentList;
	}
	
	public static void deleteDocument(String studentId, Code1.FileType documentType, java.util.Date submissionDate) throws SQLException
	{
		@SuppressWarnings("deprecation")
		java.sql.Date date = new java.sql.Date(submissionDate.getYear(), submissionDate.getMonth(), submissionDate.getDay());    //취소선 그어진거 잘 안쓰는 함수 쓰고있다고 경고 주는건데 대체 기능 없길래 일단 씀

        String deleteDocumentQuery = "DELETE FROM 서류 WHERE 학번=" + studentId + " AND 서류유형=" + documentType + " AND 제출일=" + date;
        Connection connection = DBHandler.INSTANCE.getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement(deleteDocumentQuery);

		preparedStatement.execute(deleteDocumentQuery);

		preparedStatement.close();
		DBHandler.INSTANCE.returnConnection(connection);
	}
	
	public static void updateDocument(Bool isValid, Date submissionSqlDate, Date diagnosisSqlDate, Code1.FileType type) throws SQLException
	{
		String updateDocument = "UPDATE 서류 SET 유효여부=" + isValid.yn + " WHERE 제출일 = " + submissionSqlDate + " AND 진단일=" + diagnosisSqlDate + " AND 서류유형=" + type;

        Connection connection = DBHandler.INSTANCE.getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement(updateDocument);

		preparedStatement.executeUpdate(updateDocument);

		preparedStatement.close();
		DBHandler.INSTANCE.returnConnection(connection);
	}
	
	//차예승씨 어서 대가리 안박고 뭐하십니까?
	//1차 SQL문 쿼리는 분리하는게 좋습니다.
	//정리했는데도 제대로 안돌아가는거같네(명근, 2019-12-09 21:08 수정)
	public static Document findDocument(String studentId, FileType fileType) throws ClassNotFoundException, SQLException 
	{
		int currentSemester = CurrentSemesterParser.getCurrentSemester();
		
		java.util.Date today = new java.util.Date();
		String getSEDaySQL;
		switch(currentSemester%10)
		{
		case 1:
			getSEDaySQL = "SELECT 시작일, 종료일, 비고 FROM " + DB.DBHandler.DB_NAME + ".스케쥴  WHERE `스케쥴 할일 코드_ID` = 6 AND 시작일>=" + today.getYear() + "0101";
			break;
		case 2:
			getSEDaySQL = "SELECT 시작일, 종료일, 비고 FROM " + DB.DBHandler.DB_NAME + ".스케쥴  WHERE `스케쥴 할일 코드_ID` = 7 AND 시작일>=" + today.getYear() + "0101";
			break;
		case 3:
			getSEDaySQL = "SELECT 시작일, 종료일, 비고 FROM " + DB.DBHandler.DB_NAME + ".스케쥴  WHERE `스케쥴 할일 코드_ID` = 8 AND 시작일>=" + today.getYear() + "0101"; 
			break;
		case 4:
			getSEDaySQL = "SELECT 시작일, 종료일, 비고 FROM " + DB.DBHandler.DB_NAME + ".스케쥴  WHERE `스케쥴 할일 코드_ID` = 9 AND 시작일>=" + today.getYear() + "0101";
			break;
		default:
			return null;
		}
		
		//1차 SQL문 확인
		System.out.println(getSEDaySQL);
		
		Connection connection = DBHandler.INSTANCE.getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement(getSEDaySQL);
		ResultSet currentSEDay = preparedStatement.executeQuery();	//current start/end day라는 뜻ㅎ

		Date startDate = null;
		Date endDate = null;
		
		if(currentSEDay.next())
		{
			startDate = currentSEDay.getDate("시작일");
			endDate = currentSEDay.getDate("종료일");
		}
		
		if(startDate != null)
			System.out.println("시작일 : " + startDate.toString());
		
		if(endDate != null)
			System.out.println("종료일 : " + endDate.toString());
		
		//시작일 혹은 종료일이 null, 예외처리
		if(startDate == null || endDate == null)
		{
			System.out.println("getSEDaySQL 쿼리 이후 시작일 or 종료일이 없음. 예외처리함. code-202");
			return null;
		}
		
		String sFileType = fileType == FileType.MEDICAL_REPORT ? "1" : "2";
		
		//필요한 값 꺼내고 제발 커넥션 종료하십시오.
		preparedStatement.close();
		DBHandler.INSTANCE.returnConnection(connection);
		
		//2차 SQL
		String getDocument = "SELECT * FROM " + DBHandler.DB_NAME + ".서류 WHERE 학번=" + studentId + " AND 서류유형=" + sFileType + " AND 제출일 BETWEEN DATE('" + startDate + "') AND DATE('" + endDate + "')";
		System.out.println(getDocument);
		PreparedStatement preparedStatement2 = connection.prepareStatement(getDocument);
		ResultSet foundDocument = preparedStatement2.executeQuery();
        
		java.util.Date submissionDate = null;
		java.util.Date diagnosisDate= null;
		String isValidStr= null;
		String parsedId= null;
		String filePath = null;
		
        if(foundDocument.next())
        {
        	submissionDate = new java.util.Date(foundDocument.getDate("제출일").getTime());
            diagnosisDate = new java.util.Date(foundDocument.getDate("진단일").getTime());
            isValidStr = foundDocument.getString("유효여부"); 
            parsedId = foundDocument.getString("학번");
            filePath = foundDocument.getString("서류저장경로");
        }
        else
        {
        	//조회된 내역 없음.
        	System.out.println("서류 조회된 내역 없음.");
        	return null;
        }
        
        //파일 찾은거 객체화.
        
		//필요한 값 꺼내고 제발...또 제발 커넥션 종료하십시오.
        preparedStatement2.close();
		DBHandler.INSTANCE.returnConnection(connection);
		
		//예외처리한다.
		Bool isValid = null;
		if(isValidStr != null)
			isValid = isValidStr.equals("Y") ? Bool.TRUE : Bool.FALSE;
        
        //문서객체 만들다가 예외가 날수있으니...
        Document document = null;
        try
        {
        	document = new Document(parsedId, fileType, submissionDate, diagnosisDate, filePath, isValid);        	
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
		return document;
	}
}
