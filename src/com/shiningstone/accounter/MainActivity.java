
package com.shiningstone.accounter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {
	private final String SHOW_MONTH_DAY = "MM/dd";
	private final String SHOW_DAY       = "dd";
	
	private SmartDate mDate = new SmartDate();

	private Button add_expense_quickly_btn = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);

		SummaryShow();
		
		ListDayShow();
		ListWeekShow();
		ListMonthShow();

		findViewById(R.id.add_expense_quickly_btn).setOnClickListener(this);
		findViewById(R.id.today_row_rl).setOnClickListener(this);
		findViewById(R.id.week_row_rl).setOnClickListener(this);
		findViewById(R.id.month_row_rl).setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch( v.getId() ) {
			case R.id.add_expense_quickly_btn:
				StartTransactionTabActivity();
				break;
			case R.id.today_row_rl:
				StartNavExpenseActivity(mDate.CurDate(), mDate.CurDate(), NavExpenseActivity.MODE_DAY);
				break;
			case R.id.week_row_rl:
				StartNavExpenseActivity(mDate.WeekStart(), mDate.WeekEnd(), NavExpenseActivity.MODE_WEEK);
				break;
			case R.id.month_row_rl:
				StartNavExpenseActivity(mDate.MonthStart(), mDate.MonthEnd(), NavExpenseActivity.MODE_MONTH);
				break;
		}
	}
	
	private void StartTransactionTabActivity()
	{
		Intent in = new Intent(MainActivity.this, TransactionTabActivity.class);
		startActivity(in);
		finish();
	}
	
	private void StartNavExpenseActivity(Date start, Date end, int mode)
	{
		Intent intent = new Intent(this, NavExpenseActivity.class);
		
		intent.putExtra(NavExpenseActivity.START_DATE, start.getTime());
		intent.putExtra(NavExpenseActivity.END_DATE,   end.getTime());
		intent.putExtra(NavExpenseActivity.MODE,       mode);
		
		startActivity(intent);
	}

	private void TextShow(int id,String value) {
		((TextView)findViewById(id)).setText(value);
	}
	
	private void ListDayShow() {
		TextShow( R.id.today_datestr_tv, mDate.mYear + "/" + mDate.mMonth + "/" + mDate.mDay );
		TextShow( R.id.today_expense_amount_tv, "- � " + 4 );
		TextShow( R.id.today_income_amount_tv, "� " + 5 );
	}
	
	private void ListWeekShow() {
		Date Monday = mDate.WeekStart();
		Date Sunday = mDate.WeekEnd();
		
		TextShow( R.id.week_datestr_tv, Transform(Monday,SHOW_MONTH_DAY) + "-" + Transform(Sunday,SHOW_MONTH_DAY) );
		TextShow( R.id.week_expense_amount_tv, "- � " + 6 );
		TextShow( R.id.week_income_amount_tv, "� " + 7 );
	}
	
	private void ListMonthShow() {
		Date LastDay = mDate.MonthEnd();
		
		TextShow( R.id.month_datestr_tv, mDate.mMonth + "/01" + "-" + Transform(LastDay,SHOW_MONTH_DAY) );
		TextShow( R.id.month_expense_amount_tv, "- � " + 8 );
		TextShow( R.id.month_income_amount_tv, "� " + 9 );
	}

	private void SummaryShow() {
		TextShow( R.id.month_tv, mDate.mMonth );
		TextShow( R.id.income_amount_tv, "� " + 1 );
		TextShow( R.id.expense_amount_tv, "� " + 2 );
		TextShow( R.id.budget_balance_amount_tv, "� " + 3 );
	}
	

	private String Transform(Date date,String format){
		SimpleDateFormat ymd = new SimpleDateFormat(format);
		return ymd.format(date); 
	}

	public class SmartDate {
		public String mYear;
		public String mMonth;
		public String mDay;
		private Calendar calendar = Calendar.getInstance();

		public SmartDate() {
			mYear  = String.valueOf(calendar.get(Calendar.YEAR));
			mMonth = String.valueOf(calendar.get(Calendar.MONTH)+1);
			mDay   = String.valueOf(calendar.get(Calendar.DATE));

			if( Integer.parseInt(mMonth)<10 )
				mMonth ="0" + mMonth;
			if( Integer.parseInt(mDay)<10 )
				mDay ="0" + mDay;

			calendar.setFirstDayOfWeek(Calendar.MONDAY);
		}
		
		public Date CurDate() {
			GregorianCalendar currentDate = new GregorianCalendar(); 
			return currentDate.getTime(); 
		}
		
		public Date WeekStart() {
			GregorianCalendar currentDate = new GregorianCalendar(); 
			currentDate.add( GregorianCalendar.DATE, -GetDaysSinceMonday() ); 
			return currentDate.getTime(); 
		}
		
		public Date WeekEnd() {
			GregorianCalendar currentDate = new GregorianCalendar(); 
			currentDate.add( GregorianCalendar.DATE, GetDaysToSunday() ); 
			return currentDate.getTime(); 
		}

		public Date MonthStart() {
			Calendar lastDate = Calendar.getInstance(); 
			lastDate.set(Calendar.DATE,1);
			return lastDate.getTime(); 
		}

		public Date MonthEnd() {
			Calendar lastDate = Calendar.getInstance(); 
			lastDate.add(Calendar.MONTH,1);
			lastDate.set(Calendar.DATE,1);
			lastDate.add(Calendar.DATE,-1);
			return lastDate.getTime(); 
		}
		
		public long GetCurrentTime() {
			return Calendar.getInstance().getTimeInMillis();
		}
	
		private int GetDaysSinceSunday() {
			return (calendar.get(Calendar.DAY_OF_WEEK) - 1);
		}
		
		private int GetDaysSinceMonday() {
			if( GetDaysSinceSunday()==0 ) {
				return ( 6 );
			} else {
				return ( GetDaysSinceSunday() - 1 );
			}
		} 
		
		private int GetDaysToSunday() {
			if( GetDaysSinceSunday()==0 ) {
				return ( 0 );
			} else {
				return ( 7-GetDaysSinceSunday() );
			}
		} 
	}
}