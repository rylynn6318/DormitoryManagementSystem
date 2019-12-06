package DB;
public class DBinfo {
	public static final String DRIVER_NAME = "mysql";
	public static final String HOSTNAME = "wehatejava.czztgstzacsv.us-east-1.rds.amazonaws.com";
	public static final String PORT = "3306";
	public static final String DB_NAME = "Prototype";													//DB이름
	public static final String USER_NAME = "admin"; 													//DB에 접속할 사용자 이름을 상수로 정의
	public static final String PASSWORD = "En2i3oHKLGh9UlnbYFP1"; 										//사용자의 비밀번호를 상수로 정의
	public static final String DB_URL =
					"jdbc:" + 
					DRIVER_NAME + "://" + 
					HOSTNAME + ":" + 
					PORT + "/" + 
					DB_NAME + "?user=" + 
					USER_NAME + "&password=" + 
					PASSWORD; 
	
}
