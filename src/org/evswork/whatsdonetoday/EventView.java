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

import android.content.Context;
import android.database.Cursor;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class EventView extends LinearLayout {
	private TextView TimeText;
	private TextView WorthText;
	private TextView TagsText;
	private TextView DescText;
	private LinearLayout layout;

	// column required: [event] _id, time, worth, description, [tag] title
	// get titles from database, and others from cursor directly

	private Event event;
	
	public EventView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public EventView(Context context) {
		super(context);
		initView();
	}

	public void updateData(Cursor eventCursor, Cursor tagCursor) {
		event = new Event(eventCursor, tagCursor);
		updateUI();
	}
	
	public Event getEvent() {
		return event;
	}
	
	public void updateUI()
	{
		TimeText.setText(event.dateTime.toStringUI());
		WorthText.setText("" + event.worth);
		// at least one element should be available in tagsRaw
		StringBuilder sb = new StringBuilder();
		
		if (event.tags.size() == 0) {
			sb.append(Tag.NO_TAG.title);
		} else {
			sb.append(event.tags.get(0).title);
			for (int i=1; i<event.tags.size(); ++i)
				sb.append(", ").append(event.tags.get(i).title);
		}
		
		TagsText.setText(sb.toString());
		DescText.setText(event.description);
		
	}

	private void initView() {
		LayoutInflater inflater = LayoutInflater.from(getContext());
		event = null;
		layout = (LinearLayout) inflater.inflate(R.layout.event_view,
				this);
		// now bind views
		TimeText = (TextView) layout.findViewById(R.id.text_time);
		WorthText = (TextView) layout.findViewById(R.id.text_worth);
		TagsText = (TextView) layout.findViewById(R.id.text_tags);
		DescText = (TextView) layout.findViewById(R.id.text_desc);
	}
}
