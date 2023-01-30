package com.mor.sa.android.activities;

import android.app.AlarmManager;
import android.app.Application;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.checker.sa.android.data.BasicLog;
import com.checker.sa.android.data.FilterData;
import com.checker.sa.android.helper.Constants;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class CheckerApp extends Application {
    private static Intent qdata;
    public static FilterData globalFilterVar;
public static String audioMediaRecorder_filename="";
    public static String changeDisplayCondition(String ObjectDisplayCondition) {

        ObjectDisplayCondition = ObjectDisplayCondition.replace("&lt;", "<");
        ObjectDisplayCondition = ObjectDisplayCondition.replace("&gt;", ">");
        ObjectDisplayCondition = ObjectDisplayCondition.replace("&amp;", "&");
        ObjectDisplayCondition = ObjectDisplayCondition.replace("&", "&amp;");
        ObjectDisplayCondition = ObjectDisplayCondition.replace("<", "&lt;");
        ObjectDisplayCondition = ObjectDisplayCondition.replace(">", "&gt;");

        return ObjectDisplayCondition;

    }

    public static Intent getQuestionResult() {
        return qdata;
    }

    public static void clearQuestionResult() {
        qdata = null;
    }

    public static void setQuestionResult(Intent data) {
        qdata = data;

    }

    public void customAlert(Context context, String textString) {
        final Dialog dialog = new Dialog(CheckerApp.this);
        dialog.setContentView(R.layout.custom_red_alert);

        // set the custom dialog components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.textView1);
        text.setText(textString);

        Button dialogButton = (Button) dialog.findViewById(R.id.btnOk);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
    }

    @Override
    public void onCreate() {
        // The following line triggers the initialization of ACRA
        super.onCreate();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Thread.setDefaultUncaughtExceptionHandler(
                new Thread.UncaughtExceptionHandler() {
                    @Override
                    public void uncaughtException(Thread thread, Throwable e) {
                        handleUncaughtException(thread, e);
                    }
                });
        localFilesDir = getApplicationContext().getFilesDir();

        //ACRA.init(this);
    }
    public static File localFilesDir;

    private void handleUncaughtException(Thread thread, Throwable e) {
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("CheckerCrash", sw.toString());
            clipboard.setPrimaryClip(clip);
        } catch (Exception ex) {

        }


        System.exit(10);


        // The following shows what I'd like, though it won't work like this.

        // Add some code logic if needed based on your requirement
    }

    private String getCrashLogCat() throws IOException {
        String cmd = (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) ?
                "logcat -d -v time MyApp:v dalvikvm:v System.err:v *:s" :
                "logcat -d -v time";

        // get input stream
        Process process = Runtime.getRuntime().exec(cmd);
        InputStreamReader reader = new InputStreamReader(process.getInputStream());

        // write output stream
        String writer = "";
        String model = Build.MODEL;
        if (!model.startsWith(Build.MANUFACTURER))
            model = Build.MANUFACTURER + " " + model;

        // Make file name - file must be saved to external storage or it wont be readable by
        // the email app.
        PackageInfo info = null;
        PackageManager manager = this.getPackageManager();
        try {
            info = manager.getPackageInfo(this.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e2) {
        }
        writer += ("Android version: " + Build.VERSION.SDK_INT + "\n");
        writer += ("Device: " + model + "\n");
        writer += ("App version: " + (info == null ? "(null)" : info.versionCode) + "\n");

        char[] buffer = new char[10000];
        do {
            int n = reader.read(buffer, 0, buffer.length);
            if (n == -1)
                break;
            writer += (buffer.toString());
        } while (true);

        reader.close();
        return writer;
    }

}