package com.shiningstone.accounter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import android.os.AsyncTask;
import android.widget.TextView;

public class AccountListAsyncTask extends AsyncTask<AccountActivity, Void, Void> {

	AccountActivity employer;
	CommonData data;
	ArrayList<Object> accounts = new ArrayList<Object>();
	List<AccountData> mAccounts = new ArrayList<AccountData>();
	
	@Override
	protected Void doInBackground(AccountActivity... params) {
		employer = params[0];
		data = CommonData.getInstance();

		
		GetSortedAccounts( data );
		
		Iterator<AccountData> iterator = mAccounts.iterator();
		int prev_category = -1;
		while (iterator.hasNext()){
			AccountData account = iterator.next();
			
			int category = data.mAccountSubType.get( account.Category ).parent;
			if (prev_category == -1 || category != prev_category) {
				accounts.add( data.mAccountType.get(category).name );
				prev_category = category;
			}
			
			accounts.add(account);
		}
		
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		((TextView)employer.findViewById(R.id.asset_amount_tv)).setText(String.format("￥.2f", data.mAsset));
		((TextView)employer.findViewById(R.id.liabilitiy_amount_tv)).setText(String.format("￥.2f", data.mLiability));
		
		employer.account_lv.setAdapter(new AccountListAdapter(employer, (ArrayList<Object>)accounts.clone()));
		employer.account_lv.setSelection(0);
		super.onPostExecute(result);
	}

	private void GetSortedAccounts(CommonData data) {
		Iterator<AccountData> iterator = data.mAccount.values().iterator();
		while (iterator.hasNext()){
			mAccounts.add( iterator.next() );
		}
		
		Collections.sort( mAccounts, new Comparator<AccountData>(){  
										@Override
										public int compare(AccountData object1, AccountData object2) {
											return String.valueOf(object1.TypeId).compareTo(String.valueOf(object2.TypeId));   
										}  
        });  
	}
}
