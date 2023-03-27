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

import com.checker.sa.android.data.RefundData;
import com.checker.sa.android.helper.Helper;
import com.mor.sa.android.activities.R;

public class refundAdapter extends ArrayAdapter<RefundData> {
	private final Context context;
	private final ArrayList<RefundData> values;

	@Override
	public int getCount() {
		if (values != null)
			return values.size();
		return 0;
	}

	public refundAdapter(Context context, ArrayList<RefundData> valuess,
			int isDetailed) {
		super(context, R.layout.menu_row, valuess);
		this.context = context;
		if (isDetailed > 0) {
			this.values = sortbyClientOrFlow(isDetailed == 1, valuess); // by
																		// client
																		// == 1
		} else {
			this.values = valuess;
		}
		this.isDetailed = isDetailed;
	}

	private ArrayList<RefundData> sortbyClientOrFlow(boolean isClient,
			ArrayList<RefundData> valuess) {
		ArrayList<RefundData> tmp = new ArrayList<RefundData>();
		{
			// sort by cient

			for (int i = 0; i < valuess.size(); i++) {
				String ClientName = valuess.get(i).getClientName();
				RefundData rf = new RefundData();
				rf.setCritLink(ClientName);

				String flowType = valuess.get(i).getFlowTypeLink();

				double total = 0.0;
				try {
					total += Double.parseDouble(valuess.get(i).getValue());
				} catch (Exception ex) {

				}
				for (int j = i + 1; j < valuess.size(); j++) {
					String tempClient = valuess.get(j).getClientName();

					if ((!isClient
							&& flowType
									.equals(valuess.get(j).getFlowTypeLink()) && tempClient
								.equals(ClientName))// check for flowtype
							|| (isClient && tempClient.equals(ClientName)))// chk
																			// for
																			// client
																			// type
																			// grouping
					{
						try {
							total += Double.parseDouble(valuess.get(j)
									.getValue());
						} catch (Exception ex) {

						}
						valuess.remove(j);
						j--;
					}
				}
				rf.setValue(total + "");
				rf.setProjectName(valuess.get(i).getProjectName());
				rf.setFlowTypeLink(valuess.get(i).getFlowTypeLink());
				tmp.add(rf);
			}
		}
		return tmp;
	}

	String tmpHeading = "";
	private int isDetailed;

