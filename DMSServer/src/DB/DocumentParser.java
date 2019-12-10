package DB;

import java.nio.file.Path;
import java.sql.*;

import java.util.ArrayList;
import java.util.Collection;

import enums.Bool;
import enums.Code1;
import enums.Code1.FileType;
import models.Document;
import utils.IOHandler;
import utils.Logger;

public class DocumentParser {

    // 모든 서류 반혼
    // 에러 발생시 빈 컬렉션 반환
    public static Collection<Document> getAllDocuments() {
        ArrayList<Document> documentList = new ArrayList<>();

        String getDocuments = "SELECT * FROM " + DBHandler.DB_NAME + ".서류 ";
        System.out.println(getDocuments);

        Connection connection;
        try 
        {
            connection = DBHandler.INSTANCE.getConnection();
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
            return documentList;
        }

        try 
        {
            PreparedStatement preparedStatement = connection.prepareStatement(getDocuments);

            ResultSet documents = preparedStatement.executeQuery();

            while (documents.next()) 
            {
                java.util.Date submissionDate = new java.util.Date(documents.getTimestamp("제출일").getTime());
                Date nullCheck = documents.getDate("진단일");
                java.util.Date diagnosisDate = null;
                if(nullCheck != null)
                	diagnosisDate = new java.util.Date(nullCheck.getTime());
                
                Document temp = new Document(
                        documents.getString("학번"),
                        Code1.FileType.valueOf(documents.getString("서류유형")),
                        submissionDate,
                        diagnosisDate,
                        documents.getString("서류저장경로"),
                        Bool.get(documents.getString("유효여부")));
                documentList.add(temp);
            }

            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        DBHandler.INSTANCE.returnConnection(connection);
        return documentList;
    }
    
    public static boolean isExist(String id, Code1.FileType documentType) throws SQLException
    {   	
    	String sql = "SELECT * FROM " + DBHandler.DB_NAME + ".서류 WHERE 학번='" + id + "' AND 서류유형 = '" + documentType.name() + "'";
    	System.out.println(sql);
    	
    	Connection connection;
		connection = DBHandler.INSTANCE.getConnection();
    	
    	PreparedStatement state;
		state = connection.prepareStatement(sql);
		
		ResultSet rs;
		try
		{
			rs = state.executeQuery();
		}
		catch (SQLException e)
		{
			System.out.println("sql Execute Error");
			state.close();
			DBHandler.INSTANCE.returnConnection(connection);
			return false;
		}
    	
		if(!rs.next())
		{
			state.close();
			DBHandler.INSTANCE.returnConnection(connection);
			return false;
		}
		
		state.close();
		DBHandler.INSTANCE.returnConnection(connection);
		
    	return true;
    }

    // 해당 키값을 가지는 서류 레코드와 파일을 삭제함.
    // 반환값으로 영향을 받은 레코드 갯수를 반환함. 에러 발생시 -2 반환.
    public static int deleteDocument(Code1.FileType documentType, String studentId) {
        int result = -2;

        String deleteDocumentQuery = "DELETE FROM " + DBHandler.DB_NAME + ".서류 WHERE 학번 = '" + studentId + "' AND 서류유형 = '" + documentType.name() + "'";
        System.out.println(deleteDocumentQuery);

        Connection connection;
        try {
            connection = DBHandler.INSTANCE.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return result;
        }

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(deleteDocumentQuery);

            result = preparedStatement.executeUpdate(deleteDocumentQuery);

            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        DBHandler.INSTANCE.returnConnection(connection);

        try {
            Path path = IOHandler.INSTANCE.getFilePath(documentType, studentId);
            boolean isSucceed = IOHandler.INSTANCE.delete(path);
            if(!isSucceed) {
                Logger.INSTANCE.print("DB에서는 삭제 완료, 로컬 서류 파일 삭제 실패.");
            }
        } catch(Exception e) {
            Logger.INSTANCE.print("DB에서는 삭제 완료, 로컬 서류 파일 존재하지 않음.");
            e.printStackTrace();
        }
        return result;
    }

    // insert 수행, 이미 레코드가 있으면 Update
    // 반환값으로 영향을 받은 레코드 갯수를 반환, 에러 발생시 -2 반환.
    public static int renewDocument(Document doc) {
        int result = -2;

        // 제출일은 서버 기본값 사용
        String query =
                "INSERT INTO 서류 (학번, 서류유형, 제출일, 진단일, 서류저장경로, 유효여부) " +
                        "VALUES (?, ?, NOW(), ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE " +
                        "학번 = VALUES(학번), " +
                        "서류유형 = VALUES(서류유형), " +
                        "제출일 = VALUES(제출일), " +
                        "진단일 = VALUES(진단일), " +
                        "서류저장경로 = VALUES(서류저장경로), " +
                        "유효여부 = VALUES(유효여부)";

        Connection connection;
        try {
            connection = DBHandler.INSTANCE.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return result;
        }

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, doc.studentId);
            preparedStatement.setString(2, doc.documentType.name());
            preparedStatement.setDate(3, ScheduleParser.utilDateToSqlDate(doc.diagnosisDate));
            preparedStatement.setString(4, doc.documentStoragePath);
            preparedStatement.setString(5, doc.isValid.yn);

            result = preparedStatement.executeUpdate();

            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        DBHandler.INSTANCE.returnConnection(connection);
        return result;
    }

