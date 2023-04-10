package com.checker.sa.android.adapter;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;

import com.checker.sa.android.data.BasicLog;
import com.checker.sa.android.data.Cert;
import com.checker.sa.android.data.CustomFields;
import com.checker.sa.android.data.Objects;
import com.checker.sa.android.data.Order;
import com.checker.sa.android.data.Orders;
import com.checker.sa.android.data.QuestionnaireData;
import com.checker.sa.android.data.Set;
import com.checker.sa.android.data.SubmitQuestionnaireData;
import com.checker.sa.android.data.Survey;
import com.checker.sa.android.data.Surveys;
import com.checker.sa.android.data.filePathDataID;
import com.checker.sa.android.data.orderListItem;
import com.checker.sa.android.data.parser.Parser;
import com.checker.sa.android.db.DBHelper;
import com.checker.sa.android.helper.BranchTVListener;
import com.checker.sa.android.helper.Constants;
import com.checker.sa.android.helper.DateTVListener;
import com.checker.sa.android.helper.Helper;
import com.checker.sa.android.helper.UIHelper;
import com.checker.sa.android.helper.jobBoardCertsListener;
import com.mor.sa.android.activities.JobDetailActivity;
import com.mor.sa.android.activities.JobListActivity;
import com.mor.sa.android.activities.QuestionnaireActivity;
import com.mor.sa.android.activities.R;
import com.mor.sa.android.activities.SplashScreen;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class JobItemAdapter extends BaseAdapter {


    public ArrayList<SubmitQuestionnaireData> getInProgressJobs() {
        ArrayList<SubmitQuestionnaireData> inProgressJobs = new ArrayList<SubmitQuestionnaireData>();
        for (int i = 0; i < full_joblistarray.size(); i++) {
            ArrayList<Order> listOrders = full_joblistarray.get(i).listOrders;
            for (int j = 0; j < listOrders.size(); j++) {
                Order order = listOrders.get(j);
                if (order != null
                        && !order.getOrderID().contains("-")
                        && (order.getStatusName().toLowerCase()
                        .equals(filterThree.toLowerCase()))) {
                    SubmitQuestionnaireData item = new SubmitQuestionnaireData();
                    item.setOrderid(order.getOrderID());
                    inProgressJobs.add(item);
                }
            }
        }
        return inProgressJobs;
    }

    Context ct;
    private final int JOB_DETAIL_ACTIVITY_CODE = 2;
    Activity act;
    public ArrayList<orderListItem> joblistarray;
    ArrayList<orderListItem> full_joblistarray;
    Parser thisParser;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private String tabFilter;
    private ImageView tabSync;
    private ImageView tabOne;
    private ImageView tabTwo;
    private ImageView tabThree;
    private ImageView tabFour;
    private TextView txtSync;
    private TextView txtOne;
    private TextView txtTwo;
    private TextView txtThree;
    private TextView txtFour;
    private View VtabOne;
    private View VtabTwo;
    private View VtabThree;
    private View VtabFour;
    private String tabFilterTwo;

    private BranchTVListener branchCallback;
    private DateTVListener dateCallback;

    boolean isFromWatch;

    TextView tvAccept, tvStatusShow, tvRejectShow, tvTime, tvDec;

//    boolean select = false;


    public void setDateCallback(DateTVListener dateCallback) {
        this.dateCallback = dateCallback;
    }

    public void setBranchCallback(BranchTVListener branchCallback) {
        this.branchCallback = branchCallback;
    }

    public ArrayList<orderListItem> getJobList() {
        return joblistarray;
    }

    public JobItemAdapter(Context ct, ArrayList<orderListItem> listarray,
                          Parser parser, String tabFilter, ImageView imgtabSync,
                          ImageView imgtabOne, ImageView imgtabTwo, ImageView imgtabThree,
                          ImageView imgtabFour, TextView txttabSync, TextView txttabOne,
                          TextView txttabTwo, TextView txttabThree, TextView txttabFour,
                          View ltabOne, View ltabTwo, View ltabThree, View ltabFour) {

        this.VtabOne = ltabOne;
        this.VtabTwo = ltabTwo;
        this.VtabThree = ltabThree;
        this.VtabFour = ltabFour;
        this.tabSync = imgtabSync;
        this.tabOne = imgtabOne;
        this.tabTwo = imgtabTwo;
        this.tabThree = imgtabThree;
        this.tabFour = imgtabFour;

        this.txtSync = txttabSync;
        this.txtOne = txttabOne;
        this.txtTwo = txttabTwo;
        this.txtThree = txttabThree;
        this.txtFour = txttabFour;

        this.thisParser = parser;
        this.tabFilter = tabFilter;
        if (tabFilter.toLowerCase().equals("assigned"))
            this.tabFilterTwo = "survey";
        else
            this.tabFilterTwo = tabFilter;
        this.ct = ct;
        act = ((JobListActivity) ct);
        this.joblistarray = this.full_joblistarray = listarray;

    }

    public void mainSetter(Activity ct, ArrayList<orderListItem> listarray,
                           String tabFilte, ImageView imgtabSync, ImageView imgtabOne,
                           ImageView imgtabTwo, ImageView imgtabThree, ImageView imgtabFour,
                           TextView txttabSync, TextView txttabOne, TextView txttabTwo,
                           TextView txttabThree, TextView txttabFour, View ltabOne,
                           View ltabTwo, View ltabThree, View ltabFour) {

        this.VtabOne = ltabOne;
        this.VtabTwo = ltabTwo;
        this.VtabThree = ltabThree;
        this.VtabFour = ltabFour;

        this.tabSync = imgtabSync;
        this.tabOne = imgtabOne;
        this.tabTwo = imgtabTwo;
        this.tabThree = imgtabThree;
        this.tabFour = imgtabFour;

        this.txtSync = txttabSync;
        this.txtOne = txttabOne;
        this.txtTwo = txttabTwo;
        this.txtThree = txttabThree;
        this.txtFour = txttabFour;

        this.act = ct;
        this.ct = act;
        if (tabFilte.toLowerCase().equals("assigned"))
            this.tabFilterTwo = "survey";
        else
            this.tabFilterTwo = tabFilte;
        this.tabFilter = tabFilte;
        this.joblistarray = this.full_joblistarray = listarray;

    }

    public JobItemAdapter(Activity ct, ArrayList<orderListItem> listarray,
                          String tabFilte, ImageView imgtabSync, ImageView imgtabOne,
                          ImageView imgtabTwo, ImageView imgtabThree, ImageView imgtabFour,
                          TextView txttabSync, TextView txttabOne, TextView txttabTwo,
                          TextView txttabThree, TextView txttabFour, View ltabOne,
                          View ltabTwo, View ltabThree, View ltabFour, onJobItemClickListener mListener, onJobStartClickLister mJobStartListener) {

        this.VtabOne = ltabOne;
        this.VtabTwo = ltabTwo;
        this.VtabThree = ltabThree;
        this.VtabFour = ltabFour;

        this.tabSync = imgtabSync;
        this.tabOne = imgtabOne;
        this.tabTwo = imgtabTwo;
        this.tabThree = imgtabThree;
        this.tabFour = imgtabFour;

        this.txtSync = txttabSync;
        this.txtOne = txttabOne;
        this.txtTwo = txttabTwo;
        this.txtThree = txttabThree;
        this.txtFour = txttabFour;

        this.act = ct;
        this.ct = act;
        if (tabFilte.toLowerCase().equals("assigned"))
            this.tabFilterTwo = "survey";
        else
            this.tabFilterTwo = tabFilte;
        this.tabFilter = tabFilte;
        this.joblistarray = this.full_joblistarray = listarray;
        this.mListener = mListener;
        this.mJobStartListener = mJobStartListener;

    }

    String filterOne = "assigned";
    String filterTwo = "scheduled";
    String filterThree = "in progress";
    String filterFour = "completed";

    String sfilterOne = "survey";
    String sfilterTwo = "scheduled";
    String sfilterThree = "in progress";
    String sfilterFour = "completed";

    public int filterOneCount = 0;
    public int filterTwoCount = 0;
    public int filterThreeCount = 0;
    public int filterFourCount = 0;
    int c = 0;
    private Context con;
    public ProgressDialog pd;
    private boolean isLongTaskCalled = true;
    protected View lastpopuplayout;

    SharedPreferences myPrefs;

    Order order;
    Survey survey;
    boolean isSurvey;
    private boolean isBriefing;
    private final int JOB_GPS_CODE = 678;
    private Set set;
    private ArrayList<Cert> pendingCerts = null;
    //    String OrderID;
    private boolean refreshJoblist;

    private int serverGroupedNumber;

    private static jobBoardCertsListener certCallBack;

    ImageView ivCalendar;
    LinearLayout clBottom;
    View viewDate;


    private onJobItemClickListener mListener;

    public interface onJobItemClickListener {
        void onItemClick(int position);
    }

    private onJobStartClickLister mJobStartListener;

    public interface onJobStartClickLister {
        void onJobStartClick(int position, String status);

    }

//    public void setOnItemClickListener(onItemClickListener listener) {
//        this.mListener = listener;
//    }

    public void callCount() {
        filterOne = "assigned";
        filterTwo = "scheduled";
        filterThree = "in progress";
        filterFour = "completed";

        sfilterOne = "survey";
        sfilterTwo = "scheduled";
        sfilterThree = "in progress";
        sfilterFour = "completed";

        filterOneCount = 0;
        filterTwoCount = 0;
        filterThreeCount = 0;
        filterFourCount = 0;

        joblistarray = new ArrayList<orderListItem>();
        int count = joblistarray.size();
        c = 0;
        for (int i = 0; i < full_joblistarray.size(); i++) {
            Order order = full_joblistarray.get(i).orderItem;
            Locale current = con.getResources().getConfiguration().locale;
            if (current != null && current.getLanguage() != null && current.getLanguage().contains("tr") && order.getStatusName() != null && order.getStatusName().contains("rogress"))
                order.setStatusName(filterThree);

            if (order.getOrderID().contains("-")) {
                Survey s = Surveys.getCurrentSurve(order.getOrderID().replace(
                        "-", ""));
                if (s == null || s.getSurveyName() == null) {
                    full_joblistarray.remove(i);
                    i--;
                    continue;
                }

            }

            String status = order.getStatusName().toLowerCase();
            if (order != null
                    && (status
                    .equals(tabFilter.toLowerCase()) || status
                    .equals(tabFilterTwo.toLowerCase()))) {
                c++;
                joblistarray.add(full_joblistarray.get(i));
            }

            if (order != null
                    && (status
                    .equals(filterOne.toLowerCase()) || (status.equals(sfilterOne
                    .toLowerCase())))) {
                if (order != null
                        && full_joblistarray.get(i).listOrders != null
                        && full_joblistarray.get(i).listOrders.size() > 1) {
                    filterOneCount += full_joblistarray.get(i).listOrders
                            .size();
                } else if (order != null
                        && full_joblistarray.get(i).listSurveys != null
                        && full_joblistarray.get(i).listSurveys.size() > 1) {
                    filterOneCount += full_joblistarray.get(i).listSurveys
                            .size();
                } else
                    filterOneCount++;
            }
            if (order != null
                    && (status.equals(filterTwo
                    .toLowerCase()))) {
                if (order != null
                        && full_joblistarray.get(i).listOrders != null
                        && full_joblistarray.get(i).listOrders.size() > 1) {
                    filterTwoCount += full_joblistarray.get(i).listOrders
                            .size();
                } else if (order != null
                        && full_joblistarray.get(i).listSurveys != null
                        && full_joblistarray.get(i).listSurveys.size() > 1) {
                    filterTwoCount += full_joblistarray.get(i).listSurveys
                            .size();
                } else
                    filterTwoCount++;
            }

            if (order != null
                    && (status.equals(filterThree
                    .toLowerCase()))) {

                if (order != null
                        && full_joblistarray.get(i).listOrders != null
                        && full_joblistarray.get(i).listOrders.size() > 1) {
                    filterThreeCount += full_joblistarray.get(i).listOrders
                            .size();
                } else if (order != null
                        && full_joblistarray.get(i).listSurveys != null
                        && full_joblistarray.get(i).listSurveys.size() > 1) {
                    filterThreeCount += full_joblistarray.get(i).listSurveys
                            .size();
                } else
                    filterThreeCount++;
            }
            if (order != null
                    && (status.equals(filterFour
                    .toLowerCase()))) {
                if (order != null
                        && full_joblistarray.get(i).listOrders != null
                        && full_joblistarray.get(i).listOrders.size() > 1) {
                    filterFourCount += full_joblistarray.get(i).listOrders
                            .size();
                } else if (order != null
                        && full_joblistarray.get(i).listSurveys != null
                        && full_joblistarray.get(i).listSurveys.size() > 1) {
                    filterFourCount += full_joblistarray.get(i).listSurveys
                            .size();
                } else
                    filterFourCount++;

            }
        }

        if (this.txtOne != null && filterOneCount > 0) {
            this.txtOne.setVisibility(RelativeLayout.VISIBLE);
            this.tabOne.setVisibility(RelativeLayout.VISIBLE);
            this.txtOne.setText(filterOneCount + "");
        } else {
            this.txtOne.setVisibility(RelativeLayout.INVISIBLE);
            this.tabOne.setVisibility(RelativeLayout.INVISIBLE);
        }
        if (this.txtTwo != null && filterTwoCount > 0) {
            this.txtTwo.setVisibility(RelativeLayout.VISIBLE);
            this.tabTwo.setVisibility(RelativeLayout.VISIBLE);
            this.txtTwo.setText(filterTwoCount + "");
        } else {
            this.txtTwo.setVisibility(RelativeLayout.INVISIBLE);
            this.tabTwo.setVisibility(RelativeLayout.INVISIBLE);
        }
        if (this.txtThree != null && filterThreeCount > 0) {
            this.txtThree.setVisibility(RelativeLayout.VISIBLE);
            this.tabThree.setVisibility(RelativeLayout.VISIBLE);
            this.txtThree.setText(filterThreeCount + "");
        } else {
            this.txtThree.setVisibility(RelativeLayout.INVISIBLE);
            this.tabThree.setVisibility(RelativeLayout.INVISIBLE);
        }
        if (this.txtFour != null && filterFourCount > 0) {
            this.txtFour.setVisibility(RelativeLayout.VISIBLE);
            this.tabFour.setVisibility(RelativeLayout.VISIBLE);
            this.txtFour.setText(filterFourCount + "");
        } else {
            this.txtFour.setVisibility(RelativeLayout.INVISIBLE);
            this.tabFour.setVisibility(RelativeLayout.INVISIBLE);
        }

        this.txtSync.setText("");
        this.tabSync.setVisibility(RelativeLayout.INVISIBLE);

        ((JobListActivity) (con))
                .sendBalloonData(filterOneCount, filterTwoCount,
                        filterThreeCount, filterFourCount, joblistarray);

    }

    private class LongOperation extends AsyncTask<String, Void, String> {

        private boolean isStart;

        public LongOperation(boolean isStart) {
            this.isStart = isStart;
        }

        public void executeIt() {
            try {
                // ArrayList<Order> jobordersss = DBHelper.getOrders(
                // Constants.DB_TABLE_JOBLIST, new String[] {
                // Constants.DB_TABLE_JOBLIST_ORDERID,
                // Constants.DB_TABLE_JOBLIST_DATE,
                // Constants.DB_TABLE_JOBLIST_SN,
                // Constants.DB_TABLE_JOBLIST_DESC,
                // Constants.DB_TABLE_JOBLIST_SETNAME,
                // Constants.DB_TABLE_JOBLIST_SETLINK,
                // Constants.DB_TABLE_JOBLIST_CN,
                // Constants.DB_TABLE_JOBLIST_BFN,
                // Constants.DB_TABLE_JOBLIST_BN,
                // Constants.DB_TABLE_JOBLIST_CITYNAME,
                // Constants.DB_TABLE_JOBLIST_ADDRESS,
                // Constants.DB_TABLE_JOBLIST_BP,
                // Constants.DB_TABLE_JOBLIST_OH,
                // Constants.DB_TABLE_JOBLIST_TS,
                // Constants.DB_TABLE_JOBLIST_TE,
                // Constants.DB_TABLE_JOBLIST_SETID,
                // Constants.DB_TABLE_JOBLIST_BL,
                // Constants.DB_TABLE_JOBLIST_BLNG,
                // Constants.DB_TABLE_JOBLIST_FN,
                // Constants.DB_TABLE_JOBLIST_JC,
                // Constants.DB_TABLE_JOBLIST_JI,
                // Constants.DB_TABLE_JOBLIST_BLINK,
                // Constants.DB_TABLE_JOBLIST_MID,
                // Constants.DB_TABLE_CHECKER_CODE,
                // Constants.DB_TABLE_CHECKER_LINK,
                // Constants.DB_TABLE_BRANCH_CODE,
                // Constants.DB_TABLE_SETCODE,
                // Constants.DB_TABLE_PURCHASE_DESCRIPTION,
                // Constants.DB_TABLE_PURCHASE,
                // Constants.DB_TABLE_JOBLIST_BRIEFING, },
                // Constants.DB_TABLE_JOBLIST_JI);
            } catch (Exception ex) {
            }
            isLongTaskCalled = true;
            filterOne = "assigned";
            filterTwo = "scheduled";
            filterThree = "in progress";
            filterFour = "completed";

            sfilterOne = "survey";
            sfilterTwo = "scheduled";
            sfilterThree = "in progress";
            sfilterFour = "completed";

            filterOneCount = 0;
            filterTwoCount = 0;
            filterThreeCount = 0;
            filterFourCount = 0;

            joblistarray = new ArrayList<orderListItem>();
            int count = joblistarray.size();
            c = 0;
            for (int i = 0; i < full_joblistarray.size(); i++) {
                Order order = full_joblistarray.get(i).orderItem;

                if (order.getOrderID().contains("-")) {
                    Survey s = Surveys.getCurrentSurve(order.getOrderID()
                            .replace("-", ""));
                    if (s == null || s.getSurveyName() == null) {
                        full_joblistarray.remove(i);
                        i--;
                        continue;
                    }

                }

                if (order != null
                        && (order.getStatusName().toLowerCase()
                        .equals(tabFilter.toLowerCase()) || order
                        .getStatusName().toLowerCase()
                        .equals(tabFilterTwo.toLowerCase()))) {
                    c++;
                    joblistarray.add(full_joblistarray.get(i));
                }

                if (order != null
                        && (order.getStatusName().toLowerCase()
                        .equals(filterOne.toLowerCase()) || (order
                        .getStatusName().toLowerCase()
                        .equals(sfilterOne.toLowerCase())))) {
                    if (order != null
                            && full_joblistarray.get(i).listOrders != null
                            && full_joblistarray.get(i).listOrders.size() > 1) {
                        filterOneCount += full_joblistarray.get(i).listOrders
                                .size();
                    } else if (order != null
                            && full_joblistarray.get(i).listSurveys != null
                            && full_joblistarray.get(i).listSurveys.size() > 1) {
                        filterOneCount += full_joblistarray.get(i).listSurveys
                                .size();
                    } else
                        filterOneCount++;
                }
                if (order != null
                        && (order.getStatusName().toLowerCase()
                        .equals(filterTwo.toLowerCase()))) {
                    if (order != null
                            && full_joblistarray.get(i).listOrders != null
                            && full_joblistarray.get(i).listOrders.size() > 1) {
                        filterTwoCount += full_joblistarray.get(i).listOrders
                                .size();
                    } else if (order != null
                            && full_joblistarray.get(i).listSurveys != null
                            && full_joblistarray.get(i).listSurveys.size() > 1) {
                        filterTwoCount += full_joblistarray.get(i).listSurveys
                                .size();
                    } else
                        filterTwoCount++;
                }
                if (order != null
                        && (order.getStatusName().toLowerCase()
                        .equals(filterThree.toLowerCase()))) {

                    if (order != null
                            && full_joblistarray.get(i).listOrders != null
                            && full_joblistarray.get(i).listOrders.size() > 1) {
                        filterThreeCount += full_joblistarray.get(i).listOrders
                                .size();
                    } else if (order != null
                            && full_joblistarray.get(i).listSurveys != null
                            && full_joblistarray.get(i).listSurveys.size() > 1) {
                        filterThreeCount += full_joblistarray.get(i).listSurveys
                                .size();
                    } else
                        filterThreeCount++;
                }
                if (order != null
                        && (order.getStatusName().toLowerCase()
                        .equals(filterFour.toLowerCase()))) {
                    if (order != null
                            && full_joblistarray.get(i).listOrders != null
                            && full_joblistarray.get(i).listOrders.size() > 1) {
                        filterFourCount += full_joblistarray.get(i).listOrders
                                .size();
                    } else if (order != null
                            && full_joblistarray.get(i).listSurveys != null
                            && full_joblistarray.get(i).listSurveys.size() > 1) {
                        filterFourCount += full_joblistarray.get(i).listSurveys
                                .size();
                    } else
                        filterFourCount++;

                }
            }

        }

        @Override
        protected String doInBackground(String... params) {
            executeIt();
            return "Executed";
        }

        public void postExecute() {
            try {
                if (isStart)
                    // notifyDataSetChanged();
                    // else
                    callCount();

                if (pd.isShowing())
                    pd.dismiss();

            } catch (Exception ex) {
            }

        }

        @Override
        protected void onPostExecute(String result) {
            postExecute();
        }

        @Override
        protected void onPreExecute() {
            try {
                if (pd == null)
                    pd = new ProgressDialog(con);
                // pd.setTitle(con.getResources().getString(R.string._alert_title));
                pd.setMessage(con.getResources().getString(
                        R.string.alert_switching));
                pd.setCancelable(false);
                pd.setIndeterminate(true);

                if (!pd.isShowing())
                    pd.show();
            } catch (Exception ex) {
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        int i = 0;
        i++;

    }

    public void doFilter(String filter, Context jobListActivity, boolean isStart) {
        this.con = jobListActivity;
        if (filter.toLowerCase().equals("assigned"))
            this.tabFilterTwo = "survey";
        else
            this.tabFilterTwo = filter;

        tabFilter = filter;
        // if (!isLongTaskCalled)
        {
            LongOperation task = new LongOperation(isStart);
            task.executeIt();
            task.postExecute();
        }
    }

    @Override
    public int getCount() {
        // if (!isLongTaskCalled)
        {

            filterOne = "assigned";
            filterTwo = "scheduled";
            filterThree = "in progress";
            filterFour = "completed";

            sfilterOne = "survey";
            sfilterTwo = "scheduled";
            sfilterThree = "in progress";
            sfilterFour = "completed";

            filterOneCount = 0;
            filterTwoCount = 0;
            filterThreeCount = 0;
            filterFourCount = 0;

            joblistarray = new ArrayList<orderListItem>();
            int count = joblistarray.size();
            c = 0;
            for (int i = 0; i < full_joblistarray.size(); i++) {
                Order order = full_joblistarray.get(i).orderItem;
                if (order != null
                        && (order.getStatusName().toLowerCase()
                        .equals(tabFilter.toLowerCase()) || order
                        .getStatusName().toLowerCase()
                        .equals(tabFilterTwo.toLowerCase()))) {
                    c++;
                    joblistarray.add(full_joblistarray.get(i));
                }

                if (order != null
                        && (order.getStatusName().toLowerCase()
                        .equals(filterOne.toLowerCase()) || (order
                        .getStatusName().toLowerCase()
                        .equals(sfilterOne.toLowerCase())))) {
                    if (order != null
                            && full_joblistarray.get(i).listOrders != null
                            && full_joblistarray.get(i).listOrders.size() > 1) {
                        filterOneCount += full_joblistarray.get(i).listOrders
                                .size();
                    } else if (order != null
                            && full_joblistarray.get(i).listSurveys != null
                            && full_joblistarray.get(i).listSurveys.size() > 1) {
                        filterOneCount += full_joblistarray.get(i).listSurveys
                                .size();
                    } else
                        filterOneCount++;
                }
                if (order != null
                        && (order.getStatusName().toLowerCase()
                        .equals(filterTwo.toLowerCase()))) {
                    if (order != null
                            && full_joblistarray.get(i).listOrders != null
                            && full_joblistarray.get(i).listOrders.size() > 1) {
                        filterTwoCount += full_joblistarray.get(i).listOrders
                                .size();
                    } else if (order != null
                            && full_joblistarray.get(i).listSurveys != null
                            && full_joblistarray.get(i).listSurveys.size() > 1) {
                        filterTwoCount += full_joblistarray.get(i).listSurveys
                                .size();
                    } else
                        filterTwoCount++;
                }
                if (order != null
                        && (order.getStatusName().toLowerCase()
                        .equals(filterThree.toLowerCase()))) {

                    if (order != null
                            && full_joblistarray.get(i).listOrders != null
                            && full_joblistarray.get(i).listOrders.size() > 1) {
                        filterThreeCount += full_joblistarray.get(i).listOrders
                                .size();
                    } else if (order != null
                            && full_joblistarray.get(i).listSurveys != null
                            && full_joblistarray.get(i).listSurveys.size() > 1) {
                        filterThreeCount += full_joblistarray.get(i).listSurveys
                                .size();
                    } else
                        filterThreeCount++;
                }
                if (order != null
                        && (order.getStatusName().toLowerCase()
                        .equals(filterFour.toLowerCase()))) {
                    if (order != null
                            && full_joblistarray.get(i).listOrders != null
                            && full_joblistarray.get(i).listOrders.size() > 1) {
                        filterFourCount += full_joblistarray.get(i).listOrders
                                .size();
                    } else if (order != null
                            && full_joblistarray.get(i).listSurveys != null
                            && full_joblistarray.get(i).listSurveys.size() > 1) {
                        filterFourCount += full_joblistarray.get(i).listSurveys
                                .size();
                    } else
                        filterFourCount++;

                }
            }
            Constants.applied_count = filterOneCount + filterTwoCount;
            Log.e("sum", String.valueOf(filterOneCount + filterTwoCount));
        }
        isLongTaskCalled = true;
        if (this.txtOne != null && filterOneCount > 0) {
            this.txtOne.setVisibility(RelativeLayout.VISIBLE);
            this.txtOne.setText(filterOneCount + "");
        } else
            this.txtOne.setVisibility(RelativeLayout.INVISIBLE);
        if (this.txtTwo != null && filterTwoCount > 0) {
            this.txtTwo.setVisibility(RelativeLayout.VISIBLE);
            this.txtTwo.setText(filterTwoCount + "");
        } else
            this.txtTwo.setVisibility(RelativeLayout.INVISIBLE);
        if (this.txtThree != null && filterThreeCount > 0) {
            this.txtThree.setVisibility(RelativeLayout.VISIBLE);
            this.txtThree.setText(filterThreeCount + "");
        } else
            this.txtThree.setVisibility(RelativeLayout.INVISIBLE);
        if (this.txtFour != null && filterFourCount > 0) {
            this.txtFour.setVisibility(RelativeLayout.VISIBLE);
            this.txtFour.setText(filterFourCount + "");
        } else
            this.txtFour.setVisibility(RelativeLayout.INVISIBLE);

        this.txtSync.setText("");
        this.tabSync.setVisibility(RelativeLayout.INVISIBLE);

        return c;
    }

    @Override
    public String getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public int getResource(String type) {
        if (type.equals("Assigned"))
            return R.drawable.assigned;
        else if (type.equals("Scheduled"))
            return R.drawable.assigned;
        else if (type.equals("wrong"))
            return R.drawable.cross_red;
        else if (type.equals("In progress"))// ct.getString(R.string.jd_begin_button_status_inprogress)))
            return R.drawable.assigned;
        else if (type.equals("survey"))// ct.getString(R.string.jd_begin_button_status_inprogress)))
            return R.drawable.assigned;
        else
            return R.drawable.assigned;
    }

    public Boolean hideShowRight(String type, ImageView imgRight) {
        if (type.equals("Assigned") && !Constants.getDateFilter() && imgRight != null)
            imgRight.setVisibility(RelativeLayout.GONE);
        else if (type.equals("Scheduled") && !Constants.getDateFilter() && imgRight != null)
            imgRight.setVisibility(RelativeLayout.GONE);
        else if (type.equals("wrong") && imgRight != null)
            imgRight.setVisibility(RelativeLayout.GONE);
        else if (type.equals("survey") && imgRight != null)// ct.getString(R.string.jd_begin_button_status_inprogress)))
            imgRight.setVisibility(RelativeLayout.GONE);
        else {
            if (imgRight != null)
                imgRight.setVisibility(RelativeLayout.VISIBLE);
            return true;
        }
        return false;
    }

    public Boolean hideSurvey(String type, ImageView imgRight) {
        if (type.equals("Assigned"))
            return true;
        else if (type.equals("Scheduled"))
            return true;
        else if (type.equals("wrong"))
            return true;
        else if (type.equals("survey"))// ct.getString(R.string.jd_begin_button_status_inprogress)))
            return true;
        else {

            return false;
        }
    }

    String getDate(String date) {
        Date d = null;
        try {
            d = sdf.parse(date);
            DateFormat dateFormat = android.text.format.DateFormat
                    .getDateFormat(ct);
            String str = dateFormat.format(d);
            return str;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return "";
    }

    public Boolean checkIfCorrect(String setId) {
        return true;
    }

    public static void customAlert(Context context, String textString) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_red_alert);

        // set the custom dialog components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.textView1);
        text.setText(textString);

        Button dialogButton = (Button) dialog.findViewById(R.id.btnOk);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
    }


    public static Set getSetsRecords(String setid, String orderID) {
        Set set = null;
        try {
            // set = (Set) DBHelper.convertFromBytes(setid);
            set = (Set) DBHelper.convertFromBytesWithOrder(setid, orderID);
        } catch (Exception e) {
            e.printStackTrace();
            set = null;
        }

        if (set != null) {
//			try {
//				server_attached_files = (ArrayList<InProgressFileData>) DBHelper
//						.convertFromBytes("inprogress_data");
//			} catch (IOException e) {
//				e.printStackTrace();
//			}

            if (set != null)
                set.currentSurveys = DBHelper.getSurveyyRecords();
            if (set != null)
                set.loopData = DBHelper.selecttblLoopData(Constants.tblLoops,
                        Constants.getWhereSetId(setid), set.loopData);

        }
        if (set == null) {
            return null;
        }

        return set;

    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflator = act.getLayoutInflater();
        final View row = inflator.inflate(R.layout.job_row, parent, false);
        final Order order = joblistarray.get(position).orderItem;
        ImageView returnedReview = (ImageView) row.findViewById(R.id.vreturned);
        ImageView err = (ImageView) row.findViewById(R.id.imgerr);
        Survey survey = joblistarray.get(position).surveyItem;


        if (order != null && order.getOrderID() != null && !order.getOrderID().contains("-")
                && order.getIsJobInProgressOnServer() != null
                && order.getIsJobInProgressOnServer().contains("true")
                && order.getStatusName() != null
                && (order.getStatusName().toLowerCase().contains("progress") || order
                .getStatusName().toLowerCase().contains("completed"))
        ) {
            returnedReview.setVisibility(RelativeLayout.VISIBLE);
            returnedReview.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {

                    customAlert(con, act.getString(R.string.returned_review));
                    // AlertDialog.Builder builder = new
                    // AlertDialog.Builder(con);
                    // builder.setMessage(act.getString(R.string.returned_review))
                    // .setCancelable(false)
                    // .setPositiveButton(
                    // act.getString(R.string.button_ok),
                    // new DialogInterface.OnClickListener() {
                    // public void onClick(
                    // DialogInterface dialog, int id) {
                    // // do things
                    // }
                    // });
                    // AlertDialog alert = builder.create();
                    // alert.show();
                }
            });
        }

        if (order != null && order.getOrderID() != null && !order.getOrderID().contains("-")
                && order.getIsJobDeleted() == true
                && order.getStatusName() != null
                && (order.getStatusName().toLowerCase().contains("progress") || order
                .getStatusName().toLowerCase().contains("completed"))
        ) {
            if (returnedReview != null) returnedReview.setVisibility(RelativeLayout.GONE);
            err.setVisibility(RelativeLayout.VISIBLE);
            err.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {

                    String string = act.getString(R.string.deleted_review);
                    Spanned spReviewName = Html.fromHtml(order.getClientName()
                            + ", " + order.getSetName());

                    try {
                        string = string.replace("[QUESTIONNAIRE]",
                                spReviewName.toString());
                    } catch (Exception ex) {

                    }
                    try {

                        Spanned spBranch = Html.fromHtml(order.getBranchFullname());
                        string = string.replace("[BRANCH]", spBranch.toString());
                    } catch (Exception ex) {

                    }
                    try {

                        Spanned spDate = Html.fromHtml(order.getDate());
                        string = string.replace("[DATE]", spDate.toString());
                    } catch (Exception ex) {

                    }

                    customAlert(con, string);
                }
            });
        }
        final ImageView imgChangeDate = (ImageView) row
                .findViewById(R.id.btn_alt_order);
        final ImageView imgChangeBranch = (ImageView) row
                .findViewById(R.id.btn_alt_branch);
        final ImageView imgCalendar = (ImageView) row
                .findViewById(R.id.btn_calendar);
        final ImageView imgBriefing = (ImageView) row
                .findViewById(R.id.btn_briefing);
        final ImageView imgPreview = (ImageView) row
                .findViewById(R.id.btn_preview);
        final ImageView imgpopup = (ImageView) row.findViewById(R.id.ivbrief);
        final RelativeLayout popuplayout = (RelativeLayout) row
                .findViewById(R.id.popup_layout);
