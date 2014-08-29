package com.shiningstone.accounter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

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
		AccountData data = (AccountData)view.getTag();
		if (data != null) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setItems(R.array.setting_listview_item_operation, new AccountItemLongClickListener(this, data));
			builder.create().show();
			return false;
		} else {
			return true;
		}
	}

	public void Update() {
		new AccountListAsyncTask().execute(this);
	}
	
	class AccountItemLongClickListener implements DialogInterface.OnClickListener {
		AccountActivity mEmployer;
		AccountData     data;

		public AccountItemLongClickListener(AccountActivity activity, AccountData account) {
			mEmployer = activity;
			data = account;
		}
		
		public void onClick(DialogInterface dialog, int which) {
			if (which == 0) {
				StartAccountEditorActivity();
			} else if (data.Balance == 0) {
				StartDeleteDialogue();
			} else {
				Toast.makeText(mEmployer, R.string.account_canot_delete, 0).show();
			}
		}

		private void StartAccountEditorActivity() {
			Intent intent = new Intent(mEmployer, AccountEditorActivity.class);
			intent.putExtra("mode", AccountEditorActivity.EDIT);
			intent.putExtra("accountid", data.Id);
			mEmployer.startActivityForResult(intent, 0);
		}
	
		private void StartDeleteDialogue() {
			AlertDialog.Builder builder = new AlertDialog.Builder(mEmployer);
			builder.setTitle(R.string.delete_title);
			builder.setMessage(R.string.delete_message);
			builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					CommonData.getInstance().Delete(data.Id);
					Update();
					Toast.makeText(mEmployer, getString(R.string.message_delete_ok), 0).show();
				}
			});
			builder.setNegativeButton(R.string.delete_cancel, null);
			builder.create().show();
		}
	}
}
