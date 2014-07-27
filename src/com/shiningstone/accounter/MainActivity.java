
package com.shiningstone.accounter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {
	private SmartDate mDate = new SmartDate();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);

		SummaryShow();
		
		ListDayShow();
		ListWeekShow();
		ListMonthShow();
	}
	
	@Override
	public void onClick(View v) {
	}
	
	private void TextShow(int id,String value) {
		((TextView)findViewById(id)).setText(value);
	}
	
	private void ListDayShow() {
		TextShow( R.id.today_datestr_tv, mDate.mYear + "/" + mDate.mMonth + "/" + mDate.mDay );
		TextShow( R.id.today_expense_amount_tv, "- ¥ " + 4 );
		TextShow( R.id.today_income_amount_tv, "¥ " + 5 );
	}
	
	private void ListWeekShow() {
		TextShow( R.id.week_datestr_tv, mDate.WeekStart() + "-" + mDate.WeekEnd() );
		TextShow( R.id.week_expense_amount_tv, "- ¥ " + 6 );
		TextShow( R.id.week_income_amount_tv, "¥ " + 7 );
	}
	
	private void ListMonthShow() {
		TextShow( R.id.month_datestr_tv, mDate.mMonth + "/01" + "-" + mDate.mMonth + "/" + mDate.GetDaySumOfCurMonth() );
		TextShow( R.id.month_expense_amount_tv, "- ¥ " + 8 );
		TextShow( R.id.month_income_amount_tv, "¥ " + 9 );
	}

	private void SummaryShow() {
		TextShow( R.id.month_tv, mDate.mMonth );
		TextShow( R.id.income_amount_tv, "¥ " + 1 );
		TextShow( R.id.expense_amount_tv, "¥ " + 2 );
		TextShow( R.id.budget_balance_amount_tv, "¥ " + 3 );
	}
	
	public class SmartDate {
		public String mYear;
		public String mMonth;
		public String mDay;
		private Calendar calendar = Calendar.getInstance();

		private final String SHOW_MONTH_DAY = "MM/dd";
		private final String SHOW_DAY       = "dd";
		
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
		
		public String WeekStart() {
			GregorianCalendar currentDate = new GregorianCalendar(); 
			currentDate.add( GregorianCalendar.DATE, -GetDaysSinceMonday() ); 

			Date monday = currentDate.getTime(); 
			return Transform(monday,SHOW_MONTH_DAY);
		}
		
		public String WeekEnd() {
			GregorianCalendar currentDate = new GregorianCalendar(); 
			currentDate.add( GregorianCalendar.DATE, GetDaysToSunday() ); 

			Date sunday = currentDate.getTime(); 
			return Transform(sunday,SHOW_MONTH_DAY);
		}

		public String GetDaySumOfCurMonth() {
			Calendar lastDate = Calendar.getInstance(); 
			
			lastDate.add(Calendar.MONTH,1);
			lastDate.set(Calendar.DATE,1);
			lastDate.add(Calendar.DATE,-1);
			
			return Transform(lastDate.getTime(),SHOW_DAY); 
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
			return ( GetDaysSinceSunday()%7 );
		} 

		private String Transform(Date date,String format){
			SimpleDateFormat ymd = new SimpleDateFormat(format);
			return ymd.format(date); 
		}
	}
}