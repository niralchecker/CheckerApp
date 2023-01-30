package com.mor.sa.android.activities;

import android.Manifest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.checker.sa.android.data.BasicLog;
import com.checker.sa.android.db.DBAdapter;
import com.checker.sa.android.helper.Constants;
import com.checker.sa.android.helper.Devices;
import com.checker.sa.android.helper.Helper;
import com.checker.sa.android.helper.UIHelper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.maps.android.MapActivity;
import com.mor.sa.android.activities.R;

import org.apache.commons.io.FileUtils;

public class SplashScreen extends Activity {

    public static final int SPLASH_DISPLAY_TIME = 2000;
    TextView tv;
    private ProgressDialog progressd;

    public double megabytesAvailable() {
        return getFileSize((long) CheckerApp.localFilesDir//Environment.getExternalStorageDirectory()
                .getUsableSpace());
    }

    public double getFileSize(long size) {
        if (size <= 0)
            return 0.0;
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return size / Math.pow(1024, 2);
    }

    public void ShowAlert(Context context, String title, final String message,
                          String button_lbl) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setCancelable(false);
        alert.setTitle(title);
        TextView textView = new TextView(context);
        textView.setTextSize(UIHelper.getFontSize(SplashScreen.this,
                textView.getTextSize()));
        textView.setText(Helper.makeHtmlString(message));
        alert.setView(textView);
        alert.setPositiveButton(button_lbl,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
        alert.show();
    }

    public double getTotalRAM() {
        double mb = 0.0;

        RandomAccessFile reader = null;
        String load = null;
        DecimalFormat twoDecimalForm = new DecimalFormat("#.##");
        double totRam = 0;
        String lastValue = "";
        try {
            reader = new RandomAccessFile("/proc/meminfo", "r");
            load = reader.readLine();

            // Get the Number value from the string
            Pattern p = Pattern.compile("(\\d+)");
            Matcher m = p.matcher(load);
            String value = "";
            while (m.find()) {
                value = m.group(1);
                // System.out.println("Ram : " + value);
            }
            reader.close();

            totRam = Double.parseDouble(value);
            // totRam = totRam / 1024;

            mb = totRam / 1024.0;
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            // Streams.close(reader);
        }

        return mb;
    }

