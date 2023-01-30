package com.checker.sa.android.adapter;

import java.util.List;

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

public class errIconAdapter extends ArrayAdapter<String> {
	private final Context context;
	private final List<String> values;
	private final List<String> headings;

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (values != null)
			return values.size();
		return 0;
	}

	public errIconAdapter(Context context, List<String> valuess,
			List<String> headingss) {
		super(context, R.layout.file_name_row, valuess);
		this.context = context;
		values = valuess;
		headings = headingss;
	}

	String tmpHeading = "";

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = null;
		if (Helper.getTheme(context) == 0) {
			rowView = inflater.inflate(R.layout.preview_row_night, parent,
					false);
		} else {
			rowView = inflater.inflate(R.layout.preview_row, parent, false);
		}
		if (values.get(position) != null
				&& values.get(position).startsWith("-")) {
			TextView textView = (TextView) rowView.findViewById(R.id.nameText);
			textView.setText(values.get(position).replace("-", ""));

			TextView textValueView = (TextView) rowView
					.findViewById(R.id.valueText);
			textValueView.setVisibility(RelativeLayout.GONE);
			textValueView.setText(values.get(position));
		} else {
			TextView textView = (TextView) rowView.findViewById(R.id.nameText);
			textView.setVisibility(RelativeLayout.GONE);

			TextView textValueView = (TextView) rowView
					.findViewById(R.id.valueText);
			textValueView.setText(values.get(position));
		}

		ImageView minusSign = (ImageView) rowView.findViewById(R.id.minusImg);
		minusSign.setVisibility(View.GONE);
		return rowView;
	}
}
