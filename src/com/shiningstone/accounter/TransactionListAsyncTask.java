
package com.shiningstone.accounter;

import java.util.ArrayList;
import java.util.Date;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class TransactionListAsyncTask extends AsyncTask<TransactionNavActivity, Void, Void> {
	private final String Tag = "TransactionListAsyncTask";

	TransactionNavActivity mEmployer = null;
	ArrayList<Object> mTransactions = new ArrayList<Object>();
	
	double income = 0;
	double expense = 0;

	@Override
	protected Void doInBackground(TransactionNavActivity... params) {
		mEmployer = params[0];
		Date start = mEmployer.mStartDate;
		Date end   = mEmployer.mEndDate;
		
		Log.v(Tag,"doInBackground");
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
		
		((TextView)mEmployer.findViewById(R.id.income_amount_tv)).setText(String.format("+.2f", income));
		((TextView)mEmployer.findViewById(R.id.payout_amount_tv)).setText(String.format("-.2f", expense));
		
		mEmployer.ExpensesList.setAdapter(new TransactionListAdapter(mEmployer, (ArrayList<Object>)mTransactions.clone()));
		super.onPostExecute(result);
	}
}
