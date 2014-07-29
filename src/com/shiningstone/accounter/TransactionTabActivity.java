
package com.shiningstone.accounter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class TransactionTabActivity extends Activity {
	final static int INCOME_MODE = 0;
	final static int PAYOUT_MODE = 1;
	final static int EDIT_MODE = 2;
	
	private String[] TITLES = null;
	private int mMode = 0;
	private Calendar calendar = Calendar.getInstance();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.transaction_tab_activity);

		TITLES = new String[]{getString(R.string.edit_income),getString(R.string.edit_expense)};
		Intent intent = getIntent();
		mMode = intent.getIntExtra("mode", 1);
		
		ShowDate();
		ShowItems();
		ShowStores();
		
	}

	private void ListviewShow(Spinner target,String[] options) {
		List<String> list = new ArrayList<String>();
		for (int i=0;i<options.length;i++ ) {
			list.add(options[i]);
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		target.setAdapter( adapter );
	}

	private void ShowItems() {
		Resources res = this.getResources();
		String[]  options = res.getStringArray(R.array.TBL_ITEM);
		Spinner   spinner = (Spinner) findViewById(R.id.project_spn);

		ListviewShow(spinner,options);
	}

	private void ShowStores() {
		Resources res = this.getResources();
		String[]  options = res.getStringArray(R.array.TBL_STORE);
		Spinner   spinner = (Spinner) findViewById(R.id.corporation_spn);
		
		ListviewShow(spinner,options);
	}
	
	private void ShowDate() {
		Button trade_time_btn = (Button) findViewById(R.id.trade_time_btn);
		trade_time_btn.setText(format(calendar.getTime()));
	}
	
	private String format(Date date){
		SimpleDateFormat ymd = new SimpleDateFormat("yyyy-MM-dd");
		return ymd.format(date); 
	}
}
