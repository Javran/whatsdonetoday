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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class WhatsDoneTodayActivity extends Activity {
	/** Called when the activity is first created. */
	private EventDatabase mDB = null;
	Event eve;

	@Override
	protected void onPause() {
		mDB.close();
		mDB = null;
		super.onPause();
	}

	@Override
	protected void onResume() {
		if (mDB == null)
			mDB = new EventDatabase(getBaseContext());
		super.onResume();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mDB = new EventDatabase(getBaseContext());
		//((TextView) findViewById(R.id.text)).setText(mDB.runTest());
		((Button) findViewById(R.id.btn1))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						startActivity(new Intent(
								WhatsDoneTodayActivity.this,
								EventDetailListActivity.class));
					}
				});
		((Button) findViewById(R.id.btn2))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						startActivity(new Intent(
								WhatsDoneTodayActivity.this,
								TagDetailListActivity.class));
					}
				});
		((Button) findViewById(R.id.btn3))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						startActivity(new Intent(
								WhatsDoneTodayActivity.this,
								TagManageListActivity.class));
					}
				});
		((Button) findViewById(R.id.btn4))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Cursor e = mDB.getCursorOfEventsByTag("1");
						e.moveToFirst();
						Cursor t = mDB.getCursorOfTagsByEvent(e.getString(e.getColumnIndex("_id")));
						eve = new Event(e,t);
						
						Intent i = new Intent(
								WhatsDoneTodayActivity.this,
								EventModifyPreferenceActivity.class);
						i.putExtra("org.evswork.whatsdonetoday.eventmodify.action", EventModifyPreferenceActivity.MODE_MOD);
						i.putExtra("org.evswork.whatsdonetoday.eventmodify.src", eve);
						startActivity(i);
					}
				});
		
	}
}