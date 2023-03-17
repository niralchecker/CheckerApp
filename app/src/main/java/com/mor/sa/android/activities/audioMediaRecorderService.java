package com.mor.sa.android.activities;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaRecorder;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;

import com.checker.sa.android.data.BasicLog;
import com.checker.sa.android.helper.Constants;

import static com.mor.sa.android.activities.CheckerApp.audioMediaRecorder_filename;

public class audioMediaRecorderService extends Service {
    private MediaRecorder recorder;
    private SharedPreferences myPrefs;
    private String fileName;

    @Override
    public void onDestroy() {
        super.onDestroy();
        myPrefs = getSharedPreferences("pref", MODE_PRIVATE);
        SplashScreen.addAudioLog(new BasicLog(
                myPrefs.getString(Constants.SETTINGS_SYSTEM_URL_KEY, ""),
                myPrefs.getString(Constants.POST_FIELD_LOGIN_USERNAME, ""),
                "Audio stopped", fileName.replace(".", "__")));
        try{
            recorder.stop();
        }catch (Exception e){
            Log.e("Exception",e.toString());
        }
        recorder.release();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //fileName=intent.getExtras().getString("filename");
        fileName = audioMediaRecorder_filename;
        recorder = new MediaRecorder();
        ContentValues values = new ContentValues(3);
        values.put(MediaStore.MediaColumns.TITLE, fileName);
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        recorder.setOutputFile(fileName);
        try {
            recorder.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            recorder.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        myPrefs = getSharedPreferences("pref", MODE_PRIVATE);
        SplashScreen.addAudioLog(new BasicLog(
                myPrefs.getString(Constants.SETTINGS_SYSTEM_URL_KEY, ""),
                myPrefs.getString(Constants.POST_FIELD_LOGIN_USERNAME, ""),
                "Audio started", fileName.replace(".", "__")));
        return START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
