package com.checker.sa.android.helper;

import com.checker.sa.android.data.Order;
import com.checker.sa.android.data.orderListItem;
import com.google.android.gms.maps.model.LatLng;
import com.mor.sa.android.activities.JobListActivity;
import com.mor.sa.android.activities.QuestionnaireActivity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;

import java.util.ArrayList;
import java.util.Locale;

public class Constants {

    public static double user_location_latitude;
    public static double user_location_longitude;

    public static ArrayList<orderListItem> orderList;
    public static String activity = "";
    public static int screen_type_dialog = -1;
    public static String accept_txt = "";
    public static String select_jobs = "";
    public static int applied_count = 0;

    public static String UPLOAD_PATH = "/data/data/com.mor.sa.android.activity/uploads/";
    public static String DOWNLOAD_PATH = "/data/data/com.mor.sa.android.activity/downloadfiles/";
    public static boolean isQAAllowed = false;
    public static int proxyPort = -1;
    public static String proxyUsername = null;
    public static String proxyPassword = null;
    public static String proxyHost = null;
    private static boolean fullBranchName;
    private static boolean isDateFilter;

    public static int getProxyPort() {
        return Constants.proxyPort;
    }

    public static void setProxyPort(int proxyp) {
        Constants.proxyPort = proxyp;
    }

    public static void setProxyUsername(String proxyp) {
        Constants.proxyUsername = proxyp;
    }

    public static void setProxyPassword(String proxyp) {
        Constants.proxyPassword = proxyp;
    }

    public static String getProxyHost() {
        return Constants.proxyHost;
    }

    public static String getProxyUSername() {
        return Constants.proxyUsername;
    }

    public static String getProxyPassword() {
        return Constants.proxyPassword;
    }

    public static void setProxyHost(String proxyh) {
        Constants.proxyHost = proxyh;
    }

    public static void setFullBranchName(boolean is) {
        Constants.fullBranchName = is;
    }

    public static boolean getFullBranchName() {
        return Constants.fullBranchName;
    }

    public static void setDateFilter(boolean is) {
        Constants.isDateFilter = is;
    }

    public static boolean getDateFilter() {
        return Constants.isDateFilter;
    }

    public static String getLoginURL() {
        return Helper.getSystemURL() + "/c_login.php";
    }

    public static String getJobListURL(String ver) {
        String url = Helper.getSystemURL()
                + "/c_pda-ordered-crits.php?app=1&ver=11.52";
        // 10.47 abve for in progress api

        Log.e("TAG", "getJobListURL  Jobbbbbb: " + url);
        return url;
    }

    public static String getInProgressJobsURL(String ver) {
        // http://checker.co.il/testing/c_pda-job-board.php?ver=9.0&json=1&date_start=2016-12-26&date_end=2016-12-29&lat1=28.644800&long1=77.216721&lat2=31.771959&long2=78.217018
        String url = Helper.getSystemURL()
                + "/c_pda-unfinished-reviews.php?app=1&ver=" + ver;
        Log.e("TAG", "getInProgressJobsURL: " + url);
        return url;
    }//https://eu.checker-soft.com/testing/c_pda-unfinished-reviews.php?app=1&ver=12.93

    public static String getRefundListURL(String ver, int year, int month) {
        String url = Helper.getSystemURL() + "/c_pda-refund-list.php?ver="
                + ver + "&month=" + month + "&year=" + year;
        return url;
    }

    public static String getCritHistoryURL(String ver, String date) {
        String url = Helper.getSystemURL()
                + "/c_pda-crit-history.php?app=1&date=" + date;
        return url;
    }

    public static String getBoardListURL(String ver, String start_date,
                                         String end_date, String lat1, String long1, String lat2,
                                         String long2) {
        // http://checker-soft.com/testing//c_pda-job-board.php?ver=11.0&date_start=2018-12-10&date_end=2018-12-29&lat1=-1000.0&long1=-1000.0&lat2=1000.0&long2=1000.0
        String url = Helper.getSystemURL() + "/c_pda-job-board.php?json=1&ver="
                + ver + "&date_start=" + start_date + "&date_end=" + end_date
                + "&lat1=" + lat1 + "&lat2=" + lat2 + "&long1=" + long1
                + "&long2=" + long2;
        return url;
    }

    public static String getCheckerTificates(String ver, boolean showhidden) {
        // http://checker.co.il/testing/c_pda-job-board.php?ver=9.0&json=1&date_start=2016-12-26&date_end=2016-12-29&lat1=28.644800&long1=77.216721&lat2=31.771959&long2=78.217018
        String url = Helper.getSystemURL() + "/c_pda-checkertificates.php?ver="
                + ver;
        if (showhidden) url += "&showhidden=1";
        return url;
    }

    public static String getBoardApplyURL(String ver) {
        // http://checker.co.il/testing/c_pda-job-board.php?ver=9.0&json=1&date_start=2016-12-26&date_end=2016-12-29&lat1=28.644800&long1=77.216721&lat2=31.771959&long2=78.217018
        String url = Helper.getSystemURL()
                + "/c_pda-apply-order.php?json=1&ver=" + ver;

        Log.e("TAG", "getBoardApplyURL: " + url);
        return url;
    }

    public static String getBoardRemoveURL(String ver) {
        // http://checker.co.il/testing/c_pda-job-board.php?ver=9.0&json=1&date_start=2016-12-26&date_end=2016-12-29&lat1=28.644800&long1=77.216721&lat2=31.771959&long2=78.217018
        String url = Helper.getSystemURL()
                + "/c_pda-apply-order.php?json=1&ver=" + ver + "&remove=1";
        return url;
    }

    public static String getRefundTypeListURL(String ver) {
        String url = Helper.getSystemURL()
                + "/c_pda-refund-types-list.php?ver=" + ver;
        return url;
    }

    public static String getLangListURL(String ver) {
        String url = Helper.getSystemURL()
                + "/c_pda-lang-list.php?app=1&ver=7.90";
        return url;
    }

    public static String getAlternateJobsURL(String ver, String isUpdating,
                                             String orderid) {
        String url = Helper.getSystemURL()
                + "/c_om-change-order-branch.php?app=1&OrderID=" + orderid
                + "&update=" + isUpdating;
        return url;
    }

    public static String getCritHistoryURL(String ver) {
        String url = Helper.getSystemURL() + "/c_pda-crit-history.php?app=1";
        return url;
    }

    public static String assignAlternateJobsURL(String ver, String isUpdating,
                                                String OldOrderid, String NewOrderid) {
        String url = Helper.getSystemURL()
                + "/c_om-change-order-branch.php?app=1&OrderID=" + OldOrderid
                + "&update=" + isUpdating + "&assign_order=" + NewOrderid;
        return url;
    }

    public static String assignAlternateDateJobsURL(String ver,
                                                    String isUpdating, String OldOrderid, String date) {
        String url = Helper.getSystemURL() + "/c_pda-change-order.php";
        return url;
    }

    public static String getcheckConnectionURL(String nurl) {
        String url = nurl + "/c_pda-check-connection.html";
        Log.e("TAG", "getcheckConnectionURL: " + url);
        return url;
    }

    public static String getcheckConnectionURL() {
        String url = Helper.getSystemURL() + "/c_pda-check-connection.html";
        Log.e("TAG", "getcheckConnectionURL: " + url);

        return url;
    }

    public static String getFieldListURL(String ver) {

        return Helper.getSystemURL()
                + "/c_pda-ordered-surveys.php?app=1&ver=11.18";
    }

    public static String getQestionnaireURL(String ver) {
        String str = Helper.getSystemURL()
                + "/c_pda-ordered-sets.php?app=1&ver=6.51";
        Log.e("TAG", "getQestionnaireURL: " + str);
        return str;
    }

