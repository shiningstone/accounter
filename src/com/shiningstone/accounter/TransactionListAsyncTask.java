
package com.shiningstone.accounter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import com.shiningstone.accounter.db.MyDbHelper;

import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class TransactionListAsyncTask extends AsyncTask<TransactionNavActivity, Void, Void> {
	private final String Tag = "TransactionListAsyncTask";

	TransactionNavActivity mEmployer = null;
	ArrayList<Object> mTransactions = new ArrayList<Object>();
	
	double mIncome = 0;
	double mExpense = 0;

	final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	private MyDbHelper mDb = null;
    private List<TransactionData> mList = new ArrayList<TransactionData>(); 
    private TransactionData mData = null;

	private void GetAmount(Date start,Date end) {
		Cursor cursor = mDb.rawQuery("select sum(AMOUNT) from INCOME where strftime('%Y-%m-%d',DATE)>=? and strftime('%Y-%m-%d',DATE)<=?"
				,new String[]{format(start),format(end)});
		if (cursor.moveToNext()) {
			if(cursor.getString(0) != null)
				mIncome = Double.parseDouble(cursor.getString(0));
		}

		cursor = mDb.rawQuery("select sum(AMOUNT) from EXPENSE where strftime('%Y-%m-%d',DATE)>=? and strftime('%Y-%m-%d',DATE)<=?"
				,new String[]{format(start),format(end)});
		if (cursor.moveToNext()) {
			if(cursor.getString(0) != null)
				mExpense = Double.parseDouble(cursor.getString(0));
		}
		
		cursor.close();
	}
	
	private void GetTransactions(Date start,Date end) {
		Cursor cursor = mDb.rawQuery("select * from EXPENSE where strftime('%Y-%m-%d',DATE)>=? and strftime('%Y-%m-%d',DATE)<=? order by ID desc"
				,new String[]{format(start),format(end)});
		while (cursor.moveToNext()) {
			mData = new TransactionData(1, 
					cursor.getInt(0), 
					cursor.getDouble(1), 
					cursor.getInt(2), 
					cursor.getInt(3), 
					cursor.getInt(4), 
					cursor.getInt(5), 
					cursor.getInt(6), 
					cursor.getString(7), 
					cursor.getString(8));
			mList.add(mData);
		}
		
		cursor = mDb.rawQuery("select * from INCOME where strftime('%Y-%m-%d',DATE)>=? and strftime('%Y-%m-%d',DATE)<=? order by ID desc"
				,new String[]{format(start),format(end)});
		while (cursor.moveToNext()) {
			mData = new TransactionData(0, 
					cursor.getInt(0), 
					cursor.getDouble(1), 
					cursor.getInt(2), 
					cursor.getInt(3), 
					cursor.getInt(4), 
					0, 
					cursor.getInt(5), 
					cursor.getString(6), 
					cursor.getString(7));
			mList.add(mData);
		}

		cursor.close();
	}
	
	@Override
	protected Void doInBackground(TransactionNavActivity... params) {
		mEmployer = params[0];
		mDb = MyDbHelper.getInstance(mEmployer);
		
		Date start = mEmployer.mStartDate;
		Date end   = mEmployer.mEndDate;

		GetAmount(start,end);
		GetTransactions(start,end);
		
		Collections.sort(mList, new Comparator<TransactionData>(){  
            public int compare(TransactionData o1, TransactionData o2)  
            {  
                int ret = 0;  
                try  
                {  
                	ret = DATE_FORMAT.parse(o2.date).compareTo(DATE_FORMAT.parse(o1.date));   
                } catch (Exception e)  
                {                     
                    throw new RuntimeException(e);  
                }  
                return  ret;  
            }  
              
        });  

		ListIterator<TransactionData> iterator = mList.listIterator();
		String date = null;
		while(iterator.hasNext()){
			TransactionData data = iterator.next();
			if(date == null || !date.equals(data.date)){
				date = data.date;
				mTransactions.add(data.date);
			}
			mTransactions.add( data );
		}
		
		return null;
	}
	
	@Override
	protected void onPostExecute(Void result) {
		mEmployer.findViewById(R.id.listview_loading_tv).setVisibility(View.GONE);
		if (mTransactions.size() == 0) {
			mEmployer.findViewById(R.id.lv_empty_iv).setVisibility(View.VISIBLE);
		}
		else {
			mEmployer.findViewById(R.id.lv_empty_iv).setVisibility(View.GONE);
		}
		
		((TextView)mEmployer.findViewById(R.id.income_amount_tv)).setText(String.format("¥ %.2f", mIncome));
		((TextView)mEmployer.findViewById(R.id.payout_amount_tv)).setText(String.format("- ¥ %.2f", mExpense));
		
		mEmployer.ExpensesList.setAdapter(new TransactionListAdapter(mEmployer, (ArrayList<Object>)mTransactions.clone()));
		super.onPostExecute(result);
	}
	
	private String format(Date date){
		String str = "";
		SimpleDateFormat ymd = null;
		ymd = new SimpleDateFormat("yyyy-MM-dd");
		str = ymd.format(date); 
		return str;
	}

}
