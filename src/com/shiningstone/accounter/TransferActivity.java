package com.shiningstone.accounter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class TransferActivity extends Activity implements OnClickListener,OnItemSelectedListener{

	private TransferData transfer = null;
	CommonData commondata = CommonData.getInstance();
	private Calendar calendar = Calendar.getInstance();
	private DatePickerDialog datePicker = null;
	private AlertDialog dialog = null;
	
	
	private boolean isInitSpn = true;
	private String strNum = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.transfer_activity);
		
		LoadResources();
		StartListen();
		SpinnerInit();
		
		date_btn.setText(format(calendar.getTime()));
	}

	@Override
	public void onClick(View v) {
		if(v == date_btn){
			ShowDateEditor();
		}
		if(v == memo_btn){
			ShowMemoEditor();
		}
		if(v == ok_btn){
			Save();
		}
	}
	
	private void Save(){
		double transfer_amount;
		transfer = new TransferData();
		if (TextUtils.isEmpty(amount_et.getText().toString())) {
			Toast.makeText(getApplicationContext(), getString(R.string.transfer_amount_empty), 1).show();
			return;
		}
		
		try {
			transfer_amount = Double.valueOf(amount_et.getText().toString()).doubleValue();
		} catch (NumberFormatException e) {
			Toast.makeText(getApplicationContext(), getString(R.string.transfer_amount_number), 1).show();
			return;
		}
		if(transfer_amount == 0){
			Toast.makeText(getApplicationContext(),getString(R.string.transfer_amount_0), 1).show();
			return;
		}
		
		transfer.Amount= (-transfer_amount);
		transfer.Account = source_spn.getSelectedItemPosition()+1;
		transfer.Item = category_spn.getSelectedItemPosition()+1;
		transfer.Date = date_btn.getText().toString();
		transfer.Memo = memo_btn.getText().toString();
		commondata.Add(transfer);
		
		transfer.Amount= transfer_amount;
		transfer.Account = dest_spn.getSelectedItemPosition()+1;
		commondata.Add(transfer);
		
		Toast.makeText(getApplicationContext(), getString(R.string.transfer_ok), 0).show();
		this.setResult(RESULT_OK, getIntent());  
        this.finish();  
	}
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		if(view == source_spn.getSelectedView()){
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}
	
	/****************************************************
					private methods
	****************************************************/
	private void SpinnerInit(){
		ArrayList<String> transfer_account = new ArrayList<String>();
		for (AccountData item : commondata.mAccount.values()) {
			transfer_account.add(item.Name);
		}
		LoadSpinner(source_spn,transfer_account);
		LoadSpinner(dest_spn,transfer_account);
		
		ArrayList<String> transfer_item = new ArrayList<String>();
		for (CommonData.TransferItem item : commondata.mTransfer.values()) {
			transfer_item.add(item.name);
		}
		LoadSpinner(category_spn,transfer_item);
	}
	
	private void LoadSpinner( Spinner sp,ArrayList<String> list ){
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_item_gravity_right, list);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp.setAdapter(adapter);
	}
	
	private void ShowDateEditor() {
		datePicker = new DatePickerDialog(this, mDateSetListenerStart,
				calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
		datePicker.show();
	}
	
	private DatePickerDialog.OnDateSetListener mDateSetListenerStart = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int month, int day) {
			calendar.set(Calendar.DAY_OF_MONTH, day);
			calendar.set(Calendar.MONTH, month);
			calendar.set(Calendar.YEAR, year);
			
			date_btn.setText(format(calendar.getTime()));
		}
	};

	private EditText edit = null;
	private void ShowMemoEditor() {
		edit = new EditText(this);/* why this variable cannot be declared in this function ??? */

		edit.setLines(5);
		edit.setText(memo_btn.getText());
		
		dialog = new AlertDialog.Builder(this)
			.setTitle(getString(R.string.dialog_memo_title))
			.setView(edit)
			.setPositiveButton(getString(R.string.dialog_memo_ok), 
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										memo_btn.setText( edit.getText() );
									}
								})
			.setNegativeButton(getString(R.string.dialog_memo_cancle), 
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
									}
								})
			.show();
	}

	private String format(Date date){
		String str = "";
		SimpleDateFormat ymd = null;
		ymd = new SimpleDateFormat("yyyy-MM-dd");
		str = ymd.format(date); 
		return str;
	}

	/****************************************************
					resources
	****************************************************/
	private Spinner source_spn = null;
	private Spinner dest_spn = null;
	private EditText amount_et = null;
	private Button date_btn = null;
	private Spinner category_spn = null;
	private Button memo_btn = null;
	private Button ok_btn = null;
	
	private void LoadResources() {
		source_spn = (Spinner) findViewById(R.id.transfer_out_account_spn);
		dest_spn = (Spinner) findViewById(R.id.transfer_in_account_spn);
		amount_et = (EditText) findViewById(R.id.transfer_amount_out_et);
		date_btn = (Button) findViewById(R.id.transfer_info_tradetime_btn);
		category_spn = (Spinner) findViewById(R.id.transfer_info_project_spn);
		memo_btn = (Button) findViewById(R.id.transfer_info_memo_btn);
		ok_btn = (Button) findViewById(R.id.transfer_btn);
	}
	
	private void StartListen() {
		source_spn.setOnItemSelectedListener(this);
		dest_spn.setOnItemSelectedListener(this);
		amount_et.addTextChangedListener( AmountWatcher );
		date_btn.setOnClickListener(this);
		memo_btn.setOnClickListener(this);
		ok_btn.setOnClickListener(this);
	}
	
	TextWatcher AmountWatcher = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			Editable etext = amount_et.getText(); 

			if(s.toString().indexOf(".") > -1){
				if (s.length() > 12) {
					amount_et.setText(strNum);
					Selection.setSelection(etext, etext.length());
				}else if(s.toString().indexOf(".")<s.length()-3){
					amount_et.setText(s.subSequence(0, s.toString().indexOf(".")+3));
					Selection.setSelection(etext, etext.length());
				}
			}else{
				if (s.length() > 9) {
					amount_et.setText(strNum);
					Selection.setSelection(etext, etext.length());
				}
			}
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			strNum = s.toString();
		}
		
		@Override
		public void afterTextChanged(Editable s) {
		}
	};
}
