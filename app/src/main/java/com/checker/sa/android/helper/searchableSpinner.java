package com.checker.sa.android.helper;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.checker.sa.android.data.Answers;
import com.checker.sa.android.data.Objects;
import com.checker.sa.android.data.QuestionnaireData;
import com.mor.sa.android.activities.QuestionnaireActivity;
import com.mor.sa.android.activities.R;

public class searchableSpinner {
//	int viewId = -1;
//	private ImageView workerBranchImage;
//	Spinner workerDropDown = null;
//	ListView workerListView = null;
//	int workerListViewListener = -1;
//	EditText workerEditText = null;
//	int selectedWorker = -1;
//	
//	
//	private void setFontSize(Context con,View v) {
//
//		try {
//			if (v.getClass().equals(EditText.class)) {
//				EditText btnView = (EditText) v;
//
//				btnView.setTextSize(UIHelper.getFontSize(
//						con, btnView.getTextSize()));
//				if (Helper.getTheme(con) == 0) {
//					btnView.setTextColor(con.getResources().getColor(
//							android.R.color.black));
//				}
//			}
//
//		} catch (Exception ex) {
//
//		}
//
//		try {
//			if (v.getClass().equals(Button.class)) {
//				Button btnView = (Button) v;
//
//				btnView.setTextSize(UIHelper.getFontSize(
//						con, btnView.getTextSize()));
//			}
//		} catch (Exception ex) {
//
//		}
//
//		try {
//			if (v.getClass().equals(CheckBox.class)) {
//				CheckBox btnView = (CheckBox) v;
//
//				btnView.setTextSize(UIHelper.getFontSize(
//						con, btnView.getTextSize()));
//				if (Helper.getTheme(con) == 0) {
//					btnView.setTextColor(con.getResources().getColor(
//							android.R.color.white));
//				}
//			}
//		} catch (Exception ex) {
//
//		}
//
//		try {
//			if (v.getClass().equals(RadioButton.class)) {
//				RadioButton btnView = (RadioButton) v;
//
//				btnView.setTextSize(UIHelper.getFontSize(
//						con, btnView.getTextSize()));
//				if (Helper.getTheme(con) == 0) {
//					btnView.setTextColor(con.getResources().getColor(
//							android.R.color.white));
//				}
//			}
//		} catch (Exception ex) {
//
//		}
//		try {
//			if (v.getClass().equals(TextView.class)) {
//				TextView textView = (TextView) v;
//
//				textView.setTextSize(UIHelper.getFontSize(
//						con, textView.getTextSize()));
//				if (Helper.getTheme(con) == 0) {
//					textView.setTextColor(con.getResources().getColor(
//							android.R.color.white));
//				}
//			}
//		} catch (Exception ex) {
//
//		}
//
//	}
//
//	private EditText displayListEdit(Context con,String text) {
//		// Constants.QUESTIONNAIRE_JUMPTO_LBL
//		EditText tv = new EditText(con);
//		tv.setText(Helper.makeHtmlString(text));
//		tv.setTextColor(Color.BLACK);
//		viewId = getViewId(viewId);
//		tv.setId(viewId);
//		setFontSize(con,tv);
//		return tv;
//	}
//	
//	private int getViewId(int id) {
//		int val = id + 1;
//		return val;
//	}
//
//	private ImageView displayupdownicon(Context con, Drawable drawable) {
//		// Constants.QUESTIONNAIRE_JUMPTO_LBL
//		ImageView tv = new ImageView(con);
//		tv.setBackgroundDrawable(drawable);
//		viewId = getViewId(viewId);
//		tv.setId(viewId);
//		return tv;
//	}
//	
//	private RelativeLayout.LayoutParams getIconParam() {
//		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
//				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
//				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
//		// lp.addRule(setAlignmentOfView(Helper.getInt(questionObject.getAlign())));
//		lp.setMargins(5, 0, 0, 5);
//		lp.addRule(RelativeLayout.BELOW, viewId - 1);
//		lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//		lp.addRule(RelativeLayout.CENTER_VERTICAL);
//		lp.addRule(RelativeLayout.ALIGN_RIGHT);
//		return lp;
//	}
//	
//	private RelativeLayout.LayoutParams getSearchEditTextLayoutParam() {
//		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
//				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
//				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
//		//lp.addRule(setAlignmentOfView(0));
//		lp.setMargins(0, 5, 0, 5);
//		lp.addRule(RelativeLayout.BELOW, viewId - 2);
//		lp.addRule(RelativeLayout.LEFT_OF, viewId - 1);
//		return lp;
//	}
//	
//	private ListView getWorkerListView(int selection,boolean IsMandatory,String[] array_spinner,QuestionnaireData qd,Context con,int viewidd, Objects questionObject) {
//
//		final ListView s = new ListView(con);
//		
//		for (int i = 0; i < listAnswers.size(); i++) {
//			array_spinner[i + 1] = Helper.stripHtml(listAnswers.get(i)
//					.getWorkerName());
//			String workerId = listAnswers.get(i).getWorkerID();
//
//			if (qd != null && qd.getWorkerID() != null
//					&& qd.getWorkerID().equals(workerId)) {
//				if (selectedWorker == -1)
//					selectedWorker = i + 1;
//			}
//		}
//		ArrayAdapter adapter = new ArrayAdapter(this,
//				UIHelper.getSpinnerLayoutSize(QuestionnaireActivity.this),
//				array_spinner);
//
//		s.setAdapter(adapter);
//		s.setBackgroundColor(Color.GRAY);
//		if (selectedWorker == -1)
//			selectedWorker = 0;
//		workerListViewListener = -1;
//		if (array_spinner[selectedWorker].toLowerCase().equals("not selected")) {
//			workerEditText.setText("");
//			workerEditText.setHint(array_spinner[selectedWorker]);
//		} else
//			workerEditText.setText(Helper
//					.makeHtmlString(array_spinner[selectedWorker]));
//		s.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//					long arg3) {
//				InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//
//				inputManager.hideSoftInputFromWindow(getCurrentFocus()
//						.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//				selectedWorker = arg2;
//				if (spinnerFirstTime)
//					spinnerFirstTime = false;
//				else if (isServerAnswersActAsSubmit) {
//					nextbtnClickListener();
//					spinnerFirstTime = true;
//				}
//				workerListViewListener = -1;
//				workerListView.setVisibility(View.GONE);
//				workerBranchImage.setBackgroundDrawable(getResources()
//						.getDrawable(R.drawable.down_btn));
//
//				for (int i = 0; i < array_spinner.length; i++) {
//					final String text = ((TextView) arg1).getText().toString();
//					if (array_spinner[i].equals(text)) {
//						selectedWorker = i;
//					}
//				}
//				if (array_spinner[selectedWorker].toLowerCase().equals(
//						"not selected")) {
//					workerEditText.setText("");
//					workerEditText.setHint(array_spinner[selectedWorker]);
//				} else
//					workerEditText.setText(Helper
//							.makeHtmlString(array_spinner[selectedWorker]));
//			}
//
//		});
//		return s;
//	}
//
//	public void makeSearchAbleSPinner(Objects questionObject,RelativeLayout questionnaireLayout,final Context con, int id) {
//		viewId = id;
//		workerBranchImage = displayupdownicon(con, con.getResources()
//				.getDrawable(R.drawable.down_btn));
//		workerBranchImage.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				if (workerListView.getVisibility() == View.GONE) {
//					workerListView.setVisibility(View.VISIBLE);
//					workerBranchImage.setBackgroundDrawable(con.getResources()
//							.getDrawable(R.drawable.up_btn));
//
//				} else {
//					workerListView.setVisibility(View.GONE);
//					workerBranchImage.setBackgroundDrawable(con.getResources()
//							.getDrawable(R.drawable.down_btn));
//				}
//
//			}
//		});
//		questionnaireLayout.addView((workerBranchImage), getIconParam());
//		workerEditText = displayListEdit(con,"");
//		questionnaireLayout.addView((workerEditText),
//				getSearchEditTextLayoutParam());
//
//		viewId = getViewId(viewId);
//		workerListView = getWorkerListView(viewId, questionObject);
//		workerListView.setVisibility(View.GONE);
//		workerListView.setCacheColorHint(Color.TRANSPARENT);
//		workerListView.setOnTouchListener(new ListView.OnTouchListener() {
//
//			@Override
//			public boolean onTouch(View arg0, MotionEvent arg1) {
//				int action = arg1.getAction();
//				switch (action) {
//				case MotionEvent.ACTION_DOWN:
//					// Disallow ScrollView to
//					// intercept touch events.
//					arg0.getParent().requestDisallowInterceptTouchEvent(true);
//					break;
//
//				case MotionEvent.ACTION_UP:
//					// Allow ScrollView to intercept
//					// touch events.
//					arg0.getParent().requestDisallowInterceptTouchEvent(false);
//					break;
//				}
//
//				// Handle ListView touch events.
//				arg0.onTouchEvent(arg1);
//				return true;
//			}
//		});
//
//		workerEditText.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				// workerEditText.setText("");
//				if (workerListView.getVisibility() == View.GONE) {
//					workerListView.setVisibility(View.VISIBLE);
//					workerBranchImage.setBackgroundDrawable(con.getResources()
//							.getDrawable(R.drawable.up_btn));
//
//				}
//			}
//		});
//
//		workerEditText.addTextChangedListener(new TextWatcher() {
//
//			@Override
//			public void onTextChanged(CharSequence cs, int arg1, int arg2,
//					int arg3) {
//				// When user changed the Text
//				if (workerListViewListener != -1) {
//					((ArrayAdapter) (workerListView.getAdapter())).getFilter()
//							.filter(cs);
//					if (workerListView.getVisibility() == View.GONE) {
//						workerListView.setVisibility(View.VISIBLE);
//						workerBranchImage.setBackgroundDrawable(con.getResources()
//								.getDrawable(R.drawable.up_btn));
//
//					}
//				} else
//					workerListViewListener = 0;
//
//			}
//
//			@Override
//			public void beforeTextChanged(CharSequence arg0, int arg1,
//					int arg2, int arg3) {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void afterTextChanged(Editable arg0) {
//				// TODO Auto-generated method stub
//
//			}
//		});
//
//	}
}
