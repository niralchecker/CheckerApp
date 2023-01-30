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

import com.checker.sa.android.data.AltLanguage;
import com.checker.sa.android.data.AlternateJob;
import com.checker.sa.android.data.filePathDataID;
import com.checker.sa.android.db.DBAdapter;
import com.checker.sa.android.helper.Constants;
import com.mor.sa.android.activities.R;

public class AlternateLanguageAdapter extends ArrayAdapter<AltLanguage> {
	private final Context context;
	private List<AltLanguage> values;
	private int selectedIndex;

	@Override
	public int getCount() {
		if (values != null)
			return values.size();

		return 0;
	}

	public AlternateLanguageAdapter(Context context, List<AltLanguage> valuess,
			int selectedIndex) {
		super(context, R.layout.name_row, valuess);
		values = valuess;
		this.context = context;
		this.selectedIndex = selectedIndex;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = null;

		rowView = inflater.inflate(R.layout.alt_name_row, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.nameText);
		textView.setText(values.get(position).getAltLangName());
		if (position == selectedIndex)
			rowView.findViewById(R.id.tick).setVisibility(
					RelativeLayout.VISIBLE);
		return rowView;
	}
}
