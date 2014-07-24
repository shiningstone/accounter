
package com.shiningstone.accounter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

public class SplashScreenActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_entrance);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Intent i=new Intent(SplashScreenActivity.this,MainActivity.class);
		startActivity(i);
		this.finish();

		return super.onTouchEvent(event);
	}
}