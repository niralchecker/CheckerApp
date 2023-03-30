package com.checker.sa.android.adapter;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.checker.sa.android.data.Objects;
import com.checker.sa.android.data.Order;
import com.checker.sa.android.data.QuestionnaireData;
import com.checker.sa.android.data.Set;
import com.checker.sa.android.data.Sets;
import com.checker.sa.android.data.Survey;
import com.checker.sa.android.data.Surveys;
import com.checker.sa.android.data.filePathDataID;
import com.checker.sa.android.data.parser.Parser;
import com.checker.sa.android.db.DBHelper;
import com.checker.sa.android.helper.Constants;
import com.checker.sa.android.helper.Helper;
import com.checker.sa.android.helper.UIHelper;
import com.mor.sa.android.activities.JobListActivity;
import com.mor.sa.android.activities.QuestionnaireActivity;
import com.mor.sa.android.activities.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;

public class jobInnerItemAdapter extends BaseAdapter {

    Context ct;
    Activity act;
    ArrayList<Order> joblistarray;
    Parser thisParser;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private RelativeLayout lastpopuplayout;

    public jobInnerItemAdapter(Context ct, ArrayList<Order> listarray,
                               Parser parser) {
        this.thisParser = parser;
        this.ct = ct;
        act = ((JobListActivity) ct);
        this.joblistarray = listarray;

    }

    public jobInnerItemAdapter(Activity ct, ArrayList<Order> listarray) {
        this.act = ct;
        this.ct = act;
        this.joblistarray = listarray;
    }

    @Override
    public int getCount() {
        return joblistarray.size();
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

    String getDate(String date) {
        Date d = null;
        try {
            d = sdf.parse(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        DateFormat dateFormat = android.text.format.DateFormat
                .getDateFormat(ct);
        String str = dateFormat.format(d);
        return str;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflator = act.getLayoutInflater();
        final Order order = joblistarray.get(position);
        View row = inflator.inflate(R.layout.new_item_assigned, parent, false);
        ImageView returnedReview = (ImageView) row.findViewById(R.id.vreturned);
        ImageView err = (ImageView) row.findViewById(R.id.imgerr);
        if (order != null && order.getOrderID() != null && !order.getOrderID().contains("-") && order.getIsJobInProgressOnServer() != null
                && order.getIsJobInProgressOnServer().contains("true")) {
            returnedReview.setVisibility(RelativeLayout.VISIBLE);
            returnedReview.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    JobItemAdapter.customAlert(ct,
                            act.getString(R.string.returned_review));

                }
            });
        }

        if (order != null && order.getOrderID() != null && !order.getOrderID().contains("-") && order.getIsJobDeleted() == true) {
            if (returnedReview != null) returnedReview.setVisibility(RelativeLayout.GONE);
            err.setVisibility(RelativeLayout.VISIBLE);
            err.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ct);
                    String string = act.getString(R.string.deleted_review);
                    Spanned spReviewName = Html.fromHtml(order.getClientName()
                            + ", " + order.getSetName());
                    string = string.replace("[QUESTIONNAIRE]",
                            spReviewName.toString());

                    Spanned spBranch = Html.fromHtml(order.getBranchFullname());
                    string = string.replace("[BRANCH]", spBranch.toString());

                    Spanned spDate = Html.fromHtml(order.getDate());
                    string = string.replace("[DATE]", spDate.toString());

                    JobItemAdapter.customAlert(ct, string);

                    // <string name="deleted_review">[QUESTIONNAIRE] Review for
                    // Branch [BRANCH], [DATE] is already submitted from another
                    // device, uploading it again will overwrite all information
                    // on server. please contact office to validate.</string>
                }
            });
        }

        TextView tv = (TextView) row.findViewById(R.id.tv_name);
        tv.setTextSize(UIHelper.getFontSize(ct, tv.getTextSize()));
        TextView datetv = (TextView) row.findViewById(R.id.tv_date);
        tv.setTextSize(UIHelper.getFontSize(ct, tv.getTextSize()));
        ImageView iv = (ImageView) row.findViewById(R.id.iv_location);
        iv.setVisibility(RelativeLayout.GONE);

        if (Helper.getTheme(ct) == 0) {
            tv.setTextColor(ct.getResources().getColor(android.R.color.white));
            datetv.setTextColor(ct.getResources().getColor(
                    android.R.color.white));
        }

        if (order.getOrderID().contains("-")) {
            Survey s = Surveys.getCurrentSurve(order.getOrderID().replace("-",
                    ""));
            if (s.getSurveyName() == null)
                s.setSurveyName("");

            Spanned sp = Html.fromHtml(s.getSurveyName());
            if (order.getOrderID().contains("_")) {
                tv.setText("Interview "
                        + order.getOrderID().substring(
                        order.getOrderID().indexOf("_") + 1));
                try {
                    int i = Integer.parseInt(order.getOrderID().substring(
                            order.getOrderID().indexOf("_") + 1));
                    i++;
                    String str = "Interview " + i;
                    str = setBranchIfNeeded(str, (order != null), order);

                    tv.setText(str);

                } catch (Exception ex) {

                }
            }
        } else if (order.getOrderID().contains("-")) {
            Survey s = Surveys.getCurrentSurve(order.getOrderID().replace("-",
                    ""));
            if (s.getSurveyName() == null)
                s.setSurveyName("");

            Spanned sp = Html.fromHtml(s.getSurveyName());
            tv.setText(sp.toString());
            if (order.getOrderID().contains("_")) {
                tv.setText("Interview "
                        + order.getOrderID().substring(
                        order.getOrderID().indexOf("_") + 1));
                try {
                    int i = Integer.parseInt(order.getOrderID().substring(
                            order.getOrderID().indexOf("_") + 1));
                    i++;
                    String str = "Interview " + i;
                    str = setBranchIfNeeded(str, (order != null), order);

                    tv.setText(str);

                } catch (Exception ex) {

                }
                // tv.setText(sp);
            }
        } else if (!order.getSetName().equals("")) {
            if (order.getClientName() == null)
                order.setClientName("");
            if (order.getSetName() == null)
                order.setSetName("");
            Spanned sp = Html.fromHtml(order.getClientName() + ", "
                    + order.getSetName());
            tv.setText(sp.toString());
        } else {
            if (order.getClientName() == null)
                order.setClientName("");
            Spanned sp = Html.fromHtml(order.getClientName());
            tv.setText(sp.toString());
        }
        if (order.getOrderID().contains("-")) {
            Survey s = Surveys.getCurrentSurve(order.getOrderID().replace("-",
                    ""));
            if (s.getTargetQuota() == null)
                s.setTargetQuota("");

            if (s.getListAllocations() != null
                    && s.getListAllocations().size() > 0) {
                Spanned sp = Html.fromHtml(s.getListAllocations().get(0)
                        .getSurveyCount()
                        + "/" + s.getListAllocations().get(0).getAllocation());
                datetv.setText(sp);
                datetv.setText("Started: " + order.getTimeStart());
            } else {
                Spanned sp = Html.fromHtml(s.getSurveyCount() + "/"
                        + s.getTargetQuota());
                datetv.setText("Started: " + order.getTimeStart());
                // datetv.setText(sp);
            }

        } else if (Constants.getFullBranchName()
                && order.getBranchFullname() != null
                && order.getBranchFullname().length() > 0) {
            Spanned sp = Html.fromHtml(getDate(order.getDate()) + ", "
                    + order.getBranchFullname() + " " + order.getAddress());
            datetv.setText(sp.toString());
        } else if (!order.getBranchName().equals("")) {
            Spanned sp = Html.fromHtml(getDate(order.getDate()) + ", "
                    + order.getBranchName());
            datetv.setText(sp.toString());
        } else {
            Spanned sp = Html.fromHtml(getDate(order.getDate()));
            datetv.setText(sp.toString());
        }
        // datetv.setText(order.getOrderID());
        iv.setBackgroundResource(getResource(order.getStatusName()));

        if (order.getOrderID().contains("-"))
            iv.setBackgroundResource(getResource("survey"));
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
        ImageView imgpopup = (ImageView) row.findViewById(R.id.ivbrief);
        final RelativeLayout popuplayout = (RelativeLayout) row
                .findViewById(R.id.popup_layout);
