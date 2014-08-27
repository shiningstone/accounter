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
	ArrayList<Object> mAccounts = new ArrayList<Object>();
	
	@Override
	protected Void doInBackground(AccountActivity... params) {
		employer = params[0];
		data = CommonData.getInstance();

		List<AccountData> accountList = new ArrayList<AccountData>();
		GetSortedAccounts( data,accountList );
		LoadListItems( accountList );
		
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		((TextView)employer.findViewById(R.id.asset_amount_tv)).setText(String.format("%.2f", data.mAsset));
		((TextView)employer.findViewById(R.id.liabilitiy_amount_tv)).setText(String.format("%.2f", data.mLiability));
		
		employer.account_lv.setAdapter(new AccountListAdapter(employer, (ArrayList<Object>)mAccounts.clone()));
		employer.account_lv.setSelection(0);
		super.onPostExecute(result);
	}

	private void GetSortedAccounts( CommonData data, List<AccountData> accountList ) {
		Iterator<AccountData> iterator = data.mAccount.values().iterator();
		while (iterator.hasNext()){
			accountList.add( iterator.next() );
		}
		
		Collections.sort( accountList, new Comparator<AccountData>(){  
											@Override
											public int compare(AccountData object1, AccountData object2) {
												return String.valueOf(object1.Type).compareTo(String.valueOf(object2.Type));   
											}  
										}
		);  
	}
	
	private void LoadListItems( List<AccountData> accountList ) {
		int cur_type = -1;

		Iterator<AccountData> iterator = accountList.iterator();
		while (iterator.hasNext()){
			AccountData account = iterator.next();
			
			int type = account.Type;
			if ( cur_type == -1 || type != cur_type ) {
				mAccounts.add( data.mAccountType.get(type).name );
				cur_type = type;
			}
			
			mAccounts.add(account);
		}
	}
}
