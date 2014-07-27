
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
	
	private void ListDayShow() {
		TextView today_datestr_tv = (TextView) findViewById(R.id.today_datestr_tv);
		today_datestr_tv.setText(mDate.mYear + "/" + mDate.mMonth + "/" + mDate.mDay);

		TextView today_expense_amount_tv = (TextView) findViewById(R.id.today_expense_amount_tv);
		today_expense_amount_tv.setText("- ¥ " + 4);

		TextView today_income_amount_tv = (TextView) findViewById(R.id.today_income_amount_tv);
		today_income_amount_tv.setText("¥ " + 5);
	}
	
	private void ListWeekShow() {
		TextView week_datestr_tv = (TextView) findViewById(R.id.week_datestr_tv);
		week_datestr_tv.setText(mDate.WeekStart() + "-" + mDate.WeekEnd());

		TextView week_expense_amount_tv = (TextView) findViewById(R.id.week_expense_amount_tv);
		week_expense_amount_tv.setText("- ¥ " + 6);

		TextView week_income_amount_tv = (TextView) findViewById(R.id.week_income_amount_tv);
		week_income_amount_tv.setText("¥ " + 7);
	}
	
	private void ListMonthShow() {
		TextView month_datestr_tv = (TextView) findViewById(R.id.month_datestr_tv);
		month_datestr_tv.setText(mDate.mMonth + "/01" + "-" + mDate.mMonth + "/" + mDate.GetDaySumOfCurMonth() );

		TextView month_expense_amount_tv = (TextView) findViewById(R.id.month_expense_amount_tv);
		month_expense_amount_tv.setText("- ¥ " + 8);

		TextView month_income_amount_tv = (TextView) findViewById(R.id.month_income_amount_tv);
		month_income_amount_tv.setText("¥ " + 9);
	}

	private void SummaryShow() {
		MonthShow();
		IncomeShow(1);
		ExpenseShow(2);
		BudgetShow(3);
	}
	
	private void MonthShow() {
		String year  = mDate.mYear;
		String month = mDate.mMonth;
		String day   = mDate.mDay;
		
		TextView month_tv = (TextView) findViewById(R.id.month_tv);
		month_tv.setText(month);
	}
	
	private void IncomeShow(double value){
		TextView income_amount_tv = (TextView) findViewById(R.id.income_amount_tv);
		income_amount_tv.setText("¥ " + value);
	}
	
	private void ExpenseShow(double value) {
		TextView expense_amount_tv = (TextView) findViewById(R.id.expense_amount_tv);
		expense_amount_tv.setText("¥ " + value);
	}
	
	private void BudgetShow(double value) {
		TextView budget_balance_amount_tv = (TextView) findViewById(R.id.budget_balance_amount_tv);
		budget_balance_amount_tv.setText("¥ " + value);
	}
	
	public class SmartDate {
		public String mYear;
		public String mMonth;
		public String mDay;
		private Calendar calendar = Calendar.getInstance();

		private final int DATE_MOTH_DAY = 1;
		private final int DATE_DAY = 2;
		
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
			int mondayPlus = this.getMondayPlus(); 
			GregorianCalendar currentDate = new GregorianCalendar(); 
			currentDate.add(GregorianCalendar.DATE, mondayPlus); 
			Date monday = currentDate.getTime(); 
		
			String preMonday = format(monday,0); 
			return format(monday,DATE_MOTH_DAY);
		}
		
		public String WeekEnd() {
			int mondayPlus = this.getMondayPlus(); 
			GregorianCalendar currentDate = new GregorianCalendar(); 
			currentDate.add(GregorianCalendar.DATE, mondayPlus+6); 
			Date monday = currentDate.getTime(); 
		
			String preMonday = format(monday,0); 
			return format(monday,DATE_MOTH_DAY);
		}

		public String GetDaySumOfCurMonth() {
			return "31";
		}
		
		private int getMondayPlus() {
			int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)-1;
			if (dayOfWeek == 1) { 
				return 0; 
			} else { 
				return 1 - dayOfWeek; 
			} 
		} 

		private String format(Date date,int id){
			SimpleDateFormat ymd = null;
			
			switch (id) {
				case DATE_MOTH_DAY:
					ymd = new SimpleDateFormat("MM/dd");
					break;
				case DATE_DAY:
					ymd = new SimpleDateFormat("dd");
					break;
				default:
					ymd = new SimpleDateFormat("yyyy-MM-dd");
					break;
			}
			
			return ymd.format(date); 
		}
	}
}