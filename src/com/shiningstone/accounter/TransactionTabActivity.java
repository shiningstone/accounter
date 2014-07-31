
package com.shiningstone.accounter;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.Spinner;

public class TransactionTabActivity extends Activity implements OnCheckedChangeListener,OnItemSelectedListener {
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

		LoadResources();
		
		ShowDate();
		
		SpinnerShow( CategorySpn, OptionsOfCategory );
		SpinnerShow( SubCateSpn, OptionsOfSubCategory);
		SpinnerShow( AccountSpn, OptionsOfAccount);
		SpinnerShow( ItemSpn, OptionsOfItem);
		SpinnerShow( StoreSpn, OptionsOfStore);
		
		RadioButton rb1 = (RadioButton) findViewById(R.id.payout_tab_rb);
		rb1.setChecked(true);
		rb1.setOnCheckedChangeListener(this);
	}

	@Override
	protected void onActivityResult(int requestCode,int resultCode,Intent data) {
		if(requestCode==0 && resultCode==Activity.RESULT_OK) {
			Bundle extras = data.getExtras();
			String value = extras.getString("value");
			
			Button cost_btn = (Button)findViewById(R.id.cost_btn);
			cost_btn.setText(DecimalFormat.getCurrencyInstance().format(Double.parseDouble(value)));
		}
	}
	
	@Override
	public void onCheckedChanged(CompoundButton view, boolean isChecked) {

		if(ExpenseBtn.isChecked()) {
			StoreFrm.setVisibility(View.VISIBLE);
			EmptyFrm.setVisibility(View.GONE);
		} else {
			StoreFrm.setVisibility(View.GONE);
			EmptyFrm.setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
		if( view==CategorySpn.getSelectedView() ) {
			
		}
	}
	
	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}

	
	/**********************************************************
	 *    private method
	 **********************************************************/
	private void ShowDate() {
		TradeDateBtn.setText(format(calendar.getTime()));
	}
	
	private void SpinnerShow(Spinner target,String[] options) {
		List<String> list = new ArrayList<String>();
		for (int i=0;i<options.length;i++ ) {
			list.add(options[i]);
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		target.setAdapter( adapter );
	}

	private String format(Date date){
		SimpleDateFormat ymd = new SimpleDateFormat("yyyy-MM-dd");
		return ymd.format(date); 
	}

	/**********************************************************
	 *    resources control
	 **********************************************************/
	private Spinner CategorySpn = null;
	private Spinner SubCateSpn = null;
	private Spinner AccountSpn = null;
	private Spinner ItemSpn = null;
	private Spinner StoreSpn = null;
	
	private String[] OptionsOfCategory = null;
	private String[] OptionsOfSubCategory = null;
	private String[] OptionsOfAccount = null;
	private String[] OptionsOfItem = null;
	private String[] OptionsOfStore = null;
	
	private Button TradeDateBtn = null;
	private RadioButton ExpenseBtn = null;
	private FrameLayout StoreFrm = null;
	private FrameLayout EmptyFrm = null;
	
	private void LoadResources() {
		Resources res = this.getResources();

		OptionsOfCategory = res.getStringArray(R.array.TBL_EXPENDITURE_CATEGORY);
		OptionsOfSubCategory = res.getStringArray(R.array.TBL_EXPENDITURE_SUB_CATEGORY_1);
		OptionsOfAccount = res.getStringArray(R.array.TBL_ACCOUNT);;
		OptionsOfItem = res.getStringArray(R.array.TBL_ITEM);
		OptionsOfStore = res.getStringArray(R.array.TBL_STORE);;
		
		CategorySpn = (Spinner) findViewById(R.id.first_level_category_spn);
		SubCateSpn = (Spinner) findViewById(R.id.sub_category_spn);
		AccountSpn = (Spinner) findViewById(R.id.account_spn);
		ItemSpn = (Spinner) findViewById(R.id.project_spn);
		StoreSpn = (Spinner) findViewById(R.id.corporation_spn);

		TradeDateBtn = (Button) findViewById(R.id.trade_time_btn);
		ExpenseBtn = (RadioButton) findViewById(R.id.payout_tab_rb);
		StoreFrm = (FrameLayout)findViewById(R.id.corporation_fl);
		EmptyFrm = (FrameLayout)findViewById(R.id.empty_fl);
	}
}
