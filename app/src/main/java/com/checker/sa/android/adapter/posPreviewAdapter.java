package com.checker.sa.android.adapter;

import java.util.List;

import com.checker.sa.android.data.POS_Shelf;
import com.checker.sa.android.helper.Helper;
import com.checker.sa.android.helper.POS_Toggles;
import com.checker.sa.android.helper.POS_Toggles.EnumToggleButton;
import com.mor.sa.android.activities.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class posPreviewAdapter extends ArrayAdapter<String> {
	private final Context context;
	private final List<String> values;
	private final List<String> headings;
	POS_Toggles.EnumToggleButton currentToggle;
	POS_Shelf shelf;
	Boolean isPreview;

	public posPreviewAdapter(Context context, List<String> valuess,
			List<String> headingss, POS_Toggles.EnumToggleButton currentToggle,
			POS_Shelf shelfItem, Boolean isPreview) {
		super(context, R.layout.file_name_row, valuess);
		this.isPreview = isPreview;
		this.context = context;
		values = valuess;
		headings = headingss;
		this.currentToggle = currentToggle;
		shelf = shelfItem;
	}

	public void OnDelClick(int pos) {
		if (currentToggle == EnumToggleButton.EXPIRATION) {
			shelf.expiration_item.deleteItemAt(pos);
		}
		if (currentToggle == EnumToggleButton.COUNT) {
			shelf.quantity_item.deleteItemAt(pos);
		}
		if (currentToggle == EnumToggleButton.NOTE) {
			shelf.note_item.deleteItemAt(pos);
		}
		if (currentToggle == EnumToggleButton.PRICE) {
			shelf.price_item.deleteItemAt(pos);
		}
		values.remove(pos);
		headings.remove(pos);

		notifyDataSetChanged();
	}

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
		TextView textView = (TextView) rowView.findViewById(R.id.nameText);
		textView.setText(headings.get(position));

		TextView textValueView = (TextView) rowView
				.findViewById(R.id.valueText);
		textValueView.setText(values.get(position));

		ImageView minusSign = (ImageView) rowView.findViewById(R.id.minusImg);
		minusSign.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				OnDelClick(position);

			}
		});
		if (isPreview)
			minusSign.setVisibility(View.GONE);
		return rowView;
	}
}
