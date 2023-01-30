package com.checker.sa.android.adapter;

import android.app.Dialog;
import android.content.Context;
import android.media.Image;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.checker.sa.android.data.CustomFields;
import com.checker.sa.android.data.HistoryFields;
import com.checker.sa.android.data.Order;
import com.checker.sa.android.data.OrderOrFile;
import com.mor.sa.android.activities.ArchiveActivity;
import com.mor.sa.android.activities.R;

import java.util.ArrayList;

public class archiveAdapter extends ArrayAdapter<OrderOrFile> {
	private final ArchiveActivity context;
	private final ArrayList<OrderOrFile> values;
	@Override
	public int getCount() {
		if (values != null)
			return values.size();
		return 0;
	}

	public archiveAdapter(ArchiveActivity context, ArrayList<OrderOrFile> valuess) {
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
		rowView = inflater.inflate(R.layout.archive_item, parent, false);
		ImageView imgUpload= (ImageView)rowView.findViewById(R.id.imgUpload);
		imgUpload.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				context.sendThisJobToServer(values.get(position).getSubmitArchiveData());
			}
		});
		TextView txtHEading = (TextView) rowView.findViewById(R.id.txtHeading);
		//values.get(position).setStatusName("archived");
		if (values.get(position).getOrderID().contains("-"))
		{

			//txtHEading.setText("Survey");
			imgUpload.setVisibility(RelativeLayout.GONE);
		}
		else txtHEading.setText(values.get(position).getOrderID());
		LinearLayout layoutView = (LinearLayout) rowView
				.findViewById(R.id.toplayout);
		// layoutView.addView(addNewView(values.get(position)
		// .getActualFinishTime(), "Actual Finish Time"));

		if (values.get(position).getOrderID().contains("-") && values.get(position).getOrder()!=null)
		{
			layoutView.addView(addNewView(values.get(position).getOrder().getBranchName(),
					context.getResources().getString(R.string.jd_survey_name)));//jd_survey_name
		}
		else if(values.get(position).getOrder()!=null)
			layoutView.addView(addNewView(values.get(position).getOrder().getClientName(),
					context.getResources().getString(R.string.s_item_column_0_line_67_file_37)));//client name

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
		if (values.get(position).getOrderID().contains("-"))
		{
//			layoutView.addView(addNewView(values.get(position).getBranchFullname(),
//					"Branch"));
		}
		else if(values.get(position).getOrder()!=null)
			layoutView.addView(addNewView(values.get(position).getOrder().getBranchFullname(),
					context.getResources().getString(R.string.s_item_column_0_line_362_file_52)));
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
