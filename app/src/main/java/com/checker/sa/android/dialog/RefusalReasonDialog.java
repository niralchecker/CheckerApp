package com.checker.sa.android.dialog;

import com.checker.sa.android.helper.Constants;
import com.checker.sa.android.helper.Helper;
import com.mor.sa.android.activities.JobDetailActivity;
import com.mor.sa.android.activities.JobListActivity;
import com.mor.sa.android.activities.R;
import com.mor.sa.android.activities.ShowJobInMapViewActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;


public class RefusalReasonDialog extends Dialog implements android.view.View.OnClickListener {

    EditText et;
    Button OK_btn;
    //	JobDetailActivity jobdetail;
    JobListActivity jobdetail;

    ShowJobInMapViewActivity showJobInMapViewActivity;
    JobDetailActivity jobDetailScreen;

    public RefusalReasonDialog(Context context) {
        super(context);
        if (Constants.screen_type_dialog == 1) {
            jobdetail = (JobListActivity) context;
        } else if (Constants.screen_type_dialog == 2) {
            jobDetailScreen = (JobDetailActivity) context;
        } else {
            showJobInMapViewActivity = (ShowJobInMapViewActivity) context;
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Helper.getTheme(context) == 0) {
            setContentView(R.layout.reject_night);
        } else
            setContentView(R.layout.reject);
        et = (EditText) findViewById(R.id.edittext);
        OK_btn = (Button) findViewById(R.id.ok_btn);
        OK_btn.setOnClickListener(this);
//		OK_btn.setBackgroundColor(Color.parseColor("#007BFF"));
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (OK_btn.getId() == v.getId()) {
            if (et.getText().toString().equals("")) {
                if (Constants.screen_type_dialog == 1) {
                    ShowAlert(jobdetail, jobdetail.getString(R.string._alert_title),
                            jobdetail.getString(R.string.reject_job_resusal_text_alert),
                            jobdetail.getString(R.string.alert_btn_lbl_ok));
                } else if (Constants.screen_type_dialog == 2) {
                    ShowAlert(jobDetailScreen, jobdetail.getString(R.string._alert_title),
                            jobDetailScreen.getString(R.string.reject_job_resusal_text_alert),
                            jobdetail.getString(R.string.alert_btn_lbl_ok));
                } else {
                    ShowAlert(showJobInMapViewActivity, jobdetail.getString(R.string._alert_title),
                            jobdetail.getString(R.string.reject_job_resusal_text_alert),
                            jobdetail.getString(R.string.alert_btn_lbl_ok));
                }
            } else {
                if (Constants.screen_type_dialog == 1) {
                    jobdetail.rejectJob(et.getText().toString());
                } if (Constants.screen_type_dialog == 2) {
                    jobDetailScreen.rejectJob(et.getText().toString());
                } else {
                    showJobInMapViewActivity.rejectJob(et.getText().toString());
                }
                this.dismiss();
            }
        }
    }

    public void ShowAlert(Context context, String title, String message, String button_lbl) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(title);
        alert.setMessage(message);
        alert.setPositiveButton(button_lbl, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.show();
    }
}
