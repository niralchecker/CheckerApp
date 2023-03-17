package com.checker.sa.android.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.LightingColorFilter;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.checker.sa.android.helper.Helper;
import com.mor.sa.android.activities.R;

public class Revamped_Loading_Dialog extends Dialog implements
        android.view.View.OnClickListener {

    private static Revamped_Loading_Dialog dialog;
    private static int counter = 7;
    private final RelativeLayout progress_layout;
    private final ProgressBar progress_bar;
    private final ImageView imgvidloader;
    private final TextView txtimgvid;
    Activity act;
    ImageView img;
    TextView txt;
    private int total;
    private int doneValue;


    public static void show_dialog(Activity act, String string) {
        try {
            if (dialog == null || !dialog.isShowing()) {
                dialog = new Revamped_Loading_Dialog(act, string);
                dialog.getWindow()
                        .getDecorView()
                        .getBackground()
                        .setColorFilter(
                                new LightingColorFilter(0x1a262c, 0x1a262c));
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                lp.gravity = Gravity.CENTER;
                dialog.getWindow().setAttributes(lp);
                dialog.setCancelable(false);
                dialog.show();
                Revamped_Loading_Dialog.changeImage();
            }
        } catch (Exception e) {

        }
    }

    private String msg;

    private Runnable changeMessage = new Runnable() {
        @Override
        public void run() {
            try {
                dialog.setMessage(msg);
            } catch (Exception ex) {

            }
        }
    };

    public void changeMessage(final String newMEssage) {
        this.msg = newMEssage;
        act.runOnUiThread(changeMessage);
    }

    private Runnable changePercentage = new Runnable() {
        @Override
        public void run() {
            try {
                dialog.setPercentage(total, doneValue);
            } catch (Exception ex) {

            }
        }
    };

    private void setPercentage(int total, int doneValue) {
        if (progress_bar != null) {
            progress_bar.setIndeterminate(false);
            progress_bar.setMax(total);
            progress_bar.setProgress(doneValue);

        }
    }

    public void changePercentage(final int value, final int total) {
        this.total = total;
        this.doneValue = value;
        act.runOnUiThread(changePercentage);
    }

    protected void setMessage(String msg2) {

        if (msg2.contains(" IMG=")) {


            String str = msg2.substring(msg2.indexOf(" IMG="));
            txtimgvid.setText(str.replace("IMG=", ""));
            txt.setText(msg2.replace(str, ""));
            progress_layout.setVisibility(RelativeLayout.VISIBLE);
            imgvidloader.setImageResource(R.drawable.icons_img);

            progress_bar.getIndeterminateDrawable().setColorFilter(
                    0xFFFF8C00,
                    android.graphics.PorterDuff.Mode.MULTIPLY);
            progress_bar.setProgress(0);
            progress_bar.setMax(0);
        } else if (msg2.contains(" VID=")) {

            String str = msg2.substring(msg2.indexOf(" VID="));
            txtimgvid.setText(str.replace("VID=", ""));
            txt.setText(msg2.replace(str, ""));
            progress_layout.setVisibility(RelativeLayout.VISIBLE);

            imgvidloader.setImageResource(R.drawable.icons_vid);
            progress_bar.getIndeterminateDrawable().setColorFilter(
                    0xFFFF8C00,
                    android.graphics.PorterDuff.Mode.MULTIPLY);
            progress_bar.setProgress(0);
            progress_bar.setMax(0);
        } else {
            txt.setText(msg2);
            progress_layout.setVisibility(RelativeLayout.GONE);
            progress_bar.getIndeterminateDrawable().setColorFilter(
                    0xFFFF8C00,
                    android.graphics.PorterDuff.Mode.MULTIPLY);
            progress_bar.setProgress(0);
        }
    }

    private static void changeImage() {
        new android.os.Handler().postDelayed(new Runnable() {
            public void run() {
                if (dialog != null && dialog.isShowing()) {
                    if (counter > 7)
                        counter = 1;
//                    String mDrawableName = "loader" + counter;
                    String mDrawableName = "newloader" + counter;
                    if (counter == 7) {
                        String iconName = Helper.getLoaderIconName();
                        mDrawableName = iconName + counter;
                    }
                    int resID = dialog.act.getResources().getIdentifier(
                            mDrawableName, "drawable",
                            dialog.act.getPackageName());
                    dialog.img.setImageResource(resID);
                    counter++;
                    Revamped_Loading_Dialog.changeImage();
                }
            }
        }, 3000);
    }

    public static void hide_dialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.hide();
            dialog = null;
        }
    }

    public Revamped_Loading_Dialog(Context context, String string) {
        super(context);
        act = (Activity) context;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.custom_circular_loader);
        //ProgressBar img_vid_loader
        ((ProgressBar) findViewById(R.id.marker_progress))
                .getIndeterminateDrawable().setColorFilter(
                        Helper.getLoaderColor(),
                        android.graphics.PorterDuff.Mode.MULTIPLY);
        txt = (TextView) findViewById(R.id.txt);
        img = (ImageView) findViewById(R.id.imgloader);
        progress_layout = (RelativeLayout) findViewById(R.id.img_vid_loader);
        imgvidloader = (ImageView) findViewById(R.id.imgvidloader);
        progress_bar = (ProgressBar) findViewById(R.id.loader_progress);
        progress_bar.getIndeterminateDrawable().setColorFilter(
                0xFFFF8C00,
                android.graphics.PorterDuff.Mode.MULTIPLY);
        txtimgvid = (TextView) findViewById(R.id.txtimgvid);
        progress_layout.setVisibility(RelativeLayout.GONE);
        counter++;
        if (counter > 4)
            counter = 1;
//        String mDrawableName = "loader" + counter;
        String mDrawableName = "newloader" + counter;
        int resID = act.getResources().getIdentifier(mDrawableName, "drawable",
                act.getPackageName());
        img.setImageResource(resID);
        if (string == null)
            txt.setText(context
                    .getString(R.string.jd_please_alert_msg));
        else
            txt.setText(string);
    }

    @Override
    public void onClick(View v) {

    }

    public static Revamped_Loading_Dialog getDialog() {
        return dialog;
    }
}
