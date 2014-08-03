
package com.shiningstone.accounter.db;

class Field {
	public static final int ID = 0;
	public static final int NAME = 1;
	public static final int AMOUNT = 2;

	public String Name;
	public String Type;
	
	public Field(String name,String type) {
		Name = name;
		Type = type;
	}
};

class Table {
	public String Name;
	public Field[] Fields;
	private int idx;
	
	public Table(String name) {
		Name = name;
		idx = 0;
		
		AddField(Field.ID);
	}
	
	public void AddField(String fname,String ftype) {
		Fields[idx] = new Field(fname,ftype);
		idx++;
	}
	
	public void AddField(int type) {
		switch( type ) {
			case Field.ID:
				AddField("ID","INTEGER PRIMARY KEY AUTOINCREMENT");
				break;
			case Field.NAME:
				AddField("NAME","TEXT");
				break;
			case Field.AMOUNT:
				AddField("AMOUNT","DOUBLE");
				break;
			default:
				break;
		}
	}
};

public class MyDbInfo {
	public static Table[] Tables = null;
	private static String[] TableNames = null;
	private static String[][] FieldNames = null;
	private static String[][] FieldTypes = null;

	public MyDbInfo() {
		TablesInit();
		
		int tblIdx = 0;
		for(; tblIdx<Tables.length; tblIdx++) {
			TableNames[tblIdx] = Tables[tblIdx].Name;
			
			int fieldIdx = 0;
			for(; fieldIdx<Tables[tblIdx].Fields.length; fieldIdx++) {
				FieldNames[tblIdx][fieldIdx] = Tables[tblIdx].Fields[fieldIdx].Name;
				FieldTypes[tblIdx][fieldIdx] = Tables[tblIdx].Fields[fieldIdx].Type;
			}
		}
	}
	
	public static String[] getTableNames() {
		return TableNames;
	}
	
	public static String[][] getFieldNames() {
		return FieldNames;
	}
	
	public static String[][] getFieldTypes() {
		return FieldTypes;
	}
	
	private void TablesInit() {
		Tables[0] = new Table("EXPENSE_CATEGORY");
		Tables[0].AddField(Field.NAME);
		Tables[0].AddField("BUDGET","DOUBLE");

		Tables[1] = new Table("EXPENSE_SUB_CATEGORY");
		Tables[1].AddField(Field.NAME);
		Tables[1].AddField("PARENT_CATEGORY_ID","INTEGER");

		Tables[2] = new Table("INCOME_CATEGORY");
		Tables[2].AddField(Field.NAME);

		Tables[3] = new Table("INCOME_SUB_CATEGORY");
		Tables[3].AddField(Field.NAME);
		Tables[3].AddField("PARENT_CATEGORY_ID","INTEGER");
		
		Tables[4] = new Table("ACCOUNT_TYPE");
		Tables[4].AddField(Field.NAME);
		Tables[4].AddField("POSTIVE","DOUBLE");
		
		Tables[5] = new Table("ACCOUNT_SUB_TYPE");
		Tables[5].AddField(Field.NAME);
		Tables[5].AddField("PARENT_CATEGORY_ID","INTEGER");
		
		Tables[6] = new Table("ACCOUNT");
		Tables[6].AddField(Field.NAME);
		Tables[6].AddField("TYPE_ID","INTEGER");
		Tables[6].AddField("SUB_TYPE_ID","INTEGER");
		Tables[6].AddField("ACCOUNT_BALANCE","DOUBLE");
		
		Tables[7] = new Table("STORE");
		Tables[7].AddField(Field.NAME);
		
		Tables[8] = new Table("ITEM");
		Tables[8].AddField(Field.NAME);
		
		Tables[9] = new Table("EXPENSE");
		Tables[9].AddField(Field.AMOUNT);
		Tables[9].AddField("EXPENDITURE_CATEGORY_ID","INTEGER");
		Tables[9].AddField("EXPENDITURE_SUB_CATEGORY_ID","INTEGER");
		Tables[9].AddField("ACCOUNT_ID","INTEGER");
		Tables[9].AddField("STORE_ID","INTEGER");
		Tables[9].AddField("ITEM_ID","INTEGER");
		Tables[9].AddField("DATE","TEXT");
		Tables[9].AddField("MEMO","TEXT");
		
		Tables[10] = new Table("INCOME");
		Tables[10].AddField(Field.AMOUNT);
		Tables[10].AddField("INCOME_CATEGORY_ID","INTEGER");
		Tables[10].AddField("INCOME_SUB_CATEGORY_ID","INTEGER");
		Tables[10].AddField("ACCOUNT_ID","INTEGER");
		Tables[10].AddField("ITEM_ID","INTEGER");
		Tables[10].AddField("DATE","TEXT");
		Tables[10].AddField("MEMO","TEXT");
		
		Tables[11] = new Table("TRANSFER");
		Tables[11].AddField(Field.AMOUNT);
		Tables[11].AddField("ACCOUNT_ID","INTEGER");
		Tables[11].AddField("ITEM_ID","INTEGER");
		Tables[11].AddField("DATE","TEXT");
		Tables[11].AddField("MEMO","TEXT");
	}
}
