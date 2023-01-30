package com.checker.sa.android.adapter;

import java.util.ArrayList;
import java.util.List;

import com.checker.sa.android.data.CustomFields;
import com.checker.sa.android.data.filePathDataID;
import com.checker.sa.android.helper.UIHelper;
import com.mor.sa.android.activities.QuestionnaireActivity;
import com.mor.sa.android.activities.R;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CustomFieldAdapter extends ArrayAdapter<CustomFields> {
	private final Context context;
	private boolean isHebrew = false;
	private final ArrayList<CustomFields> customFields;

	public CustomFieldAdapter(Context context, ArrayList<CustomFields> uris,
			boolean isHebrew) {
		super(context, R.layout.file_name_row, uris);
		customFields = uris;
		this.context = context;
		this.isHebrew = isHebrew;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.custom_field_custom_row,
				parent, false);
		TextView tvLeft = (TextView) rowView.findViewById(R.id.tvleft);
		tvLeft.setTextSize(UIHelper.getFontSize(context, tvLeft.getTextSize()));
		TextView tvRight = (TextView) rowView.findViewById(R.id.tvright);
		tvRight.setTextSize(UIHelper.getFontSize(context, tvRight.getTextSize()));

		{
			tvLeft.setText(customFields.get(position).getName());
			tvRight.setText(customFields.get(position).getValue());
		} 
		return rowView;
	}
}
