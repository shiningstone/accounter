
package com.shiningstone.accounter;

public class BudgetData extends DbData {
	private final int TABLE_ID = 0;

	int    Category;
	String Name;
	double Balance;
	
	public BudgetData(int id, String name,int category, double balance)	{
		Id = id;
		Name = name;
		Category = category;
		Balance = balance;
	}
	
	public void AddToDb() {
		
	}
	
	public void UpdateDb() {
		db.update( TABLE_ID, new String[]{"BUDGET"}, new String[]{ String.valueOf(Balance) }, "ID="+Id, null );
	}
}
