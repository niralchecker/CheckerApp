package com.checker.sa.android.helper;

import java.io.ByteArrayInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.TimePicker;
import android.widget.Toast;

import com.checker.sa.android.adapter.selectedFilesAdapter;
import com.checker.sa.android.data.Answers;
import com.checker.sa.android.data.Branches;
import com.checker.sa.android.data.Objects;
import com.checker.sa.android.data.Order;
import com.checker.sa.android.data.Orders;
import com.checker.sa.android.data.QuestionnaireData;
import com.checker.sa.android.data.Set;
import com.checker.sa.android.data.Titles;
import com.checker.sa.android.data.Workers;
import com.checker.sa.android.data.dataQuestionGroup;
import com.checker.sa.android.db.DBAdapter;
import com.checker.sa.android.db.DBHelper;
import com.ikovac.timepickerwithseconds.view.MyTimePickerDialog;
import com.mor.sa.android.activities.QuestionnaireActivity;
import com.mor.sa.android.activities.R;

import de.congrace.exp4j.Calculable;
import de.congrace.exp4j.ExpressionBuilder;
import de.congrace.exp4j.UnknownFunctionException;
import de.congrace.exp4j.UnparsableExpressionException;

public class UIQuestionGroupHelper {

	private static UIQuestionGroupHelper uqhelper;

	public static UIQuestionGroupHelper getinstance() {
		if (uqhelper == null)
			uqhelper = new UIQuestionGroupHelper();

		return uqhelper;
	}

	ZoomableScrollView hr;
	EditText firstEditText = null;
	dataQuestionGroup thisQGroup;
	EditText workerEditText = null;
	Spinner workerDropDown = null;
	ListView workerListView = null;
	private boolean spinnerFirstTime;
	private Object enableValidationQuestion;
	private boolean show_attachedfilescreen;
	public int viewId;

	int miLabelViewId, miEditViewId;
	private int tempId;
	private boolean isKeyboardOn;
	private boolean isNextObj;
	private int isHideAdditionalInfoSingleChoiceAnswers;
	private int isMandatorySingleChoiceAnswers;
	private boolean isSplit;
	Display display;
	private boolean showSubmitMenu;
	ListView branchListView = null;
	ListView productListView = null;
	ListView locationListView = null;
	Objects questionObject = null;
	Set set;
	int objectCount, ansCount;
	Order order;
	Helper helper;
	private ArrayList<QuestionnaireData> questionnaireData;

	String DataID = "";
	public Context con;
	private boolean IsMandatory;
	// int questionNumber;
	private int modeSelect;
	private boolean IsAttachement;
	// private ArrayList<Answers> listAnswers;
	private LinearLayout checkboxgrp;
	private RadioGroup radioGroup;
	private RelativeLayout questionnaireLayoutt;
	private boolean IsMiMandatory;
	private EditText textbox;
	private EditText txtCurrent;
	private ImageView workerBranchImage;
	private ArrayList<Branches> listBranches;
	private ArrayList<Workers> listWorkers;
	private String companyLink;
	private EditText branchEditText;

	ArrayList<View> currentViews = null;

	private RelativeLayout.LayoutParams getIconParam() {
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		// lp.addRule(setAlignmentOfView(Helper.getInt(questionObject.getAlign())));
		lp.setMargins(5, 0, 0, 5);
		lp.addRule(RelativeLayout.BELOW, viewId - 1);
		lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		lp.addRule(RelativeLayout.CENTER_VERTICAL);
		lp.addRule(RelativeLayout.ALIGN_RIGHT);
		return lp;
	}

	private RadioButton getTextFromHtmlFormate(String html, RadioButton tv) {
		Spanned sp = Helper.makeHtmlString(html);
		tv.setText(sp, BufferType.SPANNABLE);
		return tv;
	}

	private RelativeLayout.LayoutParams getElseLayoutParam() {
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				display.getWidth(),
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		lp.addRule(setAlignmentOfView(Helper.getInt(questionObject.getAlign())));
		lp.setMargins(0, 5, 0, 5);
		if (viewId != 101) {
			// lp.addRule(RelativeLayout.BELOW, tempId);
			lp.addRule(RelativeLayout.BELOW, viewId - 1);
		}
		lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		lp.addRule(RelativeLayout.ALIGN_RIGHT);
		return lp;
	}

	private RadioButton setRadioButtonTextViewProperties(RadioButton rb,
			String text, String color, String italic, String bold,
			String underline) {
		rb = getTextFromHtmlFormate(Helper.getValidString(text), rb);
		rb = getTextFromHtmlFormate(rb.getText().toString(), rb);
		// rb.setText(Helper.getValidString(text));
		rb.setTextColor(Helper.getIntColor(color));
		if (Helper.comapreString(italic, "1"))
			rb.setTypeface(null, Typeface.ITALIC);
		if (Helper.comapreString(bold, "1"))
			rb.setTypeface(null, Typeface.BOLD);
		if (Helper.comapreString(underline, "1"))
			rb = (RadioButton) setUnderLineText(text, rb);
		setFontSize(rb);
		return rb;
	}

	private TextView getTextFromHtmlFormate(String html, TextView tv) {
		Spanned sp = Helper.makeHtmlString(html);
		tv.setText(sp, BufferType.SPANNABLE);
		return tv;
	}

	private QuestionnaireData getSingleAnswer(String dataID) {
		if (questionnaireData == null)
			return null;
		for (int qCount = 0; qCount < questionnaireData.size(); qCount++) {
			QuestionnaireData qd = questionnaireData.get(qCount);
			if (qd.getDataID().equals(dataID))
				return qd;
		}
		return null;
	}

	private boolean isSelectedAnswers(QuestionnaireData qd, String ansID) {
		if (questionnaireData == null || qd == null)
			return false;
		for (int qCount = 0; qCount < qd.getAnswersList().size(); qCount++) {
			Answers ans = qd.getAnswersList().get(qCount);
			if (ans.getAnswerID() != null && ans.getAnswerID().equals(ansID))
				return true;
		}
		return false;
	}

	private int getWidth(Answers answers, int displayType, int qtl) {
		Boolean isCheckbox = false;
		switch (displayType) {
		case 0:

			if (qtl == 8 || qtl == 9)
				isCheckbox = true;
			isCheckbox = false;
		case 1:
		case 5:
			// return getDropDownListView(listAns, null, null, isPrev);
			// return getDropDownView(listAns, null, null, isPrev);
		case 2:
			// return getSelectionList();
		case 3:
			// return getTextView();
		case 4:
			isCheckbox = true;
		}
		if (isCheckbox) {
			CheckBox cb = new CheckBox(con);
			if (answers.getIconName() != null) {
				SharedPreferences myPrefs = con.getSharedPreferences("pref",
						con.MODE_PRIVATE);
				boolean isDownloadedYet = myPrefs.getBoolean(
						Constants.IS_DOWNLOADED_FOR_NEW_DOWNLOAD_PATH, false);
				Drawable d = new BitmapDrawable(con.getResources(),
						helper.readFile(answers.getIconName(), isDownloadedYet));
				cb.setCompoundDrawablesWithIntrinsicBounds(null, null, d, null);
			} else {
				cb.setText(Helper.makeHtmlString(ConvertTextCodetoText(answers
						.getAnswer())));
			}
			cb.setTextColor(Helper.getIntColor(answers.getColor()));
			// checkboxgrp.addView(cb, lp);
			setFontSize(cb);
			RelativeLayout view = new RelativeLayout(con);
			view.addView(cb);

			return view.getLayoutParams().width;
		} else {
			RadioButton cb = new RadioButton(con);
			if (answers.getIconName() != null) {
				SharedPreferences myPrefs = con.getSharedPreferences("pref",
						con.MODE_PRIVATE);
				boolean isDownloadedYet = myPrefs.getBoolean(
						Constants.IS_DOWNLOADED_FOR_NEW_DOWNLOAD_PATH, false);
				Drawable d = new BitmapDrawable(con.getResources(),
						helper.readFile(answers.getIconName(), isDownloadedYet));
				cb.setCompoundDrawablesWithIntrinsicBounds(null, null, d, null);
			} else {
				cb.setText(Helper.makeHtmlString(ConvertTextCodetoText(answers
						.getAnswer())));
			}
			cb.setTextColor(Helper.getIntColor(answers.getColor()));
			// checkboxgrp.addView(cb, lp);
			setFontSize(cb);
			RelativeLayout view = new RelativeLayout(con);
			view.addView(cb);
			return view.getWidth();

		}
	}

	private View writeAnswersHorizontally(int displayType, int qtl,
			ArrayList<Answers> listAnswers) {
		LinearLayout layout = new LinearLayout(con);
		viewId = getViewId(viewId);
		layout.setId(viewId);
		layout.setOrientation(LinearLayout.HORIZONTAL);

		String color1 = "#989898";
		String color2 = "#808080";
		QuestionnaireData qd = getSingleAnswer(questionObject.getDataID());
		for (ansCount = 0; ansCount < listAnswers.size(); ansCount++) {
			Answers answers = listAnswers.get(ansCount);
			if (answers.getAnswerDisplayCondition() != null) {
				Boolean b = IsObjectdisplaybyCondition(
						answers.getAnswerDisplayCondition(), false);
				if (!b)
					continue;
			}

			TextView cb = new TextView(con);

			cb.setId(ansCount + 1);
			cb.setTag(ConvertTextCodetoText(answers.getAnswer()));
			cb.setText(Helper.makeHtmlString(ConvertTextCodetoText(answers
					.getAnswer())));
			cb.setTextColor(con.getResources().getColor(android.R.color.black));
			cb.setPadding(0, 10, 0, 10);
			cb.setTextSize(20);
			cb.setGravity(Gravity.CENTER_HORIZONTAL);
			LinearLayout.LayoutParams lpout = new LinearLayout.LayoutParams(0,
					android.view.ViewGroup.LayoutParams.MATCH_PARENT);
			lpout.gravity = Gravity.CENTER;
			lpout.weight = 1;
			RelativeLayout view = new RelativeLayout(con);
			int color = R.drawable.tbl_border_grey_darker;

			view.setBackgroundDrawable(con.getResources().getDrawable(color));
			LinearLayout.LayoutParams lpoutOne = new LinearLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.MATCH_PARENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			lpoutOne.gravity = Gravity.CENTER;
			if (answers.getIconName() != null) {
				lpoutOne.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;

				view.addView(
						setRadioButtonImageViewProperties(new ImageView(con),
								answers.getIconName()), lpoutOne);
			} else
				view.addView(cb, lpoutOne);

			layout.addView(view, lpout);
		}

		return layout;
	}

	private ImageView setRadioButtonImageViewProperties(ImageView rb,
			String text) {
		SharedPreferences myPrefs = con.getSharedPreferences("pref",
				con.MODE_PRIVATE);
		boolean isDownloadedYet = myPrefs.getBoolean(
				Constants.IS_DOWNLOADED_FOR_NEW_DOWNLOAD_PATH, false);
		Drawable d = new BitmapDrawable(con.getResources(), helper.readFile(
				text, isDownloadedYet));
		rb.setImageDrawable(d);
		// rb.set(Gravity.CENTER_HORIZONTAL);

		return rb;
	}

	public int convertDiptoPix(int dip) {
		// float scale = con.getResources().getDisplayMetrics().density;
		// return (int) ((dip - 0.5f) / scale);
		return dip;
	}

	private View getCheckBoxView(ArrayList<Answers> listAnswers, boolean isPrev) {
		checkboxgrp = new LinearLayout(con);
		thisQGroup.setCheckboxgrp(checkboxgrp);
		viewId = getViewId(viewId);
		checkboxgrp.setId(viewId);
		checkboxgrp.setOrientation(LinearLayout.HORIZONTAL);
		checkboxgrp.setFocusable(true);
		checkboxgrp.setFocusableInTouchMode(true);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		QuestionnaireData qd = getSingleAnswer(questionObject.getDataID());
		for (ansCount = 0; ansCount < listAnswers.size(); ansCount++) {
			Answers answers = listAnswers.get(ansCount);
			if (answers.getAnswerDisplayCondition() != null) {
				Boolean b = IsObjectdisplaybyCondition(
						answers.getAnswerDisplayCondition(), false);
				if (!b)
					continue;
			}
			CheckBox cb = new CheckBox(con);
			cb.requestFocus();
			cb.setId(ansCount + 1);
			cb.setTag(ConvertTextCodetoText(answers.getAnswer()));
			if (answers.getIconName() != null) {
				cb.setText("");
				lp = new LinearLayout.LayoutParams(
						convertDiptoPix(UIHelper.getDisplacementSize(con)),
						android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
				// cb.setPadding(0, 0, convertDiptoPix(160), 0);
			} else {
				lp = new LinearLayout.LayoutParams(
						convertDiptoPix(UIHelper.getDisplacementSize(con)),
						android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
				// cb.setText(ConvertTextCodetoText( answers.getAnswer()));
			}
			cb.setTextColor(con.getResources().getColor(
					android.R.color.transparent));

			cb.setTextSize(UIHelper.getFontSize(con, cb.getTextSize()));

			if (isPrev && isSelectedAnswers(qd, answers.getAnswerID()))
				cb.setChecked(true);
			checkboxgrp.addView(cb, lp);
			// setFontSize(cb);
		}
		return checkboxgrp;
	}

	private RadioButton setRadioButtonImageViewProperties(RadioButton rb,
			String text, String color, String italic, String bold,
			String underline) {
		// rb = getTextFromHtmlFormate(Helper.getValidString(text), rb);
		// rb = getTextFromHtmlFormate(rb.getText().toString(), rb);
		rb.setText("      ");
		rb.setTextColor(Helper.getIntColor(color));
		SharedPreferences myPrefs = con.getSharedPreferences("pref",
				con.MODE_PRIVATE);
		boolean isDownloadedYet = myPrefs.getBoolean(
				Constants.IS_DOWNLOADED_FOR_NEW_DOWNLOAD_PATH, false);
		Drawable d = new BitmapDrawable(con.getResources(), helper.readFile(
				text, isDownloadedYet));
		rb.setCompoundDrawablesWithIntrinsicBounds(null, null, d, null);
		// if (Helper.comapreString(italic, "1"))
		// rb.setTypeface(null, Typeface.ITALIC);
		// if (Helper.comapreString(bold, "1"))
		// rb.setTypeface(null, Typeface.BOLD);
		// if (Helper.comapreString(underline, "1"))
		// rb = (RadioButton) setUnderLineText(text, rb);
		// setFontSize(rb);
		return rb;
	}

	Boolean toggleColor = true;
	private boolean isRadioCheck;
	private double ultimateWidth;
	private MultiSelectionSpinner multiSpinner;

	private View getCheckButtonImageView(ArrayList<Answers> listAnswers,
			boolean isPrev) {
		CustomCheckButtonGroup radioGroup = new CustomCheckButtonGroup(con);
		thisQGroup.setCustomCheckboxgrp(radioGroup);
		LinearLayout layoutRadioGroupOuter = new LinearLayout(con);
		int totalAnswers = 0;
		for (ansCount = 0; ansCount < listAnswers.size(); ansCount++) {
			Answers answers = listAnswers.get(ansCount);
			if (answers.getAnswerDisplayCondition() != null) {
				Boolean b = IsObjectdisplaybyCondition(
						answers.getAnswerDisplayCondition(), false);
				if (!b)
					continue;
			}
			totalAnswers++;
		}
		double width = 0.0;
		if (totalAnswers <= Constants.getAnswerCount(con)) {
			width = display.getWidth() * (sixtyTwo / 100.0);
		} else {
			width = ((display.getWidth() * (sixtyTwo / 100.0) / Constants
					.getAnswerCount(con)) * totalAnswers);
			isRadioCheck = true;
			ultimateWidth = width;
		}
		LinearLayout.LayoutParams lpOut = new LinearLayout.LayoutParams(
				(int) width, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);

		LinearLayout layoutRadioGroup = new LinearLayout(con);
		layoutRadioGroup.setLayoutParams(lpOut);
		// radioGroup = new RadioGroup(con);
		layoutRadioGroup.setOrientation(LinearLayout.HORIZONTAL);

		layoutRadioGroup.setFocusable(true);
		layoutRadioGroup.setFocusableInTouchMode(true);
		// thisQGroup.setRadioGroup(layoutRadioGroup);
		QuestionnaireData qd = null;
		RadioButton rdnotFilled = null;
		Boolean isRdChecked = false;
		if (isPrev)
			qd = getSingleAnswer(questionObject.getDataID());
		for (ansCount = 0; ansCount < listAnswers.size(); ansCount++) {
			Answers answers = listAnswers.get(ansCount);
			if (answers.getAnswerDisplayCondition() != null) {
				Boolean b = IsObjectdisplaybyCondition(
						answers.getAnswerDisplayCondition(), false);
				if (!b)
					continue;
			}
			FrameLayout layoutinner = new FrameLayout(con);

			FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
					Constants.getIconSize(), Constants.getIconSize(),
					Gravity.CENTER_HORIZONTAL);
			// lp.weight = 1;
			lp.setMargins(0, 20, 0, 20);
			lp.gravity = Gravity.CENTER_HORIZONTAL;

			boolean isSelected = false;

			int color2 = Color.parseColor("#E0E0E0");
			int color1 = R.drawable.tbl_border_grey_light;

			if (toggleColor) {
				color2 = Color.parseColor("#C0C0C0");
				color1 = R.drawable.tbl_border_grey_dark;
			}
			if (isPrev && isSelectedAnswers(qd, answers.getAnswerID()))
				isSelected = true;
			CustomCheckButtonClass rd = radioGroup.addRadioButton(isSelected,
					listAnswers.get(ansCount),
					ConvertTextCodetoText(answers.getAnswer()), color2,thisQGroup);

			rd.radioIem.setId(ansCount);
			if (order!=null && order.getIsJobInProgressOnServer()!=null
					&& order.isDataIdEnabled(set,questionObject.getDataID()))
				rd.radioIem.setEnabled(false);
			layoutinner.addView(rd.radioIem, lp);

			// Drawable cd =
			layoutinner.setBackgroundDrawable(con.getResources().getDrawable(
					color1));

			LinearLayout.LayoutParams lptu = new LinearLayout.LayoutParams(0,
					android.view.ViewGroup.LayoutParams.MATCH_PARENT);
			lptu.weight = 1;
			layoutRadioGroup.addView(layoutinner, lptu);
		}
		if (toggleColor == false) {
			toggleColor = true;
		} else {
			toggleColor = false;
		}

		layoutRadioGroup.setTag(questionObject.getDataID());
		layoutRadioGroup.setLayoutParams(lpOut);

		layoutRadioGroupOuter.addView(layoutRadioGroup);

		return layoutRadioGroupOuter;
	}

	private View getRadioButtonImageView(ArrayList<Answers> listAnswers,
			boolean isPrev) {
		CustomRadioButtonGroup radioGroup = new CustomRadioButtonGroup(con);
		thisQGroup.setcustomRadioGroup(radioGroup);
		LinearLayout layoutRadioGroupOuter = new LinearLayout(con);
		int totalAnswers = 0;
		for (ansCount = 0; ansCount < listAnswers.size(); ansCount++) {
			Answers answers = listAnswers.get(ansCount);
			if (answers.getAnswerDisplayCondition() != null) {
				Boolean b = IsObjectdisplaybyCondition(
						answers.getAnswerDisplayCondition(), false);
				if (!b)
					continue;
			}
			totalAnswers++;
		}
		double width = 0.0;

		if (totalAnswers <= Constants.getAnswerCount(con)) {
			width = display.getWidth() * (sixtyTwo / 100.0);
		} else {
			width = ((display.getWidth() * (sixtyTwo / 100.0)) / Constants
					.getAnswerCount(con)) * totalAnswers;
			isRadioCheck = true;
			ultimateWidth = width;
		}
		LinearLayout.LayoutParams lpOut = new LinearLayout.LayoutParams(
				(int) width, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);

		LinearLayout layoutRadioGroup = new LinearLayout(con);
		layoutRadioGroup.setLayoutParams(lpOut);
		// radioGroup = new RadioGroup(con);
		layoutRadioGroup.setOrientation(LinearLayout.HORIZONTAL);

		layoutRadioGroup.setFocusable(true);
		layoutRadioGroup.setFocusableInTouchMode(true);
		// thisQGroup.setRadioGroup(layoutRadioGroup);
		QuestionnaireData qd = null;
		ImageView rdnotFilled = null;
		Boolean isRdChecked = false;
		if (isPrev)
			qd = getSingleAnswer(questionObject.getDataID());
		for (ansCount = 0; ansCount < listAnswers.size(); ansCount++) {
			Answers answers = listAnswers.get(ansCount);
			if (answers.getAnswerDisplayCondition() != null) {
				Boolean b = IsObjectdisplaybyCondition(
						answers.getAnswerDisplayCondition(), false);
				if (!b)
					continue;
			}
			FrameLayout layoutinner = new FrameLayout(con);

			FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
					Constants.getIconSize(), Constants.getIconSize(),
					Gravity.CENTER_HORIZONTAL);
			// lp.weight = 1;
			lp.setMargins(0, 20, 0, 20);
			lp.gravity = Gravity.CENTER_HORIZONTAL;

			int color2 = Color.parseColor("#E0E0E0");
			int color1 = R.drawable.tbl_border_grey_light;

			if (toggleColor) {
				color2 = Color.parseColor("#C0C0C0");
				color1 = R.drawable.tbl_border_grey_dark;
			}
			boolean isSelected = false;
			if (isPrev && isSelectedAnswers(qd, answers.getAnswerID())) {
				isSelected = true;
				thisQGroup.setSelectedAnswer(answers);
			}
			ImageView rd = radioGroup.addRadioButton(isSelected,
					listAnswers.get(ansCount), color2, thisQGroup);
			rd.setId(ansCount);
			if (order!=null && order.getIsJobInProgressOnServer()!=null
					&& order.isDataIdEnabled(set,questionObject.getDataID()))
				rd.setEnabled(false);
			if (answers.getAnswer().toString().toLowerCase()
					.contains("not filled")) {
				rdnotFilled = rd;
			}

			if (isPrev && isSelectedAnswers(qd, answers.getAnswerID())) {
				isRdChecked = true;
				radioGroup.setChecked(rd, true);
			}

			layoutinner.addView(rd, lp);

			// Drawable cd =
			layoutinner.setBackgroundDrawable(con.getResources().getDrawable(
					color1));

			LinearLayout.LayoutParams lptu = new LinearLayout.LayoutParams(0,
					android.view.ViewGroup.LayoutParams.MATCH_PARENT);
			lptu.weight = 1;
			layoutRadioGroup.addView(layoutinner, lptu);
		}
		if (isRdChecked == false) {
			if (rdnotFilled != null)
				radioGroup.setChecked(rdnotFilled, true);
		}
		if (toggleColor == false) {
			toggleColor = true;
		} else {
			toggleColor = false;
		}

		layoutRadioGroup.setTag(questionObject.getDataID());
		layoutRadioGroup.setLayoutParams(lpOut);

		layoutRadioGroupOuter.addView(layoutRadioGroup);

		return layoutRadioGroupOuter;
	}

