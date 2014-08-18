package com.shiningstone.accounter;

public class TransferData extends DbData {
	double Amount;
	int Account;
	int Item;
	String Date;
	String Memo;
	
	public TransferData() {
	}

	public TransferData(int id, double amount,int account_id,int item_id, String date,String memo)
	{
		Id = id;
		Amount = amount;
		Account = account_id;
		Item = item_id;
		Date = date;
		Memo = memo;
	}

	public void AddToDb() {
		String fields_values[][] = new String[][] {
				new String[]{"AMOUNT","ACCOUNT_ID","ITEM_ID","DATE","MEMO"},
				new String[]{ String.valueOf(Amount), String.valueOf(Account), String.valueOf(Item), Date, Memo }
		};
		db.insert( 6,fields_values );
	}
}