    public static String getQestionnaireOneByOneURL(String ver, String setid, String orderids) {

        String str = Helper.getSystemURL()
                + "/c_pda-ordered-sets.php?app=1&ver=12.50&SetID=" + setid
                + "&OrderID=" + orderids;
        if (orderids == null || orderids.equals(""))
            str = Helper.getSystemURL()
                    + "/c_pda-ordered-sets.php?app=1&ver=12.50&SetID=" + setid;
        return str;
    }

    public static String getQestionnaireOneByOneURL(String ver, String setid,
                                                    String altid, String orderids) {
        String str = Helper.getSystemURL()
                + "/c_pda-ordered-sets.php?app=1&ver=12.50&SetID=" + setid
                + "&OrderID=" + orderids
                + "&LangID=" + altid;
        return str;
    }

    public static String getSubmitSurveyURL() {
        return Helper.getSystemURL() + "/c_pda-receive-criticism.php";
    }

    public static String getLocationURL() {
        Log.e("TAG", "getLocationURL: " + getLocationURL());
        return Helper.getSystemURL() + "/c_pda-receive-geo.php";
    }

    public static String getJobStartURL() {
        String url = Helper.getSystemURL() + "/c_pda-change-order.php";

        Log.e("TAG", "getJobStartURL: " + url);
        // url =
        // "https://dl.dropboxusercontent.com/u/13666396/checkerresponse.txt";
        return url;
    }

    public static String getAttachmentURL() {
        return Helper.getSystemURL() + "/c_pda-receive-file.php";
        //return "http://www.appicsol.com/sweeps_apis/uploadfile.php";
    }

    public static Boolean isPortrait = true;

    // LOGIN POST PARAMETERS
    public static final String FILE_EXPLORE_PATH = "file_explore_path";
    public static final String POST_FIELD_LOGIN_USERNAME = "username";
    public static final String POST_FIELD_LOGIN_RESPONSE = "RESPONSE";
    public static final String POST_FIELD_LOGIN_PASSWORD = "password";
    public static final String POST_FIELD_LOGIN_DO_LOGIN = "do_login";
    public static final String POST_FIELD_LOGIN_NO_REDIR = "noRedir";
    public static final String POST_FIELD_LOGIN_IS_APP = "is_app";
    public static final String POST_FIELD_LOGIN_SHOPPER_DETAIL = "shopperdetail";
    public static final String POST_VALUE_LOGIN_NO_REDIR = "1";
    public static final String POST_VALUE_LOGIN_IS_APP = "1";
    public static final String POST_VALUE_LOGIN_DO_LOGIN = "1";
    public static final String LOGIN_RESP_FIELD_PARAM = "login";
    public static final String LOGIN_RESP_FIELD_PARAM_LOGIN_TAG = "<login>";
    public static final String HTTP_FIELD_PARAM = "http://";
    public static final String HTTPS_FIELD_PARAM = "https://";
    public static final String ALREADY_LOGIN_STATUS = "already_login";
    public static final String ALREADY_BRANCHPROPERR = "branch_prop_Err";
    public static final String IS_LOGIN = "is_login";
    public static final String DOWNLOADIP = "DOWNLOADIP";

    // JOB DETAIL PARAMETERS
    public static final String POST_VALUE_JOB_DETAIL_ACCEPT = "accept";
    public static final String POST_VALUE_JOB_DETAIL_APPLY = "apply";
    public static final String POST_VALUE_JOB_DETAIL_REMOVE_APPICATION = "removeApplication";
    public static final String POST_VALUE_JOB_DETAIL_REJECT = "reject";
    public static final String POST_FIELD_JOB_DETAIL_REFUSAL_REASON = "RefusalReason";
    public static final String POST_FIELD_JOB_DETAIL_ORDER_ID = "OrderID";
    public static final String POST_FIELD_JOB_DETAIL_GROUPED_NUMBER = "GroupedNumber";

    public static final String POST_VALUE_JOB_DETAIL_PARAM_VALUE = "1";
    public static final String JOB_DETAIL_RESP_FIELD_PARAM = "<status>";
    public static final String JOB_DETAIL_JOB_START_FIELD_KEY = "Status";
    public static final String JOB_DETAIL_IS_REJECT_FIELD_KEY = "isReject";
    public static final String SELECT_FILE_PATH = "file_path";
    public static final String SELECT_FILE_PATH_ARRAY = "file_path_array";
    public static final String JOB_DETAIL_IS_INVALID_LOGIN_FIELD_KEY = "invalidLogin";

    // ORDER
    public static final String FIELD_ORDER_SET_ID = "SetID";
    public static final String JOB_LIST_RESP_FIELD_PARAM = "BranchProps";
    public static final String SURVEY_LIST_RESP_FIELD_PARAM = "data";
    public static final String SURVEY_LIST_RESP_FIELD_PARAM_OLD = "surveys";

    // LocationParams
    public static final String POST_FIELD_LOC_USERID = "checkername";
    public static final String POST_FIELD_LOC_LAT = "lat";
    public static final String POST_FIELD_LOC_LONG = "long";

    // PRODUCT POST PARAMETERS
    public static final String POST_FIELD_PROD = "prod";
    public static final String POST_FIELD_PROD_ID = "-ProductID";
    public static final String POST_FIELD_PROD_NAME = "-ProductName";
    public static final String POST_FIELD_PROD_CODE = "-ProductCode";
    public static final String POST_FIELD_PROD_LOCID = "-ProdLocationID";
    public static final String POST_FIELD_PROD_LOC = "-Location";
    public static final String POST_FIELD_PROD_QUANTITY = "-Quantity";
    public static final String POST_FIELD_PROD_PRICE = "-Price";
    public static final String POST_FIELD_PROD_NOTE = "-Note";
    public static final String POST_FIELD_PROD_EXPIRATION = "-Expiration";

    // LOOP FILLER
    public static final String POST_FIELD_LOOPS_STARTED = "loops";
    public static final String POST_FIELD_LOOPS_FINISHEd = "loop_finish";
    public static final String POST_FIELD_LOOPS_ITEMS = "items";
    public static final String POST_FIELD_LOOPS_NAME = "name";
    public static final String POST_FIELD_LOOPS_RESPONSES = "responses";

    // SURVEY FILLER
    public static final String POST_FIELD_QUOTA_SURVEY_ID = "SurveyID";
    public static final String POST_FIELD_QUOTA_QuotasToUpdate = "QuotasToUpdate";

    // QUESTIONNAIRE POST PARAMETERS
    public static final String POST_FIELD_SETID = "SetID";
    public static final String POST_FIELD_SETVERSIONID = "SetVersionID";
    public static final String POST_FIELD_CERT_ID = "CertID";
    public static final String POST_FIELD_UNEMPTY_QUES_COUNT = "TotalAnswersSent";
    public static final String POST_FIELD_DEVICE_INFO = "device_info";
    public static final String POST_FIELD_QUES_ORDER_ID = "OrderID";
    public static final String POST_FIELD_IS_ARCHIVE = "IS_ARCHIVE";
    public static final String POST_FIELD_QUES_UNIX = "UnixTimestamp";
    public static final String POST_FIELD_QUES_CRITFREETEXT = "CritFreeText";
    public static final String POST_FIELD_QUES_RS = "message_to_operation";
    public static final String POST_FIELD_QUES_CRITSTARTLAT = "CritStartLat";
    public static final String POST_FIELD_QUES_CRITSTARTLONG = "CritStartLong";
    public static final String POST_FIELD_QUES_CRITENDLAT = "CritEndLat";
    public static final String POST_VALUE_QUES_CRITENDLONG = "CritEndLong";
    public static final String POST_VALUE_QUES_REPORTED_START_TIME = "reported_StartTime";
    public static final String POST_VALUE_QUES_REPORTED_FINISH_TIME = "reported_FinishTime";
    public static final String POST_VALUE_QUES_APP_VERSION = "android_appversion";
    public static final String POST_VALUE_QUES_APP_ACTUAL_VERSION = "5.96";
    public static final String JOB_RESP_FIELD_PARAM = "jobs";
    public static final String INPROGRESS_RESP_FIELD_PARAM = "inprogress";
    public static final String REFUND_RESP_FIELD_PARAM = "refund";
    public static final String HIST_RESP_FIELD_PARAM = "history";
    public static final String REFUND_TYPES_RESP_FIELD_PARAM = "refundTypes";
    public static final String QUES_RESP_FIELD_PARAM = "sets";
    public static final String QUES_RESP_FIELD_VALIDATION_DATAID = "ValidationDataID";
    public static final String QUES_RESP_FIELD_VALIDATION_ANSWERID = "ValidationAnswerID";

