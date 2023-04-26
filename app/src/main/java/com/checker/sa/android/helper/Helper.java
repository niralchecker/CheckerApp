package com.checker.sa.android.helper;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.SerializationUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.ByteArrayBuffer;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.media.ExifInterface;
import android.os.Environment;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.checker.sa.android.data.Answers;
import com.checker.sa.android.data.Order;
import com.checker.sa.android.data.Orders;
import com.checker.sa.android.data.QuestionnaireData;
import com.checker.sa.android.data.Quota;
import com.checker.sa.android.data.Survey;
import com.checker.sa.android.data.Surveys;
import com.checker.sa.android.db.DBHelper;
import com.mor.sa.android.activities.CheckerApp;
import com.mor.sa.android.activities.R;

@SuppressLint("NewApi")
public class Helper {
    public static final int NoTimePicker = -1;
    public static final int TimePicker = 0;
    public static final int DatePicker = 1;
    public static final int DateTimePicker = 2;

    // public static final CharSequence CONST_BE_THERE = "mystery";
    // public static final CharSequence imgprefix = "mys_";
    // public static final String appColor = "#a3a119"; // for bethere #d20024
    // public static final String unselectappColor = "#666666"; // for
    // bethere"#d84760"
    // public static final int loaderOuterColor = 0xFFa3a119;
    // public static final CharSequence CONSTPACKAGEPREFIX = "com.mys";
    // ////////////////////////
    // public static final CharSequence CONST_BE_THERE = "mystery";
    // public static final CharSequence imgprefix = "mys_";
    // public static final String appColor = "#a3a119"; // for bethere #d20024
    // public static final String btnInsideColor = "#cdcdcd"; // for bethere
    // // #d20024
    // public static final String unselectappColor = "#666666"; // for
    // // bethere"#d84760"
    // public static final int loaderOuterColor = 0xFFa3a119;
    // public static final CharSequence CONSTPACKAGEPREFIX = "com.mys";
    // public static final String systemUrl =
    // "https://system.mysterydelegates.com/";
    // public static final String serviceTitle = "Mysterio by MDS";
    // public static final int splash_layout = R.layout.mys_splash;
    // public static final boolean showSignup = false;
    // public static final boolean isMisteroMenu = false;
    // public static final int DEFAULT_LANGUAGE = 0;
    // //////////////////////////

    // public static final CharSequence CONST_BE_THERE = "bethere";
    // public static final CharSequence imgprefix = "wbt_";
    // public static final String appColor = "#d20024"; // for bethere #d20024
    // public static final String unselectappColor = "#d84760"; // for
    // bethere"#d84760"
    // public static final int loaderOuterColor = 0xFFd20024;
    // public static final CharSequence CONSTPACKAGEPREFIX = "com.wbt";
    // public static final String systemUrl = "https://www.webethere.net/";
    // public static final int splash_layout = R.layout.wbt_splashscreen;
    // ///////////////////////

    // CHANGE THISI TO TRUE
    public static final boolean isMisteroMenu = false;// /////
    // //////////

    public static String helpline = "+0";
    public static final CharSequence CONST_BE_THERE = "mistero";// "testing";
    public static final CharSequence imgprefix = "";
    public static final String appColor = "#CFB9AC"; // for bethere #d20024
    public static final String btnInsideColor = "#ffffff";//"#fecc08"; // for bethere
    // #d20024
    public static final String unselectappColor = "#CFDA9A";//"#fecc08"; // for
    // bethere"#d84760"
    public static final int loaderOuterColor = 0xFF305699;//0xFFfecc08;
    // https://abastecaseubolso.misteroclienteoculto.com.br/recover-password.php?w=ch2
    public static final CharSequence CONSTPACKAGEPREFIX = "com.mistero";
    // public static final String systemUrl =
    // public static final String systemUrl =
    // "https://checker-soft.com/testing";
    // "https://abastecaseubolso.misteroclienteoculto.com.br";//
    public static final String systemUrl = "https://abastecaseubolso.misteroclienteoculto.com.br";// "https://www.checker-soft.com/testing";
    public static final String serviceTitle = "Mister O - Abaste?a Seu Bolso";
    public static final int splash_layout = R.layout.mistero_splashscreen;
    public static final boolean showSignup = true;

    public static final int DEFAULT_LANGUAGE = 5;
    // CHECKER COMMON FIELDS
    // public static final boolean ENABLESORTINGBYDEFAULT = false;// true for
    // public static final boolean ENABLEBRANCHFULLNAMEBYDEFAULT = false;// true
    // public static final boolean ENABLEDATEFILTERBYDEFAULT = false;// false
    // public static final boolean ENABLETRACKINGBYDEFAULT = true;// true for
    // public static final boolean ENABLEMULTIPLEPICTUREDEFAULT = false;// true
    // public static final boolean ENABLETIMESTAMPBYDEFAULT = false;// false for
    // public static final boolean ENABLEALTERNATEJOBS = false;// true for

