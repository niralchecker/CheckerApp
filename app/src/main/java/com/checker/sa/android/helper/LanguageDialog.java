package com.checker.sa.android.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.checker.sa.android.data.AltLanguage;
import com.mor.sa.android.activities.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class LanguageDialog implements OnMultiChoiceClickListener {
	String[] _items = null;
	boolean[] mSelection = null;

	ArrayAdapter<String> simple_adapter;
	private android.content.DialogInterface.OnClickListener btnListener;
	private android.content.DialogInterface.OnClickListener btnNegListener;
	private Context context;
	private OnItemSelectedListener OnItemListener;
	private String title;

	public void setOnItemListener(OnItemSelectedListener itemListener) {
		this.OnItemListener = itemListener;
	}

	public void setBtnListener(
			android.content.DialogInterface.OnClickListener btnListener,
			android.content.DialogInterface.OnClickListener btnNegListener) {
		this.btnListener = btnListener;
		this.btnNegListener = btnNegListener;
	}

	public LanguageDialog(Context context, String title) {
		this.context = context;
		simple_adapter = new ArrayAdapter<String>(context,
				android.R.layout.simple_spinner_item);

		this.title = title;
	}

	public LanguageDialog(Context context, AttributeSet attrs) {
		this.context = context;
		simple_adapter = new ArrayAdapter<String>(context,
				android.R.layout.simple_spinner_item);
	}

	public void onClick(DialogInterface dialog, int which, boolean isChecked) {
		if (mSelection != null && which < mSelection.length) {
			mSelection[which] = isChecked;

			simple_adapter.clear();
			simple_adapter.add(buildSelectedItemString());
		} else {
			throw new IllegalArgumentException(
					"Argument 'which' is out of bounds.");
		}
	}

	public boolean performClick(ArrayList<AltLanguage> listAnswers) {

		ArrayList<String> listFinalAnswers = new ArrayList<String>();
		ArrayList<String> listFinalSelectedAnswers = new ArrayList<String>();
		for (int ansCount = 0; ansCount < listAnswers.size(); ansCount++) {
			AltLanguage answers = listAnswers.get(ansCount);

			listFinalAnswers.add(answers.getAltLangName());
			if (listAnswers.get(ansCount).getIsSelected() != null
					&& listAnswers.get(ansCount).getIsSelected().equals("1")) {
				listFinalSelectedAnswers.add(answers.getAltLangName());
			}
		}
		setItems(listFinalAnswers);
		setSelection(listFinalSelectedAnswers);

		TextView textView = new TextView(context);
		textView.setPadding(20, 30, 20, 30);
		textView.setTextSize(20F);
		textView.setBackgroundColor(Color.parseColor("#1792ff"));
		textView.setTextColor(Color.WHITE);
		textView.setText(title);

		final AlertDialog.Builder builder = new AlertDialog.Builder(context);
		if (title != null && title.length() > 0) {
//			builder.setTitle(title);
			builder.setCustomTitle(textView);
		}

		builder.setMultiChoiceItems(_items, mSelection, this);
		if (this.btnListener != null)
			builder.setPositiveButton(
					context.getResources()
							.getString(R.string.save_and_download),
					this.btnListener);
		builder.setNegativeButton(
				context.getResources().getString(
						R.string.non_answered_conf_alert_cancel_btn),
				this.btnNegListener);
		builder.show();
		builder.setCancelable(false);
		return true;
	}

	public void setItems(String[] items) {
		_items = items;
		mSelection = new boolean[_items.length];
		simple_adapter.clear();
		simple_adapter.add(_items[0]);
		Arrays.fill(mSelection, false);
	}

	public void setItems(List<String> items) {
		_items = items.toArray(new String[items.size()]);
		mSelection = new boolean[_items.length];
		simple_adapter.clear();
		simple_adapter.add(_items[0]);
		Arrays.fill(mSelection, false);
	}

	public void setSelection(String[] selection) {
		for (String cell : selection) {
			for (int j = 0; j < _items.length; ++j) {
				if (_items[j].equals(cell)) {
					mSelection[j] = true;
				}
			}
		}
	}

	public void setSelection(List<String> selection) {
		for (int i = 0; i < mSelection.length; i++) {
			mSelection[i] = false;
		}
		for (String sel : selection) {
			for (int j = 0; j < _items.length; ++j) {
				if (_items[j].equals(sel)) {
					mSelection[j] = true;
				}
			}
		}
		simple_adapter.clear();
		simple_adapter.add(buildSelectedItemString());
	}

	public void setSelection(int index) {
		for (int i = 0; i < mSelection.length; i++) {
			mSelection[i] = false;
		}
		if (index >= 0 && index < mSelection.length) {
			mSelection[index] = true;
		} else {
			throw new IllegalArgumentException("Index "
					+ index
					+ context.getResources().getString(
							R.string.save_and_download));
		}
		simple_adapter.clear();
		simple_adapter.add(buildSelectedItemString());
	}

	public void setSelection(int[] selectedIndicies) {
		for (int i = 0; i < mSelection.length; i++) {
			mSelection[i] = false;
		}
		for (int index : selectedIndicies) {
			if (index >= 0 && index < mSelection.length) {
				mSelection[index] = true;
			} else {
				throw new IllegalArgumentException("Index "
						+ index
						+ context.getResources().getString(
								R.string.save_and_download));
			}
		}
		simple_adapter.clear();
		simple_adapter.add(buildSelectedItemString());
	}

	public List<String> getSelectedStrings() {
		List<String> selection = new LinkedList<String>();
		for (int i = 0; i < _items.length; ++i) {
			if (mSelection[i]) {
				selection.add(_items[i]);
			}
		}
		return selection;
	}

	public List<Integer> getSelectedIndicies() {
		List<Integer> selection = new LinkedList<Integer>();
		for (int i = 0; i < _items.length; ++i) {
			if (mSelection[i]) {
				selection.add(i);
			}
		}
		return selection;
	}

	private String buildSelectedItemString() {
		StringBuilder sb = new StringBuilder();
		boolean foundOne = false;

		for (int i = 0; i < _items.length; ++i) {
			if (mSelection[i]) {
				if (foundOne) {
					sb.append(", ");
				}
				foundOne = true;

				sb.append(_items[i]);
			}
		}
		return sb.toString();
	}

	public String getSelectedItemsAsString() {
		StringBuilder sb = new StringBuilder();
		boolean foundOne = false;

		for (int i = 0; i < _items.length; ++i) {
			if (mSelection[i]) {
				if (foundOne) {
					sb.append(", ");
				}
				foundOne = true;
				sb.append(_items[i]);
			}
		}
		return sb.toString();
	}
}
