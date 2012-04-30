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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.database.Cursor;

public class Event implements Serializable {
	
	private static final long serialVersionUID = 3040539043334563651L;
	public final static long FAKE_ID = 2147483647;

	public final long eventId;
	public DateTime dateTime;
	public int worth = 0;
	public List<Tag> tags;
	public String description = "";
	
	public Event(long eventID) {
		this.eventId = eventID;	
	}
	
	public Event(Cursor eCursor, Cursor tCursor) {
		// columns available for eCursor:
		// _id, description, time, worth
		eventId = eCursor.getLong(eCursor.getColumnIndex("_id"));
		description = eCursor.getString(eCursor.getColumnIndex("description"));
		dateTime = DateTime.fromString(eCursor.getString(eCursor.getColumnIndex("time")));
		worth = eCursor.getInt(eCursor.getColumnIndex("worth"));
		
		tags = new ArrayList<Tag>();
		if (tCursor.moveToFirst()) {
			do {
				tags.add(new Tag(tCursor));
			} while (tCursor.moveToNext());
		}
		Collections.sort(tags);
	}

	public boolean equalInContent(Event another) {
		return another == null ? false :
			( 
				dateTime.equals(another.dateTime) && 
				worth == another.worth &&
				description.equals(another.description) &&
				tags.equals(another.tags)
			);
	}

}
