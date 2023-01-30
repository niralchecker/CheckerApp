package com.checker.sa.android.helper;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.lang.SerializationUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.checker.sa.android.data.filePathDataID;
import com.mor.sa.android.activities.CheckerApp;
import com.mor.sa.android.activities.QuestionnaireActivity;
import com.mor.sa.android.activities.R;
import com.mor.sa.android.activities.audioMediaRecorderService;

import static com.mor.sa.android.activities.CheckerApp.audioMediaRecorder_filename;

public class audioMediaRecorder {

    Context context;
    public boolean recording;
    ArrayList<Uri> uploadFileList = null;
    ArrayList<filePathDataID> uploadList = null;
    ArrayList<String> uploadFileListDataId = null;
    public String DataId;
    private QuestionnaireActivity activity;
    private boolean isPause = false;
    private filePathDataID tempFileVar;

    public boolean isLast = false;
    protected QuestionnaireActivity questact;

    public audioMediaRecorder(Context con, ArrayList<Uri> uploadFileList,
                              ArrayList<filePathDataID> uploadList,
                              ArrayList<String> uploadFileListDataId, String dataId,
                              filePathDataID tempvar, QuestionnaireActivity questionnaireActivity) {
        context = con;
        this.uploadFileList = uploadFileList;
        this.uploadFileListDataId = uploadFileListDataId;
        this.uploadList = uploadList;
        this.DataId = dataId;
        this.activity = questionnaireActivity;
        this.questact = questionnaireActivity;
        this.tempFileVar = tempvar;
        activity = null;
    }

    //MediaRecorder recorder = null;
    private ImageView btnRecord;
    private ImageView btnPause;
    private TextView txtTimer;
    private View mainLayout;

    public String convertTimer(int seconds) {
        if (seconds <= 0 || isTimerCanceled == true) {
            seconds = 0;
            return "00:00";
        } else {
            int millis = seconds * 1000;
            TimeZone tz = TimeZone.getTimeZone("UTC");
            SimpleDateFormat df = new SimpleDateFormat("mm:ss");
            df.setTimeZone(tz);
            String time = df.format(new Date(millis));
            return (time);
        }
    }

    private java.util.Timer recordTimer;

    String fileName;

