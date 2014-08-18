package com.shiningstone.accounter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

public class AccountActivity extends Activity implements OnClickListener, OnItemLongClickListener
{
	ListView account_lv;
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account_activity);
		
		account_lv = (ListView)findViewById(R.id.account_lv);
		account_lv.setFocusable(false);
		account_lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		account_lv.setOnItemLongClickListener(this);
		
		findViewById(R.id.add_btn).setOnClickListener(this);
		findViewById(R.id.go_to_transfer_btn).setOnClickListener(this);
		
		Update();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add_btn:
			Intent intent = new Intent(this, AccountEditorActivity.class);
			intent.putExtra("mode", AccountEditorActivity.CREATE);
		    startActivityForResult(intent, 0);
			break;
		case R.id.go_to_transfer_btn:
		    startActivityForResult(new Intent(this, TransferActivity.class), 0);
			break;
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		return true;
	}

	public void Update() {
		new AccountListAsyncTask().execute(this);
	}
}
