
package com.shiningstone.accounter;

public class AccountData extends DbData {
	private final int TABLE_ID = 6;
	
	int    Type;
	int    Subtype;
	String Name;
	double Balance;
	
	public AccountData() {
	}
	
	public AccountData(int id, String name,int type,int subtype, double balance) {
		Id       = id;
		Type     = type;
		Subtype  = subtype;
		Name     = name;
		Balance  = balance;
	}
	
	public void AddToDb() {
		String fields_values[][] = new String[][] {
				new String[]{ "NAME","TYPE_ID",              "SUB_TYPE_ID",            "ACCOUNT_BALANCE"},
				new String[]{ Name, String.valueOf(Type), String.valueOf(Subtype), String.valueOf(Balance) }
		};
		db.insert( TABLE_ID,fields_values );
	}
	
	public void UpdateDb() {
		db.update( TABLE_ID, new String[]{"NAME","TYPE_ID",        "SUB_TYPE_ID",            "ACCOUNT_BALANCE"}, 
						new String[]{ Name, String.valueOf(Type), String.valueOf(Subtype), String.valueOf(Balance) }, 
						"ID="+Id , null );
	}
}