	public void updateRow(RefundData data, View dialog, int isDetailed2) {

		TextView status_text, critlink, approved_paid, addedAt, flowType, value, invoiceNumber, approved, paid, clientName, branchCode, branchName, cityName, regionName, address, projectName, description, orderID;
		addedAt = (TextView) dialog.findViewById(R.id.AddedAt);
		flowType = (TextView) dialog.findViewById(R.id.FlowType);
		value = (TextView) dialog.findViewById(R.id.Value);
		invoiceNumber = (TextView) dialog.findViewById(R.id.InvoiceNumber);
		approved = (TextView) dialog.findViewById(R.id.Approved);
		paid = (TextView) dialog.findViewById(R.id.Paid);
		clientName = (TextView) dialog.findViewById(R.id.ClientName);
		branchCode = (TextView) dialog.findViewById(R.id.BranchCode);
		branchName = (TextView) dialog.findViewById(R.id.BranchName);
		cityName = (TextView) dialog.findViewById(R.id.CityName);
		regionName = (TextView) dialog.findViewById(R.id.RegionName);
		address = (TextView) dialog.findViewById(R.id.Address);
		projectName = (TextView) dialog.findViewById(R.id.ProjectName);
		description = (TextView) dialog.findViewById(R.id.Description);
		orderID = (TextView) dialog.findViewById(R.id.OrderID);
		critlink = (TextView) dialog.findViewById(R.id.critlink);
		approved_paid = (TextView) dialog.findViewById(R.id.status);
		status_text = (TextView) dialog.findViewById(R.id.status_top);

		dialog.findViewById(R.id.xbutton).setVisibility(RelativeLayout.GONE);
		addedAt.setText(data.getAddedAt());
		flowType.setText(data.getFlowTypeLink());
		value.setText(data.getValue());
		invoiceNumber.setText(data.getInvoiceNumber());
		approved.setText(data.getApproved());
		paid.setText(data.getPaid());
		clientName.setText(data.getClientName());
		branchCode.setText(data.getBranchCode());
		branchName.setText(data.getBranchName());
		cityName.setText(data.getCityName());
		regionName.setText(data.getRegionName());
		address.setText(data.getAddress());
		projectName.setText(data.getProjectName());
		description.setText(data.getDescription());
		orderID.setText(data.getOrderID());
		critlink.setText(data.getCritLink());
		try {
			java.text.DateFormat inFormat = new SimpleDateFormat(
					"yyyy-mm-dd hh:mm:ss");
			Date inDate = null;

			inDate = inFormat.parse(data.getAddedAt());

			java.text.DateFormat outFormatTime = android.text.format.DateFormat
					.getTimeFormat(context);
			String outTime = outFormatTime.format(inDate);

			java.text.DateFormat outFormatDate = android.text.format.DateFormat
					.getDateFormat(context);
			String outDate = outFormatDate.format(inDate);
			addedAt.setText(outDate + " " + outTime);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		dialog.findViewById(R.id.topbar).setBackgroundColor(
				Color.parseColor("#bcbcbc"));

		Helper.changeTobBoardColor(dialog.findViewById(R.id.topbar));

		if (data.getApproved() != null && data.getPaid() != null
				&& data.getApproved().equals("0") && data.getPaid().equals("0")) {
			dialog.findViewById(R.id.topbar).setBackgroundColor(
					Color.parseColor(context.getResources().getString(
							R.color.refund_red)));
			dialog.findViewById(R.id.bottombar).setBackgroundColor(
					Color.parseColor(context.getResources().getString(
							R.color.refund_red)));
			status_text.setText(getContext().getResources().getString(
					R.string.s_item_column_0_line_174_file_210));
		}
		if (data.getApproved() != null && data.getPaid() != null
				&& data.getApproved().equals("1") && data.getPaid().equals("0")) {
			dialog.findViewById(R.id.topbar).setBackgroundColor(
					Color.parseColor(context.getResources().getString(
							R.color.refund_yellow)));
			dialog.findViewById(R.id.bottombar).setBackgroundColor(
					Color.parseColor(context.getResources().getString(
							R.color.refund_yellow)));
			status_text.setText(getContext().getResources().getString(
					R.string.s_item_column_0_line_184_file_210));
		}
		if (data.getApproved() != null && data.getPaid() != null
				&& data.getPaid().equals("1")) {
			dialog.findViewById(R.id.topbar).setBackgroundColor(
					Color.parseColor(context.getResources().getString(
							R.color.dark_blue)));
			dialog.findViewById(R.id.bottombar).setBackgroundColor(
					Color.parseColor(context.getResources().getString(
							R.color.dark_blue)));
			status_text.setText(getContext().getResources().getString(
					R.string.s_item_column_0_line_194_file_210));
		}
		dialog.findViewById(R.id.bottombar).setBackgroundColor(
				Color.parseColor(context.getResources().getString(
						R.color.dark_blue)));
		approved_paid.setText(getContext().getResources().getString(
				R.string.s_item_column_0_line_199_file_210));
		Helper.changeTxtViewBothColors(approved_paid);

		if (isDetailed2 == 0) {
			critlink.setText(getContext().getResources().getString(
					R.string.s_item_column_0_line_202_file_210)
					+ data.getCritLink());
			dialog.findViewById(R.id.layout_5).setVisibility(LinearLayout.GONE);
			dialog.findViewById(R.id.layout_6).setVisibility(LinearLayout.GONE);
			dialog.findViewById(R.id.layout_7).setVisibility(LinearLayout.GONE);
			dialog.findViewById(R.id.layout_8).setVisibility(LinearLayout.GONE);
			dialog.findViewById(R.id.layout_9).setVisibility(LinearLayout.GONE);
			dialog.findViewById(R.id.layout_10)
					.setVisibility(LinearLayout.GONE);
			dialog.findViewById(R.id.layout_11)
					.setVisibility(LinearLayout.GONE);
			dialog.findViewById(R.id.layout_12)
					.setVisibility(LinearLayout.GONE);
			dialog.findViewById(R.id.layout_13)
					.setVisibility(LinearLayout.GONE);
			dialog.findViewById(R.id.layout_14)
					.setVisibility(LinearLayout.GONE);
			dialog.findViewById(R.id.layout_15)
					.setVisibility(LinearLayout.GONE);
		} else if (isDetailed2 == 1) {// hide all but project name and amount
			approved_paid.setVisibility(RelativeLayout.GONE);
			dialog.findViewById(R.id.layout_1).setVisibility(LinearLayout.GONE);
			dialog.findViewById(R.id.layout_2).setVisibility(LinearLayout.GONE);
			dialog.findViewById(R.id.layout_3).setVisibility(LinearLayout.GONE);
			dialog.findViewById(R.id.layout_5).setVisibility(LinearLayout.GONE);
			dialog.findViewById(R.id.layout_6).setVisibility(LinearLayout.GONE);
			dialog.findViewById(R.id.layout_7).setVisibility(LinearLayout.GONE);
			dialog.findViewById(R.id.layout_8).setVisibility(LinearLayout.GONE);
			dialog.findViewById(R.id.layout_9).setVisibility(LinearLayout.GONE);
			dialog.findViewById(R.id.layout_10)
					.setVisibility(LinearLayout.GONE);
			dialog.findViewById(R.id.layout_11)
					.setVisibility(LinearLayout.GONE);
			dialog.findViewById(R.id.layout_12)
					.setVisibility(LinearLayout.GONE);
			dialog.findViewById(R.id.layout_14)
					.setVisibility(LinearLayout.GONE);
			dialog.findViewById(R.id.layout_15)
					.setVisibility(LinearLayout.GONE);

			critlink.setText(data.getCritLink());
		} else if (isDetailed2 == 2) {// hide all but flow type and amount
			approved_paid.setVisibility(RelativeLayout.GONE);
			dialog.findViewById(R.id.layout_1).setVisibility(LinearLayout.GONE);
			dialog.findViewById(R.id.layout_3).setVisibility(LinearLayout.GONE);
			dialog.findViewById(R.id.layout_5).setVisibility(LinearLayout.GONE);
			dialog.findViewById(R.id.layout_6).setVisibility(LinearLayout.GONE);
			dialog.findViewById(R.id.layout_7).setVisibility(LinearLayout.GONE);
			dialog.findViewById(R.id.layout_8).setVisibility(LinearLayout.GONE);
			dialog.findViewById(R.id.layout_9).setVisibility(LinearLayout.GONE);
			dialog.findViewById(R.id.layout_10)
					.setVisibility(LinearLayout.GONE);
			dialog.findViewById(R.id.layout_11)
					.setVisibility(LinearLayout.GONE);
			dialog.findViewById(R.id.layout_12)
					.setVisibility(LinearLayout.GONE);
			dialog.findViewById(R.id.layout_13)
					.setVisibility(LinearLayout.GONE);
			dialog.findViewById(R.id.layout_14)
					.setVisibility(LinearLayout.GONE);
			dialog.findViewById(R.id.layout_15)
					.setVisibility(LinearLayout.GONE);

			critlink.setText(data.getCritLink());
		}
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = null;
		rowView = inflater.inflate(R.layout.refund_dialog, parent, false);
		if (isDetailed == 0)
			rowView.findViewById(R.id.toplayout).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							RefundData rf = values.get(position);

							openDialog(rf);
						}

					});
		updateRow(values.get(position), rowView, isDetailed);
		return rowView;
	}

	public void openDialog(RefundData data) {
		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.main_refund_dialog);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.height = WindowManager.LayoutParams.MATCH_PARENT;
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		dialog.getWindow().setLayout(lp.width, lp.height);

		TextView status_text, critlink, approved_paid, addedAt, flowType, value, invoiceNumber, approved, paid, clientName, branchCode, branchName, cityName, regionName, address, projectName, description, orderID;
		addedAt = (TextView) dialog.findViewById(R.id.AddedAt);
		flowType = (TextView) dialog.findViewById(R.id.FlowType);
		value = (TextView) dialog.findViewById(R.id.Value);
		invoiceNumber = (TextView) dialog.findViewById(R.id.InvoiceNumber);
		approved = (TextView) dialog.findViewById(R.id.Approved);
		paid = (TextView) dialog.findViewById(R.id.Paid);
		clientName = (TextView) dialog.findViewById(R.id.ClientName);
		branchCode = (TextView) dialog.findViewById(R.id.BranchCode);
		branchName = (TextView) dialog.findViewById(R.id.BranchName);
		cityName = (TextView) dialog.findViewById(R.id.CityName);
		regionName = (TextView) dialog.findViewById(R.id.RegionName);
		address = (TextView) dialog.findViewById(R.id.Address);
		projectName = (TextView) dialog.findViewById(R.id.ProjectName);
		description = (TextView) dialog.findViewById(R.id.Description);
		orderID = (TextView) dialog.findViewById(R.id.OrderID);
		critlink = (TextView) dialog.findViewById(R.id.critlink);
		approved_paid = (TextView) dialog.findViewById(R.id.status);
		status_text = (TextView) dialog.findViewById(R.id.status_top);

		// addedAt.setText(data.getAddedAt());
		try {
			java.text.DateFormat inFormat = new SimpleDateFormat(
					"yyyy-mm-dd hh:mm:ss");
			Date inDate = null;

			inDate = inFormat.parse(data.getAddedAt());

			java.text.DateFormat outFormatTime = android.text.format.DateFormat
					.getTimeFormat(context);
			String outTime = outFormatTime.format(inDate);

			java.text.DateFormat outFormatDate = android.text.format.DateFormat
					.getDateFormat(context);
			String outDate = outFormatDate.format(inDate);
			addedAt.setText(outDate + " " + outTime);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		flowType.setText(data.getFlowTypeLink());
		value.setText(data.getValue());
		invoiceNumber.setText(data.getInvoiceNumber());
		if (data.getApproved() != null && data.getApproved().equals("1"))
			approved.setText(getContext().getResources().getString(
					R.string.s_item_column_0_line_344_file_210));
		else
			approved.setText(getContext().getResources().getString(
					R.string.s_item_column_0_line_346_file_210));

		if (data.getPaid() != null && data.getPaid().equals("1"))
			paid.setText("Yes");
		else
			paid.setText("No");
		clientName.setText(data.getClientName());
		branchCode.setText(data.getBranchCode());
		branchName.setText(data.getBranchName());
		cityName.setText(data.getCityName());
		regionName.setText(data.getRegionName());
		address.setText(data.getAddress());
		projectName.setText(data.getProjectName());
		description.setText(data.getDescription());
		orderID.setText(data.getOrderID());
		critlink.setText(data.getCritLink());

		if (data.getApproved().equals("0") && data.getPaid().equals("0")) {
			dialog.findViewById(R.id.topbar).setBackgroundColor(
					Color.parseColor(context.getResources().getString(
							R.color.refund_red)));
			dialog.findViewById(R.id.bottombar).setBackgroundColor(
					Color.parseColor(context.getResources().getString(
							R.color.refund_red)));

			status_text.setText(" PENDING ");
		}
		if (data.getApproved().equals("1") && data.getPaid().equals("0")) {
			dialog.findViewById(R.id.topbar).setBackgroundColor(
					Color.parseColor(context.getResources().getString(
							R.color.refund_yellow)));
			dialog.findViewById(R.id.bottombar).setBackgroundColor(
					Color.parseColor(context.getResources().getString(
							R.color.refund_yellow)));
			status_text.setText(" APPROVED ");
		}
		if (data.getPaid().equals("1")) {
			dialog.findViewById(R.id.topbar).setBackgroundColor(
					Color.parseColor(context.getResources().getString(
							R.color.refund_green)));
			dialog.findViewById(R.id.bottombar).setBackgroundColor(
					Color.parseColor(context.getResources().getString(
							R.color.refund_green)));
			status_text.setText(" PAID ");
		}
		approved_paid.setVisibility(RelativeLayout.GONE);
		dialog.findViewById(R.id.xbutton).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
		dialog.show();
	}
}
