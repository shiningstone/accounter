
package com.shiningstone.accounter;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Keypad extends Activity implements OnClickListener {
	private String value = "0";
	private boolean isValueEmpty = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.digit_keypad);
		
		LoadResources();
	}
	
	@Override
	public void onClick(View v) {
	
	}
	
	private void LoadResources() {
		BtnNums[0] = (Button) findViewById(R.id.zero);
		BtnNums[1] = (Button) findViewById(R.id.one);
		BtnNums[2] = (Button) findViewById(R.id.two);
		BtnNums[3] = (Button) findViewById(R.id.three);
		BtnNums[4] = (Button) findViewById(R.id.four);
		BtnNums[5] = (Button) findViewById(R.id.five);
		BtnNums[6] = (Button) findViewById(R.id.six);
		BtnNums[7] = (Button) findViewById(R.id.seven);
		BtnNums[8] = (Button) findViewById(R.id.eight);
		BtnNums[9] = (Button) findViewById(R.id.nine);
		BtnNums[DOT] = (Button) findViewById(R.id.dot);
		BtnNums[DELETE] = (Button) findViewById(R.id.delete);
		
		int i = 0;
		for (i=0;i<BtnNums.length;i++) {
			BtnNums[i].setOnClickListener(this);
		}
		
		BtnCtrl[SHOW] = (Button) findViewById(R.id.display);
		BtnCtrl[CANCEL] = (Button) findViewById(R.id.cancel);
		BtnCtrl[CLEAN] = (Button) findViewById(R.id.clean);
		BtnCtrl[DONE] = (Button) findViewById(R.id.done);
		
		for (i=0;i<BtnCtrl.length;i++) {
			BtnCtrl[i].setOnClickListener(this);
		}
	}
	private final int DOT = 10;
	private final int DELETE = 11;
	private Button[] BtnNums = new Button[10+2];

	private final int SHOW = 0;
	private final int CANCEL = SHOW+1;
	private final int CLEAN = CANCEL+1;
	private final int DONE = CLEAN+1;
	private Button[] BtnCtrl = new Button[DONE+1];
}