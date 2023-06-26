package com.mor.sa.android.activities;


import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.checker.sa.android.helper.Constants;
import com.checker.sa.android.helper.Helper;
import com.checker.sa.android.helper.UIHelper;
import com.checker.sa.android.transport.Connector;
import com.elconfidencial.bubbleshowcase.BubbleShowCase;
import com.elconfidencial.bubbleshowcase.BubbleShowCaseBuilder;

import java.util.ArrayList;
import java.util.Locale;

public class NewSettingsActivity extends AppCompatActivity implements View.OnClickListener,
        View.OnFocusChangeListener {

    EditText systemUrl;

    AppCompatButton save;
    SharedPreferences myPrefs;
    AppCompatSpinner spinner, theme, resizing_list, branch_spinner;
    int modeSelect;
    TextView tv;
    String[] languages = null;
    private LinearLayoutCompat switchtracking, switchBranchName, switchFilterDate, switchWifiOnly, switchAlternateJobs;
    private LinearLayoutCompat switchhorses, switchCropping, switchDeviceCamera, switchResolution, switchAnotherPhoto;
    RelativeLayout layout;
    boolean isJobChecked = false;
    boolean isBranchName = false;
    boolean isFilterDate = false;
    boolean isHorsesChecked = false;
    boolean isWifiOnly = false;
    boolean isAlternateJobs = false;
    boolean isSwitchCropping = false;
    boolean isDeviceCamera = false;
    boolean isResolution = false;
    boolean isAnotherPhoto = false;

    public void makeGuideDialog(Context context) {

        final Dialog err_dialog = new Dialog(context);
        err_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        err_dialog.setContentView(R.layout.dialog_briefing);
        TextView txt = err_dialog.findViewById(R.id.textView1);

        WebView wv = (WebView) err_dialog.findViewById(R.id.briefingView);
        final String mimeType = "text/html";
        final String encoding = "UTF-8";
        wv.loadUrl("file:///android_asset/settingguide.html");
        Button btnClose = (Button) err_dialog.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                err_dialog.dismiss();
            }
        });

        //if (b==false)//preview dialog
        {

            //txt.setText("Preview");
            txt.setText("Help");//context.getResources().getString(R.string.questionnaire_preview_menu));
            btnClose.setText(context.getResources().getString(R.string.close));
            //btnClose.setText("Close");
        }
        err_dialog.show();
    }

    private void setInvertDisplay(int i) {
        if (i == 1) {
            layout = (RelativeLayout) findViewById(R.id.jdbottomrv);
            layout = (RelativeLayout) findViewById(R.id.rvjobdetailscreen);
            layout = (RelativeLayout) findViewById(R.id.backGroundDrawable);
            layout.setBackgroundDrawable(getResources().getDrawable(
                    R.drawable.background_dark));
        } else {
            layout = (RelativeLayout) findViewById(R.id.jdbottomrv);
            layout = (RelativeLayout) findViewById(R.id.rvjobdetailscreen);
            layout = (RelativeLayout) findViewById(R.id.backGroundDrawable);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        myPrefs = getSharedPreferences("pref", MODE_PRIVATE);
        Constants.setLocale(NewSettingsActivity.this);
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager
                .getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_settings);
        Constants.setLocale(NewSettingsActivity.this);
//        helpbtn=(ImageView)  findViewById(R.id.btnhelp);
//        if (helpbtn!=null)
//            helpbtn.setOnClickListener(this);
//        textView = (TextView) findViewById(R.id.jd_heder);
//        scroll = (ScrollView) findViewById(R.id.jdmiddle);
//		scroll.post(new Runnable() {
//			@Override
//			public void run() {
//				scroll.fullScroll(View.FOCUS_DOWN);
//				modeSelect = myPrefs.getInt(Constants.SETTINGS_LANGUAGE_INDEX, 0);
//				if (modeSelect!=3)
//					new BubbleShowCaseSequence()
//							.addShowCase(showMenuShowCase()).show();
//			}
//		});
//        textView.setTextSize(UIHelper.getFontSize(NewSettingsActivity.this,
//                textView.getTextSize()));
        switchtracking = (LinearLayoutCompat) findViewById(R.id.toggleJobs);
        switchhorses = (LinearLayoutCompat) findViewById(R.id.toggleHorses);
        switchBranchName = (LinearLayoutCompat) findViewById(R.id.switch_branch_name);
        switchFilterDate = (LinearLayoutCompat) findViewById(R.id.switch_filter_date);
        switchWifiOnly = (LinearLayoutCompat) findViewById(R.id.switch_wifi_only);
        switchAlternateJobs = (LinearLayoutCompat) findViewById(R.id.switch_alternate_jobs);
        switchCropping = (LinearLayoutCompat) findViewById(R.id.switchCropping);
        switchDeviceCamera = (LinearLayoutCompat) findViewById(R.id.switchDeviceCamera);
        switchResolution = (LinearLayoutCompat) findViewById(R.id.switchResolution);
        switchAnotherPhoto = (LinearLayoutCompat) findViewById(R.id.switchAnother);

        myPrefs = getSharedPreferences("pref", MODE_PRIVATE);
        String url = myPrefs.getString(Constants.SETTINGS_SYSTEM_URL_KEY, null);

        systemUrl = (EditText) findViewById(R.id.s_url);
        if (getPackageName() != null
                && getPackageName().contains(Helper.CONSTPACKAGEPREFIX)) {
            systemUrl.setEnabled(false);
        }
        systemUrl.setTextSize(UIHelper.getFontSize(NewSettingsActivity.this,
                systemUrl.getTextSize()));

        branch_spinner = (AppCompatSpinner) findViewById(R.id.tvSpinnerBranchSorting);
        spinner = (AppCompatSpinner) findViewById(R.id.tvSpinnerLanguage);


        String str = android.os.Build.VERSION.SDK;
        int sdk = Integer.valueOf(str);

        Locale[] langs = Locale.getAvailableLocales();
        Boolean isHindi = false;

        languages = getResources().getStringArray(R.array.lang_list);

        String[] branch_sorting_list = new String[2];
        branch_sorting_list[0] = getResources().getString(
                R.string.s_alphabetical);
        branch_sorting_list[1] = getResources().getString(R.string.s_distance);
        ArrayAdapter badapter = new ArrayAdapter(

                this, UIHelper.getSpinnerLayout(NewSettingsActivity.this, modeSelect),
                branch_sorting_list);
        badapter.setDropDownViewResource(UIHelper.getSpinnerLayout(NewSettingsActivity.this, modeSelect));
        branch_spinner.setAdapter(badapter);
        if (myPrefs.getBoolean(Constants.SETTINGS_ENABLE_SORTING,
                Helper.ENABLESORTINGBYDEFAULT)) {
            branch_spinner.setSelection(1);
        } else {
            branch_spinner.setSelection(0);
        }

        branch_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                boolean arg22 = true;
                if (arg2 == 1)
                    arg22 = true;
                else
                    arg22 = false;
                SharedPreferences.Editor prefsEditor = myPrefs.edit();
                prefsEditor.putBoolean(Constants.SETTINGS_ENABLE_SORTING, arg22);
                prefsEditor.commit();
                // ACRA.getErrorReporter().putCustomData("System Language",
                // languages[arg2].toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        ArrayAdapter adapter = new ArrayAdapter(
                this, UIHelper.getSpinnerLayout(NewSettingsActivity.this, modeSelect),
                languages);

        adapter.setDropDownViewResource(UIHelper.getSpinnerLayout(
                NewSettingsActivity.this, modeSelect));
        spinner.setAdapter(adapter);

        theme = (AppCompatSpinner) findViewById(R.id.tvSpinnerTheme);
        resizing_list = (AppCompatSpinner) findViewById(R.id.tvSpinnerImageResizing);

        String[] themesList = getResources()
                .getStringArray(R.array.themes_list);
        String[] themes = new String[1];
        if (themesList.length > 1) {
            themes[0] = themesList[0];
        } else {
            themes[0] = getResources().getString(R.string.default_theme);
        }
        themes[0] = getResources().getString(R.string.default_theme);
        ArrayList<String> themeList = new ArrayList<String>();
        themeList.add(Helper.getThemeName(getApplicationContext()));
        // themeList.add("VOC theme");
        ArrayAdapter themeadapter = new ArrayAdapter(
                this,
                UIHelper.getSpinnerLayout(NewSettingsActivity.this, modeSelect),
                themeList);
        themeadapter.setDropDownViewResource(UIHelper.getSpinnerLayout(
                NewSettingsActivity.this, modeSelect));
        theme.setAdapter(themeadapter);

        ArrayList<String> resizeList = new ArrayList<String>();
        resizeList.add(getResources().getString(
                R.string.settings_image_original));
        resizeList
                .add(getResources().getString(R.string.settings_image_medium));
        resizeList.add(getResources().getString(R.string.settings_image_small));
        resizeList.add(getResources().getString(R.string.settings_image_ask));

        ArrayAdapter resizeAdapter = new ArrayAdapter(
                this,
                UIHelper.getSpinnerLayout(NewSettingsActivity.this, modeSelect),
                resizeList);
        resizeAdapter.setDropDownViewResource(UIHelper.getSpinnerLayout(
                NewSettingsActivity.this, modeSelect));
        resizing_list.setAdapter(resizeAdapter);


        systemUrl.setWidth((Helper.getViewWidth(this.getApplicationContext())) * 2);
        save = (AppCompatButton) findViewById(R.id.btnSave);

        systemUrl.setTextSize(UIHelper.getFontSize(NewSettingsActivity.this,
                systemUrl.getTextSize()));
        save.setOnClickListener(this);

        myPrefs = getSharedPreferences("pref", MODE_PRIVATE);

        /** switchBranchName **/

        if (myPrefs.getBoolean(Constants.SETTINGS_ENABLE_BRANCH_FULL_NAME,
                Helper.ENABLEBRANCHFULLNAMEBYDEFAULT)) {
            setToggle(switchBranchName, true);
        } else {
            setToggle(switchBranchName, false);
        }
        switchBranchName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (switchBranchName.getLayoutDirection() == View.LAYOUT_DIRECTION_LTR) {
                    isBranchName = true;
                    setToggle(switchBranchName, true);
                } else {
                    isBranchName = false;
                    setToggle(switchBranchName, false);
                }

                SharedPreferences.Editor prefsEditor = myPrefs.edit();
                prefsEditor.putBoolean(
                        Constants.SETTINGS_ENABLE_BRANCH_FULL_NAME,
                        isBranchName);
                prefsEditor.commit();

            }
        });


        /** switchFilterDate **/

        if (myPrefs.getBoolean(Constants.SETTINGS_ENABLE_DATE_FILTER,
                Helper.ENABLEDATEFILTERBYDEFAULT)) {
            setToggle(switchFilterDate, true);
        } else {
            setToggle(switchFilterDate, false);
        }
        switchFilterDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (switchFilterDate.getLayoutDirection() == View.LAYOUT_DIRECTION_LTR) {
                    isFilterDate = true;
                    setToggle(switchFilterDate, true);
                } else {
                    isFilterDate = false;
                    setToggle(switchFilterDate, false);
                }

                SharedPreferences.Editor prefsEditor = myPrefs.edit();
                prefsEditor.putBoolean(
                        Constants.SETTINGS_ENABLE_DATE_FILTER,
                        isFilterDate);
                prefsEditor.commit();

            }
        });


        /** switchtracking **/

        if (myPrefs.getBoolean(Constants.SETTINGS_switchtracking,
                Helper.ENABLETRACKINGBYDEFAULT)) {
            setToggle(switchtracking, true);
        } else {
            setToggle(switchtracking, false);
        }
        switchtracking.setOnClickListener(view -> {
            if (switchtracking.getLayoutDirection() == View.LAYOUT_DIRECTION_LTR) {
                isJobChecked = true;
                setToggle(switchtracking, true);
            } else {
                isJobChecked = false;
                setToggle(switchtracking, false);
            }
            SharedPreferences.Editor prefsEditor = myPrefs.edit();
            prefsEditor.putBoolean(
                    Constants.SETTINGS_switchtracking, isJobChecked);
            prefsEditor.commit();
            if (isJobChecked) {
                try {
                    Helper.setAN168Log("line 447, bool=" + isJobChecked);
                } catch (Exception ex) {
                    Helper.setAN168Log("crashed line 451");
                }
            } else if (isMyServiceRunning(comService.class)) {
                Helper.setAN168Log("line 454, bool=" + isJobChecked);
            }

        });


        /** switchhorses **/

        switchhorses.setOnClickListener(view -> {
            if (switchhorses.getLayoutDirection() == View.LAYOUT_DIRECTION_LTR) {
                isHorsesChecked = true;
                setToggle(switchhorses, true);
            } else {
                isHorsesChecked = false;
                setToggle(switchhorses, false);
            }
        });


        /** switchAnotherPhoto **/

        if (myPrefs.getBoolean(Constants.SETTINGS_SHOW_ANOTHER_PHOTO,
                Helper.ENABLEMULTIPLEPICTUREDEFAULT)) {
            setToggle(switchAnotherPhoto, true);
        } else {
            setToggle(switchAnotherPhoto, false);
        }
        switchAnotherPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchAnotherPhoto.getLayoutDirection() == View.LAYOUT_DIRECTION_LTR) {
                    isAnotherPhoto = true;
                    setToggle(switchAnotherPhoto, true);
                } else {
                    isAnotherPhoto = false;
                    setToggle(switchAnotherPhoto, false);
                }

                SharedPreferences.Editor prefsEditor = myPrefs.edit();
                prefsEditor.putBoolean(
                        Constants.SETTINGS_SHOW_ANOTHER_PHOTO,
                        isAnotherPhoto);
                prefsEditor.commit();
            }
        });



        /** switchCropping **/

        if (myPrefs.getBoolean(Constants.SETTINGS_ENABLE_CROPPING, false)) {
            setToggle(switchCropping, true);
        } else {
            setToggle(switchCropping, false);
        }

        switchCropping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchCropping.getLayoutDirection() == View.LAYOUT_DIRECTION_LTR) {
                    isSwitchCropping = true;
                    setToggle(switchCropping, true);
                } else {
                    isSwitchCropping = false;
                    setToggle(switchCropping, false);
                }

                SharedPreferences.Editor prefsEditor = myPrefs.edit();
                prefsEditor.putBoolean(
                        Constants.SETTINGS_ENABLE_CROPPING, isSwitchCropping);
                prefsEditor.commit();
            }
        });


        /** switchDeviceCamera **/

        if (myPrefs.getBoolean(Constants.SETTINGS_ENABLE_DEFAULT_CAMERA, false)) {
            setToggle(switchDeviceCamera, true);
        } else {
            setToggle(switchDeviceCamera, false);
        }

        switchDeviceCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchDeviceCamera.getLayoutDirection() == View.LAYOUT_DIRECTION_LTR) {
                    isDeviceCamera = true;
                    setToggle(switchDeviceCamera, true);
                } else {
                    isDeviceCamera = false;
                    setToggle(switchDeviceCamera, false);
                }

                SharedPreferences.Editor prefsEditor = myPrefs.edit();
                prefsEditor.putBoolean(
                        Constants.SETTINGS_ENABLE_DEFAULT_CAMERA, isDeviceCamera);
                prefsEditor.commit();
                Helper.setDeviceCamera(isDeviceCamera);

            }
        });


        /** switchResolution **/

        if (myPrefs.getBoolean(Constants.SETTINGS_ENABLE_RESOLUTION, false)) {
            setToggle(switchResolution, true);
        } else {
            setToggle(switchResolution, false);
        }
        switchResolution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchResolution.getLayoutDirection() == View.LAYOUT_DIRECTION_LTR) {
                    isResolution = true;
                    setToggle(switchResolution, true);
                } else {
                    isResolution = false;
                    setToggle(switchResolution, false);
                }

                SharedPreferences.Editor prefsEditor = myPrefs.edit();
                prefsEditor
                        .putBoolean(
                                Constants.SETTINGS_ENABLE_RESOLUTION,
                                isResolution);
                prefsEditor.commit();
            }
        });


        /** switchWifiOnly **/

        if (myPrefs.getBoolean(Constants.SETTINGS_WIFI_ONLY, false)) {
            setToggle(switchWifiOnly, true);
        } else {
            setToggle(switchWifiOnly, false);
        }
        switchWifiOnly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchWifiOnly.getLayoutDirection() == View.LAYOUT_DIRECTION_LTR) {
                    isWifiOnly = true;
                    setToggle(switchWifiOnly, true);
                } else {
                    isWifiOnly = false;
                    setToggle(switchWifiOnly, false);
                }

                SharedPreferences.Editor prefsEditor = myPrefs.edit();
                prefsEditor
                        .putBoolean(
                                Constants.SETTINGS_WIFI_ONLY,
                                isWifiOnly);
                prefsEditor.commit();
            }
        });


        /** switchAlternateJobs **/

        if (myPrefs.getBoolean(Constants.SETTINGS_ENABLE_ALTERNATE_ORDER,
                Helper.ENABLEALTERNATEJOBS)) {
            setToggle(switchAlternateJobs, true);
        } else {
            setToggle(switchAlternateJobs, false);
        }
        switchAlternateJobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (switchAlternateJobs.getLayoutDirection() == View.LAYOUT_DIRECTION_LTR) {
                    isAlternateJobs = true;
                    setToggle(switchAlternateJobs, true);
                } else {
                    isAlternateJobs = false;
                    setToggle(switchAlternateJobs, false);
                }

                SharedPreferences.Editor prefsEditor = myPrefs.edit();
                prefsEditor.putBoolean(
                        Constants.SETTINGS_ENABLE_ALTERNATE_ORDER,
                        isAlternateJobs);
                prefsEditor.commit();

            }
        });


        systemUrl.setText(myPrefs.getString(Constants.SETTINGS_SYSTEM_URL_KEY,
                ""));
        modeSelect = myPrefs.getInt(Constants.SETTINGS_MODE_INDEX, 1);
        systemUrl.setFocusable(true);
        systemUrl.setOnFocusChangeListener(this);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }


        spinner.setSelection(myPrefs.getInt(Constants.SETTINGS_LANGUAGE_INDEX,
                0));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                Locale locale = new Locale(
                        Constants.SETTINGS_LOCALE_VAL_ARR[arg2]);
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getBaseContext().getResources().updateConfiguration(config,
                        getBaseContext().getResources().getDisplayMetrics());
                SharedPreferences.Editor prefsEditor = myPrefs.edit();
                prefsEditor.putInt(Constants.SETTINGS_LANGUAGE_INDEX, arg2);
                prefsEditor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        theme.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long arg3) {
                SharedPreferences.Editor prefsEditor = myPrefs.edit();
                prefsEditor.putInt(Constants.SETTINGS_MODE_INDEX, position);
                prefsEditor.commit();
                Helper.setTheme(position);

                modeSelect = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        resizing_list.setSelection(myPrefs.getInt(
                Constants.SETTINGS_RESIZE_INDEX, 0));
        Helper.setSampleSize(myPrefs.getInt(Constants.SETTINGS_RESIZE_INDEX, 0));
        resizing_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       final int arg2, long arg3) {

                if (arg2 == 2
                        && myPrefs.getInt(Constants.SETTINGS_RESIZE_INDEX, 0) != 2) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(
                            NewSettingsActivity.this);
                    alert.setCancelable(false);
                    alert.setTitle("");
                    TextView textView = new TextView(NewSettingsActivity.this);
                    textView.setTextSize(UIHelper.getFontSize(
                            NewSettingsActivity.this, textView.getTextSize()));
                    textView.setText(getString(R.string.smalimgWarn));
                    alert.setView(textView);
                    alert.setPositiveButton(
                            getString(R.string.s_item_column_0_line_344_file_210),
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    SharedPreferences.Editor prefsEditor = myPrefs
                                            .edit();
                                    prefsEditor.putInt(
                                            Constants.SETTINGS_RESIZE_INDEX,
                                            arg2);
                                    prefsEditor.commit();

                                    Helper.setSampleSize(arg2);
                                }
                            });

                    alert.setNegativeButton(
                            getString(R.string.s_item_column_0_line_346_file_210),
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {

                                    resizing_list.setSelection(myPrefs.getInt(
                                            Constants.SETTINGS_RESIZE_INDEX, 0));
                                }
                            });
                    alert.show();
                } else {
                    SharedPreferences.Editor prefsEditor = myPrefs.edit();
                    prefsEditor.putInt(Constants.SETTINGS_RESIZE_INDEX, arg2);
                    prefsEditor.commit();

                    Helper.setSampleSize(arg2);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        save.requestFocus();
        systemUrl.clearFocus();
    }

    private BubbleShowCaseBuilder showMenuShowCase() {
        return new BubbleShowCaseBuilder(this) //Activity instance
                .title("Camera") //Any title for the bubble view
                .description("Android's own camera with all features.") //More detailed description
                .arrowPosition(BubbleShowCase.ArrowPosition.RIGHT) //You can force the position of the arrow to change the location of the bubble.
                .backgroundColor(Color.parseColor("#007BFF")) //Bubble background color
                .textColor(Color.WHITE) //Bubble Text color
                .titleTextSize(20) //Title text size in SP (default value 16sp)
                .descriptionTextSize(15) //Subtitle text size in SP (default value 14sp)
                .showOnce("BUBBLE_SHOW_CAMERA123") //Id to show only once the BubbleShowCase
                .targetView(switchDeviceCamera);
    }

    public boolean IsInternetConnectted() {
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo i = conMgr.getActiveNetworkInfo();
        conMgr = null;
        if (i == null)
            return false;
        if (!i.isConnected())
            return false;
        if (!i.isAvailable())
            return false;
        return true;
    }

    @Override
    public void onClick(View arg0) {

        if (save.getId() == arg0.getId()) {
            hideKeyBoard();
            if (IsInternetConnectted()) {
                String systemurll = Helper.getSystemURLfromDB();

                String editTextURL = systemUrl.getText().toString();

                if (!editTextURL.equals(systemurll)) {
                    setLogin(false);
                }
            }
            if (Helper.IsEmptyString(systemUrl.getText().toString())) {
                ShowAlert(NewSettingsActivity.this,
                        getString(R.string.error_alert_title),
                        getString(R.string.settings_url_alert),
                        getString(R.string.alert_btn_lbl_ok));
                return;
            } else {
                String addedurl = systemUrl.getText().toString();
                if (!addedurl.equals(Helper.getSystemURLfromDB())) {
                    Helper.setSystemURL(Helper.getValidURL(systemUrl.getText()
                            .toString()));
                    SharedPreferences.Editor prefsEditor = myPrefs.edit();
                    prefsEditor.putString(Constants.SETTINGS_SYSTEM_URL_KEY,
                            Helper.getSystemURLfromDB());
                    prefsEditor.commit();
                    Connector.cookies = null;
                }

                Intent intent = new Intent(this.getApplicationContext(),
                        NewLoginActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    private void setLogin(boolean islogin) {
        if (myPrefs == null)
            return;
        SharedPreferences.Editor prefsEditor = myPrefs.edit();
        prefsEditor.putBoolean(Constants.ALREADY_LOGIN_STATUS, islogin);
        prefsEditor.putString(Constants.POST_FIELD_LOGIN_USERNAME, "");
        prefsEditor.putString(Constants.POST_FIELD_LOGIN_PASSWORD, "");
        prefsEditor.commit();
    }

    public void ShowAlert(Context context, String title, String message,
                          String button_lbl) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(title);
        TextView textView = new TextView(context);
        textView.setTextSize(UIHelper.getFontSize(NewSettingsActivity.this,
                textView.getTextSize()));
        textView.setText(message);
        alert.setView(textView);
        alert.setPositiveButton(button_lbl,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alert.show();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            systemUrl.setSelection(systemUrl.getText().length());
        }
    }

    private void hideKeyBoard() {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setToggle(LinearLayoutCompat view, boolean isToggleOn) {
        if (view != null) {
            if (isToggleOn) {
                view.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                view.setBackgroundResource(R.drawable.blue_corners);
            } else {
                view.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                view.setBackgroundResource(R.drawable.gray_corners);
            }
        }
    }

}
