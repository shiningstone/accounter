
package com.shiningstone.accounter;
import com.shiningstone.accounter.db.MyDbHelper;
import com.shiningstone.accounter.db.MyDbInfo;
import com.shiningstone.accounter.db.MyDbValue;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MotionEvent;

public class SplashScreenActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_entrance);
		DbInit();
		CommonData.getInstance().load(this);
		onTouchEvent(null);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Intent i=new Intent(SplashScreenActivity.this,MainActivity.class);
		startActivity(i);
		this.finish();

		return super.onTouchEvent(event);
	}
	
	/***************************************************************
				private methods
	****************************************************************/
	public static MyDbHelper db = null;
	private void DbInit() {
		MyDbInfo  dbInfo    = MyDbInfo.getInstance();
		
		MyDbValue dbStrings = MyDbValue.getInstance( this.getApplicationContext() );
		final int EXPENSE   = dbStrings.EXPENSE;
		final int INCOME    = dbStrings.INCOME;
		
		db = MyDbHelper.getInstance( this.getApplicationContext() );
		db.open();
		
		Cursor cursor = db.select( dbInfo.TableName(0), new String[] {
		"ID", "NAME", "BUDGET" }, null, null, null, null, null);
		if (cursor.moveToNext()) {
			cursor.close();
			return;
		}

		String[] arr = dbStrings.Categories[EXPENSE];
		for (int cateIdx = 0; cateIdx < arr.length; cateIdx++) {
			db.insert("EXPENSE_CATEGORY", new String[][] {{"NAME",arr[cateIdx]},{"BUDGET","0"}});

			String[] arr2 = dbStrings.SubCategories[EXPENSE][cateIdx];
			for (int i = 0; i < arr2.length; i++) {
				db.insert("EXPENSE_SUB_CATEGORY", 
						new String[][] {
							{"NAME",arr2[i]}, 
							{"PARENT_CATEGORY_ID",String.valueOf(cateIdx+1)} 
						}
				);
			}
		}
		
		arr = dbStrings.Categories[INCOME];
		for (int cateIdx = 0; cateIdx < arr.length; cateIdx++) {
			db.insert("INCOME_CATEGORY", new String[][] { {"NAME",arr[cateIdx]} } );

			String[] arr2 = dbStrings.SubCategories[INCOME][cateIdx];
			if( arr2!=null ) {
				for (int i = 0; i < arr2.length; i++) {
					db.insert("INCOME_SUB_CATEGORY", 
							new String[][] { 
								{"NAME",arr2[i]}, 
								{"PARENT_CATEGORY_ID",String.valueOf(cateIdx+1)} 
							}
					);
				}
			}
		}
		
		arr = dbStrings.AccountTypes;
		for (int cateIdx = 0; cateIdx < arr.length; cateIdx++) {
			db.insert("ACCOUNT_TYPE", 
					new String[][] { 
						{"NAME",    arr[cateIdx].substring(0, arr[cateIdx].indexOf(","))  },
						{"POSTIVE", arr[cateIdx].substring(arr[cateIdx].indexOf(",") + 1) }
					} 
			);
			
			String[] arr2 = dbStrings.AccountSubTypes[cateIdx];
			for (int i = 0; i < arr2.length; i++) {
				db.insert("ACCOUNT_SUB_TYPE", new String[][] { {"NAME",arr2[i]}, {"PARENT_TYPE_ID",String.valueOf(cateIdx+1)} });
			}
		}
		
		arr = dbStrings.Accounts;
		for (int i = 0; i < arr.length; i++) {
			String fields[] = arr[i].split(","); 
			db.insert("ACCOUNT", 
					new String[][] { 
						{"NAME",fields[0]},
						{"TYPE_ID",fields[1]},					
						{"SUB_TYPE_ID",fields[2]},					
						{"ACCOUNT_BALANCE",fields[3]},					
					} 
			);
		}
		
		arr = dbStrings.Items;
		for (int i = 0; i < arr.length; i++) {
			db.insert("ITEM", new String[][] { {"NAME",arr[i]} } );
		}
		
		arr = dbStrings.Stores;
		for (int i = 0; i < arr.length; i++) {
			db.insert("STORE", new String[][] { {"NAME",arr[i]} } );
		}
	}
}