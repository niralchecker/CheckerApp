package com.checker.sa.android.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.checker.sa.android.data.InProgressFileData;
import com.mor.sa.android.activities.QuestionnaireActivity;
import com.mor.sa.android.activities.R;

import java.util.ArrayList;
import java.util.List;

public class menuAdapter extends ArrayAdapter<String> {
	private final QuestionnaireActivity context;
	private final Menu values;
	private final Dialog dialog;
	String tag = "";
	int tSize = 0;
	private boolean isServerSide;

	public menuAdapter(Activity context,
                      Menu valuess,Dialog dialog) {
		super(context, R.layout.eye_file_name_row);
		this.context = (QuestionnaireActivity) context;
		this.values = valuess;
		isServerSide = false;
		this.dialog=dialog;
	}

	@Override
	public int getCount() {
		return values.size();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = null;
		rowView = inflater.inflate(R.layout.menu_j_item, parent, false);
        LinearLayout mItem = (LinearLayout) rowView.findViewById(R.id.mitem);
		TextView txtName = (TextView) rowView.findViewById(R.id.nameText);
		txtName.setTag( values.getItem(position));
        mItem.setTag( values.getItem(position));
		String filename = values.getItem(position).getTitle().toString();

		txtName.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (v.getTag()==null) return;
				context.OptionsItemSelected((MenuItem) v.getTag());
				if (dialog!=null) dialog.dismiss();

			}
		});

        mItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getTag()==null) return;
                context.OptionsItemSelected((MenuItem) v.getTag());
                if (dialog!=null) dialog.dismiss();

            }
        });
			// txtName.setTextColor(Color.BLUE);
		txtName.setText(filename);
		return rowView;
	}

}
