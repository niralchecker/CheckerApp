package com.checker.sa.android.adapter;

import java.util.List;

import com.checker.sa.android.data.MenuItem;
import com.checker.sa.android.data.POS_Shelf;
import com.checker.sa.android.helper.Helper;
import com.checker.sa.android.helper.POS_Toggles;
import com.checker.sa.android.helper.POS_Toggles.EnumToggleButton;
import com.mor.sa.android.activities.R;

import android.R.color;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class sideMEnuAdapter extends ArrayAdapter<MenuItem> {
	private final Context context;
	private final List<MenuItem> values;

	@Override
	public int getCount() {
		if (values != null)
			return values.size();
		return 0;
	}

	public sideMEnuAdapter(Context context, List<MenuItem> valuess) {
		super(context, R.layout.menu_row, valuess);
		this.context = context;
		values = valuess;
	}

	String tmpHeading = "";

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = null;
		rowView = inflater.inflate(R.layout.menu_row, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.nameText);
		textView.setText(values.get(position).getName());

		ImageView btnItem = (ImageView) rowView.findViewById(R.id.btnItem);
		btnItem.setImageResource(values.get(position).getResid());
		return rowView;
	}
}
