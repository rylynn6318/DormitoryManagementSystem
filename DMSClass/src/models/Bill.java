package models;

import java.io.Serializable;

public final class Bill implements Serializable
{
	public final String bankName;
	public final int accountNum;
	public final int cost;
	public Bill(String bankName, int accountNum , int cost)
	{
		this.bankName = bankName;
		this.accountNum = accountNum;
		this.cost = cost;
	}
}
