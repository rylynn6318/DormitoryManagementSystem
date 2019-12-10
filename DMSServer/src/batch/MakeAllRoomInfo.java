package batch;

import java.sql.SQLException;
import DB.DormParser;

public class MakeAllRoomInfo {
	public static AssignRoomInfo[] getO1() throws ClassNotFoundException, SQLException
	{
		AssignRoomInfo[] O1 = new AssignRoomInfo[DormParser.getMaxCapacity("오름1")];
		for(int i = 0; i < O1.length; i++)
		{
			O1[i] = new AssignRoomInfo();
			O1[i].setDormitoryName("오름1");
			if(i % 2 == 0)
			{
				O1[i].setSeat("A");
			}
			else
			{
				O1[i].setSeat("B");
			}
		}
		return O1;
	}
	
	public static AssignRoomInfo[] getO2() throws ClassNotFoundException, SQLException
	{
		AssignRoomInfo[] O2 = new AssignRoomInfo[DormParser.getMaxCapacity("오름2")];
		for(int i = 0; i < O2.length; i++)
		{
			O2[i] = new AssignRoomInfo();
			O2[i].setDormitoryName("오름2");
			if(i % 2 == 0)
			{
				O2[i].setSeat("A");
			}
			else
			{
				O2[i].setSeat("B");
			}
		}
		return O2;
	}
	
	public static AssignRoomInfo[] getO3() throws ClassNotFoundException, SQLException
	{
		AssignRoomInfo[] O3 = new AssignRoomInfo[DormParser.getMaxCapacity("오름3")];
		for(int i = 0; i < O3.length; i++)
		{
			O3[i] = new AssignRoomInfo();
			O3[i].setDormitoryName("오름3");
			if(i % 2 == 0)
			{
				O3[i].setSeat("A");
			}
			else
			{
				O3[i].setSeat("B");
			}
		}
		return O3;
	}
	
	public static AssignRoomInfo[] getP1() throws ClassNotFoundException, SQLException
	{
		AssignRoomInfo[] P1 = new AssignRoomInfo[DormParser.getMaxCapacity("푸름1") + DormParser.getMaxCapacity("푸름1_탑층")];
		for(int i = 0; i < P1.length; i++)
		{
			P1[i] = new AssignRoomInfo();
			P1[i].setDormitoryName("푸름1");
			if(i % 2 == 0)
			{
				P1[i].setSeat("A");
			}
			else
			{
				P1[i].setSeat("B");
			}
		}
		return P1;
	}
	
	public static AssignRoomInfo[] getP2() throws ClassNotFoundException, SQLException
	{
		AssignRoomInfo[] P2 = new AssignRoomInfo[DormParser.getMaxCapacity("푸름2") + DormParser.getMaxCapacity("푸름2_탑층")];
		for(int i = 0; i < P2.length; i++)
		{
			P2[i] = new AssignRoomInfo();
			P2[i].setDormitoryName("푸름2");
			if(i % 2 == 0)
			{
				P2[i].setSeat("A");
			}
			else
			{
				P2[i].setSeat("B");
			}
		}
		return P2;
	}
	
	public static AssignRoomInfo[] getP3() throws ClassNotFoundException, SQLException
	{
		AssignRoomInfo[] P3 = new AssignRoomInfo[DormParser.getMaxCapacity("푸름3") + DormParser.getMaxCapacity("푸름3_탑층")];
		for(int i = 0; i < P3.length; i++)
		{
			P3[i] = new AssignRoomInfo();
			P3[i].setDormitoryName("푸름3");
			if(i % 4 == 0)
			{
				P3[i].setSeat("A");
			}
			else if(i % 4 == 1)
			{
				P3[i].setSeat("B");
			}
			else if(i % 4 == 2)
			{
				P3[i].setSeat("C");
			}
			else if(i % 4 == 3)
			{
				P3[i].setSeat("D");
			}
		}
		return P3;
	}
	
	public static AssignRoomInfo[] getP4() throws ClassNotFoundException, SQLException
	{
		AssignRoomInfo[] P4 = new AssignRoomInfo[DormParser.getMaxCapacity("푸름4") + DormParser.getMaxCapacity("푸름4_탑층")];
		for(int i = 0; i < P4.length; i++)
		{
			P4[i] = new AssignRoomInfo();
			P4[i].setDormitoryName("푸름4");
			if(i % 4 == 0)
			{
				P4[i].setSeat("A");
			}
			else if(i % 4 == 1)
			{
				P4[i].setSeat("B");
			}
			else if(i % 4 == 2)
			{
				P4[i].setSeat("C");
			}
			else if(i % 4 == 3)
			{
				P4[i].setSeat("D");
			}
		}
		return P4;
	}
	
	public static AssignRoomInfo[] getSN() throws ClassNotFoundException, SQLException
	{
		AssignRoomInfo[] SN = new AssignRoomInfo[DormParser.getMaxCapacity("신평남")];
		for(int i = 0; i < SN.length; i++)
		{
			SN[i] = new AssignRoomInfo();
			SN[i].setDormitoryName("신평남");
			if(i % 2 == 0)
			{
				SN[i].setSeat("A");
			}
			else
			{
				SN[i].setSeat("B");
			}
		}
		return SN;
	}
	
	public static AssignRoomInfo[] getSY() throws ClassNotFoundException, SQLException
	{
		AssignRoomInfo[] SY = new AssignRoomInfo[DormParser.getMaxCapacity("신평여")];
		for(int i = 0; i < SY.length; i++)
		{
			SY[i] = new AssignRoomInfo();
			SY[i].setDormitoryName("신평여");
			if(i % 2 == 0)
			{
				SY[i].setSeat("A");
			}
			else
			{
				SY[i].setSeat("B");
			}
		}
		return SY;
	}
}

