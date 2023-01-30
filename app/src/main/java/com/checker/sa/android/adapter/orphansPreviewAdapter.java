package com.checker.sa.android.adapter;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.nfc.tech.IsoDep;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.checker.sa.android.data.filePathDataID;
import com.checker.sa.android.db.DBAdapter;
import com.checker.sa.android.helper.Constants;
import com.mor.sa.android.activities.R;

public class orphansPreviewAdapter extends ArrayAdapter<filePathDataID> {
	private final Context context;
	private final List<filePathDataID> values;
	Boolean isPreview;
	String currentOrderId = null;
	private View viewAttach;
	private RelativeLayout dialog;

	@Override
	public int getCount() {
		currentOrderId = "";
		if (values != null)
			return values.size();

		this.viewAttach.setVisibility(RelativeLayout.GONE);
		return 0;
	}

	private List<filePathDataID> sortImagesAccordingToOrderId(
			List<filePathDataID> unsortedvalues) {
		List<filePathDataID> tempList = new ArrayList<filePathDataID>();

		for (int i = 0; i < unsortedvalues.size(); i++) {
			String currentOrderID = unsortedvalues.get(i)
					.getUPLOAD_FILe_ORDERID();
			for (int j = 0; j < unsortedvalues.size(); j++) {
				if (unsortedvalues.get(j).isRead == false
						&& currentOrderID != null
						&& unsortedvalues.get(j).getUPLOAD_FILe_ORDERID() != null
						&& unsortedvalues.get(j).getUPLOAD_FILe_ORDERID()
								.equals(currentOrderID)) {
					unsortedvalues.get(j).isRead = true;
					tempList.add(unsortedvalues.get(j));
				}
			}
		}
		return tempList;
	}

	public orphansPreviewAdapter(Context context, List<filePathDataID> valuess,
			Boolean isPreview, View attachLayout, RelativeLayout dialog) {

		super(context, R.layout.file_name_row, valuess);
		this.dialog = dialog;
		this.viewAttach = attachLayout;
		values = sortImagesAccordingToOrderId(valuess);
		this.context = context;
		this.isPreview = true;
	}

	public void OnDelClick(int pos) {
		deleteFromTable(values.get(pos));
		values.remove(pos);
		if (values.size() <= 0) {
			viewAttach.setVisibility(RelativeLayout.GONE);
			if (this.dialog != null)
				this.dialog.setVisibility(RelativeLayout.GONE);
		} else {

			notifyDataSetChanged();
		}
	}

	private void deleteFromTable(filePathDataID filePathDataID) {
		try {
			String where = Constants.UPLOAD_FILe_MEDIAFILE + "=" + "\""
					+ filePathDataID.getFilePath() + "\"";
			DBAdapter.openDataBase();
			DBAdapter.db.delete(Constants.UPLOAD_FILE_TABLE, where, null);
			DBAdapter.closeDataBase();

		} catch (Exception ex) {
			String str = "";
			str += "";
		}
	}

	public String getImagePath(Uri uri) {
		Cursor cursor = context.getContentResolver().query(uri, null, null,
				null, null);
		cursor.moveToFirst();
		String document_id = cursor.getString(0);
		document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
		cursor.close();

		cursor = context.getContentResolver().query(
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				null, MediaStore.Images.Media._ID + " = ? ",
				new String[] { document_id }, null);
		cursor.moveToFirst();
		String path = cursor.getString(cursor
				.getColumnIndex(MediaStore.Images.Media.DATA));
		cursor.close();

		return path;
	}

