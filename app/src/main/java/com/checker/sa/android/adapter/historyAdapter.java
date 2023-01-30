package com.checker.sa.android.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.checker.sa.android.data.CritHistoryData;
import com.checker.sa.android.data.CustomFields;
import com.checker.sa.android.data.HistoryFields;
import com.checker.sa.android.data.RefundData;
import com.checker.sa.android.helper.Helper;
import com.mor.sa.android.activities.R;

public class historyAdapter extends ArrayAdapter<HistoryFields> {
	private final Context context;
	private final ArrayList<HistoryFields> values;

	public void openDialog(HistoryFields data) {
		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.history_dialog_full);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.height = WindowManager.LayoutParams.MATCH_PARENT;
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		dialog.getWindow().setLayout(lp.width, lp.height);

		TextView txtHEading = (TextView) dialog.findViewById(R.id.txtHeading);
		txtHEading.setText(data.getCritID());
		LinearLayout layoutView = (LinearLayout) dialog
				.findViewById(R.id.toplayout);
		if (Helper.getSystemURL()!=null && Helper.getSystemURL().toLowerCase().contains("ajis")) {}
		else {

			layoutView.addView(addNewView(data.getActualFinishTime(),
					"Actual Finish Time"));
		}
		layoutView.addView(addNewView(data.getClientName(), "Client Name"));
		layoutView.addView(addNewView(data.getRegionName(), "Region Name"));
		layoutView.addView(addNewView(data.getSetName(), "Set Name"));
		layoutView.addView(addNewView(data.getStatus(), "Status"));
		layoutView.addView(addNewView(data.getBranchName(), "Branch Name"));

		layoutView.addView(addNewView(data.getAddress(), "Address"));
		layoutView.addView(addNewView(data.getCityName(), "City Name"));
		if (Helper.getSystemURL()!=null && Helper.getSystemURL().toLowerCase().contains("ajis")) {}
		else {

			layoutView.addView(addNewView(data.getResult(), "Result"));
		}
		layoutView.addView(addNewView(data.getWasSentBack(), "Was Sent Back"));
		if (Helper.getSystemURL()!=null && Helper.getSystemURL().toLowerCase().contains("ajis")) {}
		else {
			layoutView
					.addView(addNewView(data.getQaDoneByUser(), "QA done by User"));
			layoutView.addView(addNewView(data.getQaGradeAdjusted(),
					"QA grade adjusted"));
			layoutView.addView(addNewView(data.getQaNote(), "QA Note"));
		}
		layoutView.addView(addNewView(data.getFinishTime(), "Finish Time"));
		layoutView.addView(addNewView(data.getLinkedMoneySum(),
				"Linked Money Sum"));
		if (Helper.getSystemURL()!=null && Helper.getSystemURL().toLowerCase().contains("ajis")) {}
		else {
			addNewViews(data.getCustomFields(), layoutView);
		}
		dialog.findViewById(R.id.xbutton).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
		dialog.show();
	}

	@Override
	public int getCount() {
		if (values != null)
			return values.size();
		return 0;
	}

	public historyAdapter(Context context, ArrayList<HistoryFields> valuess) {
		super(context, R.layout.menu_row, valuess);
		this.values = valuess;
		this.context = context;
	}

	String tmpHeading = "";
	private int isDetailed;

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = null;
		rowView = inflater.inflate(R.layout.history_dialog, parent, false);
		rowView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				HistoryFields rf = values.get(position);

				openDialog(rf);
			}

		});

		TextView txtHEading = (TextView) rowView.findViewById(R.id.txtHeading);
		txtHEading.setText(values.get(position).getCritID());
		LinearLayout layoutView = (LinearLayout) rowView
				.findViewById(R.id.toplayout);
		// layoutView.addView(addNewView(values.get(position)
		// .getActualFinishTime(), "Actual Finish Time"));
		layoutView.addView(addNewView(values.get(position).getClientName(),
				"Client Name"));
		// layoutView.addView(addNewView(values.get(position).getRegionName(),
		// "Region Name"));
		// layoutView.addView(addNewView(values.get(position).getSetName(),
		// "Set Name"));
		// layoutView.addView(addNewView(values.get(position).getStatus(),
		// "Status"));
		// layoutView.addView(addNewView(values.get(position).getBranchName(),
		// "Branch Name"));
		//
		// layoutView.addView(addNewView(values.get(position).getAddress(),
		// "Address"));
		// layoutView.addView(addNewView(values.get(position).getCityName(),
		// "City Name"));
		// layoutView.addView(addNewView(values.get(position).getResult(),
		// "Result"));
		// layoutView.addView(addNewView(values.get(position).getWasSentBack(),
		// "Was Sent Back"));
		// layoutView.addView(addNewView(values.get(position).getQaDoneByUser(),
		// "QA done by User"));
		// layoutView
		// .addView(addNewView(values.get(position).getQaGradeAdjusted(),
		// "QA grade adjusted"));
		// layoutView.addView(addNewView(values.get(position).getQaNote(),
		// "QA Note"));
		layoutView.addView(addNewView(values.get(position).getFinishTime(),
				"Finish Time"));
		// layoutView.addView(addNewView(values.get(position).getLinkedMoneySum(),
		// "Linked Money Sum"));
		//
		// addNewViews(values.get(position).getCustomFields(), layoutView);

		return rowView;
	}

	private void addNewViews(ArrayList<CustomFields> customFields,
			LinearLayout layoutView) {
		for (int i = 0; customFields != null && i < customFields.size(); i++) {
			try {
				layoutView.addView(addNewView(customFields.get(i).getValue(),
						customFields.get(i).getName()));
			} catch (Exception ex) {
			}
		}
	}

	private View addNewView(String value, String title) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = null;
		rowView = inflater.inflate(R.layout.history_item, null, false);
		TextView tvTitle = (TextView) rowView.findViewById(R.id.history_title);
		TextView tvDetail = (TextView) rowView
				.findViewById(R.id.history_detail);
		tvTitle.setText(title);
		tvDetail.setText(value);
		return rowView;
	}
}
