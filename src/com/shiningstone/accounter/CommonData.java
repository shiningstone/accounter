
package com.shiningstone.accounter;

import java.util.ArrayList;
import java.util.HashMap;

import com.shiningstone.accounter.db.MyDbHelper;
import com.shiningstone.accounter.db.MyDbInfo;

import android.content.Context;
import android.database.Cursor;

public class CommonData {
	/***********************************************************
						data
	***********************************************************/
	public HashMap<String, Category> mCat    = new HashMap<String, Category>();
	public HashMap<String, Category> mSubCat = new HashMap<String, Category>();

	/***********************************************************
						singleton
	***********************************************************/
	private static CommonData instance = null;
	public static CommonData getInstance() {
		if (instance == null) {
			instance = new CommonData();
		}
		return instance;
	}
	
	/***********************************************************
						db operations
	***********************************************************/
	MyDbHelper db     = MyDbHelper.getInstance(null);
	
	public void load(Context context) {
		LoadCategories();
		LoadAccounts();
	}

	/**************************************************
			account operations
	**************************************************/
	public boolean IsAccountExist(String name)
	{
		for ( AccountData adata : mAccount.values() ) {
			if ( adata.name.equals(name) )
				return true;
		}
		
		return false;
	}
	
	public boolean Add(AccountData data)
	{
		data.id = mMaxAccountId + 1;
		mAccount.put(data.id, data);
		
		String fields_values[][] = new String[][] {
				new String[]{"NAME","TYPE_ID","SUB_TYPE_ID","ACCOUNT_BALANCE"},
				new String[]{data.name,String.valueOf(data.type_id),String.valueOf(data.category),String.valueOf(data.balance)}
		};
		db.insert( 6,fields_values );
		
		CalcAcounts();

		return true;
	}

	public void Update(AccountData data)
	{
		mAccount.put(data.id, data);
		
		String values[] = new String[]{
				data.name,
				String.valueOf(data.type_id),
				String.valueOf(data.category),
				String.valueOf(data.balance)
		};
		db.update( 6, new String[]{"NAME","TYPE_ID","SUB_TYPE_ID","ACCOUNT_BALANCE"}, values, "ID="+data.id , null );
		
		CalcAcounts();
	}

	public void Delete(int id)
	{
		mAccount.remove(id);
		db.delete( 6, "ID="+id, null );
		CalcAcounts();
	}

	/**************************************************
			transfer operations
	**************************************************/
	public boolean Add(TransferData data)
	{
		String fields_values[][] = new String[][] {
				new String[]{"AMOUNT","ACCOUNT_ID","ITEM_ID","DATE","MEMO"},
				new String[]{String.valueOf(data.amount), String.valueOf(data.account_id),
							String.valueOf(data.item_id), data.date, data.memo}
		};
		db.insert( 6,fields_values );
		
		AccountData account = mAccount.get(data.account_id);
		account.balance = account.balance + data.amount;
		Update(account);
		
		CalcAcounts();
		
		return true;
	}

	/**************************************************
			category data
	**************************************************/
	public class Category {
		int    id;
		int    icon;
		int    type;
		String name;

		public Category(int id, int icon, String name, int type) {
			this.id = id;
			this.icon = icon;
			this.name = name;
			this.type = type;
		}
	}
	
	public class SubCategory extends Category {
		int    parent;

		public SubCategory(int id, int icon, String name, int type, int parent) {
			super(id,icon,name,type);
			this.parent = parent;
		}
	} 
	
	/**************************************************
			account data
	**************************************************/
	class AccountType {
		int		id;
		String 	name;
		int     postive;

		public AccountType(int id, String name,int postive) {
			this.id = id;
			this.name = name;
			this.postive = postive;
		}
	}

	class AccountSubtype {
		int		parent;
		int		id;
		String 	name;

		public AccountSubtype(int id, int parent, String name) {
			this.id = id;
			this.parent = parent;
			this.name = name;
		}
	}

	class TransferItem {
		int		id;
		String 	name;

		public TransferItem(int id, String name) {
			this.id   = id;
			this.name = name;
		}
	}
	
	public HashMap<Integer, AccountType> mAccountType       = new HashMap<Integer, AccountType>();
	public HashMap<Integer, AccountSubtype> mAccountSubType = new HashMap<Integer, AccountSubtype>();
	public HashMap<Integer, AccountData> mAccount           = new HashMap<Integer, AccountData>();
	public HashMap<Integer, TransferItem> mTransfer         = new HashMap<Integer, TransferItem>();

	public double mAsset, mLiability;
	private int mMaxAccountId = 0;
	
	
	public HashMap<Integer, String> shop = new HashMap<Integer, String>();
	public HashMap<Integer, String> purpose = new HashMap<Integer, String>();
	
	/*********************************************************
			private methods
	*********************************************************/
	final int EXPENSE = 1;
	final int INCOME  = 0;
	
	private void LoadCategories()
	{
		mCat.clear();
		mSubCat.clear();
		
		LoadExpenseCategory();
		LoadExpenseSubCategory();
		LoadIncomeCategory();
		LoadIncomeSubCategory();
	}

	private void LoadExpenseCategory() {
		ArrayList<Integer> ids   = new ArrayList<Integer>();
		ArrayList<String>  names = new ArrayList<String>();
		
		db.LoadIdName(ids,names,0);
		for( int i=0; i<ids.size(); i++ ) {
			mCat.put( "out"+ids.get(i), 
						new Category(mCat.size(), R.drawable.default_subcategory_icon, names.get(i), EXPENSE) );
		}
	}
	