    // AND MISTERO COMMON FIELDS
    public static final boolean ENABLESORTINGBYDEFAULT = true;// true for
    public static final boolean ENABLEBRANCHFULLNAMEBYDEFAULT = false;// true
    public static final boolean ENABLEDATEFILTERBYDEFAULT = false;// false
    public static final boolean ENABLETRACKINGBYDEFAULT = true;// true for
    public static final boolean ENABLEMULTIPLEPICTUREDEFAULT = false;// true
    public static final boolean ENABLETIMESTAMPBYDEFAULT = false;// false for
    public static final boolean ENABLEALTERNATEJOBS = false;// true for this is
    // no auto uploading

    private static String companyId = "";
    private static String systemURL = "";
    private static boolean isParsed = false;
    public static Location loc = null;
    public static Location lastLoc = null;

    public static String critStartLat, critStartLong, critEndLat, critEndLong;

    public static ArrayList<QuestionnaireData> questionnaireData;
    private static int theme = 0;
    private static boolean configChange;
    public static int seed;
    public static Bitmap thisImage;
    private static String dbPath = null;
    public static boolean syncing = false;
    private static String alternateURL = null;

    public static String limitExceededMessage(ArrayList<Quota> quotaQuestions,
                                              ArrayList<Survey> surveys, String returnMessage, Context c) {
        for (int i = 0; i < quotaQuestions.size(); i++) {
            if (quotaQuestions.get(i) != null) {
                try {
                    int n = Integer.valueOf(quotaQuestions.get(i)
                            .getDoneCount());
                    n++;
                    int k = Integer
                            .valueOf(quotaQuestions.get(i).getQuotaMin());
                    if (n > k) {
                        returnMessage = getQuotaMessage(quotaQuestions.get(i)
                                .getSurveyLink(), surveys, c);
                        if (quotaQuestions.get(i).getActionToTake().equals("1")) {
                            return "<b>"
                                    + returnMessage
                                    + "</b>"
                                    + c.getResources()
                                    .getString(
                                            R.string.survey_quota_delete_message)
                                    .replace(
                                            "QNAME",
                                            quotaQuestions.get(i)
                                                    .getQuotaName())
                                    .replace("##", k + "")
                                    .replace("#break#", "<br>");
                            // "<br>Quota["
                            // + quotaQuestions.get(i).getQuotaName()
                            // + "] reached.<br> You have done all " + k
                            // + "<br>Deleting interview.";
                        } else {
                            return "<b>"
                                    + returnMessage
                                    + "</b>"
                                    + c.getResources()
                                    .getString(
                                            R.string.survey_quota_upload_message)
                                    .replace(
                                            "QNAME",
                                            quotaQuestions.get(i)
                                                    .getQuotaName())
                                    .replace("##", k + "")
                                    .replace("#break#", "<br>");
                        }

                    }
                } catch (Exception e) {

                }
            }
        }
        returnMessage = null;
        return returnMessage;
    }

    public static String limitExceeded(ArrayList<Quota> quotaQuestions,
                                       ArrayList<Survey> surveys, String returnMessage, Context c) {
        for (int i = 0; i < quotaQuestions.size(); i++) {
            if (quotaQuestions.get(i) != null) {
                try {
                    int n = Integer.valueOf(quotaQuestions.get(i)
                            .getDoneCount());
                    n++;
                    int k = Integer
                            .valueOf(quotaQuestions.get(i).getQuotaMin());
                    if (n > k) {
                        returnMessage = getQuotaMessage(quotaQuestions.get(i)
                                .getSurveyLink(), surveys, c);
                        return quotaQuestions.get(i).getActionToTake();
                    }
                } catch (Exception e) {

                }
            }
        }
        returnMessage = null;
        return null;
    }

    private static String getQuotaMessage(String surveyLink,
                                          ArrayList<Survey> surveys, Context c) {
        for (int i = 0; i < surveys.size(); i++) {
            if (surveys.get(i).getSurveyID().equals(surveyLink))
                return surveys.get(i).getQuotaReachedMessage(c);
        }
        return "Limit Exceeded!";
    }

    public static List<NameValuePair> convertQuotasToNameValuePairs(
            List<NameValuePair> extraDataList, ArrayList<Quota> quotas,
            String surveyID, boolean doIncrement, boolean isCompleted) {

        if (surveyID.equals(""))
            return extraDataList;

        if (quotas != null) {
            for (int i = 0; i < quotas.size(); i++) {
                Quota answer = quotas.get(i);
                extraDataList.add(Helper.getNameValuePair(
                        Constants.POST_FIELD_QUOTA_QuotasToUpdate + "[]",
                        answer.getsquoID()));
                if (doIncrement)
                    Surveys.increaseQuota(answer);
            }
        }

        String id = surveyID;
        if (id.contains("_")) {
            id = id.substring(0, id.indexOf("_"));
        }
        extraDataList.add(Helper.getNameValuePair(
                Constants.POST_FIELD_QUOTA_SURVEY_ID, id));
        if (doIncrement && !isCompleted) {
            Surveys.increaseAllocation(surveyID);

            DBHelper.AddSurveys(Surveys.getSets(), false);
        }
        return extraDataList;
    }