//        imgpopup.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (popuplayout.getVisibility() == RelativeLayout.VISIBLE)
//                    popuplayout.setVisibility(RelativeLayout.GONE);
//                else {
//                    if (lastpopuplayout != null)
//                        lastpopuplayout.setVisibility(RelativeLayout.GONE);
//                    popuplayout.setVisibility(RelativeLayout.VISIBLE);
//                    lastpopuplayout = popuplayout;
//                }
//            }
//        });
        if (order != null && order.getOrderID() != null
                && !order.getOrderID().contains("-")
                && order.getStatusName() != null
                && !order.getStatusName().toLowerCase().contains("survey")

        ) {
//			imgpopup.setVisibility(RelativeLayout.VISIBLE);
            imgChangeDate.setVisibility(RelativeLayout.VISIBLE);
            imgChangeDate.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    popuplayout.setVisibility(RelativeLayout.GONE);
                    if (dateCallback != null) {
                        dateCallback.datecallback(order.getOrderID(),
                                order.getDate());
                    }
                }
            });

            imgChangeBranch.setVisibility(RelativeLayout.VISIBLE);
            imgChangeBranch.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    popuplayout.setVisibility(RelativeLayout.GONE);

                    if (branchCallback != null) {
                        branchCallback.callback(order.getOrderID(),
                                order.getStatusName());
                    }
                }
            });

            imgCalendar.setVisibility(RelativeLayout.VISIBLE);
            imgCalendar.setTag(order);
            imgCalendar.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    popuplayout.setVisibility(RelativeLayout.GONE);
                    Order or = (Order) v.getTag();
                    if (act != null && or.getOrderID() != null && !or.getOrderID().contains("-")) {
                        ((JobListActivity) act).showCalendar(or);
                    }
                }
            });


            if (order.getBriefingContent() != null
                    && order.getBriefingContent().trim().length() > 0) {
                imgBriefing.setVisibility(RelativeLayout.VISIBLE);
                imgBriefing.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popuplayout.setVisibility(RelativeLayout.GONE);
                        if (act != null) {
                            String content = "<HTML><BODY>"
                                    + order.getBriefingContent()
                                    + "</HTML></BODY>";
                            ((JobListActivity) act).makeBriefingDialog(act,
                                    content, true);
                        }
                    }
                });
            } else
                imgBriefing.setVisibility(RelativeLayout.INVISIBLE);


        }
        imgPreview.setVisibility(RelativeLayout.VISIBLE);
        imgPreview.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                String content = "";
                Set set = getSetsRecords(order.getSetID(), order.getOrderID());
                if (set != null && set.getShowPreviewButton().equals("1")
                        || set.getShowPreviewButton().equals("2")) {
                    try {
                        content = DBHelper.readHTMLFromFile("html_" + order.getOrderID() + ".html");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (content == null || content.equals("")) {

                        ArrayList<Objects> listObjects = set.getStructuredListObjects(true);
                        listObjects = QuestionnaireActivity.setQuestionNumbers(listObjects, null);
                        ArrayList<filePathDataID> uploadList = new ArrayList<filePathDataID>();
                        uploadList = DBHelper.getQuestionnaireUploadFiles(
                                Constants.UPLOAD_FILE_TABLE, new String[]{
                                        Constants.UPLOAD_FILe_MEDIAFILE,
                                        Constants.UPLOAD_FILe_DATAID,
                                        Constants.UPLOAD_FILe_ORDERID,
                                        Constants.UPLOAD_FILe_BRANCH_NAME,
                                        Constants.UPLOAD_FILe_CLIENT_NAME,
                                        Constants.UPLOAD_FILe_DATE,
                                        Constants.UPLOAD_FILe_SET_NAME,
                                        Constants.UPLOAD_FILe_SAMPLE_SIZE,}, order.getOrderID(),
                                Constants.DB_TABLE_SUBMITSURVEY_OID, uploadList);
                        content = QuestionnaireActivity.getHtml(listObjects
                                , new ArrayList<QuestionnaireData>(), order.getOrderID(), uploadList, set, null, null);
                    }


                } else {
                    content = "You are not allowed to see this questionnaire's preview";
                }
                popuplayout.setVisibility(RelativeLayout.GONE);
                if (act != null) {
                    //String content = "";

                    content = "<HTML><BODY>"
                            + content
                            + "</HTML></BODY>";
                    ((JobListActivity) act).makeBriefingDialog(act,
                            content, false);
                }

            }
        });
        if (order.getStatusName().toLowerCase().contains("rogress") ||
                order.getStatusName().toLowerCase().contains("completed")) {
            imgChangeDate.setVisibility(RelativeLayout.INVISIBLE);
            imgChangeBranch.setVisibility(RelativeLayout.INVISIBLE);
            imgCalendar.setVisibility(RelativeLayout.INVISIBLE);
            imgPreview.setVisibility(RelativeLayout.VISIBLE);
//			imgpopup.setVisibility(RelativeLayout.VISIBLE);

        }