    public static final String CERTS_FIELD_PARAM = "certificates";

    public static final String POST_purchase_PurchaseInvoiceNumber = "purchase_PurchaseInvoiceNumber";
    public static final String POST_purchase_PurchasePayment = "purchase_PurchasePayment";
    public static final String POST_purchase_PurchaseDescription = "purchase_PurchaseDescription";
    public static final String POST_purchase_ServiceInvoiceNumber = "purchase_ServiceInvoiceNumber";
    public static final String POST_purchase_ServicePayment = "purchase_ServicePayment";
    public static final String POST_purchase_ServiceDescription = "purchase_ServiceDescription";
    public static final String POST_transportation_TransportationPayment = "transportation_TransportationPayment";
    public static final String POST_transportation_Description = "transportation_Description";

    // VALIDATION POST PARAMETERS
    public static final String VALITION_QUESTION_DATA_TABLE = "ValidationQuestionTbl";
    public static final String VALITION_QUESTION_TABLE = "ValidationQuestion";
    public static final String VALITION_ANSWER_TABLE = "ValidationQuesAnswer";
    public static final String UPLOAD_FILE_TABLE = "UploadFile";
    public static final String UPLOAD_FILe_MEDIAFILE = "MediaFile";
    public static final String UPLOAD_FILe_DATAID = "DataID";
    public static final String UPLOAD_FILe_ORDERID = "OrderID";
    public static final String UPLOAD_FILe_CLIENT_NAME = "CLIENT_NAME";
    public static final String UPLOAD_FILe_BRANCH_NAME = "BranchName";
    public static final String UPLOAD_FILe_SET_NAME = "SetName";
    public static final String UPLOAD_FILe_SAMPLE_SIZE = "samplesize";
    public static final String UPLOAD_FILe_DATE = "Date_Taken";
    public static final String UPLOAD_FILe_PRODUCTID = "ProductID";
    public static final String UPLOAD_FILe_LOCATIONID = "LocationID";

    // Png Table
    public static final String PNG_FILE_TABLE = "PngFiles";
    public static final String PNG_FILe_MEDIAFILE = "MediaFile";
    public static final String PNG_FILe_DATAID = "DataID";
    public static final String PNG_FILe_ORDERID = "OrderID";
    public static final String PNG_FILe_SETID = "SetID";
    public static final String PREVIEW_ITEM = "PREVIEW_ITEM";
    // Crash reports

    public static final String Crash_Last_ORDERID = "OrderID";
    public static final String Crash_Last_SETID = "SetID";
    public static final String Crash_isAlreadyShown = "isAlreadyShown";
    // SETTINGS PAGE
    public static final String SETTINGS_SHOW_REPLY_BOX = "SETTINGS_SHOW_REPLY_BOX";
    public static final String SETTINGS_SYSTEM_URL_KEY = "SYSTEM_URL";
    public static final String SETTINGS_ALTERNATE_SYSTEM_URL_KEY = "ALTERNATE_SYSTEM_URL";
    public static final String SETTINGS_PROXY_HOST_KEY = "PROXY_HOST";
    public static final String SETTINGS_PROXY_PORT_KEY = "PROXY_PORT";
    public static final String SETTINGS_PROXY_HOST_USERNAME = "PROXY_USERNAME";
    public static final String SETTINGS_PROXY_HOST_PASSWORD = "PROXY_PASSWORD";
    public static final String SETTINGS_LANGUAGE_INDEX = "language_arr_index";
    public static final String SETTINGS_RESIZE_INDEX = "resize_arr_index";
    public static final String SETTINGS_LANGUAGE_INDEX_PREFFER = "language_arr_index_preffer";
    public static final String[] SETTINGS_LOCALE_VAL_ARR = {"en", "sp", "ro",
            "iw", "ja", "pt", "fr", "ru", "sv", "tr", "it", "cs", "hi", "bg",
            "zh", "km"};
    public static final String SETTINGS_MODE_INDEX = "setting_mode";
    public static final String AUTOSYNC_CURRENT_TIME = "current_time";
    public static final String IS_DOWNLOADED_FOR_NEW_DOWNLOAD_PATH = "IS_DOWNLOADED_FOR_NEW_DOWNLOAD_PATH";

    // QUESTIONNAIRE PAGE
    public static final String QUESTIONNAIRE_JUMPTO_LBL = "Jump to:";
    public static final String QUESTIONNAIRE_JUMPTO_NEXT_PAGE = "Next ";
    public static final String QUESTIONNAIRE_STAUS = "Status";
    // public static final String QUESTIONNAIRE_JUMPTO_NEXT_PAGE = "Next page";

    // DB FIELDS

    public static final String DB_TABLE_SUBMITSURVEY = "SubmitSurvey";
    public static final String DB_TABLE_SUBMITSURVEY_OID = "OrderID";
    public static final String DB_TABLE_SUBMITSURVEY_SID = "SurveyID";
    public static final String DB_TABLE_SUBMITSURVEY_FT = "FreeText";
    public static final String DB_TABLE_SUBMITSURVEY_RS = "ReplyToServer";
    public static final String DB_TABLE_SUBMITSURVEY_SLT = "StartLat";
    public static final String DB_TABLE_SUBMITSURVEY_SLNG = "StartLng";
    public static final String DB_TABLE_SUBMITSURVEY_ELT = "EndLat";
    public static final String DB_TABLE_SUBMITSURVEY_ELNG = "EndLng";
    public static final String DB_TABLE_SUBMITSURVEY_REPORTED_FINISH_TIME = "reported_FinishTime";
    public static final String DB_TABLE_SUBMITSURVEY_REPORTED_START_TIME = "reported_StartTime";
    public static final String DB_TABLE_SUBMITSURVEY_UNEMPTY_QUES_COUNT = "TotalAnswersSent";
    public static final String DB_TABLE_SUBMITSURVEY_purchase_details = "purchase_details";
    public static final String DB_TABLE_SUBMITSURVEY_purchase_payment = "purchase_payment";
    public static final String DB_TABLE_SUBMITSURVEY_purchase_description = "purchase_description";
    public static final String DB_TABLE_SUBMITSURVEY_service_invoice_number = "service_invoice_number";
    public static final String DB_TABLE_SUBMITSURVEY_service_payment = "service_payment";
    public static final String DB_TABLE_SUBMITSURVEY_service_description = "service_description";
    public static final String DB_TABLE_SUBMITSURVEY_transportation_payment = "transportation_payment";
    public static final String DB_TABLE_SUBMITSURVEY_transportation_description = "transportation_description";

    public static final String DB_TABLE_SUBMITQUOTA = "SubmitQuota";
    public static final String DB_TABLE_SUBMITQUOTA_OrderID = "OrderID";
    public static final String DB_TABLE_SUBMITQUOTA_QuotaID = "QuotaID";

    public static final String DB_TABLE_QUESTIONNAIRE = "Questions";
    public static final String DB_TABLE_QUESTIONNAIRE_DATAID = "DataID";
    public static final String DB_TABLE_QUESTIONNAIRE_ORDERID = "OrderID";
    public static final String DB_TABLE_QUESTIONNAIRE_QTEXT = "Qtext";
    public static final String DB_TABLE_QUESTIONNAIRE_QVALUE = "Qvalue";
    public static final String DB_TABLE_QUESTIONNAIRE_QTL = "QuestionTypeLink";
    public static final String DB_TABLE_QUESTIONNAIRE_OT = "ObjectType";
    public static final String DB_TABLE_QUESTIONNAIRE_FT = "FreeText";
    public static final String DB_TABLE_QUESTIONNAIRE_LoopInfo = "LoopInfo";