	private void LoadExpenseSubCategory() {
		ArrayList<Integer> ids   = new ArrayList<Integer>();
		ArrayList<String>  names = new ArrayList<String>();

		db.LoadIdName(ids,names,1);
		for( int i=0; i<ids.size(); i++ ) {
			mSubCat.put( "out"+ids.get(i), 
						new SubCategory(mSubCat.size(), R.drawable.default_subcategory_icon, names.get(i), EXPENSE, 0) );
		}
	}
	
	private void LoadIncomeCategory() {
		ArrayList<Integer> ids   = new ArrayList<Integer>();
		ArrayList<String>  names = new ArrayList<String>();

		db.LoadIdName(ids,names,2);
		for( int i=0; i<ids.size(); i++ ) {
			mCat.put( "in"+ids.get(i), 
						new Category(mCat.size(), R.drawable.default_firstlevelcategory_icon, names.get(i), INCOME ) );
		}
	}
	
	private void LoadIncomeSubCategory() {
		ArrayList<Integer> ids   = new ArrayList<Integer>();
		ArrayList<String>  names = new ArrayList<String>();

		db.LoadIdName(ids,names,3);
		for( int i=0; i<ids.size(); i++ ) {
			mSubCat.put( "in"+ids.get(i), 
						new SubCategory(mSubCat.size(), R.drawable.default_subcategory_icon, names.get(i), INCOME, 0) );
		}
	}
	
	private void LoadAccounts() {
		ArrayList<Integer> ids   = new ArrayList<Integer>();
		ArrayList<String>  names = new ArrayList<String>();
		ArrayList<Integer> nums  = new ArrayList<Integer>();
		ArrayList<Integer> types  = new ArrayList<Integer>();
		ArrayList<Integer> cates  = new ArrayList<Integer>();
		ArrayList<Double> amounts = new ArrayList<Double>();
		
		mAccountType.clear();
		mAccountSubType.clear();
		mAccount.clear();

		db.LoadIdNameNum(ids,names,nums,4);
		for( int i=0; i<ids.size(); i++ ) {
			mAccountType.put( ids.get(i), new AccountType(ids.get(i), names.get(i), nums.get(i)) );
		}
		
		db.LoadIdNameNum(ids,names,nums,5);
		for( int i=0; i<ids.size(); i++ ) {
			mAccountSubType.put( ids.get(i), new AccountSubtype(ids.get(i), nums.get(i), names.get(i)) );
		}
		
		db.LoadAccountData(ids,names,types,cates,amounts,6);
		for( int i=0; i<ids.size(); i++ ) {
			mAccount.put( ids.get(i), new AccountData(ids.get(i), names.get(i), types.get(i), cates.get(i), amounts.get(i)) );
		}

		db.LoadIdName(ids,names,8);
		for( int i=0; i<ids.size(); i++ ) {
			mTransfer.put( ids.get(i), new TransferItem( ids.get(i), names.get(i) ) );
		}
		
		CalcAcounts();
	}
	
	private void CalcAcounts() {
		Cursor cursor = db.rawQuery("select * " +
				"from (select sum(ACCOUNT_BALANCE) from ACCOUNT where ACCOUNT_BALANCE>0) as positive " +
				",(select sum(ACCOUNT_BALANCE) from ACCOUNT where ACCOUNT_BALANCE<0) as negative", null);
		if (cursor.moveToNext()) {
			mAsset     = cursor.getDouble(0);
			mLiability = cursor.getDouble(1);
		}
		cursor.close();
	}

	public HashMap<Integer, BudgetData> mBudgetData = new HashMap<Integer, BudgetData>();
	public double mBudget;
	public int[] BUDGET_BAR = {
		R.drawable.widget_progress_bg_left,
		R.drawable.widget_progress_bg_middle,
		R.drawable.widget_progress_bg_right,
		R.drawable.widget_progress_red_left,
		R.drawable.widget_progress_red_middle,
		R.drawable.widget_progress_red_right,
	};
	public int[] BUDGET_ICONS = {
		R.drawable.icon_qtzx,
		R.drawable.icon_jrbx,
		R.drawable.icon_ylbj,
		R.drawable.icon_rqwl,
		R.drawable.icon_xxjx,
		R.drawable.icon_xxyl,
		R.drawable.icon_jltx,
		R.drawable.icon_xcjt,
		R.drawable.icon_jjwy,
		R.drawable.icon_spjs,
		R.drawable.icon_yfsp
	};
	
	public void LoadBudget() {
		ArrayList<Integer> ids = new ArrayList<Integer>();
		ArrayList<String> names = new ArrayList<String>();
		ArrayList<Double> budgets = new ArrayList<Double>();
		
		mBudgetData.clear();

		db.LoadIdBudget( ids,names,budgets,0 );
		for( int i=0; i<ids.size(); i++ ) {
			mBudgetData.put( ids.get(i), new BudgetData(ids.get(i), names.get(i), BUDGET_ICONS[i], budgets.get(i)) );
		}
		
		CalcBudget();
	}
	
	private void CalcBudget()
	{
		Cursor cursor = db.rawQuery("select sum(BUDGET) from TBL_EXPENDITURE_CATEGORY",null);
		if (cursor.moveToNext()) {
			mBudget = cursor.getDouble(0);
		}
		cursor.close();
	}
	
	public void Update( BudgetData data ) {
		mBudgetData.put( data.id, data );
		db.update( 0, new String[]{"BUDGET"}, new String[]{String.valueOf(data.balance)}, "ID="+data.id, null );
		CalcBudget();
	}
	
	public void LoadShop() {
		shop.clear();
		shop.put(0, "其他");
	}
	
	public void loadPurpose(){
		purpose.clear();
		purpose.put(0, "其他");
	}
}
