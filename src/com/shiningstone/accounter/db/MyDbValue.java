
package com.shiningstone.accounter.db;

import com.shiningstone.accounter.R;

import android.content.Context;
import android.content.res.Resources;

public class MyDbValue {
	public static final int EXPENSE = 0;
	public static final int INCOME  = 1;
	
	public static String[][]   Categories = new String[2][];
	public static String[][][] SubCategories = new String[2][11][];
	public static String[]     Accounts = null;
	public static String[]     Items = null;
	public static String[]     Stores = null;
	
	/**********************************************************
				Singleton
	**********************************************************/
	private static MyDbValue instance = null;

	public static MyDbValue getInstance(Context context) {
		if (instance==null) {
			instance = new MyDbValue(context);
		}
		return instance;
	}
	
	private MyDbValue(Context context) {
		Resources res = context.getResources();
		
		String[] s = res.getStringArray(R.array.TBL_EXPENDITURE_CATEGORY);
		
		Categories[EXPENSE] = res.getStringArray(R.array.TBL_EXPENDITURE_CATEGORY);
		SubCategories[EXPENSE][0] = res.getStringArray(R.array.TBL_EXPENDITURE_SUB_CATEGORY_1);
		SubCategories[EXPENSE][1] = res.getStringArray(R.array.TBL_EXPENDITURE_SUB_CATEGORY_2);
		SubCategories[EXPENSE][2] = res.getStringArray(R.array.TBL_EXPENDITURE_SUB_CATEGORY_3);
		SubCategories[EXPENSE][3] = res.getStringArray(R.array.TBL_EXPENDITURE_SUB_CATEGORY_4);
		SubCategories[EXPENSE][4] = res.getStringArray(R.array.TBL_EXPENDITURE_SUB_CATEGORY_5);
		SubCategories[EXPENSE][5] = res.getStringArray(R.array.TBL_EXPENDITURE_SUB_CATEGORY_6);
		SubCategories[EXPENSE][6] = res.getStringArray(R.array.TBL_EXPENDITURE_SUB_CATEGORY_7);
		SubCategories[EXPENSE][7] = res.getStringArray(R.array.TBL_EXPENDITURE_SUB_CATEGORY_8);
		SubCategories[EXPENSE][8] = res.getStringArray(R.array.TBL_EXPENDITURE_SUB_CATEGORY_9);
		SubCategories[EXPENSE][9] = res.getStringArray(R.array.TBL_EXPENDITURE_SUB_CATEGORY_10);
		SubCategories[EXPENSE][10] = res.getStringArray(R.array.TBL_EXPENDITURE_SUB_CATEGORY_11);

		Categories[INCOME] = res.getStringArray(R.array.TBL_INCOME_CATEGORY);
		SubCategories[INCOME][0] = res.getStringArray(R.array.TBL_INCOME_SUB_CATEGORY_1);
		SubCategories[INCOME][1] = res.getStringArray(R.array.TBL_INCOME_SUB_CATEGORY_2);
		
		Accounts = res.getStringArray(R.array.TBL_ACCOUNT);
		Items = res.getStringArray(R.array.TBL_ITEM);
		Stores = res.getStringArray(R.array.TBL_STORE);
	}
	
};