//		else
//			imgPreview.setVisibility(RelativeLayout.INVISIBLE);


        if (order != null
                && (order.getStatusName().toLowerCase()
                .equals(tabFilter.toLowerCase()) || order
                .getStatusName().toLowerCase()
                .equals(tabFilterTwo.toLowerCase()))) {

            TextView tv = (TextView) row.findViewById(R.id.tv1);
            tvDec = (TextView) row.findViewById(R.id.tvDec);
            tv.setTextSize(UIHelper.getFontSize(ct, tv.getTextSize()));
            TextView datetv = (TextView) row.findViewById(R.id.tv2);
            TextView branchtv = (TextView) row.findViewById(R.id.tvbranch);
            TextView counttv = (TextView) row.findViewById(R.id.tvcount);
            TextView tvLocation = (TextView) row.findViewById(R.id.tv_location);
            ImageView ivLocation = (ImageView) row.findViewById(R.id.iv_location);
            tv.setTextSize(UIHelper.getFontSize(ct, tv.getTextSize()));
            TextView tvReject = (TextView) row.findViewById(R.id.tv_reject);
            tvAccept = (TextView) row.findViewById(R.id.tv_accept);
            tvStatusShow = (TextView) row.findViewById(R.id.tv_status_show);
            tvRejectShow = (TextView) row.findViewById(R.id.tv_reject_show);
            tvTime = (TextView) row.findViewById(R.id.tv_time);
            View view2 = (View) row.findViewById(R.id.view2);
            View view1 = (View) row.findViewById(R.id.view1);
            LinearLayout llSchedulesExpandLayout = (LinearLayout) row.findViewById(R.id.ll_schedules_expand_layout);
            LinearLayout llAssignedExpandLayout = (LinearLayout) row.findViewById(R.id.ll_assigned_expand_layout);
            ImageView ivDownArrow = (ImageView) row.findViewById(R.id.iv_down_arrow);
            CardView cardView = (CardView) row.findViewById(R.id.cardView);
            NestedScrollView nestedScroll = (NestedScrollView) row.findViewById(R.id.nestedScroll);
            LinearLayout llStartJob = (LinearLayout) row.findViewById(R.id.ll_start_job);
            TextView tvStartJob = (TextView) row.findViewById(R.id.tv_start_job);
            ImageView ivLanguage = (ImageView) row.findViewById(R.id.iv_language);
            ivCalendar = (ImageView) row.findViewById(R.id.iv_calendar);
            LinearLayout llAccept = (LinearLayout) row.findViewById(R.id.ll_accept);
            clBottom = (LinearLayout) row.findViewById(R.id.cl_bottom);

            TextView tvSurveyName = (TextView) row.findViewById(R.id.tv_survey_name);
            TextView tvBranchFullName = (TextView) row.findViewById(R.id.tv_branch_full_name);
            TextView tvClientDesc = (TextView) row.findViewById(R.id.tv_client_desc);
            TextView tvQuestionnaire = (TextView) row.findViewById(R.id.tv_questionnaire);
            TextView tvBranch_s = (TextView) row.findViewById(R.id.tv_branch_s);
            TextView tvBranchFullName_s = (TextView) row.findViewById(R.id.tv_branch_full_name_s);
            TextView tvCity = (TextView) row.findViewById(R.id.tv_city);
            TextView tvAddress = (TextView) row.findViewById(R.id.tv_address);
            TextView tvBranchPhone = (TextView) row.findViewById(R.id.tv_branch_phone);
            TextView tvOpeningHours = (TextView) row.findViewById(R.id.tv_opening_hours);
            TextView tvStartTime = (TextView) row.findViewById(R.id.tv_start_time);
            TextView tvEndTime = (TextView) row.findViewById(R.id.tv_end_time);
            TextView tvPurchaseDescription = (TextView) row.findViewById(R.id.tv_purchase_description);
            TextView tvMakePurchase = (TextView) row.findViewById(R.id.tv_make_purchase);
            TextView tvNonRefundableServicePayment = (TextView) row.findViewById(R.id.tv_nonRefundableServicePayment);
            TextView tvTransportationPayment = (TextView) row.findViewById(R.id.tv_transportation_payment);
            TextView tvCriticismPayment = (TextView) row.findViewById(R.id.tv_criticism_payment);
            TextView tvBonusPayment = (TextView) row.findViewById(R.id.tv_bonus_payment);
            TextView tvStatus = (TextView) row.findViewById(R.id.tv_status);
            TextView tvStatusAssigned = (TextView) row.findViewById(R.id.tv_status_assigned);

            ListView lvjdsurvey_quotas_list = (ListView) row.findViewById(R.id.quotas_list);
            ListView lvjdsurvey_allocations_list = (ListView) row.findViewById(R.id.allocations_list);

            View space = (View) row.findViewById(R.id.space);
            viewDate = (View) row.findViewById(R.id.viewDate);


            nestedScroll.setVisibility(View.GONE);
            space.setVisibility(View.GONE);

            if (position == getCount() - 1) {
                space.setVisibility(View.VISIBLE);
            }

//            ImageView uncheck_checkbox = (ImageView) row.findViewById(R.id.uncheck_checkbox);
//            ImageView check_checkbox = (ImageView) row.findViewById(R.id.check_checkbox);
//            ConstraintLayout cl_checkbox = (ConstraintLayout) row.findViewById(R.id.cl_checkbox);
//            cl_checkbox.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    select = !select;
//                    if (select) {
//                        check_checkbox.setVisibility(View.VISIBLE);
//                        uncheck_checkbox.setVisibility(View.GONE);
//                    } else {
//                        check_checkbox.setVisibility(View.GONE);
//                        uncheck_checkbox.setVisibility(View.VISIBLE);
//                    }
//                }
//            });


            tvRejectShow.setText(con.getResources().getString(R.string.jd_reject_btn_text));

            ivDownArrow.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (nestedScroll.getVisibility() == View.GONE) {
                        nestedScroll.setVisibility(View.VISIBLE);
//                        ivDownArrow.setBackgroundDrawable(ct.getResources()
//                                .getDrawable(R.drawable.ic_upper_arrow));

                        if (order.getOrderID().contains("-")) {
                            llAssignedExpandLayout.setVisibility(View.VISIBLE);
                            llSchedulesExpandLayout.setVisibility(View.GONE);
                        } else if (order != null && order.getSetName() != null
                                && !order.getSetName().equals("")) {
                            llAssignedExpandLayout.setVisibility(View.GONE);
                            llSchedulesExpandLayout.setVisibility(View.VISIBLE);
                        }
                    } else {
                        nestedScroll.setVisibility(View.GONE);
//                        ivDownArrow.setBackgroundDrawable(ct.getResources()
//                                .getDrawable(R.drawable.ic_down_arrow));
                    }
                }
            });


            if (order.getOrderID().contains("-"))
                tvStatusShow.setText(con.getString(R.string.jd_begin_btn_text));
            else if (order.getStatusName().equals("Assigned"))
                tvStatusShow.setText(con.getString(R.string.jd_accept_btn_text));
            else if (order.getStatusName().equals("Scheduled") || order.getStatusName().equals("cert"))
                tvStatusShow.setText(con.getString(R.string.jd_begin_review_btn_text));
            else if (order.getStatusName().equals("in progress") || order.getStatusName().equals("In progress") || order.getStatusName().toLowerCase().equals("archived"))// (getString(R.string.jd_begin_button_status_inprogress)))
                tvStatusShow.setText(con.getString(R.string.jd_continue_review_btn_text));
            else if (order.getStatusName().equals("Completed"))// (getString(R.string.jd_begin_button_status_completed)))
            {
                // rejectbtn.setText(getString(R.string.jd_synch_button_text));
                tvStatusShow.setText(con.getString(R.string.jd_continue_review_btn_text));
            }

            myPrefs = con.getSharedPreferences("pref", MODE_PRIVATE);
            tvAccept.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tvStatusShow.getText().toString()
                            .equals(con.getString(R.string.jd_begin_btn_text))) {
                        if (order.getOrderID().contains("-")) {
                            if (order.getStatusName().equals("survey")) {
                                Survey s = Surveys.getCurrentSurve(order.getOrderID().replace(
                                        "-", ""));
                                if (s != null && s.isAllocationReached()) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(con);
                                    builder.setMessage(
                                                    con.getResources()
                                                            .getString(
                                                                    R.string.questionnaire_open_survey_alert))
                                            .setTitle(
                                                    con.getResources().getString(
                                                            R.string._alert_title))
                                            .setCancelable(false)
                                            .setPositiveButton(
                                                    con.getResources().getString(
                                                            R.string.button_ok),
                                                    new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(
                                                                DialogInterface dialog,
                                                                int id) {
                                                            dialog.dismiss();
//                                                            ((Activity) con).finish();
                                                        }
                                                    });
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                } else {
                                    SplashScreen.addLog(new BasicLog(
                                            myPrefs.getString(Constants.SETTINGS_SYSTEM_URL_KEY, ""),
                                            myPrefs.getString(Constants.POST_FIELD_LOGIN_USERNAME, ""), "Starting survey!" + order.getSetName() + "status:" + order.getStatusName(), order.getOrderID()));

                                    ((JobListActivity) con).startLocationCheckerAdapter();
                                }

                            } else {
                                SplashScreen.addLog(new BasicLog(
                                        myPrefs.getString(Constants.SETTINGS_SYSTEM_URL_KEY, ""),
                                        myPrefs.getString(Constants.POST_FIELD_LOGIN_USERNAME, ""), "Starting survey!" + order.getSetName() + "status:" + order.getStatusName(), order.getOrderID()));

                                ((JobListActivity) con).startLocationCheckerAdapter();
                            }
                        }
                    }
                    if (tvStatusShow.getText().toString()
                            .equals(con.getString(R.string.jd_accept_btn_text))) {
                        int ins = ((JobListActivity) con).IsInternetConnecttedAdapter();
                        if (ins >= 0) {
                            ((JobListActivity) con).ShowAlertButton(con,
                                    con.getString(R.string.jd_parsing_alert_title),
                                    con.getString(R.string.accept_job_fail_alert),
                                    con.getString(R.string.alert_btn_lbl_ok));
                            return;
                        }
                        ((JobListActivity) con).someMethod(con.getString(R.string.jd_accept_btn_text));

                    } else if (tvStatusShow.getText().toString()
                            .equals(con.getString(R.string.jd_begin_review_btn_text))) {
                        if (isBriefing == true) {
                            isBriefing = false;
//                            showBriefing();
                        } else {
                            SplashScreen.addLog(new BasicLog(
                                    myPrefs.getString(Constants.SETTINGS_SYSTEM_URL_KEY, ""),
                                    myPrefs.getString(Constants.POST_FIELD_LOGIN_USERNAME, ""), "Starting Order!" + order.getOrderID() + " = " + order.getSetName() + "status:" + order.getStatusName(), order.getOrderID()));

                            ((JobListActivity) con).startLocationCheckerAdapter();
                        }

                    } else if (tvStatusShow.getText().toString()
                            .equals(con.getString(R.string.jd_continue_review_btn_text))) {
                        SplashScreen.addLog(new BasicLog(
                                myPrefs.getString(Constants.SETTINGS_SYSTEM_URL_KEY, ""),
                                myPrefs.getString(Constants.POST_FIELD_LOGIN_USERNAME, ""), "Starting Order!" + order.getOrderID() + " = " + order.getSetName() + "status:" + order.getStatusName(), order.getOrderID()));

                        if (Settings.Secure.getString(
                                con.getContentResolver(),
                                Settings.Secure.ALLOW_MOCK_LOCATION).equals("1")) {
                            ((JobListActivity) con).MockGPSALERT();
                            return;
                        }

                        ((JobListActivity) con).BeginReview(false);
                    }
                }
            });

            if ((order != null && order.getStatusName() != null && order.getStatusName().toLowerCase().contains("archive")) || (order != null && order.getAsArchive())) {
                tvRejectShow.setText(con.getResources().getString(R.string.button_back_archive));
            }

            tvReject.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClick(position);
                }
            });

            ivLanguage.setOnClickListener(v -> ((JobListActivity) ct).showLanguageDialog(
                    ct.getResources().getString(
                            R.string.preffered_questionnaire_language), false));

            tvStartJob.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (tvStatusShow.getText().toString().equals(ct.getString(R.string.jd_begin_review_btn_text))) {
                        Constants.accept_txt = "Begin_Survey";
                        mJobStartListener.onJobStartClick(position, Constants.accept_txt);
                    } else if (tvStatusShow.getText().toString().equals(ct.getString(R.string.jd_continue_review_btn_text))) {
                        Constants.accept_txt = "Continue_survey";
                        mJobStartListener.onJobStartClick(position, Constants.accept_txt);
                    }


