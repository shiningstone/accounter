/**
 * 
 */
package com.shiningstone.accounter;

import java.text.DecimalFormat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author
 *
 */
public class BudgetActivity extends Activity implements OnItemClickListener{
	ListView budget_lv;
	
	private View empty_tips;
	private int mSelectedBudget = -1;
	CommonData commondata = CommonData.getInstance();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.budget_activity);
		
		empty_tips = ((LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.common_lv_empty_tips, null);

		budget_lv = (ListView)findViewById(R.id.budget_category_lv);
		budget_lv.setOnItemClickListener(this);
		budget_lv.setEmptyView(empty_tips);

		Refresh();
	}
	
	/* reaction of keypad */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode == RESULT_OK){
			Bundle extras = data.getExtras(); 
			Refresh();
			UpdateSelectedBudget( extras.getString("value") );
		}
	}
	
	private void Refresh() {
		new BudgetListAsyncTask().execute(this);
	}

	private void UpdateSelectedBudget( String balance ) {
		if(mSelectedBudget != -1){
			BudgetData budget = commondata.mBudgetData.get( mSelectedBudget );
			budget.Balance = Double.valueOf( balance );
			commondata.Update( budget );
			
			mSelectedBudget = -1;

			Toast.makeText(this, getString(R.string.budget_ok), 0).show();
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		BudgetData data = (BudgetData)view.getTag();
		
		mSelectedBudget = data.Id;
		StartKeyPad( data.Balance );
	}
	
	public void onBackPressed() {
		setResult(RESULT_OK, this.getIntent());
		finish();
	}
	
	private void StartKeyPad(double initValue) {
		Intent i=new Intent(this,Keypad.class);
		i.putExtra( "value", String.valueOf(initValue) );
		startActivityForResult(i, 0);
	}
}
