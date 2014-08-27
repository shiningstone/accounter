package com.shiningstone.accounter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class BudgetListAsyncTask extends AsyncTask<BudgetActivity, Void, Void> {
	CommonData        commondata;
	BudgetActivity    mEmployer;
	ArrayList<Object> mBudgets = new ArrayList<Object>();

	@Override
	protected Void doInBackground(BudgetActivity... params) {
		mEmployer = params[0];
		commondata = CommonData.getInstance();
		
		Iterator<BudgetData> iterator = commondata.mBudgetData.values().iterator();
		while (iterator.hasNext()){
			BudgetData data = iterator.next();
			mBudgets.add(data);
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		mEmployer.findViewById(R.id.listview_loading_tv).setVisibility(View.GONE);
		
		if (mBudgets.size() == 0){
			mEmployer.findViewById(R.id.lv_empty_iv).setVisibility(View.VISIBLE);
			mEmployer.findViewById(R.id.header_empty_iv).setVisibility(View.GONE);
		}
		else{
			mEmployer.findViewById(R.id.lv_empty_iv).setVisibility(View.GONE);
			mEmployer.findViewById(R.id.header_empty_iv).setVisibility(View.VISIBLE);
		}
		
		((TextView)mEmployer.findViewById(R.id.budget_amount_tv)).setText(String.format("¥ %.2f", commondata.mBudget));
		mEmployer.budget_lv.setAdapter(new BudgetListAdapter(mEmployer,(ArrayList<Object>)mBudgets.clone()));
		mEmployer.budget_lv.setSelection(0);
		
		super.onPostExecute(result);
	}

}