//                    if (order.getOrderID().contains("-")) {
//                        if (order.getStatusName().equals("survey")) {
//
//                            if (survey != null && survey.isAllocationReached()) {
//                                AlertDialog.Builder builder = new AlertDialog.Builder(
//                                        con);
//                                builder.setMessage(
//                                                con.getResources()
//                                                        .getString(
//                                                                R.string.questionnaire_open_survey_alert))
//                                        .setTitle(
//                                                con.getResources().getString(
//                                                        R.string._alert_title))
//                                        .setCancelable(false)
//                                        .setPositiveButton(
//                                                con.getResources().getString(
//                                                        R.string.button_ok),
//                                                new DialogInterface.OnClickListener() {
//                                                    @Override
//                                                    public void onClick(
//                                                            DialogInterface dialog,
//                                                            int id) {
//                                                        dialog.dismiss();
////                                                        finish();
//                                                    }
//                                                });
//                                AlertDialog alert = builder.create();
//                                alert.show();
//                            } else {
//                                SplashScreen.addLog(new BasicLog(
//                                        myPrefs.getString(Constants.SETTINGS_SYSTEM_URL_KEY, ""),
//                                        myPrefs.getString(Constants.POST_FIELD_LOGIN_USERNAME, ""), "Starting survey!" + order.getSetName() + "status:" + order.getStatusName(), order.getOrderID()));
//
//                                ((JobListActivity) con).startLocationCheckerAdapter();
//                            }
//
//                        } else {
//                            SplashScreen.addLog(new BasicLog(
//                                    myPrefs.getString(Constants.SETTINGS_SYSTEM_URL_KEY, ""),
//                                    myPrefs.getString(Constants.POST_FIELD_LOGIN_USERNAME, ""), "Starting survey!" + order.getSetName() + "status:" + order.getStatusName(), order.getOrderID()));
//
//                            ((JobListActivity) con).startLocationCheckerAdapter();
//                        }
//                    }
//
//                    if (order.getStatusName().equals("Scheduled") || order.getStatusName().equals("cert")) {
//                        if (isBriefing == true) {
//                            isBriefing = false;
////                                showBriefing();
//                        } else {
//                            SplashScreen.addLog(new BasicLog(
//                                    myPrefs.getString(Constants.SETTINGS_SYSTEM_URL_KEY, ""),
//                                    myPrefs.getString(Constants.POST_FIELD_LOGIN_USERNAME, ""), "Starting Order!" + order.getOrderID() + " = " + order.getSetName() + "status:" + order.getStatusName(), order.getOrderID()));
//
//                            ((JobListActivity) con).startLocationCheckerAdapter();
//                        }
//
//                    } else if (order.getStatusName().equals("Completed") || order.getStatusName().equals("in progress") || order.getStatusName().equals("In progress") || order.getStatusName().equals("archived")) {
//                        SplashScreen.addLog(new BasicLog(
//                                myPrefs.getString(Constants.SETTINGS_SYSTEM_URL_KEY, ""),
//                                myPrefs.getString(Constants.POST_FIELD_LOGIN_USERNAME, ""), "Starting Order!" + order.getOrderID() + " = " + order.getSetName() + "status:" + order.getStatusName(), order.getOrderID()));
//
//                        if (Settings.Secure.getString(
//                                con.getContentResolver(),
//                                Settings.Secure.ALLOW_MOCK_LOCATION).equals("1")) {
//                            ((JobListActivity) con).MockGPSALERT();
//                            return;
//                        }
//
//                        ((JobListActivity) con).BeginReview(false);
//                    }


                    //"OrderID" - Assigned
                    //"SurveyID" - Scheduled,Other

