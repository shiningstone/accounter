
package com.shiningstone.accounter;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.Spinner;

public class TransactionTabActivity extends Activity implements OnClickListener,OnCheckedChangeListener,OnItemSelectedListener {
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
		ExpenseBtn.setChecked(true);
		UpdateOptions();
		
		ExpenseBtn.setOnCheckedChangeListener(this);
		CategorySpn.setOnItemSelectedListener(this);
		SubCateSpn.setOnItemSelectedListener(this);
		TradeDateBtn.setOnClickListener(this);
		MemoBtn.setOnClickListener(this);
		AmountBtn.setOnClickListener(this);
	}

	@Override
	protected void onActivityResult(int requestCode,int resultCode,Intent data) {
		if(requestCode==0 && resultCode==Activity.RESULT_OK) {
			Bundle extras = data.getExtras();
			String value = extras.getString("value");
			AmountBtn.setText(DecimalFormat.getCurrencyInstance().format(Double.parseDouble(value)));
		}
	}
	
	@Override
	public void onCheckedChanged(CompoundButton view, boolean isChecked) {
		UpdateOptions();
	}
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
		if( view==CategorySpn.getSelectedView() ) {
			SpinnerShow( SubCateSpn, OptionsOfSubCategory[CashFlowOption][position]);
		} else {
			
		}
	}
	
	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}

	@Override
	public void onClick(View v) {
		if( v==AmountBtn ) {
			UpdateAmount();
		}
		
		if( v==TradeDateBtn ) {
			UpdateDate();
		}

		if( v==MemoBtn ) {
			UpdateMemo();
		}
	}
	/**********************************************************
	 *    private method
	 **********************************************************/
	private void UpdateMemo() {
		
	}
	
	private void UpdateAmount() {
		Intent intent = new Intent(TransactionTabActivity.this,Keypad.class);
		intent.putExtra("value", "0");
		startActivityForResult(intent, 0);
	}
	
	private DatePickerDialog.OnDateSetListener mDateSetListenerSatrt = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int month, int day) {
			calendar.set(Calendar.YEAR, year);
			calendar.set(Calendar.MONTH, month);
			calendar.set(Calendar.DAY_OF_MONTH, day);
			
			TradeDateBtn.setText( format(calendar.getTime()) );
		}
	};

	private void UpdateDate() {
		DatePickerDialog datePicker = new DatePickerDialog(this, 
				mDateSetListenerSatrt,
				calendar.get(Calendar.YEAR), 
				calendar.get(Calendar.MONTH), 
				calendar.get(Calendar.DAY_OF_MONTH));
		datePicker.show();
	}
	
	private void UpdateOptions() {
		if(ExpenseBtn.isChecked()) {
			StoreFrm.setVisibility(View.VISIBLE);
			EmptyFrm.setVisibility(View.GONE);
			CashFlowOption = EXPENSE;
		} else {
			StoreFrm.setVisibility(View.GONE);
			EmptyFrm.setVisibility(View.VISIBLE);
			CashFlowOption = INCOME;
		}
		
		SpinnerShow( CategorySpn, OptionsOfCategory[CashFlowOption] );
		SpinnerShow( SubCateSpn, OptionsOfSubCategory[CashFlowOption][0]);
		SpinnerShow( AccountSpn, OptionsOfAccount);
		SpinnerShow( ItemSpn, OptionsOfItem);
		SpinnerShow( StoreSpn, OptionsOfStore);
	}
	
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
	private final int CASH_FLOW = 2;
	private final int INCOME = 0;
	private final int EXPENSE = 1;
	private int CashFlowOption = EXPENSE;
	
	private final int SUB_CATEGORY_NUM = 11;
	
	private Spinner CategorySpn = null;
	private Spinner SubCateSpn = null;
	private Spinner AccountSpn = null;
	private Spinner ItemSpn = null;
	private Spinner StoreSpn = null;
	
	private String[][] OptionsOfCategory = new String[CASH_FLOW][];
	private String[][][] OptionsOfSubCategory = new String[CASH_FLOW][SUB_CATEGORY_NUM][];
	private String[] OptionsOfAccount = null;
	private String[] OptionsOfItem = null;
	private String[] OptionsOfStore = null;
	
	private Button TradeDateBtn = null;
	private Button AmountBtn = null;
	private Button MemoBtn = null;
	private RadioButton ExpenseBtn = null;
	private FrameLayout StoreFrm = null;
	private FrameLayout EmptyFrm = null;
	
	private void LoadResources() {
		Resources res = this.getResources();

		OptionsOfCategory[EXPENSE] = res.getStringArray(R.array.TBL_EXPENDITURE_CATEGORY);
		OptionsOfSubCategory[EXPENSE][0] = res.getStringArray(R.array.TBL_EXPENDITURE_SUB_CATEGORY_1);
		OptionsOfSubCategory[EXPENSE][1] = res.getStringArray(R.array.TBL_EXPENDITURE_SUB_CATEGORY_2);
		OptionsOfSubCategory[EXPENSE][2] = res.getStringArray(R.array.TBL_EXPENDITURE_SUB_CATEGORY_3);
		OptionsOfSubCategory[EXPENSE][3] = res.getStringArray(R.array.TBL_EXPENDITURE_SUB_CATEGORY_4);
		OptionsOfSubCategory[EXPENSE][4] = res.getStringArray(R.array.TBL_EXPENDITURE_SUB_CATEGORY_5);
		OptionsOfSubCategory[EXPENSE][5] = res.getStringArray(R.array.TBL_EXPENDITURE_SUB_CATEGORY_6);
		OptionsOfSubCategory[EXPENSE][6] = res.getStringArray(R.array.TBL_EXPENDITURE_SUB_CATEGORY_7);
		OptionsOfSubCategory[EXPENSE][7] = res.getStringArray(R.array.TBL_EXPENDITURE_SUB_CATEGORY_8);
		OptionsOfSubCategory[EXPENSE][8] = res.getStringArray(R.array.TBL_EXPENDITURE_SUB_CATEGORY_9);
		OptionsOfSubCategory[EXPENSE][9] = res.getStringArray(R.array.TBL_EXPENDITURE_SUB_CATEGORY_10);
		OptionsOfSubCategory[EXPENSE][10] = res.getStringArray(R.array.TBL_EXPENDITURE_SUB_CATEGORY_11);

		OptionsOfCategory[INCOME] = res.getStringArray(R.array.TBL_INCOME_CATEGORY);
		OptionsOfSubCategory[INCOME][0] = res.getStringArray(R.array.TBL_INCOME_SUB_CATEGORY_1);
		OptionsOfSubCategory[INCOME][1] = res.getStringArray(R.array.TBL_INCOME_SUB_CATEGORY_2);
		
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
		AmountBtn = (Button) findViewById(R.id.cost_btn);
		MemoBtn = (Button) findViewById(R.id.memo_btn);
		StoreFrm = (FrameLayout)findViewById(R.id.corporation_fl);
		EmptyFrm = (FrameLayout)findViewById(R.id.empty_fl);
	}
}
