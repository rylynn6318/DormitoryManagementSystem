package logic;

import java.sql.*;

import DB.DBHandler;
import enums.Bool;
import enums.Code1;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Document;

public class DocumentManager {
    public static void lookUpDocuments() throws ClassNotFoundException, SQLException    //FXCollection 몰라서 일단 띵근이가 써놓은거 따라했는데 사용법 아는사람 있으면 확인점
    {
        String callAllDocuments = "SELECT * FROM 서류";
        Connection connection = DBHandler.INSTANCE.getConnetion();
        PreparedStatement state = connection.prepareStatement(callAllDocuments);
        ResultSet documents = state.executeQuery(callAllDocuments);

        ObservableList<Document> documentList = FXCollections.observableArrayList();

        while (documents.next()) {
            java.util.Date submissionDate = new java.util.Date(documents.getDate("제출일").getTime());
            java.util.Date diagnosisDate = new java.util.Date(documents.getDate("진단일").getTime());
            Document temp = new Document(documents.getString("학번"), Code1.FileType.get((byte)documents.getInt("서류유형")), submissionDate, diagnosisDate, documents.getString("서류저장경로"), Bool.get( documents.getString("유효여부")));
            documentList.add(temp);
        }
        state.close();
        DBHandler.INSTANCE.returnConnection(connection);
    }

    public static void deleteDocument(String studentId, int documentType, java.util.Date submissionDate) throws ClassNotFoundException, SQLException {
        @SuppressWarnings("deprecation")
        java.sql.Date date = new java.sql.Date(submissionDate.getYear(), submissionDate.getMonth(), submissionDate.getDay());    //취소선 그어진거 잘 안쓰는 함수 쓰고있다고 경고 주는건데 대체 기능 없길래 일단 씀

        String deleteDocumentQuery = "DELETE FROM 서류 WHERE 학번=" + studentId + " AND 서류유형=" + documentType + " AND 제출일=" + date;
        Connection connection = DBHandler.INSTANCE.getConnetion();
        PreparedStatement state = connection.prepareStatement(deleteDocumentQuery);
        state.executeUpdate();
        state.close();
        DBHandler.INSTANCE.returnConnection(connection);
    }

    public static void uploadDocument(String studentId, int documentType, String filePath)    //파일 업로드는 어케 해야하지??
    {

    }

    public static void updateDocument(String studentId, int documentType, java.util.Date submissionDate, java.util.Date diagnosisDate, Bool isValid) throws ClassNotFoundException, SQLException {
        @SuppressWarnings("deprecation")
        java.sql.Date submissionSqlDate = new java.sql.Date(submissionDate.getYear(), submissionDate.getMonth(), submissionDate.getDay());
        @SuppressWarnings("deprecation")
        java.sql.Date diagnosisSqlDate = new java.sql.Date(diagnosisDate.getYear(), diagnosisDate.getMonth(), diagnosisDate.getDay());

        String deleteDocumentQuery = "UPDATE 서류 SET 유효여부=" + isValid.yn + " WHERE 제출일 = " + submissionSqlDate + " AND 진단일=" + diagnosisSqlDate + " AND 서류유형=" + isValid.yn;
        Connection connection = DBHandler.INSTANCE.getConnetion();
        PreparedStatement state = connection.prepareStatement(deleteDocumentQuery);
        state.executeUpdate();
        state.close();
        DBHandler.INSTANCE.returnConnection(connection);
    }
}