//                    if (order.getOrderID().contains("-")) {
////                        ((JobListActivity) con).BeginReview(true);
//
//                        Intent intent = new Intent(con.getApplicationContext(),
//                                QuestionnaireActivity.class);
//                        intent.putExtra(Constants.POST_FIELD_QUES_ORDER_ID, order.getOrderID());
//                        intent.putExtra(Constants.FIELD_ORDER_SET_ID, order.getSetID());
//                        intent.putExtra(Constants.POST_FIELD_QUES_ORDER_ID, myPrefs.getString("order_id", ""));
//                        intent.putExtra(Constants.FIELD_ORDER_SET_ID, myPrefs.getString("setid", ""));
//                        con.startActivity(intent);
//
//                    } else if (order != null && order.getSetName() != null
//                            && !order.getSetName().equals("")) {
//
////                        ((JobListActivity) con).BeginReview(true);
//                        Intent intent = new Intent(con.getApplicationContext(),
//                                QuestionnaireActivity.class);
//                        if (order == null)
//                            ((JobListActivity) con).setOrder();
//                        intent.putExtra(Constants.POST_FIELD_QUES_ORDER_ID,
//                                order.getOrderID());
//                        intent.putExtra(Constants.FIELD_ORDER_SET_ID, order.getSetID());
////                        if (isFromWatch)
////                            startActivityForResult(intent, QUESTIONNAIRE_ACTIVITY_CODE);
////                        else
//                        con.startActivity(intent);
//
//                    } else {
////                        Toast.makeText(con, "Other tab", Toast.LENGTH_SHORT).show();
//                    }
                }
            });


            String status = order.getStatusName().toLowerCase();

            if (order != null && (status.equals(filterOne.toLowerCase()) || (status.equals(sfilterOne.toLowerCase())))) {
                llStartJob.setVisibility(View.GONE);
                tvAccept.setVisibility(View.VISIBLE);
            } else {
                llStartJob.setVisibility(View.VISIBLE);
                tvAccept.setVisibility(View.GONE);
            }


            //TODO
            final ImageView ivRight = (ImageView) row
                    .findViewById(R.id.rightiv);
            final ListView jobItemList = (ListView) row
                    .findViewById(R.id.joblistbelow);
            if (order != null
                    && this.joblistarray.get(position).listOrders != null
                    && this.joblistarray.get(position).listOrders.size() > 1
                    && hideShowRight(order.getStatusName(), ivRight)) {
                row.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
//                        if (jobItemList.getVisibility() == View.GONE) {
//                            jobItemList.setVisibility(View.VISIBLE);
//                            ivRight.setBackgroundDrawable(ct.getResources()
//                                    .getDrawable(R.drawable.upppp));
//                            if (lastpopuplayout != null)
//                                lastpopuplayout.setVisibility(RelativeLayout.GONE);
//                        } else {
//                            jobItemList.setVisibility(View.GONE);
//                            ivRight.setBackgroundDrawable(ct.getResources()
//                                    .getDrawable(R.drawable.downnnn));
//                        }
                    }
                });
                jobItemList.setAdapter(new jobInnerItemAdapter(act,
                        this.joblistarray.get(position).listOrders));