    public void btnRecordClick(boolean islast) {

        this.isLast = islast;
        if (recording == false) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            mainLayout.setVisibility(RelativeLayout.VISIBLE);
                            // Yes button clicked
                            isShowRecorder = true;
                            isShowRecorder(true);

                            recordTimer = new java.util.Timer();
                            isTimerCanceled = false;
                            seconds = 0;
                            recordTimer.schedule(new java.util.TimerTask() {
                                @Override
                                public void run() {
                                    TimerMethod();
                                }

                            }, 0, 1000);

                            btnPause.setVisibility(RelativeLayout.VISIBLE);
                            fileName = CheckerApp.localFilesDir.getPath()//Environment.getExternalStorageDirectory().getPath()
                                    + "/DCIM/";
                            File Root = new File(fileName);
                            if (!Root.exists())
                                Root.mkdir();
                            Calendar cal = Calendar.getInstance();
                            fileName = fileName + "checker_" + DataId + "_"
                                    + (System.currentTimeMillis() / 1000) + "_"
                                    + (System.currentTimeMillis() / (1000 * 60))
                                    + ".m4a";

                            File file = new File(fileName);

                            try {
                                file.createNewFile();
                            } catch (Exception ex) {
                                Exception ee = ex;
                            }

                            //DataId = tempDataId;
                            recording = true;
                            isPause = false;
                            btnRecord.setImageDrawable(context.getResources()
                                    .getDrawable(R.drawable.stop));

                            filePathDataID fId = makeCopy(tempFileVar);
                            if (audioMediaRecorder.this.questact != null
                                    && audioMediaRecorder.this.questact.questionObject != null
                                    && audioMediaRecorder.this.questact.questionObject.getDataID() != null) {
                                fId.setDataID(audioMediaRecorder.this.questact.questionObject.getDataID(), isLast);
                            } else {
                                fId.setDataID(DataId, isLast);
                            }

                            fId.setFilePath(fileName);

                            uploadList.add(fId);

                            uploadFileList.add(Uri.fromFile(new File(fileName)));
                            if (audioMediaRecorder.this.questact != null
                                    && audioMediaRecorder.this.questact.questionObject != null
                                    && audioMediaRecorder.this.questact.questionObject.getDataID() != null) {
                                uploadFileListDataId.add(audioMediaRecorder.this.questact.questionObject.getDataID());
                            } else {
                                uploadFileListDataId.add(DataId);
                            }


                            if (audioMediaRecorder.this.questact != null)
                                audioMediaRecorder.this.questact.saveUploadFilestoDB("");

                            startService(fileName);
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            // No button clicked

                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(context.getResources().getString(
                    R.string._alert_title));
            builder.setMessage(
                    context.getResources().getString(R.string.start_recording))
                    .setPositiveButton(
                            context.getResources()
                                    .getString(
                                            R.string.questionnaire_exit_delete_alert_yes),
                            dialogClickListener)
                    .setNegativeButton(
                            context.getResources()
                                    .getString(
                                            R.string.questionnaire_exit_delete_alert_no),
                            dialogClickListener).show();

        } else {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            // Yes button clicked

                            stopRecording();

                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            // No button clicked

                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(context.getResources().getString(
                    R.string._alert_title));
            builder.setMessage(
                    context.getResources().getString(R.string.stop_recording))
                    .setPositiveButton(
                            context.getResources()
                                    .getString(
                                            R.string.questionnaire_exit_delete_alert_yes),
                            dialogClickListener)
                    .setNegativeButton(
                            context.getResources()
                                    .getString(
                                            R.string.questionnaire_exit_delete_alert_no),
                            dialogClickListener).show();
        }

    }

    public void recordAudio(final ImageView btnRecord,
                            final ImageView btnPause, final TextView txtTimer,
                            final View mainLayout) {
        this.mainLayout = mainLayout;
        this.btnRecord = btnRecord;
        this.btnPause = btnPause;
        this.txtTimer = txtTimer;
        final ProgressDialog mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setTitle("recording");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        this.btnPause.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                btnPause.setVisibility(RelativeLayout.GONE);
                btnRecord.setImageDrawable(context.getResources().getDrawable(
                        R.drawable.stop));
                pauseRecording();
            }
        });
        this.btnRecord.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                btnRecordClick(false);
            }
        });

        mProgressDialog.setButton("Stop recording",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        mProgressDialog.dismiss();
                        stopService();
                    }
                });

        mProgressDialog
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    public void onCancel(DialogInterface p1) {
                        stopService();
                    }
                });
        // recorder.start();
        // mProgressDialog.show();
    }

    int seconds = 0;
    private Runnable Timer_Tick = new Runnable() {
        public void run() {
            seconds++;
            String mmss = convertTimer(seconds);
            txtTimer.setText(mmss);
        }
    };
    private boolean isShowRecorder;
    private boolean isTimerCanceled;
    private String tempDataId;

    private void TimerMethod() {
        ((Activity) context).runOnUiThread(Timer_Tick);
    }

    public void pauseRecording() {
        txtTimer.setText(convertTimer(seconds));
        // isTimerCanceled = true;
        recordTimer.cancel();
        isPause = true;
        recording = false;
        btnPause.setVisibility(RelativeLayout.GONE);
        btnRecord.setImageDrawable(context.getResources().getDrawable(
                R.drawable.record));
        if (audioMediaRecorder.this.questact != null)
            audioMediaRecorder.this.questact.updateServerSideFiles();
        stopService();
    }

    public void stopRecording() {

        if (recording == false)
            return;
        seconds = -1;
        txtTimer.setText(convertTimer(0));
        isTimerCanceled = true;
        recordTimer.cancel();
        recording = false;
        btnPause.setVisibility(RelativeLayout.GONE);
        btnRecord.setImageDrawable(context.getResources().getDrawable(
                R.drawable.record));
        isShowRecorder = false;
        isShowRecorder(false);
        if (audioMediaRecorder.this.questact != null)
            audioMediaRecorder.this.questact.updateServerSideFiles();

        stopService();
    }

    private filePathDataID makeCopy(filePathDataID tempFileVar2) {
        byte[] serializedObj = SerializationUtils.serialize(tempFileVar2);

        return (filePathDataID) SerializationUtils.deserialize(serializedObj);
    }

    public void setDataID(String dataID2) {
//		if (isPause == false && recording == false)
//			this.DataId = dataID2;
//		tempDataId = dataID2;

    }

    public void isShowRecorder(boolean isShowRecorder) {
        this.isShowRecorder = isShowRecorder;
        if (recording == false && isPause == false) {
            if (isShowRecorder) {
                txtTimer.setText(convertTimer(0));
                btnPause.setVisibility(RelativeLayout.GONE);
                btnRecord.setVisibility(RelativeLayout.VISIBLE);
                btnRecord.setImageDrawable(context.getResources().getDrawable(
                        R.drawable.record));
                txtTimer.setVisibility(RelativeLayout.VISIBLE);
                mainLayout.setVisibility(RelativeLayout.VISIBLE);
            } else {
                mainLayout.setVisibility(RelativeLayout.GONE);
                btnPause.setVisibility(RelativeLayout.GONE);
                btnRecord.setVisibility(RelativeLayout.GONE);
                txtTimer.setVisibility(RelativeLayout.GONE);
            }
        }
    }

    public void startService(String fileName) {
        Intent in = new Intent(context, audioMediaRecorderService.class);
        //in.putExtra("filename",fileName);
        audioMediaRecorder_filename = fileName;
        context.startService(in);
    }

    public void stopService() {
        Intent in = new Intent(context, audioMediaRecorderService.class);
        context.stopService(in);
    }

    public void setUploadList(ArrayList<filePathDataID> uploadList2) {
        this.uploadList = uploadList2;
    }

    public void setUploadUriList(ArrayList<Uri> uploadList2) {
        this.uploadFileList = uploadList2;
    }
}
