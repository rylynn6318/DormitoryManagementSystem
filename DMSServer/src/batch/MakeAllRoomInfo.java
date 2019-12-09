package batch;

import java.sql.SQLException;
import DB.DormParser;

public class MakeAllRoomInfo {
	static int maxO1, maxO2, maxO3, maxP1, maxP2, maxP3, maxP4, maxSN, maxSY;
	public static void getCapacity() throws ClassNotFoundException, SQLException
	{
		int maxCapacity[] = DormParser.getMaxCapacity();
		maxO1 = maxCapacity[0];
		maxO2 = maxCapacity[1];
		maxO3 = maxCapacity[2];
		maxP1 = maxCapacity[3];
		maxP2 = maxCapacity[4];
		maxP3 = maxCapacity[5];
		maxP4 = maxCapacity[6];
		maxSN = maxCapacity[7];
		maxSY = maxCapacity[8];
	}
	public static AssignRoomInfo[] getO1() throws ClassNotFoundException, SQLException
	{
		AssignRoomInfo[] O1 = new AssignRoomInfo[maxO1];
		for(int i = 1; i < O1.length; i++)
		{
			O1[i] = new AssignRoomInfo();
			O1[i].setDormitoryName("오름1");
			if(i % 2 == 1)
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
	
	public static AssignRoomInfo[] getO2()
	{
		AssignRoomInfo[] O2 = new AssignRoomInfo[maxO2];
		for(int i = 1; i < O2.length; i++)
		{
			O2[i] = new AssignRoomInfo();
			O2[i].setDormitoryName("오름2");
			if(i % 2 == 1)
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
	
	public static AssignRoomInfo[] getO3()
	{
		AssignRoomInfo[] O3 = new AssignRoomInfo[maxO3];
		for(int i = 1; i < O3.length; i++)
		{
			O3[i] = new AssignRoomInfo();
			O3[i].setDormitoryName("오름3");
			if(i % 2 == 1)
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
	
	public static AssignRoomInfo[] getP1()
	{
		AssignRoomInfo[] P1 = new AssignRoomInfo[maxP1];
		for(int i = 1; i < P1.length; i++)
		{
			P1[i] = new AssignRoomInfo();
			P1[i].setDormitoryName("푸름1");
			if(i % 2 == 1)
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
	
	public static AssignRoomInfo[] getP2()
	{
		AssignRoomInfo[] P2 = new AssignRoomInfo[maxP2];
		for(int i = 1; i < P2.length; i++)
		{
			P2[i] = new AssignRoomInfo();
			P2[i].setDormitoryName("푸름2");
			if(i % 2 == 1)
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
	
	public static AssignRoomInfo[] getP3()
	{
		AssignRoomInfo[] P3 = new AssignRoomInfo[maxP3];
		for(int i = 1; i < P3.length; i++)
		{
			P3[i] = new AssignRoomInfo();
			P3[i].setDormitoryName("푸름3");
			if(i % 4 == 1)
			{
				P3[i].setSeat("A");
			}
			else if(i % 4 == 2)
			{
				P3[i].setSeat("B");
			}
			else if(i % 4 == 3)
			{
				P3[i].setSeat("C");
			}
			else if(i % 4 == 0)
			{
				P3[i].setSeat("D");
			}
		}
		return P3;
	}
	
	public static AssignRoomInfo[] getP4()
	{
		AssignRoomInfo[] P4 = new AssignRoomInfo[maxP4];
		for(int i = 1; i < P4.length; i++)
		{
			P4[i] = new AssignRoomInfo();
			P4[i].setDormitoryName("푸름4");
			if(i % 4 == 1)
			{
				P4[i].setSeat("A");
			}
			else if(i % 4 == 2)
			{
				P4[i].setSeat("B");
			}
			else if(i % 4 == 3)
			{
				P4[i].setSeat("C");
			}
			else if(i % 4 == 0)
			{
				P4[i].setSeat("D");
			}
		}
		return P4;
	}
	
	public static AssignRoomInfo[] getSN()
	{
		AssignRoomInfo[] SN = new AssignRoomInfo[maxSN];
		for(int i = 1; i < SN.length; i++)
		{
			SN[i] = new AssignRoomInfo();
			SN[i].setDormitoryName("신평남");
			if(i % 2 == 1)
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
	
	public static AssignRoomInfo[] getSY()
	{
		AssignRoomInfo[] SY = new AssignRoomInfo[maxSY];
		for(int i = 1; i < SY.length; i++)
		{
			SY[i] = new AssignRoomInfo();
			SY[i].setDormitoryName("신평여");
			if(i % 2 == 1)
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

