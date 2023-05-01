package com.checker.sa.android.dialog;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;

import com.checker.sa.android.data.FilterData;
import com.checker.sa.android.data.Order;
import com.checker.sa.android.data.Orders;
import com.checker.sa.android.data.orderListItem;
import com.checker.sa.android.helper.Constants;
import com.checker.sa.android.helper.Helper;
import com.checker.sa.android.helper.UIHelper;
import com.google.maps.android.MapActivity;
import com.mor.sa.android.activities.JobListActivity;
import com.mor.sa.android.activities.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

public class JobFilterDialog extends Dialog implements
        android.view.View.OnClickListener {

    TextView calander_h, sdate_text, edate_text, s_date, e_date;
    Spinner jobTypeSpinner, regionTypeSpinner, bregionspinner, statusSpinner;
    //	ImageView save_btn;
    AppCompatButton save_btn, btn_clear;
    String jobtype, city, region, project, status;
    private String branchcode;
    TextView tv;
    int tag = 0;// , d1, m1, y1, d2, m2, y2;
    Activity joblist;
    String[] monthVal = {"01", "02", "03", "04", "05", "06", "07", "08", "09",
            "10", "11", "12"};

    AppCompatImageView iv_calendar_start, iv_calendar_end;

    CardView card_status, card_jobtypelist, card_regionlist, card_bcodelist, card_bproplist, card_bprojectslist,card_fromList;

    private void setInvertDisplay() {
        if (Helper.getTheme(superContext) == 0) {
            // ImageView ilayout = (ImageView) findViewById(R.id.bottombar);
            // ilayout.setBackgroundDrawable(superContext.getResources().getDrawable(
            // R.drawable.navigation_bar_dark));
            //
            // RelativeLayout layout = (RelativeLayout)
            // findViewById(R.id.topbar);
            // layout.setBackgroundDrawable(superContext.getResources().getDrawable(
            // R.drawable.navigation_bar_dark));

            RelativeLayout layout = (RelativeLayout) findViewById(R.id.backgroundTheme);
            layout.setBackgroundDrawable(superContext.getResources()
                    .getDrawable(R.drawable.background_dark));

            // TextView tv = (TextView) findViewById(R.id.questionnaire_title);
            // tv.setTextColor(superContext.getResources().getColor(android.R.color.white));
        }
    }

    private void setFontSize(View v) {

        try {
            if (v.getClass().equals(EditText.class)) {
                EditText btnView = (EditText) v;

                btnView.setTextSize(UIHelper.getFontSize(superContext,
                        btnView.getTextSize()));
                if (Helper.getTheme(superContext) == 0) {
                    btnView.setTextColor(superContext.getResources().getColor(
                            android.R.color.black));
                }
            }

        } catch (Exception ex) {

        }

        try {
            if (v.getClass().equals(Button.class)) {
                Button btnView = (Button) v;

                btnView.setTextSize(UIHelper.getFontSize(superContext,
                        btnView.getTextSize()));
            }
        } catch (Exception ex) {

        }

        try {
            if (v.getClass().equals(CheckBox.class)) {
                CheckBox btnView = (CheckBox) v;

                btnView.setTextSize(UIHelper.getFontSize(superContext,
                        btnView.getTextSize()));
                if (Helper.getTheme(superContext) == 0) {
                    btnView.setTextColor(superContext.getResources().getColor(
                            android.R.color.white));
                }
            }
        } catch (Exception ex) {

        }

        try {
            if (v.getClass().equals(RadioButton.class)) {
                RadioButton btnView = (RadioButton) v;

                btnView.setTextSize(UIHelper.getFontSize(superContext,
                        btnView.getTextSize()));
                if (Helper.getTheme(superContext) == 0) {
                    btnView.setTextColor(superContext.getResources().getColor(
                            android.R.color.white));
                }
            }
        } catch (Exception ex) {

        }
        try {
            if (v.getClass().equals(TextView.class)) {
                TextView textView = (TextView) v;

                textView.setTextSize(UIHelper.getFontSize(superContext,
                        textView.getTextSize()));
                if (Helper.getTheme(superContext) == 0) {
                    textView.setTextColor(superContext.getResources().getColor(
                            android.R.color.white));
                }
            }
        } catch (Exception ex) {

        }

    }

    private Context superContext;
    private Spinner bcodelistSpinner;
    private Spinner bprojectslistSpinner;
    private Spinner bproplistSpinner;
    protected String branchprop;
    protected String regionname;
    protected String projectname;

    public JobFilterDialog(Context context) {
        super(context);
        superContext = context;
        joblist = (Activity) context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.job_filter_dialog_en);
//        joblist.setTitle("Filter settings");

        setInvertDisplay();
        s_date = (TextView) findViewById(R.id.startdate);
        setFontSize(s_date);
        e_date = (TextView) findViewById(R.id.enddate);
        setFontSize(e_date);
        s_date.setText(joblist.getString(R.string.job_filter_select_date));
        s_date.setPaintFlags(s_date.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        e_date.setText(joblist.getString(R.string.job_filter_select_date));
        e_date.setPaintFlags(e_date.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
//		save_btn = (ImageView) findViewById(R.id.dialog_save);
        save_btn = (AppCompatButton) findViewById(R.id.dialog_save);
        btn_clear = (AppCompatButton) findViewById(R.id.btn_clear);
        iv_calendar_start = (AppCompatImageView) findViewById(R.id.iv_calendar_start);
        iv_calendar_end = (AppCompatImageView) findViewById(R.id.iv_calendar_end);

        statusSpinner = findViewById(R.id.spinner_status);
        fillSpinner(statusSpinner, "status");
        jobTypeSpinner = (Spinner) findViewById(R.id.jobtypelist);
        fillSpinner(jobTypeSpinner, "jobtype");
        regionTypeSpinner = (Spinner) findViewById(R.id.regionlist);
        fillSpinner(regionTypeSpinner, "city");
        bregionspinner = (Spinner) findViewById(R.id.bregionlist);
        fillSpinner(bregionspinner, "region");
        bcodelistSpinner = (Spinner) findViewById(R.id.bcodelist);
        fillSpinner(bcodelistSpinner, "bcode");
        bprojectslistSpinner = (Spinner) findViewById(R.id.bprojectslist);
        fillSpinner(bprojectslistSpinner, "bprojects");
        bproplistSpinner = (Spinner) findViewById(R.id.bproplist);
        fillSpinner(bproplistSpinner, "bprop");
        s_date.setOnClickListener(this);
        e_date.setOnClickListener(this);
        save_btn.setOnClickListener(this);
        btn_clear.setOnClickListener(this);
        iv_calendar_start.setOnClickListener(this);
        iv_calendar_end.setOnClickListener(this);
        setFontSize(findViewById(R.id.Label_enddate));
        setFontSize(findViewById(R.id.Label_sdate));
        setFontSize(findViewById(R.id.Label_jobtypelist));
        setFontSize(findViewById(R.id.Label_regionlist));
        setFontSize(findViewById(R.id.jobtypelist));
        setFontSize(findViewById(R.id.Label_jobfilter_title));
        setFontSize(findViewById(R.id.calandertitle));
        setFontSize(save_btn);

        card_status = findViewById(R.id.card_status);
        card_jobtypelist = findViewById(R.id.card_jobtypelist);
        card_regionlist = findViewById(R.id.card_regionlist);
        card_bcodelist = findViewById(R.id.card_bcodelist);
        card_bproplist = findViewById(R.id.card_bproplist);
        card_bprojectslist = findViewById(R.id.card_bprojectslist);
        card_fromList = findViewById(R.id.card_fromList);

        if (Constants.select_jobs == "MY_JOBS") {
            card_status.setVisibility(View.INVISIBLE);
            card_jobtypelist.setVisibility(View.VISIBLE);
            card_regionlist.setVisibility(View.VISIBLE);
            card_bcodelist.setVisibility(View.VISIBLE);
            card_bproplist.setVisibility(View.VISIBLE);
            card_bprojectslist.setVisibility(View.VISIBLE);
            card_fromList.setVisibility(View.VISIBLE);
        } else {
            card_status.setVisibility(View.VISIBLE);
            card_jobtypelist.setVisibility(View.INVISIBLE);
            card_regionlist.setVisibility(View.INVISIBLE);
            card_bcodelist.setVisibility(View.INVISIBLE);
            card_bproplist.setVisibility(View.INVISIBLE);
            card_bprojectslist.setVisibility(View.INVISIBLE);
            card_fromList.setVisibility(View.INVISIBLE);
        }

        jobTypeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                jobtype = ((TextView) arg1).getText().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
        bregionspinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                region = ((TextView) arg1).getText().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
        bprojectslistSpinner
                .setOnItemSelectedListener(new OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {
                        // TODO Auto-generated method stub
                        project = ((TextView) arg1).getText().toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub
                    }
                });

        regionTypeSpinner
                .setOnItemSelectedListener(new OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {
                        // TODO Auto-generated method stub
                        city = ((TextView) arg1).getText().toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub
                    }
                });
        bcodelistSpinner
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {
                        // TODO Auto-generated method stub
                        branchcode = ((TextView) arg1).getText().toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub
                    }
                });
        bproplistSpinner
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {
                        // TODO Auto-generated method stub
                        branchprop = ((TextView) arg1).getText().toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub
                    }
                });

        statusSpinner
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {
                        // TODO Auto-generated method stub
                        status = ((TextView) arg1).getText().toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub
                    }
                });
    }

    private void fillSpinner(Spinner spinner, String type) {
        String[] array = getArraY(type);
        if (array != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(joblist,
                    android.R.layout.simple_spinner_item, getArraY(type));
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        }
    }

    private String[] getArraY(String str) {
        if (str.equals("bcode"))//Branch
            return getbranchCodeArraY();
        else if (str.equals("city"))//City
            return getCityArraY();
        else if (str.equals("region"))//Regions
            return getRegionArraY();
        else if (str.equals("bprojects"))//Project
            return getProjectsArraY();
        else if (str.equals("bprop"))//Branch Properties
            return getPropList();
        else if (str.equals("status"))
            return getStatus();
        else
            return getJobTypeArraY();//Client
    }

    private String[] getPropList() {
        if (Orders.getBranchProps() == null)
            return null;
        int count = Orders.getBranchProps().size();
        Vector<String> vector = new Vector<String>();
//        vector.add(joblist.getString(R.string.job_filter_default_dd_option));
        vector.add(joblist.getString(R.string.job_filter_default_choose_branch_properties));
        boolean isExits = false;
        for (int ordercount = 0; ordercount < count; ordercount++) {
            isExits = false;
            String value = Orders.getBranchProps().get(ordercount).getContent();
            String client = Orders.getBranchProps().get(ordercount)
                    .getPropertyName()
                    + " - " + value;
            for (int itemcount = 0; itemcount < vector.size(); itemcount++) {
                if (vector.get(itemcount).equals(client)) {
                    isExits = true;
                    break;
                }
            }
            if (!isExits && client != null)
                vector.add(client);
        }
        String[] items = new String[vector.size()];
        vector.copyInto(items);
        vector.removeAllElements();
        vector = null;
        return items;
    }

    private String[] getCityArraY() {
        int count = Orders.getOrders().size();
        Vector<String> vector = new Vector<String>();
//        vector.add(joblist.getString(R.string.job_filter_default_dd_option));
        vector.add(joblist.getString(R.string.job_filter_choose_city_lbl));
        boolean isExits = false;
        for (int ordercount = 0; ordercount < count; ordercount++) {
            isExits = false;
            if (Orders.getOrders().get(ordercount).getOrderID().contains("-"))
                continue;

            String client = Orders.getOrders().get(ordercount).getCityName();
            for (int itemcount = 0; itemcount < vector.size(); itemcount++) {
                if (vector.get(itemcount).equals(client)) {
                    isExits = true;
                    break;
                }
            }
            if (!isExits && client != null)
                vector.add(client);
        }
        String[] items = new String[vector.size()];
        vector.copyInto(items);
        vector.removeAllElements();
        vector = null;
        return items;
    }

    private String[] getRegionArraY() {
        int count = Orders.getOrders().size();
        Vector<String> vector = new Vector<String>();
//        vector.add(joblist.getString(R.string.job_filter_default_dd_option));
        vector.add(joblist.getString(R.string.job_filter_default_choose_regions));
        boolean isExits = false;
        for (int ordercount = 0; ordercount < count; ordercount++) {
            isExits = false;
            if (Orders.getOrders().get(ordercount).getOrderID().contains("-"))
                continue;

            String client = Orders.getOrders().get(ordercount).getRegionName();
            for (int itemcount = 0; itemcount < vector.size(); itemcount++) {
                if (client != null && vector.get(itemcount).equals(client)) {
                    isExits = true;
                    break;
                }
            }
            if (!isExits && client != null)
                vector.add(client);
        }
        String[] items = new String[vector.size()];
        vector.copyInto(items);
        vector.removeAllElements();
        vector = null;
        return items;
    }

    private String[] getProjectsArraY() {
        int count = Orders.getOrders().size();
        Vector<String> vector = new Vector<String>();
//        vector.add(joblist.getString(R.string.job_filter_default_dd_option));
        vector.add(joblist.getString(R.string.job_filter_default_choose_project));
        boolean isExits = false;
        for (int ordercount = 0; ordercount < count; ordercount++) {
            isExits = false;
            if (Orders.getOrders().get(ordercount).getOrderID().contains("-"))
                continue;

            String client = Orders.getOrders().get(ordercount).getProjectName();
            for (int itemcount = 0; itemcount < vector.size(); itemcount++) {
                if (client != null && vector.get(itemcount).equals(client)) {
                    isExits = true;
                    break;
                }
            }
            if (!isExits && client != null)
                vector.add(client);
        }
        String[] items = new String[vector.size()];
        vector.copyInto(items);
        vector.removeAllElements();
        vector = null;
        return items;
    }

    private String[] getbranchCodeArraY() {
        int count = Orders.getOrders().size();
        Vector<String> vector = new Vector<String>();
//        vector.add(joblist.getString(R.string.job_filter_default_dd_option));
        vector.add(joblist.getString(R.string.job_filter_default_choose_branch));
        boolean isExits = false;
        for (int ordercount = 0; ordercount < count; ordercount++) {
            isExits = false;
            if (Orders.getOrders().get(ordercount).getOrderID().contains("-"))
                continue;

            String client = Orders.getOrders().get(ordercount).getBranchCode();
            for (int itemcount = 0; itemcount < vector.size(); itemcount++) {
                if (client != null && vector.get(itemcount).equals(client)) {
                    isExits = true;
                    break;
                }
            }
            if (!isExits)
                vector.add(client);
        }
        String[] items = new String[vector.size()];
        vector.copyInto(items);
        vector.removeAllElements();
        vector = null;
        return items;
    }

    private String[] getJobTypeArraY() {
        int count = Orders.getOrders().size();
        Vector<String> vector = new Vector<String>();
//        vector.add(joblist.getString(R.string.job_filter_default_dd_option));
        vector.add(joblist.getString(R.string.job_filter_default_choose_client));
        boolean isExits = false;
        for (int ordercount = 0; ordercount < count; ordercount++) {
            isExits = false;
            if (Orders.getOrders().get(ordercount).getOrderID().contains("-"))
                continue;

            String client = Orders.getOrders().get(ordercount).getClientName();
            for (int itemcount = 0; itemcount < vector.size(); itemcount++) {
                if (client != null && vector.get(itemcount).equals(client)) {
                    isExits = true;
                    break;
                }
            }
            if (!isExits)
                vector.add(client);
        }
        String[] items = new String[vector.size()];
        vector.copyInto(items);
        vector.removeAllElements();
        vector = null;
        return items;
    }

    private String[] getStatus() {
        int count = Orders.getOrders().size();
        Vector<String> vector = new Vector<String>();
//        vector.add(joblist.getString(R.string.job_filter_default_dd_option));
        vector.add(joblist.getString(R.string.job_filter_choose_status));
        boolean isExits = false;
        for (int ordercount = 0; ordercount < count; ordercount++) {
            isExits = false;
            if (Constants.select_jobs == "CAPI_JOBS") {
                if (Orders.getOrders().get(ordercount).getOrderID().contains("-")) {
                    String c = Orders.getOrders().get(ordercount).getStatusName();
                    for (int itemcount = 0; itemcount < vector.size(); itemcount++) {
                        if (c != null && vector.get(itemcount).equals(c)) {
                            isExits = true;
                            break;
                        }
                    }
                    if (!isExits)
                        vector.add(c);
                }
            }
        }
        String[] items = new String[vector.size()];
        vector.copyInto(items);
        vector.removeAllElements();
        vector = null;
        return items;
    }

    String getDate() {
        Calendar cl = Calendar.getInstance();
        int y = cl.get(Calendar.YEAR);
        int m = cl.get(Calendar.MONTH);
        int d = cl.get(Calendar.DAY_OF_MONTH);
        return getCurrentDate(d, m, y);
    }

    private DatePickerDialog.OnDateSetListener setDate = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            String date = getCurrentDate(dayOfMonth, monthOfYear, year);
            if (tag == 1) {
                s_date.setText(date);
                // d1 = dayOfMonth;
                // m1 = monthOfYear;
                // y1 = year;
            } else {
                e_date.setText(date);
                // d2 = dayOfMonth;
                // m2 = monthOfYear;
                // y2 = year;
            }
        }
    };

    private void openDatePicker() {
        Calendar cl = Calendar.getInstance();
        int y = cl.get(Calendar.YEAR);
        int m = cl.get(Calendar.MONTH);
        int d = cl.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dp = new DatePickerDialog(joblist, setDate, y, m, d);
        dp.show();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.iv_calendar_start:
            case R.id.startdate:
                tag = 1;
                openDatePicker();
                break;
            case R.id.iv_calendar_end:
            case R.id.enddate:
                tag = 2;
                openDatePicker();
                break;
            case R.id.dialog_save:
                if (s_date.getText().toString()
                        .equals(joblist.getString(R.string.job_filter_select_date))
                        || e_date
                        .getText()
                        .toString()
                        .equals(joblist.getString(R.string.job_filter_select_date))) {
                    if (joblist instanceof JobListActivity) {
                        ((JobListActivity) joblist).FilterJobList(new FilterData(region, project,
                                branchprop, branchcode, jobtype, city, "1/1/1900",
                                "1/1/1900", status));
                    } else if (joblist instanceof MapActivity) {
                        ((MapActivity) joblist).FilterJobList(new FilterData(region,
                                project, branchprop, branchcode, jobtype, city,
                                "1/1/1900", "1/1/1900", status));
                    }
                } else {
                    if ((s_date.getText().toString().equals(joblist
                            .getString(R.string.job_filter_select_date)))
                            || (e_date.getText().toString().equals(joblist
                            .getString(R.string.job_filter_select_date)))) {
                        if (joblist instanceof JobListActivity) {
                            ((JobListActivity) joblist).FilterJobList(new FilterData(region,
                                    project, branchprop, branchcode, jobtype, city,
                                    "", "", status));
                        } else if (joblist instanceof MapActivity) {
                            ((MapActivity) joblist).FilterJobList(
                                    new FilterData(region, project, branchprop, branchcode,
                                            jobtype, city, "", "", status));
                        }
                    } else {
                        if (joblist instanceof JobListActivity) {
                            ((JobListActivity) joblist).FilterJobList(new FilterData(region,
                                    project, branchprop, branchcode, jobtype, city,
                                    s_date.getText().toString(), e_date.getText()
                                    .toString(), status));
                        } else if (joblist instanceof MapActivity) {
                            ((MapActivity) joblist).FilterJobList(new FilterData(
                                    region, project, branchprop, branchcode,
                                    jobtype, city, s_date.getText().toString(),
                                    e_date.getText().toString(), status));
                        }

                    }
                }
                this.dismiss();
                break;
            case R.id.btn_clear:
                this.dismiss();
                break;
        }
    }

    private String getCurrentDate(int day, int month, int year) {
        String str = String.valueOf(day) + "/" + monthVal[month] + "/"
                + String.valueOf(year);
        return str;
    }
}
