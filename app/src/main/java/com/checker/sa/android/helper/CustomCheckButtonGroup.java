package com.checker.sa.android.helper;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.checker.sa.android.data.Answers;
import com.checker.sa.android.data.dataQuestionGroup;
import com.mor.sa.android.activities.QuestionnaireActivity;
import com.mor.sa.android.activities.R;

public class CustomCheckButtonGroup {

	private Context con;
	public ArrayList<CustomCheckButtonClass> listRadio;
	private int color_code;
	private dataQuestionGroup thisQGroup;

	public CustomCheckButtonGroup(Context con) {
		this.con = con;
		listRadio = new ArrayList<CustomCheckButtonClass>();
	}

	private void turnAllOff() {
		for (int i = 0; i < listRadio.size(); i++) {
			Answers a=(Answers)listRadio.get(i).radioIem.getTag();
			a.setIsChecked("false");
			//listRadio.get(i).radioIem.setTag("false");
			//listRadio.get(i).radioIem.setBackgroundResource(R.drawable.gray);
			if (Helper.getTheme(con) == 0) {
				listRadio.get(i).radioIem.setBackgroundResource(R.drawable.unchecked_checkbox_n);
			} else {
				listRadio.get(i).radioIem.setBackgroundResource(R.drawable.unchecked_checkbox);
			}
		}
	}

	private ImageView getRadioButtonImageViewClickAble(boolean isChecked, Answers answers) {
		ImageView rd = new ImageView(con);
		rd.setTag(answers);
		setChecked(rd, isChecked);
		rd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ImageView rd = (ImageView) (arg0);
				Answers a=(Answers)rd.getTag();
				if (a.getIsChecked().equals("true")) {
					ColorDrawable[] color = {
							new ColorDrawable(Color.parseColor("#94BA09")),
							new ColorDrawable(color_code) };
					TransitionDrawable trans = new TransitionDrawable(color);
					// This will work also on old devices. The latest
					// API says you have to use setBackground instead.
					((View) arg0.getParent()).setBackgroundDrawable(trans);
					trans.startTransition(1000);
					a.setIsChecked("false");
					//.setTag("false");
					if (Helper.getTheme(con) == 0) {
						rd.setBackgroundResource(R.drawable.unchecked_checkbox_n);
					} else {
						rd.setBackgroundResource(R.drawable.unchecked_checkbox);
					}

				} else {
					// turnAllOff();
					boolean isClearOtherAnswerTicked=alreadyTunedOff();

					if (a.getClearOtherAnswers()!=null && a.getClearOtherAnswers()
							.equals("1"))
					{
						turnAllOff();
					}
					ColorDrawable[] color = {
							new ColorDrawable(Color.parseColor("#94BA09")),
							new ColorDrawable(color_code) };
					TransitionDrawable trans = new TransitionDrawable(color);
					// This will work also on old devices. The latest
					// API says you have to use setBackground instead.
					((View) arg0.getParent()).setBackgroundDrawable(trans);
					trans.startTransition(1000);
					if (isClearOtherAnswerTicked==true) return;
					a.setIsChecked("true");
					if (Helper.getTheme(con) == 0) {
						rd.setBackgroundResource(R.drawable.checked_checkbox_n);
					} else {
						rd.setBackgroundResource(R.drawable.checked_checkbox);
					}

				}
				//fro groups
				if (thisQGroup!=null
						&& (thisQGroup.getCustomCheckboxgrp()!=null
						|| thisQGroup.getMultiSpinner()!=null))
				{
					UIQuestionGroupHelper.hideShowMiInCheckBox(thisQGroup.getListAnswers(),thisQGroup.getMultiSpinner(),thisQGroup.getCustomCheckboxgrp(),thisQGroup);
				}
			}
		});

		// rd.setLayoutParams(new LinearLayout.LayoutParams(width, height,
		// gravity));
		return rd;
	}

	private boolean alreadyTunedOff() {
		boolean isTickked=false;
		for (int i = 0; i < listRadio.size(); i++) {
			Answers a=(Answers)listRadio.get(i).radioIem.getTag();
			if (a.getClearOtherAnswers()!=null && a.getClearOtherAnswers().equals("1")
					&& a.getIsChecked()!=null && a.getIsChecked().equals("true"))
			{
				isTickked=true;
				break;
			}
		}
		return isTickked;
	}

	public CustomCheckButtonClass addRadioButton(boolean isChecked,
												 Answers answers, String tag, int color,dataQuestionGroup thisQGroup) {
		this.color_code = color;
		ImageView img = getRadioButtonImageViewClickAble(isChecked,answers);
		CustomCheckButtonClass CustomCheckButton = new CustomCheckButtonClass(
				img, answers, tag);
		listRadio.add(CustomCheckButton);
		this.thisQGroup=thisQGroup;
		return CustomCheckButton;
	}

	public void setChecked(final ImageView rd, boolean isChecked) {
		Answers a=(Answers)rd.getTag();
		if (isChecked == true) {
			a.setIsChecked("true");
			//rd.setTag("true");
			if (Helper.getTheme(con) == 0) {
				rd.setBackgroundResource(R.drawable.checked_checkbox_n);
			} else {
				rd.setBackgroundResource(R.drawable.checked_checkbox);
			}
		} else {
			a.setIsChecked("false");
			//rd.setTag("false");
			if (Helper.getTheme(con) == 0) {
				rd.setBackgroundResource(R.drawable.unchecked_checkbox_n);
			} else {
				rd.setBackgroundResource(R.drawable.unchecked_checkbox);
			}
		}
	}

	public int getSelectedItemPositions() {
		for (int i = 0; i < listRadio.size(); i++) {
			Answers a=(Answers)listRadio.get(i).radioIem.getTag();
			if (a.getIsChecked().equals("true"))
				return i;
		}
		return -1;
	}

	public boolean isAnySelected() {
		for (int i = 0; i < listRadio.size(); i++) {
			Answers a=(Answers)listRadio.get(i).radioIem.getTag();
			if (a.getIsChecked().equals("true"))
				return true;
		}

		return false;
	}
	
	public List<Integer> answersSelected() {
		List<Integer> ints=new ArrayList<Integer>();
		for (int i = 0; i < listRadio.size(); i++) {
			Answers a=(Answers)listRadio.get(i).radioIem.getTag();
			if (a.getIsChecked().equals("true"))
				ints.add(i);
		}

		return ints;
	}

	public int getChildCount() {
		return listRadio.size();
	}

	public boolean getChildChecked(int i) {
		Answers a=(Answers)listRadio.get(i).radioIem.getTag();
		if (a.getIsChecked().equals("true"))
			return true;
		return false;
	}

	public String getChildTagAt(int i) {
		Answers a=(Answers)listRadio.get(i).radioIem.getTag();

		return listRadio.get(i).tag;
	}
}
