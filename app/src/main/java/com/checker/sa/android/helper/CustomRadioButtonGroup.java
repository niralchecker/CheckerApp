package com.checker.sa.android.helper;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.checker.sa.android.data.Answers;
import com.checker.sa.android.data.dataQuestionGroup;
import com.mor.sa.android.activities.QuestionnaireActivity;
import com.mor.sa.android.activities.R;

public class CustomRadioButtonGroup {

	private Context con;
	private QuestionnaireActivity act;
	ArrayList<CustomRadioCheckButtonClass> listRadio;
	private int color_code;
	private dataQuestionGroup thisQGroup;

	public CustomRadioButtonGroup(Context con) {
		this.con = con;
		listRadio = new ArrayList<CustomRadioCheckButtonClass>();
	}

	private void turnAllOff() {
		for (int i = 0; i < listRadio.size(); i++) {
			listRadio.get(i).radioIem.setTag("false");
			if (Helper.getTheme(con) == 0) {
				listRadio.get(i).radioIem
						.setBackgroundResource(R.drawable.gray_n);
			} else {
				listRadio.get(i).radioIem
						.setBackgroundResource(R.drawable.gray);
			}
			// listRadio.get(i).radioIem.setBackgroundResource(R.drawable.gray);
		}
	}

	public void ImageClicked(View rd) {

		{

			ColorDrawable[] color = {
					new ColorDrawable(Color.parseColor("#4863A0")),
					new ColorDrawable(color_code) };
			TransitionDrawable trans = new TransitionDrawable(color);
			// This will work also on old devices. The latest
			// API says you have to use setBackground instead.
			((View) rd.getParent()).setBackgroundDrawable(trans);
			trans.startTransition(500);

			turnAllOff();
			rd.setTag("true");
			if (Helper.getTheme(con) == 0) {
				rd.setBackgroundResource(R.drawable.grayon_n);
			} else {
				rd.setBackgroundResource(R.drawable.grayon);
			}

			for (int i = 0; i < listRadio.size(); i++) {
				if (listRadio.get(i).radioIem.equals((ImageView) rd)) {
					QuestionnaireActivity qact = (QuestionnaireActivity) con;
					if (thisQGroup == null
							&& qact.validateadditionjumpapramsRadioButton(i))
						qact.goNextAfterDelay();
					else if (thisQGroup != null
							&& (thisQGroup.getWholeMiView() != null
									|| thisQGroup.getbTextbox() != null || thisQGroup
									.getTextbox() != null)
							&& thisQGroup.getSelectedAnswer(i) != null
							&& (thisQGroup.getSelectedAnswer(i)
									.getHideAdditionalInfo() != null && thisQGroup
									.getSelectedAnswer(i)
									.getHideAdditionalInfo().equals("1"))) {
						// if (thisQGroup.getbTextbox() != null) {
						// thisQGroup.getbTextbox().setVisibility(
						// RelativeLayout.GONE);
						// } else if (thisQGroup.getTextbox() != null) {
						// thisQGroup.getTextbox().setVisibility(
						// RelativeLayout.GONE);
						// }

						if (thisQGroup.getWholeMiView() != null) {
							thisQGroup.getWholeMiView().setVisibility(
									RelativeLayout.GONE);
							if (thisQGroup.getMiTextView() != null) {
								thisQGroup.getMiTextView().setVisibility(
										RelativeLayout.GONE);
							}
						} else if (thisQGroup.getbTextbox() != null) {
							thisQGroup.getbTextbox().setVisibility(
									RelativeLayout.GONE);
							if (thisQGroup.getMiTextView() != null) {
								thisQGroup.getMiTextView().setVisibility(
										RelativeLayout.GONE);
							}
						} else if (thisQGroup.getTextbox() != null) {
							thisQGroup.getTextbox().setVisibility(
									RelativeLayout.GONE);
							if (thisQGroup.getMiTextView() != null) {
								thisQGroup.getMiTextView().setVisibility(
										RelativeLayout.GONE);
							}
						}
					} else if (thisQGroup != null
							&& (thisQGroup.getWholeMiView() != null
									|| thisQGroup.getbTextbox() != null || thisQGroup
									.getTextbox() != null)
							&& thisQGroup.getSelectedAnswer(i) != null) {
						// if (thisQGroup.getbTextbox() != null) {
						// thisQGroup.getbTextbox().setVisibility(
						// RelativeLayout.VISIBLE);
						// } else if (thisQGroup.getTextbox() != null) {
						// thisQGroup.getTextbox().setVisibility(
						// RelativeLayout.VISIBLE);
						// }

						if (thisQGroup.getWholeMiView() != null) {
							thisQGroup.getWholeMiView().setVisibility(
									RelativeLayout.VISIBLE);
							if (thisQGroup.getMiTextView() != null) {
								thisQGroup.getMiTextView().setVisibility(
										RelativeLayout.VISIBLE);
							}
						} else if (thisQGroup.getbTextbox() != null) {
							thisQGroup.getbTextbox().setVisibility(
									RelativeLayout.VISIBLE);
							if (thisQGroup.getMiTextView() != null) {
								thisQGroup.getMiTextView().setVisibility(
										RelativeLayout.VISIBLE);
							}
						} else if (thisQGroup.getTextbox() != null) {
							thisQGroup.getTextbox().setVisibility(
									RelativeLayout.VISIBLE);
							if (thisQGroup.getMiTextView() != null) {
								thisQGroup.getMiTextView().setVisibility(
										RelativeLayout.VISIBLE);
							}
						}
					}

				}
			}
		}
	}

