package DB;

import java.sql.SQLException;

public class AvailablePeriodParser 
{
	public static int getAvailablePeriod() throws SQLException, ClassNotFoundException
	{
		int currentSemester = CurrentSemesterParser.getCurrentSemester();
		int availablePeriod = 0;
		switch(currentSemester % 10)
		{
		case 1:
			availablePeriod = (currentSemester - 1) * 100 + 620; // 1학기
			break;
		case 2:
			availablePeriod = (currentSemester - 2) * 100 + 720; // 여름계절
			break;
		case 3:
			availablePeriod = (currentSemester - 3) * 100 + 820; // 여름방학 전기간
			break;
		case 4:
			availablePeriod = (currentSemester - 4) * 100 + 1220; // 2학기
			break;
		case 5:
			availablePeriod = (currentSemester - 5) * 100 + 10120; // 겨울계절
			break;
		case 6:
			availablePeriod = (currentSemester - 6) * 100 + 10220; // 겨울방학 전기간
			break;
		}
		return availablePeriod;
	}
}
