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

import com.checker.sa.android.data.AlternateJob;
import com.checker.sa.android.data.Cert;
import com.checker.sa.android.data.filePathDataID;
import com.checker.sa.android.db.DBAdapter;
import com.checker.sa.android.helper.Constants;
import com.mor.sa.android.activities.R;

public class CheckertificateAdapter extends ArrayAdapter<Cert> {
	private final Context context;
	private List<Cert> values;

	@Override
	public int getCount() {
		if (values != null)
			return values.size();

		return 0;
	}

	public CheckertificateAdapter(Context context, List<Cert> valuess) {
		super(context, R.layout.name_row, valuess);
		values = valuess;
		this.context = context;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = null;

		rowView = inflater.inflate(R.layout.checkertificate_row, parent, false);

		TextView textViewSet = (TextView) rowView
				.findViewById(R.id.setValueText);
		TextView gradeValueText = (TextView) rowView
				.findViewById(R.id.gradeValueText);
		TextView LastDateValueText = (TextView) rowView
				.findViewById(R.id.LastDateValueText);
		TextView AttemptsValueText = (TextView) rowView
				.findViewById(R.id.AttemptsValueText);
		TextView statusValueText = (TextView) rowView
				.findViewById(R.id.statusValueText);
		TextView textView = (TextView) rowView.findViewById(R.id.nameText);
		ImageView imgView = (ImageView) rowView.findViewById(R.id.imgStatus);
		textView.setText(values.get(position).getCertificateName());
		textViewSet.setText(values.get(position).getSetName());
		statusValueText.setText(values.get(position).getStatus());
		String d = values.get(position).getGrade() + " ("+context.getResources().getString(R.string.reqGrade)+"= "
				+ values.get(position).getDependencySetGrade() + ")";
		try {
			double gradeGot = Double.parseDouble(values.get(position)
					.getGrade());
			double gradeToTake = Double.parseDouble(values.get(position)
					.getDependencySetGrade());
			if (gradeGot >= gradeToTake)
				d += "";
		} catch (Exception ex) {

		}
		gradeValueText.setText(d);
		LastDateValueText.setText(values.get(position).getLastDateTaken());
		AttemptsValueText.setText(values.get(position).getTimesTaken() + " out of "
				+ values.get(position).getMaxRetake());
		if (values.get(position).getStatus() != null
				&& values.get(position).getStatus().toLowerCase()
						.contains("allowed")) {
			imgView.setImageResource(R.drawable.resume);
		} else if (values.get(position).getStatus() != null
				&& values.get(position).getStatus().toLowerCase()
						.equals("passed")) {
			imgView.setImageResource(R.drawable.pic_tick);
		} else if (values.get(position).getStatus() != null
				&& (values.get(position).getStatus().toLowerCase()
						.equals("blocked") || values.get(position).getStatus()
						.toLowerCase().contains("maximum"))) {
			imgView.setImageResource(R.drawable.manhul);
		}
		return rowView;
	}
}
