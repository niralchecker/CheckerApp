package com.checker.sa.android.helper;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.RatingBar;

import com.mor.sa.android.activities.R;

public class CustomRBar extends RatingBar {

	private Context context;

	public CustomRBar(Context context, AttributeSet attrs, int defStyle) {

		super(context, attrs, defStyle);
		this.context = context;
	}

	public CustomRBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public CustomRBar(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction() & MotionEvent.ACTION_MASK;
		int stars = getNumStars();
		// Log.d("CV", "Action ["+action+"]");
		switch (action) {
		case MotionEvent.ACTION_DOWN: {
			break;
		}
		case MotionEvent.ACTION_MOVE: {
			break;
		}

		}
		float x = event.getX();
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		int screenWidth = display.getWidth(); // deprecated
		if (display.getWidth() > display.getHeight()) {
			screenWidth = display.getHeight();
		}
		screenWidth -= 20;
		if (stars < 6)
			stars = 6;
		int star = (int) ((x / screenWidth) * stars);
		if (star > getNumStars())
			star = getNumStars();
		setRating(star + 1);
		invalidate();
		return true;
	}

	@Override
	protected synchronized void onDraw(Canvas canvas) {
		int stars = getNumStars();
		float rating = getRating();
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		int screenWidth = display.getWidth(); // deprecated
		int screenHeight = display.getHeight(); // deprecated
		if (display.getWidth() > display.getHeight()) {
			screenWidth = display.getHeight();
		}
		screenWidth -= 20;
		if (stars < 6)
			stars = 6;
		float newWidth = (float) screenWidth / stars;
		float newHeight = newWidth;

		for (int i = 0; i < getNumStars(); i++) {
			Bitmap bitmap;
			Resources res = getResources();
			Paint paint = new Paint();

			if ((int) rating > i) {
				bitmap = BitmapFactory.decodeResource(res,
						R.drawable.star_filled);
			} else {
				bitmap = BitmapFactory.decodeResource(res,
						R.drawable.star_n_filled);
			}
			bitmap = resizeBitmap(bitmap, newWidth, newHeight);
			canvas.drawBitmap(bitmap, i * newWidth, 0, paint);
			canvas.save();
		}

		// super.onDraw(canvas);
	}

	private Bitmap resizeBitmap(Bitmap bm, float newWidth, float newHeight) {

		int width = bm.getWidth();
		int height = bm.getHeight();

		float scaleWidth = ((float) newWidth) / width;

		float scaleHeight = ((float) newHeight) / height;

		// CREATE A MATRIX FOR THE MANIPULATION
		Matrix matrix = new Matrix();
		// RESIZE THE BIT MAP
		matrix.postScale(scaleWidth, scaleHeight);

		// "RECREATE" THE NEW BITMAP
		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
				matrix, false);
		bm.recycle();
		return resizedBitmap;
	}
}