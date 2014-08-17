package com.shiningstone.accounter;

import java.util.ArrayList;

import com.shiningstone.accounter.CommonData.AccountSubtype;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AccountEditorActivity extends Activity implements OnItemSelectedListener
{
	final static int CREATE = 0;
	final static int EDIT   = 1;
	private int mMode;
	
	final private int OK    = 0;
	final private int FAIL  = 1;
	
	private CommonData data;
	private AccountData account;
	private int init_cate = -1, init_subcate = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account_editor);
		
		data = CommonData.getInstance();
		
		GetAccount( getIntent() );
		
		LoadResources();
		StartListen();
		LoadCategories( data,CateIds,Categories );
		UpdateSpinner( TypeSpn,Categories );
		
		ShowAccount();
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		if ( parent==TypeSpn ) {
			if( mMode==CREATE ){
				if( position == 3 || position == 4 ) {
					AmountEt.setEnabled(false);
					AmountEt.setInputType(InputType.TYPE_NULL);
					AmountEt.setText("<"+getString(R.string.edit_point)+">");
				}else{
					AmountEt.setEnabled(true);
					AmountEt.setText("0");
				}
			}
			
			if (init_cate != -1) {
				TypeSpn.setSelection( CateIds.indexOf(init_cate) );
				init_cate = -1;
			} else {
				SubcateIds.clear();
				Subcategories.clear();
				for (CommonData.AccountSubtype category : data.mAccountSubType.values()) {
					if (category.parent == CateIds.get(position)) {
						SubcateIds.add(category.id);
						Subcategories.add(category.name);
					}
				}
	
				UpdateSpinner( SubTypeSpn,Subcategories );
			}
		} else if ( parent == SubTypeSpn ) {
			if (init_cate == -1 && init_subcate != -1) {
				SubTypeSpn.setSelection(SubcateIds.indexOf(init_subcate));
				init_cate = -1;
			} else {
				account.Subtype = SubcateIds.get(position);
			}
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		if (parent == TypeSpn)
			SubTypeSpn.setAdapter(null);
	}
	
	/********************************************************
				common data access
	********************************************************/
	private ArrayList<Integer> CateIds = new ArrayList<Integer>();
	private ArrayList<String> Categories =  new ArrayList<String>();
	private ArrayList<Integer> SubcateIds =  new ArrayList<Integer>();
	private ArrayList<String> Subcategories =  new ArrayList<String>();
	
	private void LoadCategories( CommonData data, ArrayList<Integer> ids, ArrayList<String> names ) {
		for( CommonData.AccountType type : data.mAccountType.values() ) {
			ids.add( type.id );
			names.add( type.name );
		}
	}	
	/********************************************************
				account data access
	********************************************************/
	private void GetAccount(Intent intent) {
		mMode = intent.getIntExtra("mode", CREATE);
		
		switch (mMode) {
			case CREATE:
				account = new AccountData();
				break;
				
			case EDIT:
				int id = intent.getIntExtra("accountid", -1);
				if (id >= 0) {
					account = (AccountData)data.mAccount.get(id).clone();
				} else {
					Toast.makeText(this, getString(R.string.error_system), 0).show();
					finish();
				}
				break;
		}
	}
	
	private void ShowAccount() {
		NameEt.setText( account.Name );
		AmountEt.setText( String.format("%.2f", account.Balance) );
		
		if (mMode == EDIT) {
			SubTypeSpn.setClickable(false);
			TypeSpn.setClickable(false);
			
			if(account.Type == 4 || account.Type == 5){
				AmountEt.setEnabled(false);
				AmountEt.setInputType(InputType.TYPE_NULL);
			}
			
			AccountSubtype subcat = data.mAccountSubType.get(account.Subtype);
			init_cate = subcat.parent;
			init_subcate = subcat.id;
		}
	}
	
	private int UpdateAccount() {
		String name = NameEt.getText().toString();
		if ( TextUtils.isEmpty(name) ) {
			Toast.makeText(getApplicationContext(), getString(R.string.error_user_empty), 1).show();
			return FAIL;
		}

		String balance = AmountEt.getText().toString();
		if ( TextUtils.isEmpty(balance) ) {
			Toast.makeText(getApplicationContext(), getString(R.string.error_balance_empty), 1).show();
			return FAIL;
		}

		double balance_d;
		try {
			balance_d = Double.valueOf(balance).doubleValue();
		} catch (NumberFormatException e) {
			Toast.makeText(getApplicationContext(), getString(R.string.error_balance_number), 1).show();
			return FAIL;
		}
		
		int account_type_id = TypeSpn.getSelectedItemPosition()+1;
		int account_subcategory = SubcateIds.get(SubTypeSpn.getSelectedItemPosition());
		
		account.Name = name;
		account.Balance = balance_d;
		account.Type = account_type_id;
		account.Subtype = account_subcategory;
		
		return OK;
	}
	
	private void SaveAccount() {
		if ( UpdateAccount() != OK ) {
			return;
		}
		
		if (mMode == CREATE) {
			if ( data.IsAccountExist(account.Name) ) {
				Toast.makeText(getApplicationContext(), getString(R.string.error_user_there), 1).show();
				return;
			}
			
			data.Add(account);
		} else if (mMode == EDIT) {
			data.Update(account);
		}

		setResult(Activity.RESULT_OK);
		Toast.makeText(getApplicationContext(), getString(R.string.save_message), 0).show();
		finish();
	}
	
	/*************************************************************
				resource control
	*************************************************************/
	private EditText NameEt;
	private EditText AmountEt;
	private Spinner  TypeSpn;
	private Spinner  SubTypeSpn;
	
	private void LoadResources() {
		NameEt   = (EditText)findViewById(R.id.name_et);
		AmountEt = (EditText)findViewById(R.id.amount_of_money_et);
		TypeSpn  = (Spinner)findViewById(R.id.first_level_accountgroup_spn);
		SubTypeSpn = (Spinner)findViewById(R.id.second_level_accountgroup_spn);
	}
	
	private void UpdateSpinner( Spinner spn, ArrayList<String> contents ) {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_item_gravity_right, contents);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spn.setAdapter(adapter);
	}
	
	private void StartListen() {
		TypeSpn.setOnItemSelectedListener(this);
		SubTypeSpn.setOnItemSelectedListener(this);
		
		findViewById(R.id.save_btn).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				SaveAccount();
			}
		});
		
		findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	}
}
