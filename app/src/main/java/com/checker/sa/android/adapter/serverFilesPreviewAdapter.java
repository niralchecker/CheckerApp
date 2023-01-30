package com.checker.sa.android.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.checker.sa.android.data.InProgressFileData;
import com.checker.sa.android.helper.Helper;
import com.checker.sa.android.helper.UIHelper;
import com.mor.sa.android.activities.QuestionnaireActivity;
import com.mor.sa.android.activities.R;
import com.squareup.picasso.Picasso;

public class serverFilesPreviewAdapter extends ArrayAdapter<String> {
	private final QuestionnaireActivity context;
	private final List<InProgressFileData> values;
	private final Dialog dialog;
	String tag = "";
	int tSize = 0;
	private boolean isServerSide;

	public serverFilesPreviewAdapter(QuestionnaireActivity context,
			ArrayList<InProgressFileData> valuess,Dialog dialog) {
		super(context, R.layout.eye_file_name_row);
		this.context = context;
		this.values = valuess;
		isServerSide = false;
		this.dialog=dialog;
	}

	@Override
	public int getCount() {
		tSize = 0;
		for (int i = 0; values != null && i < values.size(); i++) {
			if (values.get(i).isOnAppSide())
				tSize++;
		}
		return values.size();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = null;
		rowView = inflater.inflate(R.layout.eye_file_name_row, parent, false);
		ImageView imgv= (ImageView) rowView.findViewById(R.id.imgv);
		ImageView close=(ImageView) rowView.findViewById(R.id.btnClose);
		close.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(false);
                builder.setTitle(context.getResources().getString(R.string._alert_title));
                TextView textView = new TextView(context);
                textView.setTextSize(UIHelper.getFontSize(context,
                        textView.getTextSize()));
                textView.setText(Helper
                        .makeHtmlString(context.getString(R.string.msg_sure)));
                builder.setView(textView);
                builder.setPositiveButton(
                        context.getString(R.string.questionnaire_exit_delete_alert_yes),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                context.deleteThisFile(values.get(position),serverFilesPreviewAdapter.this.dialog);
                            }
                        })
                        .setNegativeButton(
                                context.getString(R.string.questionnaire_exit_delete_alert_no),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).show();

			}
		});
		TextView txtName = (TextView) rowView.findViewById(R.id.nameText);
		TextView txtheader = (TextView) rowView.findViewById(R.id.header);
		TextView txtVal = (TextView) rowView.findViewById(R.id.valueText);
		TextView txtSize = (TextView) rowView.findViewById(R.id.sizeText);
		String filename = values.get(position).getFileName();

		if (filename != null && filename.contains("/")) {
			//Uri uri=Uri.fromFile(new File(filename));
			if ((values.get(position).isOnAppSide()) && (
			        filename.toLowerCase().endsWith(".jpg")
					|| filename.toLowerCase().endsWith(".jpeg")
			|| filename.toLowerCase().endsWith(".png")))
				Picasso.get().load(new File(filename)).into(imgv);
			else imgv.setVisibility(RelativeLayout.GONE);
			filename = filename.substring(filename.lastIndexOf("/") + 1);
		}
		if (!values.get(position).isOnAppSide()) {
			imgv.setVisibility(RelativeLayout.GONE);
			close.setVisibility(RelativeLayout.GONE);
		}
		//filename = "- " + filename;
		if (position == 0) {
			txtheader.setText(context.getString(R.string.pending_upload));//already_upload
			txtheader.setVisibility(RelativeLayout.VISIBLE);
		}

		txtSize.setText(values.get(position).getSize());
		if (values.get(position).isOnAppSide()) {
			// txtName.setTextColor(Color.BLUE);
			txtName.setText(filename);
			txtVal.setText(context.getString(R.string.local_path) + values.get(position).getFileName());
		} else {

			if (position >= tSize && position < tSize + 1) {
				txtheader.setText(context.getString(R.string.already_upload));
				txtheader.setVisibility(RelativeLayout.VISIBLE);
			}
			SpannableStringBuilder str = new SpannableStringBuilder(""
					+ filename);
			txtName.setText(str);

			txtVal.setText(context.getString(R.string.server_path) + values.get(position).getFileName());
		}
		return rowView;
	}

}
