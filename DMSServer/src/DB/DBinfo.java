package DB;


public enum DBinfo {
	DRIVER_NAME("mysql"),
	HOSTNAME("wehatejava.czztgstzacsv.us-east-1.rds.amazonaws.com"),
	PORT("3306"),
	DB_NAME("Prototype"),											//DB이름
	USER_NAME("admin"),												//DB에 접속할 사용자 이름을 상수로 정의
	PASSWORD("En2i3oHKLGh9UlnbYFP1"),			//사용자의 비밀번호를 상수로 정의
	DB_URL(			"jdbc:" + 
					DRIVER_NAME + "://" + 
					HOSTNAME + ":" + 
					PORT + "/" + 
					DB_NAME + "?user=" + 
					USER_NAME + "&password=" + 
					PASSWORD);
	DBinfo(String s)
	{
		
	}
	
}