    public void customAlert(Context context, String textString) {
        final Dialog dialog = new Dialog(SplashScreen.this);
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

    private static void zipFolder(String inputFolderPath, String outZipPath) {
        try {
            FileOutputStream fos = new FileOutputStream(outZipPath);
            ZipOutputStream zos = new ZipOutputStream(fos);
            File srcFile = new File(inputFolderPath);
            File[] files = srcFile.listFiles();
            Log.d("", "Zip directory: " + srcFile.getName());
            for (int i = 0; i < files.length; i++) {
                Log.d("", "Adding file: " + files[i].getName());
                if (files[i].isDirectory()) continue;
                byte[] buffer = new byte[1024];
                FileInputStream fis = new FileInputStream(files[i]);
                zos.putNextEntry(new ZipEntry(files[i].getName()));
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, length);
                }
                zos.closeEntry();
                fis.close();
            }
            zos.close();
        } catch (IOException ioe) {
            Log.e("", ioe.getMessage());
        }
    }

    private String saveOfflineData(String path) {

        DBAdapter dba = new DBAdapter(this.getApplicationContext());
        String outPath = null;
        // db.deleteDB();
        try {

            SharedPreferences myPrefs = getSharedPreferences("pref",
                    MODE_PRIVATE);
            outPath = dba.createDataBase(
                    myPrefs.getString(Constants.SETTINGS_SYSTEM_URL_KEY, ""),
                    myPrefs.getString(Constants.POST_FIELD_LOGIN_USERNAME, ""),
                    path);

            File root = CheckerApp.localFilesDir;//android.os.Environment.getExternalStorageDirectory();
            outPath = root.getAbsolutePath() + "/checker_db.zip";
            zipFolder(path, outPath);
            Toast.makeText(SplashScreen.this, "Db exported to:" + outPath,
                    Toast.LENGTH_LONG).show();
            //customAlert(SplashScreen.this, "Db copied here:" + outPath);

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(SplashScreen.this, "Issue copying",
                    Toast.LENGTH_LONG).show();
        }


        return outPath;
    }

    //@Override
    protected void onCrreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);

        String path = CheckerApp.localFilesDir//Environment.getExternalStorageDirectory()
                + "";

        path = saveOfflineData(path);


    }

    private void sendDeviceInfo() {
        // try {
        // ParseObject gameScore = new ParseObject("DevicesInfo");
        // String deviceid = "";
        // try {
        // deviceid = Secure.getString(
        // SplashScreen.this.getContentResolver(),
        // Secure.ANDROID_ID);
        //
        // } catch (Exception ex) {
        //
        // }
        // gameScore.put("DeviceId", deviceid);
        // gameScore.put("AndroidVersion", Build.VERSION.SDK_INT);
        // gameScore.put("DeviceCompany", Build.MANUFACTURER);
        // gameScore.put("DeviceModel", Build.MODEL);
        // PackageInfo pInfo;
        // int verCode = 0;
        // try {
        // pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        // verCode = pInfo.versionCode;
        // } catch (NameNotFoundException e1) {
        // // TODO Auto-generated catch block
        // e1.printStackTrace();
        // }
        //
        // gameScore.put("app_version_code", verCode);
        // gameScore.saveInBackground(new SaveCallback() {
        //
        // @Override
        // public void done(com.parse.ParseException e) {
        //
        // }
        // });
        // } catch (Exception ex) {
        //
        // }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference().child("installations").push();
        reference.setValue(new String("App started!"));

    }

    private String getUserDetails() {
        myPrefs = getSharedPreferences("pref", MODE_PRIVATE);
        String userid = myPrefs.getString(Constants.POST_FIELD_LOGIN_USERNAME,
                "");
        String url = myPrefs.getString(Constants.SETTINGS_SYSTEM_URL_KEY, "");
        return userid + "@" + url;
    }


    private String getPhoneDetails() {
        return Devices.getDeviceName();
    }

    private String getOrderId() {

        myPrefs = getSharedPreferences("pref", MODE_PRIVATE);
        String data = myPrefs.getString(Constants.Crash_Last_ORDERID, "");
        return data;
    }

    private String getSetId() {

        myPrefs = getSharedPreferences("pref", MODE_PRIVATE);
        String data = myPrefs.getString(Constants.Crash_Last_SETID, "");
        return data;
    }

    public static void sendFireBaseCrash(final org.acra.util.CrashData crash) {
        String time = "";
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy MM/dd_HH:mm:ss");
            df.setTimeZone(TimeZone.getTimeZone("gmt"));
            time = df.format(new Date());
            String month = String.format("MMM", new Date());
            df = new SimpleDateFormat("MM");
            month = df.format(new Date());
            String year = String.format("yyyy", new Date());
            df = new SimpleDateFormat("yyyy");
            year = df.format(new Date());
            //crash.setTime(time);

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference reference = database.getReference().child("crashtwelveEU").child("month_" + month).child(crash.getSystem_url()).push();
            reference.setValue(crash);
        } catch (Exception ec) {
            int i = 0;
            i++;
        }
    }


    SharedPreferences myPrefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        myPrefs = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = myPrefs.edit();
        SplashScreen.sendCrashReport(myPrefs, SplashScreen.this);

        SimpleDateFormat timeformat = new SimpleDateFormat("kk:mm:ss",
                Locale.ENGLISH);
        if (getPackageName() != null
                && getPackageName().contains(Helper.CONSTPACKAGEPREFIX)) {
            setContentView(Helper.splash_layout);
            // SharedPreferences.Editor prefsEditor = myPrefs.edit();
            prefsEditor.putString(Constants.SETTINGS_SYSTEM_URL_KEY,
                    Helper.systemUrl);
            prefsEditor.commit();
            if (Helper.DEFAULT_LANGUAGE > 0) {
                prefsEditor.putInt(Constants.SETTINGS_LANGUAGE_INDEX,
                        Helper.DEFAULT_LANGUAGE);
                prefsEditor.commit();
            }
        } else
            setContentView(R.layout.splashscreen);
        Helper.setConfigChange(false);
        Helper.setDeviceCamera(myPrefs.getBoolean(Constants.SETTINGS_ENABLE_DEFAULT_CAMERA, false));
        Helper.setSampleSize(myPrefs.getInt(Constants.SETTINGS_RESIZE_INDEX, 0));
        // Toast.makeText(SplashScreen.this, "Splas screen",
        // Toast.LENGTH_LONG).show();
        //
        double thisRam = getTotalRAM();
        double thisMemCard = megabytesAvailable();
        if (thisRam < 250.0) {
            ShowAlert(SplashScreen.this,
                    getResources().getString(R.string.memory_info),
                    getResources().getString(R.string.not_enough_space_ram),
                    "Ok");
        } else if (thisMemCard < 10.0) {
            ShowAlert(
                    SplashScreen.this,
                    getResources().getString(R.string.memory_info),
                    getResources().getString(
                            R.string.not_enough_space_internal_memory), "Ok");
        } else {
            sendDeviceInfo();

//			File root = android.os.Environment.getExternalStorageDirectory();
//			String path = root.getAbsolutePath() + "/mnt/sdcard/Checker_binaries/";
//			path = saveOfflineData(path);
            if (checkforPermissions()) {
                boolean allChecktoMoveFiles = ChecktoMoveFiles();
                if (allChecktoMoveFiles) {
                    progressd = new ProgressDialog(SplashScreen.this);
                    progressd.setMessage("Moving external memory files to internal...");
                    progressd.show();
                    ChecktoMoveDbfilestoInternal();
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Toast.makeText(SplashScreen.this, "opening Login screen",
                            // Toast.LENGTH_LONG).show();
                            Constants.setLocale(SplashScreen.this);
                            Intent main = new Intent(SplashScreen.this
                                    .getApplicationContext(), NewLoginActivity.class);
                            SplashScreen.this.startActivity(main);
                            SplashScreen.this.finish();

                            // overridePendingTransition(android.R.anim.fade_in,
                            // android.R.anim.fade_out);
                        }
                    }, SPLASH_DISPLAY_TIME);
                }
            } else {
                grantPermissions(-1);
            }
            // tv = (TextView) findViewById(R.id.darktview);
            // if (Helper.getTheme(SplashScreen.this) == 1)
            // tv.setVisibility(View.VISIBLE);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (checkforPermissions()) {
            boolean allChecktoMoveFiles = ChecktoMoveFiles();
            if (allChecktoMoveFiles) {
                progressd = new ProgressDialog(SplashScreen.this);
                progressd.setMessage("Moving external memory files to internal...");
                progressd.show();
                ChecktoMoveDbfilestoInternal();
            } else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Toast.makeText(SplashScreen.this, "opening Login screen",
                        // Toast.LENGTH_LONG).show();
                        Constants.setLocale(SplashScreen.this);
                        Intent main = new Intent(SplashScreen.this
                                .getApplicationContext(), NewLoginActivity.class);
                        SplashScreen.this.startActivity(main);
                        SplashScreen.this.finish();

                        // overridePendingTransition(android.R.anim.fade_in,
                        // android.R.anim.fade_out);
                    }
                }, SPLASH_DISPLAY_TIME);
            }
        }
    }

    private boolean checkforPermissions() {

        //int haswriteexternalstorage = this.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int hasreadexternalstorage = this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
        int hasrecordaudio = this.checkSelfPermission(Manifest.permission.RECORD_AUDIO);
        int hasaccessc = this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
        // int hasaccfinelocation = this.checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION);
        //int hasaccesslocationextracommand = this.checkSelfPermission(android.Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS);
        int hasaccessnetworkstate = this.checkSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE);
        int hasintermission = this.checkSelfPermission(Manifest.permission.INTERNET);
        int hascamera = this.checkSelfPermission(Manifest.permission.CAMERA);
        // int haswriteexternalstorage = this.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int haswakelock = this.checkSelfPermission(Manifest.permission.WAKE_LOCK);


        //if (haswriteexternalstorage != PackageManager.PERMISSION_GRANTED ||
        if (hasreadexternalstorage != PackageManager.PERMISSION_GRANTED)
