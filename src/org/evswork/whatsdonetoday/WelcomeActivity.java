/* 
Copyright 2012 Javran Cheng

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package org.evswork.whatsdonetoday;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WelcomeActivity extends Activity {
	
	//TextView todayText;
	//TextView countText;
	EventDatabase mDB = null;
	LinearLayout infoLayout;

	@Override
	protected void onPause() {
		if (mDB != null) {
			mDB.close();
			mDB = null;
		}
		super.onPause();
	}

	private TextView stringToTextView(String text) {
		TextView tv = new TextView(this);
		tv.setText(text);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
		return tv;
	}
	
	@Override
	protected void onResume() {
		if (mDB == null)
			mDB = new EventDatabase(this);
		
		DateTime dt = DateTime.getCurrent();
		infoLayout.removeAllViews();
		infoLayout.addView(stringToTextView(dt.getDateString()),
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		
		//todayText.setText(dt.getDateString());
		//countText.setText( "Worth count: " + mDB.getDayWorthCount(dt) );
		infoLayout.addView(stringToTextView("Worth count: " + mDB.getDayWorthCount(dt)),
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		
		Cursor tCur = mDB.getCursorOfTagsSimple();
		if (tCur.moveToFirst()) {
			do {
				infoLayout.addView(stringToTextView(tCur.getString(1) + ": " + mDB.getDayWorthCountByTagId(tCur.getLong(0), dt)),
						ViewGroup.LayoutParams.WRAP_CONTENT,
						ViewGroup.LayoutParams.WRAP_CONTENT );
			} while (tCur.moveToNext());
		}
		
		
		super.onResume();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		((Button)findViewById(R.id.btn_add)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(
					WelcomeActivity.this,
					EventModifyPreferenceActivity.class);
				i.putExtra("org.evswork.whatsdonetoday.eventmodify.action", EventModifyPreferenceActivity.MODE_ADD);
				startActivity(i);
			}
		});
		((Button)findViewById(R.id.btn_detail)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(WelcomeActivity.this, DetailTabActivity.class));
			}
		});
		((Button)findViewById(R.id.btn_exit)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		//todayText = (TextView) findViewById(R.id.today);
		//countText = (TextView) findViewById(R.id.count);
		infoLayout = (LinearLayout) findViewById(R.id.infos);
	}


}
