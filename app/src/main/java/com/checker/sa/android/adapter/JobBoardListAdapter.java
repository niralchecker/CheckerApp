package com.checker.sa.android.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.checker.sa.android.data.Job;
import com.checker.sa.android.helper.Constants;
import com.checker.sa.android.helper.Helper;
import com.google.maps.android.JobBoardActivityFragment;
import com.mor.sa.android.activities.R;

public class JobBoardListAdapter extends ArrayAdapter<Job> {

    private final Context context;
    public final List<Job> values;
    JobBoardlistAdapter_CallBack jobBoardlistAdapter_CallBack;
    private Location personLocation;
    private JobBoardActivityFragment jobboardact;
    private Button btnApplyAll;

    public List<Job> getValues() {
        return values;
    }

    @Override
    public int getCount() {
        if (values != null)
            return values.size();
        return 0;
    }

    public void setUnsetCheckBoxes(boolean checked) {
        if (values != null) {
            for (int i = 0; i < values.size(); i++) {
                if (values.get(i) != null) {

                    if (listOfCheckBoxes == null)
                        listOfCheckBoxes = new ArrayList<Object>();
                    if (checked) {
                        if (values.get(i).getRowView() != null) {
                            CheckBox chkbox = ((CheckBox) values.get(i)
                                    .getRowView().findViewById(R.id.chkBox));
                            if (chkbox.getVisibility() == RelativeLayout.INVISIBLE) {
                            } else {
                                chkbox.setChecked(checked);
                                values.get(i).getRowView()
                                        .findViewById(R.id.btnApply)
                                        .setVisibility(RelativeLayout.GONE);
                                addObject((Object) values.get(i));
                            }
                        }

                    } else {

                        if (values.get(i).getRowView() != null) {
                            ((CheckBox) values.get(i).getRowView()
                                    .findViewById(R.id.chkBox))
                                    .setChecked(checked);
                            values.get(i).getRowView()
                                    .findViewById(R.id.btnApply)
                                    .setVisibility(RelativeLayout.VISIBLE);
                        }
                        if (listOfCheckBoxes.contains((Object) values.get(i)))
                            listOfCheckBoxes.remove((Object) values.get(i));
                    }
                    jobboardact.setUnSetBottomBar(listOfCheckBoxes);
                }
            }
        }
    }

    public JobBoardListAdapter(JobBoardActivityFragment context,
                               List<Job> valuess,
                               JobBoardlistAdapter_CallBack mjobBoardlistAdapter_CallBack,
                               Location thisPersonLocaation, Button btnApplyAll) {
        super(context, R.layout.job_board_row, valuess);
        this.jobboardact = context;
        this.context = context;
        this.jobBoardlistAdapter_CallBack = mjobBoardlistAdapter_CallBack;
        this.values = valuess;
        this.personLocation = thisPersonLocaation;
        this.btnApplyAll = btnApplyAll;
    }

    String tmpHeading = "";
    private int isDetailed;
    private ArrayList<Object> listOfCheckBoxes;

    public void addObject(Object object) {
        if (listOfCheckBoxes == null)
            listOfCheckBoxes = new ArrayList<Object>();
        if (!listOfCheckBoxes.contains(object)) {
            listOfCheckBoxes.add(object);
        }
    }

