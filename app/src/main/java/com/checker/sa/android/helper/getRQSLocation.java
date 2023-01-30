package com.checker.sa.android.helper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.os.AsyncTask;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import android.widget.TextView;

public class getRQSLocation extends AsyncTask<Integer, Location, Location> {
	int myLatitude, myLongitude;
	int mcc = 0;
	int mnc = 0;
	GsmCellLocation location;
	private Context con;

	public getRQSLocation(Context con) {
		this.con = con;
	}

	private void WriteData(OutputStream out, int cid, int lac)
			throws IOException {
		DataOutputStream dataOutputStream = new DataOutputStream(out);
		dataOutputStream.writeShort(21);
		dataOutputStream.writeLong(0);
		dataOutputStream.writeUTF("en");
		dataOutputStream.writeUTF("Android");
		dataOutputStream.writeUTF("1.0");
		dataOutputStream.writeUTF("Web");
		dataOutputStream.writeByte(27);
		dataOutputStream.writeInt(0);
		dataOutputStream.writeInt(0);
		dataOutputStream.writeInt(3);
		dataOutputStream.writeUTF("");

		dataOutputStream.writeInt(cid);
		dataOutputStream.writeInt(lac);

		dataOutputStream.writeInt(0);
		dataOutputStream.writeInt(0);
		dataOutputStream.writeInt(0);
		dataOutputStream.writeInt(0);
		dataOutputStream.flush();
	}

	public void ShowAlert(Context context, String title, final String message,
			String button_lbl) {
		AlertDialog.Builder alert = new AlertDialog.Builder(context);
		alert.setCancelable(false);
		alert.setTitle(title);
		TextView textView = new TextView(context);
		textView.setTextSize(UIHelper.getFontSize(context,
				textView.getTextSize()));
		textView.setText(Helper.makeHtmlString(message));
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

	private Boolean RqsLocation() {
		Boolean result = false;
		TelephonyManager tel = (TelephonyManager) con
				.getSystemService(Context.TELEPHONY_SERVICE);
		String networkOperator = tel.getNetworkOperator();
		if (!TextUtils.isEmpty(networkOperator)) {
			mcc = Integer.parseInt(networkOperator.substring(0, 3));
			mnc = Integer.parseInt(networkOperator.substring(3));
		}

		final TelephonyManager telephony = (TelephonyManager) con
				.getSystemService(Context.TELEPHONY_SERVICE);
		if (telephony.getPhoneType() == TelephonyManager.PHONE_TYPE_GSM) {
			location = (GsmCellLocation) telephony.getCellLocation();
			if (location != null) {

				// ShowAlert(con, "Location Info", "LAC: " + location.getLac()
				// + " CID: " + location.getCid() + " MCC:" + mcc
				// + " MNC:" + mnc, "Ok");

				String urlmmap = "http://www.google.com/glm/mmap";

				try {
					// URL url = new URL(urlmmap);
					// URLConnection conn = url.openConnection();
					// HttpURLConnection httpConn = (HttpURLConnection) conn;
					// httpConn.setRequestMethod("POST");
					// httpConn.setDoOutput(true);
					// httpConn.setDoInput(true);
					// httpConn.connect();
					//
					// OutputStream outputStream = httpConn.getOutputStream();
					// WriteData(outputStream, location.getCid(),
					// location.getLac());
					//
					// InputStream inputStream = httpConn.getInputStream();
					// DataInputStream dataInputStream = new DataInputStream(
					// inputStream);
					//
					// dataInputStream.readShort();
					// dataInputStream.readByte();
					// int code = dataInputStream.readInt();
					// if (code == 0) {
					// myLatitude = dataInputStream.readInt();
					// myLongitude = dataInputStream.readInt();
					//
					// result = true;
					//
					// }
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return result;

	}

	@Override
	protected void onPreExecute() {
	}

	@Override
	protected void onPostExecute(Location result) {
		ShowAlert(con, "Location Info", "LAC: " + location.getLac() + " CID: "
				+ location.getCid() + " MCC:" + mcc + " MNC:" + mnc, "Ok");
	}

	@Override
	protected Location doInBackground(Integer... params) {
		RqsLocation();
		return null;
	}

}
