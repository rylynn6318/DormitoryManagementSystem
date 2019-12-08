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
	
	@SuppressWarnings("deprecation")
	public static Document findDocument(String studentId, FileType fileType) throws ClassNotFoundException, SQLException 
	{
		int currentSemester = CurrentSemesterParser.getCurrentSemester();
		
		java.util.Date today = new java.util.Date();
		String getSEDay;
		switch(currentSemester%10)
		{
		case 1:
			getSEDay = "SELECT 시작일, 종료일, 비고 FROM " + DB.DBHandler.DB_NAME + ".스케쥴  WHERE `스케쥴 할일 코드_ID` = 6 AND 시작일>=" + today.getYear() + "0101";
			break;
		case 2:
			getSEDay = "SELECT 시작일, 종료일, 비고 FROM " + DB.DBHandler.DB_NAME + ".스케쥴  WHERE `스케쥴 할일 코드_ID` = 7 AND 시작일>=" + today.getYear() + "0101";
			break;
		case 3:
			getSEDay = "SELECT 시작일, 종료일, 비고 FROM " + DB.DBHandler.DB_NAME + ".스케쥴  WHERE `스케쥴 할일 코드_ID` = 8 AND 시작일>=" + today.getYear() + "0101"; 
			break;
		case 4:
			getSEDay = "SELECT 시작일, 종료일, 비고 FROM " + DB.DBHandler.DB_NAME + ".스케쥴  WHERE `스케쥴 할일 코드_ID` = 9 AND 시작일>=" + today.getYear() + "0101";
			break;
		default:
			getSEDay = "";		//실행될 리는 없는 부분이지만 선언 안해두면 아래에서 getSEDay를 못씀;	
		}
		
		Connection connection = DBHandler.INSTANCE.getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement(getSEDay);
		ResultSet currentSEDay = preparedStatement.executeQuery();	//current start/end day라는 뜻ㅎ
		currentSEDay.next();
		
		String sFileType;
		if(fileType == FileType.MEDICAL_REPORT)
			sFileType = "1";
		else
			sFileType = "2";
		
		String getDocument = "SELECT * FROM " + DBHandler.DB_NAME + ".서류 WHERE 학번=" + studentId + " AND 서류유형=" + sFileType + " AND 제출일 BETWEEN DATE('" + currentSEDay.getDate("시작일") + "') AND DATE('" + currentSEDay.getDate("종료일") + "')";
		PreparedStatement preparedStatement2 = connection.prepareStatement(getDocument);
		ResultSet foundDocument = preparedStatement2.executeQuery();
        foundDocument.next();
        
        java.util.Date submissionDate = new java.util.Date(foundDocument.getDate("제출일").getTime());
        java.util.Date diagnosisDate = new java.util.Date(foundDocument.getDate("진단일").getTime());
		
        Bool isValid;
        if(foundDocument.getString("유효여부").equals("Y"))
        	isValid = Bool.TRUE;
        else
        	isValid = Bool.FALSE;
        Document document = new Document(foundDocument.getString("학번"), fileType, submissionDate, diagnosisDate, foundDocument.getString("서류저장경로"), isValid);
		return document;
	}
}
