package com.shiningstone.accounter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class AccountActivity extends Activity
{
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account_activity);
	}
}
