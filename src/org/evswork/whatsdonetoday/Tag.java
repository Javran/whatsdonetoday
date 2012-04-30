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

import java.io.Serializable;

import android.database.Cursor;

public class Tag implements Comparable<Object>, CharSequence, Serializable {
	private static final long serialVersionUID = -2357853864031331520L;
	public final long id;
	public String title;
	public final static Tag NO_TAG = new Tag(2147483647, "No Tag");
	
	Tag(long id, String title) {
		this.id = id;
		this.title = title;
	}
	
	Tag(Cursor cursor) {
		this.id = cursor.getLong(cursor.getColumnIndex("_id"));
		this.title = cursor.getString(cursor.getColumnIndex("title"));
	}

	@Override
	public boolean equals(Object o) {
		Tag t = (Tag)o;
		return t != null && id == t.id;
	}

	@Override
	public int compareTo(Object another) {
		Tag t = (Tag)another;
		if ( t == null ) 
			return 1;
		if ( id == t.id )
			return 0;
		return id < t.id ? -1 : 1;
	}

	@Override
	public char charAt(int index) {
		return title.charAt(index);
	}

	@Override
	public int length() {
		return title.length();
	}

	@Override
	public CharSequence subSequence(int start, int end) {
		return title.subSequence(start, end);
	}

	@Override
	public String toString() {
		return title;
	}
}
