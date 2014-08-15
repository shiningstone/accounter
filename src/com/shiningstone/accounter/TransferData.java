package com.shiningstone.accounter;

import android.app.Activity;
import android.os.Bundle;

public class TransferData extends Activity {
	int id;
	double amount;
	int account_id;
	int item_id;
	String date;
	String memo;
	
	public TransferData(int id, double amount,int account_id,int item_id, String date,String memo)
	{
		this.id = id;
		this.amount = amount;
		this.account_id = account_id;
		this.item_id = item_id;
		this.date = date;
		this.memo = memo;
	}
	
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
