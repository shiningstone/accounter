
package com.shiningstone.accounter;

import java.util.ArrayList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class TransactionListAdapter extends BaseAdapter {
	TransactionNavActivity 	mEmpolyer;
	ArrayList<Object> 	    mTransactions;
	LayoutInflater 		    inflater;
	
	public TransactionListAdapter(TransactionNavActivity activity, ArrayList<Object> list)
	{
		this.mEmpolyer = activity;
		this.mTransactions = list;
		this.inflater = LayoutInflater.from(activity);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Object item = mTransactions.get(position);
		
		convertView = inflater.inflate(R.layout.common_expense_lv_item, null);

		return convertView;
	}

	@Override
	public int getCount() {
		return mTransactions.size();
	}

	@Override
	public Object getItem(int position) {
		return mTransactions.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
}