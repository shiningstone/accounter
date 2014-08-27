package com.shiningstone.accounter;

import java.util.ArrayList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BudgetListAdapter extends BaseAdapter {
	BudgetActivity 	        mEmployer;
	ArrayList<Object> 		mBudgetList;
	LayoutInflater 			mInflater;
	CommonData              commondata;
	
	public BudgetListAdapter(BudgetActivity activity, ArrayList<Object> budget)
	{
		mEmployer = activity;
		mBudgetList = budget;
		mInflater = LayoutInflater.from(mEmployer);
		commondata = CommonData.getInstance();
	}

	@Override
	public int getCount() {
		return mBudgetList.size();
	}

	@Override
	public Object getItem(int position) {
		return mBudgetList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		BudgetData budget = (BudgetData)mBudgetList.get(position);
		convertView = mInflater.inflate(R.layout.budget_list_item, null);

		((ImageView)convertView.findViewById(R.id.category_icon_iv)).setBackgroundResource(budget.Category);
		((TextView)convertView.findViewById(R.id.category_name_tv)).setText(budget.Name);

		String cost = String.format("$%.2f", budget.Balance);
		((TextView)convertView.findViewById(R.id.budget_amount_tv)).setText(cost);
		((TextView)convertView.findViewById(R.id.balance_amount_tv)).setText(mEmployer.getString(R.string.balance_amount_title) + cost);
		
		if(budget.Balance != 0){
			((ImageView)convertView.findViewById(R.id.line_bar_left)).setBackgroundResource(BUDGET_BAR[3]);
			((ImageView)convertView.findViewById(R.id.line_bar_middle)).setBackgroundResource(BUDGET_BAR[4]);
			((ImageView)convertView.findViewById(R.id.line_bar_right)).setBackgroundResource(BUDGET_BAR[5]);
		}else{
			((ImageView)convertView.findViewById(R.id.line_bar_left)).setBackgroundResource(BUDGET_BAR[0]);
			((ImageView)convertView.findViewById(R.id.line_bar_middle)).setBackgroundResource(BUDGET_BAR[1]);
			((ImageView)convertView.findViewById(R.id.line_bar_right)).setBackgroundResource(BUDGET_BAR[2]);
		}
		
		convertView.setTag(budget);

		return convertView;
	}

	private final int[] BUDGET_BAR = {
		R.drawable.widget_progress_bg_left,
		R.drawable.widget_progress_bg_middle,
		R.drawable.widget_progress_bg_right,
		R.drawable.widget_progress_red_left,
		R.drawable.widget_progress_red_middle,
		R.drawable.widget_progress_red_right,
	};

}
