
package com.shiningstone.accounter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.shiningstone.accounter.db.MyDbHelper;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		DisplayUpdate();
		StartListen();
	}
	
	@Override
	public void onClick(View v) {
		switch( v.getId() ) {
			case R.id.add_expense_quickly_btn:
				StartTransactionTabActivity();
				break;
			case R.id.today_row_rl:
				StartTransactionNavActivity(mDate.CurDate(), mDate.CurDate(), TransactionNavActivity.MODE_DAY);
				break;
			case R.id.week_row_rl:
				StartTransactionNavActivity(mDate.WeekStart(), mDate.WeekEnd(), TransactionNavActivity.MODE_WEEK);
				break;
			case R.id.month_row_rl:
				StartTransactionNavActivity(mDate.MonthStart(), mDate.MonthEnd(), TransactionNavActivity.MODE_MONTH);
				break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode == RESULT_OK){
			DisplayUpdate();
		}
	}
	/*********************************************************
				Resources control
	*********************************************************/
	private void DisplayUpdate() {
		SummaryShow();
		DaySummaryShow();
		WeekSummaryShow();
		MonthSummaryShow();
	}
	
	private void SummaryShow() {
		MyDbHelper db = MyDbHelper.getInstance( this.getApplicationContext() );
	
		double expense  = db.GetSum("EXPENSE","AMOUNT",null,null);
		double income   = db.GetSum("INCOME","AMOUNT",null,null);
		double banlance = db.GetSum("EXPENSE_CATEGORY","BUDGET",null,null);
		
		TextShow( R.id.month_tv, mDate.mMonth );
		TextShow( R.id.income_amount_tv, "¥ " + income );
		TextShow( R.id.expense_amount_tv, "¥ " + expense );
		TextShow( R.id.budget_balance_amount_tv, "¥ " + banlance );
	}

	private void DaySummaryShow() {
		MyDbHelper db = MyDbHelper.getInstance( this.getApplicationContext() );
		String CurDay = Transform(mDate.CurDate(),SHOW_FULL);
		
		double expense  = db.GetSum("EXPENSE","AMOUNT",CurDay,null);
		double income   = db.GetSum("INCOME", "AMOUNT",CurDay,null);
		
		TextShow( R.id.today_datestr_tv, mDate.mYear + "/" + mDate.mMonth + "/" + mDate.mDay );
		TextShow( R.id.today_expense_amount_tv, "- ¥ " + expense );
		TextShow( R.id.today_income_amount_tv, "¥ " + income );
	}
	
	private void WeekSummaryShow() {
		Date Monday = mDate.WeekStart();
		Date Sunday = mDate.WeekEnd();
		
		MyDbHelper db = MyDbHelper.getInstance( this.getApplicationContext() );
		String sMonday = Transform(Monday,SHOW_FULL);
		String sSunday = Transform(Sunday,SHOW_FULL);

		double expense  = db.GetSum("EXPENSE","AMOUNT",sMonday,sSunday);
		double income   = db.GetSum("INCOME", "AMOUNT",sMonday,sSunday);

		TextShow( R.id.week_datestr_tv, Transform(Monday,SHOW_MONTH_DAY) + "-" + Transform(Sunday,SHOW_MONTH_DAY) );
		TextShow( R.id.week_expense_amount_tv, "- ¥ " + expense );
		TextShow( R.id.week_income_amount_tv, "¥ " + income );
	}
	
	private void MonthSummaryShow() {
		Date FirstDay = mDate.MonthStart();
		Date LastDay = mDate.MonthEnd();
		
		MyDbHelper db = MyDbHelper.getInstance( this.getApplicationContext() );
		String sFirstDay = Transform(FirstDay,SHOW_FULL);
		String sLastDay  = Transform(LastDay,SHOW_FULL);

		double expense  = db.GetSum("EXPENSE","AMOUNT",sFirstDay,sLastDay);
		double income   = db.GetSum("INCOME", "AMOUNT",sFirstDay,sLastDay);

		TextShow( R.id.month_datestr_tv, Transform(FirstDay,SHOW_MONTH_DAY) + "-" + Transform(LastDay,SHOW_MONTH_DAY) );
		TextShow( R.id.month_expense_amount_tv, "- ¥ " + expense );
		TextShow( R.id.month_income_amount_tv, "¥ " + income );
	}

	private void TextShow(int id,String value) {
		((TextView)findViewById(id)).setText(value);
	}
	
	private void StartListen() {
		findViewById(R.id.add_expense_quickly_btn).setOnClickListener(this);
		findViewById(R.id.today_row_rl).setOnClickListener(this);
		findViewById(R.id.week_row_rl).setOnClickListener(this);
		findViewById(R.id.month_row_rl).setOnClickListener(this);
	}
	
	private String Transform(Date date,String format){
		SimpleDateFormat ymd = new SimpleDateFormat(format);
		return ymd.format(date); 
	}

	/*********************************************************
				Interface to other activities
	*********************************************************/
	private void StartTransactionTabActivity()
	{
		Intent in = new Intent(MainActivity.this, TransactionTabActivity.class);
		startActivity(in);
		finish();
	}
	
	private void StartTransactionNavActivity(Date start, Date end, int mode)
	{
		Intent intent = new Intent(this, TransactionNavActivity.class);
		
		intent.putExtra(TransactionNavActivity.START_DATE, start.getTime());
		intent.putExtra(TransactionNavActivity.END_DATE,   end.getTime());
		intent.putExtra(TransactionNavActivity.MODE,       mode);
		
		startActivity(intent);
	}

	/*********************************************************
				private member
	*********************************************************/
	private final String SHOW_FULL      = "yyyy-MM-dd";
	private final String SHOW_MONTH_DAY = "MM/dd";
	private final String SHOW_DAY       = "dd";
	private SmartDate mDate = new SmartDate();
	
	/*********************************************************
				SmartDate
	*********************************************************/
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