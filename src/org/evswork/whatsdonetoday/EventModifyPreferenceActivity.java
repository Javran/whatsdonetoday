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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.widget.Toast;

public class EventModifyPreferenceActivity extends PreferenceActivity {
	EventDatabase mDB;
	DatePickerPreference DatePref;
	TimePickerPreference TimePref;
	TagSelectListPreference TagPref;
	EditTextPreference WorthPref;
	EditTextPreference DescPref;
	Preference OKPref, CancelPref;
	
	Event srcEvent;
	
	private int mode;
	public final static int MODE_ADD = 0;
	public final static int MODE_MOD = 1;
	public final static int MODE_ERR = -1;
	
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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mDB = new EventDatabase(getBaseContext());
		addPreferencesFromResource(R.xml.event_mod_pref);
		
		// bind views && set up
		DatePref = (DatePickerPreference)findPreference("pref_date");
		TimePref = (TimePickerPreference)findPreference("pref_time");
		WorthPref = (EditTextPreference)findPreference("pref_worth");
		WorthPref.getEditText().setFilters(new InputFilter[] {new DigitsKeyListener()});
		WorthPref.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
		
		TagPref = (TagSelectListPreference)findPreference("pref_tag");
		DescPref = (EditTextPreference)findPreference("pref_descript");
		DescPref.getEditText().setFilters(new InputFilter[] {new InputFilter.LengthFilter(50)});
		
		OKPref = findPreference("pref_ok");
		OKPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				Event newEvent = buildEvent();
				
				if (newEvent != null) {
					if (mode == MODE_ADD) {	// MODE_ADD
						mDB.insertEvent(newEvent);
					} else {		// MODE_MOD
						if (!srcEvent.equalInContent(newEvent)) {
							mDB.updateEvent(newEvent);
						} 
					}
				}
				else {
					Toast.makeText(
						getBaseContext(), 
						"Failed to create an event", 
						Toast.LENGTH_SHORT).show();
				}
				finish();
				
				return true;
			}
		});
		
		CancelPref = findPreference("pref_cancel");
		CancelPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				finish();
				return true;
			}
		});
		
		// read intent
		// "org.evswork.whatsdonetoday.eventmodify.action" = MODE_ADD | MODE_MOD (required)
		// "org.evswork.whatsdonetoday.eventmodify.src" = Event (required by MODE_MOD mode)
		mode = getIntent().getIntExtra("org.evswork.whatsdonetoday.eventmodify.action", MODE_ERR);
		if (mode == MODE_ERR) {
			throw new RuntimeException("mode not supported");
		}
		
		if (mode == MODE_MOD) {
			srcEvent = (Event) getIntent().getExtras().get("org.evswork.whatsdonetoday.eventmodify.src");
			if (srcEvent == null)
				throw new RuntimeException("Event object is required by MODE_MOD but not found anywhere");
			loadFrom(srcEvent);
			
			addPreferencesFromResource(R.xml.event_mod_remove);
			Preference prefRemove = findPreference("pref_remove");
			prefRemove.setOnPreferenceClickListener(new OnPreferenceClickListener() {
				
				@Override
				public boolean onPreferenceClick(Preference preference) {
					new AlertDialog.Builder(EventModifyPreferenceActivity.this)
						.setMessage("Confirm removing this event?")
						.setPositiveButton("Yes", new OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								mDB.removeEventById(srcEvent.eventId);
								finish();
							}
						})
						.setNegativeButton("No", null)
						.show();
					
					
					
					return true;
				}
			});
		}
		
		if (mode == MODE_ADD) {
			createNew();
			srcEvent = null;
		}
		
	}
	
	private void createNew() {
		GregorianCalendar c = new GregorianCalendar();
		DatePref.year = c.get(Calendar.YEAR);
		DatePref.month = c.get(Calendar.MONTH) + 1;
		DatePref.day = c.get(Calendar.DATE);
		DatePref.updateUI();
		
		TimePref.hour = c.get(Calendar.HOUR_OF_DAY);
		TimePref.minute = c.get(Calendar.MINUTE);
		TimePref.updateUI();
		
		WorthPref.content = "0";
		WorthPref.updateUI();
		
		Cursor tagCur = mDB.getCursorOfTagsSimple();
		TagPref.tags = new Tag[tagCur.getCount()];
		if (tagCur.moveToFirst()) {
			int i = 0;
			do {
				TagPref.tags[i] = new Tag(tagCur);
				++i;
			} while (tagCur.moveToNext());
		}
		tagCur.close();
		TagPref.chosenTags = new ArrayList<Tag>();
		
		DescPref.content = "";
		DescPref.updateUI();
	}

	private void loadFrom(Event event) {
		DatePref.year = event.dateTime.year;
		DatePref.month = event.dateTime.month;
		DatePref.day = event.dateTime.day;
		DatePref.updateUI();
		
		TimePref.hour = event.dateTime.hour;
		TimePref.minute = event.dateTime.minute;
		TimePref.updateUI();
		
		WorthPref.content = "" + event.worth;
		WorthPref.updateUI();
		
		Cursor tagCur = mDB.getCursorOfTagsSimple();
		TagPref.tags = new Tag[tagCur.getCount()];
		if (tagCur.moveToFirst()) {
			int i = 0;
			do {
				TagPref.tags[i] = new Tag(tagCur);
				++i;
			} while (tagCur.moveToNext());
		}
		tagCur.close();
		TagPref.chosenTags = new ArrayList<Tag>();
		for (Tag t : event.tags) {
			TagPref.chosenTags.add(t);
		}
		TagPref.updateUI();
		
		DescPref.content = event.description;
		DescPref.updateUI();
	}
	
	private Event buildEvent() {
		// create a fake id for adding mode, and use original srcEvent for modifying mode
		Event event = new Event( mode == MODE_ADD ? Event.FAKE_ID : srcEvent.eventId);
		// field needed:
		// dateTime
		// worth
		// tags
		// description
		
		event.dateTime = new DateTime(
			DatePref.year, DatePref.month, DatePref.day,
			TimePref.hour, TimePref.minute, 0
		);
		
		// InputFilter has been responsible for the validity of input
		event.worth = Integer.valueOf(WorthPref.content);
		event.tags = TagPref.chosenTags;
		event.description = DescPref.content;
		
		// discard Event if some fields are failed on construction
		if (event.dateTime == null || event.tags == null || event.description == null)
			event = null;
		return event;
	}

}
