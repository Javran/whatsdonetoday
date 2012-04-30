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
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateTime implements Comparable<Object>, Serializable {
	private static final long serialVersionUID = 6420337600553520318L;
	
	// field "second" is not used
	
	public int year;
	public int month;
	public int day;
	
	public int hour;
	public int minute;
	public int second;
	
	private final static Pattern pattern = 
		Pattern.compile("(\\d{4})-(\\d{2})-(\\d{2}) (\\d{2}):(\\d{2}):(\\d{2})");
		//               ^1       ^2       ^3       ^4       ^5       ^6     
	public DateTime(int year, int month, int day, 
			int hour, int minute, int second) {
		this.year = year;
		this.month = month;
		this.day = day;
		this.hour = hour;
		this.minute = minute;
		this.second = 0;
	}
	
	public static DateTime fromString(String rawString) {
		Matcher m = pattern.matcher(rawString);
		if (!m.matches())
			return null;
		int year = 	Integer.valueOf(m.group(1));
		int month = 	Integer.valueOf(m.group(2));
		int day = 	Integer.valueOf(m.group(3));
		int hour = 	Integer.valueOf(m.group(4));
		int minute = 	Integer.valueOf(m.group(5));
		//int second =	Integer.valueOf(m.group(6));
		return new DateTime(year, month, day, hour, minute, 0);
	}
	
	public static DateTime getCurrent() {
		
		GregorianCalendar c = new GregorianCalendar();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int day = c.get(Calendar.DATE);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		return new DateTime(year, month, day, hour, minute, 0);
	}

	@Override
	public String toString() {
		return String.format("%04d-%02d-%02d %02d:%02d:%02d",
				year, month, day,
				hour, minute, 0);
	}
	
	public String toStringUI() {
		return String.format("%04d-%02d-%02d %02d:%02d",
				year, month, day,
				hour, minute);
	}
	
	public String getDateString() {
		return String.format("%04d-%02d-%02d", year, month, day);
	}
	
	@Override
	public boolean equals(Object o) {
		return toString().equals(o.toString());
	}

	@Override
	public int compareTo(Object another) {
		return toString().compareTo(another.toString()); 
	}

}