//                TODO JobDetailActivity
                jobItemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1,
                                            int arg2, long arg3) {
                        if (joblistarray.get(position).listOrders != null
                                && joblistarray.get(position).listOrders
                                .get(arg2) != null
                                && joblistarray.get(position).listOrders.get(
                                arg2).getStatusName() != null
                                && joblistarray.get(position).listOrders
                                .get(arg2).getStatusName()
                                .equals("wrong")) {
                            // showRAlert(JobListActivity.this);
                        } else {
                            Intent intent = new Intent(ct
                                    .getApplicationContext(),
                                    JobDetailActivity.class);
                            // isJobselected = true;
                            if (joblistarray.get(position).listOrders != null
                                    && joblistarray.get(position).listOrders
                                    .size() > 0) {

                                // intent.putExtra("briefing",joblistarray.get(position).listOrders.
                                // );
                                intent.putExtra("OrderID", joblistarray
                                        .get(position).listOrders.get(arg2)
                                        .getOrderID());
                            }
                            intent.putExtra("OrderIndex", arg2);
                            intent.putExtra("Index", arg2);
                            Constants.setLocale(act);
                            act.startActivityForResult(intent,
                                    JOB_DETAIL_ACTIVITY_CODE);
                        }
                    }
                });
                //TODO Date filter
                jobItemList.setVisibility(View.GONE);
                setListViewHeightBasedOnChildren(jobItemList);
                ivRight.setVisibility(View.VISIBLE);
                imgpopup.setVisibility(RelativeLayout.INVISIBLE);
                clBottom.setVisibility(View.GONE);
                ivCalendar.setVisibility(View.GONE);
                viewDate.setVisibility(View.GONE);
                tvTime.setVisibility(View.GONE);
                tvDec.setVisibility(View.GONE);
            } else {
                // if (!Constants.getDateFilter())
                imgpopup.setVisibility(RelativeLayout.GONE);
                ivRight.setVisibility(View.GONE);
                clBottom.setVisibility(View.VISIBLE);
                ivCalendar.setVisibility(View.VISIBLE);
                viewDate.setVisibility(View.VISIBLE);
                tvTime.setVisibility(View.VISIBLE);
                tvDec.setVisibility(View.VISIBLE);
            }

            ivRight.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    if (jobItemList.getVisibility() == View.GONE) {
                        jobItemList.setVisibility(View.VISIBLE);
                        ivRight.setBackgroundDrawable(ct.getResources()
                                .getDrawable(R.drawable.upppp));
//                        if (lastpopuplayout != null)
//                            lastpopuplayout.setVisibility(RelativeLayout.GONE);
                    } else {
                        jobItemList.setVisibility(View.GONE);
                        ivRight.setBackgroundDrawable(ct.getResources()
                                .getDrawable(R.drawable.downnnn));
                    }
                }
            });

            if (Helper.getTheme(ct) == 0) {
                tv.setTextColor(ct.getResources().getColor(
                        android.R.color.white));
                datetv.setTextColor(ct.getResources().getColor(
                        android.R.color.white));
            }

            //Assigned tab data
            if (order.getOrderID().contains("-")) {
                Survey s = Surveys.getCurrentSurve(order.getOrderID().replace(
                        "-", ""));
                if (s != null && s.getSurveyName() != null) {
                    // tv.setText(order.getOrderID()+s.getSurveyName());
                    tv.setText(s.getSurveyName());
                    tvSurveyName.setText(s.getSurveyName());
                    tvBranchFullName.setText(s.getBranchFullName());
                    tvStatusAssigned = getTextFromHtmlFormate(order.getStatusName(), tvStatusAssigned);
                } else {
                    if (!Constants.getDateFilter())
                        ivRight.setVisibility(RelativeLayout.GONE);
                    // iv.setBackgroundResource(getResource("wrong"));

                    // Spanned sp = Html.fromHtml(s.getSurveyName());
                    tv.setText("Could not retrieve Survey...");
                    return row;
                }

                branchtv.setVisibility(View.GONE);
                counttv.setVisibility(View.GONE);
                view1.setVisibility(View.GONE);

                try {
                    if (order.getBranchLong() != null && order.getBranchLat() != null) {
                        double location_latitude = Double.parseDouble(order.getBranchLat());
                        double location_longitude = Double.parseDouble(order.getBranchLong());
                        String get_location = String.valueOf(((JobListActivity) con).CalculationByDistance(Constants.user_location_latitude, location_latitude, Constants.user_location_longitude, location_longitude));
                        tvLocation.setText(get_location + " Km");
                        tvLocation.setVisibility(View.VISIBLE);
                        ivLocation.setVisibility(View.VISIBLE);
                    } else if (order.getBranchLong() == null || order.getBranchLat() == null) {
                        tvLocation.setVisibility(View.GONE);
                        ivLocation.setVisibility(View.GONE);
                    }
                } catch (NumberFormatException e) {

                }


                if (s != null && s.getArrayQuotas() != null) {
                    String str[] = s.getArrayQuotas();

                    ArrayAdapter adapter = new ArrayAdapter(ct,
                            UIHelper.getListLayoutSize(ct), str);
                    lvjdsurvey_quotas_list.setAdapter(adapter);

                    setListViewHeightBased(lvjdsurvey_quotas_list);
                }


                if (s != null && s.getArrayAllocations() != null) {

                    String str[] = s.getArrayAllocations();

                    ArrayAdapter adapter = new ArrayAdapter(ct,
                            UIHelper.getListLayoutSize(ct), str);
                    lvjdsurvey_allocations_list.setAdapter(adapter);
                    setListViewHeightBased(lvjdsurvey_allocations_list);
                }


            } else if (order != null && order.getSetName() != null
                    && !order.getSetName().equals("")) {

                if (order.getClientName() == null)
                    order.setClientName("");
                if (order.getSetName() == null)
                    order.setSetName("");
//                Spanned sp = Html.fromHtml(order.getClientName() + ", "
//                        + order.getSetName());

                tv = getTextFromHtmlFormate(order.getClientName(), tv);
                tvDec = getTextFromHtmlFormate(order.getSetName(), tvDec);
                tvClientDesc = getTextFromHtmlFormate(order.getDescription(), tvClientDesc);
                tvQuestionnaire = getTextFromHtmlFormate(order.getSetName(), tvQuestionnaire);
                tvBranch_s = getTextFromHtmlFormate(order.getBranchName(), tvBranch_s);
                tvBranchFullName_s = getTextFromHtmlFormate(order.getBranchFullname(), tvBranchFullName_s);
                tvCity = getTextFromHtmlFormate(order.getCityName(), tvCity);
                tvAddress = getTextFromHtmlFormate(order.getAddress(), tvAddress);
                tvBranchPhone = getTextFromHtmlFormate(order.getBranchPhone(), tvBranchPhone);
                tvOpeningHours = getTextFromHtmlFormate(order.getOpeningHours(), tvOpeningHours);
                tvStartTime = getTextFromHtmlFormate(order.getTimeStart(), tvStartTime);
                tvEndTime = getTextFromHtmlFormate(order.getTimeEnd(), tvEndTime);
                tvPurchaseDescription = getTextFromHtmlFormate(order.getsPurchaseDescription(), tvPurchaseDescription);
                tvNonRefundableServicePayment = getTextFromHtmlFormate(order.getsNonRefundableServicePayment(), tvNonRefundableServicePayment);
                tvTransportationPayment = getTextFromHtmlFormate(
                        order.getsTransportationPayment(), tvTransportationPayment);
                tvCriticismPayment = getTextFromHtmlFormate(
                        order.getsCriticismPayment(), tvCriticismPayment);
                tvBonusPayment = getTextFromHtmlFormate(order.getsBonusPayment(),
                        tvBonusPayment);
                tvStatus = getTextFromHtmlFormate(order.getStatusName(), tvStatus);

                try {
                    if (order.getBranchLong() != null && order.getBranchLat() != null) {
                        double location_latitude = Double.parseDouble(order.getBranchLat());
                        double location_longitude = Double.parseDouble(order.getBranchLong());
                        String get_location = String.valueOf(((JobListActivity) con).CalculationByDistance(Constants.user_location_latitude, location_latitude, Constants.user_location_longitude, location_longitude));
                        tvLocation.setText(get_location + " Km");
                        tvLocation.setVisibility(View.VISIBLE);
                        ivLocation.setVisibility(View.VISIBLE);
                    } else {
                        tvLocation.setVisibility(View.GONE);
                        ivLocation.setVisibility(View.GONE);
                    }
                } catch (NumberFormatException e) {

                }


                if ((order != null && order.getsPurchase() != null && order
                        .getsPurchase().equals("1"))) {
                    tvMakePurchase = getTextFromHtmlFormate(
                            con.getString(R.string.questionnaire_exit_delete_alert_yes),
                            tvMakePurchase);
                } else {
                    tvMakePurchase = getTextFromHtmlFormate(
                            con.getString(R.string.questionnaire_exit_delete_alert_no),
                            tvMakePurchase);
                }

            } else {
                if (order.getClientName() == null)
                    order.setClientName("");
                Spanned sp = Html.fromHtml(order.getClientName());
                tv.setText(sp.toString());
            }

            if (order.getOrderID().contains("-")) {
                Survey s = Surveys.getCurrentSurve(order.getOrderID().replace(
                        "-", ""));
                if (s.getTargetQuota() == null)
                    s.setTargetQuota("");
                int subCount = 0;
                if (s.getListAllocations() != null
                        && s.getListAllocations().size() > 0) {
                    Spanned sp = Html.fromHtml(s.getListAllocations().get(0)
                            .getSurveyCount()
                            + "/"
                            + s.getListAllocations().get(0).getAllocation());
                    datetv.setText(sp.toString());
                    subCount = Integer.parseInt(s.getListAllocations().get(0)
                            .getAllocation())
                            - Integer.parseInt(s.getListAllocations().get(0)
                            .getSurveyCount());
                } else {
                    Spanned sp = Html.fromHtml(s.getSurveyCount() + "/"
                            + s.getTargetQuota());
                    subCount = Integer.parseInt(s.getTargetQuota())
                            - Integer.parseInt(s.getSurveyCount());
                    datetv.setText(sp.toString());
                }
                String str = "<B>(" + subCount + ")</B> "
                        + datetv.getText().toString();
                str = setBranchIfNeeded(
                        str,
                        !(order != null
                                && this.joblistarray.get(position).listOrders != null
                                && this.joblistarray.get(position).listOrders
                                .size() > 1
                                && !tabFilter.equals("assigned")
                                && !tabFilter.equals("scheduled") && !hideSurvey(
                                order.getStatusName(), ivRight)), order);
                Spanned sp = Html.fromHtml(str);
                datetv.setText(sp, BufferType.SPANNABLE);
            } else if (!order.getBranchName().equals("")
                    || !order.getDate().equals("")) {
                if (!order.getBranchName().equals("")
                        && Constants.getEnableAlternateOrder()
                        && this.tabFilter != null
                        && (this.tabFilter.equals(this.filterOne) || this.tabFilter
                        .equals(this.filterTwo))) {
                    Spanned sp = Html.fromHtml(getDate(order.getDate()));
                    branchtv.setVisibility(RelativeLayout.VISIBLE);
                    counttv.setVisibility(RelativeLayout.VISIBLE);
                    datetv.setText(sp.toString());
                    branchtv.setVisibility(RelativeLayout.VISIBLE);
                    branchtv.setText(order.getBranchName());
                    if (Constants.getFullBranchName()
                            && order.getBranchFullname() != null
                            && order.getBranchFullname().length() > 0) {
                        branchtv.setText(order.getBranchFullname() + " "
                                + order.getAddress());

                    }

                    if (Constants.getFullBranchName()
                            && order.getBranchFullname() != null
                            && order.getBranchFullname().length() > 0)
                        branchtv.setText(order.getBranchFullname() + " "
                                + order.getAddress());

                    // branchtv.setOnClickListener(new OnClickListener() {
                    //
                    // @Override
                    // public void onClick(View v) {
                    // if (branchCallback != null) {
                    // branchCallback.callback(order.getOrderID(),
                    // order.getStatusName());
                    // }
                    // }
                    // });
                } else {
//                    Spanned sp = Html.fromHtml(getDate(order.getDate()) + ", "
//                            + order.getBranchName());
                    Spanned sp = Html.fromHtml(getDate(order.getDate()));


                    if (Constants.getFullBranchName()
                            && order.getBranchFullname() != null
                            && order.getBranchFullname().length() > 0)
                        sp = Html.fromHtml(getDate(order.getDate()) + ", "
                                + order.getBranchFullname() + " "
                                + order.getAddress());

                    branchtv.setText(order.getBranchName());
                    branchtv.setVisibility(View.VISIBLE);
                    // branchtv.setVisibility(RelativeLayout.GONE);
                    counttv.setVisibility(View.GONE);
                    datetv.setText(sp.toString());
                }

                // alternate dates
                if (order.getDate() != null
                        && !order.getDate().equals("")
                        && this.tabFilter != null
                        && (this.tabFilter.equals(this.filterOne) || this.tabFilter
                        .equals(this.filterTwo))) {
                    if (Constants.getFullBranchName()
                            && order.getBranchFullname() != null
                            && order.getBranchFullname().length() > 0) {
                        branchtv.setText(order.getBranchFullname() + " "
                                + order.getAddress());
                        branchtv.setVisibility(RelativeLayout.VISIBLE);

                    } else {
                        branchtv.setText(order.getBranchName());
                        branchtv.setVisibility(RelativeLayout.VISIBLE);
                    }
                    Spanned sp = Html.fromHtml(getDate(order.getDate()));
                    datetv.setText(sp.toString());
                    datetv.setTextColor(Color.parseColor("#0000ef"));
                    // datetv.setOnClickListener(new OnClickListener() {
                    //
                    // @Override
                    // public void onClick(View v) {
                    // if (dateCallback != null) {
                    // dateCallback.datecallback(order.getOrderID(),
                    // order.getDate());
                    // }
                    // }
                    // });
                }

            } else {
                if (Constants.getFullBranchName()
                        && order.getBranchFullname() != null
                        && order.getBranchFullname().length() > 0) {
                    branchtv.setText(order.getBranchFullname() + " "
                            + order.getAddress());
                    branchtv.setVisibility(RelativeLayout.VISIBLE);

                } else {
                    branchtv.setText(order.getBranchName());
                    branchtv.setVisibility(RelativeLayout.VISIBLE);
                }
                Spanned sp = Html.fromHtml(getDate(order.getDate()));
                datetv.setText(sp.toString());
            }
            if (!order.getOrderID().contains("-") && order.getCount() > 1
                    && branchtv.getVisibility() == RelativeLayout.GONE) {
                String str = "<B>(" + order.getCount() + ")</B> "
                        + datetv.getText().toString();
                Spanned sp = Html.fromHtml(str);
                datetv.setText(sp, BufferType.SPANNABLE);
            } else if (!order.getOrderID().contains("-")
                    && order.getCount() > 1) {
                // counttv.setVisibility(RelativeLayout.VISIBLE);
                counttv.setText("(" + order.getCount() + ")");
            }

            if (checkIfCorrect(order.getSetID()) == false) {
                // order.setStatusName("wrong");
                // iv.setBackgroundResource(getResource("wrong"));
            } else {
                // iv.setBackgroundResource(getResource(order.getStatusName()));
            }

            // hideShowRight(order.getStatusName(),ivRight);
        }
        if (survey != null
                && (order.getStatusName().toLowerCase().equals(tabFilter) || tabFilter
                .equals("all"))) {
            TextView tv = (TextView) row.findViewById(R.id.tv1);
            tv.setTextSize(UIHelper.getFontSize(ct, tv.getTextSize()));
            TextView datetv = (TextView) row.findViewById(R.id.tv2);
            tv.setTextSize(UIHelper.getFontSize(ct, tv.getTextSize()));
            ImageView iv = (ImageView) row.findViewById(R.id.leftiv);
            iv.setBackgroundResource(getResource("survey"));
            //TODO
            final ImageView ivRight = (ImageView) row
                    .findViewById(R.id.rightiv);
            final ListView jobItemList = (ListView) row
                    .findViewById(R.id.joblistbelow);
            if (survey != null
                    && this.joblistarray.get(position).listSurveys != null
                    && this.joblistarray.get(position).listSurveys.size() > 1
                    && order.getStatusName().equals("In progress")) {
                row.findViewById(R.id.vreturned).setVisibility(
                        RelativeLayout.GONE);
                row.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
//                        if (jobItemList.getVisibility() == View.GONE) {
//                            jobItemList.setVisibility(View.VISIBLE);
//                            ivRight.setBackgroundDrawable(ct.getResources()
//                                    .getDrawable(R.drawable.upppp));
//                        } else {
//                            jobItemList.setVisibility(View.GONE);
//                            ivRight.setBackgroundDrawable(ct.getResources()
//                                    .getDrawable(R.drawable.downnnn));
//                        }
                    }
                });
                jobItemList.setAdapter(new jobInnerItemAdapter(act,
                        this.joblistarray.get(position).listOrders));