//                    || hasrecordaudio != PackageManager.PERMISSION_GRANTED ||
//                    hasaccessc != PackageManager.PERMISSION_GRANTED || hasaccessnetworkstate != PackageManager.PERMISSION_GRANTED ||
//                    hasintermission != PackageManager.PERMISSION_GRANTED || hascamera != PackageManager.PERMISSION_GRANTED ||
//                    haswakelock != PackageManager.PERMISSION_GRANTED || hasreadexternalstorage != PackageManager.PERMISSION_GRANTED)
        {
            return false;

        }


        return true;
    }

    private boolean grantPermissions(int lastpermission) {
        boolean allgranted = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            int hasreadexternalstorage = this.checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE);
            int hasrecordaudio = this.checkSelfPermission(android.Manifest.permission.RECORD_AUDIO);
            int hasaccessc = this.checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION);

            int hascamera = this.checkSelfPermission(android.Manifest.permission.CAMERA);


            List<String> permissions = new ArrayList<String>();

            if (hasaccessc != PackageManager.PERMISSION_GRANTED && lastpermission < 1) {
                permissions.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
                requestPermissions(permissions.toArray(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}), 1);
                allgranted = false;
            } else if (hascamera != PackageManager.PERMISSION_GRANTED && lastpermission < 2) {
                permissions.add(android.Manifest.permission.CAMERA);
                requestPermissions(permissions.toArray(new String[]{Manifest.permission.CAMERA}), 2);
                allgranted = false;
            } else if (hasrecordaudio != PackageManager.PERMISSION_GRANTED && lastpermission < 3) {
                permissions.add(android.Manifest.permission.RECORD_AUDIO);
                requestPermissions(permissions.toArray(new String[]{Manifest.permission.RECORD_AUDIO}), 3);
                allgranted = false;
            } else if (hasreadexternalstorage != PackageManager.PERMISSION_GRANTED && lastpermission < 4) {
                permissions.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
                requestPermissions(permissions.toArray(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}), 4);
                allgranted = false;
            }


        }
        return allgranted;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1 || requestCode == 2 || requestCode == 3 || requestCode == 4) {
            if (grantResults.length > 0) {
                boolean anyfalsepermission = grantPermissions(requestCode);
                if (requestCode == 4) {
                    if (grantResults.length > 0) {
                        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                            //If user presses allow
                            boolean allChecktoMoveFiles = ChecktoMoveFiles();
                            //if (allChecktoMoveFiles)
                            {
                                try {
                                    progressd = new ProgressDialog(SplashScreen.this);
                                    progressd.setMessage("Moving external memory files to internal...");
                                    progressd.show();
                                    ChecktoMoveDbfilestoInternal();
                                } catch (Exception e) {
                                }
                            }
                        } else {
                            // grantPermissions(3);
                            Alertdialogtoopensettings();
                        }
                    }
                }
            }
        }

    }

    public void Alertdialogtoopensettings() {
        new AlertDialog.Builder(SplashScreen.this)
                .setTitle("You denied a mandatory permission")
                .setMessage("READ_EXTERNAL_STORAGE. Please provide necessary access to continue using the app.")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", getPackageName(), null)));
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton("Cancel", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private boolean Checkfornotgrantedpermission(int[] grantResults) {
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }


    private boolean ChecktoMoveFiles() {
//		Checker_Crash
//				checker_signatures
//		CheckerVideos
//		checker_db.zip

        File externalRoot1 = new File(android.os.Environment.getExternalStorageDirectory().getPath() + "/mnt/sdcard/Checker/");
        File externalRoot2 = new File(android.os.Environment.getExternalStorageDirectory().getPath() + "/mnt/sdcard/Checker_binaries/");
        File externalRoot3 = new File(android.os.Environment.getExternalStorageDirectory().getPath() + "/mnt/sdcard/Checker_htmls/");
        File externalRoot4 = new File(android.os.Environment.getExternalStorageDirectory().getPath() + "/mnt/sdcard/Checker_archive_binaries/");
        File externalRoot5 = new File(android.os.Environment.getExternalStorageDirectory().getPath() + "/mnt/sdcard/CheckerFiles/");

        File externalRoot6 = new File(android.os.Environment.getExternalStorageDirectory().getPath() + "/mnt/sdcard/Checker_Crash/");
        File externalRoot7 = new File(android.os.Environment.getExternalStorageDirectory().getPath() + "/mnt/sdcard/checker_signatures/");
        File externalRoot8 = new File(android.os.Environment.getExternalStorageDirectory().getPath() + "/mnt/sdcard/CheckerVideos/");
        File externalRoot9 = new File(android.os.Environment.getExternalStorageDirectory().getPath() + "/mnt/sdcard/checker_db.zip/");
        //  File externalRoot10 = new File(android.os.Environment.getExternalStorageDirectory().getPath() + "/checkerimgss/");

        File internalpath1 = new File(CheckerApp.localFilesDir.getAbsolutePath() + "/mnt/sdcard/Checker");
        File internalpath2 = new File(CheckerApp.localFilesDir.getAbsolutePath() + "/mnt/sdcard/Checker_binaries/");
        File internalpath3 = new File(CheckerApp.localFilesDir.getAbsolutePath() + "/mnt/sdcard/Checker_htmls/");
        File internalpath4 = new File(CheckerApp.localFilesDir.getAbsolutePath() + "/mnt/sdcard/Checker_archive_binaries/");
        File internalpath5 = new File(CheckerApp.localFilesDir.getAbsolutePath() + "/mnt/sdcard/CheckerFiles/");

        File internalpath6 = new File(CheckerApp.localFilesDir.getAbsolutePath() + "/mnt/sdcard/Checker_Crash/");
        File internalpath7 = new File(CheckerApp.localFilesDir.getAbsolutePath() + "/mnt/sdcard/checker_signatures/");
        File internalpath8 = new File(CheckerApp.localFilesDir.getAbsolutePath() + "/mnt/sdcard/CheckerVideos/");
        File internalpath9 = new File(CheckerApp.localFilesDir.getAbsolutePath() + "/mnt/sdcard/checker_db.zip/");
        //File internalpath10 = new File(CheckerApp.localFilesDir.getAbsolutePath() + "/checkerimgss/");

        if ((externalRoot1.exists() && !internalpath1.exists()) || (externalRoot2.exists() && !internalpath2.exists()) || (externalRoot3.exists() && !internalpath3.exists())
                || (externalRoot4.exists() && !internalpath4.exists()) || (externalRoot5.exists() && !internalpath5.exists()) || (externalRoot6.exists() && !internalpath6.exists())
                || (externalRoot7.exists() && !internalpath7.exists()) || (externalRoot8.exists() && !internalpath8.exists()) || (externalRoot9.exists() && !internalpath9.exists()))
            // || (externalRoot10.exists()  && !internalpath10.exists()))
            return true;
        return false;
    }

    private void ChecktoMoveDbfilestoInternal() {

        File externalRoot1 = new File(android.os.Environment.getExternalStorageDirectory().getPath() + "/mnt/sdcard/Checker/");
        File externalRoot2 = new File(android.os.Environment.getExternalStorageDirectory().getPath() + "/mnt/sdcard/Checker_binaries/");
        File externalRoot3 = new File(android.os.Environment.getExternalStorageDirectory().getPath() + "/mnt/sdcard/Checker_htmls/");
        File externalRoot4 = new File(android.os.Environment.getExternalStorageDirectory().getPath() + "/mnt/sdcard/Checker_archive_binaries/");
        File externalRoot5 = new File(android.os.Environment.getExternalStorageDirectory().getPath() + "/mnt/sdcard/CheckerFiles/");

        File externalRoot6 = new File(android.os.Environment.getExternalStorageDirectory().getPath() + "/mnt/sdcard/Checker_Crash/");
        File externalRoot7 = new File(android.os.Environment.getExternalStorageDirectory().getPath() + "/mnt/sdcard/checker_signatures/");
        File externalRoot8 = new File(android.os.Environment.getExternalStorageDirectory().getPath() + "/mnt/sdcard/CheckerVideos/");
        File externalRoot9 = new File(android.os.Environment.getExternalStorageDirectory().getPath() + "/mnt/sdcard/checker_db.zip/");
        // File externalRoot10 = new File(android.os.Environment.getExternalStorageDirectory().getPath() + "/checkerimgss/");

        File internalpath1 = new File(CheckerApp.localFilesDir.getAbsolutePath() + "/mnt/sdcard/Checker");
        File internalpath2 = new File(CheckerApp.localFilesDir.getAbsolutePath() + "/mnt/sdcard/Checker_binaries/");
        File internalpath3 = new File(CheckerApp.localFilesDir.getAbsolutePath() + "/mnt/sdcard/Checker_htmls/");
        File internalpath4 = new File(CheckerApp.localFilesDir.getAbsolutePath() + "/mnt/sdcard/Checker_archive_binaries/");
        File internalpath5 = new File(CheckerApp.localFilesDir.getAbsolutePath() + "/mnt/sdcard/CheckerFiles/");

        File internalpath6 = new File(CheckerApp.localFilesDir.getAbsolutePath() + "/mnt/sdcard/Checker_Crash/");
        File internalpath7 = new File(CheckerApp.localFilesDir.getAbsolutePath() + "/mnt/sdcard/checker_signatures/");
        File internalpath8 = new File(CheckerApp.localFilesDir.getAbsolutePath() + "/mnt/sdcard/CheckerVideos/");
        File internalpath9 = new File(CheckerApp.localFilesDir.getAbsolutePath() + "/mnt/sdcard/checker_db.zip/");
        //  File internalpath10 = new File(CheckerApp.localFilesDir.getAbsolutePath() + "/checkerimgss/");

        if ((externalRoot1.exists() && !internalpath1.exists()) || (externalRoot2.exists() && !internalpath2.exists()) || (externalRoot3.exists() && !internalpath3.exists())
                || (externalRoot4.exists() && !internalpath4.exists()) || (externalRoot5.exists() && !internalpath5.exists()) || (externalRoot6.exists() && !internalpath6.exists())
                || (externalRoot7.exists() && !internalpath7.exists()) || (externalRoot8.exists() && !internalpath8.exists()) ||
                (externalRoot9.exists() && !internalpath9.exists())) { //|| (externalRoot10.exists() && !internalpath10.exists())

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    MovefilesToInternalstorage();
                }
            }, 500);
        }


    }

    public void MovefilesToInternalstorage() {
        class CopyFiletask extends AsyncTask<String, Void, String> {


            public CopyFiletask() {
            }

            public void executeIt() {


            }

            @Override
            protected String doInBackground(String... params) {
                String exception = "";
                try {
                    File externalRoot1 = new File(android.os.Environment.getExternalStorageDirectory().getPath() + "/mnt/sdcard/Checker");
                    File internalpath1 = new File(CheckerApp.localFilesDir.getAbsolutePath() + "/mnt/sdcard/Checker");
                    if (internalpath1.exists()) {
                        internalpath1.mkdir();
                    }
                    if (externalRoot1.exists() && !internalpath1.exists())
                        FileUtils.copyDirectory(externalRoot1, internalpath1);
                } catch (Exception ee) {
                    exception = exception + " Checker -- " + ee.getMessage();
                    int i = 0;
                    i++;
                }
                try {
                    File externalRoot2 = new File(android.os.Environment.getExternalStorageDirectory().getPath() + "/mnt/sdcard/Checker_binaries/");
                    File internalpath2 = new File(CheckerApp.localFilesDir.getAbsolutePath() + "/mnt/sdcard/Checker_binaries/");
                    if (externalRoot2.exists() && !internalpath2.exists()) {
                        FileUtils.copyDirectory(externalRoot2, internalpath2);
                    }
                } catch (Exception ee) {
                    exception = exception + " Checker_binaries -- " + ee.getMessage();
                    int i = 0;
                    i++;
                }
                try {
                    File externalRoot3 = new File(android.os.Environment.getExternalStorageDirectory().getPath() + "/mnt/sdcard/Checker_htmls/");
                    File internalpath3 = new File(CheckerApp.localFilesDir.getAbsolutePath() + "/mnt/sdcard/Checker_htmls/");
                    if (externalRoot3.exists() && !internalpath3.exists()) {
                        FileUtils.copyDirectory(externalRoot3, internalpath3);
                    }
                } catch (Exception ee) {
                    exception = exception + " Checker_htmls -- " + ee.getMessage();
                    int i = 0;
                    i++;
                }
                try {
                    File externalRoot4 = new File(android.os.Environment.getExternalStorageDirectory().getPath() + "/mnt/sdcard/Checker_archive_binaries/");
                    File internalpath4 = new File(CheckerApp.localFilesDir.getAbsolutePath() + "/mnt/sdcard/Checker_archive_binaries/");
                    if (externalRoot4.exists() && !internalpath4.exists()) {
                        FileUtils.copyDirectory(externalRoot4, internalpath4);
                    }
                } catch (Exception ee) {
                    exception = exception + " Checker_archive_binaries -- " + ee.getMessage();
                    int i = 0;
                    i++;
                }
                try {
                    File externalRoot5 = new File(android.os.Environment.getExternalStorageDirectory().getPath() + "/mnt/sdcard/CheckerFiles/");
                    File internalpath5 = new File(CheckerApp.localFilesDir.getAbsolutePath() + "/mnt/sdcard/CheckerFiles/");
                    if (externalRoot5.exists() && !internalpath5.exists()) {
                        FileUtils.copyDirectory(externalRoot5, internalpath5);
                    }
                } catch (Exception ee) {
                    exception = exception + " CheckerFiles -- " + ee.getMessage();

                    int i = 0;
                    i++;
                }
                try {
                    File externalRoot6 = new File(android.os.Environment.getExternalStorageDirectory().getPath() + "/mnt/sdcard/Checker_Crash/");
                    File internalpath6 = new File(CheckerApp.localFilesDir.getAbsolutePath() + "/mnt/sdcard/Checker_Crash/");
                    if (externalRoot6.exists() && !internalpath6.exists()) {
                        FileUtils.copyDirectory(externalRoot6, internalpath6);
                    }
                } catch (Exception ee) {
                    exception = exception + " Checker_Crash -- " + ee.getMessage();
                    int i = 0;
                    i++;
                }
                try {
                    File externalRoot7 = new File(android.os.Environment.getExternalStorageDirectory().getPath() + "/mnt/sdcard/checker_signatures/");
                    File internalpath7 = new File(CheckerApp.localFilesDir.getAbsolutePath() + "/mnt/sdcard/checker_signatures/");
                    if (externalRoot7.exists() && !internalpath7.exists()) {
                        FileUtils.copyDirectory(externalRoot7, internalpath7);
                    }
                } catch (Exception ee) {
                    exception = exception + " checker_signatures -- " + ee.getMessage();
                    int i = 0;
                    i++;
                }
                try {
                    File externalRoot8 = new File(android.os.Environment.getExternalStorageDirectory().getPath() + "/mnt/sdcard/CheckerVideos/");
                    File internalpath8 = new File(CheckerApp.localFilesDir.getAbsolutePath() + "/mnt/sdcard/CheckerVideos/");
                    if (externalRoot8.exists() && !internalpath8.exists()) {
                        FileUtils.copyDirectory(externalRoot8, internalpath8);
                    }
                } catch (Exception ee) {
                    exception = exception + " CheckerVideos -- " + ee.getMessage();
                    int i = 0;
                    i++;
                }
                try {
                    File externalRoot9 = new File(android.os.Environment.getExternalStorageDirectory().getPath() + "/mnt/sdcard/checker_db.zip/");
                    File internalpath9 = new File(CheckerApp.localFilesDir.getAbsolutePath() + "/mnt/sdcard/checker_db.zip/");
                    if (externalRoot9.exists() && !internalpath9.exists()) {
                        FileUtils.copyDirectory(externalRoot9, internalpath9);
                    }
                } catch (Exception ee) {
                    exception = exception + " checker_db.zip -- " + ee.getMessage();
                    int i = 0;
                    i++;
                }
//                try {
//                    File externalRoot10 = new File(android.os.Environment.getExternalStorageDirectory().getPath() + "/checkerimgss/");
//                    File internalpath10 = new File(CheckerApp.localFilesDir.getAbsolutePath() + "/checkerimgss/");
//                    if (externalRoot10.exists() && !internalpath10.exists()) {
//                        FileUtils.copyDirectory(externalRoot10, internalpath10);
//                    }
//                } catch (Exception ee) {
//                    exception = exception + " checkerimgss -- " + ee.getMessage();
//                    int i = 0;
//                    i++;
//                }
                return exception;
            }

            public void postExecute() {
                /////////

//				File internalpath = new File(CheckerApp.localFilesDir.getAbsolutePath() + "/mnt/sdcard/Checker_binaries/");
//				if (internalpath.exists()) {
//					int i = 0;
//					i++;
//				}
//				File externalRoot = new File(android.os.Environment.getExternalStorageDirectory().getPath() + "/mnt/sdcard/Checker_binaries/");
//				if (externalRoot.exists()) {
//					int i = 0;
//					i++;
//				}
//				int I_file_size = Integer.parseInt(String.valueOf(internalpath.length() / 1024));
//				int E_file_size = Integer.parseInt(String.valueOf(externalRoot.length() / 1024));
//				if (E_file_size != 0 && I_file_size == E_file_size) {
//					externalRoot.delete();
//				}
//				//////1
//				File internalpath1 = new File(CheckerApp.localFilesDir.getAbsolutePath() + "/mnt/sdcard/Checker_htmls/");
//				if (internalpath1.exists()) {
//					int i = 0;
//					i++;
//				}
//				File externalRoot1 = new File(android.os.Environment.getExternalStorageDirectory().getPath() + "/mnt/sdcard/Checker_htmls/");
//				if (externalRoot1.exists()) {
//					int i = 0;
//					i++;
//				}
//				int I_file_size1 = Integer.parseInt(String.valueOf(internalpath1.length() / 1024));
//				int E_file_size1 = Integer.parseInt(String.valueOf(externalRoot1.length() / 1024));
//				if (E_file_size1 != 0 && I_file_size1 == E_file_size1) {
//					externalRoot1.delete();
//				}
//				/////////2
//				File internalpath2 = new File(CheckerApp.localFilesDir.getAbsolutePath() + "/mnt/sdcard/Checker/");
//				if (internalpath2.exists()) {
//					int i = 0;
//					i++;
//				}
//				File externalRoot2 = new File(android.os.Environment.getExternalStorageDirectory().getPath() + "/mnt/sdcard/Checker/");
//				if (externalRoot2.exists()) {
//					int i = 0;
//					i++;
//				}
//				int I_file_size2 = Integer.parseInt(String.valueOf(internalpath2.length() / 1024));
//				int E_file_size2 = Integer.parseInt(String.valueOf(externalRoot2.length() / 1024));
//				if (E_file_size2 != 0 && I_file_size2 == E_file_size2) {
//					externalRoot2.delete();
//				}
//				///////3
//				File internalpath3 = new File(CheckerApp.localFilesDir.getAbsolutePath() + "/mnt/sdcard/Checker_archive_binaries/");
//				if (internalpath3.exists()) {
//					int i = 0;
//					i++;
//				}
//				File externalRoot3 = new File(android.os.Environment.getExternalStorageDirectory().getPath() + "/mnt/sdcard/Checker_archive_binaries/");
//				if (externalRoot3.exists()) {
//					int i = 0;
//					i++;
//				}
//				int I_file_size3 = Integer.parseInt(String.valueOf(internalpath3.length() / 1024));
//				int E_file_size3 = Integer.parseInt(String.valueOf(externalRoot3.length() / 1024));
//				if (E_file_size3 != 0 && I_file_size3 == E_file_size3) {
//					externalRoot3.delete();
//				}
//				//////4 CheckerFiles
//				File internalpath4 = new File(CheckerApp.localFilesDir.getAbsolutePath() + "/mnt/sdcard/CheckerFiles/");
//				if (internalpath4.exists()) {
//					int i = 0;
//					i++;
//				}
//				File externalRoot4 = new File(android.os.Environment.getExternalStorageDirectory().getPath() + "/mnt/sdcard/CheckerFiles/");
//				if (externalRoot4.exists()) {
//					int i = 0;
//					i++;
//				}
//				int I_file_size4 = Integer.parseInt(String.valueOf(internalpath4.length() / 1024));
//				int E_file_size4 = Integer.parseInt(String.valueOf(externalRoot4.length() / 1024));
//				if (E_file_size4 != 0 && I_file_size4 == E_file_size4) {
//					externalRoot4.delete();
//				}
            }

            @Override
            protected void onPostExecute(String result) {
                //Toast.makeText(SplashScreen.this, " " + result, Toast.LENGTH_SHORT).show();
                if (progressd != null && progressd.isShowing())
                    progressd.dismiss();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Toast.makeText(SplashScreen.this, "opening Login screen",
                        // Toast.LENGTH_LONG).show();
                        Constants.setLocale(SplashScreen.this);
                        Intent main = new Intent(SplashScreen.this
                                .getApplicationContext(), NewLoginActivity.class);
                        SplashScreen.this.startActivity(main);
                        SplashScreen.this.finish();

                        // overridePendingTransition(android.R.anim.fade_in,
                        // android.R.anim.fade_out);
                    }
                }, SPLASH_DISPLAY_TIME);
                postExecute();
            }

            @Override
            protected void onPreExecute() {

            }

            @Override
            protected void onProgressUpdate(Void... values) {
            }
        }

        CopyFiletask task = new CopyFiletask();
        task.execute();
    }

    public static void sendCrashReport(SharedPreferences myPrefs, Context con) {
        try {
            myPrefs = con.getSharedPreferences("pref", MODE_PRIVATE);
            String userid = myPrefs.getString(Constants.POST_FIELD_LOGIN_USERNAME,
                    "");
            String url = myPrefs.getString(Constants.SETTINGS_SYSTEM_URL_KEY, "");
            String usr = userid + "@" + url;

            String setid = myPrefs.getString(Constants.Crash_Last_SETID, "");
            String orderid = myPrefs.getString(Constants.Crash_Last_ORDERID, "");
            String devicname = Devices.getDeviceName();
            ClipboardManager clipboard = (ClipboardManager) con.getSystemService(Context.CLIPBOARD_SERVICE);
            if (clipboard.hasPrimaryClip() && clipboard.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
                String label = clipboard.getPrimaryClipDescription().getLabel().toString();
                if (clipboard.getPrimaryClipDescription() != null &&
                        label.contains("CheckerCrash")) {
//                    org.acra.util.CrashData thisCrashData = new org.acra.util.CrashData();
//                    thisCrashData.setSystem_url(usr);
//                    thisCrashData.setSet_id(setid);
//                    thisCrashData.setOrder_id(orderid);
//                    thisCrashData.setPhone_details(devicname);
//                    thisCrashData.setStack_trace(item.toString());
//                    thisCrashData.setUser_comment("without_comment");
//                    sendFireBaseCrash(thisCrashData);
//
//                    ClipData clip = ClipData.newPlainText("nothing", "");
//                    clipboard.setPrimaryClip(clip);
                }

            }
        } catch (Exception ex) {

        }
    }

    private List<File> getListFiles(File parentDir) {
        ArrayList<File> inFiles = new ArrayList<File>();
        File[] files = parentDir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                inFiles.addAll(getListFiles(file));
            } else {
                if (file.getName().endsWith(".java")) {
                    inFiles.add(file);
                }
            }
        }
        return inFiles;
    }

    private List<File> getListFilesXML(File parentDir) {
        ArrayList<File> inFiles = new ArrayList<File>();
        File[] files = parentDir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                inFiles.addAll(getListFilesXML(file));
            } else {
                if (file.getName().endsWith(".xml")) {
                    inFiles.add(file);
                }
            }
        }
        return inFiles;
    }

    private FileOutputStream writeToSDFile() {

        // Find the root of the external storage.
        // See http://developer.android.com/guide/topics/data/data-
        // storage.html#filesExternal

        File root = CheckerApp.localFilesDir;//android.os.Environment.getExternalStorageDirectory();

        // See
        // http://stackoverflow.com/questions/3551821/android-write-to-sd-card-folder

        File dir = new File(root.getAbsolutePath() + "/mnt");
        dir.mkdirs();
        File file = new File(dir, "myData.txt");

        try {
            FileOutputStream f = new FileOutputStream(file);
            return f;
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //
    // @Override
    // protected void onCreate(Bundle savedInstanceState) {
    //
    // super.onCreate(savedInstanceState);
    // setContentView(R.layout.splashscreen);
    // FileOutputStream f = writeToSDFile();
    // FileInputStream is;
    // BufferedReader reader;
    // PrintWriter pw = new PrintWriter(f);
    //
    // List<File> files = getListFiles(new File(
    // Environment.getExternalStorageDirectory()
    // + "/mnt/sdcard/source"));
    //
    // for (int fil = 0; fil < files.size(); fil++) {
    // File file = files.get(fil);
    // ArrayList<String> xmlItems = new ArrayList<String>();
    // int count = 0;
    //
    // try {
    // pw.println("New File" + file.getName());
    // pw.flush();
    //
    // } catch (Exception e1) {
    // // TODO Auto-generated catch block
    // e1.printStackTrace();
    // }
    //
    // if (file.exists()) {
    // try {
    // is = new FileInputStream(file);
    //
    // reader = new BufferedReader(new InputStreamReader(is));
    // String line = reader.readLine();
    // while (line != null) {
    // try {
    // line = reader.readLine();
    // ArrayList<String> checkStrings = readLine(line,
    // false);
    // for (int i = 0; i < checkStrings.size(); i++) {
    // String item = "<string name=\"s_item_column_"
    // + i + "_line_" + count + "_file_" + fil
    // + "\">" + checkStrings.get(i)
    // + "</string>";
    // boolean doesNotExist = checkIfExists(xmlItems,
    // checkStrings.get(i));
    // try {
    // if (doesNotExist) {
    // pw.println(item);
    // pw.flush();
    // }
    //
    // } catch (Exception e1) {
    // // TODO Auto-generated catch block
    // e1.printStackTrace();
    // }
    //
    // }
    //
    // count++;
    // if (count >= 200) {
    // count = count + 0;
    // }
    // } catch (Exception ex) {
    // int i = 0;
    // i++;
    // }
    // }
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // }
    // }
    //
    // // //////////////
    // files = getListFilesXML(new File(
    // Environment.getExternalStorageDirectory()
    // + "/mnt/sdcard/source"));
    //
    // for (int fil = 0; fil < files.size(); fil++) {
    // File file = files.get(fil);
    // ArrayList<String> xmlItems = new ArrayList<String>();
    // int count = 0;
    //
    // try {
    // pw.println("New File" + file.getName());
    // pw.flush();
    //
    // } catch (Exception e1) {
    // // TODO Auto-generated catch block
    // e1.printStackTrace();
    // }
    //
    // if (file.exists()) {
    // try {
    // is = new FileInputStream(file);
    //
    // reader = new BufferedReader(new InputStreamReader(is));
    // String line = reader.readLine();
    // while (line != null) {
    // try {
    // Log.d("StackOverflow", line);
    // line = reader.readLine();
    // ArrayList<String> checkStrings = readLine(line,
    // true);
    // for (int i = 0; i < checkStrings.size(); i++) {
    // String item = "<string name=\"s_item_column_"
    // + i + "_line_" + count + "_file_" + fil
    // + "\">" + checkStrings.get(i)
    // + "</string>";
    // boolean doesNotExist = checkIfExists(xmlItems,
    // checkStrings.get(i));
    // try {
    // if (doesNotExist) {
    // pw.println(item);
    // pw.flush();
    // }
    //
    // } catch (Exception e1) {
    // // TODO Auto-generated catch block
    // e1.printStackTrace();
    // }
    //
    // }
    //
    // count++;
    // if (count >= 200) {
    // count = count + 0;
    // }
    // } catch (Exception ex) {
    // int i = 0;
    // i++;
    // }
    // }
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // }
    // }
    //
    // try {
    // pw.close();
    // f.close();
    //
    // } catch (IOException e1) {
    // // TODO Auto-generated catch block
    // e1.printStackTrace();
    // }
    //
    // }

    private boolean checkIfExists(ArrayList<String> xmlItems, String string) {
        if (string.length() == 1)
            return false;
        for (int i = 0; i < xmlItems.size(); i++) {

            if (xmlItems.get(i) != null
                    && string != null
                    && xmlItems.get(i).toLowerCase()
                    .equals(string.toLowerCase())) {
                return false;
            }
        }
        xmlItems.add(string);
        return true;
    }

    private ArrayList<String> readLine(String line, boolean b) {

        ArrayList<String> words = new ArrayList<String>();
        if (line.indexOf("getString") > 0 || line == null)
            return words;

        if (b == true
                && (line.indexOf(":text") == -1 || line.indexOf("@string") > 0)) {
            return words;
        }
        while (true) {
            int start = line.indexOf("\"");
            int end = line.indexOf("\"", start + 1);
            if (start >= 0 && end > 0) {
                String substting = line.substring(start, end + 1);
                if (substting.replace("\"", "").startsWith("#")
                        || substting.replace("\"", "").equals("")) {
                } else
                    words.add(substting.replace("\"", ""));
                line = line.replace(substting, "");
            } else
                break;
        }
        return words;
    }

    static DatabaseReference reference;

    static FirebaseDatabase database;

    public static void addServiceLog(BasicLog basicLog) {
        String time = "";
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy MM/dd_HH:mm:ss");
            df.setTimeZone(TimeZone.getTimeZone("gmt"));
            time = df.format(new Date());
            String month = String.format("MMM", new Date());
            df = new SimpleDateFormat("MM");
            month = df.format(new Date());
            String year = String.format("yyyy", new Date());
            df = new SimpleDateFormat("yyyy");
            year = df.format(new Date());
            basicLog.setTime(time);

            if (database == null)
                database = FirebaseDatabase.getInstance();
            reference = database.getReference().child("LogstwelveServiceTEN").child(year).child("month_" + month).child(basicLog.getUrl()).child(basicLog.getUser()).child(basicLog.getOrderid()).push();
            reference.setValue(basicLog);
        } catch (Exception ec) {
            int i = 0;
            i++;
        }
    }

    public static void addLog(BasicLog basicLog) {
        String time = "";
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy MM/dd_HH:mm:ss");
            df.setTimeZone(TimeZone.getTimeZone("gmt"));
            time = df.format(new Date());
            String month = String.format("MMM", new Date());
            df = new SimpleDateFormat("MM");
            month = df.format(new Date());
            String year = String.format("yyyy", new Date());
            df = new SimpleDateFormat("yyyy");
            year = df.format(new Date());
            basicLog.setTime(time);

            if (database == null)
                database = FirebaseDatabase.getInstance();
            reference = database.getReference().
                    child("LogstwelveEU").
                    child(year)
                    .child("month_" + month)
                    .child(basicLog.getUrl())
                    .child(basicLog.getUser())
                    .child(basicLog.getOrderid())
                    .push();
            reference.setValue(basicLog);

        } catch (Exception ec) {
            int i = 0;
            i++;
        }
    }

    public static void addAudioLog(BasicLog basicLog) {
        String time = "";
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy MM/dd_HH:mm:ss");
            df.setTimeZone(TimeZone.getTimeZone("gmt"));
            time = df.format(new Date());
            String month = String.format("MMM", new Date());
            df = new SimpleDateFormat("MM");
            month = df.format(new Date());
            String year = String.format("yyyy", new Date());
            df = new SimpleDateFormat("yyyy");
            year = df.format(new Date());
            basicLog.setTime(time);

            if (database == null)
                database = FirebaseDatabase.getInstance();
            reference = database.getReference().child("LogstwelveTENAudio").child(year).child("month_" + month).child(basicLog.getUrl()).child(basicLog.getUser()).child(basicLog.getOrderid()).child("audio").push();
            reference.setValue(basicLog);
        } catch (Exception ec) {
            int i = 0;
            i++;
        }
    }
}
