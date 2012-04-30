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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.preference.DialogPreference;
import android.util.AttributeSet;

public class TagSelectListPreference extends DialogPreference {
	public Tag[] tags;
	public List<Tag> chosenTags;
	private boolean [] tagSelections;
	
	public TagSelectListPreference(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		initPreference();
	}

	public TagSelectListPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		initPreference();
	}
	
	@Override
	protected void onPrepareDialogBuilder(Builder builder) {
		super.onPrepareDialogBuilder(builder);
		
		builder.setTitle("Choose Tags");
		// build tag selections
		if (tags != null ) {
			Arrays.sort(tags);
			tagSelections = new boolean[tags.length];
			if (chosenTags != null) {
				for (int i=0; i<tagSelections.length; ++i)
					tagSelections[i] = chosenTags.contains(tags[i]);
			}
			builder.setMultiChoiceItems(tags, tagSelections, new OnMultiChoiceClickListener() {

				@Override
				public void onClick(DialogInterface dialog,
						int which, boolean isChecked) {
					tagSelections[which] = isChecked;
				}
			});
		}
	}

	@Override
	protected void onDialogClosed(boolean positiveResult) {
		if (positiveResult) {
			chosenTags.clear();
			for (int i=0;i<tags.length; ++i)
				if (tagSelections[i])
					chosenTags.add(tags[i]);
			Collections.sort(chosenTags);
			updateUI();
		}
		super.onDialogClosed(positiveResult);
	}

	private void initPreference() {
		
	}
	
	public void updateUI() {
		StringBuilder sb = new StringBuilder();
		if (chosenTags.size() > 0) {
			sb.append(chosenTags.get(0));
			for (int i=1; i<chosenTags.size(); ++i)
				sb.append(", ").append(chosenTags.get(i));
		}
		setSummary(sb.toString());
	}

}
