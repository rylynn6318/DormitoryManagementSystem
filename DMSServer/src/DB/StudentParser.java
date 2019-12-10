package DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import enums.Gender;
import models.Student;

public class StudentParser {
    //3. 학생테이블에서 학번으로 조회하여 성별을 알아낸다.
    public static Gender getGender(String id) throws Exception {
        String sql = "SELECT 성별 FROM " + DBHandler.INSTANCE.DB_NAME + ".학생 WHERE 학번 = '" + id+"'";
        Connection connection = DBHandler.INSTANCE.getConnection();
        PreparedStatement state = connection.prepareStatement(sql);
        ResultSet resultSet = state.executeQuery();
        
        String result = null;
        while(resultSet.next())
        {
        	result = resultSet.getString("성별");
        }

        state.close();
        DBHandler.INSTANCE.returnConnection(connection);
        
        Gender gender = null;
        if(result != null)
        	gender = Gender.get(result);

        return gender; //.equals("M") ? Gender.Male : Gender.Female;
    }
    
    public static ArrayList<Student> getAllStudent() throws SQLException
    {
    	
    	String sql = "SELECT * FROM " + DBHandler.DB_NAME + ".학생";
    	
    	Connection connection;
		connection = DBHandler.INSTANCE.getConnection();
    	
    	PreparedStatement state;
		state = connection.prepareStatement(sql);
		
		ResultSet rs;
		
		try 
		{
			rs=state.executeQuery();
		}
		catch (SQLException e)
		{
			System.out.println("sql Execute Error");
			state.close();
			DBHandler.INSTANCE.returnConnection(connection);
			return null;
		}
		
		ArrayList<Student> students = new ArrayList<Student>();
		while(rs.next())
		{
			students.add(new Student(rs.getString("학번"), rs.getString("성명"), Gender.get(rs.getString("성별")), rs.getString("학과명"), rs.getInt("학년"), rs.getString("주민등록번호"), rs.getString("학생전화번호"), rs.getString("보호자우편번호"), rs.getString("보호자주소")));
		}
		
		state.close();
		DBHandler.INSTANCE.returnConnection(connection);
		
		return students;
    }
    
    public static boolean isExist(String id) throws Exception 
    {
        String sql = "SELECT * FROM " + DBHandler.INSTANCE.DB_NAME + ".학생 WHERE 학번 = '" + id+"'";
        Connection connection = DBHandler.INSTANCE.getConnection();
        PreparedStatement state = connection.prepareStatement(sql);
        ResultSet resultSet = state.executeQuery();
        
        boolean isExist = false;
        if(resultSet.next())
        {
        	isExist = true;
        }

        state.close();
        DBHandler.INSTANCE.returnConnection(connection);
        
        return isExist;
    }
}