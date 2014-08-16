
package com.shiningstone.accounter;

public class AccountData extends DbData {
	private final int TABLE_ID = 6;
	
	int    TypeId;
	int    Category;
	String Name;
	double Balance;
	
	public AccountData(int id, String name,int type_id,int category, double balance) {
		Id       = id;
		TypeId   = type_id;
		Category = category;
		Name     = name;
		Balance  = balance;
	}
	
	public void AddToDb() {
		String fields_values[][] = new String[][] {
				new String[]{ "NAME","TYPE_ID",              "SUB_TYPE_ID",            "ACCOUNT_BALANCE"},
				new String[]{ Name, String.valueOf(TypeId), String.valueOf(Category), String.valueOf(Balance) }
		};
		db.insert( TABLE_ID,fields_values );
	}
	
	public void UpdateDb() {
		db.update( TABLE_ID, new String[]{"NAME","TYPE_ID",        "SUB_TYPE_ID",            "ACCOUNT_BALANCE"}, 
						new String[]{ Name, String.valueOf(TypeId), String.valueOf(Category), String.valueOf(Balance) }, 
						"ID="+Id , null );
	}
}
