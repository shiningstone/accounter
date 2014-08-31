
package com.shiningstone.accounter;

import java.util.ArrayList;

import com.shiningstone.accounter.CommonData.SubCategory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
		
		if ( item.getClass() == TransactionData.class ) {
			convertView = inflater.inflate(R.layout.common_expense_lv_item, null);
			TransactionData transaction = (TransactionData)item;
			
			CommonData	commondata = CommonData.getInstance();
			int icon = 0;
			String name = "temp";
			if(transaction.type == 0){
				icon = commondata.mCat.get("in" + transaction.category).icon;
				name = commondata.mSubCat.get("in" + (transaction.subcategory+1)).name;
			}else{
				icon = commondata.mCat.get("out" + transaction.category).icon;
				name = commondata.mSubCat.get("out" + (transaction.subcategory+1)).name;
			}
			
			((ImageView)convertView.findViewById(R.id.category_icon_iv)).setBackgroundResource(icon);
			((TextView)convertView.findViewById(R.id.category_name_tv)).setText(name);
			
			String cost = String.format("￥%.2f", transaction.amount);
			TextView cost_tv = (TextView)convertView.findViewById(R.id.cost_tv);
			if (transaction.type == 0)
				cost_tv.setTextColor(mEmpolyer.getResources().getColor(R.color.transaction_income_amount));
			else
				cost_tv.setTextColor(mEmpolyer.getResources().getColor(R.color.transaction_payout_amount));
				
			cost_tv.setText(cost);
			
			convertView.setTag(transaction);
		} else {
			convertView = inflater.inflate(R.layout.widget_separated_listview_title, null);
			TextView title = (TextView)convertView.findViewById(R.id.list_header_title);
			title.setText(item.toString());
			convertView.setTag(null);
		}

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