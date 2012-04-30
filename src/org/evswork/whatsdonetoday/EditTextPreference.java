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
import android.view.View;

public class EditTextPreference extends android.preference.EditTextPreference {
	public String content;
	
	public EditTextPreference(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		initPreference();
	}

	public EditTextPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		initPreference();

	}

	public EditTextPreference(Context context) {
		super(context);
		initPreference();
	}
	
	private void initPreference() {
		setPersistent(false);
		updateUI();
	}
	
	@Override
	protected void onDialogClosed(boolean positiveResult) {
		if (positiveResult) {
			content = getEditText().getText().toString();
			updateUI();
		}
		super.onDialogClosed(positiveResult);
	}
	
	@Override
	protected void onBindDialogView(View view) {
		super.onBindDialogView(view);
		getEditText().setText("");
		getEditText().append(content);
		
		
	}

	public void updateUI() {
		setText(content);
		setSummary(content);
	}

}