//        imgpopup.setVisibility(RelativeLayout.VISIBLE);
        if (order.getStatusName().toLowerCase().contains("rogress") ||
                order.getStatusName().toLowerCase().contains("completed")) {
            imgChangeDate.setVisibility(RelativeLayout.INVISIBLE);
            imgChangeBranch.setVisibility(RelativeLayout.INVISIBLE);
            imgCalendar.setVisibility(RelativeLayout.INVISIBLE);
        }
        {
            imgChangeDate.setVisibility(RelativeLayout.INVISIBLE);
            imgChangeBranch.setVisibility(RelativeLayout.INVISIBLE);
            imgCalendar.setVisibility(RelativeLayout.INVISIBLE);
            imgPreview.setVisibility(RelativeLayout.VISIBLE);
            imgPreview.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    String content = "";
                    Set set = JobItemAdapter.getSetsRecords(order.getSetID(), order.getOrderID());
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

//            imgpopup.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (popuplayout.getVisibility() == RelativeLayout.VISIBLE)
//                        popuplayout.setVisibility(RelativeLayout.GONE);
//                    else {
//                        if (lastpopuplayout != null)
//                            lastpopuplayout.setVisibility(RelativeLayout.GONE);
//                        popuplayout.setVisibility(RelativeLayout.VISIBLE);
//                        lastpopuplayout = popuplayout;
//                    }
//                }
//            });

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

        return row;

    }

    private String setBranchIfNeeded(String str, boolean showisTrue, Order order) {
        if (showisTrue && order.getBranchLat() != null
                && !order.getBranchLat().equals("")) {
            str += " - " + order.getBranchLat();
        } else if (order.getBranchPhone() != null
                && !order.getBranchPhone().equals("")) {
            str += " - " + order.getBranchPhone();
        }

        return str;
    }

}
