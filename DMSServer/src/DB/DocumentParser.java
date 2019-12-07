package DB;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import enums.Bool;
import enums.Code1;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Document;

public class DocumentParser 
{
	public static ObservableList<Document> getAllDocuments() throws SQLException
	{
		String getDocuments = "SELECT * FROM " + DBHandler.DB_NAME + ".서류 ";
		Connection connection = DBHandler.INSTANCE.getConnetion();
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
        
        return documentList;
	}
	
	public static void deleteDocument(String studentId, int documentType, java.util.Date submissionDate) throws SQLException
	{
		@SuppressWarnings("deprecation")
		java.sql.Date date = new java.sql.Date(submissionDate.getYear(), submissionDate.getMonth(), submissionDate.getDay());    //취소선 그어진거 잘 안쓰는 함수 쓰고있다고 경고 주는건데 대체 기능 없길래 일단 씀

        String deleteDocumentQuery = "DELETE FROM 서류 WHERE 학번=" + studentId + " AND 서류유형=" + documentType + " AND 제출일=" + date;
        Connection connection = DBHandler.INSTANCE.getConnetion();
		PreparedStatement preparedStatement = connection.prepareStatement(deleteDocumentQuery);
		preparedStatement.execute(deleteDocumentQuery);
	}
	
	public static void updateDocument(String sIsValid, Date submissionSqlDate, Date diagnosisSqlDate) throws SQLException
	{
		String updateDocument = "UPDATE 서류 SET 유효여부=" + sIsValid + " WHERE 제출일 = " + submissionSqlDate + " AND 진단일=" + diagnosisSqlDate + " AND 서류유형=" + sIsValid;

        Connection connection = DBHandler.INSTANCE.getConnetion();
		PreparedStatement preparedStatement = connection.prepareStatement(updateDocument);
		preparedStatement.executeUpdate(updateDocument);
	}
}