    public static Spanned makeHtmlString(String input) {
        if (input == null)
            input = "";
        input = input.replace("[b]", "<b>");
        input = input.replace("[/b]", "</b>");
        input = input.replace("[i]", "<i>");
        input = input.replace("[/i]", "</i>");
        input = input.replace("[u]", "<u>");
        input = input.replace("[/u]", "</u>");
        if (input.contains("<") && input.contains(">")) {
            Spanned html = Html.fromHtml(input);
            return html;
        } else {
            Spanned html = new SpannableString(input);
            return html;
        }
    }

    public static void saveCrashReportToBytes(Serializable object)
            throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutput out = new ObjectOutputStream(bos)) {
            out.writeObject(object);
            byte[] bytes = SerializationUtils.serialize(object);
            long unixTime = System.currentTimeMillis() / 1000L;
            File root = CheckerApp.localFilesDir;//Environment.getExternalStorageDirectory();
            String path = root.getAbsolutePath() + "/mnt/sdcard/Checker_Crash/";
            File dir = new File(path);
            if (dir.exists() == false) {
                dir.mkdirs();
            }

            File file = new File(path, "crash_" + unixTime + ".txt");
            file.createNewFile();
            {

                FileOutputStream fos = new FileOutputStream(file);
                fos.write(bytes);
                fos.close();
            }
        }

    }

    static boolean suggestionsEnabled = true;

    public static boolean isSuggestionsEnabled() {
        return suggestionsEnabled;
    }

    public static void setSuggestionsEnabled(boolean suggestionsEnabled) {
        Helper.suggestionsEnabled = suggestionsEnabled;
    }

    static boolean defaultcamera = false;

    public static boolean isDeviceCamera() {
        return defaultcamera;
    }

    public static void setDeviceCamera(boolean suggestionsEnabled) {
        Helper.defaultcamera = suggestionsEnabled;
    }

    static boolean lastSavedEnabled = false;

    public static boolean islastSavedEnabled() {
        return true;
    }

    public static void setlastSavedEnabled(boolean suggestionsEnabled) {
        Helper.lastSavedEnabled = suggestionsEnabled;
    }

    public Boolean deleteFilesInFolder() {
        File root = CheckerApp.localFilesDir;//Environment.getExternalStorageDirectory();

        File dir = new File(root.getAbsolutePath() + "/mnt/sdcard/Checker");
        if (dir.exists() == false) {

            return true;
        }
        try {
            if (dir.isDirectory()) {
                String[] children = dir.list();
                for (int i = 0; i < children.length; i++) {
                    new File(dir, children[i]).delete();
                }

            }
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    public Boolean deleteSetsInFolder() {
//		File root = android.os.Environment.getExternalStorageDirectory();
//
//		File dir = new File(root.getAbsolutePath()
//				+ "/mnt/sdcard/Checker_binaries");
//		if (dir.exists() == false) {
//
//			return true;
//		}
//		try {
//			if (dir.isDirectory()) {
//				String[] children = dir.list();
//				for (int i = 0; i < children.length; i++) {
//					if (!children[i].contains("file"))
//						new File(dir, children[i]).delete();
//				}
//
//			}
//		} catch (Exception ex) {
//			return false;
//		}
        return true;
    }

    public boolean validateIcon(String icon) {
        try {
            File root = CheckerApp.localFilesDir;//Environment.getExternalStorageDirectory();

            File dir = new File(root.getAbsolutePath() + "/mnt/sdcard/Checker");
            if (dir.exists() == false) {
                dir.mkdirs();
            }
            File file = new File(dir, icon);
            if (file.exists()) {
                int k = 0;
                k++;
                return true;
            } else
                return false;
        } catch (Exception e) {
            Log.d("DownloadManager", "Error: " + e);
        }

        return false;

    }

    public static File saveBitmapToCheckerFolder(Bitmap finalBitmap, String d) {
        if (finalBitmap == null)
            return null;
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fileName = "Image-" + n + ".jpg";

        File root = CheckerApp.localFilesDir;//Environment.getExternalStorageDirectory();

        File dir = new File(root.getAbsolutePath() + "/checker_signatures/");
        if (dir.exists() == false) {
            dir.mkdirs();
        }

        File file = new File(dir, fileName);
        ExifInterface exif;
        if (d != null) {
            try {
                exif = new ExifInterface(file.getAbsolutePath());
                exif.setAttribute(ExifInterface.TAG_DATETIME, d.toString());
                exif.saveAttributes();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(CompressFormat.JPEG, 60, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (finalBitmap != null && !finalBitmap.isRecycled())
            finalBitmap.recycle();
        finalBitmap = null;

        return file;
    }

    public void DownloadFromUrl(String DownloadUrl, String fileName) {

        if (DownloadUrl.contains("womanicon")) {
            int i = 0;
            i++;
        }

        try {

            File root = CheckerApp.localFilesDir;//Environment.getExternalStorageDirectory();
            String path = root.getAbsolutePath() + "/mnt/sdcard/Checker";
            File dir = new File(path);
            if (dir.exists() == false) {
                dir.mkdirs();
            }

            URL url = new URL(DownloadUrl); // you can write here any link
            File file = new File(dir, fileName);
            if (file.exists()) {
                int k = 0;
                k++;
                return;
            }

            long startTime = System.currentTimeMillis();
            Log.d("DownloadManager", "download begining");
            Log.d("DownloadManager", "download url:" + url);
            Log.d("DownloadManager", "downloaded file name:" + fileName);

            /* Open a connection to that URL. */
            URLConnection ucon = url.openConnection();

            /*
             * Define InputStreams to read from the URLConnection.
             */
            InputStream is = ucon.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);

            /*
             * Read bytes to the Buffer until there is nothing more to read(-1).
             */
            ByteArrayBuffer baf = new ByteArrayBuffer(5000);
            int current = 0;
            while ((current = bis.read()) != -1) {
                baf.append((byte) current);
            }

            /* Convert the Bytes read to a String. */
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baf.toByteArray());
            fos.flush();
            fos.close();

            Log.d("DownloadManager",
                    "download ready in"
                            + ((System.currentTimeMillis() - startTime) / 1000)
                            + " sec");

        } catch (IOException e) {
            Log.d("DownloadManager", "Error: " + e);
        }

    }

    public Bitmap readFile(String fileName, boolean isnewPAth) {
        Bitmap bitmap = null;
        try {
            String photoPath;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            File root = CheckerApp.localFilesDir;//Environment.getExternalStorageDirectory();
            String path = root.getAbsolutePath() + "/mnt/sdcard/Checker";
            // if (isnewPAth)
            // path = Constants.DOWNLOAD_PATH;
            File dir = new File(path);
            File file = new File(dir, fileName);
            if (!file.exists())
                return bitmap;
            photoPath = file.getAbsolutePath();

            bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return bitmap;
    }

    public String getVideoPath(String fileName) {
        File root = CheckerApp.localFilesDir;//Environment.getExternalStorageDirectory();
        File dir = new File(root.getAbsolutePath() + "/mnt/sdcard/Checker");
        File file = new File(dir, fileName);
        if (!file.exists()) {
            return null;
        }
        return file.getAbsolutePath();
    }

    public static String getCompanyLink() {
        return companyId;
    }

    public static void setCompanyLink(String id) {
        companyId = id;
    }

    public static String getSystemURL() {
        if (alternateURL != null && alternateURL.length() > 0) return alternateURL;
        return systemURL;
        // return "http://www.checker-soft.com/testing/";
    }

    public static String getSystemURLfromDB() {
        return systemURL;
        // return "http://www.checker-soft.com/testing/";
    }

    public static String stripHtml(String html) {
        // return html;
        return Html.fromHtml(html).toString();
    }

    public static String getAlternateSystemURL() {
        return alternateURL;
    }

    public static void setAlternateSystemURL(String string) {
        alternateURL = string;
    }

    public static void setSystemURL(String url) {
        systemURL = url;
    }

    public static int getTheme(Context ctx) {
        try {
            SharedPreferences myPrefs;
            myPrefs = ctx.getSharedPreferences("pref", Context.MODE_PRIVATE);
            theme = myPrefs.getInt(Constants.SETTINGS_MODE_INDEX, 1);
            // if (theme == 0)
            // return 1;
            // else
            // return 0;
        } catch (Exception ex) {
            return 1;
        }
        return 1;
    }

    public static void setTheme(int Theme) {
        theme = 1;
    }

    public static boolean isParsed() {
        return isParsed;
    }

    public static void setParsed(boolean isparsed) {
        isParsed = isparsed;
    }

    public static String getValidString(String temp) {
        if (temp == null || temp.equals(""))
            return "";
        return temp;
    }

    public static int getInt(String temp) {
        if (temp == null || temp.equals(""))
            return 0;
        try {
            return Integer.parseInt(temp);
        } catch (Exception ex) {
            return 0;
        }
    }

    public static int getIntColor(String temp) {
        try {
            if (temp == null || temp.equals(""))
                return Color.BLACK;
            return Color.parseColor(temp);
        } catch (Exception e) {
            // TODO: handle exception
            return Color.BLACK;
        }
    }

    public static float getFloat(String temp) {
        if (temp == null || temp.equals(""))
            return 0f;
        return Float.parseFloat(temp);
    }

    public static boolean comapreString(String str1, String str2) {
        if (str1 != null && str1.equals(str2)) {
            return true;
        }
        return false;
    }

    public static boolean IsEmptyString(String str1) {
        if (str1 == null || str1.equals("")) {
            return true;
        }
        return false;
    }

    public static String getValidURL(String url) {
        url = url.toLowerCase();
        if (url.indexOf(Constants.HTTP_FIELD_PARAM) == -1
                && url.indexOf(Constants.HTTPS_FIELD_PARAM) == -1) {
            url = Constants.HTTP_FIELD_PARAM + url;
        }
        if (url.indexOf(".php") != -1) {
            String[] strarr = url.split("/");
            String val = strarr[strarr.length - 1];
            url = url.replace("/" + val, "");
        }
        return url.trim();
    }

    public static String getValidURLForWebView(String url) {
        //url = url.toLowerCase();
//		if (url.indexOf(Constants.HTTP_FIELD_PARAM) == -1
//				&& url.indexOf(Constants.HTTPS_FIELD_PARAM) == -1) {
//			url = Constants.HTTP_FIELD_PARAM + url;
//		}
//		if (url.indexOf(".php") != -1) {
//			String[] strarr = url.split("/");
//			String val = strarr[strarr.length - 1];
//			url = url.replace("/" + val, "");
//		}
        return url.trim();
    }

    public static boolean IsValidOrder(Order order, String region,
                                       String project, String bprop, String bcode, String jtype,
                                       String city, String date1, String date3,
                                       String select, String bcode_name, String city_name, String bprop_name, String region_name, String project_name, String jtype_name) {
        boolean isAdd = false;
        if (order.getOrderID().contains("-"))
            return true;

        if (date1.equals("1/1/1900") && date3.equals("1/1/1900"))
            isAdd = true;
        else if (isBetweenDates(order.getDate(), date1, date3))
            isAdd = true;
        else {
            isAdd = false;
            return isAdd;
        }
        if (bcode.equals(bcode_name))
            isAdd = true;
        else if (order.getBranchCode().equals(bcode))
            isAdd = true;
        else {
            isAdd = false;
            return isAdd;
        }
        if (bprop.equals(bprop_name))
            isAdd = true;
        else if (Orders.isBranchPropExists(order.getBranchLink(), bprop))
            isAdd = true;
        else {
            isAdd = false;
            return isAdd;
        }
        if (region.equals(region_name))
            isAdd = true;
        else if (order.getRegionName().equals(region))
            isAdd = true;
        else {
            isAdd = false;
            return isAdd;
        }
        if (project.equals(project_name))
            isAdd = true;
        else if (order.getProjectName() != null
                && order.getProjectName().equals(project))
            isAdd = true;
        else {
            isAdd = false;
            return isAdd;
        }
        if (city.equals(city_name))
            isAdd = true;
        else if (order.getCityName().equals(city))
            isAdd = true;
        else {
            isAdd = false;
            return isAdd;
        }
        if (jtype.equals(jtype_name))
            isAdd = true;
        else if (order.getClientName().equals(jtype))
            isAdd = true;
        else {
            isAdd = false;
            return isAdd;
        }
        return isAdd;
    }

    private static boolean isBetweenDates(String oderdate, String date1,
                                          String date3) {
        if (date1.equals(""))
            return false;
        Date d1, d2, d3;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            d2 = sdf.parse(oderdate);
            sdf = new SimpleDateFormat("dd/MM/yyyy");
            d1 = sdf.parse(date1);
            d3 = sdf.parse(date3);
            if ((d1.before(d2) && d2.before(d3)) || d1.equals(d2)
                    || d2.equals(d3))
                return true;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    public static boolean IsValidObject(String str1) {
        if (str1 == null || str1.equals("") || str1.equals("1")
                || str1.equals("6") || str1.equals("5")) {
            return false;
        }
        return true;
    }

    public static boolean AuthenticateLoginResponse(String value) {
        if (value != null && value.equals("1")) {
            return true;
        }
        return false;
    }

    public static boolean getProblemMessage(String value) {
        if (value != null && value.equals("1")) {
            return true;
        }
        return false;
    }

    public static boolean IsValidResponse(String value, String tag) {
        if (value != null && value.indexOf(tag) != -1) {
            return true;
        }
        return false;
    }

    // public static boolean IsValidResponse(String result, String tag)
    // {
    // if(!IsLoginResponse(result, tag))
    // {
    // ShowAlert(LoginActivity.this, getString(R.string.error_alert_title),
    // getString(R.string.login_invalid_server_response_alert),
    // getString(R.string.alert_btn_lbl_ok));
    // return;
    // }
    // }

    public static BasicNameValuePair getNameValuePair(String key, String value) {
        return new BasicNameValuePair(key, value);
    }

    public static ArrayList<Answers> getSortData(int index,
                                                 ArrayList<Answers> listAnswers) {
        int count = listAnswers.size();
        for (int i = count / 2; i >= 0; i--)
            /* buildHeap */
            BuildHeap(listAnswers, i, count, index);

        for (int i = count - 1; i > 0; i--) {
            swapReferences(listAnswers, 0, i); // deleteMax
            BuildHeap(listAnswers, 0, i, index);
        }

        if (index == 1 || index == 4 || index == 6) {
            ArrayList<Answers> dtable = new ArrayList<Answers>();
            for (int anscount = listAnswers.size() - 1; anscount >= 0; anscount--) {
                Answers temp = listAnswers.get(anscount);
                dtable.add(temp);
            }
            listAnswers = dtable;
            dtable = null;
        }
        return listAnswers;
    }

    public static void swapReferences(ArrayList<Answers> tcArray, int index1,
                                      int index2) {
        Answers tmp = tcArray.get(index1);
        tcArray.set(index1, tcArray.get(index2));
        tcArray.set(index2, tmp);
    }

    private static void BuildHeap(ArrayList<Answers> tcArray, int i, int n,
                                  int index) {
        int child;
        Answers tmp;
        for (tmp = tcArray.get(i); leftChild(i) < n; i = child) {
            child = leftChild(i);
            if (index == 0 || index == 1) {
                if (child + 1 != tcArray.size()) {
                    if (child != n - 1
                            && tcArray
                            .get(child)
                            .getValue()
                            .compareTo(
                                    tcArray.get(child + 1).getValue()) < 0)
                        child++;
                }
                if (tmp.getValue().compareTo(tcArray.get(child).getValue()) < 0)
                    tcArray.set(i, tcArray.get(child));
                else
                    break;
            } else if (index == 3 || index == 4) {
                if (child + 1 != tcArray.size()) {
                    if (child != n - 1
                            && tcArray
                            .get(child)
                            .getAnswer()
                            .compareTo(
                                    tcArray.get(child + 1).getAnswer()) < 0)
                        child++;
                }
                if (tmp.getAnswer().compareTo(tcArray.get(child).getAnswer()) < 0)
                    tcArray.set(i, tcArray.get(child));
                else
                    break;
            } else if (index == 5 || index == 6) {
                if (tcArray.get(child).getAnswerCode() != null
                        && tcArray.get(child + 1).getAnswerCode() != null) {

                    if (child + 1 != tcArray.size()) {
                        if (child != n - 1
                                && tcArray
                                .get(child)
                                .getAnswerCode()
                                .compareTo(
                                        tcArray.get(child + 1)
                                                .getAnswerCode()) < 0)
                            child++;
                    }
                    break;
                }
                if (tmp.getAnswerCode() != null) {
                    if (tmp.getAnswerCode().compareTo(
                            tcArray.get(child).getAnswerCode()) < 0)
                        tcArray.set(i, tcArray.get(child));
                    else {
                        break;
                    }
                    break;
                }
            } else if (index == 2) {

                // Random Number
            }
        }
        tcArray.set(i, tmp);
    }

    private static int leftChild(int i) {
        return 2 * i + 1;
    }

    public static int getViewWidth(Context ct) {
        Display display = ((WindowManager) (ct
                .getSystemService(Context.WINDOW_SERVICE))).getDefaultDisplay();
        int width = display.getWidth();
        return width / 4;
    }

    public static ArrayList<QuestionnaireData> getQuestionnaireData() {
        return questionnaireData;
    }

    public static void setQuestionnaireData(
            ArrayList<QuestionnaireData> questionnaireData) {
        Helper.questionnaireData = questionnaireData;
    }

    public static void setConfigChange(boolean b) {
        configChange = b;

    }

    public static boolean getConfigChange() {
        return configChange;

    }

    public static void setDBPath(String link) {
        link = link.replace("https", "http");
        link = link.replace("/", "_");
        link = link.replace(".", "_");
        link = link.replace(":", "_");
        dbPath = link;
    }

    public static String getDBPath() {
        String link = dbPath;
        link = link.replace("/", "_");
        link = link.replace(".", "_");
        link = link.replace(":", "_");
        link = link.replace("https", "http");
        return link;
    }

    public static String getDBPathLink() {
        String link = dbPath;
        return link;
    }

    public static void setDBPathLink(String p) {
        dbPath = p;
    }

    public static void setExpressionWatcher(EditText textbox, String string) {

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
                new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    // public static void customCrashAlert(final Context context,
    // final String textString, final ParseObject gameScore) {
    // final Dialog dialog = new Dialog(context);
    // dialog.setContentView(R.layout.custom_crash_alert);
    //
    // // set the custom dialog components - text, image and button
    // TextView text = (TextView) dialog.findViewById(R.id.textView1);
    // text.setText(textString);
    //
    // final Button dialogButton = (Button) dialog.findViewById(R.id.btnOk);
    //
    // final EditText editCrash = (EditText) dialog
    // .findViewById(R.id.editcrash);
    // // if button is clicked, close the custom dialog
    // dialogButton.setOnClickListener(new OnClickListener() {
    // @Override
    // public void onClick(View v) {
    // gameScore.put("user_comment", editCrash.getText().toString()
    // + "");
    // dialogButton.setVisibility(RelativeLayout.INVISIBLE);
    // gameScore.saveInBackground(new SaveCallback() {
    //
    // @Override
    // public void done(com.parse.ParseException e) {
    // if (e == null) {
    // // Saved successfully.
    // String id = gameScore.getObjectId();
    // customAlert(context, "Issue saved with id:" + id);
    // } else {
    // // The save failed.
    // customAlert(context, "" + e.getMessage());
    // }
    // dialog.dismiss();
    // }
    // });
    // }
    // });
    // dialog.getWindow().setBackgroundDrawable(
    // new ColorDrawable(android.graphics.Color.TRANSPARENT));
    // dialog.show();
    // }
    //
    // public static ParseFile TakeScreenShot(View rootView) {
    // rootView.setDrawingCacheEnabled(true);
    // Bitmap bmp = rootView.getDrawingCache();
    // if (bmp != null) {
    // ByteArrayOutputStream stream = new ByteArrayOutputStream();
    // bmp.compress(CompressFormat.PNG, 50, stream);
    // ParseFile pFile = new ParseFile("screen.png", stream.toByteArray());
    // return pFile;
    // }
    // return null;
    // }

    static int sampleSize = 0;

    public static int getSampleSize() {
        // 1 original
        // 2 medium
        // 3 small
        // 4 ask
        if (sampleSize >= 3)
            return sampleSize - 3;
        return sampleSize;
    }

    public static int getOriginalSampleSize() {
        // 1 original
        // 2 medium
        // 3 small
        // 4 ask

        return sampleSize;
    }

    public static void setSampleSize(int sample) {
        // 1 original
        // 2 medium
        // 3 small
        // 4 ask

        sampleSize = sample;
    }

    private static int getColorCode() {
        // TODO Auto-generated method stub
        return 0;
    }

    public static void changeTobBoardColor(View view) {
        if (Helper.getSystemURL() != null
                && Helper.getSystemURL().toLowerCase().contains(CONST_BE_THERE)) {
            if (view != null) {
                view.setBackgroundColor(Color.parseColor(btnInsideColor));
                // view.setTextColor(Color.parseColor(btnInsideColor));
            }

        }
    }

    public static void changeBtnColor(Button view) {
        if (Helper.getSystemURL() != null
                && Helper.getSystemURL().toLowerCase().contains(CONST_BE_THERE)) {
            if (view != null) {
                view.setBackgroundColor(Color.parseColor(appColor));
                view.setTextColor(Color.parseColor(btnInsideColor));
            }

        }
    }

    public static void changeReleiveLayoutBG(View view) {
        if (Helper.getSystemURL() != null
                && Helper.getSystemURL().toLowerCase().contains(CONST_BE_THERE)) {
            if (view != null) {
                view.setBackgroundColor(Color.parseColor(appColor));
            }
        }
    }

    public static void changeImageViewSrc(ImageView view, Context cntxt) {
        if (Helper.getSystemURL() != null
                && Helper.getSystemURL().toLowerCase().contains(CONST_BE_THERE)) {
            if (view != null) {

                String ImageName = (String) view.getTag();
                ImageName = imgprefix + ImageName;
                int newResImgId = cntxt.getResources().getIdentifier(ImageName,
                        "drawable", cntxt.getPackageName());
                if (newResImgId != 0) {
                    view.setImageResource(newResImgId);
                }
                // view.setBackgroundColor(Color.parseColor("#d20024"));
            }
        }
    }

    public static String getappColor() {
        if (Helper.getSystemURL() != null
                && Helper.getSystemURL().toLowerCase()
                .contains(Helper.CONST_BE_THERE)) {
            return appColor;
        } else {
            return "#94BA09";
        }
    }

    public static String getunselectedappColor() {
        if (Helper.getSystemURL() != null
                && Helper.getSystemURL().toLowerCase()
                .contains(Helper.CONST_BE_THERE)) {
            return unselectappColor;
        } else {
            return "#CFDA9A";
        }
    }

    public static int getImageID(String ImageName, Context cntxt) {
        if (ImageName != null) {
            if (Helper.getSystemURL() != null
                    && Helper.getSystemURL().toLowerCase()
                    .contains(CONST_BE_THERE)) {
                String thisImageName = imgprefix + ImageName;
                int newResImgId = cntxt.getResources().getIdentifier(
                        thisImageName, "drawable", cntxt.getPackageName());
                if (newResImgId != 0) {
                    return newResImgId;
                } else {
                    newResImgId = cntxt.getResources().getIdentifier(ImageName,
                            "drawable", cntxt.getPackageName());
                    return newResImgId;
                }
            } else {
                int newResImgId = cntxt.getResources().getIdentifier(ImageName,
                        "drawable", cntxt.getPackageName());
                if (newResImgId != 0) {
                    return newResImgId;
                } else
                    return 0;
            }
        }
        return 0;
    }

    public static void changeTxtViewColor(TextView view) {
        if (Helper.getSystemURL() != null
                && Helper.getSystemURL().toLowerCase().contains(CONST_BE_THERE)) {
            if (view != null) {
                view.setTextColor(Color.parseColor(appColor));
            }
        }
    }

    public static void changeTextViewVisibilty(TextView view) {
        if (Helper.getSystemURL() != null
                && Helper.getSystemURL().toLowerCase().contains(CONST_BE_THERE)) {
            if (view != null) {
                view.setVisibility(View.VISIBLE);
            }
        }
    }

    public static void changeBtnVisibilty(Button view) {
        if (Helper.getSystemURL() != null
                && Helper.getSystemURL().toLowerCase().contains(CONST_BE_THERE)) {
            if (view != null) {
                view.setVisibility(View.VISIBLE);
            }
        }
    }

    public static void changeTxtViewBG(TextView view) {
        if (Helper.getSystemURL() != null
                && Helper.getSystemURL().toLowerCase().contains(CONST_BE_THERE)) {
            if (view != null) {
                view.setBackgroundColor(Color.parseColor("#ffffff"));
            }
        }
    }

    public static void changeTxtViewBothColors(TextView view) {
        if (Helper.getSystemURL() != null
                && Helper.getSystemURL().toLowerCase().contains(CONST_BE_THERE)) {
            if (view != null) {
                view.setBackgroundColor(Color.parseColor(appColor));
                view.setTextColor(Color.parseColor(btnInsideColor));
            }
        }
    }

    public static String getLoaderIconName() {
        if (Helper.getSystemURL() != null
                && Helper.getSystemURL().toLowerCase().contains(CONST_BE_THERE)) {
            return "wbt_loader";
        } else
//            return "loader";
            return "newloader";
    }

    public static String getThemeName(Context cntxt) {
        if (Helper.getSystemURL() != null
                && Helper.getSystemURL().toLowerCase().contains(CONST_BE_THERE)) {
            return cntxt.getResources().getString(R.string.default_theme);
        } else
            return cntxt.getResources().getString(R.string.default_theme);
    }

    public static int getLoaderColor() {
        if (Helper.getSystemURL() != null
                && Helper.getSystemURL().toLowerCase().contains(CONST_BE_THERE)) {
            return loaderOuterColor;
        } else
            return 0xFF305699;
    }

    public static void changeViewColor(View view) {
        if (Helper.getSystemURL() != null
                && Helper.getSystemURL().toLowerCase().contains(CONST_BE_THERE)) {
            if (view != null) {
                view.setBackgroundColor(Color.parseColor(appColor));
                // view.setTextColor(Color.parseColor("#d20024"));
            }
        }
    }

    public static void changeToggleColor(ToggleButton view) {
        if (Helper.getSystemURL() != null
                && Helper.getSystemURL().toLowerCase().contains(CONST_BE_THERE)) {
            if (view != null) {

                view.setBackgroundColor(Color.parseColor(appColor));
            }
        }
    }

    public static boolean isUploadingEnabled() {
        // TODO Auto-generated method stub
        return true;
    }

    public static void setAN168Log(String message) {
        try {
            File Root = CheckerApp.localFilesDir;//Environment.getExternalStorageDirectory();
            // if (Root.canWrite())
            BufferedWriter out;

            Root = new File(Root.getPath() + "/com.checker.Logs/");
            if (!Root.exists())
                Root.mkdir();
            FileWriter LogWriter;

            LogWriter = new FileWriter(Root.getPath() + "/" + "nlogs.csv", true);
            out = new BufferedWriter(LogWriter);
            out.write(System.currentTimeMillis() + "   " + message
                    + "\n");
            out.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public static void changeToggleButtonColor(ToggleButton view, boolean isToggleOn) {
        if (Helper.getSystemURL() != null
                && Helper.getSystemURL().toLowerCase().contains(CONST_BE_THERE)) {
            if (view != null) {
                if (isToggleOn) {

                } else {

                }
            }
        }
    }

}