    // doc에서 일부 정보만 사용한다.
    public static int updateCheck(Document doc) {
        int result = -2;

        String query =
                "Update 서류 Set 유효여부 = ? " +
                        "Where 학번 = ? " +
                        "And 서류유형 = ?";

        Connection connection;
        try {
            connection = DBHandler.INSTANCE.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return result;
        }

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, doc.isValid.yn);
            preparedStatement.setString(2, doc.studentId);
            preparedStatement.setString(3, doc.documentType.name());

            result = preparedStatement.executeUpdate();

            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        DBHandler.INSTANCE.returnConnection(connection);
        return result;
    }

    //차예승씨 어서 대가리 안박고 뭐하십니까?
    //1차 SQL문 쿼리는 분리하는게 좋습니다.
    //정리했는데도 제대로 안돌아가는거같네(명근, 2019-12-09 21:08 수정)
    @SuppressWarnings("deprecation")
    public static Document findDocument(String studentId, FileType fileType) throws ClassNotFoundException, SQLException {
        int currentSemester = CurrentSemesterParser.getCurrentSemester();

        java.util.Date today = new java.util.Date();
        String getSEDaySQL;
        switch (currentSemester % 10) {
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
        ResultSet currentSEDay = preparedStatement.executeQuery();    //current start/end day라는 뜻ㅎ

        Date startDate = null;
        Date endDate = null;

        if (currentSEDay.next()) {
            startDate = currentSEDay.getDate("시작일");
            endDate = currentSEDay.getDate("종료일");
        }

        if (startDate != null)
            System.out.println("시작일 : " + startDate.toString());

        if (endDate != null)
            System.out.println("종료일 : " + endDate.toString());

        //시작일 혹은 종료일이 null, 예외처리
        if (startDate == null || endDate == null) {
            System.out.println("getSEDaySQL 쿼리 이후 시작일 or 종료일이 없음. 예외처리함. code-202");
            return null;
        }

        //필요한 값 꺼내고 제발 커넥션 종료하십시오.
        preparedStatement.close();
        DBHandler.INSTANCE.returnConnection(connection);

        //2차 SQL
        // 여기서 부터는 내가 바꿈
        String getDocument = "SELECT * FROM " + DBHandler.DB_NAME + ".서류 WHERE 학번='" + studentId + "' AND 서류유형='" + fileType.name() + "' AND 제출일 BETWEEN DATE('" + startDate + "') AND DATE('" + endDate + "')";
        System.out.println(getDocument);
        PreparedStatement preparedStatement2 = connection.prepareStatement(getDocument);
        ResultSet foundDocument = preparedStatement2.executeQuery();

        java.util.Date submissionDate;
        java.util.Date diagnosisDate = null;
        Bool isValid;
        String parsedId;
        String filePath;

        if (foundDocument.next()) {
            submissionDate = new java.util.Date(foundDocument.getTimestamp("제출일").getTime());
            java.sql.Date jindan = foundDocument.getDate("진단일");
            if (jindan != null)
                diagnosisDate = new java.util.Date(jindan.getTime());
            isValid = Bool.get(foundDocument.getString("유효여부"));
            parsedId = foundDocument.getString("학번");
            filePath = foundDocument.getString("서류저장경로");
        } else {
            //조회된 내역 없음.
            System.out.println("서류 조회된 내역 없음.");
            return null;
        }

        //필요한 값 꺼내고 제발...또 제발 커넥션 종료하십시오.
        preparedStatement2.close();
        DBHandler.INSTANCE.returnConnection(connection);

        return new Document(parsedId, fileType, submissionDate, diagnosisDate, filePath, isValid);
    }
}
