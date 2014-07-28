
package com.shiningstone.accounter;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class NavExpenseActivity extends Activity {
	public static String START_DATE 	= "startTime";
	public static String END_DATE   	= "endTime";
	public static String MODE   		= "mode";
	public static final int MODE_NONE 	= 0;
	public static final int MODE_MONTH 	= 1;
	public static final int MODE_WEEK 	= 2;
	public static final int MODE_DAY	= 3;

	Date mStartDate = null;
	Date mEndDate = null;
	int  mMode = MODE_NONE;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nav_expense_activity);
		
		GetParamsFromParent();
	}
	
	private void GetParamsFromParent() {
		Intent intent = getIntent();
		
		mStartDate = new Date(intent.getLongExtra(START_DATE, 0));
		mEndDate   = new Date(intent.getLongExtra(END_DATE, 0));
		mMode      = intent.getIntExtra(MODE, MODE_NONE);
	}
}