    public static final String DB_TABLE_AUTOVALUES = "AutoValues";
    public static final String DB_TABLE_AUTOVALUES_avID = "avID";
    public static final String DB_TABLE_AUTOVALUES_SetLink = "SetLink";
    public static final String DB_TABLE_AUTOVALUES_DataLink = "DataLink";
    public static final String DB_TABLE_AUTOVALUES_Priority = "Priority";
    public static final String DB_TABLE_AUTOVALUES_Condition = "Condition";
    public static final String DB_TABLE_AUTOVALUES_Value_AnswerCode = "Value_AnswerCode";

    public static final String DB_TABLE_ANSWERS = "Answers";
    public static final String DB_TABLE_ANSWERS_RANK = "rank";
    public static final String DB_TABLE_ANSWERS_ANSWERID = "AnswerID";
    public static final String DB_TABLE_ANSWERS_ATEXT = "Atext";
    public static final String DB_TABLE_ANSWERS_AVALUE = "Avalue";
    public static final String DB_TABLE_ANSWERS_DATAID = "DataID";
    public static final String DB_TABLE_ANSWERS_ORDERID = "OrderID";
    public static final String DB_TABLE_ANSWERS_MI = "Mi";
    public static final String DB_TABLE_ANSWERS_BRANCHID = "BranchID";
    public static final String DB_TABLE_ANSWERS_WORKERID = "WorkerID";
    public static final String DB_TABLE_ANSWERS_DISPLAYCONDITION = "answerDisplayCondition";
    public static final String DB_TABLE_ANSWERS_ENCRYPTED = "encrypted";

    public static final String DB_TABLE_POS = "POSParams";
    public static final String DB_TABLE_POS_ProductId = "ProductId";
    public static final String DB_TABLE_POS_LocationId = "LocationId";
    public static final String DB_TABLE_POS_PropertyId = "PropertyId";
    public static final String DB_TABLE_POS_Price = "Price";
    public static final String DB_TABLE_POS_Quantity = "Quantity";
    public static final String DB_TABLE_POS_SetId = "SetId";
    public static final String DB_TABLE_POS_OrderId = "OrderId";
    public static final String DB_TABLE_POS_Notee = "Notee";
    public static final String DB_TABLE_POS_date = "Expiration";

    public static final String DB_TABLE_ORDERS = "Orders";
    public static final String DB_TABLE_ORDERS_ORDERID = "OrderID";
    public static final String DB_TABLE_ORDERS_STATUS = "Status";
    public static final String DB_TABLE_ORDERS_LASTDATAID = "lastdid";
    public static final String DB_TABLE_ORDERS_START_TIME = "start_time";

    public static final String DB_TABLE_JOBLISTARCHIVE = "JobListArchive";//same fields but different name
    public static final String DB_TABLE_JOBLIST = "JobList";
    public static final String DB_TABLE_JOBLIST_AllowShopperToReject = "AllowShopperToReject";
    public static final String DB_TABLE_JOBLIST_ORDERID = "OrderID";
    public static final String DB_TABLE_JOBLIST_DATE = "Date";
    public static final String DB_TABLE_JOBLIST_SN = "StatusName";
    public static final String DB_TABLE_JOBLIST_DESC = "Description";
    public static final String DB_TABLE_JOBLIST_SETNAME = "SetName";
    public static final String DB_TABLE_JOBLIST_BRIEFING = "BriefingContent";
    public static final String DB_TABLE_JOBLIST_SETLINK = "SetLink";
    public static final String DB_TABLE_JOBLIST_CN = "ClientName";
    public static final String DB_TABLE_JOBLIST_BN = "BranchName";
    public static final String DB_TABLE_JOBLIST_BFN = "BranchFullName";
    public static final String DB_TABLE_JOBLIST_CITYNAME = "CityName";
    public static final String DB_TABLE_JOBLIST_ADDRESS = "Address";
    public static final String DB_TABLE_JOBLIST_BP = "BranchPhone";
    public static final String DB_TABLE_JOBLIST_OH = "OpeningHour";
    public static final String DB_TABLE_JOBLIST_TS = "TimeStart";
    public static final String DB_TABLE_JOBLIST_TE = "TimeEnd";
    public static final String DB_TABLE_JOBLIST_SETID = "SetID";
    public static final String DB_TABLE_JOBLIST_BL = "BranchLat";
    public static final String DB_TABLE_JOBLIST_BLNG = "BranchLang";
    public static final String DB_TABLE_JOBLIST_FN = "FullName";
    public static final String DB_TABLE_JOBLIST_JC = "JobCount";
    public static final String DB_TABLE_JOBLIST_JI = "JobIndex";
    public static final String DB_TABLE_JOBLIST_BLINK = "BranchLink";
    public static final String DB_TABLE_JOBLIST_MID = "MassID";
    public static final String DB_TABLE_CHECKER_CODE = "CheckerCode";
    public static final String DB_TABLE_CHECKER_LINK = "CheckerLink";
    public static final String DB_TABLE_BRANCH_CODE = "BranchCode";
    public static final String DB_TABLE_SETCODE = "SetCode";
    public static final String DB_TABLE_PURCHASE_DESCRIPTION = "PurchaseDescription";
    public static final String DB_TABLE_PURCHASE = "Purchase";
    public static final String DB_TABLE_JOBLIST_sPurchaseLimit = "sPurchaseLimit";
    public static final String DB_TABLE_JOBLIST_sNonRefundableServicePayment = "sNonRefundableServicePayment";
    public static final String DB_TABLE_JOBLIST_sTransportationPayment = "sTransportationPayment";
    public static final String DB_TABLE_JOBLIST_sCriticismPayment = "sCriticismPayment";
    public static final String DB_TABLE_JOBLIST_sBonusPayment = "sBonusPayment";
    public static final String DB_TABLE_JOBLIST_sinprogressonserver = "inprogressonserver";
    public static final String DB_TABLE_JOBLIST_sRegionName = "RegionName";
    public static final String DB_TABLE_JOBLIST_sProjectName = "ProjectName";
    public static final String DB_TABLE_JOBLIST_sProjectID = "ProjectID";
    public static final String DB_TABLE_JOBLIST_sdeletedjob = "deletedjob";