    public void updateRow(Job data, View dialog, int isDetailed2) {

        TextView critlink, Payment, cityName, clientName, branchName, dates, total, showDistance;
        CardView cardView;
        View green_view;
        RelativeLayout topbar;
//		Button btnApply;
        TextView btnApply;
        float distance = (float) -1.0;
        if (data.getBranchLat() != null && data.getBranchLat().length() > 0
                && data.getBranchLong() != null
                && data.getBranchLong().length() > 0 && personLocation != null) {
            Location loc1 = new Location("");
            loc1.setLatitude(Double.parseDouble(data.getBranchLat()));
            loc1.setLongitude(Double.parseDouble(data.getBranchLong()));
            distance = personLocation.distanceTo(loc1);
        }

        distance = (float) (distance / 1000.0);

        double totalPayment;
        totalPayment = Double.parseDouble(data.getBonusPayment() == null
                || data.getBonusPayment().equals("") ? "0.0" : data
                .getBonusPayment())
                + Double.parseDouble(data.getCriticismPayment() == null
                || data.getCriticismPayment().equals("") ? "0.0" : data
                .getCriticismPayment())
                + Double.parseDouble(data.getTransportationPayment() == null
                || data.getTransportationPayment().equals("") ? "0.0"
                : data.getTransportationPayment());
        int totalPay = (int) totalPayment;
        topbar = (RelativeLayout) dialog.findViewById(R.id.topbar);
        critlink = (TextView) dialog.findViewById(R.id.critlink);
        clientName = (TextView) dialog.findViewById(R.id.ClientName);
        branchName = (TextView) dialog.findViewById(R.id.BranchName);
        cityName = (TextView) dialog.findViewById(R.id.cityName);
        dates = (TextView) dialog.findViewById(R.id.Dates);
        total = (TextView) dialog.findViewById(R.id.Total);
        showDistance = (TextView) dialog.findViewById(R.id.ShowDistance);
//		btnApply = (Button) dialog.findViewById(R.id.btnApply);
        btnApply = (TextView) dialog.findViewById(R.id.btnApply);
        Payment = (TextView) dialog.findViewById(R.id.Payment);
        cardView = (CardView) dialog.findViewById(R.id.cardView);
        green_view = (View) dialog.findViewById(R.id.green_view);

        CheckBox cb = (CheckBox) dialog.findViewById(R.id.chkBox);

        if (btnApply.getText().toString().equals(R.string.s_item_column_0_line_145_file_223)){
            btnApply.setVisibility(View.GONE);
            green_view.setVisibility(View.VISIBLE);
        }else {
            btnApply.setVisibility(View.VISIBLE);
            green_view.setVisibility(View.GONE);
        }

        if (data.getoaID() != null && data.getoaID().length() > 0) {
            topbar.setBackgroundColor(Color.parseColor("#2cbdbf"));
            btnApply.setVisibility(View.GONE);
            green_view.setVisibility(View.VISIBLE);
            btnApply.setBackgroundColor(Color.parseColor("#a7a9ab"));
            btnApply.setText(getContext().getResources().getString(
                    R.string.s_item_column_0_line_145_file_223));
            cb.setVisibility(RelativeLayout.INVISIBLE);
            cb.setEnabled(false);
            cardView.setBackgroundColor(Color.parseColor("#dfe5eb"));

            // btnApply.setEnabled(false);
        } else {

            topbar.setBackgroundColor(Color.parseColor("#f18931"));
        }
        topbar.setBackgroundColor(Color.parseColor(data.getColor()));
//		Helper.changeBtnColor(btnApply);
        Helper.changeTxtViewColor(btnApply);
        // Helper.changeTobBoardColor(topbar);
        if (cb.isChecked())
            btnApply.setVisibility(RelativeLayout.GONE);
        if (Constants.getLoginURL() != null && Constants.getLoginURL().toLowerCase().contains("ajis")) {
//			critlink.setText(data.getBranchName());
            clientName.setText(data.getBranchName());
        } else {
//			critlink.setText(data.getSetName());
            clientName.setText(data.getSetName());
        }
        Payment.setText(totalPay + "");
        cityName.setText(data.getCityName());
//		clientName.setText(data.getClientName());
        critlink.setText(data.getClientName());
//        branchName.setText(data.getBranchName() + "," + data.getBranchCode()
//                + "," + data.getAddress());
        branchName = getTextFromHtmlFormate(data.getDescription(), branchName);
        dates.setText(data.getDate());
        total.setText(data.getOrderCount());
        // showDistance.setText(data.getBranchLat() + "," +
        // data.getBranchLong());
        if (distance >= 0) {
            distance = Math.round(distance);
            int dis = (int) distance;
            showDistance.setText(dis + " Km");
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = null;
        rowView = inflater.inflate(R.layout.job_board_row, parent, false);
        CheckBox cb = (CheckBox) rowView.findViewById(R.id.chkBox);
        values.get(position).setRowView(rowView);
        if (listOfCheckBoxes != null
                && listOfCheckBoxes.contains(values.get(position)))
            cb.setChecked(true);
        cb.setTag(values.get(position));
        cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                AddRemoveButton(isChecked, buttonView.getTag());
            }
        });
        rowView.findViewById(R.id.toplayout).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
        rowView.findViewById(R.id.btnApply).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        int id = position;
                        jobBoardlistAdapter_CallBack.PassIndex(id,
                                values.get(position).getM());
                    }

                });

        updateRow(values.get(position), rowView, isDetailed);

        View space = (View) rowView.findViewById(R.id.space);


        space.setVisibility(View.GONE);

        if (position == getCount() - 1) {
            space.setVisibility(View.VISIBLE);
        }

        return rowView;
    }

    protected void AddRemoveButton(boolean isChecked, Object buttonView) {

        View row = ((Job) buttonView).getRowView();
        if (listOfCheckBoxes == null)
            listOfCheckBoxes = new ArrayList<Object>();
        if (isChecked) {
            row.findViewById(R.id.btnApply).setVisibility(RelativeLayout.GONE);
            addObject(buttonView);
        } else {
            row.findViewById(R.id.btnApply).setVisibility(
                    RelativeLayout.VISIBLE);
            listOfCheckBoxes.remove(buttonView);
        }
        if (btnApplyAll != null && btnApplyAll.getText().toString() != null
                && btnApplyAll.getText().toString().contains("(")) {
            String text = btnApplyAll.getText().toString();
            text = text.substring(0, text.indexOf("("));

            btnApplyAll.setText(text);
        }
        btnApplyAll.setText(btnApplyAll.getText().toString() + "("
                + listOfCheckBoxes.size() + ")");

        jobboardact.setUnSetBottomBar(listOfCheckBoxes);
    }

    private TextView getTextFromHtmlFormate(String html, TextView tv) {
        if (html == null)
            html = "";
        Spanned sp = Html.fromHtml(html);
        if (tv != null && sp != null)
            tv.setText(sp, TextView.BufferType.SPANNABLE);
        return tv;
    }
}
