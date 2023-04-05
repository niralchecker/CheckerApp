package com.checker.sa.android.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import com.mor.sa.android.activities.R;

public class ApplyJobDialog extends Dialog implements android.view.View.OnClickListener {

    Activity act;

    public ApplyJobDialog(Context context) {
        super(context);
        act = (Activity) context;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.apply_job_dialog);
    }

    @Override
    public void onClick(View v) {

    }
}
