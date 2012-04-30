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

import android.app.ExpandableListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorTreeAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

public class TagDetailListActivity extends ExpandableListActivity {
	private EventDatabase mDB = null;
	TagDetailTreeAdapter mAdapter;
	
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
		
		Cursor oldCursor = mAdapter.getCursor();
		mAdapter.changeCursor(mDB.getCursorOfTags());
		if (oldCursor != null)
			oldCursor.close();
		
		super.onResume();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mDB = new EventDatabase(getBaseContext());
		
		setContentView(R.layout.event_detail_list);
		
		// build adapter
		mAdapter = new TagDetailTreeAdapter(mDB.getCursorOfTags());
		setListAdapter(mAdapter);
	}
	
	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		Intent intent = new Intent(this, EventModifyPreferenceActivity.class);
		intent.putExtra("org.evswork.whatsdonetoday.eventmodify.action", EventModifyPreferenceActivity.MODE_MOD);
		intent.putExtra("org.evswork.whatsdonetoday.eventmodify.src", ((EventView)v).getEvent());
		startActivity(intent);
		return true;
	}

	private class TagDetailTreeAdapter extends CursorTreeAdapter {
		private LayoutInflater mInflater;
		
		public TagDetailTreeAdapter(Cursor cursor) {
			super(cursor, getBaseContext());
			mInflater = LayoutInflater.from(getBaseContext());
		}

		@Override
		protected void bindChildView(View view, Context context,
				Cursor cursor, boolean isLastChild) {
			EventView ev = (EventView)view;
			Cursor tagCur = mDB.getCursorOfTagsByEvent(cursor.getString(0));
			ev.updateData(cursor, tagCur);
		}

		@Override
		protected void bindGroupView(View view, Context context,
				Cursor cursor, boolean isExpanded) {
			ExpandableTwoLineView ev = (ExpandableTwoLineView)view;
			ev.setLargeLine(cursor.getString(1));
			ev.setSmallLine("Worth count: " + cursor.getInt(2));
		}

		@Override
		protected Cursor getChildrenCursor(Cursor groupCursor) {
			return mDB.getCursorOfEventsByTag(groupCursor.getString(0));
		}

		@Override
		protected View newChildView(Context context, Cursor cursor,
				boolean isLastChild, ViewGroup parent) {
			EventView ev = new EventView(context);
			bindChildView(ev, context, cursor, false);
			return ev;

		}

		@Override
		protected View newGroupView(Context context, Cursor cursor,
				boolean isExpanded, ViewGroup parent) {
			View v = new ExpandableTwoLineView(context);
			bindGroupView(v, context, cursor, false);
			return v;
		}

	}
}
