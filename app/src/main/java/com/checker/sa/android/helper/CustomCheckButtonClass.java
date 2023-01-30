package com.checker.sa.android.helper;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.checker.sa.android.data.Answers;
import com.mor.sa.android.activities.R;

public class CustomCheckButtonClass {

	public Answers answers;
	public ImageView radioIem;
	public String tag;

	public CustomCheckButtonClass(ImageView img, Answers answers2, String tag) {
		radioIem = img;
		// radioIem.setPadding(0, 40, 0, 40);
		answers = answers2;
		this.tag = tag;

	}

	public boolean compare(Answers answerin) {
		if (answers != null && answerin != null
				&& answers.getAnswerID() != null
				&& answerin.getAnswerID() != null
				&& answers.getAnswerID().equals(answerin.getAnswerID())) {
			return true;
		} else
			return false;
	}
}
