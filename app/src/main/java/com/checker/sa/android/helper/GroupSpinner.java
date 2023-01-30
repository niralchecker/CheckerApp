package com.checker.sa.android.helper;

import com.checker.sa.android.data.dataQuestionGroup;
import com.mor.sa.android.activities.R;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Spinner;

/**
 * Created by lgp on 2015/10/5.
 */
public class GroupSpinner extends Spinner {

	private dataQuestionGroup thisQgroup;

	public GroupSpinner(Context context) {
		super(context);

	}

	public dataQuestionGroup getThisQgroup() {
		return thisQgroup;
	}

	public void setThisQgroup(dataQuestionGroup thisQgroup) {
		this.thisQgroup = thisQgroup;
	}

}