    public static final String DB_TABLE_SETS = "Sets";
    public static final String DB_TABLE_SETS_SETID = "SetID";
    public static final String DB_TABLE_SETS_AllowCheckerToSetLang = "AllowCheckerToSetLang";
    public static final String DB_TABLE_SETS_isDifferentLangsAvailable = "isDifferentLangsAvailable";
    public static final String DB_TABLE_SET_NAME = "SetName";
    public static final String DB_TABLE_SET_COMP_LINK = "CompanyLink";
    public static final String DB_TABLE_SET_DESC = "SetDesc";
    public static final String DB_TABLE_SET_CODE = "SetCode";
    public static final String DB_TABLE_SET_SHOWSAVEANDEXIT = "Showsaveandexit";
    public static final String DB_TABLE_SET_SHOWTOC = "ShowToc";
    public static final String DB_TABLE_SET_SHOWPREVIEW = "Showpreview";
    public static final String DB_TABLE_SET_SHOWBACK = "showback";
    public static final String DB_TABLE_SET_CLIENTNAME = "ClientName";
    public static final String DB_TABLE_SET_SHOWFREETEXT = "Showfreetext";
    public static final String DB_TABLE_SET_ENABLE_NON_ANSWERED_CONFIRMATION = "EnableNonansweredConfirmation";
    public static final String DB_TABLE_SET_ENABLE_QUESTION_NUMBERING_INFORM = "EnableQuestionNumberingInForm";
    public static final String DB_TABLE_SET_ENABLE_VALIDATION_QUESTION = "EnableValidationQuestion";
    public static final String DB_TABLE_SET_ALLOW_CHECKER_TO_SET_FINISHTIME = "AllowCheckerToSetFinishTime";
    public static final String DB_TABLE_SET_ALLOW_CRIT_FILE_UPLOAD = "AllowCritFileUpload";
    public static final String DB_TABLE_SET_ALTLANG_ID = "AltLangID";
    public static final String DB_TABLE_SET_ANSWERS_ACT_AS_SUBMIT = "AnswersActAsSubmit";
    public static final String DB_TABLE_SET_DefaultPaymentForChecker = "DefaultPaymentForChecker";
    public static final String DB_TABLE_SET_DefaultBonusPayment = "DefaultBonusPayment";
    public static final String DB_TABLE_SET_AskForServiceDetails = "AskForServiceDetails";
    public static final String DB_TABLE_SET_AskForPurchaseDetails = "AskForPurchaseDetails";
    public static final String DB_TABLE_SET_AskForTransportationDetails = "AskForTransportationDetails";
    public static final String DB_TABLE_SET_AutoApproveTransportation = "AutoApproveTransportation";
    public static final String DB_TABLE_SET_AutoApprovePayment = "AutoApprovePayment";
    public static final String DB_TABLE_SET_AutoApproveService = "AutoApproveService";
    public static final String DB_TABLE_SET_AutoApprovePurchase = "AutoApprovePurchase";
    public static final String DB_TABLE_SET_ForceStamping = "ForceStamping";

    public static final String DB_TABLE_QUES = "Questionnaire";
    public static final String DB_TABLE_QUES_DynamicTitlesDefaultAmount = "DynamicTitlesDefaultAmount";
    public static final String DB_TABLE_QUES_PipingSourceDataLink = "PipingSourceDataLink";
    public static final String DB_TABLE_QUES_randomTitlesOrder = "randomTitlesOrder";
    public static final String DB_TABLE_QUES_questionOrientation = "questionOrientation";
    public static final String DB_TABLE_QUES_groupName = "groupName";
    public static final String DB_TABLE_QUES_RandomQuestionrder = "RandomQuestionrder";
    public static final String DB_TABLE_QUES_FONT = "Font";
    public static final String DB_TABLE_QUES_COLOR = "Color";
    public static final String DB_TABLE_QUES_SIZE = "Size";
    public static final String DB_TABLE_QUES_BOLD = "Bold";
    public static final String DB_TABLE_QUES_ITALIC = "Italic";
    public static final String DB_TABLE_QUES_UL = "Underline";
    public static final String DB_TABLE_QUES_ATTACHMENT = "Attachment";
    public static final String DB_TABLE_QUES_DATAID = "DataID";
    public static final String DB_TABLE_QUES_OT = "ObjectType";
    public static final String DB_TABLE_QUES_PFN = "PictureFilename";
    public static final String DB_TABLE_QUES_MAND = "Mandatory";
    public static final String DB_TABLE_QUES_CUSTOM_LINK = "CustomScaleLink";
    public static final String DB_TABLE_QUES_DT = "DisplayType";
    public static final String DB_TABLE_QUES_AO = "AnswerOedering";
    public static final String DB_TABLE_QUES_MT = "MiType";
    public static final String DB_TABLE_QUES_MM = "MiMandatory";
    public static final String DB_TABLE_QUES_MD = "MiDesc";
    public static final String DB_TABLE_QUES_Q = "Question";
    public static final String DB_TABLE_QUES_QD = "Qdesc";
    public static final String DB_TABLE_QUES_QTL = "QTypeLink";
    public static final String DB_TABLE_QUES_UIT = "UseInToc";
    public static final String DB_TABLE_QUES_SID = "SetID";
    public static final String DB_TABLE_QUES_TEXT = "Text";
    public static final String DB_TABLE_QUES_TID = "TextID";
    public static final String DB_TABLE_QUES_OBJECTDISCONDITINO = "ObjectDisplayCondition";
    public static final String DB_TABLE_QUES_OBJECTCODE = "ObjectCode";
    public static final String DB_TABLE_QUES_MI_MIN = "MiNumberMin";
    public static final String DB_TABLE_QUES_MI_MAX = "MiNumberMax";
    public static final String DB_TABLE_QUES_MAX_ANSWER = "MaxAnswersForMultiple";
    public static final String DB_TABLE_QUES_MI_FREE_TEXT_MIN = "MiFreeTextMinlength";
    public static final String DB_TABLE_QUES_MI_FREE_TEXT_MAX = "MiFreeTextMaxlength";
    public static final String DB_TABLE_QUES_URLCONTENT = "UrlContent";
    public static final String DB_TABLE_QUES_DESTINATION_OBJECT = "DestinationObject";
    public static final String DB_TABLE_QUES_URL_ID = "UrlID";
    public static final String DB_TABLE_QUES_DEST_DESC = "DestinationDescription";
    public static final String DB_TABLE_QUES_WORKERINPUTCAPTION = "WorkerInputCaption";
    public static final String DB_TABLE_QUES_BRANCHINPUTCAPTION = "BranchInputCaption";
    public static final String DB_TABLE_QUES_WORKERInputMandatory = "WorkerInputMandatory";
    public static final String DB_TABLE_QUES_BranchInputMandatory = "BranchInputMandatory";
    public static final String DB_TABLE_QUES_AS = "AnswersSource";
    public static final String DB_TABLE_QUES_AC = "AnswersCondition";
    public static final String DB_TABLE_QUES_AF = "AnswersFormat";
    public static final String DB_TABLE_QUES_LoopName = "LoopName";
    public static final String DB_TABLE_QUES_LoopSource = "LoopSource";
    public static final String DB_TABLE_QUES_RandomizeLoop = "RandomizeLoop";
    public static final String DB_TABLE_QUES_LoopCondition = "LoopCondition";
    public static final String DB_TABLE_QUES_LoopFormat = "LoopFormat";
    public static final String DB_TABLE_QUES_AFFECTED_QUESTIONS = "AFFECTED_QUESTIONS";
    public static final String DB_TABLE_QUES_SOURCE_SECTION_LINK = "SOURCE_SECTION_LINK";


    public static final String DB_TABLE_WORKERS = "Workers";
    public static final String DB_TABLE_WORKERS_WID = "workersID";
    public static final String DB_TABLE_WORKERS_SETID = "SetID";
    public static final String DB_TABLE_WORKERS_WORKERID = "workerID";
    public static final String DB_TABLE_WORKERS_WN = "workerName";
    public static final String DB_TABLE_WORKERS_BL = "BranchLink";

    public static final String DB_TABLE_BRANCHES = "Branches";
    public static final String DB_TABLE_BRANCHES_BID = "BranchID";
    public static final String DB_TABLE_BRANCHES_SETID = "SetID";
    public static final String DB_TABLE_BRANCHES_BN = "BranchName";
    public static final String DB_TABLE_BRANCHES_BLAT = "BranchLat";
    public static final String DB_TABLE_BRANCHES_BLONG = "BranchLong";

    public static final String DB_TABLE_PRODUCT_PROPERTIES_Values = "ProductPropertiesValues";
    public static final String DB_TABLE_PRODUCT_PROPERTIES_Values_Content = "Content";
    public static final String DB_TABLE_PRODUCT_PROPERTIES_Values_PropLink = "PropLink";
    public static final String DB_TABLE_PRODUCT_PROPERTIES_Values_Order = "OrderLink";
    public static final String DB_TABLE_PRODUCT_PROPERTIES_Values_IsActive = "IsActive";
    public static final String DB_TABLE_PRODUCT_PROPERTIES_Values_SetId = "SetId";
    public static final String DB_TABLE_PRODUCT_PROPERTIES_Values_ValueID = "ValueID";
    public static final String DB_TABLE_PRODUCT_PROPERTIES_Values_ProdPropID = "ProdPropID";