	private View getRadioButtonView(ArrayList<Answers> listAnswers,
			boolean isPrev) {
		radioGroup = new RadioGroup(con);
		radioGroup.setFocusable(true);
		radioGroup.setFocusableInTouchMode(true);

		radioGroup.setOrientation(LinearLayout.HORIZONTAL);
		thisQGroup.setRadioGroup(radioGroup);
		QuestionnaireData qd = null;
		RadioButton rdnotFilled = null;
		Boolean isRdChecked = false;
		if (isPrev)
			qd = getSingleAnswer(questionObject.getDataID());
		for (ansCount = 0; ansCount < listAnswers.size(); ansCount++) {
			Answers answers = listAnswers.get(ansCount);

			if (answers.getAnswerDisplayCondition() != null) {
				Boolean b = IsObjectdisplaybyCondition(
						answers.getAnswerDisplayCondition(), false);
				if (!b)
					continue;
			}

			// if (ansCount == 0) {
			// if (answers.getAnswer().equalsIgnoreCase("Not filled")
			// || answers.getAnswer().equalsIgnoreCase("Irrelevant")) {
			// // Answers tempanswers =
			// // listAnswers.get(listAnswers.size()-1);
			// // listAnswers.set(0, tempanswers);
			// listAnswers.remove(answers);
			// listAnswers.add(answers);
			// answers = listAnswers.get(ansCount);
			// // tempanswers = null;rad
			// }
			// }

			RadioButton rd = new RadioButton(con);
			boolean myFocus = rd.requestFocus();
			LinearLayout.LayoutParams lp = null;

			rd.setTextColor(con.getResources().getColor(
					android.R.color.transparent));
			rd.setTextSize(UIHelper.getFontSize(con, rd.getTextSize()));
			{
				if (answers.getIconName() != null) {
					rd.setText("");
					lp = new LinearLayout.LayoutParams(
							convertDiptoPix(UIHelper.getDisplacementSize(con)),
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
					// cb.setPadding(0, 0, convertDiptoPix(160), 0);
				} else {
					lp = new LinearLayout.LayoutParams(
							convertDiptoPix(UIHelper.getDisplacementSize(con)),
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
					rd.setText("");
				}

			}
			if (answers.getAnswer().toString().toLowerCase()
					.contains("not filled")) {
				rdnotFilled = rd;
			}

			rd.setId(ansCount);
			if (isPrev) {
				if (qd != null && qd.getAnswersList().size() > 0
						&& answers.getAnswerID() != null
						&& qd.getPoistion() == ansCount) {
					isRdChecked = true;
					rd.setChecked(true);
					try {
						String str = answers.getAdditionalInfoMandatory();
						if (Integer.valueOf(answers
								.getAdditionalInfoMandatory()) > 0) {
							isMandatorySingleChoiceAnswers = 2;
						} else
							isMandatorySingleChoiceAnswers = 0;
						str = answers.getHideAdditionalInfo();
						if (Integer.valueOf(answers.getHideAdditionalInfo()) > 0) {
							isHideAdditionalInfoSingleChoiceAnswers = 2;
						} else
							isHideAdditionalInfoSingleChoiceAnswers = 0;
					} catch (Exception ex) {
						isMandatorySingleChoiceAnswers = 0;
						isHideAdditionalInfoSingleChoiceAnswers = 0;
					}
				}
			}

			// setFontSize(rd);
			radioGroup.addView(rd, lp);
		}
		if (isRdChecked == false) {
			if (rdnotFilled != null)
				rdnotFilled.setChecked(true);
		}
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				// if (true) {
				// View v = questionnaireLayout.findViewById(miLabelViewId);
				// setFontSize(v);
				// if (v != null && v.getClass().equals(TextView.class))
				// v.setVisibility(View.GONE);
				// v = questionnaireLayout.findViewById(miEditViewId);
				// setFontSize(v);
				// if (v != null && v.getClass().equals(EditText.class)) {
				// EditText edit = (EditText) v;
				// edit.setText("");
				// v.setVisibility(View.GONE);
				// }
				// } else {
				// View v = questionnaireLayout.findViewById(miLabelViewId);
				// if (v != null && v.getClass().equals(TextView.class))
				// v.setVisibility(View.VISIBLE);
				// v = questionnaireLayout.findViewById(miEditViewId);
				// if (v != null && v.getClass().equals(EditText.class))
				// v.setVisibility(View.VISIBLE);
				// }
			}
		});
		radioGroup.setTag(questionObject.getDataID());
		return radioGroup;
	}

	private String[] getListItem(ArrayList<Answers> listAnswers,
			ArrayList<Branches> listBranches, ArrayList<Workers> listWorkers) {
		String[] items = null;
		if (listAnswers != null && listAnswers.size() > 0) {
			boolean b = listAnswers
					.get(0)
					.getAnswer()
					.equals(((QuestionnaireActivity) (con)).getPlsSelectValue());
			if (IsMandatory && !b) {
				Answers ans = new Answers();
				ans.setAnswer(((QuestionnaireActivity) (con))
						.getPlsSelectValue());
				ans.setAnswerID("-2");
				listAnswers.add(0, ans);
				ans = null;
			}

			items = new String[listAnswers.size()];
			for (ansCount = 0; ansCount < listAnswers.size(); ansCount++) {
				Answers answers = listAnswers.get(ansCount);
				if (ansCount == 0) {
					if (answers.getAnswer().equalsIgnoreCase("Not filled")) {
						listAnswers.remove(answers);
						listAnswers.add(answers);
						answers = listAnswers.get(ansCount);
					}
				} else {
					if (ansCount == 1 && IsMandatory) {
						if (answers.getAnswer().equalsIgnoreCase("Not filled")) {
							listAnswers.remove(answers);
							listAnswers.add(answers);
							answers = listAnswers.get(ansCount);
						}
					}
				}
				if (answers.getAnswer().contains("<")) {
					items[ansCount] = answers.getAnswer();
				} else {
					Spanned sp = Html.fromHtml(ConvertTextCodetoText(answers
							.getAnswer()));
					items[ansCount] = sp.toString();
				}
			}
		} else if (listBranches != null && listBranches.size() > 0) {
			items = new String[listBranches.size()];
			for (ansCount = 0; ansCount < listBranches.size(); ansCount++) {
				Branches branch = listBranches.get(ansCount);
				// items[ansCount] = branch.getBranchName();
				Spanned sp = Html.fromHtml(branch.getBranchName());
				items[ansCount] = sp.toString();
			}
		} else {
			items = new String[listWorkers.size()];
			for (ansCount = 0; ansCount < listWorkers.size(); ansCount++) {
				Workers worker = listWorkers.get(ansCount);
				// items[ansCount] = worker.getWorkerName();
				Spanned sp = Html.fromHtml(worker.getWorkerName());
				items[ansCount] = sp.toString();
			}
		}
		return items;
	}

	private void showDropdownAnswer(QuestionnaireData qd,
			ArrayList<Answers> listAnswers, Spinner spinner) {
		for (ansCount = 0; ansCount < listAnswers.size(); ansCount++) {
			String answer = spinner.getItemAtPosition(ansCount).toString();
			if (answer != null && qd != null && qd.getAnswersList() != null
					&& qd.getAnswersList().size() > 0
					&& answer.equals(qd.getAnswersList().get(0).getAnswer())) {
				thisQGroup.setSelectedAnswer(thisQGroup
						.getSelectedAnswer(ansCount));
				spinner.setSelection(ansCount);
				break;
			}
		}
	}

	private void showDropdownBranch(QuestionnaireData qd,
			ArrayList<Branches> listAnswers, Spinner spinner) {
		for (ansCount = 0; ansCount < listAnswers.size(); ansCount++) {
			Branches answers = listAnswers.get(ansCount);
			if (qd.getBranchID().equals(answers.getBranchID())) {
				spinner.setSelection(ansCount);
				break;
			}
		}
	}

	private void showDropdownWorker(QuestionnaireData qd,
			ArrayList<Workers> listAnswers, Spinner spinner) {
		for (ansCount = 0; ansCount < listAnswers.size(); ansCount++) {
			Workers answers = listAnswers.get(ansCount);
			if (qd.getWorkerID().equals(answers.getWorkerID())) {
				spinner.setSelection(ansCount);
				break;
			}
		}
	}

	private void setSpinnerValue(int objtp, QuestionnaireData qd,
			ArrayList<Answers> listAnswers, ArrayList<Branches> listBranches,
			ArrayList<Workers> listWorkers, Spinner spinner) {
		switch (objtp) {
		case 4:
		case 6:
			thisQGroup.setListAnswers(listAnswers);
			showDropdownAnswer(qd, listAnswers, spinner);
			break;
		case 10:
			thisQGroup.setListBranches(listBranches);
			showDropdownBranch(qd, listBranches, spinner);
			break;
		case 9:
			thisQGroup.setListWorkers(listWorkers);
			showDropdownWorker(qd, listWorkers, spinner);
			break;
		}
	}

	private int getNotFilledDropdOwn(ArrayList<Answers> listAnswers,
			ArrayList<Branches> listBranches, ArrayList<Workers> listWorkers) {

		String[] items = null;
		if (listAnswers != null && listAnswers.size() > 0) {
			boolean b = listAnswers
					.get(0)
					.getAnswer()
					.equals(((QuestionnaireActivity) (con)).getPlsSelectValue());
			if (IsMandatory && !b) {
				return 0;
			}

			items = new String[listAnswers.size()];
			for (ansCount = 0; ansCount < listAnswers.size(); ansCount++) {
				Answers answers = listAnswers.get(ansCount);

				if (answers.getAnswer().equalsIgnoreCase("Not filled")) {
					return (ansCount);
				}

			}
		} else if (listBranches != null && listBranches.size() > 0) {
			items = new String[listBranches.size()];
			for (ansCount = 0; ansCount < listAnswers.size(); ansCount++) {
				Answers answers = listAnswers.get(ansCount);

				if (answers.getAnswer().equalsIgnoreCase("Not filled")) {
					return (ansCount);
				}

			}
		} else {
			items = new String[listWorkers.size()];
			for (ansCount = 0; ansCount < listAnswers.size(); ansCount++) {
				Answers answers = listAnswers.get(ansCount);

				if (answers.getAnswer().equalsIgnoreCase("Not filled")) {
					return (ansCount);
				}

			}
		}
		return 0;

	}

	private View getMultipleDropdown(ArrayList<Answers> listAnswers,
			boolean isPrev) {
		multiSpinner = new MultiSelectionSpinner(con,listAnswers,thisQGroup);
		thisQGroup.setMultiSpinner(multiSpinner);
		CheckBox cbnotfilled = null;
		viewId = getViewId(viewId);
		multiSpinner.setId(viewId);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		QuestionnaireData qd = getSingleAnswer(questionObject.getDataID());
		ArrayList<String> listFinalAnswers = new ArrayList<String>();
		ArrayList<String> listFinalSelectedAnswers = new ArrayList<String>();
		for (ansCount = 0; ansCount < listAnswers.size(); ansCount++) {
			Answers answers = listAnswers.get(ansCount);
			listFinalAnswers.add(answers.getAnswer());
			if (answers.getAnswerDisplayCondition() != null) {
				Boolean b = IsObjectdisplaybyCondition(
						answers.getAnswerDisplayCondition(), false);
				if (!b)
					continue;
			}

			if (isPrev && isSelectedAnswers(qd, answers.getAnswerID())) {
				listFinalSelectedAnswers.add(answers.getAnswer());
			}
		}
		multiSpinner.setItems(listFinalAnswers);
		multiSpinner.setSelection(listFinalSelectedAnswers);

		if (order!=null && order.getIsJobInProgressOnServer()!=null
				&& order.isDataIdEnabled(set,questionObject.getDataID()))
			multiSpinner.setEnabled(false);
		return multiSpinner;
	}

	public static void hide_mandatoryFunction_for_spinner(
			dataQuestionGroup spinner, int i) {
		dataQuestionGroup thisQGroup = (dataQuestionGroup) spinner;
		Answers selectedAnswer = thisQGroup.getSelectedAnswer(i);
		hide_show_mi_view(thisQGroup, selectedAnswer);
	}

	public static void hide_show_mi_view(dataQuestionGroup thisQGroup,
			Answers selectedAnswer) {
		if (thisQGroup != null
				&& (thisQGroup.getWholeMiView() != null
						|| thisQGroup.getbTextbox() != null || thisQGroup
						.getTextbox() != null)
				&& selectedAnswer != null
				&& (selectedAnswer.getHideAdditionalInfo() != null && selectedAnswer
						.getHideAdditionalInfo().equals("1"))) {

			if (thisQGroup.getWholeMiView() != null) {
				thisQGroup.getWholeMiView().setVisibility(RelativeLayout.GONE);
				if (thisQGroup.getMiTextView() != null) {
					thisQGroup.getMiTextView().setVisibility(
							RelativeLayout.GONE);
				}
			} else if (thisQGroup.getbTextbox() != null) {
				thisQGroup.getbTextbox().setVisibility(RelativeLayout.GONE);
				if (thisQGroup.getMiTextView() != null) {
					thisQGroup.getMiTextView().setVisibility(
							RelativeLayout.GONE);
				}
			} else if (thisQGroup.getTextbox() != null) {
				thisQGroup.getTextbox().setVisibility(RelativeLayout.GONE);
				if (thisQGroup.getMiTextView() != null) {
					thisQGroup.getMiTextView().setVisibility(
							RelativeLayout.GONE);
				}
			}
		} else if (thisQGroup != null
				&& (thisQGroup.getWholeMiView() != null
						|| thisQGroup.getbTextbox() != null || thisQGroup
						.getTextbox() != null) && selectedAnswer != null) {
			if (thisQGroup.getWholeMiView() != null) {
				thisQGroup.getWholeMiView().setVisibility(
						RelativeLayout.VISIBLE);
				if (thisQGroup.getMiTextView() != null) {
					thisQGroup.getMiTextView().setVisibility(
							RelativeLayout.VISIBLE);
				}
			} else if (thisQGroup.getbTextbox() != null) {
				thisQGroup.getbTextbox().setVisibility(RelativeLayout.VISIBLE);
				if (thisQGroup.getMiTextView() != null) {
					thisQGroup.getMiTextView().setVisibility(
							RelativeLayout.VISIBLE);
				}
			} else if (thisQGroup.getTextbox() != null) {
				thisQGroup.getTextbox().setVisibility(RelativeLayout.VISIBLE);
				if (thisQGroup.getMiTextView() != null) {
					thisQGroup.getMiTextView().setVisibility(
							RelativeLayout.VISIBLE);
				}
			}

		}

	}

	private View getDropDownView(final ArrayList<Answers> listAnswers,
			ArrayList<Branches> listBranches, ArrayList<Workers> listWorkers,
			boolean isPrev) {
		final GroupSpinner spinner = new GroupSpinner(con);
		spinner.setTag(thisQGroup);
		spinner.setThisQgroup(thisQGroup);
		thisQGroup.setSpinner(spinner);
		thisQGroup.setOrdinarySpinner(spinner);
		QuestionnaireData qd = null;
		if (isPrev)
			qd = getSingleAnswer(questionObject.getDataID());
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(con,
				UIHelper.getSpinnerLayoutSize(con, modeSelect), getListItem(
						listAnswers, listBranches, listWorkers));

		adapter.setDropDownViewResource(UIHelper.getSpinnerLayoutSize(con,
				modeSelect));
		spinner.setAdapter(adapter);
		if (isPrev && qd != null) {
			// spinner.setSelection(listAnswers.size() - 1);
			setSpinnerValue(Helper.getInt(qd.getObjectType()), qd, listAnswers,
					listBranches, listWorkers, spinner);
		} else {
			if (IsMandatory) {
				spinner.setSelection(0);
			} else {
				if (IsMandatory) {
					spinner.setSelection(0);
				} else
					spinner.setSelection(getNotFilledDropdOwn(listAnswers,
							listBranches, listWorkers));
			}
		}
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int i,
					long id) {
				View spinner = (View) view.getParent();
				dataQuestionGroup thisQGroup = ((GroupSpinner) spinner)
						.getThisQgroup();
				UIQuestionGroupHelper.hide_mandatoryFunction_for_spinner(
						thisQGroup, i);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		// spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
		// @Override
		// public void onItemSelected(AdapterView<?> arg0, View arg1,
		// int arg2, long arg3) {
		// // TODO Auto-generated method stub
		// if (spinnerFirstTime)
		// spinnerFirstTime = false;
		// else {
		// if (!questionObject.isMi())
		// nextbtnClickListener();
		// spinnerFirstTime = true;
		// }
		// }
		//
		// @Override
		// public void onNothingSelected(AdapterView<?> arg0) {
		// // TODO Auto-generated method stub
		// }
		// });

		viewId = getViewId(viewId);
		spinner.setId(viewId);

		changeSpinnerFontSize(spinner);

		if (order!=null && order.getIsJobInProgressOnServer()!=null
				&& order.isDataIdEnabled(set,questionObject.getDataID()))
			spinner.setEnabled(false);
		return spinner;
	}

	public Spinner changeSpinnerFontSize(Spinner spinner) {

		for (int i = 0; i < spinner.getAdapter().getCount(); i++) {
			View tblRow = spinner.getAdapter().getView(i, null, null);
			setFontSize(tblRow);

		}
		return spinner;
	}

	private View getAnswersView(int displayType, int qtl,
			ArrayList<Answers> listAns, boolean isPrev) {

		switch (displayType) {
		case 0:

			if (qtl == 4 || qtl == 8 || qtl == 9)
				return getCheckButtonImageView(listAns, isPrev);
			return getRadioButtonImageView(listAns, isPrev);




		case 1:
			case 5:
			if (qtl == 4)
				return null;
			if (qtl == 8 || qtl == 9) {
				return getMultipleDropdown(listAns, isPrev);
			} else
				return getDropDownView(listAns, null, null, isPrev);
		case 3:
		case 2:

			if (qtl == 4)
				return null;
			return getMultipleDropdown(listAns, isPrev);

			// return getTextView();
		case 4:

			if (qtl == 4)
				return null;
			return getCheckButtonImageView(listAns, isPrev);
		}
		return null;
	}

	private View getAnswersView(boolean isPrev) {
		ArrayList<Answers> listAnswers = questionObject.getListAnswers();
		// qGroups.
		thisQGroup.setListAnswers(listAnswers);
		if (listAnswers == null)
			return null;
		// listAnswers =
		// Helper.getSortData(Helper.getInt(questionObject.getAnswerOrdering()),
		// listAnswers);
		View v = getAnswersView(Helper.getInt(questionObject.getDisplayType()),
				Helper.getInt(questionObject.getQuestionTypeLink()),
				listAnswers, isPrev);
		if (v == null)
			return null;
		viewId = getViewId(viewId);
		v.setId(viewId);
		v.setTag(questionObject.getDataID());
		return v;
	}

	private View getQuestionDescriptionView() {
		TextView tv = new TextView(con);
		if (Helper.comapreString(questionObject.getAttachment(), "110"))
			IsAttachement = true;
		tv = setTextViewProperties(tv, questionObject.getQuestionDescription(),
				questionObject.getFont(), questionObject.getColor(),
				questionObject.getSize(), questionObject.getItalics(), "0",
				questionObject.getUnderline(), questionObject.getAlign());

		tv = getTextFromHtmlFormate(
				Helper.getValidString(questionObject.getQuestionDescription()),
				tv);
		tv = getTextFromHtmlFormate(tv.getText().toString(), tv);
		tv.setText(Helper.makeHtmlString(ConvertTextCodetoText(questionObject
				.getQuestionDescription())));
		if (Helper.IsEmptyString(tv.getText().toString()))
			tv.setVisibility(View.GONE);
		viewId = getViewId(viewId);
		tv.setId(viewId);
		if (Build.VERSION.SDK_INT > 13) {
			if (modeSelect == 3)
				tv.setGravity(Gravity.RIGHT);
		}
		setFontSize(tv);
		tv.setPadding(Constants.dpToPx(10),0,Constants.dpToPx(10),0);
		return tv;
	}

	private RelativeLayout.LayoutParams getAttachmentLayoutParam(int id) {
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		// lp.addRule(setAlignmentOfView(Helper.getInt(questionObject.getAlign())));
		lp.addRule(RelativeLayout.BELOW, id);
		// lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		// lp.addRule(RelativeLayout.ALIGN_RIGHT);
		// lp.setMargins(300, 0, 0,0 );
		// lp.addRule(RelativeLayout.BELOW, id);
		return lp;
	}

	private RelativeLayout.LayoutParams getAttechmentLayout(int id) {
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		lp.setMargins(60, 0, 20, 0);
		lp.addRule(RelativeLayout.CENTER_VERTICAL, id);
		lp.addRule(RelativeLayout.ALIGN_TOP, id);
		return lp;
	}

	private View getattachmentView() {
		ImageButton ib = new ImageButton(con);
		ib.setBackgroundResource(R.drawable.attachment);
		if (order!=null && order.getIsJobInProgressOnServer()!=null
				&& order.isDataIdEnabled(set,questionObject.getDataID()))
			ib.setEnabled(false);
		ib.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				((Activity) con).openOptionsMenu();
			}
		});
		viewId = getViewId(viewId);
		ib.setId(viewId);
		ib.setTag(questionObject.getDataID());
		return ib;
	}

	private RelativeLayout.LayoutParams getLayoutParam(int id) {
		double width = 0;
		if (isRadioCheck) {
			width = ultimateWidth;
		} else
			width = display.getWidth() * (sixtyTwo / 100.0);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				(int) width, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		// lp.addRule(setAlignmentOfView(Helper.getInt(questionObject.getAlign())));
		lp.addRule(RelativeLayout.BELOW, id);
		if (modeSelect == 3) {
			lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			lp.addRule(RelativeLayout.ALIGN_RIGHT);
		}
		// lp.setMargins(300, 0, 0,0 );
		// lp.addRule(RelativeLayout.BELOW, id);
		return lp;
	}

	private RelativeLayout.LayoutParams getQuestionLayoutParam(int id) {
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				(int) (display.getWidth() * sixtyTwo / 100.0),
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		// lp.addRule(setAlignmentOfView(Helper.getInt(questionObject.getAlign())));
		lp.addRule(RelativeLayout.BELOW, id);
		if (modeSelect == 3) {
			lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			lp.addRule(RelativeLayout.ALIGN_RIGHT);
		}
		//lp.setMargins(20, 0, 20,0 );
		// lp.addRule(RelativeLayout.BELOW, id);
		return lp;
	}

	private RelativeLayout.LayoutParams getQuestionLayoutParamWidth(int id,
			int width) {
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(width,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		// lp.addRule(setAlignmentOfView(Helper.getInt(questionObject.getAlign())));
		lp.addRule(RelativeLayout.BELOW, id);
		if (modeSelect == 3) {
			lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			lp.addRule(RelativeLayout.ALIGN_RIGHT);
		}
		// lp.setMargins(300, 0, 0,0 );
		// lp.addRule(RelativeLayout.BELOW, id);
		return lp;
	}

	private RelativeLayout.LayoutParams getGroupLayoutParam(int id) {
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		// lp.addRule(setAlignmentOfView(Helper.getInt(questionObject.getAlign())));
		lp.addRule(RelativeLayout.BELOW, id);
		if (modeSelect == 3) {
			lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			lp.addRule(RelativeLayout.ALIGN_RIGHT);
		} else {
			lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			lp.addRule(RelativeLayout.ALIGN_RIGHT);
		}
		lp.setMargins(0, 0, 0, 20);
		// lp.addRule(RelativeLayout.BELOW, id);
		return lp;
	}

	private RelativeLayout.LayoutParams getQuestionGroupLayoutParam(int id) {
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				display.getWidth(),
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		// lp.addRule(setAlignmentOfView(Helper.getInt(questionObject.getAlign())));
		{
			lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			lp.addRule(RelativeLayout.ALIGN_RIGHT);
		}
		// lp.setMargins(300, 0, 0,0 );
		// lp.addRule(RelativeLayout.BELOW, id);
		return lp;
	}

	private String getProperDataId(String dataID2) {
		if (dataID2.contains("_")) {

			String dataId = getDataIdFromDataId(dataID2);
			String groupId = getGroupIdFromDataId(dataID2.replace(dataId + "_",
					""));
			if (dataID2.contains("-")) {
				String titleId = getTitleFromDataId(dataID2.replace(dataId
						+ "_" + groupId + "-", ""));
				return groupId + ";" + titleId + ";" + dataId;
			}
			return groupId + ";" + "0" + ";" + dataId;
		}

		return dataID2;
	}

	private String getText(int index, String val) // 13, 32432
	{
		switch (index) {
		case 101:
			if (Orders.getBranchProps() != null
					&& Orders.getBranchProps().size() > 0) {
				for (int i = 0; i < Orders.getBranchProps().size(); i++) {
					if (Orders.getBranchProps().get(i).getID() != null
							&& Orders.getBranchProps().get(i).getID()
									.equals(order.getBranchLink())
							&& Orders.getBranchProps().get(i).getPropID() != null
							&& Orders.getBranchProps().get(i).getPropID()
									.equals(val)) {
						return Orders.getBranchProps().get(i).getValueID();
					}
				}
			}
			break;

		case 1:
			if (questionnaireData != null) {
				for (int i = 0; i < questionnaireData.size(); i++) {
					String dataId = getProperDataId(questionnaireData.get(i)
							.getDataID());
					if (val.equals(dataId)) {
						if (questionnaireData.get(i).getAnswersList() != null
								&& questionnaireData.get(i).getAnswersList()
										.size() > 0)
							return questionnaireData.get(i).getAnswersList()
									.get(0).getAnswer();
						else if (questionnaireData.get(i).getAnswerText() != null)
							return questionnaireData.get(i).getAnswerText();
						else
							return questionnaireData.get(i).getFreetext();
					}
				}
			}
			return "";
		case 2:
			if (questionnaireData != null) {
				for (int i = 0; i < questionnaireData.size(); i++) {
					String dataId = getProperDataId(questionnaireData.get(i)
							.getDataID());
					if (val.equals(dataId)) {
						return questionnaireData.get(i).getAnswerText();
					}
				}
			}
			return "";

		case 3:
			String value = "";
			if (questionnaireData != null) {

				for (int i = 0; i < questionnaireData.size(); i++) {

					String dataId = getProperDataId(questionnaireData.get(i)
							.getDataID());
					if (val.equals(dataId))
						value = questionnaireData.get(i).getAnswerText();
				}
			}

			if (value.contains(":") && !value.contains("-")) {
				String s = value;
				SimpleDateFormat timeformat = new SimpleDateFormat("kk:mm:ss",
						Locale.ENGLISH);
				try {
					Date date = timeformat.parse(s);
					long l = date.getTime();
					// if (l < 0)
					// l = -1 * l;
					value = l + "";
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (!value.contains(":") && value.contains("-")) {
				String s = value;
				SimpleDateFormat timeformat = new SimpleDateFormat(
						"yyyy-MM-dd", Locale.ENGLISH);
				try {
					Date date = timeformat.parse(s);
					long l = date.getTime();
					// if (l < 0)
					// l = -1 * l;
					value = l + "";
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			return value;
		case 11:
			if (questionnaireData != null) {
				for (int i = 0; i < questionnaireData.size(); i++) {
					if (val.equals(questionnaireData.get(i).getObjectCode()))
						return questionnaireData.get(i).getAnswersList().get(0)
								.getAnswerCode();
				}
			}
			return "";
		case 13:
			if (questionnaireData != null) {
				for (int i = 0; i < questionnaireData.size(); i++) {
					if (val.equals(questionnaireData.get(i).getObjectCode())
							&& questionnaireData.get(i).getAnswerText() != null)
						return questionnaireData.get(i).getAnswerText();
				}
			}
			return "";
		}
		return "";
	}

	private TextView commentTextView(String msg2) {
		TextView commonTextbox = new TextView(con);
		commonTextbox.setGravity(Gravity.TOP);
		viewId = getViewId(viewId);
		commonTextbox.setTextSize(15);
		commonTextbox.setId(viewId);
		commonTextbox.setText(Helper.makeHtmlString(msg2));
		commonTextbox.setTextColor(Color.BLACK);
		commonTextbox.setTag(questionObject.getDataID());
		setFontSize(commonTextbox);

		return commonTextbox;
	}

	private boolean getThisAnswer(String val2, List<Answers> listAnswer) {
		for (int i = 0; i < listAnswer.size(); i++) {
			if (val2.equals(listAnswer.get(i).getAnswerCode())) {
				return true;
			}
		}
		return false;
	}

	private boolean getThisAnswerbyAnswerId(String val2,
			List<Answers> listAnswer) {
		for (int i = 0; i < listAnswer.size(); i++) {
			if (val2.equals(listAnswer.get(i).getAnswerID())) {
				return true;
			}
		}
		return false;
	}

	private int getText(String val, String val2, boolean is15) {
		if (questionnaireData != null && !is15) {
			for (int i = 0; i < questionnaireData.size(); i++) {
				if (val.equals(questionnaireData.get(i).getObjectCode())
						&& questionnaireData.get(i).getAnswersList() != null
						&& questionnaireData.get(i).getAnswersList().size() > 0
						&& getThisAnswer(val2, questionnaireData.get(i)
								.getAnswersList()))
					return 1;
			}
		} else if (questionnaireData != null) {
			for (int i = 0; i < questionnaireData.size(); i++) {
				if (val.equals(questionnaireData.get(i).getDataID())
						&& questionnaireData.get(i).getAnswersList() != null
						&& questionnaireData.get(i).getAnswersList().size() > 0
						&& getThisAnswerbyAnswerId(val2,
								questionnaireData.get(i).getAnswersList()))
					return 1;
			}
		}
		return 0;
	}

	int getTextCode(String text) {
		// return 0;
		try {
			int start = text.indexOf("$[");
			int end = text.indexOf("]$");
			if (start == -1 || end == -1)
				return 0;
			return Integer.parseInt(text.substring(start + 2, end)); // 12,
																		// 12312
		} catch (Exception e) {
			e.printStackTrace();
			return 401;
		}
	}

	String getTextmultiCode(String text) {
		while (true) {
			int start = text.indexOf("$[");
			int end = text.indexOf("]$");
			if (start == -1 || end == -1)
				break;
			String str = text.substring(start + 2, end); // 12, 1323
			String[] strarr = str.split(",");
			String s = text.substring(start, end + 2);
			if (strarr.length == 1) {
				if (text.contains("text"))
					break;
				String ss = ConvertTextCodetoText(s);
				text = text.replace(s, ss);
			} else if (strarr.length == 2) {
				String defaultValue = null;
				if (strarr[0].contains(";")) {
					String[] defArray = strarr[0].split(";");
					strarr[0] = defArray[0];
					if (defArray.length > 1)
						defaultValue = defArray[1];
					else
						defaultValue = "";
				}
				String ss = getText(Integer.parseInt(strarr[0]), strarr[1]);
				if ((ss == null || ss.equals("0") || ss.equals(""))
						&& defaultValue != null) {
					ss = defaultValue;
				}
				text = text.replace(s, ss);
			} else if (strarr.length > 2) {
				if (Integer.parseInt(strarr[0]) == 120) {
					String defaultValue = null;
					if (strarr[0].contains(";")) {
						String[] defArray = strarr[0].split(";");
						strarr[0] = defArray[0];
						if (defArray.length > 1)
							defaultValue = defArray[1];
						else
							defaultValue = "";
					}
					String ss = getText(Integer.parseInt(strarr[0]), strarr[1],
							strarr[2]);
					if ((ss == null || ss.equals("0") || ss.equals(""))
							&& defaultValue != null) {
						ss = defaultValue;
					}
					text = text.replace(s, "" + ss);
				} else {

					int ss = getText(strarr[1], strarr[2],
							strarr[0].equals("15"));
					text = text.replace(s, "" + ss);
				}
			}
		}
		return text;
	}

	private String getText(int index, String val, String val2) // 13, 32432
	{
		switch (index) {
		case 120:
			if (questionnaireData != null) {
				for (int i = 0; i < questionnaireData.size(); i++) {
					if (val != null
							&& questionnaireData.get(i) != null
							&& questionnaireData.get(i).getObjectCode() != null
							&& val.equals(questionnaireData.get(i)
									.getObjectCode())
							&& questionnaireData.get(i).getAnswersList() != null) {
						String s = "";
						String comma = "";
						for (int j = 0; j < questionnaireData.get(i)
								.getAnswersList().size(); j++) {
							s += comma
									+ questionnaireData.get(i).getAnswersList()
											.get(j).getAnswer();
							comma = ",";
						}
						return s;
					}
				}
			}
			return "";
		}

		return "";
	}

	private String ConvertTextCodetoText(String text) {
		if (text != null)
			return ((QuestionnaireActivity) (con)).ConvertTextCodetoText(text,
					false);
		while (true) {
			switch (getTextCode(text)) {
			case 0:
				return text;
			case 200:
				text = text.replace("$[200]$", order.getBranchFullname());
				break;
			case 201:
				text = text.replace("$[201]$", order.getBranchName());
				break;
			case 202:
				text = text.replace("$[202]$", order.getFullname());
				break;
			case 203:
				if (order.getCheckerLink() == null)
					order.setCheckerLink("");
				text = text.replace("$[203]$", order.getCheckerLink());
				break;
			case 204:
				if (order.getCheckerCode() == null)
					order.setCheckerCode("");
				text = text.replace("$[204]$", order.getCheckerCode());
				break;
			case 205:
				text = text.replace("$[205]$", order.getBranchCode());
				break;
			case 206:
				if (set.getSetCode() == null)
					set.setSetCode("");
				text = text.replace("$[206]$", set.getSetCode());
				break;
			case 207:
				text = text.replace("$[207]$", "");
				break;
			case 211:
				text = text.replace("$[211]$", "");
				break;
			case 209:
				text = text.replace("$[209]$", "");
				break;
			case 210:
				text = text.replace("$[210]$", "");
				break;
			case 212:
				text = text.replace("$[212]$", "");
				break;
			case 216:
				text = text.replace("$[216]$", "");
				break;
			case 213:
				text = text.replace("$[213]$", order.getClientName());
				break;
			case 208:
				if (order.getBranchPhone() == null)
					order.setBranchPhone("");
				text = text.replace("$[208]$", order.getBranchPhone());
				break;
			case 214:
				if (order.getAddress() == null)
					order.setAddress("");
				text = text.replace("$[214]$", order.getAddress());
				break;
			case 215:
				text = text.replace("$[215]$", order.getCityName());
				break;
			case 217:
				if (order.getOpeningHours() == null)
					order.setOpeningHours("");
				text = text.replace("$[217]$", order.getOpeningHours());
				break;
			case 218:
				text = text.replace("$[218]$", order.getSetName());
				break;
			case 300:
				text = text.replace("$[300]$", order.getDescription());
				break;
			case 301:
				text = text.replace("$[301]$", order.getDate());
				break;
			case 302:
				text = text.replace("$[302]$", order.getTimeStart());
				break;
			case 303:
				if (order.getsPurchaseDescription() == null)
					order.setsPurchaseDescription("");
				text = text.replace("$[303]$", order.getsPurchaseDescription());
				break;
			case 304:
				if (order.getsPurchase() == null)
					order.setsPurchase("");
				text = text.replace("$[304]$", order.getsPurchase());
				break;

			case 400:
				text = text.replace("$[400]$", "");
				break;

			case 510:
				text = text.replace("$[510]$", "");
				break;

			case 401:
				try {
					if (!text.toLowerCase().contains("text"))
						return getTextmultiCode(text);
					else {

					}
				} catch (Exception e) {
					return text;
				}
			default:
				return text;
			}
		}
		// return text;
	}

	String CheckDisplayCondition(String text) {
		while (true) {
			int start = text.indexOf("$[");
			int end = text.indexOf("]$");
			if (start == -1 || end == -1)
				break;
			String str = text.substring(start + 2, end);
			String[] strarr = str.split(",");
			if (strarr.length == 2) {
				String defaultValue = null;
				if (strarr[0].contains(";")) {
					String[] defArray = strarr[0].split(";");
					strarr[0] = defArray[0];
					if (defArray.length > 1)
						defaultValue = defArray[1];
					else
						defaultValue = "";
				}
				String ss = getText(Integer.parseInt(strarr[0]), strarr[1]);
				if ((ss == null || ss.equals("0") || ss.equals(""))
						&& defaultValue != null) {
					ss = defaultValue;
				}
				return ss;
			} else if (strarr.length > 2) {
				int ss = getText(strarr[1], strarr[2], strarr[0].equals("15"));
				return "" + ss;
			} else {
				return ConvertTextCodetoText(text);
			}
		}
		return text;
	}

	private boolean verifyDisplayCondition(String[] str) {
		// if(str.length > 1)
		{
			boolean b = (str[1].startsWith("'") && str[1].endsWith("'"));
			String s = CheckDisplayCondition(str[0]);
			if (b) {
				if (str[1].trim().contains(s.trim()))
					return true;
			} else {
				if (str[1].trim().equals(s.trim()))
					return true;
			}
		}
		return false;
	}

	private boolean callBracketLayerFunction(String displayCondition) {

		while (true) {
			int start = displayCondition.lastIndexOf("(",
					displayCondition.length());
			int end = displayCondition.indexOf(")", start);

			if (start == -1 || end == -1)
				break;
			String subString = displayCondition.substring(start, end + 1);
			boolean isOkay = IsObjectdisplaybyCondition(
					subString.replace("(", "").replace(")", ""), false);
			if (isOkay) {
				displayCondition = displayCondition.replace(subString, "1=1");
			} else {
				displayCondition = displayCondition.replace(subString, "1=2");
			}
		}
		return IsObjectdisplaybyCondition(displayCondition, false);
	}

	private boolean outerFunctionTocallIfORGate(String displayCondition) {
		// displayCondition = displayCondition.replace("(", "");
		// displayCondition = displayCondition.replace(")", "");
		//
		if (displayCondition != null && displayCondition.contains("(")
				&& displayCondition.contains("")) {
			return callBracketLayerFunction(displayCondition);
		}

		String[] strArr = displayCondition.split("\\|\\|");
		for (int i = 0; i < strArr.length; i++) {
			displayCondition = strArr[i];
			String[] str = null;
			try {
				if (IsObjectdisplaybyCondition(strArr[i], false))
					return true;
			} catch (Exception ex) {
				Toast.makeText(
						con,
						con.getResources().getString(
								R.string._error_display_condition),
						Toast.LENGTH_LONG).show();
				return true;
			}
		}
		return false;
	}

	private boolean IsObjectdisplaybyCondition(String displayCondition,
			boolean isValidaing) {
		// if (displayCondition != null)
		// return ((QuestionnaireActivity) con)
		// .IsObjectdisplaybyCondition(displayCondition);
		if (displayCondition == null || displayCondition.equals(""))
			return true;
		if (isValidaing == false && displayCondition.equals(""))
			return true;
		if (displayCondition.replace(" ", "").equals("1=2"))
			return false;
		if (displayCondition.replace(" ", "").equals("1=43"))
			return false;
		if (displayCondition.replace(" ", "").equals("1=1"))
			return true;

		if (displayCondition != null)
			return ((QuestionnaireActivity) con)
					.IsObjectdisplaybyCondition(displayCondition);

		if (displayCondition != null && displayCondition.contains("||")) {
			boolean isOkay = outerFunctionTocallIfORGate(displayCondition
					.replace(" ", ""));

			return isOkay;
		}

		displayCondition = displayCondition.replace("&amp;", "&");
		displayCondition = displayCondition.replace("&", "&amp;");

		displayCondition = displayCondition.replace("&amp;gt;", "&gt;");
		displayCondition = displayCondition.replace("&amp;lt;", "&lt;");

		// displayCondition="$[12,S2,1]$+ $[12,S2,2]$$&gt;0 &amp; $[12,S2,3]$+ $[12,S2,4]$+ $[12,S2,5]$&gt;1";
		String[] strArr = displayCondition.split("&amp;");
		for (int i = 0; i < strArr.length; i++) {
			displayCondition = strArr[i];
			String[] str = null;
			try {
				if (!verifyDisplayConditionFull(strArr[i]))
					return false;
			} catch (Exception ex) {
				Toast.makeText(
						con,
						con.getResources().getString(
								R.string._error_display_condition),
						Toast.LENGTH_LONG).show();
			}
		}

		return true;
	}

	private boolean verifyDisplayConditionFull(String text) {
		if (text != null) {
			text = text.replace(" ", "");
			text = text.replace("&quot;", "'");
			text = text.replace("\\", "");
			if (text.contains("$[11,S9_1]$")) {
				int i = 0;
				i++;
			}
		}
		int total = 0;
		String totalSs = "";
		String ss = "";
		while (true) {
			int start = text.indexOf("$[");
			int end = text.indexOf("]$");

			if (start == -1 || end == -1)
				break;
			String str = text.substring(start + 2, end);

			String[] strarr = str.split(",");
			if (strarr.length == 2) {
				String defaultValue = null;
				if (strarr[0].contains(";")) {
					String[] defArray = strarr[0].split(";");
					strarr[0] = defArray[0];
					if (defArray.length > 1)
						defaultValue = defArray[1];
					else
						defaultValue = "";
				}
				ss = getText(Integer.parseInt(strarr[0]), strarr[1]);
				if ((ss == null || ss.equals("0") || ss.equals(""))
						&& defaultValue != null) {
					ss = defaultValue;
				}
				totalSs += ss;
				boolean isAdd = true;
				try {
					String sbstring = text.substring(start - 1, start);
					isAdd = sbstring.equals("+");
				} catch (Exception ex) {
					isAdd = true;
				}
				if (ss.equals("")) {
					text = text.replace("$[" + str + "]$", "''");
				}
				text = text.replace("$[" + str + "]$", ss);
				try {

					int i = Integer.parseInt(ss);
					if (isAdd == true)
						total += i;

				} catch (Exception ex) {

				}
			} else if (strarr.length > 2) {
				boolean isAdd = true;
				try {
					String sbstring = text.substring(start - 1, start);
					isAdd = sbstring.equals("+");
				} catch (Exception ex) {
					isAdd = true;
				}
				int number = getText(strarr[1], strarr[2],
						strarr[0].equals("15"));
				text = text.replace("$[" + str + "]$", number + "");
				if (isAdd == true)
					total += number;
			}
			text = text.replace("$[" + str + "]$", "");
		}
		String str[];
		text = text.replace("(", "");
		text = text.replace(")", "");

		if (text.contains("&lt;=")) {

			str = text.split("&lt;=");
			try {
				int i = Integer.parseInt(str[1].trim());
				total = calculateMath(str[0], total);
				if (total <= i)
					return true;
				else
					return false;

			} catch (Exception ex) {

			}

		} else if (text.contains("&gt;=")) {

			str = text.split("&gt;=");
			try {
				int i = Integer.parseInt(str[1].trim());
				total = calculateMath(str[0], total);
				if (total >= i)
					return true;
				else
					return false;

			} catch (Exception ex) {

			}

		} else if (text.contains("&lt;")) {

			str = text.split("&lt;");
			try {
				int i = Integer.parseInt(str[1].trim());
				total = calculateMath(str[0], total);
				if (total < i)
					return true;
				else
					return false;

			} catch (Exception ex) {

			}

		} else if (text.contains("&gt;")) {

			str = text.split("&gt;");
			try {
				int i = Integer.parseInt(str[1].trim());
				total = calculateMath(str[0], total);
				if (total > i)
					return true;
				else
					return false;

			} catch (Exception ex) {

			}

		}
		if (text.contains("!=")) {

			str = text.split("!=");
			boolean b = (str[1].contains("'"));
			try {
				double i = Double.parseDouble(str[1].trim());
				total = calculateMath(str[0], total);
				if (total != i)
					return true;
				else
					return false;

			} catch (Exception ex) {

			}

			if (b) {

				if (str.length == 2
						&& !str[0].replace("'", "").equals(
								str[1].replace("'", "")))
					return true;
				else
					return false;

				// if (totalSs.equals(""))
				// return false;
				// else
				// return true;
			}
			if (str.length == 2 && !str[0].equals(str[1]))
				return true;
			else
				return false;

		} else if (text.contains("=")) {
			str = text.split("=");
			boolean b = (str[1].contains("'"));
			try {
				total = calculateMath(str[0], total);
				double i = Double.parseDouble(str[1].trim());
				if (total == i)
					return true;
				else
					return false;

			} catch (Exception ex) {

			}

			if (!b) {
				if (str.length == 2
						&& str[0].replace("'", "").equals(
								str[1].replace("'", "")))
					return true;
				else
					return false;

				// if (totalSs.equals(""))
				// return true;
				// else
				// return false;
			}

			if (str.length == 2
					&& str[0].replace("'", "").equals(str[1].replace("'", "")))
				return true;
			else
				return false;

		}

		return false;
	}

	private int calculateMath(String text, int total) {
		Calculable calc;
		try {
			calc = new ExpressionBuilder(text).build();
			double result1 = calc.calculate();
			return (int) result1;
		} catch (UnknownFunctionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnparsableExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return total;
	}

	private CheckBox getShowCriticalView() {
		CheckBox ckb = new CheckBox(con);
		ckb.setText("Critical");
		ckb.setTextColor(Helper.getIntColor(questionObject.getColor()));
		viewId = getViewId(viewId);
		ckb.setId(viewId);
		setFontSize(ckb);
		ckb.setTag(questionObject.getDataID());
		return ckb;
	}

	private TextView getMiView() {
		TextView tv = new TextView(con);
		StringBuffer sb = new StringBuffer();
		if (Helper.comapreString(questionObject.getMiMandatory(), "1")) {
			sb.append("*");
			IsMiMandatory = true;
		}
		sb.append(Helper.getValidString(questionObject.getMiDescription()));
		// Log.v("sb text",sb.toString());
		tv = getTextFromHtmlFormate(sb.toString(), tv);
		tv.setTag(questionObject.getDataID());
		sb = null;
		viewId = getViewId(viewId);
		miLabelViewId = viewId;
		tv.setId(viewId);
		tv = setTextViewProperties(tv, questionObject.getQuestionDescription(),
				questionObject.getFont(), questionObject.getColor(),
				questionObject.getSize(), questionObject.getItalics(),
				questionObject.getBold(), questionObject.getUnderline(),
				questionObject.getAlign());
		tv.setPadding(20,0,20,0);
		setFontSize(tv);
		return tv;
	}

	private View getTitleTextView(String title) {
		TextView tv = new TextView(con);

		String altTitle = "";

		if (titles != null)
			for (int i = 0; i < titles.size(); i++) {
				if (titles.get(i).getTitleText() != null
						&& title != null
						&& titles.get(i).getTitleText().toLowerCase().trim()
								.equals(title.toLowerCase().trim())) {
					altTitle = titles.get(i).getAltTitle();
					altTitle = ConvertTextCodetoText(altTitle);
				}
			}

		setFontSize(tv);
		title = ConvertTextCodetoText(title);

		LinearLayout layout = new LinearLayout(con);
		double width = display.getWidth() / 3.5;
		if (sixtyTwo < 50)
			width = display.getWidth() * (sixtyTwo / 100.0);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				(int) width, android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		lp.gravity = Gravity.CENTER_VERTICAL;
		lp.setMargins(10, 0, 10, 0);
		layout.addView(tv, lp);
		tv.setGravity(Gravity.CENTER_VERTICAL);

		if (modeSelect == 3 && sixtyTwo < 50)
			tv.setGravity(Gravity.RIGHT);

		// tv = setTextViewProperties(tv, questionObject.getText(),
		// questionObject.getFont(), questionObject.getColor(),
		// questionObject.getSize(), questionObject.getItalics(),
		// questionObject.getBold(), questionObject.getUnderline(),
		// questionObject.getAlign());
		// tv.setTag(questionObject.getDataID());
		setFontSize(tv);
		tv = getTextFromHtmlFormate(Helper.getValidString(title), tv);
		if (altTitle != null && altTitle.length() > 0)
			tv.setText(Helper.makeHtmlString(altTitle));
		else
			tv.setText(Helper.makeHtmlString(title));
		tv.setTextColor(con.getResources().getColor(android.R.color.black));
		String str = ConvertTextCodetoText(tv.getText().toString());
		tv.setText(Helper.makeHtmlString(str));
		tv.setPadding(20,0,20,0);
		return layout;
	}

	private TextView getTextView() {
		TextView tv = new TextView(con);

		{
			tv = setTextViewProperties(tv, questionObject.getText(),
					questionObject.getFont(), questionObject.getColor(),
					questionObject.getSize(), questionObject.getItalics(),
					questionObject.getBold(), questionObject.getUnderline(),
					questionObject.getAlign());
			tv.setTag(questionObject.getDataID());
			setFontSize(tv);
			tv = getTextFromHtmlFormate(
					Helper.getValidString(questionObject.getText()), tv);
			tv = getTextFromHtmlFormate(tv.getText().toString(), tv);
			String str = ConvertTextCodetoText(tv.getText().toString());
			tv.setText(Helper.makeHtmlString(str));
		}
		if (questionObject.getText().contains("href")) {
			final String href = questionObject.getText();

			tv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

				}
			});
		}
		viewId = getViewId(viewId);
		tv.setId(viewId);
		if (Build.VERSION.SDK_INT > 13) {
			if (modeSelect == 3)
				tv.setGravity(Gravity.RIGHT);
		}

		// String str1 =
		// "&lt;h3 style=&quot;text-align: center;&quot;&gt;&lt;span style=&quot;color:#ffa500;&quot;"
		// +
		// "&gt;&lt;span style=&quot;background-color:#f0ffff;&quot;&gt;Short Branch Name: &lt;/span&gt;&lt;"
		// +
		// "span style=&quot;font-family: Arial;&quot;&gt;&lt;span style=&quot;background-color:#f0ffff;&quot;"
		// +
		// "&gt;$[201]$  &lt;/span&gt;&lt;/span&gt;&lt;/span&gt;&lt;span style=&quot;font-size: 18px; color: "
		// +
		// "rgb(0, 204, 0); text-align: center;&quot;&gt;About Employees&lt;/span&gt;&lt;/h3&gt;";
		//
		// tv = getTextFromHtmlFormate(str1, tv);
		// tv = getTextFromHtmlFormate(tv.getText().toString(), tv);

		return tv;
	}

	private ImageView getIamgeView() {
		try {
			ImageView iv = new ImageView(con);
			LayoutParams lp = new LayoutParams(display.getWidth(),
					display.getHeight() / 8);
			iv.setLayoutParams(lp);
			ByteArrayInputStream imageStream = new ByteArrayInputStream(
					questionObject.getPictureData());
			Bitmap theImage = BitmapFactory.decodeStream(imageStream);
			iv.setImageBitmap(theImage);
			viewId = getViewId(viewId);
			iv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
				}
			});
			iv.setId(viewId);
			iv.setTag(questionObject.getDataID());
			return iv;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private TextView getLinkedTextView() {
		TextView tv = new TextView(con);
		final String str = questionObject.getUrlContent();
		tv.setText(Html.fromHtml("<a href=\"http://www.google.com\">" + str
				+ "</a> "));
		tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse(Helper
						.getValidURL(str)));
				con.startActivity(browse);
			}
		});
		viewId = getViewId(viewId);
		tv.setId(viewId);
		tv.setTag(questionObject.getDataID());
		setFontSize(tv);
		return tv;
	}

	private View getViewByObjectType(int type, boolean isPrev) {
		switch (type) {
		case 2:
			return getTextView();
		case 3:
			return getIamgeView();
		case 7:
			break;
		case 8:
			return getLinkedTextView();
		case 9:
			// questionnaireLayout.addView(displayLabel(questionObject.getWorkerInputCaption()),
			// getLayoutParam(viewId-1));
			// displayLabel(questionObject.getWorkerInputCaption())
			return getDropDownView(null, null, listWorkers, isPrev);
		case 10:
			// questionnaireLayout.addView(displayLabel(questionObject.getBranchInputCaption()),
			// getLayoutParam(viewId-1));
			// displayLabel(questionObject.getBranchInputCaption());
			return getDropDownView(null, listBranches, null, isPrev);
		}
		return null;
	}

	private int getSDK() {
		String str = android.os.Build.VERSION.SDK;
		int sdk = Integer.valueOf(str);
		return sdk;
	}

	private int setAlignmentOfView(int align) {
		switch (align) {
		case 1:
			return (RelativeLayout.ALIGN_PARENT_RIGHT);
		case 2:
			return (RelativeLayout.CENTER_HORIZONTAL);
		case 3:
			return (RelativeLayout.ALIGN_PARENT_LEFT);
		}
		return (RelativeLayout.ALIGN_PARENT_LEFT);
	}

	private RelativeLayout.LayoutParams getEditTextLayoutParam(int id,
			boolean isNumber) {
		if (isNumber) {
			if (sixtyTwo < 50) {
				RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
						(int) (display.getWidth() * 25.0 / 100.0),
						display.getHeight() / 10);
				lp.addRule(setAlignmentOfView(Helper.getInt(questionObject
						.getAlign())));
				lp.addRule(RelativeLayout.BELOW, id);
				return lp;
			} else {
				RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
						(int) (display.getWidth() * sixtyTwo / 100.0),
						RelativeLayout.LayoutParams.WRAP_CONTENT);
				lp.addRule(setAlignmentOfView(Helper.getInt(questionObject
						.getAlign())));
				lp.addRule(RelativeLayout.BELOW, id);
				return lp;
			}
		} else {
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
					(int) (display.getWidth() * sixtyTwo / 100.0),
					display.getHeight() / 6);
			lp.addRule(setAlignmentOfView(Helper.getInt(questionObject
					.getAlign())));
			lp.addRule(RelativeLayout.BELOW, id);
			return lp;
		}
	}

	private View getEditTextView(int type, boolean isPrev) {

		final TextView txtView = new TextView(con);

		String color = "#00FF00";

		textbox = new EditText(con);
		textbox.setTextColor(R.color.black);
		thisQGroup.setTextbox(textbox);
		if (order!=null && order.getIsJobInProgressOnServer()!=null
				&& order.isDataIdEnabled(set,questionObject.getDataID())) {
			if (btextbox!=null) btextbox.setEnabled(false);
			if (textbox!=null) textbox.setEnabled(false);
		}
		viewId = getViewId(viewId);
		textbox.setId(viewId);
		textbox.setOnFocusChangeListener(onKeyBoardOn);
		if (modeSelect == 3) {
			textbox.setGravity(Gravity.RIGHT);
		}
		String str = android.os.Build.VERSION.SDK;
		int sdk = Integer.valueOf(str);
		if (isPrev) {
			QuestionnaireData qd = getSingleAnswer(questionObject.getDataID());
			if (qd != null)
				textbox.setText(Helper.makeHtmlString(qd.getAnswerText()));
		}

		setFontSize(textbox);
		txtCurrent = textbox;
		if (sdk >= 14) {
			txtCurrent.requestFocus();
			txtCurrent.postDelayed(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					{
						InputMethodManager keyboard = (InputMethodManager) con
								.getSystemService(Context.INPUT_METHOD_SERVICE);
						keyboard.showSoftInput(txtCurrent, 0);
					}
				}
			}, 50);

			txtCurrent.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					InputMethodManager keyboard = (InputMethodManager) con
							.getSystemService(Context.INPUT_METHOD_SERVICE);
					keyboard.showSoftInput(textbox, 0);
				}
			});
		}
		switch (type) {
		case 0:
			textbox.setVisibility(RelativeLayout.GONE);
			break;
		case 1:
			textbox.setInputType(InputType.TYPE_CLASS_TEXT
					| InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
			textbox.setSingleLine(false);
			textbox.setLines(4); // desired number of lines
			textbox.setHorizontallyScrolling(false);

			textbox.setRawInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
			txtView.setTextSize(12);
			textbox.setTag(questionObject);

			textbox.addTextChangedListener(new CustomTextWatcher(textbox) {

				@Override
				public void afterTextChanged(Editable s) {
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
				}

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {

					String color = "#00FF00";
					count = s.length();
					Objects questionObject = (Objects) mEditText.getTag();
					String txt = "";
					if (Helper.getInt(questionObject.getMiFreeTextMinlength()) == 0
							&& (Helper.getInt(questionObject
									.getMiFreeTextMaxlength()) == 0 || Helper
									.getInt(questionObject
											.getMiFreeTextMaxlength()) == Integer.MAX_VALUE)) {
						txtView.setVisibility(RelativeLayout.INVISIBLE);
					} else if (Helper.getInt(questionObject
							.getMiFreeTextMinlength()) == 0) {
						// <string name="questionnaire_min_zero_max">#MAX#
						// characters
						// left.</string>
						txt = con
								.getString(R.string.questionnaire_min_zero_max);

					} else if (Helper.getInt(questionObject
							.getMiFreeTextMaxlength()) == 0
							|| Helper.getInt(questionObject
									.getMiFreeTextMaxlength()) == Integer.MAX_VALUE) {
						// <string name="questionnaire_min_max">Minimum #MIN#
						// characters.</string>
						txt = con.getString(R.string.questionnaire_min_max);
					} else {
						// <string name="questionnaire_min_max_zero">#MAX#
						// characters
						// left.
						// Minimum #MIN# characters.</string>
						txt = con
								.getString(R.string.questionnaire_min_max_zero);
					}

					txt = txt.replace(
							"#MAX#",
							(Helper.getInt(questionObject
									.getMiFreeTextMaxlength()) - count) + "");
					txt = txt.replace("#MIN#",
							questionObject.getMiFreeTextMinlength());
					txtView.setText(txt);
					txtView.setTextColor(Color.parseColor("#993f6c"));

				}
			});

			String txt = "";
			if (Helper.getInt(questionObject.getMiFreeTextMinlength()) == 0
					&& (Helper.getInt(questionObject.getMiFreeTextMaxlength()) == 0 || (Helper
							.getInt(questionObject.getMiFreeTextMaxlength()) == Integer.MAX_VALUE))) {
				txtView.setVisibility(RelativeLayout.INVISIBLE);
			} else if (Helper.getInt(questionObject.getMiFreeTextMinlength()) == 0) {
				// <string name="questionnaire_min_zero_max">#MAX# characters
				// left.</string>
				txt = con.getString(R.string.questionnaire_min_zero_max);

			} else if (Helper.getInt(questionObject.getMiFreeTextMaxlength()) == 0
					|| Helper.getInt(questionObject.getMiFreeTextMaxlength()) == Integer.MAX_VALUE) {
				// <string name="questionnaire_min_max">Minimum #MIN#
				// characters.</string>
				txt = con.getString(R.string.questionnaire_min_max);
			} else {
				// <string name="questionnaire_min_max_zero">#MAX# characters
				// left.
				// Minimum #MIN# characters.</string>
				txt = con.getString(R.string.questionnaire_min_max_zero);
			}
			txt = txt
					.replace(
							"#MAX#",
							(Helper.getInt(questionObject
									.getMiFreeTextMaxlength()) - (textbox
									.getText().toString().length()))
									+ "");
			txt = txt.replace("#MIN#", questionObject.getMiFreeTextMinlength());
			txtView.setText(txt);
			txtView.setTextColor(Color.parseColor("#993f6c"));

			textbox.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
					Helper.getInt(questionObject.getMiFreeTextMaxlength())) });

			if (textbox != null && sdk < 14)
				return textbox;
			break;
		case 2:
		case 5:
			txtView.setVisibility(RelativeLayout.INVISIBLE);
			textbox.setInputType(InputType.TYPE_CLASS_NUMBER
					| InputType.TYPE_NUMBER_FLAG_DECIMAL);
			// textbox.setText("");
			textbox.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View arg0) {
					Calculation c = new Calculation((EditText) arg0);
					c.makeDialog(con);
					return false;
				}
			});

			textbox.addTextChangedListener(new TextWatcher() {

				@Override
				public void afterTextChanged(Editable s) {
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
				}

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int ccount) {

					String color = "#00FF00";
					double count = 0.0;
					if (s.length() != 0) {
						try {
							count = Double.parseDouble(s.toString().toString());
						} catch (Exception ex) {

						}
					}

					String txt = "";
					txt = txt.replace("#MIN#", questionObject.getMiNumberMin());
					txtView.setText(txt);
					txtView.setTextColor(Color.parseColor("#993f6c"));

				}
			});
			double cccount = 0.0;
			if (textbox.getText().toString().length() > 0) {
				try {
					cccount = Double.parseDouble(textbox.getText().toString()
							.toString());
				} catch (Exception ex) {

				}
			}

			txt = "";
			txtView.setVisibility(RelativeLayout.INVISIBLE);
			break;
		case 3:
			textbox.setFocusable(false);
			textbox.setInputType(InputType.TYPE_CLASS_DATETIME);
			ShowDatePicker();
			break;
		case 4:
		case 6:
		case 7:
		case 8:
			textbox.setFocusable(false);
			textbox.setInputType(InputType.TYPE_CLASS_DATETIME);
			showTimePickerSeconds(textbox);
			break;
		}

		if (questionObject.getMiType().equals("11")) {
			SimpleDateFormat sdf = new SimpleDateFormat("kk:mm:ss",
					Locale.ENGLISH);
			String time = sdf.format(new Date().getTime());
			textbox.setText(time);
			textbox.setTextColor(Color.BLACK);
			textbox.setVisibility(RelativeLayout.GONE);
		}
		if (questionObject.getMiType().equals("12")) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",
					Locale.ENGLISH);
			String time = sdf.format(new Date().getTime());
			textbox.setText(time);
			textbox.setTextColor(Color.BLACK);
			textbox.setVisibility(RelativeLayout.GONE);
		}

		LinearLayout hebrewLayout = new LinearLayout(con);
		hebrewLayout.setOrientation(LinearLayout.VERTICAL);
		if (modeSelect == 3) {
			txtView.setGravity(Gravity.RIGHT);
		}
		hebrewLayout.setId(viewId);
		hebrewLayout.addView(textbox,
				new LinearLayout.LayoutParams(
						android.view.ViewGroup.LayoutParams.MATCH_PARENT,
						display.getHeight() / 13, Gravity.LEFT));

		LinearLayout.LayoutParams lpp = new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.RIGHT);
		hebrewLayout.addView(txtView, lpp);

		return hebrewLayout;
	}

	private View getEditTextViewOld(int type, boolean isPrev) {
		// Toast.makeText(con, "Showing TextBox", Toast.LENGTH_LONG).show();
		textbox = new EditText(con);
		textbox.setTextColor(R.color.black);
		if (firstEditText == null)
			firstEditText = textbox;
		textbox.setGravity(Gravity.TOP);
		viewId = getViewId(viewId);
		textbox.setId(viewId);
		textbox.setOnFocusChangeListener(onKeyBoardOn);
		thisQGroup.setTextbox(textbox);
		if (order!=null && order.getIsJobInProgressOnServer()!=null
				&& order.isDataIdEnabled(set,questionObject.getDataID())) {
			if (btextbox!=null) btextbox.setEnabled(false);
			if (textbox!=null) textbox.setEnabled(false);
		}
		String str = android.os.Build.VERSION.SDK;
		int sdk = Integer.valueOf(str);
		// Toast.makeText(con, "before previous", Toast.LENGTH_LONG).show();
		if (isPrev) {

			// Toast.makeText(con, questionObject.getDataID(),
			// Toast.LENGTH_LONG).show();
			QuestionnaireData qd = getSingleAnswer(questionObject.getDataID());
			if (qd != null) {
				// Toast.makeText(con, qd.getAnswerText(), Toast.LENGTH_LONG)
				// .show();
				if (qd != null && qd.getAnswerText() != null
						&& !qd.getAnswerText().equals(""))
					textbox.setText(Helper.makeHtmlString(qd.getAnswerText()));
				else if (qd != null && qd.getMi() != null
						&& !qd.getMi().equals(""))
					textbox.setText(Helper.makeHtmlString(qd.getMi()));
				else if (qd != null && qd.getFreetext() != null
						&& !qd.getFreetext().equals(""))
					textbox.setText(Helper.makeHtmlString(qd.getFreetext()));
				else if (qd != null && qd.getAnsText() != null
						&& !qd.getAnsText().equals(""))
					textbox.setText(Helper.makeHtmlString(qd.getAnsText()));
			}
		}

		setFontSize(textbox);
		txtCurrent = textbox;
		if (getSDK() >= 14) {
			txtCurrent.requestFocus();
			txtCurrent.postDelayed(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					{
						InputMethodManager keyboard = (InputMethodManager) con
								.getSystemService(Context.INPUT_METHOD_SERVICE);
						keyboard.showSoftInput(txtCurrent, 0);
					}
				}
			}, 10);
		}

		switch (type) {
		case 1:
			textbox.setInputType(InputType.TYPE_CLASS_TEXT
					| InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
			textbox.setSingleLine(false);
			textbox.setLines(4); // desired number of lines
			textbox.setHorizontallyScrolling(false);
			textbox.setRawInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
			// textbox.setOnClickListener(new OnClickListener( ) {
			//
			// @Override
			// public void onClick(View arg0) {
			// // TODO Auto-generated method stub
			// EditText ed=(EditText)arg0;
			// ed.setInputType(InputType.TYPE_CLASS_TEXT);
			// InputMethodManager m = (InputMethodManager)
			// getSystemService(Context.INPUT_METHOD_SERVICE);
			// if (m != null) {
			// m.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT);
			// }
			// }
			// });
			if (textbox != null && sdk < 14)
				return textbox;
			break;
		case 2:
		case 5:
			textbox.setInputType(InputType.TYPE_CLASS_NUMBER
					| InputType.TYPE_NUMBER_FLAG_DECIMAL);
			// textbox.setText("");
			textbox.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View arg0) {
					Calculation c = new Calculation((EditText) arg0);
					c.makeDialog(con);
					return false;
				}
			});

			// textbox.setOnClickListener(new OnClickListener( ) {
			//
			// @Override
			// public void onClick(View arg0) {
			// // TODO Auto-generated method stub
			// EditText ed=(EditText)arg0;
			// ed.setInputType(InputType.TYPE_CLASS_NUMBER
			// | InputType.TYPE_NUMBER_FLAG_DECIMAL);
			// InputMethodManager m = (InputMethodManager)
			// getSystemService(Context.INPUT_METHOD_SERVICE);
			// if (m != null) {
			// m.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT);
			// }
			// }
			// });
			if (textbox != null && sdk < 14)
				return textbox;
			break;
		case 3:
			textbox.setFocusable(false);
			textbox.setInputType(InputType.TYPE_CLASS_DATETIME);
			ShowDatePicker();
			break;
		case 4:
			textbox.setFocusable(false);
			textbox.setInputType(InputType.TYPE_CLASS_DATETIME);
			showTimePickerSeconds(textbox);
			break;
		}
		textbox.clearFocus();
		return textbox;
	}

	private ImageView displayupdownicon(Drawable drawable) {
		// Constants.QUESTIONNAIRE_JUMPTO_LBL
		ImageView tv = new ImageView(con);
		tv.setBackgroundDrawable(drawable);
		viewId = getViewId(viewId);
		tv.setId(viewId);
		return tv;
	}

	private void addView(View v) {
		if (v == null)
			currentViews = new ArrayList<View>();
		currentViews.add(v);
	}

	public static ArrayList<dataQuestionGroup> qGroups = new ArrayList<dataQuestionGroup>();
	private ArrayList<Titles> titles;

	// int objectCount = 0;
	View questionView = null;
	private ArrayList<Objects> listObjects;
	private boolean isGroupShown = false;
	private int questionNumber;
	private double sixtyTwo = 62.0;

	private String getGroupIdFromDataId(String data_id) {

		if (data_id.contains("-")) {
			data_id = data_id.substring(0, data_id.indexOf("-"));
		}
		return data_id;
	}

	private String getDataIdFromDataId(String data_id) {

		if (data_id.contains("_")) {
			data_id = data_id.substring(0, data_id.indexOf("_"));
		}
		return data_id;
	}

	private String getTitleFromDataId(String data_id) {

		if (data_id.contains(";")) {
			data_id = data_id.substring(0, data_id.indexOf(";"));
		}
		return data_id;
	}

	private String getAcualTitleFromDataId(String data_id) {

		if (data_id.contains(";")) {
			data_id = data_id.substring(data_id.indexOf(";") + 1);
		}
		return data_id;
	}

	private String convertDataIdForNameValuePair(String dataID2) {
		// TODO Auto-generated method stub
		if (dataID2.contains("_")) {

			String dataId = getDataIdFromDataId(dataID2);
			String groupId = getGroupIdFromDataId(dataID2.replace(dataId + "_",
					""));
			if (dataID2.contains("-")) {
				String titleId = getTitleFromDataId(dataID2.replace(dataId
						+ "_" + groupId + "-", ""));
				if (dataID2.contains(";")) {
					String title = getAcualTitleFromDataId(dataID2.replace(
							dataId + "_" + groupId + "-" + titleId, ""));
					return title;
				}
				return titleId;
			}
		}

		return null;
	}

	private boolean isTitleDisplayConditionValid(String dataID2) {
		// TODO Auto-generated method stub
		if (dataID2.contains("_")) {

			String dataId = getDataIdFromDataId(dataID2);
			String groupId = getGroupIdFromDataId(dataID2.replace(dataId + "_",
					""));
			if (dataID2.contains("-")) {
				String titleId = getTitleFromDataId(dataID2.replace(dataId
						+ "_" + groupId + "-", ""));
				for (int i = 0; i < titles.size(); i++) {
					if (titles.get(i).getqgtID().equals(titleId))
						return true;
				}
			}
		}

		return false;
	}

	private static ArrayList<Objects> randomizeQuestions(
			ArrayList<Objects> listObjects, int seed) {
		ArrayList<Objects> randomizeList = new ArrayList<Objects>();
		Calendar calendar = Calendar.getInstance();
		System.out.println("Seconds in current minute = "
				+ calendar.get(Calendar.SECOND));

		if (seed < 0) {
			seed = Helper.seed = calendar.get(Calendar.SECOND);
		}
		Random randomGenerator = new Random(seed);
		int size = listObjects.size();
		while (randomizeList.size() != size) {
			int n = randomGenerator.nextInt(listObjects.size());
			randomizeList.add(listObjects.get(n));
			listObjects.remove(listObjects.get(n));
		}

		return randomizeList;
	}

	public ArrayList<Objects> randomizeObjects(ArrayList<Objects> inputList,
			Boolean shouldDo, int seed) {
		if (!shouldDo)
			return inputList;
		return randomizeQuestions(inputList, seed);
		// return inputList;
	}

	int theTotalWidth = -1;
	private RelativeLayout qtitleLayout;

	public LinearLayout prepareGroupLayout(Display display, Helper helper,
			int modeSelect, Set set, Order order,
			ArrayList<QuestionnaireData> questionnaireData, final Context con,
			ArrayList<Objects> listObjects, ScrollView subLayout,
			RelativeLayout questionnaireLayou, ArrayList<Titles> titles,
			Boolean isRandomTitles, Boolean isRandomQuestions,
			int questionNumber, ImageView imgPlus, ImageView imgMinus) {
		qGroups = new ArrayList<dataQuestionGroup>();
		isRadioCheck = false;
		ultimateWidth = 0.0;
		boolean isNumericOnly = true;
		for (int i = 0; i < listObjects.size(); i++) {
			if (listObjects.get(i).getMiType().equals("2")
					|| listObjects.get(i).getMiType().equals("5")) {

			} else {
				isNumericOnly = false;
				break;
			}
		}
		//
		// if (isNumericOnly)
		// sixtyTwo = 20.0f;
		if (imgPlus != null) {
			imgPlus.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					hr.scale(Float.parseFloat(hr.mScaleFactor + 0.1 + ""),
							hr.mPivotX, hr.mPivotY);
				}
			});
			imgPlus.setVisibility(RelativeLayout.GONE);
		}
		if (imgMinus != null) {
			imgMinus.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					hr.scale(Float.parseFloat(hr.mScaleFactor - 0.1 + ""),
							hr.mPivotX, hr.mPivotY);
				}
			});
			imgMinus.setVisibility(RelativeLayout.GONE);
		}

		this.questionNumber = questionNumber;
		firstEditText = null;
		this.display = display;
		this.modeSelect = modeSelect;
		this.helper = helper;
		this.con = con;
		this.set = set;
		this.titles = titles;
		// listObjects = set.getListObjects();
		listBranches = set.getListBranches();
		listWorkers = set.getListWorkers();
		companyLink = set.getCompanyLink();
		this.order = order;
		if (this.titles != null && this.titles.size() > 0) {
			this.listObjects = listObjects;
		} else
			this.listObjects = listObjects = randomizeObjects(listObjects,
					isRandomQuestions, Helper.seed);
		this.questionnaireData = questionnaireData;
		spinnerFirstTime = true;
		enableValidationQuestion = show_attachedfilescreen = false;
		viewId = 100;
		tempId = 0;
		isKeyboardOn = false;
		isNextObj = true;
		this.questionnaireLayoutt = questionnaireLayou;

		TableLayout MainLayout = new TableLayout(con);
		MainLayout.setLayoutParams(new TableRow.LayoutParams(
				TableRow.LayoutParams.WRAP_CONTENT,
				TableRow.LayoutParams.FILL_PARENT));
		MainLayout.setStretchAllColumns(true);
		RelativeLayout questionnaireLayout = new RelativeLayout(con);

		ArrayList<TableRow> tblRows = new ArrayList<TableRow>();
		if (this.titles == null || this.titles.size() == 0) {
			tblRows = simpleTwoRowQuestions(2, tblRows, MainLayout);
		} else {
			tblRows = simpleTitleRows(2, tblRows, MainLayout);
		}
		// tblRows = simpleTwoRowQuestions(2, tblRows, MainLayout);
		int size = listObjects.size();
		int skipObjCount = 0;
		isHideAdditionalInfoSingleChoiceAnswers = 0;
		isMandatorySingleChoiceAnswers = 0;

		int viewOnPage = 1;
		// dataQuestionGroup thisQGroup = null;
		int objectCount = 0;
		String thisTitle = null;

		for (objectCount = 0, viewOnPage = 1; objectCount < size; objectCount++, viewOnPage++) {
			ultimateWidth = display.getWidth() * (sixtyTwo / 100.0);
			isSplit = false;
			showSubmitMenu = false;
			questionObject = listObjects.get(objectCount);

			boolean isTitleDisplayShown = true;
			if (convertDataIdForNameValuePair(questionObject.getDataID()) != null) {
				thisTitle = convertDataIdForNameValuePair(questionObject
						.getDataID());
				if (!isTitleDisplayConditionValid(questionObject.getDataID())) {
					thisTitle = null;
					isTitleDisplayShown = false;
					// qGroups.remove(thisQGroup);
				}
			} else {
				thisTitle = null;
				// isTitleDisplayShown = false;
				// qGroups.remove(thisQGroup);
			}
			thisQGroup = new dataQuestionGroup();

			thisQGroup.setMandatory(questionObject != null
					&& questionObject.getMandatory() != null
					&& questionObject.getMandatory().equals("1"));
			thisQGroup.setQbjecttype(questionObject.getObjectType());
			thisQGroup.setQuestionObject(questionObject);
			// if (!isTitleDisplayShown
			// && (questionObject.getObjectDisplayCondition() == null ||
			// questionObject
			// .getObjectDisplayCondition().equals(""))) {
			// questionObject.setObjectDisplayCondition("1=43");
			// } else if (questionObject.getObjectDisplayCondition() != null) {
			// // questionObject.setObjectDisplayCondition("");
			// }

			// if (!isTitleDisplayShown) {
			// questionObject.setObjectDisplayCondition("1=2");
			// }

			thisQGroup.setObjcode(questionObject.getObjectCode());
			thisQGroup.setOrderID(this.order.getOrderID());
			if (questionObject.getObjectType().equals("9")
					|| questionObject.getObjectType().equals("10")) {
				thisQGroup.setQtype(0);
				thisQGroup.setDisplayType(0);
			} else if (questionObject.getObjectType().equals("15")) {
				thisQGroup.setQtype(0);
				thisQGroup.setDisplayType(15);

			} else {
				if (questionObject.getQuestionTypeLink() == null
						|| questionObject.getDisplayType() == null) {
				} else {
					thisQGroup.setQtype(Integer.valueOf(questionObject
							.getQuestionTypeLink()));
					thisQGroup.setDisplayType(Integer.valueOf(questionObject
							.getDisplayType()));
				}
			}

			thisQGroup.setDisplayType(Integer.valueOf(questionObject
					.getDisplayType()));

			// quotaQuestions = getQuotaQuestions(questionObject.getDataID());
			// try {
			isHideAdditionalInfoSingleChoiceAnswers = 0;
			isMandatorySingleChoiceAnswers = 0;

			if (!IsObjectdisplaybyCondition(
					questionObject.getObjectDisplayCondition(), false)
					|| !isTitleDisplayShown) {
				// questionObject.setObjectDisplayCondition("1=2");
				skipObjCount++;
				if (!DataID.equals("")) {
					if (DataID.equals(questionObject.getDataID()))
						DataID = "";
					else
						continue;
				}
				continue;
			} else
			// questionObject.setObjectDisplayCondition("1=1");

			if (Helper.comapreString(questionObject.getObjectType(), "7")) {
				DataID = questionObject.getDestinationObject();
				continue;
			}
			if (!DataID.equals("")) {
				if (DataID.equals(questionObject.getDataID()))
					DataID = "";
				else
					continue;
			}

			qGroups.add(thisQGroup);

			if (Helper.comapreString(questionObject.getObjectType(), "4")
					|| (!Helper.IsEmptyString(questionObject
							.getQuestionTypeLink()))) {
				isNextObj = true;
				// if (questionObject.getQuestion().contains("$[text]$")) {
				// getSplitQuestion(questionObject, questionnaireLayout);
				// continue;
				// }
				int tempId = viewId;
				if (tempId < 0)
					tempId = 0;
				questionnaireLayout.addView(getGroupNamevView("", "", viewId),
						getGroupLayoutParam(tempId));
				// questionnaireLayout
				// .addView(
				// getHiddenEditView(listObjects.get(0)
				// .getGroupName(), listObjects.get(0)
				// .getObjectCode(), viewId),
				// getGroupLayoutParam(tempId));

				if (!isGroupShown) {
					isGroupShown = true;

				}

				tempId = viewId;
				if (tempId < 0)
					tempId = 0;
				questionnaireLayout.addView(getQuestionView(viewId, false),
						getQuestionLayoutParam(tempId));

				if (questionObject.getAttachment().equals("110")
						|| questionObject.getAttachment().equals("210")) {
					// questionnaireLayout.addView(getattachmentView(),
					// getLayoutParamRight());
					// questionnaireLayout.addView(commentTextView("    ")
					// getLayoutParam(viewId-1));
					// RelativeLayout questionnaireLayout=new
					// RelativeLayout(con);
					questionnaireLayout.addView(getattachmentView(),
							getAttachmentLayoutParam(viewId - 1));
					TextView tv = commentTextView(con.getResources().getString(
							R.string.questionnaire_select_files)
							+ "\n");
					tv.setTag(questionObject.getDataID());
					setFontSize(tv);
					tv.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							// showFileChooserphotomenu = true;
							((Activity) con).openOptionsMenu();
						}
					});
					questionnaireLayout.addView(tv,
							getAttechmentLayout(viewId - 1));

				}
				View v = getQuestionDescriptionView();
				v.setTag(questionObject.getDataID());
				questionnaireLayout.addView(v, getLayoutParam(viewId - 1));
				if (questionObject.getListAnswers() != null
						&& questionObject.getListAnswers().size() > 0
						&& !questionObject.getDisplayType().equals("3")
						&& !questionObject.getDisplayType().equals("2")
						&& !questionObject.getDisplayType().equals("1")
						&& !questionObject.getDisplayType().equals("5")) {
					tempId = viewId;
					v = writeAnswersHorizontally(
							Helper.getInt(questionObject.getDisplayType()),
							Helper.getInt(questionObject.getQuestionTypeLink()),
							questionObject.getListAnswers());
					int totalAnswers = 0;
					for (ansCount = 0; ansCount < questionObject
							.getListAnswers().size(); ansCount++) {
						Answers answers = questionObject.getListAnswers().get(
								ansCount);
						if (answers.getAnswerDisplayCondition() != null) {
							Boolean b = IsObjectdisplaybyCondition(
									answers.getAnswerDisplayCondition(), false);
							if (!b)
								continue;
						}
						totalAnswers++;
					}
					double width = 0.0;
					width = display.getWidth();

					if (totalAnswers <= Constants.getAnswerCount(con)) {
						width = width * (sixtyTwo / 100.0);
					} else {
						width = ((width * (sixtyTwo / 100.0) / Constants
								.getAnswerCount(con)) * totalAnswers);
					}
					ultimateWidth = width;
					// double width = display.getWidth() * (sixtyTwo /
					// 100.0);
					RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
							(int) width,
							android.view.ViewGroup.LayoutParams.MATCH_PARENT);
					// lp.addRule(setAlignmentOfView(Helper.getInt(questionObject.getAlign())));

					questionnaireLayout = new RelativeLayout(con);
					questionnaireLayout.addView(
							getQuestionView(viewId, false),
							getQuestionLayoutParamWidth(tempId,
									(int) ultimateWidth));
					tempId = viewId;
					// viewId = getViewId(viewId);
					questionnaireLayout.addView(
							getQuestionDescriptionView(),
							getQuestionLayoutParamWidth(tempId,
									(int) ultimateWidth));

					lp.addRule(RelativeLayout.BELOW, viewId);
					lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,
							RelativeLayout.TRUE);
					questionnaireLayout.addView(v, lp);
				}

				qtitleLayout = questionnaireLayout;
				questionnaireLayout = new RelativeLayout(con);
				// questionnaireLayout = addRow(objectCount, tblRows,
				// questionnaireLayout, true, null, thisTitle);
				View answerview = getAnswersView(true);
				// //answerview.setTag(questionObject.getDataID());
				if (// !questionObject.getQuestionTypeLink().equals("4")&&
				answerview != null) {
					questionnaireLayout.addView(answerview,
							getLayoutParam(v.getId()));
				}
				if (Helper.getInt(questionObject.getShowCritical()) == 1)
					questionnaireLayout.addView(getShowCriticalView(),
							getLayoutParam(viewId - 1));
				if (Helper.getInt(questionObject.getMiType()) > 0) {
					TextView tv = getMiView();
					tv.setTag(questionObject.getDataID());
					thisQGroup.setMiTextView(tv);
					questionnaireLayout.addView(tv, getLayoutParam(viewId - 1));
				}

				if (Helper.getInt(questionObject.getMiType()) > 0
						&& !(questionObject.getQuestionTypeLink().equals("4"))) {
					questionnaireLayout.addView(
							getMiView(
									Helper.getInt(questionObject.getMiType()),
									true),
							getEditTextLayoutParam(viewId - 1, (questionObject
									.getMiType().equals("2") || questionObject
									.getMiType().equals("5"))));
					questionObject.setMiText(true);
				} else if (questionObject.getQuestionTypeLink().equals("4")) {
					questionnaireLayout.addView(
							getEditTextView(
									Helper.getInt(questionObject.getMiType()),
									true),
							getEditTextLayoutParam(viewId - 1, (questionObject
									.getMiType().equals("2") || questionObject
									.getMiType().equals("5"))));
				}
				// questionnaireLayout = checkForNextBtnVisibility(
				// questionnaireLayout, isPrev);
				if (modeSelect == 3 && titles != null && titles.size() > 0) {

					// if (titleCount == 0)
					{
						qtitleLayout = addRow(objectCount, tblRows,
								qtitleLayout, true, null, thisTitle);
					}
					questionnaireLayout = addRow(objectCount, tblRows,
							questionnaireLayout, false, questionView, thisTitle);

				} else {
					qtitleLayout = addRow(objectCount, tblRows, qtitleLayout,
							true, null, thisTitle);
					questionnaireLayout = addRow(objectCount, tblRows,
							questionnaireLayout, false, questionView, thisTitle);
				}
				continue;
			} else {
				if (Helper.IsValidObject(questionObject.getObjectType())) {
					int type = Integer.parseInt(questionObject.getObjectType());
					View view = null;
					// if (type == 15) {
					// subLayout.setVisibility(View.GONE);
					// posLayout.setVisibility(View.VISIBLE);
					// layout_shelf_study = preparePOSProductList(true,
					// layout_shelf_study);
					// layout_shelf_study = preparePOSLocationList(true,
					// layout_shelf_study);
					// layout_shelf_study =
					// prepareSubSections(layout_shelf_study);
					// layout_shelf_study =
					// prepareToggleButtons(layout_shelf_study);
					//
					// showSelectedDataPOS();
					// if (selectedProduct > 0 && selectedLocation > 0) {
					// txtAddQuantity.requestFocus();
					// txtAddQuantity.postDelayed(new Runnable() {
					// @Override
					// public void run() {
					// // TODO Auto-generated method stub
					// if (selectedProduct > 0
					// && selectedLocation > 0) {
					// InputMethodManager keyboard = (InputMethodManager)
					// getSystemService(Context.INPUT_METHOD_SERVICE);
					// keyboard.showSoftInput(
					// txtAddQuantity, 0);
					// }
					// }
					// }, 10);
					// }
					// productListView.setVisibility(View.GONE);
					// locationListView.setVisibility(View.GONE);
					// continue;
					// }
					if (type == 9) {

						thisQGroup.setWorkerListView(workerListView);

						thisQGroup.setWorkerListView(workerListView);
						view = displayLabel(questionObject
								.getWorkerInputCaption());
						view.setTag(questionObject.getDataID());
						questionnaireLayout.addView((view),
								getElseLayoutParam());
						workerBranchImage = displayupdownicon(con
								.getResources()
								.getDrawable(R.drawable.down_btn));
						workerBranchImage
								.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										if (workerListView.getVisibility() == View.GONE) {
											workerListView
													.setVisibility(View.VISIBLE);
											workerBranchImage
													.setBackgroundDrawable(con
															.getResources()
															.getDrawable(
																	R.drawable.up_btn));

										} else {
											workerListView
													.setVisibility(View.GONE);
											workerBranchImage
													.setBackgroundDrawable(con
															.getResources()
															.getDrawable(
																	R.drawable.down_btn));
										}

									}
								});
						questionnaireLayout.addView((workerBranchImage),
								getIconParam());
						workerEditText = displayListEdit("");
						questionnaireLayout.addView((workerEditText),
								getSearchEditTextLayoutParam());

						viewId = getViewId(viewId);
						workerListView = getWorkerListView(viewId,
								questionObject);
						workerListView.setVisibility(View.GONE);
						workerListView.setCacheColorHint(Color.TRANSPARENT);
						workerListView
								.setOnTouchListener(new ListView.OnTouchListener() {

									@Override
									public boolean onTouch(View arg0,
											MotionEvent arg1) {
										int action = arg1.getAction();
										switch (action) {
										case MotionEvent.ACTION_DOWN:
											// Disallow ScrollView to
											// intercept touch events.
											arg0.getParent()
													.requestDisallowInterceptTouchEvent(
															true);
											break;

										case MotionEvent.ACTION_UP:
											// Allow ScrollView to intercept
											// touch events.
											arg0.getParent()
													.requestDisallowInterceptTouchEvent(
															false);
											break;
										}

										// Handle ListView touch events.
										arg0.onTouchEvent(arg1);
										return true;
									}
								});

						workerEditText
								.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										// workerEditText.setText("");
										if (workerListView.getVisibility() == View.GONE) {
											workerListView
													.setVisibility(View.VISIBLE);
											workerBranchImage
													.setBackgroundDrawable(con
															.getResources()
															.getDrawable(
																	R.drawable.up_btn));

										}
									}
								});

						workerEditText
								.addTextChangedListener(new TextWatcher() {

									private int workerListViewListener;

									@Override
									public void onTextChanged(CharSequence cs,
											int arg1, int arg2, int arg3) {
										// When user changed the Text
										if (workerListViewListener != -1) {
											((ArrayAdapter) (workerListView
													.getAdapter())).getFilter()
													.filter(cs);
											if (workerListView.getVisibility() == View.GONE) {
												workerListView
														.setVisibility(View.VISIBLE);
												workerBranchImage
														.setBackgroundDrawable(con
																.getResources()
																.getDrawable(
																		R.drawable.up_btn));

											}
										} else
											workerListViewListener = 0;

									}

									@Override
									public void beforeTextChanged(
											CharSequence arg0, int arg1,
											int arg2, int arg3) {
										// TODO Auto-generated method stub

									}

									@Override
									public void afterTextChanged(Editable arg0) {
										// TODO Auto-generated method stub

									}
								});

						questionnaireLayout.addView(workerListView,
								getListLayoutParam());
						tempId = view.getId();
						questionnaireLayout = addRow(objectCount, tblRows,
								questionnaireLayout, false, questionView,
								thisTitle);
						continue;
					} else if (type == 10) {
						view = displayLabel(questionObject
								.getBranchInputCaption());
						questionnaireLayout.addView((view),
								getElseLayoutParam());
						workerBranchImage = displayupdownicon(con
								.getResources()
								.getDrawable(R.drawable.down_btn));
						workerBranchImage
								.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										if (branchListView.getVisibility() == View.GONE) {
											branchListView
													.setVisibility(View.VISIBLE);
											workerBranchImage
													.setBackgroundDrawable(con
															.getResources()
															.getDrawable(
																	R.drawable.up_btn));

										} else {
											branchListView
													.setVisibility(View.GONE);
											workerBranchImage
													.setBackgroundDrawable(con
															.getResources()
															.getDrawable(
																	R.drawable.down_btn));
										}

									}
								});
						questionnaireLayout.addView((workerBranchImage),
								getIconParam());
						branchEditText = displayListEdit("");

						questionnaireLayout.addView((branchEditText),
								getSearchEditTextLayoutParam());

						viewId = getViewId(viewId);
						branchListView = getBranchListView(viewId,
								questionObject);
						branchListView.setVisibility(View.GONE);
						branchListView.setCacheColorHint(Color.TRANSPARENT);
						branchListView
								.setOnTouchListener(new ListView.OnTouchListener() {

									@Override
									public boolean onTouch(View arg0,
											MotionEvent arg1) {
										int action = arg1.getAction();
										switch (action) {
										case MotionEvent.ACTION_DOWN:
											// Disallow ScrollView to
											// intercept touch events.
											arg0.getParent()
													.requestDisallowInterceptTouchEvent(
															true);
											break;

										case MotionEvent.ACTION_UP:
											// Allow ScrollView to intercept
											// touch events.
											arg0.getParent()
													.requestDisallowInterceptTouchEvent(
															false);
											break;
										}

										// Handle ListView touch events.
										arg0.onTouchEvent(arg1);
										return true;
									}
								});
						branchEditText
								.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										// branchEditText.setText("");
										if (branchListView.getVisibility() == View.GONE) {
											branchListView
													.setVisibility(View.VISIBLE);
											workerBranchImage
													.setBackgroundDrawable(con
															.getResources()
															.getDrawable(
																	R.drawable.up_btn));

										}
									}
								});

						branchEditText
								.addTextChangedListener(new TextWatcher() {

									private int workerListViewListener;

									@Override
									public void onTextChanged(CharSequence cs,
											int arg1, int arg2, int arg3) {
										// When user changed the Text
										if (workerListViewListener != -1) {
											((ArrayAdapter) (branchListView
													.getAdapter())).getFilter()
													.filter(cs);
											if (branchListView.getVisibility() == View.GONE) {
												branchListView
														.setVisibility(View.VISIBLE);
												workerBranchImage
														.setBackgroundDrawable(con
																.getResources()
																.getDrawable(
																		R.drawable.up_btn));

											}
										} else
											workerListViewListener = 0;

									}

									@Override
									public void beforeTextChanged(
											CharSequence arg0, int arg1,
											int arg2, int arg3) {
										// TODO Auto-generated method stub

									}

									@Override
									public void afterTextChanged(Editable arg0) {
										// TODO Auto-generated method stub

									}
								});

						questionnaireLayout.addView(branchListView,
								getListLayoutParam());
						tempId = view.getId();
						questionnaireLayout = addRow(objectCount, tblRows,
								questionnaireLayout, false, questionView,
								thisTitle);
						continue;
						// tempId = tv.getId();
					}
					View v = getViewByObjectType(type, true);
					v.setTag(questionObject.getDataID());
					setFontSize(v);
					if (v != null) {
						// questionnaireLayout.addView(v,
						// getElseLayoutParam());
						questionnaireLayout.addView(v, getElseLayoutParam());
						tempId = v.getId();
						questionnaireLayout = addRow(objectCount, tblRows,
								questionnaireLayout, false, questionView,
								thisTitle);
						// break;
					}
					if (Helper.comapreString(questionObject.getObjectType(),
							"9")
							|| Helper.comapreString(
									questionObject.getObjectType(), "10")) {
						continue;
					}
					// viewId = tempId;
					// viewId = getViewId(viewId);
				}

			}

			// }
			// catch (Exception e) {
			// e.printStackTrace();
			// }

		}

		if (isHideAdditionalInfoSingleChoiceAnswers > 0) {
			View v = questionnaireLayout.findViewById(miLabelViewId);
			setFontSize(v);
			if (v != null && v.getClass().equals(TextView.class))
				v.setVisibility(View.GONE);
			v = questionnaireLayout.findViewById(miEditViewId);
			setFontSize(v);
			if (v != null && v.getClass().equals(EditText.class)) {
				EditText edit = (EditText) v;
				edit.setText("");
				v.setVisibility(View.GONE);
			}
		}

		// if (set.getShowTOC().equals("1") && objectCount < (size - 1)) {
		// questionnaireLayout.addView(
		// displayLabel(Constants.QUESTIONNAIRE_JUMPTO_LBL),
		// getLayoutParam(viewId - 1));
		// questionnaireLayout.addView(displayJumptoDropdown(listString),
		// getLayoutParam(viewId - 1));
		// }
		hr = new ZoomableScrollView(con);
		hr.setId(1);
		LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);

		lp.addRule(RelativeLayout.BELOW, 0);
		hr.setLayoutParams(lp);

		questionnaireLayout = new RelativeLayout(con);
		// if (listObjects != null && listObjects.size() > 0) {
		// questionnaireLayout.addView(
		// getGroupNamevView(listObjects.get(0).getGroupName(),
		// listObjects.get(0).getObjectCode(), 0),
		// getQuestionGroupLayoutParam(0));
		// questionnaireLayout.addView(hr, getAttachmentLayoutParam(0));
		// } else
		questionnaireLayout.addView(hr, lp);

		if (firstEditText != null)
			firstEditText.requestFocus();
		LinearLayout MainLLayout = new LinearLayout(con);
		try {

			MainLLayout.setOrientation(LinearLayout.VERTICAL);
			// if (this.titles != null && this.titles.size() > 0
			// && theTotalWidth > 0) {
			//
			// hr.addView(MainLayout, new LayoutParams(
			// TableRow.LayoutParams.WRAP_CONTENT,
			// LayoutParams.FILL_PARENT));
			//
			// } else
			hr.addView(MainLayout);

			isGroupShown = false;
			MainLLayout
					.addView(getGroupNamevView(listObjects.get(0)
							.getGroupName(),
							listObjects.get(0).getObjectCode(), viewId));
			MainLLayout.addView(questionnaireLayout);
		} catch (Exception ex) {
			int i = 0;
			i++;
		}
		return MainLLayout;
	}

	private View getHiddenEditView(String groupName, String objCode, int id) {
		EditText tv = new EditText(con);
		tv.setTextColor(R.color.black);
		viewId = getViewId(id);
		tv.setId(viewId);

		tv.setFocusable(true);
		tv.requestFocus();
		return tv;

	}

	private View getGroupNamevView(String groupName, String objCode, int id) {

		groupName = ConvertTextCodetoText(groupName);
		TextView tv = new TextView(con);

		tv.setId(id);
		StringBuffer sb = new StringBuffer();

		sb.append(Helper.getValidString(groupName));
		tv = setGroupTextViewProperties(tv, groupName);
		if (set.getEnableQuestionNumberingInForm().equals("1")) {
			if (objCode.equals("") && sb.toString().equals("")) {
			} else
				tv.setText(Helper.makeHtmlString(questionNumber + ". "
						+ ConvertTextCodetoText(sb.toString())));
		} else if (set.getEnableQuestionNumberingInForm().equals("2")) {
			if (objCode != null && objCode.contains(";")) {
				objCode = objCode.substring(0, objCode.indexOf(";"));
			}
			if (objCode.equals("null")
					|| (objCode.equals("") && sb.toString().equals(""))) {
				tv.setText(Helper.makeHtmlString(sb.toString()));
			} else
				tv.setText(Helper.makeHtmlString(objCode + ". " + sb.toString()));
		} else {
			tv.setText(Helper.makeHtmlString(sb.toString()));
		}
		viewId = getViewId(id);
		tv.setGravity(Gravity.LEFT);
		tv.setId(viewId);
		if (Build.VERSION.SDK_INT > 13) {
			if (modeSelect == 3)
				tv.setGravity(Gravity.RIGHT);
		}
		setFontSize(tv);
		tv.setTextColor(con.getResources().getColor(android.R.color.black));
		if (isGroupShown)
			tv.setTextColor(con.getResources().getColor(
					android.R.color.transparent));

		tv.setPadding(20,0,20,0);
		return tv;

	}

	private ArrayList<TableRow> simpleTitleRows(int count,
			ArrayList<TableRow> trows, TableLayout layout) {
		{
			TableRow tr = new TableRow(con);
			trows.add(tr);
			layout.addView(tr);

			// layout.addView(tr);
			// if (modeSelect == 3) {
			// } else
			tr.addView(getTitleTextView(""));

		}

		ArrayList<Titles> newTitles = new ArrayList<Titles>();
		for (int j = 0; j < titles.size(); j++) {
			if (IsObjectdisplaybyCondition(titles.get(j).getDisplayCondition(),
					false)) {
				TableRow tr = new TableRow(con);
				int color1 = R.drawable.tbl_border_grey_light;

				if (j % 2 == 0) {
					color1 = R.drawable.tbl_border_grey_dark;

				}
				tr.setBackgroundDrawable(con.getResources().getDrawable(color1));
				trows.add(tr);
				layout.addView(tr);

				newTitles.add(titles.get(j));
			}
		}
		toggleColor = true;
		titles = newTitles;

		return trows;
	}

	private ArrayList<TableRow> simpleTwoRowQuestions(int count,
			ArrayList<TableRow> trows, TableLayout layout) {
		for (int i = 0; i < count; i++) {
			TableRow tr = new TableRow(con);
			trows.add(tr);
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);

			layout.addView(tr, lp);
		}
		return trows;
	}

	int titleRow = 0;
	int titleCount = 0;
	int totalDisplayed = 0;
	private String lastTitle;

	private RelativeLayout addHebrewRow(int counter,
			ArrayList<TableRow> tblRows, RelativeLayout questionnaireLayout,
			boolean isQuestion, View answersView, String thisTitle) {
		int iiii = -1;
		LinearLayout tblRow = null;
		if (isQuestion && titles != null && titles.size() > 0) {
			tblRow = tblRows.get(0);
			iiii = 0;
			titleCount++;
			if (titleCount % titles.size() == 0)
				titleCount = 0;
			if (titleCount != 1 && totalDisplayed != 0) {
				return new RelativeLayout(con);
			}
			if (thisTitle == null)
				return new RelativeLayout(con);
			thisColor++;
			this.lastTitle = thisTitle;

		} else if (isQuestion) {
			tblRow = tblRows.get(0);
			thisColor++;
		} else if (titles != null && titles.size() > 0) {
			if (titleCount == 0) {
				tblRow = tblRows.get(titles.size());
				totalDisplayed = 0;
				if (thisTitle == null)
					return new RelativeLayout(con);
				int tblRowCount = tblRow.getChildCount();
				iiii = titles.size();
				if (tblRowCount == 0 && thisTitle != null
						&& (modeSelect != 3 || sixtyTwo > 50)) {
					// if (titleCount)
					tblRow.addView(getTitleTextView(thisTitle),
							new TableRow.LayoutParams(
									TableRow.LayoutParams.WRAP_CONTENT,
									TableRow.LayoutParams.WRAP_CONTENT));
				}
			} else if (titleCount != 1 && answersView != null) {
				questionView = answersView = null;
				tblRow = tblRows.get(titleCount);
				iiii = titleCount;
			} else {

				tblRow = tblRows.get(titleCount);
				iiii = titleCount;
				if (thisTitle == null)
					return new RelativeLayout(con);
				totalDisplayed++;
				int tblRowCount = tblRow.getChildCount();
				if (tblRowCount == 0 && thisTitle != null
						&& (modeSelect != 3 || sixtyTwo > 50)) {
					tblRow.addView(getTitleTextView(thisTitle),
							new TableRow.LayoutParams(
									TableRow.LayoutParams.WRAP_CONTENT,
									TableRow.LayoutParams.WRAP_CONTENT));
					// titleCount++;
				}
			}
		} else {
			iiii = 1;
			tblRow = tblRows.get(1);
		}

		int iii = iiii;
		if (iii == 0) {
			if (ultimateWidth < 0)
				theTotalWidth += display.getWidth();
			else
				theTotalWidth += ultimateWidth;

			tblRow.addView(questionnaireLayout);
		} else
			tblRow.addView(questionnaireLayout);
		int tblRowCount = tblRow.getChildCount();

		if (!isQuestion
				&& tblRowCount == (this.listObjects.size() / this.titles.size())
				&& thisTitle != null && (modeSelect == 3 && sixtyTwo < 50)) {
			tblRow.addView(getTitleTextView(thisTitle));
			// titleCount++;
		}

		return new RelativeLayout(con);
	}

	private RelativeLayout addEnglishRow(int counter,
			ArrayList<TableRow> tblRows, RelativeLayout questionnaireLayout,
			boolean isQuestion, View answersView, String thisTitle) {

		int iiii = -1;
		LinearLayout tblRow = null;
		if (isQuestion && titles != null && titles.size() > 0) {
			tblRow = tblRows.get(0);
			iiii = 0;
			titleCount++;
			if (titleCount % titles.size() == 0)
				titleCount = 0;
			if (titleCount != 1 && totalDisplayed != 0) {
				return new RelativeLayout(con);
			}
			if (thisTitle == null)
				return new RelativeLayout(con);
			thisColor++;

		} else if (isQuestion) {
			tblRow = tblRows.get(0);
			thisColor++;
		} else if (titles != null && titles.size() > 0) {
			if (titleCount == 0) {
				tblRow = tblRows.get(titles.size());
				totalDisplayed = 0;
				if (thisTitle == null)
					return new RelativeLayout(con);
				int tblRowCount = tblRow.getChildCount();
				iiii = titles.size();
				if (tblRowCount == 0 && thisTitle != null
						&& (modeSelect != 3 || sixtyTwo > 50))
					tblRow.addView(getTitleTextView(thisTitle),
							new TableRow.LayoutParams(
									TableRow.LayoutParams.WRAP_CONTENT,
									TableRow.LayoutParams.WRAP_CONTENT));
			} else if (titleCount != 1 && answersView != null) {
				questionView = answersView = null;
				tblRow = tblRows.get(titleCount);
				iiii = titleCount;
			} else {
				if (thisTitle == null || titleCount >= tblRows.size())
					return new RelativeLayout(con);
				tblRow = tblRows.get(titleCount);
				iiii = titleCount;

				totalDisplayed++;
				int tblRowCount = tblRow.getChildCount();
				if (tblRowCount == 0 && thisTitle != null
						&& (modeSelect != 3 || sixtyTwo > 50))
					tblRow.addView(getTitleTextView(thisTitle),
							new TableRow.LayoutParams(
									TableRow.LayoutParams.WRAP_CONTENT,
									TableRow.LayoutParams.WRAP_CONTENT));
			}
		} else {
			iiii = 1;
			tblRow = tblRows.get(1);
		}

		int CELL_PADDING = 10;
		// if (answersView != null) {
		// questionnaireLayout.addView(answersView);
		// questionView = null;
		// }
		// questionnaireLayout.setPadding(CELL_PADDING, CELL_PADDING,
		// convertDiptoPix(80), CELL_PADDING);
		// questionnaireLayout.setBackgroundResource(R.drawable.tbl_border);

		// LayoutParams lp = new LayoutParams(80,
		// android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		// questionnaireLayout.setLayoutParams(lp);

		// questionnaireLayout.setBackgroundColor(getColor());
		int iii = iiii;
		if (iii == 0) {
			if (ultimateWidth < 0)
				theTotalWidth += display.getWidth();
			else
				theTotalWidth += ultimateWidth;

			tblRow.addView(questionnaireLayout);
		} else
			tblRow.addView(questionnaireLayout);
		int tblRowCount = tblRow.getChildCount();

		if (!isQuestion
				&& tblRowCount == (this.listObjects.size() / this.titles.size())
				&& thisTitle != null && (modeSelect == 3 && sixtyTwo < 50)) {
			tblRow.addView(getTitleTextView(thisTitle));
		}

		return new RelativeLayout(con);

	}

	private RelativeLayout addRow(int counter, ArrayList<TableRow> tblRows,
			RelativeLayout questionnaireLayout, boolean isQuestion,
			View answersView, String thisTitle) {
		if (modeSelect == 3 && titles != null && titles.size() > 0)
			return addHebrewRow(counter, tblRows, questionnaireLayout,
					isQuestion, answersView, thisTitle);
		else
			return addEnglishRow(counter, tblRows, questionnaireLayout,
					isQuestion, answersView, thisTitle);
	}

	static int thisColor = 0;

	// private int getColor() {
	// int color = 0;
	// if (thisColor == 0) {
	// color = Color.parseColor("#FFCCFF");
	// } else if (thisColor == 1) {
	// color = Color.parseColor("#CCCCFF");
	// } else if (thisColor == 2) {
	// color = Color.parseColor("#CCCC99");
	// } else {
	// thisColor = 0;
	// color = Color.parseColor("#FFCCFF");
	// }
	//
	// return color;
	// }

	private RelativeLayout addRoww(int counter, ArrayList<TableRow> tblRows,
			RelativeLayout questionnaireLayout, boolean isQuestion,
			View answersView, String thisTitle) {
		TableRow tblRow = null;
		if (isQuestion && titles != null && titles.size() > 0) {
			tblRow = tblRows.get(0);

			titleCount++;
			if (titleCount % titles.size() == 0)
				titleCount = 0;
			if (titleCount != 1 && totalDisplayed != 0) {
				return new RelativeLayout(con);
			}
			if (thisTitle == null)
				return new RelativeLayout(con);

		} else if (isQuestion) {
			tblRow = tblRows.get(0);

		} else if (titles != null && titles.size() > 0) {
			if (titleCount == 0) {
				tblRow = tblRows.get(titles.size());
				totalDisplayed = 0;
				if (thisTitle == null)
					return new RelativeLayout(con);
				if (tblRow.getChildCount() == 0 && thisTitle != null)
					tblRow.addView(getTitleTextView(thisTitle));
			} else if (titleCount != 1 && answersView != null) {
				questionView = answersView = null;
				tblRow = tblRows.get(titleCount);
			} else {

				tblRow = tblRows.get(titleCount);
				if (thisTitle == null)
					return new RelativeLayout(con);
				totalDisplayed++;
				if (tblRow.getChildCount() == 0 && thisTitle != null)
					tblRow.addView(getTitleTextView(thisTitle));
			}
		} else {
			tblRow = tblRows.get(1);
		}

		int CELL_PADDING = 10;
		// if (answersView != null) {
		// questionnaireLayout.addView(answersView);
		// questionView = null;
		// }
		// questionnaireLayout.setPadding(CELL_PADDING, CELL_PADDING,
		// convertDiptoPix(80), CELL_PADDING);
		// questionnaireLayout.setBackgroundResource(R.drawable.tbl_border);

		// LayoutParams lp = new LayoutParams(80,
		// android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		// questionnaireLayout.setLayoutParams(lp);
		tblRow.addView(questionnaireLayout);

		return new RelativeLayout(con);
	}

	private TextView setTextViewProperties(TextView tv, String text,
			String font, String color, String size, String italic, String bold,
			String underline, String align) {
		tv.setTextColor(Helper.getIntColor(color));

		float f = Helper.getFloat(size);
		if (f != 0)
			tv.setTextSize(f);
		if (Helper.comapreString(italic, "1"))
			tv.setTypeface(null, Typeface.ITALIC);
		if (Helper.comapreString(bold, "1"))
			tv.setTypeface(null, Typeface.BOLD);

		if (Helper.comapreString(underline, "1"))
			tv = setUnderLineText(text, tv);
		tv.setGravity(setGravityAlignment(Helper.getInt(align)));
		setFontSize(tv);
		return tv;
	}

	private TextView setGroupTextViewProperties(TextView tv, String text) {
		tv.setTextColor(Helper.getIntColor(null));

		float f = Helper.getFloat("32");
		if (f != 0)
			tv.setTextSize(f);

		tv.setTypeface(null, Typeface.BOLD);

		tv.setGravity(Gravity.RIGHT);
		setFontSize(tv);
		return tv;
	}

	private int setGravityAlignment(int align) {
		switch (align) {
		case 1:
			return (Gravity.RIGHT);
		case 2:
			return (Gravity.CENTER_HORIZONTAL);
		case 3:
			return (Gravity.LEFT);
		}
		return (Gravity.LEFT);
	}

	private TextView setUnderLineText(String text, TextView tv) {
		tv.setPaintFlags(tv.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
		tv.setText(Helper.makeHtmlString(Helper.getValidString(text)));
		return tv;
	}

	private View getQuestionView(int viewid, boolean isGroup) {
		TextView tv = new TextView(con);
		tv.setTag(questionObject.getDataID());
		StringBuffer sb = new StringBuffer();
		if (Helper.comapreString(questionObject.getMandatory(), "1")) {
			sb.append("*");

			IsMandatory = true;
		}

		sb.append(Helper.getValidString(questionObject.getQuestion()));
		tv = setTextViewProperties(tv, sb.toString(), questionObject.getFont(),
				questionObject.getColor(), questionObject.getSize(),
				questionObject.getItalics(), questionObject.getBold(),
				questionObject.getUnderline(), questionObject.getAlign());
		// if (set.getEnableQuestionNumberingInForm().equals("1")
		// && questionObject.currentIndex > 0)
		// tv.setText(Helper.makeHtmlString(questionObject.currentIndex + " "
		// + ConvertTextCodetoText(sb.toString())));
		// else if (set.getEnableQuestionNumberingInForm().equals("2")) {
		// if (questionObject.getObjectCode() == null)
		// questionObject.setObjectCode("");
		// if (questionObject.getObjectCode() != null) {
		// String objCode = questionObject.getObjectCode();
		// if (objCode.contains(";")) {
		// objCode = objCode.substring(0, objCode.indexOf(";"));
		// }
		// objCode = "";
		// tv.setText(Helper.makeHtmlString(objCode + " "
		// + ConvertTextCodetoText(sb.toString())));
		// }
		// } else
		{
			tv.setText(Helper.makeHtmlString(ConvertTextCodetoText(sb
					.toString())));
		}
		// tv.setText("");
		sb = null;
		viewId = getViewId(viewid);
		tv.setId(viewId);
		if (Build.VERSION.SDK_INT > 13) {
			if (modeSelect == 3)
				tv.setGravity(Gravity.RIGHT);
		}
		setFontSize(tv);
		tv.setPadding(Constants.dpToPx(10),0,Constants.dpToPx(10),0);
		return tv;
	}

	private int getViewId(int id) {
		int val = id + 1;
		return val;
	}

	private void setFontSize(View v) {

		try {
			if (v.getClass().equals(EditText.class)) {
				EditText btnView = (EditText) v;

				btnView.setTextSize(UIHelper.getFontSize(con,
						btnView.getTextSize()));
				if (Helper.getTheme(con) == 0) {
					btnView.setTextColor(con.getResources().getColor(
							android.R.color.black));
				}
			}

		} catch (Exception ex) {

		}

		try {
			if (v.getClass().equals(Button.class)) {
				Button btnView = (Button) v;

				btnView.setTextSize(UIHelper.getFontSize(con,
						btnView.getTextSize()));
			}
		} catch (Exception ex) {

		}

		try {
			if (v.getClass().equals(CheckBox.class)) {
				CheckBox btnView = (CheckBox) v;

				btnView.setTextSize(UIHelper.getFontSize(con,
						btnView.getTextSize()));
				if (Helper.getTheme(con) == 0) {
					btnView.setTextColor(con.getResources().getColor(
							android.R.color.white));
				}
			}
		} catch (Exception ex) {

		}

		try {
			if (v.getClass().equals(RadioButton.class)) {
				RadioButton btnView = (RadioButton) v;

				btnView.setTextSize(UIHelper.getFontSize(con,
						btnView.getTextSize()));
				if (Helper.getTheme(con) == 0) {
					btnView.setTextColor(con.getResources().getColor(
							android.R.color.white));
				}
			}
		} catch (Exception ex) {

		}
		try {
			if (v.getClass().equals(TextView.class)) {
				TextView textView = (TextView) v;

				textView.setTextSize(UIHelper.getFontSize(con,
						textView.getTextSize()));
				if (sixtyTwo < 50)
					textView.setTextSize(14);
				if (Helper.getTheme(con) == 0) {
					textView.setTextColor(con.getResources().getColor(
							android.R.color.white));
				}
			}
		} catch (Exception ex) {

		}

	}

	private View getMiView(int type, boolean isPrev) {
		View v = getEditTextView(type, isPrev);
		thisQGroup.setTextbox(textbox);
		thisQGroup.setbTextbox(btextbox);
		if (order!=null && order.getIsJobInProgressOnServer()!=null
				&& order.isDataIdEnabled(set,questionObject.getDataID())) {
			if (btextbox!=null) btextbox.setEnabled(false);
			if (textbox!=null) textbox.setEnabled(false);
		}
		thisQGroup.setWholeMiView(v);


		if (thisQGroup!=null
				&& (thisQGroup.getCustomCheckboxgrp()!=null
				|| thisQGroup.getMultiSpinner()!=null))
		{
			UIQuestionGroupHelper.hideShowMiInCheckBox(thisQGroup.getListAnswers(),thisQGroup.getMultiSpinner(),thisQGroup.getCustomCheckboxgrp(),thisQGroup);
		}
		return v;
	}

	View.OnFocusChangeListener onKeyBoardOn = new OnFocusChangeListener() {

		@Override
		public void onFocusChange(View v, boolean hasFocus) {

			isKeyboardOn = true;
		}
	};

	int year, month, day;
	Calendar myCalendar = Calendar.getInstance();
	private int selectedWorker;
	private int workerListViewListener;
	private int selectedBranch;

	private void showTimePickerSeconds(final EditText txtTime) {

		txtTime.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				CustomTimePicker cTimePicker = new CustomTimePicker();
				myCalendar = Calendar.getInstance();
				cTimePicker.makeTimeDialog((Activity) con,
						questionObject.getMiType(),
						myCalendar.get(Calendar.HOUR_OF_DAY),
						myCalendar.get(Calendar.MINUTE),
						myCalendar.get(Calendar.SECOND),
						new MyTimePickerDialog.OnTimeSetListener() {

							@Override
							public void onTimeSet(
									com.ikovac.timepickerwithseconds.view.TimePicker view,
									int hourOfDay, int minute, int seconds) {

								myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
								myCalendar.set(Calendar.MINUTE, minute);
								myCalendar.set(Calendar.SECOND, seconds);
								SimpleDateFormat timeformat = new SimpleDateFormat(
										"kk:mm:ss", Locale.ENGLISH);
								String timeLog = timeformat.format(myCalendar
										.getTime());
								txtTime.setText(Helper.makeHtmlString(timeLog));
							}
						});

			}
		});
	}

	private void showTimePicker(final EditText txtTime) {
		final TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {
			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				// hour = hourOfDay;
				// min = minute;
				myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
				myCalendar.set(Calendar.MINUTE, minute);
				SimpleDateFormat timeformat = new SimpleDateFormat("HH:mm:ss",
						Locale.ENGLISH);
				// textbox.setText(new
				// StringBuilder().append(hourOfDay).append(':')
				// .append(minute));
				String timeLog = timeformat.format(myCalendar.getTime());
				txtTime.setText(Helper.makeHtmlString(timeLog));
			}
		};
		txtTime.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				TimePickerDialog timeDialog = new TimePickerDialog(con, t,
						myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar
								.get(Calendar.MINUTE), true);
				timeDialog.show();

			}
		});
	}

	private void ShowDatePicker() {
		final DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int yr, int monthOfYear,
					int dayOfMonth) {
				year = yr;
				month = monthOfYear;
				day = dayOfMonth;
				textbox.setText(new StringBuilder().append(day).append('-')
						.append(month + 1).append('-').append(year));

			}
		};

		textbox.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				textbox = (EditText) v;
				final Calendar myCalendar = Calendar.getInstance();
				new DatePickerDialog(con, dateListener, myCalendar
						.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
						myCalendar.get(Calendar.DAY_OF_MONTH)).show();
			}
		});
		thisQGroup.setTextbox(textbox);
		if (order!=null && order.getIsJobInProgressOnServer()!=null
				&& order.isDataIdEnabled(set,questionObject.getDataID())) {
			if (btextbox!=null) btextbox.setEnabled(false);
			if (textbox!=null) textbox.setEnabled(false);
		}
	}

	private Button btextbox;

	public int getDateButtonEditTextView(UIQuestionGroupHelper th,
			Button btextbox, int type, boolean isPrev) {

		int vDateTime = -1;

		viewId = getViewId(viewId);
		btextbox.setId(viewId);
		String str = android.os.Build.VERSION.SDK;
		int sdk = Integer.valueOf(str);
		if (isPrev) {
			QuestionnaireData qd = getSingleAnswer(questionObject.getDataID());
			if (qd != null)
				btextbox.setText(Helper.makeHtmlString(qd.getAnswerText()));
		}
		setFontSize(btextbox);

		switch (type) {
		case 3:
			ShowDatePickerButton();
			vDateTime = Helper.DatePicker;
			break;
		case 4:
		case 6:
		case 7:
		case 8:
			showTimePickerSecondsButton(btextbox);
			vDateTime = Helper.TimePicker;
			break;
		}

		Drawable d = con.getResources().getDrawable(R.drawable.backdatebox);
		btextbox.setBackgroundDrawable(d);

		return vDateTime;
	}

	private void showTimePickerSecondsButton(final Button txtTime) {

		txtTime.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				CustomTimePicker cTimePicker = new CustomTimePicker();
				myCalendar = Calendar.getInstance();
				cTimePicker.makeTimeDialog((Activity) con,
						questionObject.getMiType(),
						myCalendar.get(Calendar.HOUR_OF_DAY),
						myCalendar.get(Calendar.MINUTE),
						myCalendar.get(Calendar.SECOND),
						new MyTimePickerDialog.OnTimeSetListener() {

							@Override
							public void onTimeSet(
									com.ikovac.timepickerwithseconds.view.TimePicker view,
									int hourOfDay, int minute, int seconds) {

								myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
								myCalendar.set(Calendar.MINUTE, minute);
								myCalendar.set(Calendar.SECOND, seconds);
								SimpleDateFormat timeformat = new SimpleDateFormat(
										"kk:mm:ss", Locale.ENGLISH);
								String timeLog = timeformat.format(myCalendar
										.getTime());
								txtTime.setText(Helper.makeHtmlString(timeLog));
							}
						});

			}
		});
		thisQGroup.setbTextbox(btextbox);
	}

	private void ShowDatePickerButton() {
		final DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int yr, int monthOfYear,
					int dayOfMonth) {
				year = yr;
				month = monthOfYear;
				day = dayOfMonth;
				btextbox.setText(new StringBuilder().append(day).append('-')
						.append(month + 1).append('-').append(year));

			}
		};

		btextbox.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				btextbox = (Button) v;
				final Calendar myCalendar = Calendar.getInstance();
				new DatePickerDialog(con, dateListener, myCalendar
						.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
						myCalendar.get(Calendar.DAY_OF_MONTH)).show();
			}
		});
		thisQGroup.setbTextbox(btextbox);
	}

	private View getEditText(int type, boolean isPrev) {

		if (type == 3 || type == 4 || type == 6 || type == 7 || type == 8) {
			Button btn = btextbox = new Button(con);
			getDateButtonEditTextView(this, btn, type, isPrev);
			thisQGroup.setbTextbox(btextbox);
			thisQGroup.setTextbox(null);
			if (order!=null && order.getIsJobInProgressOnServer()!=null
					&& order.isDataIdEnabled(set,questionObject.getDataID())) {
				if (btextbox!=null) btextbox.setEnabled(false);
				if (textbox!=null) textbox.setEnabled(false);
			}
			return btn;
		}

		textbox = new EditText(con);
		textbox.setTextColor(R.color.black);
		if (firstEditText == null)
			firstEditText = textbox;
		textbox.setMinWidth(Helper.getInt(questionObject
				.getMiFreeTextMinlength()));
		textbox.setMaxWidth(Helper.getInt(questionObject
				.getMiFreeTextMaxlength()));
		textbox.setGravity(Gravity.TOP);
		textbox.setLines(Helper.getInt(questionObject.getMiFreeTextMaxlength()));
		viewId = getViewId(viewId);
		miEditViewId = viewId;
		textbox.setOnFocusChangeListener(onKeyBoardOn);
		textbox.setId(viewId);
		thisQGroup.setTextbox(textbox);
		thisQGroup.setbTextbox(null);
		if (order!=null && order.getIsJobInProgressOnServer()!=null
				&& order.isDataIdEnabled(set,questionObject.getDataID())) {
			if (btextbox!=null) btextbox.setEnabled(false);
			if (textbox!=null) textbox.setEnabled(false);
		}
		// if (isPrev)
		{
			QuestionnaireData qd = getSingleAnswer(questionObject.getDataID());
			// if(listAnswers != null && listAnswers.get(0) != null &&
			// listAnswers.get(0).getAnswer() != null)
			// qd.setAnswerText(listAnswers.get(0).getAnswer());
			if (qd != null && qd.getAnswerText() != null
					&& !qd.getAnswerText().equals(""))
				textbox.setText(Helper.makeHtmlString(qd.getAnswerText()));
			else if (qd != null && qd.getMi() != null && !qd.getMi().equals(""))
				textbox.setText(Helper.makeHtmlString(qd.getMi()));
			else if (qd != null && qd.getFreetext() != null
					&& !qd.getFreetext().equals(""))
				textbox.setText(Helper.makeHtmlString(qd.getFreetext()));
			else if (qd != null && qd.getAnsText() != null
					&& !qd.getAnsText().equals(""))
				textbox.setText(Helper.makeHtmlString(qd.getAnsText()));
		}
		setFontSize(textbox);
		thisQGroup.setTextbox(textbox);
		if (order!=null && order.getIsJobInProgressOnServer()!=null
				&& order.isDataIdEnabled(set,questionObject.getDataID())) {
			if (btextbox!=null) btextbox.setEnabled(false);
			if (textbox!=null) textbox.setEnabled(false);
		}
		// txtCurrent = textbox;
		// txtCurrent.requestFocus();
		// txtCurrent.postDelayed(new Runnable() {
		// @Override
		// public void run() {
		// // TODO Auto-generated method stub
		// {
		// InputMethodManager keyboard = (InputMethodManager)
		// getSystemService(Context.INPUT_METHOD_SERVICE);
		// keyboard.showSoftInput(
		// txtCurrent, 0);
		// }
		// }
		// }, 10);
		textbox.setTag(questionObject.getDataID());
		// questionObject.getMiNumberMax()
		switch (type) {
		case 1:
			textbox.setInputType(InputType.TYPE_CLASS_TEXT
					| InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
			textbox.setSingleLine(true);
			textbox.setLines(4); // desired number of lines
			textbox.setHorizontallyScrolling(false);
			textbox.setImeOptions(EditorInfo.IME_ACTION_DONE);
			break;
		case 2:
		case 5:
			textbox.setInputType(InputType.TYPE_CLASS_NUMBER
					| InputType.TYPE_NUMBER_FLAG_DECIMAL);
			// textbox.setText("");
			textbox.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View arg0) {
					Calculation c = new Calculation((EditText) arg0);
					c.makeDialog(con);
					return false;
				}
			});

			break;
		case 3:
			textbox.setFocusable(false);
			textbox.setInputType(InputType.TYPE_CLASS_DATETIME);
			ShowDatePicker();
			break;
		case 4:
		case 6:
		case 7:
		case 8:
			textbox.setFocusable(false);
			textbox.setInputType(InputType.TYPE_CLASS_DATETIME);
			showTimePickerSeconds(textbox);
			break;
		}
		textbox.clearFocus();
		return textbox;
	}

	private TextView displayLabel(String text) {
		// Constants.QUESTIONNAIRE_JUMPTO_LBL
		TextView tv = new TextView(con);
		tv.setText(Helper.makeHtmlString(text));
		tv.setTextColor(Color.BLACK);
		viewId = getViewId(viewId);
		tv.setId(viewId);
		setFontSize(tv);
		return tv;
	}

	private EditText displayListEdit(EditText editText, String text) {
		// Constants.QUESTIONNAIRE_JUMPTO_LBL
		EditText tv = editText;
		tv.setText(Helper.makeHtmlString(text));
		tv.setTextColor(Color.BLACK);
		viewId = getViewId(viewId);
		tv.setId(viewId);
		setFontSize(tv);
		return tv;
	}

	private EditText displayListEdit(String text) {
		// Constants.QUESTIONNAIRE_JUMPTO_LBL
		EditText tv = new EditText(con);
		tv.setTextColor(R.color.black);
		if (firstEditText == null)
			firstEditText = tv;
		tv.setText(Helper.makeHtmlString(text));
		tv.setTextColor(Color.BLACK);
		viewId = getViewId(viewId);
		tv.setId(viewId);
		setFontSize(tv);
		tv.clearFocus();
		return tv;
	}

	private RelativeLayout.LayoutParams getSearchEditTextLayoutParam() {
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		lp.addRule(setAlignmentOfView(Helper.getInt(questionObject.getAlign())));
		lp.setMargins(0, 5, 0, 5);
		lp.addRule(RelativeLayout.BELOW, viewId - 2);
		lp.addRule(RelativeLayout.LEFT_OF, viewId - 1);
		return lp;
	}

	private ListView getWorkerListView(int viewidd, Objects questionObject) {

		final ListView s = new ListView(con);
		QuestionnaireData qd = getSingleAnswer(questionObject.getDataID());
		final String[] array_spinner = new String[listWorkers.size() + 1];
		array_spinner[0] = "Not Selected";

		for (int i = 0; i < listWorkers.size(); i++) {
			array_spinner[i + 1] = Helper.stripHtml(listWorkers.get(i)
					.getWorkerName());
			String workerId = listWorkers.get(i).getWorkerID();

			if (qd != null && qd.getWorkerID() != null
					&& qd.getWorkerID().equals(workerId)) {
				if (selectedWorker == -1)
					selectedWorker = i + 1;
				thisQGroup.setSelectedWorker(selectedWorker);
			}
		}
		ArrayAdapter adapter = new ArrayAdapter(con,
				UIHelper.getSpinnerLayoutSize(con, modeSelect), array_spinner);

		s.setAdapter(adapter);
		s.setBackgroundColor(Color.GRAY);
		if (selectedWorker == -1)
			selectedWorker = 0;
		thisQGroup.setSelectedWorker(selectedWorker);
		workerListViewListener = -1;
		if (array_spinner[selectedWorker].toLowerCase().equals("not selected")) {
			workerEditText.setText("");
			workerEditText.setHint(array_spinner[selectedWorker]);
		} else
			workerEditText.setText(Helper
					.makeHtmlString(array_spinner[selectedWorker]));
		s.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				InputMethodManager inputManager = (InputMethodManager) con
						.getSystemService(Context.INPUT_METHOD_SERVICE);

				inputManager.hideSoftInputFromWindow(((Activity) con)
						.getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
				selectedWorker = arg2;
				thisQGroup.setSelectedWorker(selectedWorker);
				if (spinnerFirstTime)
					spinnerFirstTime = false;
				// else if (isServerAnswersActAsSubmit) {
				// nextbtnClickListener();
				// spinnerFirstTime = true;
				// }
				workerListViewListener = -1;
				workerListView.setVisibility(View.GONE);
				workerBranchImage.setBackgroundDrawable(con.getResources()
						.getDrawable(R.drawable.down_btn));

				for (int i = 0; i < array_spinner.length; i++) {
					final String text = ((TextView) arg1).getText().toString();
					if (array_spinner[i].equals(text)) {
						selectedWorker = i;
						thisQGroup.setSelectedWorker(selectedWorker);
					}
				}
				if (array_spinner[selectedWorker].toLowerCase().equals(
						"not selected")) {
					workerEditText.setText("");
					workerEditText.setHint(array_spinner[selectedWorker]);
				} else
					workerEditText.setText(Helper
							.makeHtmlString(array_spinner[selectedWorker]));
			}

		});
		return s;
	}

	private RelativeLayout.LayoutParams getListLayoutParam() {
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				display.getWidth(), display.getHeight() / 2);
		lp.addRule(setAlignmentOfView(Helper.getInt(questionObject.getAlign())));
		lp.setMargins(5, -15, 15, 15);
		if (viewId != 101) {
			lp.addRule(RelativeLayout.BELOW, viewId - 1);
		}
		lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		lp.addRule(RelativeLayout.ALIGN_RIGHT);
		return lp;
	}

	private ListView getBranchListView(int viewidd, Objects questionObject) {

		final ListView s = new ListView(con);
		QuestionnaireData qd = getSingleAnswer(questionObject.getDataID());
		final String[] array_spinner = new String[listBranches.size() + 1];
		array_spinner[0] = "Not Selected";

		for (int i = 0; i < listBranches.size(); i++) {
			array_spinner[i + 1] = Helper.stripHtml(listBranches.get(i)
					.getBranchName());
			String workerId = listBranches.get(i).getBranchID();

			if (qd != null && qd.getBranchID() != null
					&& qd.getBranchID().equals(workerId)) {
				if (selectedBranch == -1)
					selectedBranch = i + 1;
				thisQGroup.setSelectedBranch(selectedBranch);
			}
		}
		ArrayAdapter adapter = new ArrayAdapter(con,
				UIHelper.getSpinnerLayoutSize(con, modeSelect), array_spinner);

		s.setAdapter(adapter);
		s.setBackgroundColor(Color.GRAY);
		if (selectedBranch == -1)
			selectedBranch = 0;
		thisQGroup.setSelectedBranch(selectedBranch);
		workerListViewListener = -1;
		if (array_spinner[selectedBranch].toLowerCase().equals("not selected")) {
			branchEditText.setText("");
			branchEditText.setHint(array_spinner[selectedBranch]);
		} else
			branchEditText.setText(Helper
					.makeHtmlString(array_spinner[selectedBranch]));
		s.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				InputMethodManager inputManager = (InputMethodManager) con
						.getSystemService(Context.INPUT_METHOD_SERVICE);

				inputManager.hideSoftInputFromWindow(((Activity) con)
						.getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
				selectedBranch = arg2;
				thisQGroup.setSelectedBranch(selectedBranch);
				if (spinnerFirstTime)
					spinnerFirstTime = false;
				// else if (isServerAnswersActAsSubmit) {
				// nextbtnClickListener();
				// spinnerFirstTime = true;
				// }
				workerListViewListener = -1;

				branchListView.setVisibility(View.GONE);
				workerBranchImage.setBackgroundDrawable(con.getResources()
						.getDrawable(R.drawable.down_btn));

				for (int i = 0; i < array_spinner.length; i++) {
					final String text = ((TextView) arg1).getText().toString();
					if (array_spinner[i].equals(text)) {
						selectedBranch = i;
						thisQGroup.setSelectedBranch(selectedBranch);
					}
				}
				if (array_spinner[selectedBranch].toLowerCase().equals(
						"not selected")) {
					branchEditText.setText("");
					branchEditText.setHint(array_spinner[selectedBranch]);
				} else
					branchEditText.setText(Helper
							.makeHtmlString(array_spinner[selectedBranch]));
			}

		});
		return s;
	}

	int selectedLocation = -1;
	int selectedProduct = -1;

	// /////////////////////////GET//ANSWERS//////////////
	private int getSingleChoiceAnswer(CustomRadioButtonGroup crgroup,
			RadioGroup rGroup, Spinner spinner) {
		if (rGroup != null) {
			return (rGroup.getCheckedRadioButtonId());
		} else if (crgroup != null) {
			return (crgroup.getSelectedItemPosition());
		} else if (spinner != null) {
			return (spinner.getSelectedItemPosition());
		}

		return 0;
	}

	private QuestionnaireData getEditTextData(QuestionnaireData qd,
			EditText textbox) {
		if (textbox != null) {

			final EditText tmpEdit = textbox;
			qd.setAnswerText(textbox.getText().toString());
			tmpEdit.postDelayed(new Runnable() {
				@Override
				public void run() {
					try {
						InputMethodManager keyboard = (InputMethodManager) con
								.getSystemService(Context.INPUT_METHOD_SERVICE);
						keyboard.hideSoftInputFromWindow(
								tmpEdit.getWindowToken(), 0);
					} catch (Exception ex) {
					}
				}
			}, 10);

		}
		return qd;
	}

	private QuestionnaireData getEditTextData(QuestionnaireData qd,
			Button textbox) {
		if (textbox != null) {

			final Button tmpEdit = textbox;
			qd.setAnswerText(textbox.getText().toString());

		}
		return qd;
	}

	private QuestionnaireData getAnswers(int qtype, int displayType,
			QuestionnaireData qd, ArrayList<Answers> listAnswers,
			LinearLayout checkboxgrp, RadioGroup rgroup,
			CustomRadioButtonGroup crGroup, CustomCheckButtonGroup chGroup,
			Spinner spinner, MultiSelectionSpinner multiSpinner) {
		if (qd == null)
			return null;
		switch (qtype) {
		case 3:
		case 7:
			if (displayType == 0 || displayType == 1 || displayType == 5) {
				int selectedIndex = getSingleChoiceAnswer(crGroup, rgroup,
						spinner);

				if (multiSpinner != null) {
					List<String> selectedItems = multiSpinner
							.getSelectedStrings();
					for (int i = 0; i < selectedItems.size(); i++) {

						qd.setAnswers(setCheckRadioAnswers(
								selectedItems.get(i), listAnswers));

					}
					return qd;

				} else if (selectedIndex != -1 && (listAnswers != null)) {
					qd.initListAnswer();
					qd.setAnswers(listAnswers.get(selectedIndex));
					// qd.setAnsText(listAnswers.get(selectedIndex).getAnswer());
					qd.setPosition(selectedIndex);
					// if (listAnswers.get(selectedIndex).getJumpTo() != null
					// && listAnswers.get(selectedIndex).getJumpTo()
					// .length() > 0) {
					// isJumpFromSingleChoiceAnswers = true;
					// nextPageJumpID = listAnswers.get(selectedIndex)
					// .getJumpTo();
					// }
				} else if (selectedIndex != -1) {
					qd.setPosition(selectedIndex);
				}

				return qd;
			}
		case 8:
		case 9:
			if (checkboxgrp != null) {
				qd.initListAnswer();
				for (int i = 0; i < checkboxgrp.getChildCount(); i++) {
					CheckBox cb = (CheckBox) checkboxgrp.getChildAt(i);
					if (cb.isChecked()) {
						qd.setAnswers(setCheckRadioAnswers(cb.getTag()
								.toString(), listAnswers));
					}
				}
				return qd;
			} else if (chGroup != null) {
				qd.initListAnswer();
				for (int i = 0; i < chGroup.getChildCount(); i++) {
					boolean cbChecked = chGroup.getChildChecked(i);
					String cbTag = chGroup.getChildTagAt(i);
					if (cbChecked) {
						qd.setAnswers(setCheckRadioAnswers(cbTag.toString(),
								listAnswers));
					}
				}
				return qd;
			} else if (multiSpinner != null) {
				List<String> selectedItems = multiSpinner.getSelectedStrings();
				qd.initListAnswer();
				for (int i = 0; i < selectedItems.size(); i++) {

					qd.setAnswers(setCheckRadioAnswers(selectedItems.get(i),
							listAnswers));

				}
				return qd;

			}
		}
		return qd;
	}

	private Answers setCheckRadioAnswers(String text,
			ArrayList<Answers> listAnswers2) {
		for (int i = 0; i < listAnswers2.size(); i++) {
			String ans = ConvertTextCodetoText(listAnswers2.get(i).getAnswer()
					.toLowerCase());
			if (ans.toLowerCase().equals(text.toLowerCase())) {
				return listAnswers2.get(i);
			}
		}
		return null;
	}

	private QuestionnaireData setBrachOrWorker(int qbjecttype,
			QuestionnaireData qd, ArrayList<Workers> listWorkers,
			ListView workerListView, int selectedWorker,
			ArrayList<Branches> listBranches, ListView branchListView,
			int selectedBranch, RadioGroup rGroup, Spinner spinner) {
		int selectedIndex = getSingleChoiceAnswer(null, rGroup, spinner);
		switch (qbjecttype) {
		case 9:
			if (listWorkers != null && workerListView != null) {
				if (selectedWorker <= 0) {
					qd.setWorkerID("-1");
					qd.setWorkertext("not selected");
				} else {
					qd.setWorkertext(listWorkers.get(selectedWorker - 1)
							.getWorkerName());
					qd.setWorkerID(listWorkers.get(selectedWorker - 1)
							.getWorkerID());
				}
			}
			break;
		case 10:
			if (listBranches != null && branchListView != null) {
				if (selectedBranch <= 0) {
					qd.setBranchID("-1");
					qd.setBrachtext("not selected");
				} else {
					qd.setBranchID(listBranches.get(selectedBranch - 1)
							.getBranchID());
					qd.setBrachtext(listBranches.get(selectedBranch - 1)
							.getBranchName());
				}

			}
			break;
		}
		return qd;
	}
	public static boolean hideShowMiInCheckBox(ArrayList<Answers> listAnswers,
											   MultiSelectionSpinner multiSpinner,
											   CustomCheckButtonGroup checkboxgrp,dataQuestionGroup thisQGroup)
	{
		boolean hideAdditionalInfo=true;
		boolean isAnyHideGiven=false;
		for (int k = 0;k < listAnswers.size(); k++)
		{
			if (listAnswers.get(k).getAdditionalInfoMandatory() != null
					&& listAnswers.get(k).getAdditionalInfoMandatory().length()>0

                    && listAnswers.get(k).getAdditionalInfoMandatory().equals("1")) {
				isAnyHideGiven = true;
				break;
			}
		}
		for (int k = 0;checkboxgrp!=null && k < listAnswers.size(); k++)
		{
			Answers a=(Answers)checkboxgrp.listRadio.get(k).radioIem.getTag();
			boolean cbChecked=false;
			if (a.getIsChecked().equals("true"))
				cbChecked= true;

			if (cbChecked && listAnswers.get(k).getAdditionalInfoMandatory() != null
					&& listAnswers.get(k).getAdditionalInfoMandatory().length()>0

                    && listAnswers.get(k).getAdditionalInfoMandatory().equals("1")) {
				hideAdditionalInfo = false;
				break;
			}
		}

		if (multiSpinner!=null)
			hideAdditionalInfo=QuestionnaireActivity.hideShowMiInMultipleDropDown(listAnswers,multiSpinner,thisQGroup.getQuestionObject());



		boolean miMandatory=thisQGroup.getQuestionObject()!=null && thisQGroup.getQuestionObject().getMiMandatory()!=null && Helper.comapreString(thisQGroup.getQuestionObject().getMiMandatory(), "1");
		if ((!miMandatory && hideAdditionalInfo==true && isAnyHideGiven) && thisQGroup != null
				&& (thisQGroup.getWholeMiView() != null
				|| thisQGroup.getbTextbox() != null || thisQGroup
				.getTextbox() != null)
				) {
			if (thisQGroup.getMiTextView()!=null)
			{
				thisQGroup.getMiTextView().setVisibility(
						RelativeLayout.GONE);
			}
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
		} else if (miMandatory || hideAdditionalInfo==false || isAnyHideGiven==false && thisQGroup != null
				&& (thisQGroup.getWholeMiView() != null
				|| thisQGroup.getbTextbox() != null || thisQGroup
				.getTextbox() != null)) {
			if (thisQGroup.getMiTextView()!=null)
			{
				thisQGroup.getMiTextView().setVisibility(
						RelativeLayout.VISIBLE);
			}
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
if (hideAdditionalInfo==false)
		return hideAdditionalInfo;
else if (hideAdditionalInfo==true && isAnyHideGiven==false) return hideAdditionalInfo;
else return true;
	}
	public ArrayList<QuestionnaireData> saveAnswers(Context con,
			ArrayList<QuestionnaireData> qs, Set set) {
		this.con = con;
		boolean isFound = false;
		QuestionnaireData qd = null;
		if (qGroups == null)
			return qs;
		for (int i = 0; i < qGroups.size(); i++) {

			isFound = false;
			for (int qcount = 0; qcount < qs.size(); qcount++) {
				qd = qs.get(qcount);
				if (qGroups.get(i) != null
						&& qd.getDataID().equals(
								qGroups.get(i).getQuestionObject().getDataID())) {

					if (qd != null) {
						qd = addAnswers(qGroups.get(i), qd);
						isFound = true;
						break;
					}
				}
			}
			if (qGroups.get(i) != null && isFound == false) {

				qd = addAnswers(qGroups.get(i), null);
				qs.add(qd);

				// DBHelper.updateThisQuestionnaire(table, columns,
				// questionnaireData, shelf_data, setId)
			}
			DBHelper.updateThisQuestionnaire(
					Constants.DB_TABLE_QUESTIONNAIRE,
					new String[] {
							Constants.DB_TABLE_QUESTIONNAIRE_DATAID,
							Constants.DB_TABLE_QUESTIONNAIRE_QTEXT,
							Constants.DB_TABLE_QUESTIONNAIRE_QVALUE,
							Constants.DB_TABLE_QUESTIONNAIRE_QTL,
							Constants.DB_TABLE_QUESTIONNAIRE_OT,
							Constants.DB_TABLE_ANSWERS_BRANCHID,
							Constants.DB_TABLE_ANSWERS_WORKERID,
							Constants.DB_TABLE_QUESTIONNAIRE_ORDERID,
							Constants.DB_TABLE_QUESTIONNAIRE_FT,
							Constants.DB_TABLE_SUBMITSURVEY_REPORTED_FINISH_TIME },
					qd, null, set.getSetID());
			DBAdapter.LogCommunication(
					"checkerDBLog.txt",
					"UIQuestionGroupHelper-updateThisQuestionnaire="
							+ set.getSetID());

		}

		return qs;
	}

	private QuestionnaireData addAnswers(dataQuestionGroup qGroup,
			QuestionnaireData qd) {
		Spinner tSpinner = qGroup.getSpinner();
		if (qd == null)
			qd = new QuestionnaireData();
		qd.setObjectType(qGroup.getQuestionObject().getObjectType());
		qd.setQuestionTypeLink(qGroup.getQuestionObject().getQuestionTypeLink());
		qd.setMiType(qGroup.getQuestionObject().getMiType());
		qd.setDataID(qGroup.getQuestionObject().getDataID());
		qd.setObjectCode(qGroup.getObjcode());
		qd.setOrderID(qGroup.getOrderID());
		qd.setQuestionText(qGroup.getQuestionObject().getQuestion());
		if (qGroup.getFreeTextbox() == null
				&& qGroup.getbTextbox() != null
				&& (qGroup.getSpinner() != null
						|| qGroup.getRadioGroup() != null || qGroup
						.getCheckboxgrp() != null)) {
			qGroup.setFreeTextbox(qGroup.getTextbox());
		}
		if (qGroup.getTextbox() != null) {
			qd.setAnswerText(qGroup.getTextbox().getText().toString());
		}
		if (qGroup.getFreeTextbox() != null) {

			final EditText tmpEdit = qGroup.getFreeTextbox();
			setFontSize(qGroup.getFreeTextbox());
			qd.setFreetext(qGroup.getFreeTextbox().getText().toString());
			tmpEdit.postDelayed(new Runnable() {
				@Override
				public void run() {
					try {
						InputMethodManager keyboard = (InputMethodManager) con
								.getSystemService(Context.INPUT_METHOD_SERVICE);
						keyboard.hideSoftInputFromWindow(
								tmpEdit.getWindowToken(), 0);
					} catch (Exception ex) {
					}
				}
			}, 10);

		}
		if (qGroup.getFinishTime() != null) {

			final EditText tmpEdit = qGroup.getFinishTime();

			setFontSize(qGroup.getFinishTime());
			qd.setFinishtime(qGroup.getFinishTime().getText().toString());
			tmpEdit.postDelayed(new Runnable() {
				@Override
				public void run() {
					try {
						InputMethodManager keyboard = (InputMethodManager) con
								.getSystemService(Context.INPUT_METHOD_SERVICE);
						keyboard.hideSoftInputFromWindow(
								tmpEdit.getWindowToken(), 0);
					} catch (Exception ex) {
					}
				}
			}, 10);

		}
		if (qGroup.getQuestionObject().getMiText()) {
			if (qGroup.getTextbox() != null) {

				setFontSize(qGroup.getTextbox());
				qd.setAnswerText(qGroup.getTextbox().getText().toString());
				qGroup.getQuestionObject().setMiText(false);
				final EditText tmpEdit = qGroup.getTextbox();
				tmpEdit.postDelayed(new Runnable() {
					@Override
					public void run() {
						try {
							InputMethodManager keyboard = (InputMethodManager) con
									.getSystemService(Context.INPUT_METHOD_SERVICE);
							keyboard.hideSoftInputFromWindow(
									tmpEdit.getWindowToken(), 0);
						} catch (Exception ex) {
						}
					}
				}, 10);

			}
		}
		if (qGroup.getQbjecttype() == 9 || qGroup.getQbjecttype() == 10) {
			qd = setBrachOrWorker(qGroup.getQbjecttype(), qGroup.getQd(),
					qGroup.getListWorkers(), qGroup.getWorkerListView(),
					qGroup.getSelectedWorker(), qGroup.getListBranches(),
					qGroup.getBranchListView(), qGroup.getSelectedBranch(),
					qGroup.getRadioGroup(), qGroup.getSpinner());
		} else if (qGroup.getQbjecttype() == 4) {
			// if (qGroup.getQtype() == 4)
			if (qGroup.getTextbox() != null)
				qd = getEditTextData(qd, qGroup.getTextbox());
			else if (qGroup.getbTextbox() != null)
				qd = getEditTextData(qd, qGroup.getbTextbox());
			// else
			{
				qd = getAnswers(qGroup.getQtype(), qGroup.getDisplayType(), qd,
						qGroup.getListAnswers(), qGroup.getCheckboxgrp(),
						qGroup.getRadioGroup(), qGroup.getcustomRadioGroup(),
						qGroup.getCustomCheckboxgrp(), tSpinner,
						qGroup.getMultiSpinner());
			}
		}
		return qd;
		// if (qd != null)
		// questionnaireData.add(qd);
	}

	// ///////////////////////VALIDATION
	private boolean ValidateForm2(Objects questionObject,
			dataQuestionGroup qGroupThis) {
		boolean isSimpleOkay = true;
		if (!Helper.comapreString(questionObject.getMandatory(), "1")
				&& !Helper.comapreString(questionObject.getMiMandatory(), "1")) {
			return true;
		} else if (Helper.comapreString(questionObject.getMandatory(), "1")) { // qGroupThis.setMandatory(true);

			if (questionObject.getQuestionTypeLink() == null)
				return true;
			if (!validateBlankAnswer(questionObject,
					Integer.parseInt(questionObject.getQuestionTypeLink()),
					Integer.parseInt(questionObject.getDisplayType()),
					qGroupThis)) {
				return false;
			}

			if ((Integer.parseInt(questionObject.getQuestionTypeLink()) == 8 || Integer
					.parseInt(questionObject.getQuestionTypeLink()) == 9)
					&& qGroupThis.getMultiSpinner() != null) {
				if (qGroupThis.isMandatory()) {
					List<Integer> answerInts = qGroupThis.getMultiSpinner()
							.getSelectedIndicies();
					if (answerInts!=null && answerInts.size()>0) return true;
					else return false;
				}
			}
		}

		boolean isMiOkay = true;
		if (Helper.comapreString(questionObject.getMiMandatory(), "1")) {
			if (qGroupThis != null && qGroupThis.getTextbox() != null) {
				if (qGroupThis.getTextbox().toString() != null
						&& qGroupThis.getTextbox().getText().toString()
								.length() > 0) {
					isMiOkay = true;
				} else if (qGroupThis.getTextbox().toString() != null
						&& qGroupThis.getTextbox().getText().toString()
								.length() <= 0) {
					isMiOkay = false;
				}
				isMiOkay = true;
			} else if (qGroupThis != null && qGroupThis.getbTextbox() != null) {
				if (qGroupThis.getbTextbox().toString() != null
						&& qGroupThis.getbTextbox().getText().toString()
								.length() > 0) {
					isMiOkay = true;
				} else if (qGroupThis.getbTextbox().toString() != null
						&& qGroupThis.getbTextbox().getText().toString()
								.length() <= 0) {
					isMiOkay = false;
				}
				isMiOkay = true;
			} else
				isMiOkay = true;
		}
		return true;
	}

	private boolean isValidChackBoxChecked(String val,
			LinearLayout checkboxgrp, CustomCheckButtonGroup customcheckgrp) {
		if (val == null)
			return true;
		int answercount = 0;
		for (int i = 0; i < checkboxgrp.getChildCount(); i++) {
			CheckBox cb = (CheckBox) checkboxgrp.getChildAt(i);
			if (cb.isChecked())
				answercount++;
		}
		if ((answercount > Integer.parseInt(val)) && !(val.equals("0")))
			return false;

		return true;
	}

	private boolean validateBlankAnswer(Objects qObj, int qtype,
			int displayType, dataQuestionGroup qGroupThis) {

		switch (qtype) {
		case 3:
		case 7:
			if (displayType == 0 || displayType == 1 || displayType==4)
				if (qGroupThis.getRadioGroup() != null) {
					int selectionIndex = qGroupThis.getRadioGroup()
							.getCheckedRadioButtonId();
					Answers answer = qGroupThis
							.getSelectedAnswer(selectionIndex);
					if (answer != null && qObj != null
							&& answer.getHideAdditionalInfo() != null
							&& answer.getHideAdditionalInfo().equals("1"))
						qObj.setMiGroupMandatory("0");
					else if (answer != null && qObj != null)
						qObj.setMiGroupMandatory(answer
								.getAdditionalInfoMandatory());
					return selectionIndex != -1;
				} else if (qGroupThis.getcustomRadioGroup() != null) {

					int selectionIndex = qGroupThis.getcustomRadioGroup()
							.getSelectedItemPosition();
					Answers answer = qGroupThis
							.getSelectedAnswer(selectionIndex);
					if (answer != null && qObj != null
							&& answer.getHideAdditionalInfo() != null
							&& answer.getHideAdditionalInfo().equals("1"))
						qObj.setMiGroupMandatory("0");
					else if (answer != null && qObj != null)
						qObj.setMiGroupMandatory(answer
								.getAdditionalInfoMandatory());
					return qGroupThis.getcustomRadioGroup()
							.getSelectedItemPosition() != -1;
				}
			else if (qGroupThis.getCheckboxgrp() != null)
					return isChackBoxChecked(qGroupThis.getCheckboxgrp(), null,
							qGroupThis, questionObject);
				else if (qGroupThis.getCustomCheckboxgrp() != null)
					return isChackBoxChecked(null,
							qGroupThis.getCustomCheckboxgrp(), qGroupThis,
							questionObject);


				else if (qGroupThis.getSpinner() != null) {
					if (qGroupThis.isMandatory()) {

						int selectionIndex = qGroupThis.getSpinner()
								.getSelectedItemPosition();
						Answers answer = qGroupThis
								.getSelectedAnswer(selectionIndex);
						if (answer != null && qObj != null
								&& answer.getHideAdditionalInfo() != null
								&& answer.getHideAdditionalInfo().equals("1"))
							qObj.setMiGroupMandatory("0");
						else if (answer != null && qObj != null)
							qObj.setMiGroupMandatory(answer
									.getAdditionalInfoMandatory());
						return qGroupThis.getSpinner()
								.getSelectedItemPosition() >= 1;
					} else {
						int selectionIndex = qGroupThis.getSpinner()
								.getSelectedItemPosition();
						Answers answer = qGroupThis
								.getSelectedAnswer(selectionIndex);
						if (answer != null && qObj != null
								&& answer.getHideAdditionalInfo() != null
								&& answer.getHideAdditionalInfo().equals("1"))
							qObj.setMiGroupMandatory("0");
						else if (answer != null && qObj != null)
							qObj.setMiGroupMandatory(answer
									.getAdditionalInfoMandatory());
						return qGroupThis.getSpinner()
								.getSelectedItemPosition() >= 0;
					}
				} else if (qGroupThis.getMultiSpinner() != null) {
					if (qGroupThis.isMandatory()) {
						boolean isOkay = qGroupThis.getMultiSpinner()
								.getSelectedIndicies() != null
								&& qGroupThis.getMultiSpinner()
										.getSelectedIndicies().size() >= 1;
						if (isOkay) {
							List<Integer> answerInts = qGroupThis
									.getMultiSpinner().getSelectedIndicies();
							for (int i = 0; i < answerInts.size(); i++) {
								Answers answer = qGroupThis
										.getSelectedAnswer(answerInts.get(i));
								if (answer != null
										&& qObj != null
										&& answer.getHideAdditionalInfo() != null
										&& answer.getHideAdditionalInfo()
												.equals("1"))
									questionObject.setMiGroupMandatory("0");
								else if (answer != null && qObj != null)
									questionObject.setMiGroupMandatory(answer
											.getAdditionalInfoMandatory());
							}
						}
					} else {
						List<Integer> answerInts = qGroupThis.getMultiSpinner()
								.getSelectedIndicies();
						for (int i = 0; i < answerInts.size(); i++) {
							Answers answer = qGroupThis
									.getSelectedAnswer(answerInts.get(i));
							if (answer != null
									&& qObj != null
									&& answer.getHideAdditionalInfo() != null
									&& answer.getHideAdditionalInfo().equals(
											"1"))
								questionObject.setMiGroupMandatory("0");
							else if (answer != null && qObj != null)
								questionObject.setMiGroupMandatory(answer
										.getAdditionalInfoMandatory());
						}
						return qGroupThis.getMultiSpinner()
								.getSelectedIndicies() != null
								&& qGroupThis.getMultiSpinner()
										.getSelectedIndicies().size() >= 1;
					}

				}
			break;

		case 4:
			if (qGroupThis.getTextbox() != null
					&& (!qGroupThis.getTextbox().getText().toString()
							.equals(""))) {
				return true;
			} else if (qGroupThis.getbTextbox() != null
					&& (!qGroupThis.getbTextbox().getText().toString()
							.equals(""))) {
				return true;
			}
			break;

		case 8:
		case 9:
			// if(displayType==4)
			if (qGroupThis.getCheckboxgrp() != null)
				return isChackBoxChecked(qGroupThis.getCheckboxgrp(), null,
						qGroupThis, questionObject);
			else if (qGroupThis.getCustomCheckboxgrp() != null)
				return isChackBoxChecked(null,
						qGroupThis.getCustomCheckboxgrp(), qGroupThis,
						questionObject);
			else if (qGroupThis.getMultiSpinner() != null) {
				List<Integer> answerInts = qGroupThis.getMultiSpinner()
						.getSelectedIndicies();
				for (int i = 0; i < answerInts.size(); i++) {
					Answers answer = qGroupThis.getSelectedAnswer(answerInts
							.get(i));
					if (answer != null && qObj != null
							&& answer.getHideAdditionalInfo() != null
							&& answer.getHideAdditionalInfo().equals("1"))
						qObj.setMiGroupMandatory("0");
					else if (answer != null && qObj != null)
						qObj.setMiGroupMandatory(answer
								.getAdditionalInfoMandatory());
				}
				if (qGroupThis.isMandatory())
					return qGroupThis.getMultiSpinner().getSelectedIndicies() != null
							&& qGroupThis.getMultiSpinner()
									.getSelectedIndicies().size() >= 1;
				else
					return qGroupThis.getMultiSpinner().getSelectedIndicies() != null
							&& qGroupThis.getMultiSpinner()
									.getSelectedIndicies().size() >= 1;
			} else
				return true;
		}
		return false;
	}

	private boolean isChackBoxChecked(LinearLayout checkboxgrp,
			CustomCheckButtonGroup customCheckButtonGroup,
			dataQuestionGroup qGroupThis, Objects questionObject) {
		if (checkboxgrp != null) {
			boolean isOkay = false;
			List<Integer> answerInts = new ArrayList<Integer>();
			for (int i = 0; i < checkboxgrp.getChildCount(); i++) {
				CheckBox cb = (CheckBox) checkboxgrp.getChildAt(i);
				if (cb.isChecked()) {
					answerInts.add(i);
					isOkay = true;
				}
			}
			for (int i = 0; i < answerInts.size(); i++) {
				Answers answer = qGroupThis
						.getSelectedAnswer(answerInts.get(i));
				if (answer.getAdditionalInfoMandatory() != null
						&& answer.getAdditionalInfoMandatory().equals("1")) {
					questionObject.setMiGroupMandatory(answer
							.getAdditionalInfoMandatory());
				}
			}
			return isOkay;
		} else if (customCheckButtonGroup != null
				&& customCheckButtonGroup.isAnySelected()) {

			List<Integer> answerInts = customCheckButtonGroup.answersSelected();
			for (int i = 0; i < answerInts.size(); i++) {
				Answers answer = qGroupThis
						.getSelectedAnswer(answerInts.get(i));
				if (questionObject!=null && answer.getAdditionalInfoMandatory() != null
						&& answer.getAdditionalInfoMandatory().equals("1")) {
					questionObject.setMiGroupMandatory(answer
							.getAdditionalInfoMandatory());
				}
			}
			return true;
		}
		return false;
	}

	// private boolean isChackBoxChecked(LinearLayout checkboxgrp,
	// CustomCheckButtonGroup customCheckButtonGroup) {
	// if (checkboxgrp != null) {
	// for (int i = 0; i < checkboxgrp.getChildCount(); i++) {
	// CheckBox cb = (CheckBox) checkboxgrp.getChildAt(i);
	// if (cb.isChecked())
	// return true;
	// }
	// } else if (customCheckButtonGroup != null
	// && customCheckButtonGroup.isAnySelected()) {
	//
	// return true;
	// }
	// return false;
	// }

	public String validateFreeTextBoxAnswer(Objects questionObject,
                                            EditText textbox, dataQuestionGroup thisQGroup) {
		if (textbox == null)
			return null;
		boolean isCorrect = false;
		int miType = Integer.parseInt(questionObject.getMiType());
		int val = textbox.getText().length();
		String textBoxErrorMessage = "";
        if (thisQGroup!=null
                && (thisQGroup.getCustomCheckboxgrp()!=null || thisQGroup.getMultiSpinner()!=null)
                && UIQuestionGroupHelper.hideShowMiInCheckBox(thisQGroup.getListAnswers(),thisQGroup.getMultiSpinner(),thisQGroup.getCustomCheckboxgrp(),thisQGroup)==false && val==0)
        {
            textBoxErrorMessage = con.getResources().getString(
                    R.string.questionnaire_alert_additional);
            return textBoxErrorMessage;
        }        else if (val == 0 && miType!=0 && questionObject.getMiMandatory() != null
				&& questionObject.getMiMandatory().equals("1")) {
			textBoxErrorMessage = con.getResources().getString(
					R.string.questionnaire_alert_additional);
			return textBoxErrorMessage;
		} else if (questionObject.getMiGroupMandatory() != null
				&& questionObject.getMiGroupMandatory().equals("0"))
			return null;

		switch (miType) {
			case 0:
				textBoxErrorMessage = null;
				return textBoxErrorMessage;

		case 1:

			try {
				val = textbox.getText().length();
				// if (val == 0 && IsMiMandatory == true)
				// isCorrect = false;
				// if (val == 0 && IsMiMandatory == false)
				// return true;
				double start = Double.parseDouble(questionObject
						.getMiFreeTextMinlength());
				double end = Double.parseDouble(questionObject
						.getMiFreeTextMaxlength());

				textBoxErrorMessage = con.getResources().getString(
						R.string.questionnaire_alert_text_between_first);
				textBoxErrorMessage = textBoxErrorMessage.replace("START",
						String.valueOf(start));
				textBoxErrorMessage = textBoxErrorMessage.replace("END",
						String.valueOf(end));
				if (textbox.getText().length() == 0 && (start == 0 && end == 0)) {
					isCorrect = true;
					textBoxErrorMessage = null;
					break;
				}
				if ((start == 0 && end == 0)) {
					textBoxErrorMessage = con.getResources().getString(
							R.string.questionnaire_alert_additional);
					textBoxErrorMessage = null;
					isCorrect = true;
				} else if (val >= start && val <= end) {
					isCorrect = true;
					textBoxErrorMessage = null;
				} else {
					isCorrect = false;

				}
			} catch (Exception e) {
				isCorrect = false;
			}
			break;
		case 5:
		case 6:
			isCorrect = true;
			textBoxErrorMessage = null;
			break;
		case 2:
			try {
				// if (textbox.getText().length() == 0 && IsMiMandatory == true)
				// return false;
				// if (textbox.getText().length() == 0 && IsMiMandatory ==
				// false)
				// return true;
				long start = Long.parseLong(questionObject.getMiNumberMin());
				long end = Long.parseLong(questionObject.getMiNumberMax());
				textBoxErrorMessage = con.getResources().getString(
						R.string.questionnaire_alert_text_betweeeen_first);
				textBoxErrorMessage = textBoxErrorMessage.replace("START",
						String.valueOf(start));
				textBoxErrorMessage = textBoxErrorMessage.replace("END",
						String.valueOf(end));

				if (textbox.getText().length() == 0) {
					isCorrect = true;
					textBoxErrorMessage = null;
					break;
				}
				double dval = 0.0;
				try {
					dval = Double.parseDouble(textbox.getText().toString());
				} catch (Exception ex) {
				}
				if ((start == 0 && end == 0)) {
					textBoxErrorMessage = con.getResources().getString(
							R.string.questionnaire_alert_additional);
					textBoxErrorMessage = null;
					isCorrect = true;
				} else if (dval >= start && dval <= end) {
					isCorrect = true;
					textBoxErrorMessage = null;
				} else
					isCorrect = false;
			} catch (Exception e) {
				isCorrect = false;
			}
			break;
		case 3:
			isCorrect = true;
			textBoxErrorMessage = null;
			break;
		case 4:
			isCorrect = true;
			textBoxErrorMessage = null;
			break;

		case 16:
			isCorrect = false;
			textBoxErrorMessage = con.getResources().getString(
					R.string.email_validation_text);

			if (textbox == null || textbox.getText() == null)
				break;
			if (textbox.getText().length() == 0
					&& questionObject.getMiGroupMandatory() != null
					&& questionObject.getMiGroupMandatory().equals("1"))
				break;
			if (Patterns.EMAIL_ADDRESS.matcher(textbox.getText().toString())
					.matches()) {
				textBoxErrorMessage = null;
			}
			break;
		}
		return textBoxErrorMessage;
	}

	public String validateFreeTextBoxAnswerButton(Objects questionObject,
			Button textbox) {
		if (textbox == null)
			return null;
		boolean isCorrect = false;
		int miType = Integer.parseInt(questionObject.getMiType());
		int val = textbox.getText().length();
		String textBoxErrorMessage = "";
		// if (validateadditionjumpaprams())
		// return true;
		// if (validateadditionjumpapramsonly())
		// return true;
		if (val == 0 && questionObject.getMiMandatory() != null
				&& questionObject.getMiMandatory().equals("1")) {
			textBoxErrorMessage = con.getResources().getString(
					R.string.questionnaire_alert_additional);
			return textBoxErrorMessage;
		}

		switch (miType) {
		case 1:

			try {
				val = textbox.getText().length();
				// if (val == 0 && IsMiMandatory == true)
				// isCorrect = false;
				// if (val == 0 && IsMiMandatory == false)
				// return true;
				int start = Integer.parseInt(questionObject
						.getMiFreeTextMinlength());
				int end = Integer.parseInt(questionObject
						.getMiFreeTextMaxlength());

				textBoxErrorMessage = con.getResources().getString(
						R.string.questionnaire_alert_text_between_first);
				textBoxErrorMessage = textBoxErrorMessage.replace("START",
						String.valueOf(start));
				textBoxErrorMessage = textBoxErrorMessage.replace("END",
						String.valueOf(end));
				if (textbox.getText().length() == 0) {
					isCorrect = true;
					textBoxErrorMessage = null;
					break;
				}
				if ((start == 0 && end == 0)) {
					textBoxErrorMessage = con.getResources().getString(
							R.string.questionnaire_alert_additional);
					textBoxErrorMessage = null;
					isCorrect = true;
				} else if (val >= start && val <= end) {
					isCorrect = true;
					textBoxErrorMessage = null;
				} else {
					isCorrect = false;

				}
			} catch (Exception e) {
				isCorrect = false;
			}
			break;
		case 5:
			isCorrect = true;
			textBoxErrorMessage = null;
			break;
		case 2:
			try {
				// if (textbox.getText().length() == 0 && IsMiMandatory == true)
				// return false;
				// if (textbox.getText().length() == 0 && IsMiMandatory ==
				// false)
				// return true;
				int start = Integer.parseInt(questionObject.getMiNumberMin());
				int end = Integer.parseInt(questionObject.getMiNumberMax());
				textBoxErrorMessage = con.getResources().getString(
						R.string.questionnaire_alert_text_betweeeen_first);
				textBoxErrorMessage = textBoxErrorMessage.replace("START",
						String.valueOf(start));
				textBoxErrorMessage = textBoxErrorMessage.replace("END",
						String.valueOf(end));

				if (textbox.getText().length() == 0) {
					isCorrect = true;
					textBoxErrorMessage = null;
					break;
				}
				double dval = 0.0;
				try {
					dval = Double.parseDouble(textbox.getText().toString());
				} catch (Exception ex) {
				}

				if ((start == 0 && end == 0)) {
					textBoxErrorMessage = con.getResources().getString(
							R.string.questionnaire_alert_additional);
					textBoxErrorMessage = null;
					isCorrect = true;
				} else if (dval >= start && dval <= end) {
					isCorrect = true;
					textBoxErrorMessage = null;
				} else
					isCorrect = false;
			} catch (Exception e) {

				isCorrect = false;
			}
			break;
		case 3:
			isCorrect = true;
			textBoxErrorMessage = null;
			break;
		case 4:
			isCorrect = true;
			textBoxErrorMessage = null;
			break;
		}
		return textBoxErrorMessage;
	}

	public String validateAnswers2(Context con) {
		this.con = con;
		for (int i = 0; i < qGroups.size(); i++) {
			QuestionnaireData qd = null;
			if (qGroups.get(i) == null)
				continue;
			if (IsObjectdisplaybyCondition(qGroups.get(i).getQuestionObject()
					.getObjectDisplayCondition(), true)) {
				if (qGroups.get(i) == null
						|| ValidateForm2(qGroups.get(i).getQuestionObject(),
								qGroups.get(i))) {

					if (qGroups.get(i).getbTextbox() != null) {
						String bool = validateFreeTextBoxAnswerButton(qGroups
								.get(i).getQuestionObject(), qGroups.get(i)
								.getbTextbox());
						if (qGroups.get(i).getbTextbox() == null
								|| bool == null) {
							continue;
						} else
							return bool;
					}

					{
						String bool = validateFreeTextBoxAnswer(qGroups.get(i)
								.getQuestionObject(), qGroups.get(i)
								.getTextbox(),qGroups.get(i));
						if (qGroups.get(i).getTextbox() == null || bool == null) {
							continue;
						} else
							return bool;
					}

				} else
					return "err";
			} else {
				continue;
			}

		}
		return null;
	}
	// ////////////////////////////////////
}
