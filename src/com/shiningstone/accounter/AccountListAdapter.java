package com.shiningstone.accounter;

import java.util.ArrayList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AccountListAdapter extends BaseAdapter {
	AccountActivity 	    employer;
	ArrayList<Object> 		accounts;
	LayoutInflater 			inflater;
	CommonData              data;
	
	public AccountListAdapter(AccountActivity activity, ArrayList<Object> accounts)
	{
		this.employer = activity;
		this.accounts = accounts;
		this.inflater = LayoutInflater.from(employer);
		this.data     = CommonData.getInstance();
	}

	@Override
	public int getCount() {
		return accounts.size();
	}

	@Override
	public Object getItem(int position) {
		return accounts.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Object item = accounts.get(position);
		
		if (item.getClass() == AccountData.class) {
			convertView = inflater.inflate(R.layout.account_listview_data, null);
			AccountData account = (AccountData)item;
			
			String subcatname = data.mAccountSubType.get(account.Category).name;
			String cost;
			if (account.Balance >= 0)
				cost = String.format("$%.2f", account.Balance);
			else
				cost = String.format("-$%.2f", -account.Balance);
			
			((TextView)convertView.findViewById(R.id.account_name_tv)).setText(account.Name);
			((TextView)convertView.findViewById(R.id.second_level_account_group_name_tv)).setText(subcatname);
			((TextView)convertView.findViewById(R.id.account_balance_tv)).setText(cost);
			
			convertView.setTag(account);
		} else {
			convertView = inflater.inflate(R.layout.account_listview_title, null);
			TextView title = (TextView)convertView.findViewById(R.id.list_header_title);
			title.setText(item.toString());
		}

		return convertView;
	}

}