    public static final String DB_TABLE_PRODUCT_PROPERTIES = "ProductProperties";
    public static final String DB_TABLE_PRODUCT_PROPERTIES_ProdPropID = "ProdPropID";
    public static final String DB_TABLE_PRODUCT_PROPERTIES_PropertyName = "PropertyName";
    public static final String DB_TABLE_PRODUCT_PROPERTIES_ClientLink = "ClientLink";
    public static final String DB_TABLE_PRODUCT_PROPERTIES_Mandatory = "Mandatory";
    public static final String DB_TABLE_PRODUCT_PROPERTIES_Order = "OrderLink";
    public static final String DB_TABLE_PRODUCT_PROPERTIES_AllowOtherAddition = "AllowOtherAddition";
    public static final String DB_TABLE_PRODUCT_PROPERTIES_IsActive = "IsActive";
    public static final String DB_TABLE_PRODUCT_PROPERTIES_SetId = "SetId";

    public static final String DB_TABLE_PRODUCT_LOCATION = "ProductLocation";
    public static final String DB_TABLE_PRODUCT_LOCATION_PID = "ProdLocationID";
    public static final String DB_TABLE_PRODUCT_LOCATION_SETID = "SetId";
    public static final String DB_TABLE_PRODUCT_LOCATION_location = "ProductLocation";
    public static final String DB_TABLE_PRODUCT_CLientLink = "ClientLink";

    public static final String DB_TABLE_PRODUCTS = "Products";
    public static final String DB_TABLE_PRODUCTS_PID = "ProductID";
    public static final String DB_TABLE_PRODUCTS_SETID = "SetId";
    public static final String DB_TABLE_PRODUCTS_PN = "ProductName";
    public static final String DB_TABLE_PRODUCTS_ClientLink = "ClientLink";
    public static final String DB_TABLE_PRODUCTS_IsActive = "IsActive";
    public static final String DB_TABLE_PRODUCTS_ProductCode = "ProductCode";
    public static final String DB_TABLE_PRODUCTS_CheckQuantity = "CheckQuantity";
    public static final String DB_TABLE_PRODUCTS_CheckShelfLevel = "CheckShelfLevel";
    public static final String DB_TABLE_PRODUCTS_CheckPrice = "CheckPrice";
    public static final String DB_TABLE_PRODUCTS_CheckPacking = "CheckPacking";
    public static final String DB_TABLE_PRODUCTS_CheckExpiration = "CheckExpiration";
    public static final String DB_TABLE_PRODUCTS_AddNote = "AddNote";
    public static final String DB_TABLE_PRODUCTS_TakePicture = "TakePicture";
    public static final String DB_TABLE_PRODUCTS_Size = "Size";
    public static final String DB_TABLE_PRODUCTS_Order = "OrderLink";
    public static final String DB_TABLE_PRODUCTS_Bold = "Bold";
    public static final String DB_TABLE_PRODUCTS_prop_id_51 = "prop_id_51";
    public static final String DB_TABLE_PRODUCTS_prop_id_52 = "prop_id_52";

    public static final String DB_TABLE_QTITLES = "QuestionTitles";
    public static final String DB_TABLE_QTITLES_SETID = "SetID";
    public static final String DB_TABLE_QTITLES_DID = "DataID";
    public static final String DB_TABLE_QTITLES_ggtID = "ggtID";
    public static final String DB_TABLE_QTITLES_TitleText = "TitleText";
    public static final String DB_TABLE_QTITLES_TitleCode = "TitleCode";
    public static final String DB_TABLE_QTITLES_DataLink = "DataLink";
    public static final String DB_TABLE_QTITLES_DisplayCondition = "DisplayCondition";

    public static final String DB_TABLE_QLINKS = "QuestionLinks";
    public static final String DB_TABLE_QLINKS_SETID = "SetID";
    public static final String DB_TABLE_QLINKS_DataID = "DataID";
    public static final String DB_TABLE_QLINKS_DataLink = "DataLink";

    public static final String DB_TABLE_QGROUPS = "QuestionGroups";
    public static final String DB_TABLE_QGROUPS_SETID = "SetID";
    public static final String DB_TABLE_QGROUPS_DataID = "DataID";
    public static final String DB_TABLE_QGROUPS_DataLink = "DataLink";

    public static final String DB_TABLE_QA = "QuestionnaireAnswers";
    public static final String DB_TABLE_QA_CINDEX = "c_index";
    public static final String DB_TABLE_QA_SETID = "SetID";
    public static final String DB_TABLE_QA_DID = "DataID";
    public static final String DB_TABLE_QA_AID = "AnsID";
    public static final String DB_TABLE_QA_ANS = "Answer";
    public static final String DB_TABLE_QA_VAL = "Value";
    public static final String DB_TABLE_QA_COL = "Color";
    public static final String DB_TABLE_QA_BOLD = "Bold";
    public static final String DB_TABLE_QA_ITALIC = "Italic";
    public static final String DB_TABLE_QA_UL = "UnderLine";
    public static final String DB_TABLE_QA_CODE = "Code";
    public static final String DB_TABLE_QA_JUMPTO = "JumpTo";
    public static final String DB_TABLE_QA_ICONNAME = "IconName";
    public static final String DB_TABLE_QA_DISPLAYCONDITION = "answerDisplayCondition";
    public static final String DB_TABLE_QA_HIDEADDITIONALINFO = "HideAdditionalInfo";
    public static final String DB_TABLE_QA_ADDITIONALINFOMANDATORY = "AdditionalInfoMandatory";
    public static final String DB_TABLE_QA_ClearOtherAnswers = "ClearOtherAnswers";

    public static final String DB_TABLE_SURVEY = "Surveys";
    public static final String DB_TABLE_SURVEY_SurveyID = "SurveyID";
    public static final String DB_TABLE_SURVEY_SurveyName = "SurveyName";
    public static final String DB_TABLE_SURVEY_SetLink = "SetLink";
    public static final String DB_TABLE_SURVEY_QuotaReachedMessage = "QuotaReachedMessage";
    public static final String DB_TABLE_SURVEY_ThankYouMessage = "ThankYouMessage";
    public static final String DB_TABLE_SURVEY_LandingPage = "LandingPage";
    public static final String DB_TABLE_SURVEY_RedirectAfter = "RedirectAfter";
    public static final String DB_TABLE_SURVEY_TargetQuota = "TargetQuota";
    public static final String DB_TABLE_SURVEY_SurveyCount = "SurveyCount";
    public static final String DB_TABLE_SURVEY_Branch_NAME = "BranchName";

    // CREATE TABLE "Allocations" ("fsqqaID" VARCHAR, "Allocation" VARCHAR,
    // "SurveyCount" VARCHAR, "SurveyID" VARCHAR)
    public static final String DB_TABLE_ALLOCATIONS = "Allocations";
    public static final String DB_TABLE_ALLOCATIONS_fsqqaID = "fsqqaID";
    public static final String DB_TABLE_ALLOCATIONS_Allocation = "Allocation";
    public static final String DB_TABLE_ALLOCATIONS_SurveyCount = "SurveyCount";
    public static final String DB_TABLE_ALLOCATIONS_SurveyID = "SurveyID";

    // CREATE TABLE "SurveyQnA" ("fsqqID" VARCHAR, "DataLink" VARCHAR,
    // "AnswerLink" VARCHAR)
    public static final String DB_TABLE_SURVEYQNA = "SurveyQnA";
    public static final String DB_TABLE_SURVEYQNA_fsqqID = "fsqqID";
    public static final String DB_TABLE_SURVEYQNA_DataLink = "DataLink";
    public static final String DB_TABLE_SURVEYQNA_AnswerLink = "AnswerLink";
    public static final String DB_TABLE_SURVEYQNA_quotaId = "quotaId";

