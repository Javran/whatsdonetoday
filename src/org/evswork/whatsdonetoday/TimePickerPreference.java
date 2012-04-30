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

import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.widget.TimePicker;


public class TimePickerPreference extends Preference {
	public int hour = 10;
	public int minute = 20;
	

	public TimePickerPreference(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		initPreference();
	}

	public TimePickerPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		initPreference();
	}
	
	private void initPreference() {
		setPersistent(false);
		setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				TimePickerDialog dialog = new TimePickerDialog(getContext(), new OnTimeSetListener() {
					
					@Override
					public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
						TimePickerPreference.this.hour = hourOfDay;
						TimePickerPreference.this.minute = minute;
						updateUI();
					}
				}, hour, minute,true);
				dialog.show();
				return true;
			}
		});
		updateUI();
	}
	
	public void updateUI() {
		setSummary(buildTimeString(hour, minute));
	}
	
	public static String buildTimeString(int hour, int minute) {
		return String.format("%02d:%02d", hour, minute);
	}
}
