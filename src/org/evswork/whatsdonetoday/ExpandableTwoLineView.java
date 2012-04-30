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
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ExpandableTwoLineView extends LinearLayout {
	
	private TextView text1, text2;
	private LinearLayout layout;
	
	
	public ExpandableTwoLineView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public ExpandableTwoLineView(Context context) {
		super(context);
		initView();
	}
	
	private void initView() 
	{
		LayoutInflater inflater = LayoutInflater.from(getContext());
		layout = (LinearLayout)inflater.inflate(R.layout.expand_two_line_view, this);
		text1 = (TextView)layout.findViewById(R.id.text1);
		text2 = (TextView)layout.findViewById(R.id.text2);
	}
	
	public void setLargeLine(String text) {
		text1.setText(text);
	}
	
	public void setSmallLine(String text) {
		text2.setText(text);
	}
}
