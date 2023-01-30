package com.mor.sa.android.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

import com.checker.sa.android.adapter.archiveAdapter;
import com.checker.sa.android.data.Order;
import com.checker.sa.android.data.OrderOrFile;
import com.checker.sa.android.data.Orders;
import com.checker.sa.android.data.QuestionnaireData;
import com.checker.sa.android.data.Set;
import com.checker.sa.android.data.SubmitQuestionnaireData;
import com.checker.sa.android.data.parser.Parser;
import com.checker.sa.android.db.DBAdapter;
import com.checker.sa.android.db.DBHelper;
import com.checker.sa.android.dialog.Progress_Dialog;
import com.checker.sa.android.helper.Constants;
import com.checker.sa.android.helper.Helper;
import com.checker.sa.android.helper.UIHelper;
import com.checker.sa.android.transport.Connector;

import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ArchiveActivity extends Activity {

	public static SubmitQuestionnaireData toBeUploadedSQ;
    public static String selectedOrderId;
	private static OrderOrFile selectedOrder;
	ArrayList<OrderOrFile> archivedOrders;
	private ListView listView;
	private ImageView imgBack;

	public void sendThisJobToServer(SubmitQuestionnaireData sq)
	{
		ArchiveActivity.toBeUploadedSQ=sq;
		Intent intent = new Intent();
		setResult(RESULT_OK, intent);
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LoginActivity.thisOrder=null;
		ArchiveActivity.selectedOrderId=null;
		setContentView(R.layout.archived_orders);
		TextView txtHeader=(TextView)findViewById(R.id.jd_heder);
		imgBack=(ImageView)findViewById(R.id.back_button);
		imgBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				toBeUploadedSQ=null;

				finish();
			}
		});
		listView=(ListView)findViewById(R.id.listjobs);

		archivedOrders=loadData();
		String translated=getResources().getString(R.string.job_list_archiveorder);
		if (txtHeader!=null && archivedOrders!=null) txtHeader.setText(translated+" ("+archivedOrders.size()+")");
		listView.setAdapter(new archiveAdapter(ArchiveActivity.this,archivedOrders));

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String OrderID = archivedOrders.get(position).getOrderID();
				Intent intent = new Intent(ArchiveActivity.this,
						JobDetailActivity.class);
				intent.putExtra(Constants.POST_FIELD_QUES_ORDER_ID,
						OrderID);
				intent.putExtra(Constants.POST_FIELD_IS_ARCHIVE,
						archivedOrders.get(position).getSetID());
				ArchiveActivity.selectedOrderId=OrderID;
				ArchiveActivity.selectedOrder=archivedOrders.get(position);
				startActivity(intent);
				//finish();
			}
		});
	}

	private ArrayList<OrderOrFile> loadData() {
		String where = "StatusName=" + "\"archived\""+" OR "+"StatusName=" + "\"Archived\"";
		ArrayList<Order> jobordersss = DBHelper
				.getOrders(
						where,
						Constants.DB_TABLE_JOBLIST,
						new String[]{
								Constants.DB_TABLE_JOBLIST_ORDERID,
								Constants.DB_TABLE_JOBLIST_DATE,
								Constants.DB_TABLE_JOBLIST_SN,
								Constants.DB_TABLE_JOBLIST_DESC,
								Constants.DB_TABLE_JOBLIST_SETNAME,
								Constants.DB_TABLE_JOBLIST_SETLINK,
								Constants.DB_TABLE_JOBLIST_CN,
								Constants.DB_TABLE_JOBLIST_BFN,
								Constants.DB_TABLE_JOBLIST_BN,
								Constants.DB_TABLE_JOBLIST_CITYNAME,
								Constants.DB_TABLE_JOBLIST_ADDRESS,
								Constants.DB_TABLE_JOBLIST_BP,
								Constants.DB_TABLE_JOBLIST_OH,
								Constants.DB_TABLE_JOBLIST_TS,
								Constants.DB_TABLE_JOBLIST_TE,
								Constants.DB_TABLE_JOBLIST_SETID,
								Constants.DB_TABLE_JOBLIST_BL,
								Constants.DB_TABLE_JOBLIST_BLNG,
								Constants.DB_TABLE_JOBLIST_FN,
								Constants.DB_TABLE_JOBLIST_JC,
								Constants.DB_TABLE_JOBLIST_JI,
								Constants.DB_TABLE_JOBLIST_BLINK,
								Constants.DB_TABLE_JOBLIST_MID,
								Constants.DB_TABLE_CHECKER_CODE,
								Constants.DB_TABLE_CHECKER_LINK,
								Constants.DB_TABLE_BRANCH_CODE,
								Constants.DB_TABLE_SETCODE,
								Constants.DB_TABLE_PURCHASE_DESCRIPTION,
								Constants.DB_TABLE_PURCHASE,
								Constants.DB_TABLE_JOBLIST_BRIEFING,
								Constants.DB_TABLE_JOBLIST_sPurchaseLimit,
								Constants.DB_TABLE_JOBLIST_sNonRefundableServicePayment,
								Constants.DB_TABLE_JOBLIST_sTransportationPayment,
								Constants.DB_TABLE_JOBLIST_sCriticismPayment,
								Constants.DB_TABLE_JOBLIST_sBonusPayment,
								Constants.DB_TABLE_JOBLIST_AllowShopperToReject,
								Constants.DB_TABLE_JOBLIST_sinprogressonserver,
								Constants.DB_TABLE_JOBLIST_sProjectName,
								Constants.DB_TABLE_JOBLIST_sRegionName,
								Constants.DB_TABLE_JOBLIST_sdeletedjob,},
						Constants.DB_TABLE_JOBLIST_JI);
ArrayList<OrderOrFile> orders=new ArrayList<OrderOrFile>();
for (int i=0;jobordersss!=null && i<jobordersss.size();i++)
{
	orders.add(new OrderOrFile(null,jobordersss.get(i)));
}
		return orders;

	}

}
