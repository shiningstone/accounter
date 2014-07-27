
package com.shiningstone.accounter;

import android.app.Activity;
import android.os.Bundle;

public class NavExpenseActivity extends Activity {
	public static String START_DATE 	= "startTime";
	public static String END_DATE   	= "endTime";
	public static String TITLE   		= "title";
	public static String MODE   		= "mode";
	public static final int MODE_NONE 	= 0;
	public static final int MODE_MONTH 	= 1;
	public static final int MODE_WEEK 	= 2;
	public static final int MODE_DAY	= 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nav_expense_activity);
	}
}
