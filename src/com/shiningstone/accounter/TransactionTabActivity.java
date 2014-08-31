
package com.shiningstone.accounter;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.shiningstone.accounter.db.MyDbHelper;
import com.shiningstone.accounter.db.MyDbInfo;
import com.shiningstone.accounter.db.MyDbValue;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class TransactionTabActivity extends Activity implements OnClickListener,OnCheckedChangeListener,OnItemSelectedListener {
	final static int EDIT_MODE = 2;
	
	private String[] TITLES = null;
	private Calendar calendar = Calendar.getInstance();
	private MyDbHelper mDb = null;
	private MyDbInfo mDbInfo = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.transaction_tab_activity);

		TITLES = new String[]{getString(R.string.edit_income),getString(R.string.edit_expense)};
		mDb = SplashScreenActivity.db;
		mDbInfo = MyDbInfo.getInstance();

		Intent intent = getIntent();
		CashFlowOption = intent.getIntExtra("mode", 1);

		LoadDbStrings();
		LoadResources();
		
		ShowDate();
		ExpenseBtn.setChecked(true);
		UpdateOptions();
		
		StartListen();
		
		InitData();
	}

	@Override
	protected void onActivityResult(int requestCode,int resultCode,Intent data) {
		if(requestCode==0 && resultCode==Activity.RESULT_OK) {
			Bundle extras = data.getExtras();
			mValue = extras.getString("value");
			AmountBtn.setText( DecimalFormat.getCurrencyInstance().format(Double.parseDouble(mValue)) );
		}
	}
	
	@Override
	public void onCheckedChanged(CompoundButton view, boolean isChecked) {
		UpdateOptions();
	}
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
		if( view==CategorySpn.getSelectedView() ) {
			SpinnerShow( SubCateSpn, mDbStrings.SubCategories[CashFlowOption][position]);
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
		if(v == SaveBtn){
			Save();
		}
		if(v == CancelBtn){
			Exit();
		}
	}

	public void onBackPressed() {
		Exit();
	}
	/**********************************************************
	 *    private method
	 **********************************************************/
	private String mValue = "0";
	TransactionData mTransaction;
	private String[] mAccountId = null;
	private int mSubTypeId = 0;
	
	private void InitData() {
		if(CashFlowOption == EDIT_MODE){
			if( mTransaction.type==EXPENSE )
			{
				StoreFrm.setVisibility(View.VISIBLE);
				EmptyFrm.setVisibility(View.GONE);
			}else
			{
				StoreFrm.setVisibility(View.GONE);
				EmptyFrm.setVisibility(View.VISIBLE);
			}
			
			TypeRBtn.setVisibility(View.GONE);
			TitleText.setVisibility(View.VISIBLE);
			TitleText.setText( TITLES[mTransaction.type] );
			AmountBtn.setText(DecimalFormat.getCurrencyInstance().format(mTransaction.amount));

			mValue = String.valueOf(String.format("%.2f", mTransaction.amount));
			TradeDateBtn.setText(mTransaction.date);
			calendar.set(Integer.valueOf(mTransaction.date.substring(0, 4)), Integer.valueOf(mTransaction.date.substring(5, 7))-1, Integer.valueOf(mTransaction.date.substring(8, 10)));
			MemoBtn.setText(mTransaction.memo);
		}else{
			TradeDateBtn.setText(format(calendar.getTime()));
		}
		
		StoreSpn.setAdapter( GetItemsAdapter(7) );
		ItemSpn.setAdapter( GetItemsAdapter(8) );
		
		updateInfo(-1);
	}
	
	private String GetSelectiveString(int mode) {
		if ( mode==INCOME || mode==EDIT_MODE && mTransaction.type==INCOME ) {
			return "=0";
		} else {
			return "<>1";
		}
	}
	
	private int GetId(int mode) {
		if ( mode==INCOME || mode==EDIT_MODE && mTransaction.type==INCOME ) {
			return TBL_INCOME_CATE;
		} else {
			return TBL_EXPENSE_CATE;
		}
	}
	
	private int GetSubId(int mode) {
		if ( mode==INCOME || mode==EDIT_MODE && mTransaction.type==INCOME ) {
			return TBL_INCOME_SUBCATE;
		} else {
			return TBL_EXPENSE_SUBCATE;
		}
	}
	
	private void updateInfo(int position){
		if( position < 0 ) {
			LoadAccounts();
			CategorySpn.setAdapter( GetItemsAdapter( GetId(CashFlowOption) ) );
			position = 0;
		}
		
		LoadSubCategoriesSpinner( GetSubId(CashFlowOption),position );
	}
	
	private String[] GetAccountValues(int mode) {
		if (mode==EXPENSE || (mode==EDIT_MODE && mTransaction.type==EXPENSE)) {
			return new String[]{
					mValue,
					String.valueOf( CategorySpn.getSelectedItemPosition()+1 ),
					String.valueOf( SubCateSpn.getSelectedItemPosition()+mSubTypeId ),
					mAccountId[AccountSpn.getSelectedItemPosition()],
					String.valueOf( StoreSpn.getSelectedItemPosition()+1 ),
					String.valueOf( ItemSpn.getSelectedItemPosition()+1 ),
					TradeDateBtn.getText().toString(),
					MemoBtn.getText().toString()
			};
		} else {
			return new String[]{
					mValue,
					String.valueOf( CategorySpn.getSelectedItemPosition()+1 ),
					String.valueOf( SubCateSpn.getSelectedItemPosition()+mSubTypeId ),
					mAccountId[AccountSpn.getSelectedItemPosition()],
					String.valueOf( ItemSpn.getSelectedItemPosition()+1 ),
					TradeDateBtn.getText().toString(),
					MemoBtn.getText().toString()
			};
		}
	}
	
	private int GetFlowId(int mode) {
		if (mode==EXPENSE || (mode==EDIT_MODE && mTransaction.type==EXPENSE)) {
			return TBL_EXPENSE;
		} else {
			return TBL_INCOME;
		}
	}
	
	private void Save() {
		if( mValue.equals("") || mValue == null || Double.parseDouble(mValue) <= 0 ){
			Toast.makeText( getApplicationContext(), getString(R.string.input_message),Toast.LENGTH_SHORT ).show();
			return;
		}
		
		int tableId = GetFlowId(CashFlowOption);
		String[] values = GetAccountValues(CashFlowOption);
		updataAccount(CashFlowOption);

		String[] fields = new String[mDbInfo.FieldNames(tableId).length-1];
		System.arraycopy(mDbInfo.FieldNames(tableId),1,fields,0,mDbInfo.FieldNames(tableId).length-1);
		
		if(CashFlowOption == EDIT_MODE){
			mDb.update( mDbInfo.TableName(tableId), fields, values, "ID=" + mTransaction.infoId, null);
			Toast.makeText(getApplicationContext(), getString(R.string.edit_message),Toast.LENGTH_SHORT).show();
		}else{
			mDb.insert( mDbInfo.TableName(tableId), fields, values );
			Toast.makeText(getApplicationContext(), getString(R.string.save_message),Toast.LENGTH_SHORT).show();
		}
		Exit();
	}
	
	private void updataAccount(int mode){
		CommonData data = CommonData.getInstance();
		Iterator<AccountData> iteratorSort = data.mAccount.values().iterator();
		while (iteratorSort.hasNext()){
			AccountData account = iteratorSort.next();
			if( account.Id == Integer.parseInt(mAccountId[AccountSpn.getSelectedItemPosition()]) )
			{
				if(mode == INCOME){
					account.Balance = account.Balance+Double.parseDouble(mValue);
					data.Update(account);
				}else if(mode == EXPENSE){
					account.Balance = account.Balance-Double.parseDouble(mValue);
					data.Update(account);
				}
				return;
			}
		}
	}
	
	private void Exit() {
		if( CashFlowOption != EDIT_MODE ) {
			Intent intent = new Intent(TransactionTabActivity.this,MainActivity.class);
			startActivity(intent);
			finish();
		}else{
			this.setResult( RESULT_OK, getIntent() );  
            this.finish();  
		}
	}
	
	private void LoadAccounts() {
		ArrayAdapter<String> adapter;
		List<String> list = new ArrayList<String>();

		String strWhere = GetSelectiveString( CashFlowOption );
		Cursor cursor = mDb.select( mDbInfo.TableName(6), mDbInfo.FieldNames(6), 
					"(select POSTIVE from ACCOUNT_TYPE where ID=" + mDbInfo.FieldNames(6)[2] + ")" + strWhere, null, null, null, null);

		mAccountId = new String[cursor.getCount()];
		int account_num = 0;
		while (cursor.moveToNext()) {
			mAccountId[account_num] = cursor.getString(0);
			list.add( cursor.getString(1) );
			account_num++;
		}
		
		adapter = new ArrayAdapter<String>( this, android.R.layout.simple_spinner_item, list );
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		AccountSpn.setAdapter( adapter );
	}
	
	private ArrayAdapter<String> LoadAdapter(Cursor cursor) {
		List<String> list = new ArrayList<String>();
		while (cursor.moveToNext()) {
			list.add(cursor.getString(1));
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		return adapter;
	}
	
	private void LoadSubCategoriesSpinner( int id,int position ) {
		Cursor cursor = mDb.select(mDbInfo.TableName(id), mDbInfo.FieldNames(id), 
				mDbInfo.FieldNames(id)[2] + "=?", new String[]{String.valueOf(position + 1)}, null, null, null);
		SubCateSpn.setAdapter( LoadAdapter(cursor) );
		cursor.close();
	}

	private ArrayAdapter<String> GetItemsAdapter( int id ) {
		Cursor cursor = mDb.select( mDbInfo.TableName(id), mDbInfo.FieldNames(id), null, null, null, null, null);
		ArrayAdapter<String> adapter = LoadAdapter(cursor);
		cursor.close();
		
		return adapter;
	}
	
	private void StartListen() {
		ExpenseBtn.setOnCheckedChangeListener(this);
		CategorySpn.setOnItemSelectedListener(this);
		SubCateSpn.setOnItemSelectedListener(this);
		TradeDateBtn.setOnClickListener(this);
		MemoBtn.setOnClickListener(this);
		AmountBtn.setOnClickListener(this);
		SaveBtn.setOnClickListener(this);
		CancelBtn.setOnClickListener(this);
	}
	
	private void UpdateMemo() {
		MemoText = new EditText(this);
		MemoText.setLines(5);
		MemoText.setText(MemoBtn.getText());
		AlertDialog dialog = new AlertDialog.Builder(this)
						.setTitle(getString(R.string.dialog_memo_title))
						.setView(MemoText)
						.setPositiveButton(getString(R.string.dialog_memo_ok), new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								MemoBtn.setText(MemoText.getText());
							}
						}).setNegativeButton(getString(R.string.dialog_memo_cancle), new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
							}
						}).show();
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
		
		SpinnerShow( CategorySpn, mDbStrings.Categories[CashFlowOption] );
		SpinnerShow( SubCateSpn,  mDbStrings.SubCategories[CashFlowOption][0]);
		SpinnerShow( AccountSpn,  mDbStrings.Accounts);
		SpinnerShow( ItemSpn,     mDbStrings.Items);
		SpinnerShow( StoreSpn,    mDbStrings.Stores);
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
	private final int EXPENSE = 0;
	private final int INCOME = 1;
	private int CashFlowOption = EXPENSE;
	
	private final int SUB_CATEGORY_NUM = 11;
	
	private Spinner CategorySpn = null;
	private Spinner SubCateSpn = null;
	private Spinner AccountSpn = null;
	private Spinner ItemSpn = null;
	private Spinner StoreSpn = null;
	private MyDbValue mDbStrings = null;
	
	private Button TradeDateBtn = null;
	private Button AmountBtn = null;
	private Button MemoBtn = null;
	private RadioButton ExpenseBtn = null;
	private FrameLayout StoreFrm = null;
	private FrameLayout EmptyFrm = null;
	
	private EditText MemoText = null;
	
	private Button SaveBtn = null;
	private Button CancelBtn = null;
	
	private RadioGroup TypeRBtn = null;
	private TextView TitleText = null;
	
	private void LoadDbStrings() {
		mDbStrings = MyDbValue.getInstance( this.getApplicationContext() );
	}
	
	private void LoadResources() {
		Resources res = this.getResources();
		
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
		SaveBtn = (Button) findViewById(R.id.save_btn);
		CancelBtn = (Button) findViewById(R.id.cancel_btn);
		
		TypeRBtn = (RadioGroup) findViewById(R.id.trans_type_tab_rg);
		TitleText = (TextView) findViewById(R.id.title_tv);
	}
	
	private final int TBL_EXPENSE_CATE     = 0;
	private final int TBL_EXPENSE_SUBCATE  = 1;
	private final int TBL_INCOME_CATE      = 2;
	private final int TBL_INCOME_SUBCATE   = 3;
	private final int TBL_EXPENSE          = 9;
	private final int TBL_INCOME           = 10;
}