//                TODO JobDetailActivity_2
                jobItemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1,
                                            int arg2, long arg3) {
                        if (joblistarray.get(position).listSurveys != null
                                && joblistarray.get(position).listSurveys
                                .get(arg2) != null) {
                            // showRAlert(JobListActivity.this);
                        } else {
                            Intent intent = new Intent(ct
                                    .getApplicationContext(),
                                    JobDetailActivity.class);
                            // isJobselected = true;
                            if (joblistarray.get(position).listSurveys != null
                                    && joblistarray.get(position).listSurveys
                                    .size() > 0) {
                                Order o_rder = joblistarray.get(position).orderItem;
                                Survey s = Surveys.getCurrentSurve(o_rder
                                        .getOrderID().replace("-", ""));
                                if (s != null && s.getSurveyName() != null) {

                                } else {

                                    Toast.makeText(
                                            con,
                                            con.getResources()
                                                    .getString(
                                                            R.string.could_not_retrieve),
                                            Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                // intent.putExtra("briefing",joblistarray.get(position).listOrders.
                                // );
                                intent.putExtra("SurveyID", joblistarray
                                        .get(position).listSurveys.get(arg2)
                                        .getSurveyID());
                                intent.putExtra("OrderID", "");
                            }
                            intent.putExtra("OrderIndex", arg2);
                            intent.putExtra("Index", arg2);
                            Constants.setLocale(act);
                            act.startActivityForResult(intent,
                                    JOB_DETAIL_ACTIVITY_CODE);
                        }
                    }
                });
                //TODO Date filter_2
                jobItemList.setVisibility(View.GONE);
                setListViewHeightBasedOnChildren(jobItemList);
                ivRight.setVisibility(View.GONE);
