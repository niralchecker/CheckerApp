package com.checker.sa.android.helper;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

public class ZoomableScrollView extends HorizontalScrollView {
	float mScaleFactor = 1f;
	float mPivotX;
	float mPivotY;

	public ZoomableScrollView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public ZoomableScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public ZoomableScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	protected void dispatchDraw(Canvas canvas) {
		canvas.save();
		canvas.scale(mScaleFactor, mScaleFactor, mPivotX, mPivotY);
		super.dispatchDraw(canvas);
		canvas.restore();
	}

	public void scale(float scaleFactor, float pivotX, float pivotY) {
		if (scaleFactor < 0.5)
			scaleFactor = 0.5f;
		if (scaleFactor > 1.5)
			scaleFactor = 1.5f;
		mScaleFactor = 1f;
		mPivotX = pivotX;
		mPivotY = pivotY;
		this.invalidate();
	}

	public void restore() {
		mScaleFactor = 1;
		this.invalidate();
	}

}