    // CREATE TABLE "Quotas" ("squoID" VARCHAR, "QuotaName" VARCHAR, "QuotaMin"
    // VARCHAR, "DoneCount" VARCHAR, "SurveyId" VARCHAR)
    public static final String DB_TABLE_QUOTAS = "Quotas";
    public static final String DB_TABLE_QUOTAS_squoID = "squoID";
    public static final String DB_TABLE_QUOTAS_QuotaName = "QuotaName";
    public static final String DB_TABLE_QUOTAS_QuotaMin = "QuotaMin";
    public static final String DB_TABLE_QUOTAS_DoneCount = "DoneCount";
    public static final String DB_TABLE_QUOTAS_SurveyId = "SurveyId";
    public static final String DB_TABLE_QUOTAS_ActionToTake = "ActionToTake";

    // "CREATE TABLE \"CustomOrderFields\" (\"OrderID\" VARCHAR, \"customfield_name\" VARCHAR, \"customfield_value\" VARCHAR)";
    public static final String DB_TABLE_CustomOrderFields = "CustomOrderFields";
    public static final String DB_TABLE_CustomOrderFields_OrderID = "OrderID";
    public static final String DB_TABLE_CustomOrderFields_customfield_name = "customfield_name";
    public static final String DB_TABLE_CustomOrderFields_customfield_value = "customfield_value";

    // "CREATE TABLE \"tbl_language\" (\"AltLangID\" VARCHAR, \"AltLangName\"
    // VARCHAR, \"AltLangDirection\" VARCHAR, \"IsActive\" VARCHAR,"
    // +CompanyLink
    // " \"InterfaceLanguage\" VARCHAR, \"IsSelected\" VARCHAR)";
    public static final String DB_TABLE_language = "tbl_language";
    public static final String DB_TABLE_language_AltLangID = "AltLangID";
    public static final String DB_TABLE_language_CompanyLink = "CompanyLink";
    public static final String DB_TABLE_language_AltLangName = "AltLangName";
    public static final String DB_TABLE_language_AltLangDirection = "AltLangDirection";
    public static final String DB_TABLE_language_IsActive = "IsActive";
    public static final String DB_TABLE_language_InterfaceLanguage = "InterfaceLanguage";
    public static final String DB_TABLE_language_IsSelected = "IsSelected";

    // "CREATE TABLE \"tbl_alt_questions\" (\"AltLangID\" VARCHAR,
    // \"SetID\" VARCHAR, \"DataID\" VARCHAR, \"Question\" VARCHAR," +
    // " \"QuestionDescription\" VARCHAR, \"MiDescription\" VARCHAR)";
    // "CREATE TABLE \"tbl_alt_answers\" (\"AltLangID\" VARCHAR,\"SetID\"
    // VARCHAR,
    // \"DataID\" VARCHAR, \"AnswerID\" VARCHAR, \"Answer\" VARCHAR)";

    public static final String DB_TABLE_alt_questions = "tbl_alt_questions";
    public static final String DB_TABLE_alt_AltLangID = "AltLangID";
    public static final String DB_TABLE_alt_SetID = "SetID";
    public static final String DB_TABLE_alt_DataID = "DataID";
    public static final String DB_TABLE_alt_Question = "Question";
    public static final String DB_TABLE_alt_QText = "QText";
    public static final String DB_TABLE_alt_QuestionDescription = "QuestionDescription";
    public static final String DB_TABLE_alt_GroupName = "groupName";
    public static final String DB_TABLE_alt_MiDescription = "MiDescription";
    public static final String DB_TABLE_alt_answers = "tbl_alt_answers";
    public static final String DB_TABLE_alt_AnswerID = "AnswerID";
    public static final String DB_TABLE_alt_Answer = "Answer";
    public static final String DB_TABLE_alt_TblTitle = "tbl_alt_titles";
    public static final String DB_TABLE_alt_qgtid = "qgtid";
    public static final String DB_TABLE_alt_Title = "Title";

    // "CREATE TABLE \"CustomOrderFieldsAnswers\" (\"OrderID\" VARCHAR, \"customfield_name\" VARCHAR, \"customfield_value\" VARCHAR, \"customfield_text\" VARCHAR)";
    public static final String DB_TABLE_CustomOrderFieldsAns = "CustomOrderFieldsAnswers";
    public static final String DB_TABLE_CustomOrderFieldsAns_OrderID = "OrderID";
    public static final String DB_TABLE_CustomOrderFieldsAns_customfield_name = "customfield_name";
    public static final String DB_TABLE_CustomOrderFieldsAns_customfield_value = "customfield_value";
    public static final String DB_TABLE_CustomOrderFieldsAns_customfield_text = "customfield_text";

    // "CREATE TABLE \"BranchProps\"
    // (\"Content\" VARCHAR, \"PropID\" VARCHAR, \"ValueID\" VARCHAR, \"PropertyName\" VARCHAR)";

    // BRANCH TABLE
    public static final String DB_TABLE_BRANCH_PROPS = "BranchProps";
    public static final String DB_TABLE_BRANCH_PROPS_BranchID = "BranchID";
    public static final String DB_TABLE_BRANCH_PROPS_Content = "Content";
    public static final String DB_TABLE_BRANCH_PROPS_PropID = "PropID";
    public static final String DB_TABLE_BRANCH_PROPS_ValueID = "ValueID";
    public static final String DB_TABLE_BRANCH_PROPS_PropertyName = "PropertyName";

    // QUETIONNAIRE MENU ITEMS ID
    public static final int MENUID_UPDALOAD_JOBS = 1077;
    public static final int MENUID_UPDALOAD_IMGS = 10790;
    public static final int MENUID_DOWNLOAD_JOBS = 1088;
    public static final int MENUID_DOWNLOAD_JOBS_ALT = 10881;
    public static final int MENUID_EXIT_AND_SAVE = 101;
    public static final int MENUID_PREVIEW = 102;
    public static final int MENUID_SUBMIT_SURVEY = 103;
    public static final int MENUID_UPDALOAD_COMPLETE_JOBS = 104;
    public static final int MENUID_DOWNLOAD_UPDATED_JOBS = 105;
    public static final int MENUID_DOWNLOAD_EXIT_JOBLIST = 106;
    public static final int MENUID_DOWNLOAD_SETTINGS = 107;
    public static final int MENUID_EXIT_AND_DELETE = 108;
    public static final int MENUID_CONDITION = 10812;
    public static final int MENUID_DONE = 109;
    public static final int MENUID_BUG = 134;
    public static final int MENUID_SIGN = 1345;
    public static final int MENUID_BACK_TO_ARCHIVE = 134569;
    public static final int MENUID_ATTACHED_FILE = 110;
    public static final int MENUID_IMAGE_GALLERY_OPTION = 111;
    public static final int MENUID_CAMERA_OPTION = 112;
    public static final int MENUID_VIDEO_OPTION = 113;
    public static final int MENUID_AUDIO_OPTION = 114;
    public static final int MENUID_AUDIO_FILES_OPTION = 11622;
    public static final int MENUID_IMAGE_GALLERY_OPTION_LAST = 511;
    public static final int MENUID_CAMERA_OPTION_LAST = 512;
    public static final int MENUID_VIDEO_OPTION_LAST = 513;
    public static final int MENUID_AUDIO_OPTION_LAST = 514;
    public static final int MENUID_AUDIO_FILES_OPTION_LAST = 51622;
    public static final int MENUID_SUBMIT = 115;
    public static final String BRANCH_NAME = "survey_branch";
    public static final String FILE_DATE_WISE = "FILE_DATE_WISE";
    public static final String crash_report_url = "http://www.waqarulhaq.com/waqarulhaq_com/appicattendance/parse.php";
    public static final String EncryptionKey = "mormor";

    public static int getIconSize() {
        int d = (int) (30 * Resources.getSystem().getDisplayMetrics().density);
        return d;
    }

    // public static final int size_icon = 68;

    // tblLoops Constant