//                clBottom.setVisibility(View.GONE);
//                ivCalendar.setVisibility(View.GONE);
//                viewDate.setVisibility(View.GONE);
//                tvTime.setVisibility(View.GONE);
//                tvDec.setVisibility(View.GONE);
            } else {
                if (!Constants.getDateFilter())
                    ivRight.setVisibility(View.GONE);
//                clBottom.setVisibility(View.VISIBLE);
//                ivCalendar.setVisibility(View.VISIBLE);
//                viewDate.setVisibility(View.VISIBLE);
//                tvTime.setVisibility(View.VISIBLE);
//                tvDec.setVisibility(View.VISIBLE);
            }

            ivRight.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    if (jobItemList.getVisibility() == View.GONE) {
                        jobItemList.setVisibility(View.VISIBLE);
                        ivRight.setBackgroundDrawable(ct.getResources()
                                .getDrawable(R.drawable.upppp));
                    } else {
                        jobItemList.setVisibility(View.GONE);
                        ivRight.setBackgroundDrawable(ct.getResources()
                                .getDrawable(R.drawable.downnnn));
                    }
                }
            });

            if (Helper.getTheme(ct) == 0) {
                tv.setTextColor(ct.getResources().getColor(
                        android.R.color.white));
                datetv.setTextColor(ct.getResources().getColor(
                        android.R.color.white));
            }

            if (!survey.getSurveyName().equals("")) {

                Spanned sp = Html.fromHtml(survey.getSurveyName());
                tv.setText(sp.toString());
            } else {
                if (survey.getSurveyName() == null)
                    survey.setSurveyName("");
                Spanned sp = Html.fromHtml(survey.getSurveyName());
                tv.setText(sp.toString());
            }

            if (!survey.getSurveyCount().equals("")) {
                Spanned sp = Html.fromHtml(survey.getSurveyCount() + "/ "
                        + survey.getTargetQuota());
                datetv.setText(sp.toString());
            }

            // else {
            // Spanned sp = Html.fromHtml(getDate(order.getDate()));
            // datetv.setText(sp.toString());
            // }
            // if (order.getCount() > 1) {
            // String str = datetv.getText().toString() + "<B> ("
            // + order.getCount() + ")</B>";
            // Spanned sp = Html.fromHtml(str);
            // datetv.setText(sp, BufferType.SPANNABLE);
            // }
            // if (this.joblistarray.get(position).listOrders != null
            // && this.joblistarray.get(position).listOrders.size() > 1) {
            // iv.setBackgroundResource(R.drawable.folder);
            // //iv.setVisibility(RelativeLayout.GONE);
            // }
            // else
            // if (checkIfCorrect(order.getSetID()) == false) {
            // order.setStatusName("wrong");
            // iv.setBackgroundResource(getResource("wrong"));
            // } else {
            // iv.setBackgroundResource(getResource(order.getStatusName()));
            // }
            // hideShowRight(order.getStatusName(),ivRight);
        }
        // datetv.setTextColor(getTextViewColor(order.getStatusName()));

        if (survey == null && Constants.getDateFilter()) {
            TextView tv1 = (TextView) row.findViewById(R.id.tv1);
            TextView tv2 = (TextView) row.findViewById(R.id.tv2);
            TextView tvCount = (TextView) row.findViewById(R.id.tvcount);
            TextView tvBranch = (TextView) row.findViewById(R.id.tvbranch);

            tv2.setVisibility(RelativeLayout.INVISIBLE);
            tvCount.setVisibility(RelativeLayout.INVISIBLE);
            tvBranch.setVisibility(RelativeLayout.INVISIBLE);

            String newText1 = tv1.getText().toString();
            String newtv21 = tv2.getText().toString();
            String[] str = newtv21.split(",");
            if (str.length > 0)
                tv1.setText(str[0]);

        }
        return row;
    }

    private String setBranchIfNeeded(String str, boolean showisTrue, Order order) {
        if (showisTrue && order.getBranchLat() != null
                && !order.getBranchLat().equals("")) {
            str += " - " + order.getBranchLat();
        }
        return str;
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            try {
                View listItem = listAdapter.getView(i, null, listView);
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            } catch (Exception ex) {

            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public void setListViewHeightBased(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            try {
                View listItem = listAdapter.getView(i, null, listView);
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            } catch (Exception ex) {

            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = 150 + totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    private TextView getTextFromHtmlFormate(String html, TextView tv) {
        if (html == null)
            html = "";
        Spanned sp = Html.fromHtml(html);
        if (tv != null && sp != null)
            tv.setText(sp, BufferType.SPANNABLE);
        return tv;
    }


    //new code.....
//    public void start_job() {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                // mEditText.setText( "" );
//
//                if (aceeptbtn.getText().toString()
//                        .equals(getString(R.string.jd_begin_review_btn_text))) {
//                    BeginReview(true);
//
//                } else if (aceeptbtn
//                        .getText()
//                        .toString()
//                        .equals(getString(R.string.jd_continue_review_btn_text))) {
//
//                    BeginReview(true);
//                }
//            }
//        });
//    }
}
