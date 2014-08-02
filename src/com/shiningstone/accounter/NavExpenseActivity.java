
package com.shiningstone.accounter;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class NavExpenseActivity extends Activity implements OnClickListener, OnItemClickListener, OnItemLongClickListener{
	Date mStartDate = null;
	Date mEndDate = null;
	int  mMode = MODE_NONE;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nav_expense_activity);
		
		GetParamsFromParent();
		LoadResources();
		AddActions();
		
		TitleText.setText(INTERVALS[mMode]);
		setTimeIntervalText();
		
		ExpensesList.setEmptyView(TipsLayout);
	}
	
	@Override
	public void onClick(View v) {
		if( v==PrevBtn ) {
			DateDecrease(mMode);
		} else if ( v==NextBtn ) {
			DateIncrease(mMode);
		} else {
		}
		
		setTimeIntervalText();
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		return true;
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	}
	
	private void DateIncrease(int step) {
		switch(step) {
			case MODE_DAY:
				mStartDate.setDate(mStartDate.getDate() + 1);
				mEndDate = mStartDate;
				break;
			case MODE_WEEK:
				mStartDate.setDate(mStartDate.getDate() + 7);
				mEndDate.setDate(mEndDate.getDate() + 7);
				break;
			case MODE_MONTH:
				mStartDate = new Date(mStartDate.getYear(),mStartDate.getMonth()+1,1);
				mEndDate = new Date(mEndDate.getYear(),mEndDate.getMonth()+2,0);
				break;
		}
	}
	
	private void DateDecrease(int step) {
		switch(step) {
			case MODE_DAY:
				mStartDate.setDate(mStartDate.getDate() - 1);
				mEndDate = mStartDate;
				break;
			case MODE_WEEK:
				mStartDate.setDate(mStartDate.getDate() - 7);
				mEndDate.setDate(mEndDate.getDate() - 7);
				break;
			case MODE_MONTH:
				mStartDate = new Date(mStartDate.getYear(),mStartDate.getMonth()-1,1);
				mEndDate = new Date(mEndDate.getYear(),mEndDate.getMonth(),0);
				break;
		}
	}
	
	private void setTimeIntervalText() {
		if ( mStartDate.getDate() == mEndDate.getDate() ) {
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
			IntervalText.setText(sdf.format(mStartDate));
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
			IntervalText.setText(sdf.format(mStartDate) + "-" + sdf.format(mEndDate));
		}
	}
	
	private void LoadResources() {
		TitleText    = (TextView)findViewById(R.id.title_tv);
		IntervalText = (TextView)findViewById(R.id.time_interval_tv);
		ExpensesList = (ListView)findViewById(R.id.expense_lv);
		TipsLayout   = ((LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.common_lv_empty_tips, null);
		PrevBtn      = (Button)findViewById(R.id.pre_btn);
		NextBtn      = (Button)findViewById(R.id.next_btn);

		INTERVALS = new String[MODE_MAX];
		INTERVALS[MODE_MONTH] = getString(R.string.text_title_month);
		INTERVALS[MODE_WEEK]  = getString(R.string.text_title_week);
		INTERVALS[MODE_DAY]   = getString(R.string.text_title_today);
	}
	
	private void AddActions() {
		PrevBtn.setOnClickListener(this);
		NextBtn.setOnClickListener(this);
		ExpensesList.setOnItemClickListener(this);
		ExpensesList.setOnItemLongClickListener(this);
	}
	
	private void GetParamsFromParent() {
		Intent intent = getIntent();
		
		mStartDate = new Date(intent.getLongExtra(START_DATE, 0));
		mEndDate   = new Date(intent.getLongExtra(END_DATE, 0));
		mMode      = intent.getIntExtra(MODE, MODE_NONE);
	}
	
	public static String START_DATE 	= "startTime";
	public static String END_DATE   	= "endTime";
	public static String MODE   		= "mode";
	
	public static final int MODE_NONE 	= 0;
	public static final int MODE_MONTH 	= MODE_NONE+1;
	public static final int MODE_WEEK 	= MODE_MONTH+1;
	public static final int MODE_DAY	= MODE_WEEK+1;
	public static final int MODE_MAX	= MODE_DAY+1;

	private final static String DATE_FORMAT = "yyyy-MM-dd";
	private String[] INTERVALS = null;
	
	private TextView TitleText = null;
	private TextView IntervalText = null;
	private ListView ExpensesList = null;
	private View     TipsLayout = null;
	private Button   PrevBtn = null;
	private Button   NextBtn = null;
}