    public static String tblLoops = "tblLoops";
    public static String tblSetId = "SetId";
    public static String tblColumnNumber = "ColumnNumber";
    public static String tblColumnName = "ColumnName";
    public static String tblColumnData = "ColumnData";
    public static String tblLastColumnData = "LastColumnData";
    public static String tblListName = "ListName";
    public static String tblListRowIndex = "ListRowIndex";
    public static String tblListID = "ListID";
    public static String SETTINGS_ENABLE_DATE_FILTER = "ENABLE_DATE_FILTER";
    public static String SETTINGS_SHOW_ANOTHER_PHOTO = "SHOW_ANOTHER_PHOTO";
    public static String SETTINGS_ENABLE_TIME_STAMP = "ENABLE_TIME_STAMP";
    public static String SETTINGS_BACKUP = "SETTINGS_BACKUP";
    public static String SETTINGS_switchtracking = "switchtracking";
    public static String SETTINGS_switchHorses = "switchhorses";
    public static String SETTINGS_ENABLE_BRANCH_FULL_NAME = "SETTINGS_ENABLE_BRANCH_FULL_NAME";
    public static String SETTINGS_ENABLE_SORTING = "ENABLE_SORTING";
    public static String SETTINGS_ENABLE_CROPPING = "ENABLE_CROPPING";
    public static String SETTINGS_ENABLE_DEFAULT_CAMERA = "DEFAULT_CAMERA";
    public static String SETTINGS_ENABLE_RESOLUTION = "SETTINGS_ENABLE_RESOLUTION";
    public static String SETTINGS_WIFI_ONLY = "SETTINGS_WIFI_ONLY";
    public static String SETTINGS_ENABLE_ALTERNATE_ORDER = "ENABLE_AUTO_UPLOAD";// "ENABLE_ALTERNATE_ORDER";
    private static boolean alternate_enabled;

    public static String getWhereSetId(String setid) {
        String where = tblSetId + "='" + setid + "'";
        return where;
    }

    public static String getWheresetIdnListName(String setId, String listName) {
        String where = tblSetId + "='" + setId + "' and " + tblListName + "="
                + listName;
        return where;
    }

    public static String getWheresetIdnColumnNumber(String setid,
                                                    int columnNumber) {
        String where = tblSetId + "='" + setid + "' and " + tblColumnNumber
                + "=" + columnNumber;
        return where;
    }

    public static String getWheresetIdnlastColumnNumber(String setid,
                                                        String lastColumnname) {
        String where = tblSetId + "='" + setid + "' and " + tblLastColumnData
                + "='" + lastColumnname + "'";
        return where;
    }

    public static int getAnswerCount(Context con) {

        Display display = ((QuestionnaireActivity) con).getWindowManager()
                .getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        DisplayMetrics dm = new DisplayMetrics();
        ((QuestionnaireActivity) con).getWindowManager().getDefaultDisplay()
                .getMetrics(dm);
        double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
        double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
        double screenInches = Math.sqrt(x + y);
        Log.d("debug", "Screen inches : " + screenInches);
        int totalCells = 3;
        if (screenInches > 6) {
            totalCells = 6;
        }
        if (screenInches > 9) {
            totalCells = 8;
        }

        return totalCells;
    }

    public static void setEnableAlternateOrder(boolean b) {
        alternate_enabled = b;

    }

    public static boolean getEnableAlternateOrder() {
        return true;
        // return alternate_enabled;
    }

    public static String getSignupURL() {
        String url = Helper.getSystemURL() + "/api_index.php";
        return url;
    }

    public static String getBoardListFile(String ver, String start_date,
                                          String end_date, String lat1, String long1, String lat2,
                                          String long2) {
        // http://checker.co.il/testing/c_pda-job-board.php?ver=9.0&json=1&date_start=2016-12-26&date_end=2016-12-29&lat1=28.644800&long1=77.216721&lat2=31.771959&long2=78.217018
        String url = "c_pda-job-board_date_start_" + start_date + "_date_end_"
                + end_date + "_lat1_" + lat1 + "_lat2_" + lat2 + "_long1_"
                + long1 + "_long2_" + long2;
        url = url.replace(".", "__");
        url = url.replace("-", "___");
        return url + ".txt";
    }

    public static boolean isHS() {
        // return (Helper.getSystemURL() != null && Helper.getSystemURL()
        // .toLowerCase().contains("hs-brands-india"))
        // || (Helper.getSystemURL() != null && Helper.getSystemURL()
        // .toLowerCase().contains("hsbrands"))
        // || (Helper.getSystemURL() != null && Helper.getSystemURL()
        // .toLowerCase().contains("tovanot"))
        // || (Helper.getSystemURL() != null && Helper.getSystemURL()
        // .toLowerCase().contains("testing"))
        // || (Helper.getSystemURL() != null && Helper.getSystemURL()
        // .toLowerCase().contains("testing"));

        return true;

    }

    public static String getAlternateDatesUrl(String orderId) {

        String str = Helper.getSystemURL()
                + "/c_pda-get-order-dates.php?ver=7.90&app=1&json=1"
                + "&OrderID=" + orderId;
        Log.e("getAlternateDatesUrl", str);
        return str;
    }

    public static String getViewShopperURL() {
        // https://abastecaseubolso.misteroclienteoculto.com.wqbr//c_view-checker.php
        String str = Helper.getSystemURL() + "/c_view-checker.php";
        return str;
    }

    public static String getEditShopperURL() {
        // https://abastecaseubolso.misteroclienteoculto.com.br/c_crit-history.php
        String str = Helper.getSystemURL() + "/checkers.php?edit=y&auth_mode=2";
        return str;
    }

    public static String getSurveyHistoryURL() {
        // https://abastecaseubolso.misteroclienteoculto.com.br/c_crit-history.php
        String str = Helper.getSystemURL() + "/c_crit-history.php";
        return str;
    }

    public static String getForgotURL() {
        String str = Helper.getSystemURL() + "/recover-password.php?w=ch2";
        return str;
    }

    public static String getSignUpURL() {
        String str = Helper.getSystemURL()
                + "/c_register-new-checker.php?addnew=1";
        return str;
    }

    public static String getRegisterURL() {
        String str = Helper.getSystemURL()
                + "/c_register-new-checker.php?addnew=1";
        return str;
    }

    public static boolean isUploadingEnabled() {
        // TODO Auto-generated method stub
        return alternate_enabled;
    }

    public static String CompareWithNewUrlList(String[] newurlArray) {
        String currentUrl = Helper.getSystemURL();
        String clientName = getClientName(currentUrl);

        for (int i = 0; currentUrl != null && !currentUrl.toLowerCase().contains("eu.") &&
                clientName != null && newurlArray != null && i < newurlArray.length; i++) {
            if (newurlArray[i] != null && newurlArray[i].endsWith(clientName)) {
                return newurlArray[i];
            }
        }
        return null;
    }

    private static String getClientName(String currentUrl) {
        if (currentUrl != null) {
            int index = currentUrl.toLowerCase().indexOf("checker-soft.com/");
            if (index > -1) {
                String clientName = currentUrl.substring(index + 16);
                return clientName;
            }

        }
        return null;
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static void setLocale(Activity con) {


        SharedPreferences myPrefs = con.getSharedPreferences("pref", con.MODE_PRIVATE);
        int language = myPrefs.getInt(Constants.SETTINGS_LANGUAGE_INDEX, 0);
        String local = Constants.SETTINGS_LOCALE_VAL_ARR[language];
        Locale locale = new Locale(local);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        con.getBaseContext().getResources().updateConfiguration(config,
                con.getBaseContext().getResources().getDisplayMetrics());

        if (Constants.SETTINGS_LOCALE_VAL_ARR[myPrefs.getInt(
                Constants.SETTINGS_LANGUAGE_INDEX, 0)] != null && Constants.SETTINGS_LOCALE_VAL_ARR[myPrefs.getInt(
                Constants.SETTINGS_LANGUAGE_INDEX, 0)].equals("iw"))
            con.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);


    }
}
