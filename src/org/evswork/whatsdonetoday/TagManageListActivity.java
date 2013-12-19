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

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TagManageListActivity extends ListActivity {
	private EventDatabase mDB;
	TagManageAdapter mAdapter;
	private Button btnAdd;

	@Override
	protected void onPause() {
		Cursor oldCursor = mAdapter.getCursor();
		mAdapter.changeCursor(null);
		if (oldCursor != null)
			oldCursor.close();
		mDB.close();
		mDB = null;
		super.onPause();
	}

	@Override
	protected void onResume() {
		if (mDB == null)
			mDB = new EventDatabase(getBaseContext());
		Cursor oldCursor = mAdapter.getCursor();
		mAdapter.changeCursor(mDB.getCursorOfTagsSimple());
		if (oldCursor != null)
			oldCursor.close();
		super.onResume();
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, final long id) {
		new AlertDialog.Builder(this)
			.setItems(new String[] { "Modify tag", "Remove tag", "Show/Hide in welcome screen", "Cancel"}, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch (which) {
					case 0: // modify
						onModifyTag(id);
						break;
					case 1: // remove
						onRemoveTag(id);
						break;
					case 2:
						onShowHideTagInWelcome(id);
						break;
					}
				}
			})
			.show();
		
		
	}

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mDB = new EventDatabase(getBaseContext());
		setContentView(R.layout.tag_manage_list);
		
		// build adapter
		mAdapter = new TagManageAdapter(mDB.getCursorOfTagsSimple());
		setListAdapter(mAdapter);
		
		btnAdd = (Button)findViewById(R.id.btn_add);
		btnAdd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onCreateNewTag();
			}
		});
	}
	
	private void onCreateNewTag() {
		final EditText text = new EditText(this);
		text.setFilters(new InputFilter[] {new InputFilter.LengthFilter(20)});
		
		new AlertDialog.Builder(this)
			.setTitle("New Tag Name")
			.setView(text)
			.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					String tagName = text.getText().toString();
					if (tagName == null || tagName.length() == 0)
						return;
					try {
						mDB.addNewTag(tagName);
					} catch (SQLException e) {
						Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show();
					}
					mAdapter.changeCursor(mDB.getCursorOfTagsSimple());
				}
			})
			.setNegativeButton("Cancel", null)
			.show();
		
	}
	
	private void onModifyTag(final long id) {
		Cursor cur = mDB.getDatabase(false).rawQuery(
			"SELECT title FROM Tag WHERE _id == ?", 
			new String [] { "" + id});
		cur.moveToFirst();
		
		final EditText text = new EditText(this);
		text.setFilters(new InputFilter[] {new InputFilter.LengthFilter(20)});
		text.setText(cur.getString(0));
		cur.close();
		
		new AlertDialog.Builder(this)
			.setTitle("Modify Tag Name")
			.setView(text)
			.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
				@Override
				public void onClick(DialogInterface dialog, int which) {
					String tagName = text.getText().toString();
					if (tagName == null || tagName.length() == 0)
						return;
					try {
						mDB.modifyTagById(id, tagName);
					} catch (SQLException e) {
						Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show();
					}
					mAdapter.changeCursor(mDB.getCursorOfTagsSimple());
				}
			})
			.setNegativeButton("Cancel", null)
			.show();
	}
	
	private void onRemoveTag(final long id) {
		new AlertDialog.Builder(this)
			.setMessage("Remove this tag?")
			.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					mDB.removeTagById(id);
					mAdapter.changeCursor(mDB.getCursorOfTagsSimple());
				}
			})
			.setNegativeButton("No", null)
			.show();
	}
	

	private void onShowHideTagInWelcome(final long id) {
		new AlertDialog.Builder(this)
			.setMessage("Show/Hide this tag in welcome screen? ")
			.setPositiveButton("Show", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					mDB.addToWelcomeTag(id);
				}
			})
			.setNegativeButton("Hide", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					mDB.removeFromWelcomeTag(id);
				}
			})
			.show();
	}
	
	private class TagManageAdapter extends CursorAdapter {

		public TagManageAdapter(Cursor c) {
			super(getBaseContext(), c);
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			//StringBuilder sb = new StringBuilder();
			//sb.append("Tag:").append(cursor.getString(1));
			TextView tv = (TextView)view.findViewById(android.R.id.text1);
			tv.setText(cursor.getString(1));
		}

		@Override
		public View newView(Context context, Cursor cursor,
				ViewGroup parent) {
			View v = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, null);
			bindView(v, context, cursor);
			return v;
		}
		
	}

}