	private ImageView getRadioButtonImageViewClickAble(boolean isChecked) {
		final ImageView rd = new ImageView(con);
		setChecked(rd, isChecked);
		rd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				ImageClicked(arg0);
			}
		});

		// rd.setLayoutParams(new LinearLayout.LayoutParams(width, height,
		// gravity));
		return rd;
	}
	
	private ImageView getRadioButtonImageViewClickAble(boolean isChecked,ImageView rd) {
		//final ImageView rd = new ImageView(con);
		setChecked(rd, isChecked);
		rd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				ImageClicked(arg0);
			}
		});

		// rd.setLayoutParams(new LinearLayout.LayoutParams(width, height,
		// gravity));
		return rd;
	}

	public ImageView addRadioButton(boolean isChecked, Answers answers,
			int color, dataQuestionGroup thisQGroup) {
		ImageView img = getRadioButtonImageViewClickAble(isChecked);
		this.color_code = color;
		this.thisQGroup = thisQGroup;
		listRadio.add(new CustomRadioCheckButtonClass(img, answers));
		return img;
	}
	
	
	public ImageView addRadioButton(boolean isChecked, Answers answers,
			int color, dataQuestionGroup thisQGroup,ImageView rd) {
		ImageView img = getRadioButtonImageViewClickAble(isChecked,rd);
		this.color_code = color;
		this.thisQGroup = thisQGroup;
		listRadio.add(new CustomRadioCheckButtonClass(img, answers));
		return img;
	}

	public void setChecked(final ImageView rd, boolean isChecked) {

		if (isChecked == true) {
			rd.setTag("true");
			if (Helper.getTheme(con) == 0) {
				rd.setBackgroundResource(R.drawable.grayon_n);
			} else {
				rd.setBackgroundResource(R.drawable.grayon);
			}
		} else {
			rd.setTag("false");
			if (Helper.getTheme(con) == 0) {
				rd.setBackgroundResource(R.drawable.gray_n);
			} else {
				rd.setBackgroundResource(R.drawable.gray);
			}
		}
	}

	public int getSelectedItemPosition() {
		for (int i = 0; i < listRadio.size(); i++) {
			if (listRadio.get(i).radioIem.getTag().equals("true"))
				return i;
		}
		return -1;
	}

	public void setSelectedItemPosition(int input) {
		for (int i = 0; i < listRadio.size(); i++) {
			if (i == input) {
				setChecked(listRadio.get(i).radioIem, true);
			}
		}
	}
}