	public String getRealPathFromURI(Uri contentUri) {
		try {
			// can post image

			String[] proj = { MediaStore.Images.Media.DATA };
			Cursor cursor = ((Activity) context).managedQuery(contentUri, proj, // Which
																				// columns
																				// to
																				// return
					null, // WHERE clause; which rows to return (all rows)
					null, // WHERE clause selection arguments (none)
					null); // Order-by clause (ascending by name)
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();

			String filePath = cursor.getString(column_index);

		} catch (Exception ex) {
		}
		return null;
		// {
		// String path = contentUri.getPath();
		// path = path.replace("/storage/sdcard0/DCIM/Camera/",
		// Environment.getExternalStorageDirectory() + "/lgimageq/");
		// return path.replace("file://", "");
		// }
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = null;

		rowView = inflater.inflate(R.layout.attach_preview_row, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.nameText);

		final View layoutdel = (View) rowView.findViewById(R.id.layoutDel);

		String header = "";
		if (values.get(position).getUPLOAD_FILe_CLIENT_NAME() != null
				&& !values.get(position).getUPLOAD_FILe_CLIENT_NAME().trim()
						.equals(""))
			header = values.get(position).getUPLOAD_FILe_CLIENT_NAME() + ",";
		if (values.get(position).getUPLOAD_FILe_SET_NAME() != null)
			header += values.get(position).getUPLOAD_FILe_SET_NAME() + ",";
		if (values.get(position).getUPLOAD_FILe_BRANCH_NAME() != null)
			header += cleanHeaderDates(values.get(position)
					.getUPLOAD_FILe_BRANCH_NAME().replace("null", ""));
		// if (values.get(position).getUPLOAD_FILe_DATE() != null)
		// header += cnvrtDate(values.get(position).getUPLOAD_FILe_DATE());
		textView.setText(header);
		if (values.get(position).getUPLOAD_FILe_ORDERID() != null
				&& values.get(position).getUPLOAD_FILe_ORDERID()
						.equals(currentOrderId)) {
			textView.setVisibility(RelativeLayout.GONE);
		} else if (values.get(position).getUPLOAD_FILe_ORDERID() != null
				&& !values.get(position).getUPLOAD_FILe_ORDERID()
						.equals(currentOrderId)) {
			currentOrderId = values.get(position).getUPLOAD_FILe_ORDERID();
		}

		TextView textValueView = (TextView) rowView
				.findViewById(R.id.valueText);

		String path = values.get(position).getFilePath();

		if (path.startsWith("content")) {
			String newPath = getRealPathFromURI(Uri.parse(path));
			if (newPath == null) {
				layoutdel.setVisibility(RelativeLayout.VISIBLE);
				textValueView.setText(path);
			} else {
				File file = new File(newPath);

				if (file.exists()) {

					layoutdel.setVisibility(RelativeLayout.GONE);
					textValueView.setText(path);
				} else {
					layoutdel.setVisibility(RelativeLayout.VISIBLE);
					textValueView.setText(path);
				}

			}
		} else if (path.startsWith("file:///")) {
			path = path.replace("file:///", "/");
			File file = new File(path);

			if (file.exists()) {

				layoutdel.setVisibility(RelativeLayout.GONE);
				textValueView.setText(path);
			} else {
				layoutdel.setVisibility(RelativeLayout.VISIBLE);
				textValueView.setText(path);
			}
		} else {
			File file = new File(path);

			if (file.exists()) {

				layoutdel.setVisibility(RelativeLayout.GONE);
				textValueView.setText(path);
			} else {
				layoutdel.setVisibility(RelativeLayout.VISIBLE);
				textValueView.setText(path);
			}
		}
		final ImageView minusSign = (ImageView) rowView
				.findViewById(R.id.minusImg);
		minusSign.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// if (layoutdel.getVisibility() == RelativeLayout.GONE) {
				// minusSign.setVisibility(RelativeLayout.INVISIBLE);
				// layoutdel.setVisibility(RelativeLayout.VISIBLE);
				// } else {
				// minusSign.setVisibility(RelativeLayout.VISIBLE);
				// layoutdel.setVisibility(RelativeLayout.GONE);
				//
				// }

				try {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							context);
					builder.setCancelable(false);
					builder.setTitle(context
							.getString(R.string.non_answered_conf_alert_title));
					builder.setMessage(context
							.getString(R.string.file_delete_alert));
					builder.setCancelable(false)
							.setPositiveButton(
									context.getString(R.string.questionnaire_exit_delete_alert_yes),
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog, int id) {
											OnDelClick(position);
										}
									})
							.setNegativeButton(
									context.getString(R.string.questionnaire_exit_delete_alert_no),
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog, int id) {
											return;
										}
									});

					AlertDialog alert = builder.create();
					alert.show();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

		minusSign.setVisibility(View.VISIBLE);

		return rowView;
	}

	private String cleanHeaderDates(String replace) {
		int start = -1;
		start = replace.lastIndexOf("(");
		if (start > -1) {
			String str = replace.substring(start);
			return str;
		}

		return replace;
	}

	private String cnvrtDate(String upload_FILe_DATE) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  kk:mm:ss",
				Locale.ENGLISH);
		long timeStamp = Long.parseLong(upload_FILe_DATE);
		java.util.Date time = new java.util.Date((long) timeStamp * 1000);
		return "(" + sdf.format(time) + ")";
	}
}
